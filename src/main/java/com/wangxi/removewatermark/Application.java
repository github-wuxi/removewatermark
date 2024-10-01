package com.wangxi.removewatermark;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 应用程序
 *
 * @author wuxi
 * @date 2024/06/12
 */
@SpringBootApplication
@MapperScan("com.wangxi.removewatermark.common.dal.daointerface")
public class Application {
    /**
     * 实例http请求工具方法
     *
     * @return {@link RestTemplate}
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 实例dao层处理拦截器
     *
     * @return {@link BeanNameAutoProxyCreator}
     */
    @Bean
    public BeanNameAutoProxyCreator getDaoAutoProxyCreator() {
        BeanNameAutoProxyCreator daoAutoProxyCreator = new BeanNameAutoProxyCreator();
        daoAutoProxyCreator.setBeanNames("*DAO");
        daoAutoProxyCreator.setInterceptorNames("dalMonitorInterceptor");
        return daoAutoProxyCreator;
    }

    /**
     * 主方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
