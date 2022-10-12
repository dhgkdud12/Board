package spring.board.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/*
MyBatis configuration file - MyBatis의 작업 설정을 설명하는 XML 파일
데이터베이스의 연결 대상, 매핑 파일의 경로, MyBatis의 작업 설정 등과 같은 세부사항 설명하는 파일
*/

// SqlSessionFactoryBuilder - MyBatis 구성 파일을 읽고 생성하는 SqlSessionFactory 구성 요소
// SqlSessionFactory - SqlSession을 생성하는 구성 요소
// SqlSession - SQL 실행 및 트랜잭션 제어를 위한 API를 제공하는 구성 요소

@Configuration
@MapperScan("spring.board.dao.MyBatis")
public class MySQLConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

//        Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
//        sessionFactory.setConfigLocation(myBatisConfig);

        return sessionFactory.getObject();
    }
}