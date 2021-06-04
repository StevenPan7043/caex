package com.pmzhongguo.ex.datalab.service;




public interface IService<T extends Object> {
	
	 Integer save(T pojo);
	
	 Integer update(T pojo) ;
}
