package com.apigateway;

import com.yupi.project.provide.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 下水道的小老鼠
 */
@SpringBootApplication
@EnableDubbo
public class ApiGatewayApplication {

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
        ApiGatewayApplication apiGatewayApplication= context.getBean(ApiGatewayApplication.class);
        String result = apiGatewayApplication.doSayHello("world");
        System.out.println(result);
    }

    public String doSayHello(String name){
        return demoService.sayHello(name);
    }
}