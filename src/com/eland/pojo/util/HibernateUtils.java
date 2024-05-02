package com.eland.pojo.util;

import com.eland.pojo.model.UserInformationEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static final SessionFactory sessionFactory = buildSessionFactory();
//	private static final SessionFactory sessionFactory = propBuildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        // configure()讀取hibernate.cfg.xml
        Configuration config = new Configuration().configure();
        // 此方法可透過映射配置在資料庫建立關聯表格，並將建立表格的指令(DDL)輸出到console
        // 也可使用設置(hibernate.cfg.xml)
        // <property name="hibernate.hbm2ddl.auto">create</property>
//		SchemaExport schemaExport = new SchemaExport(config);
//		schemaExport.create(true, true);
        SessionFactory sessionFactory = config.buildSessionFactory();

        return sessionFactory;
    }

    private static SessionFactory propBuildSessionFactory() {
        // 拿掉configure()讀取 hibernate.properties
        Configuration config = new Configuration();
        // 將會自動對應 Message.hbm.xml 映射文件
        config.addClass(UserInformationEntity.class);
        SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory;
    }

    public static void main(String[] args) {
        demoOpenSession();
        demoGetCurrentSession();
    }

    private static void demoOpenSession() {
        // SessionFactory.openSession
        // ‧ 此方法無論如何會建立一個新的Session物件
        // ‧ 必須手動去關閉Session
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        tx.commit();
        System.out.println("Session isOpen:" + session.isOpen());
        session.close();
        System.out.println("Session isOpen:" + session.isOpen());
    }

    private static void demoGetCurrentSession() {
        // SessionFactory.getCurrentSession
        // ‧ 同一個執行續(thread)在交易尚未結束前，多次執行此方法,都會取得相同的Session物件
        // ‧ 當Transaction commit 或 rollback 時，Session 也同時會關閉
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();

        tx.commit();
        // Transaction commit 後 session將會自動關閉(hibernate.cfg.xml須搭配以下參數設置)
        // <property name="current_session_context_class">thread</property>
        System.out.println("Session isOpen:" + session.isOpen());
    }
}
