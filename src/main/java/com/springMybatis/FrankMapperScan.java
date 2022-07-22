package com.springMybatis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FrankImportBeanDefinitionRegister.class)
public @interface FrankMapperScan {
    String value() default "";
}
