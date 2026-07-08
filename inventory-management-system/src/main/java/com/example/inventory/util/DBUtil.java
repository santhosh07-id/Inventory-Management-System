package com.example.inventory.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {

    public static void closeQuietly(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (Exception ignored) {
        }
        try {
            if (ps != null) ps.close();
        } catch (Exception ignored) {
        }
        try {
            if (conn != null) conn.close();
        } catch (Exception ignored) {
        }
    }

    public static void closeQuietly(PreparedStatement ps, Connection conn) {
        closeQuietly(null, ps, conn);
    }
}
