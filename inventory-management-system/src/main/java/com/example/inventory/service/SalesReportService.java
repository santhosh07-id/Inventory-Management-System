package com.example.inventory.service;

import com.example.inventory.config.DBConnection;
import com.example.inventory.dao.ProductDAO;
import com.example.inventory.dao.ProductDAOImpl;
import com.example.inventory.dao.SalesDAO;
import com.example.inventory.dao.SalesDAOImpl;
import com.example.inventory.model.Product;
import com.example.inventory.model.Sale;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SalesReportService {

    private final ProductDAO productDAO = new ProductDAOImpl();
    private final SalesDAO salesDAO = new SalesDAOImpl();

    /**
     * Atomically: validates stock -> reduces quantity -> records sale.
     * Returns the generated sale ID, or -1 if the sale failed (e.g. insufficient stock).
     */
    public int processSale(int productId, int quantityRequested) {
        Connection conn = null;
        int saleId = -1;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            Product product = productDAO.getProductByIdForUpdate(conn, productId);
            if (product == null) {
                System.out.println("Product not found.");
                conn.rollback();
                return -1;
            }

            if (product.getQuantity() < quantityRequested) {
                System.out.println("Insufficient stock. Available: " + product.getQuantity());
                conn.rollback();
                return -1;
            }

            boolean reduced = productDAO.reduceStock(conn, productId, quantityRequested);
            if (!reduced) {
                conn.rollback();
                return -1;
            }

            double totalPrice = product.getPrice() * quantityRequested;
            saleId = salesDAO.recordSale(conn, productId, quantityRequested, totalPrice);

            if (saleId == -1) {
                conn.rollback();
                return -1;
            }

            conn.commit();

            // Post-commit low stock check (informational only, not part of the transaction)
            int remaining = product.getQuantity() - quantityRequested;
            if (remaining <= product.getLowStockThreshold()) {
                System.out.println("*** LOW STOCK ALERT: " + product.getName()
                        + " now at " + remaining + " units ***");
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            saleId = -1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return saleId;
    }

    public List<Sale> getSalesHistory() {
        return salesDAO.getAllSales();
    }

    public double getTotalRevenue() {
        return salesDAO.getTotalRevenue();
    }
}
