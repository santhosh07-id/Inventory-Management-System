package com.example.inventory.dao;

import com.example.inventory.config.DBConnection;
import com.example.inventory.model.Sale;
import com.example.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAOImpl implements SalesDAO {

    @Override
    public int recordSale(Connection conn, int productId, int quantitySold, double totalPrice) throws SQLException {
        String sql = "INSERT INTO sale (product_id, quantity_sold, total_price) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productId);
            ps.setInt(2, quantitySold);
            ps.setDouble(3, totalPrice);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
            return -1;
        }
    }

    @Override
    public List<Sale> getAllSales() {
        String sql = "SELECT s.*, p.name AS product_name FROM sale s " +
                     "JOIN product p ON s.product_id = p.id ORDER BY s.sale_date DESC";
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("id"));
                sale.setProductId(rs.getInt("product_id"));
                sale.setProductName(rs.getString("product_name"));
                sale.setQuantitySold(rs.getInt("quantity_sold"));
                sale.setTotalPrice(rs.getDouble("total_price"));
                sale.setSaleDate(rs.getTimestamp("sale_date"));
                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return sales;
    }

    @Override
    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) AS revenue FROM sale";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double revenue = 0.0;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                revenue = rs.getDouble("revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return revenue;
    }
}
