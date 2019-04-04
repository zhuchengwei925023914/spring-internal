package test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhu on 2019/3/31.
 */
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        classPathXmlApplicationContext.setAllowBeanDefinitionOverriding(true);
        classPathXmlApplicationContext.setAllowCircularReferences(true);
    }
}
