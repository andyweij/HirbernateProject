package com.eland.pojo.service;

import com.eland.pojo.controller.UsersListController;
import com.eland.pojo.dao.UserDataDAO;
import com.eland.pojo.model.UserInformationEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserBackEndService {
    private static Logger logger = LogManager.getLogger(UsersListController.class.getName());
    private static UserDataDAO userDataDAO = UserDataDAO.getInstance();
    private static UserBackEndService userBackEndService = new UserBackEndService();

    public static UserBackEndService getInstance() {
        return userBackEndService;
    }

    public UserInformationEntity updateService(UserInformationEntity updateUserById, String userAccount, String userName, String age) {
        int userAge = 0;
        if ("" != age && null != age) {
            userAge = Integer.parseInt(age);
        }
        UserInformationEntity userInfo = updateUserById;
        if (!userInfo.getAccount().equals(userAccount) && "" != userAccount) {
            userInfo.setAccount(userAccount);
        }
        if (userInfo.getAge() != (userAge) && 0 != userAge) {
            userInfo.setAge(userAge);
        }
        if (!userInfo.getName().equals(userName) && "" != userName) {
            userInfo.setName(userName);
        }

        return userDataDAO.updateResult(userInfo);
    }

    public UserInformationEntity queryUserById(String id) {
        int userId = 0;
        if (null != id && "" != id) {
            userId = Integer.parseInt(id);
        }
        return userDataDAO.searchUserById(userId);
    }
}
