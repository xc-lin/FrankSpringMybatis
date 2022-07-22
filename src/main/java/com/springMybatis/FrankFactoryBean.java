package com.springMybatis;

import com.lxc.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */
public class FrankFactoryBean implements FactoryBean {
    private SqlSession sqlSession;
    // UserMapper,OrderMapper
    private Class mapperClass;

    /**
     *  传入需要代理的mapper接口的class，然后在getObject方法中让mybatis生成对应的mapper接口的代理对象
      */
    public FrankFactoryBean(Class mapperClass) {
        this.mapperClass = mapperClass;
    }

    /**
     * 从ioc容器中拿到factory 因为在AppConfig中直接Import的所以可以用到ioc容器，即使这个类不在包扫描范围内
     * @param factory
     */
    @Autowired
    public void setSqlSession(SqlSessionFactory factory) {
        factory.getConfiguration().addMapper(mapperClass);
        this.sqlSession = factory.openSession();
    }

    /**
     * mybatis生成的代理对象
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() throws Exception {
        // 让mybatis生成对应的mapper接口的代理对象
        Object mapper = sqlSession.getMapper(mapperClass);
        return mapper;
    }

    @Override
    public Class<?> getObjectType() {
        return mapperClass;
    }
}
