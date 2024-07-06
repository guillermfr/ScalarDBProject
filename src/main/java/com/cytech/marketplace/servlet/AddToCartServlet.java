package com.cytech.marketplace.servlet;

import com.cytech.marketplace.dao.ArticlesDAO;
import com.cytech.marketplace.entity.Articles;
import com.cytech.marketplace.utils.CartUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.abs;


@WebServlet(name = "addToCartServlet", value = "/addToCart-servlet")
public class AddToCartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String productId = req.getParameter("productId");
        String productQty = req.getParameter("productQty");
        Map<Articles, Integer> cart = CartUtil.getCart(req);

        long productIdLong = Long.parseLong(productId);
        int qty = Integer.parseInt(productQty);
        ArticlesDAO articlesDAO = new ArticlesDAO();
        Articles product = null;
        try {
            product = articlesDAO.getArticle(productIdLong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (cart.containsKey(product)) {
            int qtyInCart = cart.get(product);
            int toPutInCart = qty + qtyInCart;
            if (product.getStock() < 0){
                int correctedQty = abs(qtyInCart - product.getStock());
                try {
                    CartUtil.addArticleToCart(req, product, correctedQty);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    CartUtil.addArticleToCart(req, product, qty);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                CartUtil.addArticleToCart(req, product, qty);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        req.getRequestDispatcher("/WEB-INF/view/cart.jsp").forward(req, resp);
    }
}
