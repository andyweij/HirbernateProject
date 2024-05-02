package com.eland.pojo.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateSessionCleanupListener implements ServletContextListener {
    private SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // 初始化 SessionFactory

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // 关闭 SessionFactory
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
