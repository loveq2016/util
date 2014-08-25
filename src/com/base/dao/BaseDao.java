package com.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Dao层基接口，数据访问逻辑.这里只提供基本的操作增、删、查、改和分页的方法
 * @author willenfoo
 *
 * @param <M>
 * @param <E>
 */
public interface BaseDao<M,E>  {

	int countByExample(E example);

	int deleteByExample(E example);

	int deleteByPrimaryKey(Integer id);
	
	int deleteByPrimaryKey(String userId);

	int insert(M record);

	int insertSelective(M record);

	List<M> selectByExample(E example);

	M selectByPrimaryKey(Integer Id);
	
	M selectByPrimaryKey(String Id);
	
	M selectByPrimaryKey(Long Id);

	int updateByExampleSelective(@Param("record") M record,
			@Param("example") E example);

	int updateByExample(@Param("record") M record,
			@Param("example") E example);

	int updateByPrimaryKeySelective(M record);

	int updateByPrimaryKey(M record);

	M selectByModel(M user);

	List<M> selectByExample(E example, RowBounds row);

	List<M> distinct(E example);

	String sum(E example);

	String min(E example);

	String max(E example);

	String avg(E example);

	Map<String, Object> selectByExampleForMap(E example);
	
	List<Map<String, Object>> selectByExampleForListMap(E example);
	
	/**
	 * 查询多条数据，以map返回,支持分页
	 * @param example
	 * @return
	 */
	List<Map<String, Object>> selectByExampleForListMap(E example,RowBounds row);
	
	
	/**
	 * 批量插入数据
	 * @param models
	 * @return
	 */
	int batchInsert(List<M> list);
}
