package com.base.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.base.dao.BaseDao;

/**
 * 基层DAO的实现类，在开发中，每个模块的DAO层操作都必须继承这个类
 * @author willenfoo
 *
 * @param <M>
 * @param <E>
 */
public abstract class BaseDaoImpl<M, E> extends SqlSessionDaoSupport implements
		BaseDao<M, E> {

	
	public abstract String getNamespace();
	
	public final int countByExample(E example) {
		return getSqlSession().selectOne(getNamespace() + ".countByExample", example);
	}

	public final int deleteByExample(E example) {
		return getSqlSession().delete(getNamespace() + ".deleteByExample", example);
	}

	public final int deleteByPrimaryKey(Integer id) {
		return getSqlSession().delete(getNamespace() + ".deleteByPrimaryKey", id);
	}
	
	public final int deleteByPrimaryKey(String id) {
		return getSqlSession().delete(getNamespace() + ".deleteByPrimaryKey", id);
	}
	
	public final int insert(M record) {
		return getSqlSession().insert(getNamespace() + ".insert", record);
	}

	public final int insertSelective(M record) {
		return getSqlSession().insert(getNamespace() + ".insertSelective", record);
	}

	public final List<M> selectByExample(E example) {
		return getSqlSession().selectList(getNamespace() + ".selectByExample", example);
	}

	public final M selectByPrimaryKey(Integer id) {
		return getSqlSession().selectOne(getNamespace() + ".selectByPrimaryKey", id);
	}
	
	public final M selectByPrimaryKey(String id) {
		return getSqlSession().selectOne(getNamespace() + ".selectByPrimaryKey", id);
	}

	public final int updateByExampleSelective(M record, E example) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("record", record);
		map.put("example", example);
		return getSqlSession().update(getNamespace() + ".updateByExampleSelective",map);
	}

	public final int updateByExample(M record, E example) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("record", record);
		map.put("example", example);
		return getSqlSession().update(getNamespace() + ".updateByExample",map);
	}

	public final int updateByPrimaryKeySelective(M record) {
		return getSqlSession().update(getNamespace() + ".updateByPrimaryKeySelective", record);
	}

	public final int updateByPrimaryKey(M record) {
		return getSqlSession().update(getNamespace() + ".updateByPrimaryKey", record);
	}

	public final M selectByModel(M model) {
		return getSqlSession().selectOne(getNamespace() + ".selectByModel", model);
	}

	public final List<M> selectByExample(E example, RowBounds row) {
		return getSqlSession().selectList(getNamespace() + ".selectByExample", example, row);
	}

	public final List<M> distinct(E example) {
		return getSqlSession().selectList(getNamespace() + ".distinct", example);
	}

	public final String sum(E example) {
		return getSqlSession().selectOne(getNamespace() + ".sum", example);
	}

	public final String min(E example) {
		return getSqlSession().selectOne(getNamespace() + ".min", example);
	}

	public final String max(E example) {
		return getSqlSession().selectOne(getNamespace() + ".max", example);
	}

	public final String avg(E example) {
		return getSqlSession().selectOne(getNamespace() + ".avg", example);
	}

	public final M selectByEntity(E example) {
		List<M> list = selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		} else if (list.size() > 1) {
			throw new RuntimeException("得到数据不止一条,一共有"+list.size()+"条数据!");
		}
		return null;
	}

	public final Map<String, Object> selectByExampleForMap(E example) {
    	List<Map<String, Object>> list = selectByExampleForListMap(example);
    	if (list != null && list.size() == 1) {
    		return list.get(0);
    	} else if (list.size() > 1) {
    		throw new RuntimeException("得到数据不止一条,一共有"+list.size()+"条数据!");
    	}
    	return null;
	}
    
    public final List<Map<String, Object>> selectByExampleForListMap(E example) {
    	return getSqlSession().selectList(getNamespace()+".selectByExampleForMap", example);
    }
    
    public final List<Map<String, Object>> selectByExampleForListMap(E example,RowBounds row) {
    	return getSqlSession().selectList(getNamespace()+".selectByExampleForMap", example, row);
    }
    
    
    public final M executeQuery(E example,String methodName) {
		return  getSqlSession().selectOne(getNamespace()+"."+methodName, example);
	}
	
	public final List<M> executeQuery(E example,RowBounds row,String methodName) {
		return getSqlSession().selectList(getNamespace()+"."+methodName, example, row);
	}
	
	public final int batchInsert(List<M> list) {
		return getSqlSession().insert(getNamespace() + ".batchInsert", list);
	}
}
