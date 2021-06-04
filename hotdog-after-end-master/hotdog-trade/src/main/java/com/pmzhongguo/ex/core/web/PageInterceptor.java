package com.pmzhongguo.ex.core.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmzhongguo.ex.core.utils.HelpUtils;


/**
 * 通过拦截<code>StatementHandler</code>的<code>prepare</code>方法，重写sql语句实现物理分页。
 * 老规矩，签名里要拦截的类型只能是接口。
 * 
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor implements Interceptor {
	private Logger logger = LoggerFactory.getLogger(PageInterceptor.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static String defaultDialect = "mysql"; // 数据库类型(默认为mysql)
	private static String defaultPageSqlId = ".*Page$"; // 需要拦截的ID(正则匹配)

    /**
     * 跳过分页总数
     */
	private static Set<String> jumpPageSqlCount = ImmutableSet.of("com.pmzhongguo.ex.business.mapper.OrderMapper.getOrdersPage");


	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(
				statementHandler, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY);
		// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object,
					DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		}
		// 分离最后一个代理对象的目标类
		while (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object,
					DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		}
		Configuration configuration = (Configuration) metaStatementHandler
				.getValue("delegate.configuration");

		// defaultDialect
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
				.getValue("delegate.mappedStatement");
		// 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
		if (mappedStatement.getId().matches(defaultPageSqlId)) {
			BoundSql boundSql = (BoundSql) metaStatementHandler
					.getValue("delegate.boundSql");
			Map<String, Object> params = (Map) boundSql.getParameterObject();
			if (params == null) {
				throw new NullPointerException("pagination is null!");
			} else if (!"0".equalsIgnoreCase(params.get("needPage") + "")) {
				String sql = boundSql.getSql();

                Connection connection = (Connection) invocation.getArgs()[0];
                // 部分sql跳过这里的获取总页数，先查询总数再重写sql设置排序参数
                if (!jumpPageSqlCount.contains(mappedStatement.getId())) {
                    // 重设分页参数里的总页数等
                    setPageParameter(sql, connection, mappedStatement, boundSql,params);
                }

                if (!HelpUtils.nullOrBlank(params.get("sortname") + "")
						&& !HelpUtils.nullOrBlank(params.get("sortorder") + "")) {
					String[] sortName = (params.get("sortname") + "").split(",");
					String[] sortOrder = (params.get("sortorder") + "").split(",");
					sql = sql + " order by ";
					for (int sortI = 0; sortI < sortName.length; sortI ++) {
						sql = sql + sortName[sortI] + " " + sortOrder[sortI];
						if (sortI < sortName.length - 1) {
							sql = sql + ",";
						}
					}
				}
				// 重写sql
				String pageSql = buildPageSql(sql, params);
				metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
				// 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
				metaStatementHandler.setValue("delegate.rowBounds.offset",
						RowBounds.NO_ROW_OFFSET);
				metaStatementHandler.setValue("delegate.rowBounds.limit",
						RowBounds.NO_ROW_LIMIT);
			}
		}
		// 将执行权交给下一个拦截器
		return invocation.proceed();
	}

	/**
	 * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
	 * <code>PageParameter</code>获得相关信息。
	 *
	 * 这个方法主要是获取总记录数
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param page
	 */
	private void setPageParameter(String sql, Connection connection,
			MappedStatement mappedStatement, BoundSql boundSql,
			Map<String, Object> params) {

        // 总记录数
		String countSql = "select count(*) from (" + sql + ") as total";

		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),
					countSql, boundSql.getParameterMappings(),
					boundSql.getParameterObject());
			setParameters(countStmt, mappedStatement, countBS,
					boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			int totalCount = 0;
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
			params.put("total", totalCount);
			// int totalPage = totalCount / page.getPageSize() + ((totalCount %
			// page.getPageSize() == 0) ? 0 : 1);
			// page.setTotalPage(totalPage);

		} catch (SQLException e) {
			logger.error("Ignore this exception", e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
			try {
				countStmt.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
		}

	}

	/**
	 * 对SQL参数(?)设值
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps,
			MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ParameterHandler parameterHandler = new DefaultParameterHandler(
				mappedStatement, parameterObject, boundSql);
		parameterHandler.setParameters(ps);
	}

	/**
	 * 根据数据库类型，生成特定的分页sql
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String buildPageSql(String sql, Map params) {
		if (params != null) {
			StringBuilder pageSql = new StringBuilder();
			if ("mysql".equals(defaultDialect)) {
				pageSql = buildPageSqlForMysql(sql, params);
			} else {
				return sql;
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	/**
	 * mysql的分页语句
	 * 
	 * @param sql
	 * @param page
	 * @return String
	 */
	public StringBuilder buildPageSqlForMysql(String sql, Map params) {
		StringBuilder pageSql = new StringBuilder(100);
		int pagesize = 100000;
		if (null != params.get("pagesize")) {
			pagesize = Integer.valueOf(params.get("pagesize").toString());
		}
		if (null == params.get("page")) {
			params.put("page", 1);
		}
		String beginrow = String.valueOf((Integer.valueOf(params.get("page")
				.toString()) - 1) * pagesize);
		pageSql.append(sql);
		pageSql.append(" limit " + beginrow + "," + pagesize);
		return pageSql;
	}

	/**
	 * 参考hibernate的实现完成oracle的分页
	 * 
	 * @param sql
	 * @param page
	 * @return String
	 * 
	 *         public StringBuilder buildPageSqlForOracle(String sql,
	 *         PageParameter page) { StringBuilder pageSql = new
	 *         StringBuilder(100); String beginrow =
	 *         String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
	 *         String endrow = String.valueOf(page.getCurrentPage() *
	 *         page.getPageSize());
	 * 
	 *         pageSql.append(
	 *         "select * from ( select temp.*, rownum row_id from ( ");
	 *         pageSql.append(sql); pageSql.append(" ) temp where rownum <=
	 *         ").append(endrow); pageSql.append(") where row_id >
	 *         ").append(beginrow); return pageSql; }
	 */
	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}

}
