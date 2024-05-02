package com.eland.pojo.dao;

import com.eland.pojo.controller.UsersListController;
import com.eland.pojo.model.UserInformationEntity;
import com.eland.pojo.util.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserDataDAO {
    private static Logger logger = LogManager.getLogger(UsersListController.class.getName());
    private static UserDataDAO userDataDAO = new UserDataDAO();

    public static UserDataDAO getInstance() {
        return userDataDAO;
    }

    private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    private Session session;
    private Transaction tx;
    private EntityManagerFactory emf;
    private EntityManager em;

    public UserDataDAO() {
//        this.sessionFactory = HibernateUtils.getSessionFactory();;
//        this.session = sessionFactory.getCurrentSession();
//        this.tx = session.beginTransaction();
//        emf = Persistence.createEntityManagerFactory("UserInformationEntity");
//        em = emf.createEntityManager();
    }


    public List<UserInformationEntity> sortedUser(String queryDate, String sorted, String sortColumn) {

        session = sessionFactory.getCurrentSession();
        tx = session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserInformationEntity> query = criteriaBuilder.createQuery(UserInformationEntity.class);
        Root<UserInformationEntity> root = query.from(UserInformationEntity.class);
        Predicate condition = null;
        if (null != queryDate && "" != queryDate) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            Date queryDateObj = null;
            try {
                queryDateObj = sf.parse(queryDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            condition = criteriaBuilder.greaterThan(root.get("createTime"), queryDateObj);
            query.where(condition);
        }
        if (null == sortColumn || "" == sortColumn) {
            sortColumn = "id";
        }
        List<Order> orderList = new ArrayList<>();
        if (null == sorted || "asc".equals(sorted)) {
            orderList.add(criteriaBuilder.asc(root.get(sortColumn)));
        } else if (sorted.equals("desc")) {
            orderList.add(criteriaBuilder.desc(root.get(sortColumn)));
        }
        query.orderBy(orderList);
        List<UserInformationEntity> users = session.createQuery(query).getResultList();
        tx.commit();
        session.close();
        return users;

    }

    public boolean createUser(String userAccount, String userName, int userAge) {

        session = sessionFactory.getCurrentSession();
        tx = session.beginTransaction();
        boolean createResult = true;
        Date currentDate = new Date();
        UserInformationEntity userInfo = new UserInformationEntity();
        userInfo.setAccount(userAccount);
        userInfo.setName(userName);
        userInfo.setAge(userAge);
        userInfo.setCreateTime(currentDate);
        session.save(userInfo);
        tx.commit();
        session.close();
        return createResult;
    }

    public UserInformationEntity updateResult(UserInformationEntity userInfo) {

        session = sessionFactory.getCurrentSession();
        tx = session.beginTransaction();
        session.update(userInfo);
        tx.commit();
        session.close();
        return userInfo;
    }

    public UserInformationEntity searchUserById(int id) {

        session = sessionFactory.getCurrentSession();
        tx = session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserInformationEntity> query = criteriaBuilder.createQuery(UserInformationEntity.class);
        Root<UserInformationEntity> root = query.from(UserInformationEntity.class);
        Predicate condition = criteriaBuilder.equal(root.get("id"), id);
        query.where(condition);
        UserInformationEntity userById = session.createQuery(query).getSingleResult();
        tx.commit();
        session.close();
        return userById;

    }

    public boolean deleteUser(int userId) {
        boolean deleteResult = false;

        session = sessionFactory.getCurrentSession();
        tx = session.beginTransaction();
        String updateSql = "DELETE UserInformationEntity WHERE id = " + userId;
        Query update = session.createQuery(updateSql);
        int deleteCount = update.executeUpdate();
        if (deleteCount > 0) {
            deleteResult = true;
        }
        tx.commit();
        session.close();
        return deleteResult;
    }

    public List<Query> pagInation(String currentPage, String queryDate) {

        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        Query queryData = null;
        if (queryDate == "" || null == queryDate) {

            queryData = session.createQuery("from UserInformationEntity user order by user.id");
        } else {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            Date queryDateObj = null;
            try {
                queryDateObj = sf.parse(queryDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            queryData = session.createQuery("from UserInformationEntity user WHERE user.createTime > :queryDate order by user.id ");
            queryData.setParameter("queryDate", queryDateObj);
        }
        List<Query> users = queryData.getResultList();
        Iterator iterator = users.iterator();
        while (iterator.hasNext()) {
            UserInformationEntity user = (UserInformationEntity) iterator.next();
            logger.info(user.toString());
            System.out.println(user.getCreateTime());
        }
        tx.commit();
        session.close();
        return users;
    }

}
