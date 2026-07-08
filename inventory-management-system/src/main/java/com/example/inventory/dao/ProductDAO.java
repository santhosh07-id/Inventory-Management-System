package com.example.inventory.dao;

import com.example.inventory.model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    int addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(int id);
    Product getProductById(int id);
    List<Product> getAllProducts();
    List<Product> getLowStockProducts();

    // Used by SalesReportService inside a shared transaction (same Connection)
    boolean reduceStock(Connection conn, int productId, int quantity) throws SQLException;
    Product getProductByIdForUpdate(Connection conn, int productId) throws SQLException;
}
