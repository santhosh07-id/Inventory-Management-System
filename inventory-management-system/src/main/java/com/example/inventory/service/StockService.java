package com.example.inventory.service;

import com.example.inventory.dao.ProductDAO;
import com.example.inventory.dao.ProductDAOImpl;
import com.example.inventory.model.Product;

import java.util.List;

public class StockService {

    private final ProductDAO productDAO = new ProductDAOImpl();

    public boolean restock(int productId, int additionalQuantity) {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            return false;
        }
        product.setQuantity(product.getQuantity() + additionalQuantity);
        return productDAO.updateProduct(product);
    }

    public List<Product> checkLowStock() {
        return productDAO.getLowStockProducts();
    }

    public void printLowStockAlerts() {
        List<Product> lowStock = checkLowStock();
        if (lowStock.isEmpty()) {
            System.out.println("All stock levels are healthy.");
            return;
        }
        System.out.println("*** LOW STOCK ALERT ***");
        for (Product p : lowStock) {
            System.out.println(" - " + p.getName() + " | Qty: " + p.getQuantity()
                    + " | Threshold: " + p.getLowStockThreshold());
        }
    }
}
