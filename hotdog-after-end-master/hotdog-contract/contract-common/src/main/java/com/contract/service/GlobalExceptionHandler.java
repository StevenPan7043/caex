package com.contract.service;

import java.io.IOException;
import java.util.InputMismatchException;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.contract.exception.ThrowJsonException;
import com.contract.exception.ThrowPageException;

import common.Logger;

/**
 * 全局异常处理
 * @author arno
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static Logger logger =Logger.getLogger(GlobalExceptionHandler.class);

	
	@ExceptionHandler(value =  IllegalArgumentException.class)
    @ResponseBody
    public RestResponse paramsError(HttpServletRequest req, Exception e) {
		logger.info("IllegalArgumentException异常"+e.getMessage());
		return GetRest.getFail("请求参数错误");
    }
	
	@ExceptionHandler(value =  ClassNotFoundException.class)
    @ResponseBody
    public RestResponse classError(HttpServletRequest req,Exception e) {
		logger.info("ClassNotFoundException异常"+e.getMessage());
		return GetRest.getFail("档案遗失错误");
    }
	
	@ExceptionHandler(value =  ArrayIndexOutOfBoundsException.class)
    @ResponseBody
    public RestResponse arrayError(HttpServletRequest req,Exception e) {
		logger.info("ArrayIndexOutOfBoundsException异常"+e.getMessage());
		return GetRest.getFail("数组溢出错误");
    }
	
	@ExceptionHandler(value =  InputMismatchException.class)
    @ResponseBody
    public RestResponse inputError(HttpServletRequest req,Exception e) {
		logger.info("InputMismatchException异常"+e.getMessage());
		return GetRest.getFail("接收数据类型错误");
    }
	
	@ExceptionHandler(value = {MethodArgumentTypeMismatchException.class,NumberFormatException.class} )
    @ResponseBody
    public RestResponse numberError(HttpServletRequest req,Exception e) {
		logger.info("MethodArgumentTypeMismatchException,NumberFormatException异常"+e.getMessage());
		return GetRest.getFail("数据格式化异常");
    }
	
	@ExceptionHandler(value = { IOException.class })  
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
    public RestResponse exception(Exception exception) {  
		logger.info("IOException异常"+exception.getMessage());
		 return GetRest.getFail("流处理异常");
    }  
	
	@ExceptionHandler(value =  Exception.class)
    @ResponseBody
    public RestResponse errorResponse(HttpServletRequest req,Exception e) {
		logger.info("Exception异常："+e.getMessage());
		return GetRest.getFail("请求错误");
    }
	
	
	/**
     * 处理所有业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(ThrowJsonException.class)
    @ResponseBody
    public RestResponse handleBusinessException(ThrowJsonException e){
    		return GetRest.getFail(e.getMessage());
    }
    
    /**
     * 处理所有业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(ThrowPageException.class)
    @ResponseBody
    public ModelAndView showPageException(ThrowPageException e){
    		ModelAndView view=new ModelAndView("exception");
    		return view;
    }
   
}
