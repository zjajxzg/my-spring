### 1.想要书写简易Spring代码，首先要搞明白BeanFactory, BeanDefinition, ApplicationContext之间的关系

- BeanFactory: 工厂，生成bean，提供获取bean的方法getBean
     生产bean的话，是不是要解析注解@Service 一个bean可能是单例的，也可能是多例的
- BeanDefinition: bean定义 String scope(singleton, prototype), Class clazz 代表当前bean 属于哪个class
- ApplicationContext: 容器（上下文）主导BeanDefinition的生成 把BeanDefinition **传递** （beanDefinitionRegister）给BeanFactory生成bean

### 2.开始自己仿写源码