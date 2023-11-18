package com.cytech.marketplace.servlet;

import com.cytech.marketplace.dao.ArticlesDAO;
import com.cytech.marketplace.entity.Articles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

@WebServlet(name = "infoPaymentServlet", value = "/infoPayment-servlet")
public class InfoPaymentServlet extends HttpServlet {

    private boolean checkEmpty(String nomCarte, String numeroCarte, String dateExpiration, String codeCarte) {
        if((nomCarte == null || nomCarte.isEmpty()) ||
                (numeroCarte == null || numeroCarte.isEmpty()) ||
                (dateExpiration == null || dateExpiration.isEmpty()) ||
                (codeCarte == null || codeCarte.isEmpty())) {
            return true;
        }
        return false;
    }

    private boolean checkValues(String nomCarte, String numeroCarte, String dateExpiration, String codeCarte) {
        return !checkEmpty(nomCarte, numeroCarte, dateExpiration, codeCarte);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nomCarte = req.getParameter("nomCarte");
        String numeroCarte = req.getParameter("numeroCarte");
        String dateExpiration = req.getParameter("dateExpiration");
        String codeCarte = req.getParameter("codeCarte");

        boolean correctValues = checkValues(nomCarte, numeroCarte, dateExpiration, codeCarte);

        if(correctValues) {

            Object cartObject = req.getSession().getAttribute("cart");
            Map<Articles, Integer> cart = null;
            if(cartObject != null) {
                cart = (Map<Articles, Integer>) cartObject;
            }

            for (Map.Entry<Articles, Integer> article : cart.entrySet()) {
                Articles modifiedArticle = article.getKey();
                modifiedArticle.setStock(BigInteger.valueOf(modifiedArticle.getStock().intValue() - article.getValue()));
                ArticlesDAO.updateArticle(modifiedArticle);
            }

            req.removeAttribute("cart");

            //TODO : faire un mail de confirmation avec un résumé de la commande

            req.getRequestDispatcher("/WEB-INF/view/confirmationPayment.jsp").forward(req, resp);
        }
        else {
            req.setAttribute("error", true);
            req.getRequestDispatcher("/WEB-INF/view/infopayment.jsp").forward(req, resp);
        }
    }
}