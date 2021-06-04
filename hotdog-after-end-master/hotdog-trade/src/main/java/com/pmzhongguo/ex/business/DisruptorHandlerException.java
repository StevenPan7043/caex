package com.pmzhongguo.ex.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;


import com.lmax.disruptor.ExceptionHandler;
import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;

public class DisruptorHandlerException implements ExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(DisruptorHandlerException.class);
	
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lmax.disruptor.ExceptionHandler#handleEventException(java.lang.Throwable
     * , long, java.lang.Object)
     */
    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
//    	Order order = (Order)event;
//    	String exception = ex.toString();
//    	
//    	if (ex instanceof Exception) {
//    		Exception e = (Exception)ex;
//    		
//    		if (e instanceof DataAccessException) {
//    			Throwable root = ((DataAccessException) e).getRootCause();
//    			exception = root != null ? root.getMessage()
//    					: ((DataAccessException) e).getMessage();
//    			String[] msg = exception.split("'");
//    			String reg1 = "Data truncation: Data too long for column '(.*)' at row \\d*";
//    			String reg2 = "Data truncation: Out of range value for column '(.*)' at row \\d*";
//    			String reg3 = "Duplicate entry '(.*)' for key (.*)";
//    			String reg4 = "将截断字符串或二进制数据";
//    			if (exception.matches(reg1) || exception.matches(reg4)) {
//    				exception = msg[1] + " is too long";
//    			} else if (exception.matches(reg2)) {
//    				exception = msg[1] + " is too big";
//    			} else if (exception.matches(reg3)) {
//    				exception = "o_no '" + order.getO_no() + "' already exists";
//    			}
//    		}
//    	}
//    	
//    	JedisUtil.getInstance().set(order.getUuid(), exception, true);
    	ex.printStackTrace();
    	logger.error(ex.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lmax.disruptor.ExceptionHandler#handleOnStartException(java.lang.
     * Throwable)
     */
    @Override
    public void handleOnStartException(Throwable ex) {
    	ex.printStackTrace();
    	logger.error(ex.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lmax.disruptor.ExceptionHandler#handleOnShutdownException(java.lang
     * .Throwable)
     */
    @Override
    public void handleOnShutdownException(Throwable ex) {
    	ex.printStackTrace();
    	logger.error(ex.toString());
    }
}
