package com.procode.task.config;


import com.procode.task.model.entity.FileInfoEntity;
import com.procode.task.model.entity.RoleEntity;
import com.procode.task.model.entity.UserEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final ApplicationContext context;

    public HibernateConfig(ApplicationContext context) {
        this.context = context;
    }


    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setConfigLocation(context.getResource("classpath:hibernate.cfg.xml")); // load hibernate config from that file
        factoryBean.setAnnotatedClasses(UserEntity.class, RoleEntity.class, FileInfoEntity.class);
        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        final HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }
}