package com.pasansemage.pasanmadurangasemagecb006067;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    //Reference
    ImageView ivProfilePic;
    Button btnSelectPhoto, btnCreateAccount;
    EditText txtUsername, txtPassword, txtReEnterPassword, txtEmail, txtFullName, txtBillingAddress, txtTelephoneNumber;
    Spinner spinnerGender;
    DatabaseHelper databaseHelper, dbHelper;
    SQLiteDatabase db, dbWrite;

    //variables
    String[] GenderArray;
    final int REQUEST_CODE_GALLERY = 999;
    final int CROP_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //hide action bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //getting readable database
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        //getting writable database
        dbHelper = new DatabaseHelper(this);
        dbWrite = dbHelper.getWritableDatabase();

        //init
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        txtUsername = (EditText) findViewById(R.id.txtCAUsername);
        txtPassword = (EditText) findViewById(R.id.txtCAPassword);
        txtReEnterPassword = (EditText) findViewById(R.id.txtCAReEnterPassword);
        txtEmail = (EditText) findViewById(R.id.txtCAEmail);
        txtFullName = (EditText) findViewById(R.id.txtCAFullName);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        txtBillingAddress = (EditText) findViewById(R.id.txtCABillingAddress);
        txtTelephoneNumber = (EditText) findViewById(R.id.txtCATelephoneNumber);


        //store gender in array (Male, Female)
        GenderArray = new String[2];
        GenderArray[0] = "Male";
        GenderArray[1] = "Female";

        //creating adapter to gender spinner
        ArrayAdapter GenderAdapter = new ArrayAdapter(this, R.layout.spinner_item, GenderArray);

        //set adaptor to spinner
        spinnerGender.setAdapter(GenderAdapter);

        //when select photo button press gallery open to select photo
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {

                    //convert image to byte array
                    Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) ivProfilePic.getDrawable()).getBitmap(), 128, 128, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    //getting all information to variables
                    String username = txtUsername.getText().toString().trim();
                    String password = txtPassword.getText().toString().trim();
                    String email = txtEmail.getText().toString().trim();
                    String fullName = txtFullName.getText().toString().trim();
                    String gender = spinnerGender.getSelectedItem().toString().trim();
                    String billingAddress = txtBillingAddress.getText().toString().trim();
                    String telephoneNumber = txtTelephoneNumber.getText().toString().trim();
                    String userType = "Customer";
                    byte[] image = byteArray;


                    //adding values to the Content value
                    ContentValues values = new ContentValues();
                    values.put(Table.USER_NAME, username);
                    values.put(Table.USER_PASSWORD, password);
                    values.put(Table.USER_EMAIL, email);
                    values.put(Table.USER_FULL_NAME, fullName);
                    values.put(Table.USER_GENDER, gender);
                    values.put(Table.USER_BILLING_ADDRESS, billingAddress);
                    values.put(Table.USER_MOBILE_NUMBER, telephoneNumber);
                    values.put(Table.USER_IMAGE, image);
                    values.put(Table.USER_TYPE, userType);

                    long result = dbWrite.insert(Table.TABLE_USER, null, values);

                    if (result == -1) {
                        Toast.makeText(RegisterActivity.this, "Account is not Created! Please try again!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Account is Successfully Created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

    }

    //input validation
    private boolean validation() {

        if ("".equals(txtUsername.getText().toString()) ||
                "".equals(txtPassword.getText().toString()) ||
                "".equals(txtReEnterPassword.getText().toString()) ||
                "".equals(txtEmail.getText().toString()) ||
                "".equals(txtFullName.getText().toString()) ||
                "".equals(txtBillingAddress.getText().toString()) ||
                "".equals(txtTelephoneNumber.getText().toString())
                ) {
            Toast.makeText(RegisterActivity.this, "You cannot leave blank fields! Please fill the blanks.", Toast.LENGTH_LONG).show();
            return false;

        }

        if (!txtEmail.getText().toString().contains("@") || !txtEmail.getText().toString().contains(".")) {
            Toast.makeText(RegisterActivity.this, "Please enter valid email address!", Toast.LENGTH_LONG).show();
            return false;
        }


        if (txtTelephoneNumber.getText().length() != 10) {
            Toast.makeText(RegisterActivity.this, "Please fill the 10 digits of telephone number!", Toast.LENGTH_LONG).show();
            return false;
        }


        Cursor gettingUserDetails = db.rawQuery("SELECT * FROM " + Table.TABLE_USER + " WHERE " + Table.USER_NAME + " =?", new String[]{txtUsername.getText().toString()});

        if (gettingUserDetails.getCount() > 0) {
            if (gettingUserDetails != null) {
                Toast.makeText(RegisterActivity.this, "Username is not available! Please try another one", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (!txtPassword.getText().toString().equals(txtReEnterPassword.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Passwords are not matching, Please try again!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    //open gallery to select photo
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "You don't have permission to access photo gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //get selected photo to image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            /*Cropping image is not working on android 6.0 and some devices to crop image need to use API.
             API is increase app size so i don't implement this cropping function.
             Pasan Maduranga Semage
             */
            //cropImage(uri);

            try {

                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //Bitmap pic = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
                ivProfilePic.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } /*else if (requestCode == CROP_CODE) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");
            ivProfilePic.setImageBitmap(bitmap);
        }*/

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    private void cropImage(Uri uri) {

        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_CODE);
        } catch (ActivityNotFoundException e){

        }

    }
    */


}
