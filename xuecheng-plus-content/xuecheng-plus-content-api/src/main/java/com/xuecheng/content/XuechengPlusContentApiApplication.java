package com.xuecheng.content;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = "com.xuecheng")
public class XuechengPlusContentApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(XuechengPlusContentApiApplication.class, args);
	}

}
