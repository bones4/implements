package com.subin.framework.mybatis.em.session;

import com.subin.framework.mybatis.em.config.EmConfiguration;
import com.subin.framework.mybatis.em.config.EmMapperRegistory;
import com.subin.framework.mybatis.em.executor.Executor;
import com.subin.framework.mybatis.em.mapper.EmMapperProxy;

import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Proxy;

/**
 * TODO description:
 *
 * @author zhangsubin
 * @since 2018/3/15 下午11:41
 */
public class EmSqlSession {
    private EmConfiguration emConfiguration;
    private Executor executor;

    public EmConfiguration getEmConfiguration() {
        return emConfiguration;
    }

    public EmSqlSession(EmConfiguration emConfiguration, Executor executor) {
        this.emConfiguration = emConfiguration;
        this.executor = executor;
    }


    //Class<T> + Proxy.newProxyInstance+  invocationHandler
    public <T> T getMapper(Class<T> clazz) {
//        newProxyInstance(ClassLoader loader,
//                Class<?>[] interfaces,
//                InvocationHandler h)
        ClassLoader loader= clazz.getClassLoader();
        Class<?>[] interfaces=new Class[]{clazz};
        InvocationHandler invocationHandler= new EmMapperProxy(this,clazz);
        return (T) Proxy.newProxyInstance(
                loader,
                interfaces,
                invocationHandler);
    }

    public <T> T selectOne(EmMapperRegistory.MapperData mapperData, Object parameter) throws Exception {
        return executor.query(mapperData, parameter);
    }
}
