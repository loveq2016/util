package com.base.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.util.pager.Pager;

/**
 * Service层基接口，这里只提供基本的操作增、删、查、改和分页的方法
 * 
 * @author willenfoo
 * 
 * @param <M>
 *            实体
 * @param <E>
 *            实体的工具类
 */
public interface BaseService<M, E> {

	/**
	 * 动态插入model
	 * 
	 * @param model
	 * @return
	 */
	int insert(M model);

	/**
	 * 动态查询model，把要查询的条件放入到model中
	 * 
	 * @param model
	 * @return
	 */
	M selectByModel(M model);

	/**
	 * 动态查询model,把要查询的条件放入到example中
	 * 
	 * @param example
	 * @return
	 */
	M selectByExampleOne(E example);

	/**
	 * 通过ID查询model
	 * @param id
	 * @return
	 */
	M selectById(Long id);
	
	/**
	 * 通过ID查询model
	 * @param id
	 * @return
	 */
	M selectById(Integer id);
	
	/**
	 * 通过ID查询model
	 * @param id
	 * @return
	 */
	M selectById(String id);

	/**
	 * 动态查询数据，返回list，把要查询的条件放入到example中
	 * 
	 * @param example
	 * @return
	 */
	List<M> selectByExample(E example);

	/**
	 * 动态查询数据，返回list，把要查询的条件放入到example中，支持分页
	 * 
	 * @param example
	 * @return
	 */
	List<M> selectByExample(E example,Integer offset,Integer pageSize);
	
	/**
	 * 分页查询，返回pager对象
	 * @param example
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	Pager selectByExampleForPager(E example,Integer offset,Integer pageSize);

	/**
	 * 统计总行数
	 * 
	 * @param example
	 * @return
	 */
	int countByExample(E example);

	/**
	 * 动态删除数据，通过example工具类
	 * 
	 * @param example
	 * @return
	 */
	int deleteByExample(E example);

	/**
	 * 通过ID删除model
	 * @param id
	 * @return
	 */
	int deleteById(Long id);
	
	/**
	 * 通过ID删除model
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);
	
	/**
	 * 通过ID删除model
	 * @param id
	 * @return
	 */
	int deleteById(String id);

	/**
	 * 动态更新数据，
	 * 
	 * @param model
	 * @param example
	 * @return
	 */
	int updateByExample(M model, E example);

	/**
	 * 通过id更新数据
	 * 
	 * @param model
	 * @return
	 */
	int updateById(M model);

	/**
	 * 去除重复行
	 * 
	 * @param example
	 * @return
	 */
	List<M> distinct(E example);

	/**
	 * 得到数据相加值
	 * 
	 * @param example
	 * @return
	 */
	String sum(E example);

	/**
	 * 得到最小值
	 * 
	 * @param example
	 * @return
	 */
	String min(E example);

	/**
	 * 得到最大值
	 * 
	 * @param example
	 * @return
	 */
	String max(E example);

	/**
	 * 得到平均值
	 * 
	 * @param example
	 * @return
	 */
	String avg(E example);

	/**
	 * 查询单条数据，以map返回
	 * @param example
	 * @return
	 */
	Map<String, Object> selectByExampleForMap(E example);

	/**
	 * 查询多条数据，以map返回
	 * @param example
	 * @return
	 */
	List<Map<String, Object>> selectByExampleForListMap(E example);
	
	/**
	 * 查询多条数据，以map返回,支持分页
	 * @param example
	 * @return
	 */
	List<Map<String, Object>> selectByExampleForListMap(E example,Integer offset,Integer pageSize);
	
	
	/**
	 * 批量插入数据
	 * @param models
	 * @return
	 */
	int batchInsert(List<M> list);
	
}