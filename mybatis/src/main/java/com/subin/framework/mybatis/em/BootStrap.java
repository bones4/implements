package com.subin.framework.mybatis.em;

import com.subin.framework.mybatis.beans.Test;
import com.subin.framework.mybatis.em.config.EmConfiguration;
import com.subin.framework.mybatis.my.TestMapper;
import com.subin.framework.mybatis.em.executor.ExecutorFactory;
import com.subin.framework.mybatis.em.session.EmSqlSession;

import java.io.IOException;

/**
 * TODO description:
 *
 * @author zhangsubin
 * @since 2018/3/15 下午11:40
 */
public class BootStrap {

    public static void main(String[] args) throws IOException {
        start();
    }

    private static void start() throws IOException {

        /*
         *1.构建配置configuration
         * 读取xml配置文件，扫描mapper路径，
         * 加载map信息到spring的bean集合里
         * 注意是每个方法为单位，保存动态sql和类型
        */
        EmConfiguration configuration = new EmConfiguration();
        configuration.setScanPath("com.subin.framework.mybatis.my");
        configuration.build();


        /*
        * 2. 通过配置构建SqlSessionFactory，
        * 内部对象 Executor：sql执行者，Executor=ExecutorFactory+配置
        *
        * 3. 通过SqlSessionFactory得到SqlSession，
        *  SqlSession=Executor+配置
        * */
        EmSqlSession sqlSession = new EmSqlSession(configuration,
                ExecutorFactory.DEFAULT(configuration));


        /*
        * sqlSession.getMapper方法
        * TestMapper testMapper= TestMapper.class + Proxy.newProxyInstance+  invocationHandler
        * TestMapper代理实例=jdk动态代理类+实例类型+invocationHandler方法实现类EmMapperProxy
        * 并不是每一个xxxMapper都有对应的xxxMapperimpl实现类，
        * 只有统一的接口方法的实现类，传入类型，生成对应的xxxMapper，
        * 表现形式类似泛型，
        * */
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);

        /*
        * 调用，会跳转到invocationHandler实现类EmMapperProxy的invoke
        * 如果从bean仓库里找到方法对应的bean，里面包含sql语句和类型
        *
        * 执行sqlSession.selectOne（bean）
        * 底层实际是执行sqlSession.executor.query(sql语句和类型, parameter);
        * 再底层调的是 StatementHandler，包含三步
        * 1.ParameterHandler处理参数
        * 2.创建Statement并执行
        * 3.ResultSetHandler处理结果
        * 
        * */
        Test test = testMapper.selectByPrimaryKey(1);
        System.out.println(test);
    }
}
