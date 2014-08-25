package com.util.mybatis.interceptor;

import com.util.mybatis.BaseExample;
import com.util.spring.SpringUtils;
import java.util.Properties;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.RowBounds;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		org.apache.ibatis.session.ResultHandler.class }) })
public class CacheInterceptor implements Interceptor {
	private final Log log = LogFactory.getLog(CacheInterceptor.class);

	public Object intercept(Invocation invocation) throws Throwable {
		log.debug("缓存拦截器执行");
		Object[] args = invocation.getArgs();
		Object object = null;
		if ((args[1] instanceof BaseExample)) {
			MappedStatement ms = (MappedStatement) args[0];
			BoundSql boundSql = ms.getBoundSql(args[1]);
			BaseExample example = (BaseExample) args[1];
			if (example.isCache()) {
				Executor executor = (Executor) invocation.getTarget();
				CacheKey key = executor.createCacheKey(ms, args[1],
						(RowBounds) args[2], boundSql);
				Cache cache = (Cache) SpringUtils.getBean("cache");
				Element element = cache.get(key);
				if (element == null) {
					object = invocation.proceed();
					log.debug("第一次调用方法并缓存其值:" + object);
					cache.put(new Element(key, object));
				} else {
					object = element.getObjectValue();
					log.debug("从缓存中取得的值为：" + object);
				}
				return object;
			}
		}
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}
}