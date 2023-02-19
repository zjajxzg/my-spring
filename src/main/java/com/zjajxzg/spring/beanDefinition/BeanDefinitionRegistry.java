package com.zjajxzg.spring.beanDefinition;

/**
 * @author xuzhigang
 * @date 2023/2/18 15:22
 **/
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, AnnotateBeanDefinition beanDefinition);
}
