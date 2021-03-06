package com.example.duyda.onlinesaleshop.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.duyda.onlinesaleshop.Models.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "ShopDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId", "ProductName", "Size", "Quantity", "Price", "Discount"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Size")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Size,Quantity,Price,Discount) Values('%s','%s','%s','%s','%s','%s') ",
                order.getProductId(),
                order.getProductName(),
                order.getSize(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
    }

    public void clearCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    //Favorite
    private static final String FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS Favorites" +
            "(ProductId TEXT PRIMARY KEY)";


    public void addToFavorites(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(ProductId) VALUES ('%s');", productId);
        db.execSQL(query);
    }

    public void removeToFavorites(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE ProductId='%s';", productId);
        db.execSQL(query);
    }

    public boolean isFavorite(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE ProductId='%s';", productId);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


}
