package com.contract.cms.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.contract.cms.common.MappingUtils;
import com.contract.exception.ThrowJsonException;
import com.contract.exception.ThrowPageException;
import com.contract.service.cms.PlatSession;

public class LoginInterceptor extends HandlerInterceptorAdapter{


	
	/**  
     * 在业务处理器处理请求之前被调用  
     * 如果返回false  
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     * 如果返回true  s
     *    执行下一个拦截器,直到所有的拦截器都执行完毕  
     *    再执行被拦截的Controller  
     *    然后进入拦截器链,  
     *    从最后一个拦截器往回执行所有的postHandle()  
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()  
     */    
    @Override    
    public boolean preHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler) throws Exception {  
	    	Integer userid=PlatSession.getUserId(request);
	    	String type = request.getHeader("X-Requested-With");
	    	if(userid==null){
	    		 if ("XMLHttpRequest".equalsIgnoreCase(type)){
	        		 response.setHeader("sessionstatus", "timeout"); 
			 }else{
				 response.sendRedirect(MappingUtils.login);
			 }
	    		 return false;
	    	}else {
	    		//获取请求路径
	    		String returnUrl = String.valueOf(request.getServletPath());
	    		List<String> strs=PlatSession.getMenuUrlList(request);
	    		strs.add(MappingUtils.showIndex);
	    		strs.add(MappingUtils.showWelcome);
	    		strs.add(MappingUtils.editRole);
	    		strs.add(MappingUtils.editParam);
	    		strs.add(MappingUtils.editUser);
	    		strs.add(MappingUtils.editPassword);
	    		strs.add(MappingUtils.getCustomer);
	    		strs.add(MappingUtils.handleMoney);
	    		strs.add(MappingUtils.editBanner);
	    		strs.add(MappingUtils.editNew);
	    		strs.add(MappingUtils.uploadFile);
	    		strs.add(MappingUtils.upload_RichFile);
	    		strs.add(MappingUtils.editBanner);
	    		strs.add(MappingUtils.handleMoney);
	    		strs.add(MappingUtils.editcus);
	    		strs.add(MappingUtils.handUsdtcheck);
	    		strs.add(MappingUtils.editCoin);
	    		strs.add(MappingUtils.handleSaleman);
	    		strs.add(MappingUtils.showAuth);
	    		strs.add(MappingUtils.showEditCustomer);
	    		boolean bool = strs.contains(returnUrl);
	    		if ("XMLHttpRequest".equalsIgnoreCase(type)){
	    			 //如果是ajax请求
	    			 if(!bool) {
	    				 throw new ThrowJsonException("您还没有此功能权限不能操作");
	    			 }
	    		}else {
	    			 if(!bool) {
	    				 throw new ThrowPageException("您还没有此功能权限不能操作");
	    			 }
	    		}
	    	}
        return true;
    }    
    /** 
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作    
     * 可在modelAndView中加入数据，比如当前时间 
     */  
    @Override    
    public void postHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler,    
            ModelAndView modelAndView) throws Exception {     
    }    
    
    /**  
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等   
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()  
     */    
    @Override    
    public void afterCompletion(HttpServletRequest request,    
            HttpServletResponse response, Object handler, Exception ex)    
            throws Exception {    
    }
}
