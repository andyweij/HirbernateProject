package com.eland.pojo.controller;

import com.eland.pojo.model.UserInformationEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class testMain {

        public static void main(String[] args) {
            SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(UserInformationEntity.class)
                    .buildSessionFactory();

            Session session = factory.getCurrentSession();

            try {
                session.beginTransaction();

                // 創建查詢並讀取全部 Employee 資料
                List<UserInformationEntity> users = session.createQuery("from UserInformationEntity", UserInformationEntity.class)
                        .getResultList();

                for (UserInformationEntity user : users) {
                    System.out.println(user.toString());
                }

                session.getTransaction().commit();
            } finally {
                factory.close();
            }
        }

}
