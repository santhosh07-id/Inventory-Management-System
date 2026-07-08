package com.example.inventory;

import com.example.inventory.model.Product;
import com.example.inventory.model.Sale;
import com.example.inventory.service.AuthService;
import com.example.inventory.service.ProductService;
import com.example.inventory.service.SalesReportService;
import com.example.inventory.service.StockService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final ProductService productService = new ProductService();
    private static final StockService stockService = new StockService();
    private static final SalesReportService salesReportService = new SalesReportService();

    public static void main(String[] args) {
        if (!loginGate()) {
            System.out.println("Too many failed attempts. Exiting.");
            return;
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> productMenu();
                case "2" -> stockMenu();
                case "3" -> salesMenu();
                case "4" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Session ended. Goodbye!");
        scanner.close();
    }

    // ---------- LOGIN ----------

    private static boolean loginGate() {
        int attempts = 0;
        int maxAttempts = authService.getMaxAttempts();

        while (attempts < maxAttempts) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (authService.login(username, password)) {
                System.out.println("Login successful. Welcome, " + username + "!");
                return true;
            } else {
                attempts++;
                System.out.println("Invalid credentials. Attempts remaining: " + (maxAttempts - attempts));
            }
        }
        return false;
    }

    // ---------- MAIN MENU ----------

    private static void printMainMenu() {
        System.out.println("\n===== Inventory Management System =====");
        System.out.println("1. Product Management");
        System.out.println("2. Stock Management");
        System.out.println("3. Sales Reporting");
        System.out.println("4. Exit");
        System.out.print("Enter choice: ");
    }

    // ---------- PRODUCT MANAGEMENT ----------

    private static void productMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Product Management ---");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View Product by ID");
            System.out.println("5. View All Products");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addProduct();
                case "2" -> updateProduct();
                case "3" -> deleteProduct();
                case "4" -> viewProductById();
                case "5" -> viewAllProducts();
                case "6" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addProduct() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Initial quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        System.out.print("Low stock threshold: ");
        int threshold = Integer.parseInt(scanner.nextLine());

        int id = productService.addProduct(name, category, price, quantity, threshold);
        System.out.println(id != -1 ? "Product added with ID: " + id : "Failed to add product.");
    }

    private static void updateProduct() {
        System.out.print("Product ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        Product existing = productService.getProduct(id);
        if (existing == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("New name (" + existing.getName() + "): ");
        String name = scanner.nextLine();
        System.out.print("New category (" + existing.getCategory() + "): ");
        String category = scanner.nextLine();
        System.out.print("New price (" + existing.getPrice() + "): ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("New quantity (" + existing.getQuantity() + "): ");
        int quantity = Integer.parseInt(scanner.nextLine());
        System.out.print("New low stock threshold (" + existing.getLowStockThreshold() + "): ");
        int threshold = Integer.parseInt(scanner.nextLine());

        boolean updated = productService.updateProduct(id, name, category, price, quantity, threshold);
        System.out.println(updated ? "Product updated." : "Update failed.");
    }

    private static void deleteProduct() {
        System.out.print("Product ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean deleted = productService.deleteProduct(id);
        System.out.println(deleted ? "Product deleted." : "Delete failed or product not found.");
    }

    private static void viewProductById() {
        System.out.print("Product ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Product product = productService.getProduct(id);
        System.out.println(product != null ? product : "Product not found.");
    }

    private static void viewAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            products.forEach(System.out::println);
        }
    }

    // ---------- STOCK MANAGEMENT ----------

    private static void stockMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stock Management ---");
            System.out.println("1. Restock Product");
            System.out.println("2. View Low Stock Alerts");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> restockProduct();
                case "2" -> stockService.printLowStockAlerts();
                case "3" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void restockProduct() {
        System.out.print("Product ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Quantity to add: ");
        int qty = Integer.parseInt(scanner.nextLine());

        boolean success = stockService.restock(id, qty);
        System.out.println(success ? "Stock updated." : "Product not found or update failed.");
    }

    // ---------- SALES REPORTING ----------

    private static void salesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Sales Reporting ---");
            System.out.println("1. Record a Sale");
            System.out.println("2. View Sales History");
            System.out.println("3. View Total Revenue");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> recordSale();
                case "2" -> viewSalesHistory();
                case "3" -> viewTotalRevenue();
                case "4" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void recordSale() {
        System.out.print("Product ID: ");
        int productId = Integer.parseInt(scanner.nextLine());
        System.out.print("Quantity sold: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        int saleId = salesReportService.processSale(productId, quantity);
        System.out.println(saleId != -1 ? "Sale recorded with ID: " + saleId : "Sale failed.");
    }

    private static void viewSalesHistory() {
        List<Sale> sales = salesReportService.getSalesHistory();
        if (sales.isEmpty()) {
            System.out.println("No sales recorded yet.");
        } else {
            sales.forEach(System.out::println);
        }
    }

    private static void viewTotalRevenue() {
        double revenue = salesReportService.getTotalRevenue();
        System.out.printf("Total Revenue: %.2f%n", revenue);
    }
}
