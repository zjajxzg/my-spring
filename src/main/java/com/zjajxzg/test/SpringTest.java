package com.zjajxzg.test;

import com.zjajxzg.spring.applicationContext.AnnotationConfigApplicationContext;
import com.zjajxzg.test.config.AppConfig;

/**
 * @author xuzhigang
 * @date 2023/2/17 21:09
 **/
public class SpringTest {
    public static void main(String[] args) {
        // 创建ApplicationContext（注解形式 不用xml先不学啦）
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        // 调用getBean
        // 多例prototype 获取两次 地址不一样
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
        // 单例singleton 获取两次 地址一致
        System.out.println(applicationContext.getBean("userService1"));
        System.out.println(applicationContext.getBean("userService1"));
    }
}
