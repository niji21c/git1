package com.test.lettuce.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.SplittableRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.test.lettuce.vo.AvailablePoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LettuceController {
	
	//private final AvailablePointRedisRepository availablePointRedisRepository;
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate; 

	@GetMapping("/")
    public String ok () {
		System.out.println("1.0.0");
        return "ok";
    }

    @GetMapping("/save")
    public String save(){
		System.out.println("b2");
    		System.out.println("aaa");
    		System.out.println("b1");
    		System.out.println("100");
        String randomId = createId();
        LocalDateTime now = LocalDateTime.now();

        AvailablePoint availablePoint = AvailablePoint.builder()
                .id(randomId)
                .point(1L)
                .refreshTime(now)
                .build();

        log.info(">>>>>>> [save] availablePoint={}", availablePoint);

        //availablePointRedisRepository.save(availablePoint);
        
        redisTemplate.opsForValue().set(randomId, availablePoint);

        return "save";
    }

    
    @GetMapping("/get")
    public Object get() {
		System.out.println("b3");
    		String id = createId();
        return redisTemplate.opsForValue().get(id);
    }
    
    @GetMapping("/get/{id}")
    public Object get( @PathVariable String id ) {
    	
    		for( int i = 0; i<9; i++) {
    			redisTemplate.opsForValue().get(id);
    		}
    	
        return redisTemplate.opsForValue().get(id);
    }

    // 임의의 키를 생성하기 위해 1 ~ 1_000_000_000 사이 랜덤값 생성
    private String createId() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(1, 1_000_000_000));
    }
    
    
    @Autowired
    private ListeningExecutorService workExecutor;
    
	@GetMapping("/gettest/{url}")
    public Object getThreadTest( @PathVariable String url ) {
    	
//		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
//			public String call() throws Exception {
//
//				HttpClient client = HttpClientBuilder.create().build();
//				HttpGet request = new HttpGet("http://1.255.144.83:8080/iptvchlicense/dev/getAPITest");
//
//				RequestConfig requestConfig = RequestConfig.custom()
//				          .setSocketTimeout(5000)
//				          .setConnectTimeout(5000)
//				          .setConnectionRequestTimeout(5000)
//				          .build();
//
//				request.setConfig(requestConfig);
//				
//				HttpResponse response = client.execute(request);
//
//				String statusCode = "";
//				if (response != null) {
//					statusCode = response.getStatusLine().getStatusCode() + "";
//				}
//
//				return statusCode.toString();
//				
//			}
//		});
		
		
//		ListenableFuture<String> listenableFuture = (ListenableFuture<String>) workExecutor.submit(task);
//		
//		Futures.addCallback(listenableFuture, new FutureCallback<String>() {
//
//			
//			public void onFailure(Throwable e) {
//				// TODO 실패 로그 남기기
//				log.info("- onFailure - " + e.getMessage() + " - ");
//			}
//
//			public void onSuccess(String result) {
//				// TODO Auto-generated method stub
//				log.info("- onSuccess - " + result + " - ");
//			}
//		});
		
//		workExecutor.execute(task);
		

 		Callable<String> asyncTask = new Callable<String>() {
 			@Override
 			public String call() throws Exception {
 				
 				HttpClient client = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet("http://1.255.86.45:8080/epg/serviceAction.hm?m=getPSSUserStatus_v1&stb_id=%7BB877C22B-E42F-11E5-A490-FDCFBFF8EC17%7D&mac_addr=28:32:C5:4F:1E:4D&response_type=json");

				RequestConfig requestConfig = RequestConfig.custom()
				          .setSocketTimeout(5000)
				          .setConnectTimeout(5000)
				          .setConnectionRequestTimeout(5000)
				          .build();

				request.setConfig(requestConfig);
				
				HttpResponse response = client.execute(request);

				String statusCode = "";
				BufferedReader rd = null;
				StringBuffer result = new StringBuffer();
				
				if (response != null) {
					statusCode = response.getStatusLine().getStatusCode() + "";
					//statusCode = response.getEntity().getContent().toString();
					
					rd = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));
					
					String line = "";
					//LogUtil.info(reqparam.get("IF"), "", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", "status : " + response.getStatusLine().getStatusCode() + ", milisecond : " + ((end - before) / 1000000));
					
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
				}
				
				Thread.sleep(50);

				return statusCode.toString();
 				
 			}
 		};

 		ListenableFuture<String> future = workExecutor.submit(asyncTask);

 		String result = "ok";
 		
 		try {
 			result = future.get();
 			//System.out.println(result);
 		} catch (ExecutionException e) {
 			e.printStackTrace();
 		} catch (InterruptedException e) {
 			e.printStackTrace();
 		}
    	
    		return result;
    }
    
   
    
    
    

}
