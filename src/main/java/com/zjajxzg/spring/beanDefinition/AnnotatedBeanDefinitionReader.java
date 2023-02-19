package com.zjajxzg.spring.beanDefinition;

import com.zjajxzg.spring.annotation.Scope;

/**
 * @author xuzhigang
 * @date 2023/2/18 14:53
 **/
public class AnnotatedBeanDefinitionReader {

    private BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    // 注册路径扫描bean到bean工厂里 AppConfig
    public void register(Class<?> componentClass) {
        registerBean(componentClass);
    }

    private void registerBean(Class<?> componentClass) {
        doRegisterBean(componentClass);
    }

    private void doRegisterBean(Class<?> componentClass) {
        // 把appConfig读成一个beanDefinition
        AnnotateGenericBeanDefinition beanDefinition = new AnnotateGenericBeanDefinition();
        beanDefinition.setClazz(componentClass);

        if (componentClass.isAnnotationPresent(Scope.class)) {
            String value = componentClass.getAnnotation(Scope.class).value();
            beanDefinition.setScope(value);
        } else {
            beanDefinition.setScope("singleton");
        }

        // beanDefinition 创建完成后 给beanFactory进行bean注册
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinition, this.registry);
    }
}
