package com.zjajxzg.spring.beanDefinition;

/**
 * @author xuzhigang
 * @date 2023/2/18 15:23
 **/
public class BeanDefinitionReaderUtils {

    public static void registerBeanDefinition(AnnotateBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        String beanName = ((AnnotateGenericBeanDefinition) beanDefinition).getClazz().getSimpleName();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}
