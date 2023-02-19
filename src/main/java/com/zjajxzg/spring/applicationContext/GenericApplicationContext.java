package com.zjajxzg.spring.applicationContext;

import com.zjajxzg.spring.beanDefinition.AnnotateBeanDefinition;
import com.zjajxzg.spring.beanDefinition.BeanDefinitionRegistry;
import com.zjajxzg.spring.beanFactory.DefaultListableBeanFactory;

/**
 * @author xuzhigang
 * @date 2023/2/19 10:16
 **/
public class GenericApplicationContext implements BeanDefinitionRegistry {
    private DefaultListableBeanFactory beanFactory;

    public GenericApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    @Override
    public void registerBeanDefinition(String beanName, AnnotateBeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    public void refresh() {
        // 获取bean工厂
        DefaultListableBeanFactory beanFactory = obtainBeanFactory();
        // 将appConfig路径下的所有bean进行扫描 (userService userService1)
        invokeBeanFactoryPostProcessors(beanFactory);
        // 初始化beanDefinition所代表的单例bean 放到单例bean的容器里（map）
        finishBeanFactoryInitialization(beanFactory);
    }

    private void finishBeanFactoryInitialization(DefaultListableBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }

    private void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory) {
        // 简化类
        beanFactory.doScan();
    }

    private DefaultListableBeanFactory obtainBeanFactory() {
        return this.beanFactory;
    }


    public Object getBean(String beanName) {
        return this.beanFactory.getBean(beanName);
    }

}
