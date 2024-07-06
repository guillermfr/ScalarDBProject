package com.cytech.marketplace.entity;

public class Articles {
    private String name;
    private float price;
    private int stock;
    private String image;
    private long id;

    public Articles(String nom, float prix, int stock, String image) {
        this.name = nom;
        this.price = prix;
        this.stock = stock;
        this.image = image;
    }

    public Articles() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Articles articles = (Articles) o;

        return id == articles.id;
    }
}
