package com.cytech.marketplace.servlet;

import com.cytech.marketplace.controller.CartController;
import com.cytech.marketplace.dao.ArticlesDAO;
import com.cytech.marketplace.entity.Articles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.cytech.marketplace.utils.CartUtil.addArticleToCart;

@WebServlet(name = "debugAddProductsToCart", value = "/debug")
public class DebugAddProductsToCart extends CartController {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ArticlesDAO articlesDAO = new ArticlesDAO();
        Articles vittel = null;
        try {
            vittel = articlesDAO.getArticle("Bouteille 1L Vittel");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            addArticleToCart(req, vittel, 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.doGet(req, resp);
    }
}
