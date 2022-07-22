package com.lxc;

import com.lxc.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService bean = (UserService) applicationContext.getBean("userService");
        bean.test();
    }
}
