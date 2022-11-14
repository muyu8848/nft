package com;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.zengtengpeng.annotation.EnableCache;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.nft")
@EnableCreateCacheAnnotation
@EnableCache(value = {})
public class MemberApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

}
