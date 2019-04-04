package beanDefinitionRegistryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Created by zhu on 2019/4/3.
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String s : beanFactory.getBeanDefinitionNames()) {
            System.out.println("postProcessBeanFactory: " + s);
        }
    }
}
