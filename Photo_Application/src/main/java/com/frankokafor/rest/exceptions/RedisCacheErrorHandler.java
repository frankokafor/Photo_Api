package com.frankokafor.rest.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import io.lettuce.core.RedisCommandTimeoutException;

public class RedisCacheErrorHandler implements CacheErrorHandler{
	
	private static final Logger log = LoggerFactory.getLogger(RedisCacheErrorHandler.class);

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		handleTimeoutException(exception);
		log.info("Unable to get fromm cache "+cache.getName()+" :"+exception.getMessage());
		
	}

	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
		handleTimeoutException(exception);
		log.info("Unable to put into cache "+cache.getName()+" :"+exception.getMessage());
		
	}

	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
		handleTimeoutException(exception);
		log.info("Unable to evict fromm cache "+cache.getName()+" :"+exception.getMessage());
		
	}

	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		handleTimeoutException(exception);
		log.info("Unable to clear cache "+cache.getName()+" :"+exception.getMessage());
		
	}
	
	private void handleTimeoutException(RuntimeException exception) {
		if(exception instanceof RedisCommandTimeoutException)
			return;
	}

}
