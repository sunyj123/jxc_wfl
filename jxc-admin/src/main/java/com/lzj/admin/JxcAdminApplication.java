package com.lzj.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * 乐字节  踏实教育 用心服务
 *
 * @author 乐字节--老李
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.lzj.admin.mapper")
public class JxcAdminApplication {

    public static void main(String[] args) {

        boolean matches = new BCryptPasswordEncoder().matches("123456", "$2a$10$praeSMC3wY4TeTbEXRLskuRpcShMf0gvwBoGrJTVkKx8mRvVN043K");
        System.out.println(matches);
        SpringApplication.run(JxcAdminApplication.class, args);
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
