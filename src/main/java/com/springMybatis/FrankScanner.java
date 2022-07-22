package com.springMybatis;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */
public class FrankScanner extends ClassPathBeanDefinitionScanner {
    public FrankScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    /**
     * 重写，只扫描接口
     * @param beanDefinition
     * @return
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

    /**
     * 重写doScan得到扫描到的 BeanDefinitionHolder
     * 并修改BeanDefination成自己的
     * @param basePackages
     * @return
     */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            // 获取到BeanDefinition
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            // 为BeanDefinition 加上构造方法的参数
            try {
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // BeanDefinition修改className等于 修改了整个BeanDefinition，
            // 最后对象的创建就是依靠BeanDefinition的class name，然后通过反射获得对象
            beanDefinition.setBeanClassName(FrankFactoryBean.class.getName());
        }
        return beanDefinitionHolders;
    }
}
