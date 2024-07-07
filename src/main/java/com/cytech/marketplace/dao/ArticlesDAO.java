package com.cytech.marketplace.dao;

import com.cytech.marketplace.entity.Articles;
import com.cytech.marketplace.utils.CreateIDUtil;
import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ArticlesDAO {
    private final DistributedTransactionManager manager;

    public ArticlesDAO() throws IOException {
        Properties loadProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("scalardb.properties")) {
            loadProperties.load(input);
        } catch (IOException e) {
            throw e;
        }

        TransactionFactory transactionFactory = TransactionFactory.create(loadProperties);
        manager = transactionFactory.getTransactionManager();
    }

    public void close() {
        manager.close();
    }

    public void loadInitialData() throws Exception {
        addArticle("Article 1", 10.0f, 100, "image1");
        addArticle("Article 2", 20.0f, 200, "image2");
        addArticle("Article 3", 30.0f, 300, "image3");
        addArticle("Article 4", 40.0f, 400, "image4");
        addArticle("Article 5", 50.0f, 500, "image5");
    }

    public void addArticle(
            long id,
            String name,
            float price,
            int stock,
            String image
    ) throws Exception {

        Articles article = getArticle(name);
        DistributedTransaction transaction = null;

        try {
            transaction = manager.start();
            if (article == null) {
                transaction.put(
                        Put.newBuilder()
                                .namespace("marketplace")
                                .table("articles")
                                .partitionKey(Key.ofBigInt("id", id))
                                .textValue("name", name)
                                .floatValue("price", price)
                                .intValue("stock", stock)
                                .textValue("image", image)
                                .build());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    public void addArticle(
            String name,
            float price,
            int stock,
            String image
    ) throws Exception {
        long id = CreateIDUtil.createID();
        addArticle(id, name, price, stock, image);
    }

    public void updateArticle(Articles articles) throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            transaction.put(
                    Put.newBuilder()
                            .namespace("marketplace")
                            .table("articles")
                            .partitionKey(Key.ofBigInt("id", articles.getId()))
                            .textValue("name", articles.getName())
                            .floatValue("price", articles.getPrice())
                            .intValue("stock", articles.getStock())
                            .textValue("image", articles.getImage())
                            .build());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            e.printStackTrace();
        }
    }

    public void deleteArticle(long id) throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            transaction.delete(
                    Delete.newBuilder()
                            .namespace("marketplace")
                            .table("articles")
                            .partitionKey(Key.ofBigInt("id", id))
                            .build());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            e.printStackTrace();
        }
    }

    public void deleteArticle(Articles articles) throws AbortException {
        deleteArticle(articles.getId());
    }

    public void deleteArticle(String name) throws Exception {
        deleteArticle(getArticle(name).getId());
    }

    public Articles getArticle(long id) throws Exception {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Optional<Result> result =
                    transaction.get(
                            Get.newBuilder()
                                    .namespace("marketplace")
                                    .table("articles")
                                    .partitionKey(Key.ofBigInt("id", id))
                                    .build());
            if (!result.isPresent()) {
                throw new Exception("Article not found");
            }

            transaction.commit();

            return new Articles(
                    result.get().getText("name"),
                    result.get().getFloat("price"),
                    result.get().getInt("stock"),
                    result.get().getText("image")
            );
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

public Articles getArticle(String name) throws Exception {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                    .namespace("marketplace")
                    .table("articles")
                    .all().build();

            List<Result> resultList = transaction.scan(scan);
            for (Result result : resultList) {
                if (name.equals(result.getText("name"))) {
                    transaction.commit();
                    return new Articles(
                            result.getText("name"),
                            result.getFloat("price"),
                            result.getInt("stock"),
                            result.getText("image"),
                            result.getBigInt("id")
                    );
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
        return null;
    }

    public List<Articles> getAllArticles() throws Exception {
        List<Articles> articlesList = new ArrayList<>();
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                    .namespace("marketplace")
                    .table("articles")
                    .all().build();

            List<Result> results = transaction.scan(scan);
            for (Result result : results) {
                articlesList.add(new Articles(
                        result.getText("name"),
                        result.getFloat("price"),
                        result.getInt("stock"),
                        result.getText("image"),
                        result.getBigInt("id")));
            }

            transaction.commit();
            return articlesList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    public boolean checkStock(Articles articles, int quantity) throws Exception {
        Articles updatedArticles = getArticle(articles.getId());
        return updatedArticles.getStock() >= quantity;
    }

    public boolean checkStock(String name, int quantity) throws Exception {
        Articles updatedArticles = getArticle(name);
        return updatedArticles.getStock() >= quantity;
    }
}