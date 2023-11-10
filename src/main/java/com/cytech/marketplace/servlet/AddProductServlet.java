package com.cytech.marketplace.servlet;

import com.cytech.marketplace.dao.ArticlesDAO;
import com.cytech.marketplace.dao.UsersDAO;
import com.cytech.marketplace.entity.Articles;
import com.cytech.marketplace.tools.CheckIntFloat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@WebServlet(name = "addProductServlet", value = "/addProduct-servlet")
public class AddProductServlet extends HttpServlet {

    private boolean checkEmpty(String nom, String prix, String stock, String image) {
        if((nom == null || nom.isEmpty()) ||
                (prix == null || prix.isEmpty()) ||
                (stock == null || stock.isEmpty()) ||
                (image == null || image.isEmpty())) {
            return true;
        }
        return false;
    }

    private boolean checkValues(String nom, String prix, String stock, String image) {
        return !checkEmpty(nom, prix, stock, image) && CheckIntFloat.checkInt(stock) && CheckIntFloat.checkFloat(prix);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nom = req.getParameter("nom");
        String prix = req.getParameter("prix");
        String stock = req.getParameter("stock");
        String image = req.getParameter("image");

        boolean correctValues = checkValues(nom, prix, stock, image);

        if(correctValues) {
            // Ajouter produit à la bdd
            Articles newProduct = new Articles(nom, new BigDecimal(prix), new BigInteger(stock), image);
            ArticlesDAO.addArticle(newProduct);
            req.getRequestDispatcher("/WEB-INF/view/productManagement.jsp").forward(req, resp);
        }
        else {
            req.setAttribute("error", true);
            req.getRequestDispatcher("/WEB-INF/view/addProduct.jsp").forward(req, resp);
        }
    }
}
