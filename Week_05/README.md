### 9.2.(必做)写代码实现Spring Bean的装配，方式越多越好(XML、Annotation都可以),提 交到Github
#### 1.在XML中进行显式配置
```java
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Student {
    private String id;
    private String name;
}

```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
                        http://www.springframework.org/schema/context
                        http://www.springframework.org/schema/context/spring-context-3.2.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="stu1" class="Student">
        <property name="id" value="123"/>
        <property name="name" value="Jack"/>
    </bean>

</beans>
```
#### 2.在Java中进行显式配置
配置好xml里的component-scan
```java
@Configuration
public class BeanConfig {

    @Bean("zhi")
    public Student newStu(){
        Student student = new Student();
        student.setId("11123");
        student.setName("小马快跑");
        return student;
    }
}
```

#### 3.隐式的bean发现机制和自动装配
配置好xml里的component-scan
```java
@Service("hi")
public class SayHiService {
    public void sayHi(){
        System.out.println("hi...");
    }
}

```

### 10.3.（必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter
1.配置约定文件 spring.factories , 让 Spring 启动扫描到 ZhiConfiguration
2.ZhiConfiguration 会根据 @ConditionalOnProperty 的配置, 匹配对应的属性
3.ZhiConfiguration 中使用 @Bean 注解, 将 Student/Klass/School 配置为 Spring Bean

### 10.6. （必做）研究一下JDBC接口和数据库连接池，掌握它们的设计和用法： 1）使用JDBC原生接口，实现数据库的增删改查操作。 2）使用事务，PrepareStatement方式，批处理方式，改进上述操作。 3）配置Hikari连接池，改进上述操作。提交代码到Github。
时间不够，下次做