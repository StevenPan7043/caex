package com.pmzhongguo.zzextool.utils;

import com.pmzhongguo.zzextool.exception.BusinessException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Repository
public class DaoUtil
{

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer queryForInt(String sql, Object... args)
    {
        try
        {
            return this.getJdbcTemplate().queryForObject(sql, Integer.class,
                    args);
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    public List queryForList(String sql, Object... args)
    {
        return this.getJdbcTemplate().queryForList(sql, args);
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz, Object... args)
    {
        return this.getJdbcTemplate().queryForList(sql, args, clazz);
    }

    public Integer update(String sql, Object... args)
    {

        String argsStr = "";
        if (null != args)
        {
            for (int i = 0; i < args.length; i++)
            {
                argsStr += args[i] + ",";
            }
        }
        return this.getJdbcTemplate().update(sql, args);
    }

    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args)
    {
        try
        {
            return this.getJdbcTemplate().queryForObject(sql, requiredType,
                    args);
        } catch (DataAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Map queryForMap(String sql, Object... args)
    {
        try
        {
            return this.getJdbcTemplate().queryForMap(sql, args);
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    public void call(String spName, Object... args)
    {
        Assert.notNull(spName, "存储过程名称不能为空!");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++)
        {
            Assert.notNull(args[i], "存储过程的参数不能为空!");
            if (i != 0)
                sb.append(",");
            sb.append("?");
        }
        this.update("{call " + spName + "(" + sb.toString() + ")}", args);
    }

    public void batchInsert(List<String> sqlList) throws BusinessException
    {
        /*
         * final List<T> list= entityList; Field[] array =
         * clazz.getDeclaredFields(); if(list ==null || list.size() ==0 || array
         * == null || array.length == 0) return; String[] sqls = new
         * String[list.size()]; StringBuffer sb =null; for(int i=0;
         * i<list.size(); i++){ sb = new StringBuffer("insert into ");
         * sb.append(tableName); sb.append(" values(null"); for(Field f :
         * array){ f.get }
         *
         * sqls[i] = sb.toString(); }
         */
        if (sqlList == null || sqlList.size() == 0)
        {
            throw new BusinessException(-1, "批处理执行的sql list 为空！");
        }
        String[] sqlArray = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++)
        {
            sqlArray[i] = sqlList.get(i);
        }
        this.getJdbcTemplate().batchUpdate(sqlArray);

    }

    public JdbcTemplate getJdbcTemplate()
    {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
     * public SqlSession getSqlSession() { return sqlSession; }
     *
     * public void setSqlSession(SqlSession sqlSession) { this.sqlSession =
     * sqlSession; }
     */

    public SqlSessionFactory getSqlSessionFactory()
    {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }

}
