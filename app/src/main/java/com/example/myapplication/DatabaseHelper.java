package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "warehouse.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tạo bảng users
        String createUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";
        db.execSQL(createUsers);

        // 2. Tạo bảng products
        String createProducts = "CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "category TEXT NOT NULL, " +
                "active INTEGER NOT NULL, " +
                "description TEXT)";
        db.execSQL(createProducts);

        // 3. Chèn tài khoản mẫu
        ContentValues userValues = new ContentValues();
        userValues.put("username", "admin");
        userValues.put("password", "123456");
        db.insert("users", null, userValues);

        // 4. Chèn 3 sản phẩm mẫu
        insertSampleProduct(db, "Chuột không dây", 150000, 20, "Điện tử", 1, "Chuột không dây pin sạc êm ái");
        insertSampleProduct(db, "Bàn phím cơ", 450000, 3, "Điện tử", 1, "Bàn phím cơ switch xanh, có đèn nền");
        insertSampleProduct(db, "Sổ tay A5", 25000, 40, "Văn phòng phẩm", 1, "Sổ tay bìa cứng 200 trang");
    }

    private void insertSampleProduct(SQLiteDatabase db, String name, int price, int qty, String cat, int active, String desc) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("quantity", qty);
        values.put("category", cat);
        values.put("active", active);
        values.put("description", desc);
        db.insert("products", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    // Kiểm tra đăng nhập
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Thêm tài khoản mới
    public long insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        return db.insert("users", null, values);
    }

    // Thêm sản phẩm mới
    public long insertProduct(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", p.getName());
        values.put("price", p.getPrice());
        values.put("quantity", p.getQuantity());
        values.put("category", p.getCategory());
        values.put("active", p.getActive());
        values.put("description", p.getDescription());
        return db.insert("products", null, values);
    }

    // Lấy toàn bộ sản phẩm
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy sản phẩm theo id
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(id)});
        Product p = null;
        if (cursor.moveToFirst()) {
            p = new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            );
        }
        cursor.close();
        return p;
    }

    // Cập nhật số lượng
    public int updateQuantity(int id, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);
        return db.update("products", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Xóa sản phẩm
    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products", "id = ?", new String[]{String.valueOf(id)});
    }

    // Tổng giá trị kho: SUM(price * quantity)
    public long getTotalValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(price * quantity) FROM products", null);
        long total = 0;
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getLong(0);
        }
        cursor.close();
        return total;
    }

    // Số sản phẩm sắp hết hàng (quantity < 5)
    public int countLowStock() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM products WHERE quantity < 5", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}