package com.pasansemage.pasanmadurangasemagecb006067;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    //Reference
    EditText txtUsername, txtPassword;
    Button btnLogin;
    TextView txtVCreateNewAccount, txtVForgotPassword;


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

        //init
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtVCreateNewAccount = (TextView) findViewById(R.id.txtVCreateNewAccount);
        txtVForgotPassword = (TextView) findViewById(R.id.txtVForgotPassword);

        //when button click login logic will happen
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        //when text view click go to create new account activity
        txtVCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        //when text view click go to forgot password activity
        txtVForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPassword();
            }
        });

    }

    private void Login() {

        String Username = txtUsername.getText().toString().trim();
        String Password = txtPassword.getText().toString().trim();

        if ("".equals(txtUsername.getText().toString())) {
            ErrorAlert("Login", "Please fill Username");
        } else if ("".equals(txtPassword.getText().toString())) {
            ErrorAlert("Login", "Please fill Password");
        }else{
            Cursor cursor = db.rawQuery("SELECT * FROM " + Table.TABLE_USER + " WHERE " + Table.USER_NAME + "=? AND " + Table.USER_PASSWORD + "=?",new  String[]{Username,Password});

            if (cursor != null){
                if(cursor.getCount()>0){
                    String UI = cursor.getString(cursor.getColumnIndex(Table.USER_ID));
                    String UN = cursor.getString(cursor.getColumnIndex(Table.USER_NAME));
                    String UT =cursor.getString(cursor.getColumnIndex(Table.USER_TYPE));

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERID",UI);
                    editor.putString("USERNAME",UN);
                    editor.putString("USERTYPE",UT);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }


    private void CreateAccount() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void ForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void ErrorAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
