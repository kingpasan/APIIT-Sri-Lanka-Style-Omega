package com.pasansemage.pasanmadurangasemagecb006067;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hide Action Bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Database readable connection
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

    }
}
