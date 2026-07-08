package com.example.inventory.model;

import java.sql.Timestamp;

public class Sale {
    private int id;
    private int productId;
    private String productName; // populated on report queries
    private int quantitySold;
    private double totalPrice;
    private Timestamp saleDate;

    public Sale() {
    }

    public Sale(int productId, int quantitySold, double totalPrice) {
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        return "Sale{id=" + id +
                ", productId=" + productId +
                (productName != null ? ", productName='" + productName + '\'' : "") +
                ", quantitySold=" + quantitySold +
                ", totalPrice=" + totalPrice +
                ", saleDate=" + saleDate +
                '}';
    }
}
