package com.cytech.marketplace.dao;

import com.scalar.db.api.*;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ScalarDBUtil {
    private final DistributedTransactionManager manager;

    public ScalarDBUtil() throws IOException {
        TransactionFactory transactionFactory = TransactionFactory.create("/home/cytech/Desktop/Advanced course in database systems/Project/Test/JEEMarketplace/database.properties");
        manager = transactionFactory.getTransactionManager();
    }

    public void close() {
        manager.close();
    }

    public void loadInitialData() throws Exception {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            loadArticleIfNotExists(transaction, 1, "Article 1", 10.0f, 100, "image1");
            loadArticleIfNotExists(transaction, 2, "Article 2", 20.0f, 200, "image2");
            loadArticleIfNotExists(transaction, 3, "Article 3", 30.0f, 300, "image3");
            loadArticleIfNotExists(transaction, 4, "Article 4", 40.0f, 400, "image4");
            loadArticleIfNotExists(transaction, 5, "Article 5", 50.0f, 500, "image5");
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    private void loadArticleIfNotExists(
            DistributedTransaction transaction,
            int id,
            String name,
            float price,
            int stock,
            String image)
            throws Exception {
        Optional<Result> article =
                transaction.get(
                        Get.newBuilder()
                                .namespace("marketplace")
                                .table("articles")
                                .partitionKey(Key.ofInt("id", id))
                                .build());
        if (!article.isPresent()) {
            transaction.put(
                    Put.newBuilder()
                            .namespace("marketplace")
                            .table("articles")
                            .partitionKey(Key.ofInt("id", id))
                            .textValue("name", name)
                            .floatValue("price", price)
                            .intValue("stock", stock)
                            .textValue("image", image)
                            .build());
        }
    }

    public String getArticleInfo(int id) throws Exception {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Optional<Result> article =
                    transaction.get(
                            Get.newBuilder()
                                    .namespace("marketplace")
                                    .table("articles")
                                    .partitionKey(Key.ofInt("id", id))
                                    .build());
            if (!article.isPresent()) {
                throw new Exception("Article not found");
            }

            transaction.commit();

            return String.format(
                    "Article %d: %s, price: %.2f, stock: %d, image: %s",
                    id,
                    article.get().getText("name"),
                    article.get().getFloat("price"),
                    article.get().getInt("stock"),
                    article.get().getText("image"));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }
}