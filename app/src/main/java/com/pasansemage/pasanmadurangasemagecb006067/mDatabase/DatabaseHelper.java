package com.pasansemage.pasanmadurangasemagecb006067.mDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.pasansemage.pasanmadurangasemagecb006067.R;

import java.io.ByteArrayOutputStream;

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

        ContentValues values = new ContentValues();
        values.put(Table.USER_NAME, "admin");
        values.put(Table.USER_PASSWORD, "123");
        values.put(Table.USER_EMAIL, "admin@styleomega.com");
        values.put(Table.USER_FULL_NAME, "Pasan Maduranga Semage");
        values.put(Table.USER_GENDER, "Male");
        values.put(Table.USER_BILLING_ADDRESS, "Ambalangoda, Sri Lanka");
        values.put(Table.USER_MOBILE_NUMBER, "0774166103");
        values.put(Table.USER_TYPE, "Admin");

        db.insert(Table.TABLE_USER,null,values);
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
