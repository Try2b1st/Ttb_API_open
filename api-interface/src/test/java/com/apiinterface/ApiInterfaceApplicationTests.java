package com.apiinterface;

import com.client.ApiClient;
import com.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    @Resource
    private ApiClient apiClient;
    @Test
    void contextLoads() {
        String name = apiClient.getNameByGet("gz");
        User user = new User();
        user.setUsername("wgz");
        String username =apiClient.getNameByPost(user);
        System.out.println(name);
        System.out.println(username);
    }

}
