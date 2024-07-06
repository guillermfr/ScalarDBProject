package com.cytech.marketplace.servlet;

import com.cytech.marketplace.dao.UsersDAO;
import com.cytech.marketplace.entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "signupServlet", value = "/signup-servlet")
public class SignupServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsersDAO usersDAO = new UsersDAO();

        String fullName = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Les mots de passe ne correspondent pas.");
            req.getRequestDispatcher("/WEB-INF/view/signup.jsp").forward(req, resp);
            return;
        }

        if (usersDAO.getUser(email) != null) {
            req.setAttribute("error", "Un compte avec cet email existe déjà.");
            req.getRequestDispatcher("/WEB-INF/view/signup.jsp").forward(req, resp);
            return;
        }

        Users users = new Users();
        users.setName(fullName);
        users.setEmail(email);
        users.setPassword(password);
        users.setAdmin(false);
        users.setLoyaltyPoints(0);

        try {
            usersDAO.addUser(users);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        req.getSession().setAttribute("user", usersDAO.getUser(email));
        resp.sendRedirect(getServletContext().getContextPath() + "/home");
    }
}
