package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String userEmail = req.getParameter("user_login");
        String userPW = req.getParameter("user_pw");

        String contextPath = req.getContextPath();

        if (userEmail == null || userPW == null
                || userEmail.isBlank() || userPW.isBlank()) {

            resp.sendRedirect(contextPath + "/login?erro=true");
            return;
        }

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);
        User userToLogin;

        try {
            userToLogin = dao.findByEmail(userEmail);

        } catch (ModelException e) {
            e.printStackTrace();
            resp.sendRedirect(contextPath + "/login?erro=true");
            return;
        }

        if (userToLogin == null) {
            resp.sendRedirect(contextPath + "/login?erro=true");
            return;
        }

        boolean passwordCorrect = PasswordEncryptor.checkPassword(
                userPW,
                userToLogin.getPassword()
        );

        if (passwordCorrect) {
            req.getSession().setAttribute("user_logged", userToLogin);

            resp.sendRedirect(contextPath + "/users");

        } else {
            resp.sendRedirect(contextPath + "/login?erro=true");
        }
    }
}

