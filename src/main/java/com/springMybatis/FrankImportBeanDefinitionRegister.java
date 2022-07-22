package com.springMybatis;

import com.lxc.mapper.OrderMapper;
import com.lxc.mapper.UserMapper;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */

/**
 * 添加beanDefinition 到spring容器中
 */
public class FrankImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata 导入这个类上的所有注解信息
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 得到mapper的扫描路径
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(FrankMapperScan.class.getName());
        String path = (String) annotationAttributes.get("value");

        // 得到继承过来的 spring扫描器
        FrankScanner frankScanner = new FrankScanner(registry);
        frankScanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return true;
            }
        });
        frankScanner.scan(path);
    }
}
