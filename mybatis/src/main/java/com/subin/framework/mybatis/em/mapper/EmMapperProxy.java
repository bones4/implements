package com.subin.framework.mybatis.em.mapper;

import com.subin.framework.mybatis.em.config.EmMapperRegistory;
import com.subin.framework.mybatis.em.session.EmSqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * TODO description:
 *
 * @author zhangsubin
 * @since 2018/3/15 下午11:46
 */
public class EmMapperProxy<T> implements InvocationHandler {
    private final EmSqlSession sqlSession;
    private final Class<T> mappperInterface;
    final  String name="EmMapperProxy";
    public  EmMapperProxy(EmSqlSession sqlSession, Class<T> mappperInterface) {
        this.sqlSession = sqlSession;
        this.mappperInterface = mappperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        EmMapperRegistory emMapperRegistory= sqlSession.getEmConfiguration()
                .getMapperRegistory();

        String methodName=method.getDeclaringClass().getName() + "." + method.getName();
        EmMapperRegistory.MapperData mapperData = emMapperRegistory.get(methodName);

        if (mapperData != null) {
            System.out.println(
                    String.format("SQL [ %s ], parameter [%s] ",
                    mapperData.getSql(), args[0])
            );
            return sqlSession.selectOne(mapperData, String.valueOf(args[0]));
        }
        else {

        }
       // return method.invoke(proxy, args);
        return null;
    }
}
