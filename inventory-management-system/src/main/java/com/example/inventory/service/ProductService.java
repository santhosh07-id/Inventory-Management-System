package com.example.inventory.service;

import com.example.inventory.dao.ProductDAO;
import com.example.inventory.dao.ProductDAOImpl;
import com.example.inventory.model.Product;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAOImpl();

    public int addProduct(String name, String category, double price, int quantity, int lowStockThreshold) {
        Product product = new Product(name, category, price, quantity, lowStockThreshold);
        return productDAO.addProduct(product);
    }

    public boolean updateProduct(int id, String name, String category, double price, int quantity, int lowStockThreshold) {
        Product product = new Product(id, name, category, price, quantity, lowStockThreshold);
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }

    public Product getProduct(int id) {
        return productDAO.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
}
