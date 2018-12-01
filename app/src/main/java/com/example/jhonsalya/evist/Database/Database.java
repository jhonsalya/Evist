package com.example.jhonsalya.evist.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.jhonsalya.evist.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhonsalya on 18/11/18.
 */

public class Database extends SQLiteAssetHelper{
    private static final String DB_NAME = "EvistDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context,DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"EventId","EventName","EventAddress","EventStartDate","EventStartTime", "Quantity", "Price", "SellerId"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new Order(c.getString(c.getColumnIndex("EventId")),
                        c.getString(c.getColumnIndex("EventName")),
                        c.getString(c.getColumnIndex("EventAddress")),
                        c.getString(c.getColumnIndex("EventStartDate")),
                        c.getString(c.getColumnIndex("EventStartTime")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("SellerId"))
                ));
            }while(c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(EventId,EventName,EventAddress, EventStartDate, EventStartTime, Quantity, Price, SellerId) VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                order.getEventID(),
                order.getEventName(),
                order.getEventAddress(),
                order.getEventStartDate(),
                order.getEventStartTime(),
                order.getQuantity(),
                order.getPrice(),
                order.getSellerId());
        db.execSQL(query);
    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }
}
