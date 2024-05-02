package com.eland.pojo.controller;


import com.eland.pojo.dao.UserDataDAO;
import com.eland.pojo.model.UserInformationEntity;
import com.eland.pojo.service.UserBackEndService;
import com.eland.pojo.util.HibernateUtils;
import com.eland.pojo.vo.GenericPageable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import net.sf.json.JSONObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

@MultipartConfig
public class UsersListController extends HttpServlet {

    private UserDataDAO userDataDAO = UserDataDAO.getInstance();
    private UserBackEndService userBackEndService = UserBackEndService.getInstance();
    private static Logger logger = LogManager.getLogger(UsersListController.class.getName());
    private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGetAndPost(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGetAndPost(req, resp);
    }

    protected void doGetAndPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 解决 POST请求中文亂碼問題
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        logger.debug(action);
        switch (action) {
            case "createUser":
                // 使用者新增
                createUser(req, resp);
                break;
            case "updateUser":
                // 帳戶新增
                updateUser(req, resp);
                break;
            case "deleteUser":
                // 帳戶刪除
                deleteUser(req, resp);
                break;
            case "sortUser":
                sortUser(req, resp);
                break;
        }
    }

    public void sortUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        GenericPageable genericPageable = new GenericPageable();//        分頁物件
        genericPageable.setPagesIconSize(5);//        最大顯示分頁數量
        String curPage = req.getParameter("curPage");
        String sortColumn = req.getParameter("column");
        String sorted = req.getParameter("sorted");
        String date = req.getParameter("queryDate");//查詢日期
        List<UserInformationEntity> usersList = userDataDAO.sortedUser(date, sorted, sortColumn);//資料庫user總數
        genericPageable.setTotalItems(usersList.size());
        genericPageable.setEndPage(genericPageable.totalPages(genericPageable, genericPageable.getTotalItems()));//實際分頁數量
        if (curPage == "0" || Integer.parseInt(curPage) > genericPageable.getEndPage()) {
            genericPageable.setCurrentPageNo(1);
        } else {
            genericPageable.setCurrentPageNo(Integer.parseInt(curPage));//當前頁次
        }
        genericPageable.setRowNum(genericPageable.rowNum(genericPageable));
        genericPageable.setPagination(genericPageable.pagination(genericPageable, genericPageable.getEndPage()));//分頁列表
        List<UserInformationEntity> paginationUsers = usersList.subList(genericPageable.getRowNum().get(0), genericPageable.getRowNum().get(1));//帶入分頁列表
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", paginationUsers);
        jsonObject.put("pagination", genericPageable);
        out.println(jsonObject.toString());
    }

    public void createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userAccount = req.getParameter("userAccount");
        String userName = req.getParameter("userName");
        int userAge = Integer.parseInt(req.getParameter("userAge"));
        logger.debug(userAccount + "/" + userName + "/" + userAge);
        boolean createResult = userDataDAO.createUser(userAccount, userName, userAge);
        String createMsg = createResult ? "新增資料成功" : "新增資料失敗";
        logger.debug(createMsg);
        HttpSession session = req.getSession();
        session.setAttribute("createMsg", createMsg);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.println();
        out.flush();
        out.close();

    }

    public void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("userId");
        UserInformationEntity updateUserById = userBackEndService.queryUserById(id);
        String updateAccount = req.getParameter("userAccount");
        String updateName = req.getParameter("userName");
        String updateAge = req.getParameter("userAge");
        UserInformationEntity updateUser = userBackEndService.updateService(updateUserById, updateAccount, updateName, updateAge);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.println(JSONObject.fromObject(updateUser));
        out.flush();
        out.close();

    }

    public void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int deleteId = Integer.parseInt(req.getParameter("userId"));
        boolean deleteResult = userDataDAO.deleteUser(deleteId);
        String Result = deleteResult ? "刪除成功" : "刪除失敗";
        logger.info(Result);
        String Path = "BackEndUserData";
        resp.sendRedirect(Path);
    }

    @Override
    public void destroy() {
        super.destroy();

        // 关闭 Hibernate 连接池
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
