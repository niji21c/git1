package com.test.lettuce.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;
import redis.clients.jedis.JedisPoolConfig;

@EnableRedisRepositories
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
		
		//RedisConfiguration
		@Bean
	    public RedisConnectionFactory redisConnectionFactory() {
	        //return new JedisConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
	        // public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, JedisPoolConfig poolConfig) {
			//JedisConnectionFactory(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) 
			
			
	        //RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
	        //JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
	        //jedisConnectionFactory.setPoolConfig(jedisPoolConfig());
	        
	        //RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("1.255.86.45", 6379);
	        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
	        
	        JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
	        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(5));// 5s connection timeout
	        jedisClientConfiguration.readTimeout(Duration.ofMillis(100));// 100ms connection timeout
	        jedisClientConfiguration.usePooling().poolConfig(jedisPoolConfig());
	        
	//	        JedisClientConfiguration configuration = JedisClientConfiguration.builder().useSsl() //
	//					.hostnameVerifier(MyHostnameVerifier.INSTANCE) //
	//					.sslParameters(sslParameters) //
	//					.sslSocketFactory(socketFactory).and() //
	//					.clientName("my-client") //
	//					.connectTimeout(Duration.ofMinutes(10)) //
	//					.readTimeout(Duration.ofHours(5)) //
	//					.usePooling().poolConfig(jedisPoolConfig()) //
	//					.build();
	//	        JedisConnectionFactory(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) {
	        
	        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config, jedisClientConfiguration.build());
	        
	        return jedisConnectionFactory;
	    }
		
		@Bean
	    public RedisConnectionFactory lettuceConnectionFactory() {
			
			//RedisStandaloneConfiguration cconfig = new RedisStandaloneConfiguration("1.255.86.45", 6379);
			RedisStandaloneConfiguration cconfig = new RedisStandaloneConfiguration("localhost", 6379);
			
			GenericObjectPoolConfig poolconfig = new GenericObjectPoolConfig();
			
			
			poolconfig.setMaxTotal(600);
			poolconfig.setMaxIdle(60);
			poolconfig.setMinIdle(10);
			poolconfig.setTestOnBorrow(false);
			poolconfig.setTestOnReturn(false);
			poolconfig.setTestWhileIdle(true);
			poolconfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
			poolconfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
			poolconfig.setNumTestsPerEvictionRun(3);
			poolconfig.setBlockWhenExhausted(true);

			LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
														.poolConfig(poolconfig)
														.commandTimeout(Duration.ofSeconds(1))
														.build();
					
			//LettuceConnectionFactory lcf = new LettuceConnectionFactory(clientConfiguration);
			
	        return new LettuceConnectionFactory(cconfig, clientConfiguration);
	    }
		
		
		private JedisPoolConfig jedisPoolConfig() {
	        final JedisPoolConfig poolConfig = new JedisPoolConfig();
	        
	        poolConfig.setMaxTotal(600);
	        poolConfig.setMaxIdle(600);
	        poolConfig.setMinIdle(10);
	        poolConfig.setTestOnBorrow(false);
	        poolConfig.setTestOnReturn(false);
	        poolConfig.setTestWhileIdle(true);
	        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
	        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
	        poolConfig.setNumTestsPerEvictionRun(3);
	        poolConfig.setBlockWhenExhausted(true);
	        return poolConfig;
	    }

	    @Bean
	    public RedisTemplate<?, ?> redisTemplate() {
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	        
	        //jedis connection
	        redisTemplate.setConnectionFactory(redisConnectionFactory());
	        
	        //lettuce connection
	        //redisTemplate.setConnectionFactory(lettuceConnectionFactory());
	        redisTemplate.setKeySerializer(getRdsString());
	        redisTemplate.setHashKeySerializer(getRdsString());
	        
	        return redisTemplate;
	    }
	    
	    public StringRedisSerializer getRdsString() {
	    		return new StringRedisSerializer();
	    }

}
