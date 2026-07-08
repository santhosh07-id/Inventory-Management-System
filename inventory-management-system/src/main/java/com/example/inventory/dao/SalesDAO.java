package com.example.inventory.dao;

import com.example.inventory.model.Sale;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SalesDAO {
    // Runs inside the same transaction/connection as the stock reduction
    int recordSale(Connection conn, int productId, int quantitySold, double totalPrice) throws SQLException;

    List<Sale> getAllSales();
    double getTotalRevenue();
}
