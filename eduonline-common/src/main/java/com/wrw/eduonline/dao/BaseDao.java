package com.wrw.eduonline.dao;

import java.util.List;
import java.util.Map;

/**
 * @author wrw
 * dao基础类
 */
public abstract interface BaseDao<T> {
	
	/**
	 * 通过Id删除
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);

    int insert(T clazz);

    int insertSelective(T clazz);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T clazz);

    int updateByPrimaryKey(T clazz);
    
    void save(T t);
    
    void save(Map<String, Object> map);
    
    void saveBatch(List<T> list);
    
    int update(T clazz);
    
    int update(Map<String, Object> map);
    
    int delete(Object id);
    
    int delete(Map<String, Object> map);
    
    int deleteBatch(Object[] id);
    
    T queryObject(Object id);
    
    List<T> queryList(Map<String, Object> map);
    
    List<T> queryList(Object id);
    
    int queryTotal(Map<String, Object> map);
    
    int queryTotal();
}
