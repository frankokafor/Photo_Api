package com.frankokafor.rest.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.frankokafor.rest.exceptions.RedisCacheErrorHandler;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;

@Configuration
@EnableCaching
public class MyRedisCacheConfiguration extends CachingConfigurerSupport implements CachingConfigurer{
	
	@Value("${spring.redis.host:localhost}")
	private String redisHost;
	
	@Value("${spring.redis.port:6379}")
	private int redisPort;
	
	@Value("${spring.redis.timeout:1}")
	private int redisTimeoutInSecs;
	
	@Value("${spring.redis.socket.timeout.secs:1}")
	private int redisSocketTimeoutInSecs;
	
	@Value("${spring.redis.ttl.hours:1}")
	private int redisDataTTL;
	
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		final SocketOptions socketOptions = SocketOptions
											.builder()
											.connectTimeout(Duration.ofSeconds(redisSocketTimeoutInSecs))
											.build();
		
		final ClientOptions clientOptions = ClientOptions
											.builder().socketOptions(socketOptions).build();
		
		LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
														.commandTimeout(Duration.ofSeconds(redisTimeoutInSecs))
														.clientOptions(clientOptions).build();
		
		RedisStandaloneConfiguration serverConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
		
		final LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(serverConfiguration,clientConfiguration);
		connectionFactory.setValidateConnection(true);
		return connectionFactory;
	}
	
	@Bean
	public RedisTemplate<Object,Object> redisTemplate(){
		RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
	
	@Bean
	public RedisCacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
		RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
													.defaultCacheConfig().disableCachingNullValues()
													.entryTtl(Duration.ofHours(redisDataTTL))
													.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
		cacheConfiguration.usePrefix();
		
		RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder
										.fromConnectionFactory(connectionFactory)
										.cacheDefaults(cacheConfiguration).build();
		cacheManager.setTransactionAware(true);
		return cacheManager;
	}
	
	@Override
	public CacheErrorHandler errorHandler() {
		return new RedisCacheErrorHandler();
	}

}
