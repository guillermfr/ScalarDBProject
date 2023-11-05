package com.cytech.marketplace.dao;

import com.cytech.marketplace.entity.Articles;
import com.cytech.marketplace.entity.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.UUID;

public class DAOBase {
    public static void addUser(Users user) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.persist(user);
        transaction.commit();
        em.close();
    }

    public static void updateUser(Users user) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.merge(user);
        transaction.commit();
        em.close();
    }

    public static void deleteUser(Users user) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.remove(em.contains(user) ? user : em.merge(user));
        transaction.commit();
        em.close();
    }

    public static void deleteUser(String email) {
        deleteUser(getUser(email));
    }

    public static void deleteUser(UUID uuid) {
        deleteUser(getUser(uuid));
    }

    public static Users getUser(UUID uuid) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        Users user = em.find(Users.class, uuid);
        em.close();
        return user;
    }

    public static Users getUser(String email) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        Users user = em.find(Users.class, email);
        em.close();
        return user;
    }

    public static List<Users> getUsersByName(String name) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        List<Users> users = em.createQuery("FROM Users WHERE name = :name", Users.class).setParameter("name", name).getResultList();
        em.close();
        return users;
    }

    public static Users getFirstUserByName(String name) {
        List<Users> users = getUsersByName(name);
        return users.isEmpty() ? null : users.get(0);
    }

    public static void addArticle(Articles article) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.persist(article);
        transaction.commit();
        em.close();
    }

    public static void updateArticle(Articles article) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.merge(article);
        transaction.commit();
        em.close();
    }

    public static void deleteArticle(Articles article) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.remove(em.contains(article) ? article : em.merge(article));
        transaction.commit();
        em.close();
    }

    public static void deleteArticle(UUID uuid) {
        deleteArticle(getArticle(uuid));
    }

    public static void deleteArticle(String name) {
        deleteArticle(getArticle(name));
    }

    public static Articles getArticle(UUID uuid) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        Articles article = em.find(Articles.class, uuid);
        em.close();
        return article;
    }

    public static Articles getArticle(String name) {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        Articles article = em.find(Articles.class, name);
        em.close();
        return article;
    }

    public static List<Articles> getArticles() {
        EntityManager em = PersistenceUtil.getEmf().createEntityManager();
        List<Articles> articles = em.createQuery("FROM Articles", Articles.class).getResultList();
        em.close();
        return articles;
    }
}
