# Inventory Management System (Java + JDBC + MySQL)

Plain JDBC console application — no ORM, no frameworks. Uses `PreparedStatement`,
manual resource cleanup, and manual transaction control (`commit`/`rollback`) for
atomic sales processing.

## Structure

```
inventory-management-system/
├── pom.xml
├── sql/
│   └── schema.sql                  # Run this in MySQL first
└── src/main/
    ├── resources/
    │   └── db.properties           # DB connection settings
    └── java/com/example/inventory/
        ├── Main.java                       # Console menu / entry point
        ├── config/DBConnection.java        # JDBC connection factory
        ├── util/DBUtil.java                # Resource cleanup helper
        ├── model/
        │   ├── Admin.java
        │   ├── Product.java
        │   └── Sale.java
        ├── dao/
        │   ├── AdminDAO.java / AdminDAOImpl.java
        │   ├── ProductDAO.java / ProductDAOImpl.java
        │   └── SalesDAO.java / SalesDAOImpl.java
        └── service/
            ├── AuthService.java             # 3-attempt login gate
            ├── ProductService.java          # Add/update/delete/view products
            ├── StockService.java            # Restock + low stock alerts
            └── SalesReportService.java      # Atomic sale transaction + reports
```

## Setup

1. **Create the database and tables:**
   ```bash
   mysql -u root -p < sql/schema.sql
   ```
   This also inserts a default admin login: `admin` / `admin123`.

2. **Configure DB credentials** in `src/main/resources/db.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/inventory_db
   db.username=root
   db.password=yourpassword
   ```

3. **Build with Maven** (produces a runnable fat jar with the MySQL driver bundled):
   ```bash
   mvn clean package
   ```

4. **Run:**
   ```bash
   java -jar target/inventory-management-system-jar-with-dependencies.jar
   ```

## Features

- **Admin Login** — 3-attempt gate before the app exits.
- **Product Management** — add, update, delete, view single/all products.
- **Stock Management** — restock products, view low-stock alerts
  (`quantity <= low_stock_threshold`).
- **Sales Reporting** — records a sale atomically:
  1. Locks the product row (`SELECT ... FOR UPDATE`)
  2. Validates sufficient stock
  3. Reduces stock (`UPDATE ... WHERE quantity >= ?`)
  4. Inserts the sale record
  5. Commits (or rolls back on any failure)

  Also shows sales history and total revenue.

## Notes

- No ORM (Hibernate/JPA) — everything is raw JDBC with `PreparedStatement`.
- Connections/statements/result sets are closed manually via `DBUtil.closeQuietly(...)`.
- Requires Java 17+ and MySQL 5.7+/8+.
