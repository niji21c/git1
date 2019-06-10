package com.test.lettuce.config;


import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import lombok.RequiredArgsConstructor;

@EnableRedisRepositories
@Configuration
@RequiredArgsConstructor
public class ExcuteThreadConfig {
		
		//RedisConfiguration
	
		@Bean
		public ListeningExecutorService getListeningExcuter() {
			
			return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1000));
	    }
		
		
}
