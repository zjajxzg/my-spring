package com.zjajxzg.spring.applicationContext;

import com.zjajxzg.spring.beanDefinition.AnnotatedBeanDefinitionReader;
import com.zjajxzg.spring.beanDefinition.BeanDefinitionRegistry;

/**
 * @author xuzhigang
 * @date 2023/2/18 14:45
 **/
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements BeanDefinitionRegistry {
    private AnnotatedBeanDefinitionReader reader;


    // 如果有人调用无参构造 必须先调用父类的无参构造 GenericApplicationContext 创建beanFactory
    public AnnotationConfigApplicationContext() {
        this.reader = new AnnotatedBeanDefinitionReader(this);
    }

    public AnnotationConfigApplicationContext(Class<?> componentClass) {
        // 1.componentClass 读取扫描路径所在的类 定义一个阅读器，专门读取AnnotatedBeanDefinitionReader
        this();
        // 2.先把AppConfig注册到工厂里（BeanDefinition registerBeanDefinition FactoryBean）
        register(componentClass);
        // 3.扫描路径 然后提取路径下的所有bean 然后注册到bean工厂
        // refresh作为核心方法 需要放到父类中 让所有的子类都能使用
        refresh();
    }

    private void register(Class<?> componentClass) {
        this.reader.register(componentClass);
    }

}
