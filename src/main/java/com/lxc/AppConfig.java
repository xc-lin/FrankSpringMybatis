package com.lxc;

import com.springMybatis.FrankMapperScan;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */

@ComponentScan("com.lxc")
@FrankMapperScan("com.lxc.mapper")
public class AppConfig {


    @Bean
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        return factory;
    }
}
