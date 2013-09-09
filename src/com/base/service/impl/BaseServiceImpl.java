package com.base.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.base.dao.impl.BaseDaoImpl;
import com.base.service.BaseService;
import com.util.pager.Pager;

/**
 * 基层Service的实现类，在开发中，每个模块的Service层操作都必须继承这个类
 * @author willenfoo
 * @param <M>
 * @param <E>
 * @param <GeneralDAO>
 */
public abstract class BaseServiceImpl<M, E, GeneralDAO extends BaseDaoImpl<M, E>> implements BaseService<M, E> {

	private GeneralDAO baseDao;

	public void setBaseDao(GeneralDAO baseDao) {
		this.baseDao = baseDao;
	}

	public final int insert(M model) {
		return baseDao.insertSelective(model);
	}

	public final M selectByModel(M model) {
		return baseDao.selectByModel(model);
	}

	public final M selectById(Integer id) {
		return baseDao.selectByPrimaryKey(id);
	}
	
	public final M selectById(String id) {
		return baseDao.selectByPrimaryKey(id);
	}

	public final List<M> selectByExample(E example) {
		return baseDao.selectByExample(example);
	}

	public final List<M> selectByExample(E example, RowBounds row) {
		return baseDao.selectByExample(example, row);
	}

	public final int countByExample(E example) {
		return baseDao.countByExample(example);
	}

	public final int deleteByExample(E example) {
		return baseDao.deleteByExample(example);
	}

	public final int deleteById(Integer id) {
		return baseDao.deleteByPrimaryKey(id);
	}
	
	public final int deleteById(String id) {
		return baseDao.deleteByPrimaryKey(id);
	}

	public final int updateByExample(M model, E example) {
		return baseDao.updateByExampleSelective(model, example);
	}

	public final int updateById(M model) {
		return baseDao.updateByPrimaryKeySelective(model);
	}

	public final List<M> distinct(E example) {
		return baseDao.distinct(example);
	}

	public final String sum(E example) {
		return baseDao.sum(example);
	}

	public final String min(E example) {
		return baseDao.min(example);
	}

	public final String max(E example) {
		return baseDao.max(example);
	}

	public final String avg(E example) {
		return baseDao.avg(example);
	}

	public final M selectByEntity(E example) {
		return baseDao.selectByEntity(example);
	}

	public final Map<String, Object> selectByExampleForMap(E example) {
		return baseDao.selectByExampleForMap(example);
	}
	
	public final List<Map<String, Object>> selectByExampleForListMap(E example) {
		return baseDao.selectByExampleForListMap(example);
	}
	
	public final List<Map<String, Object>> selectByExampleForListMap(E example,RowBounds row) {
		return baseDao.selectByExampleForListMap(example, row);
	}
	
	public final M executeQuery(E example,String methodName) {
		return baseDao.executeQuery(example, methodName);
	}
	
	public final List<M> executeQuery(E example,RowBounds row,String methodName) {
		return baseDao.executeQuery(example,row, methodName);
	}
	
	public final int batchInsert(List<M> list) {
		return baseDao.batchInsert(list);
	}
	
	public final Pager selectByPager(E example,Integer offset,Integer pageSize) {
		List<M> list = this.selectByExample(example, new com.util.mybatis.RowBounds(offset, pageSize));
		Pager pager = new Pager(this.countByExample(example), offset, pageSize);
		pager.setList(list);
		return pager;
	}
}
