package com.example.inventory.service;

import com.example.inventory.dao.AdminDAO;
import com.example.inventory.dao.AdminDAOImpl;

public class AuthService {

    private static final int MAX_ATTEMPTS = 3;
    private final AdminDAO adminDAO = new AdminDAOImpl();

    /**
     * Prompts happen in Main; this just validates credentials.
     */
    public boolean login(String username, String password) {
        return adminDAO.validateLogin(username, password);
    }

    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }
}
