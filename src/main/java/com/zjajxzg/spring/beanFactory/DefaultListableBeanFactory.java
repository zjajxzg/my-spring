package com.zjajxzg.spring.beanFactory;

import com.zjajxzg.spring.annotation.ComponentScan;
import com.zjajxzg.spring.annotation.Scope;
import com.zjajxzg.spring.annotation.Service;
import com.zjajxzg.spring.beanDefinition.AnnotateBeanDefinition;
import com.zjajxzg.spring.beanDefinition.AnnotateGenericBeanDefinition;
import com.zjajxzg.spring.beanDefinition.BeanDefinitionRegistry;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuzhigang
 * @date 2023/2/19 09:54
 **/
public class DefaultListableBeanFactory implements BeanDefinitionRegistry, BeanFactory {
    private final Map<String, AnnotateBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private List<String> beanDefinitionNames = new ArrayList<>();

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);


    @Override
    public void registerBeanDefinition(String beanName, AnnotateBeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }


    public void doScan() {
        for (String beanName : beanDefinitionMap.keySet()) {
            // 拿到appConfig
            AnnotateGenericBeanDefinition bd = (AnnotateGenericBeanDefinition) beanDefinitionMap.get(beanName);
            if (bd.getClazz().isAnnotationPresent(ComponentScan.class)) {
                ComponentScan componentScan = (ComponentScan) bd.getClazz().getAnnotation(ComponentScan.class);
                String scanPackage = componentScan.value();
                URL resource = this.getClass().getClassLoader().getResource(scanPackage.replace(".", "/"));
                File file = new File(resource.getFile());
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        try {
                            Class clazz = this.getClass().getClassLoader()
                                    .loadClass(scanPackage.concat(".").concat(f.getName().split("\\.")[0]));
                            if (clazz.isAnnotationPresent(Service.class)) {
                                String name = ((Service)clazz.getAnnotation(Service.class)).value();
                                AnnotateGenericBeanDefinition abd = new AnnotateGenericBeanDefinition();
                                abd.setClazz(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    abd.setScope(((Scope)clazz.getAnnotation(Scope.class)).value());
                                } else {
                                    abd.setScope("singleton");
                                }
                                beanDefinitionMap.put(name, abd);
                                // 记录真正定义的bean 排除AppConfig
                                beanDefinitionNames.add(name);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }


            }
        }
    }

    public void preInstantiateSingletons() {
        // 初始化自定义的bean
        // 拷贝beanNames副本 避免遍历过程中存在修改操作，
        List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
        beanNames.forEach(beanName -> {
            // beanNames里的东西都是扫描出来的，如果扫描之后又新的通过动态创建的标有单例bean的class加载到JVM
            // 这部分就会被遗漏
            AnnotateGenericBeanDefinition abd = (AnnotateGenericBeanDefinition) beanDefinitionMap.get(beanName);
            if (abd.getScope().equals("singleton")) {
                // 创建单例对象，然后把这个单例对象保存到我们的单例池（内存缓存里面map）
                // getBean方法里就包含了创建对象


                // 为了确保getBean调用时，能够不遗漏应该初始化的单例bean 所以把这部分逻辑放到getBean里
                getBean(beanName);
            }
        });

    }

    /**
     * 只有bean注册以后，才能getBean
     *
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName);
    }

    private Object doGetBean(String beanName) {
        Object bean = singletonObjects.get(beanName);
        if (bean != null) {
            return bean;
        }
        // 需要根据beanDefinition创建bean
        AnnotateGenericBeanDefinition abd = (AnnotateGenericBeanDefinition) beanDefinitionMap.get(beanName);

        Object realBean = createBean(beanName, abd);
        if (abd.getScope().equals("singleton")) {
            // createBean方法是完成了beanDefinition转真正实体对象的地方
            singletonObjects.put(beanName, realBean);
        }
        return realBean;
    }

    private Object createBean(String beanName, AnnotateGenericBeanDefinition abd) {
        try {
            return abd.getClazz().getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
