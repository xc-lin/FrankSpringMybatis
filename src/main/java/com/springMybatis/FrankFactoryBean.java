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


    public FrankFactoryBean(Class mapperClass) {
        this.mapperClass = mapperClass;
    }

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

        Object mapper = sqlSession.getMapper(mapperClass);
        return mapper;
    }

    @Override
    public Class<?> getObjectType() {
        return mapperClass;
    }
}
