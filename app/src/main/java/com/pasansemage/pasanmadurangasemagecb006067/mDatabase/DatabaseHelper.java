package com.pasansemage.pasanmadurangasemagecb006067.mDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pasan .M. Semage on 2017-09-18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context) {
        super(context, Table.DATABASE_NAME, null, Table.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table.CREATE_USER_TABLE);
        db.execSQL(Table.CREATE_PRODUCT_TABLE);
        db.execSQL(Table.CREATE_IMAGE_TABLE);
        db.execSQL(Table.CREATE_SIZE_TABLE);
        db.execSQL(Table.CREATE_CART_TABLE);
        db.execSQL(Table.CREATE_PURCHASE_TABLE);
        db.execSQL(Table.CREATE_REVIEW_TABLE);
        db.execSQL(Table.CREATE_CC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Table.DROP_USER_TABLE);
        db.execSQL(Table.DROP_PRODUCT_TABLE);
        db.execSQL(Table.DROP_IMAGE_TABLE);
        db.execSQL(Table.DROP_SIZE_TABLE);
        db.execSQL(Table.DROP_CART_TABLE);
        db.execSQL(Table.DROP_PURCHASE_TABLE);
        db.execSQL(Table.DROP_REVIEW_TABLE);
        db.execSQL(Table.DROP_CC_TABLE);
        onCreate(db);
    }
}
