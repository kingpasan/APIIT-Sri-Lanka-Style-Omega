package com.pasansemage.pasanmadurangasemagecb006067;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Reference
    EditText txtUsername, txtEmail, txtTelephone;
    Button btnRecover;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db, dbWrite;
    String CurrentPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //getting readable database
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        //getting writable database
        dbWrite = databaseHelper.getWritableDatabase();


        //init
        txtUsername = (EditText) findViewById(R.id.txtFPUsename);
        txtEmail = (EditText) findViewById(R.id.txtFPEmail);
        txtTelephone = (EditText) findViewById(R.id.txtFPTelephoneNumber);
        btnRecover = (Button) findViewById(R.id.btnRecoverPassword);

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    AlertDialog.Builder  builder =  new AlertDialog.Builder(ForgotPasswordActivity.this);
                    builder.setTitle("View Password");
                    builder.setMessage("Your Password is : "+ CurrentPassword);
                    builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });


    }

    private boolean validate() {

        if ("".equals(txtUsername.getText().toString()) ||
                "".equals(txtEmail.getText().toString()) ||
                "".equals(txtTelephone.getText().toString())) {
            Toast.makeText(ForgotPasswordActivity.this, "You cannot leave blank fields! Please fill the blanks.", Toast.LENGTH_LONG).show();
            return false;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM " +
                        Table.TABLE_USER + " WHERE " +
                        Table.USER_NAME + " =?"
                , new String[]{
                        txtUsername.getText().toString()
                });

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                Toast.makeText(ForgotPasswordActivity.this, "Username is found", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(ForgotPasswordActivity.this, "Username is not found!", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        Cursor cursor1 = db.rawQuery("SELECT * FROM " +
                        Table.TABLE_USER + " WHERE " +
                        Table.USER_NAME + " =? AND " +
                        Table.USER_EMAIL + " =? AND " +
                        Table.USER_MOBILE_NUMBER + "=?"
                , new String[]{
                        txtUsername.getText().toString(),
                        txtEmail.getText().toString(),
                        txtTelephone.getText().toString()
                });

        if (cursor1 != null) {
            if (cursor1.getCount() > 0) {
                cursor1.moveToNext();
                CurrentPassword = cursor1.getString(cursor1.getColumnIndex(Table.USER_PASSWORD));
                return true;
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Email or Telephone number is incorrect!", Toast.LENGTH_LONG).show();
                return false;
            }
        }


        return false;
    }
}
