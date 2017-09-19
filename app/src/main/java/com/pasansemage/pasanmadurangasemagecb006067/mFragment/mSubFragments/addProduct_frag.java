package com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class addProduct_frag extends Fragment {


    //Reference
    EditText txtTitle, txtDescription, txtPrice;
    Spinner spinnerCategory;
    ImageView ivDress;
    Button btnChooseDress, btnAddProduct;
    CheckBox small, medium, large, xl, xxl;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db, dbRead;

    String[] CategoryArray;


    public addProduct_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_frag, container, false);

        txtTitle = (EditText) view.findViewById(R.id.txtATitle);
        txtDescription = (EditText) view.findViewById(R.id.txtADescription);
        txtPrice = (EditText) view.findViewById(R.id.txtAPrice);
        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerProductCategory);
        ivDress = (ImageView) view.findViewById(R.id.ivProductImage);
        btnAddProduct = (Button) view.findViewById(R.id.btnAddProductInto);
        btnChooseDress = (Button) view.findViewById(R.id.btnChooseDress);
        small = (CheckBox) view.findViewById(R.id.small);
        medium = (CheckBox) view.findViewById(R.id.medium);
        large = (CheckBox) view.findViewById(R.id.large);
        xl = (CheckBox) view.findViewById(R.id.xtralarge);
        xxl = (CheckBox) view.findViewById(R.id.doublexl);


        databaseHelper = new DatabaseHelper(getContext());
        db = databaseHelper.getWritableDatabase();
        dbRead = databaseHelper.getWritableDatabase();

        CategoryArray = new String[5];
        CategoryArray[0] = "Men";
        CategoryArray[1] = "Women";
        CategoryArray[2] = "Kids";
        CategoryArray[3] = "Jewelry and Watches";
        CategoryArray[4] = "Fashion Accessories";

        final ArrayAdapter CategoryAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, CategoryArray);

        spinnerCategory.setAdapter(CategoryAdapter);


        btnChooseDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                } else {
                    startGallery();
                }
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title, cate, desc, price;
                byte[] image;

                title = txtTitle.getText().toString();
                cate = spinnerCategory.getSelectedItem().toString();
                desc = txtDescription.getText().toString();
                price = txtPrice.getText().toString();
                image = picToArray(ivDress);

                if (validate()) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Table.PRODUCT_TITLE, title);
                    contentValues.put(Table.PRODUCT_CATEGORY, cate);
                    contentValues.put(Table.PRODUCT_DESCRIPTION, desc);
                    contentValues.put(Table.PRODUCT_PRICE, price);
                    contentValues.put(Table.PRODUCT_IMAGE, image);

                    long result = db.insert(Table.TABLE_PRODUCT, null, contentValues);

                    if (result == -1) {
                        Toast.makeText(getContext(), "Product is not added!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_LONG).show();

                        //clearing all fields
                        txtTitle.setText("");
                        txtPrice.setText("");
                        txtDescription.setText("");
                        ivDress.setImageResource(R.drawable.dress);

                    }

                    Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_PRODUCT + " WHERE " + Table.PRODUCT_TITLE + "=?", new String[]{title});
                    cursor.moveToNext();

                    int id = cursor.getInt(cursor.getColumnIndex(Table.PRODUCT_ID));

                    if (small.isChecked()){
                        ContentValues values = new ContentValues();
                        values.put(Table.SIZE_PRODUCT_ID,id );
                        values.put(Table.SIZE_SIZE,"S");
                        db.insert(Table.TABLE_SIZE,null,values);
                    }

                    if (medium.isChecked()){
                        ContentValues values = new ContentValues();
                        values.put(Table.SIZE_PRODUCT_ID,id );
                        values.put(Table.SIZE_SIZE,"M");
                        db.insert(Table.TABLE_SIZE,null,values);
                    }

                    if (large.isChecked()){
                        ContentValues values = new ContentValues();
                        values.put(Table.SIZE_PRODUCT_ID,id );
                        values.put(Table.SIZE_SIZE,"L");
                        db.insert(Table.TABLE_SIZE,null,values);
                    }

                    if (xl.isChecked()){
                        ContentValues values = new ContentValues();
                        values.put(Table.SIZE_PRODUCT_ID,id );
                        values.put(Table.SIZE_SIZE,"XL");
                        db.insert(Table.TABLE_SIZE,null,values);
                    }

                    if (xxl.isChecked()){
                        ContentValues values = new ContentValues();
                        values.put(Table.SIZE_PRODUCT_ID,id );
                        values.put(Table.SIZE_SIZE,"XXL");
                        db.insert(Table.TABLE_SIZE,null,values);
                    }

                }
            }
        });


        return view;
    }

    private boolean validate() {

        if ("".equals(txtTitle.getText().toString()) ||
                "".equals(txtDescription.getText().toString()) ||
                "".equals(txtPrice.getText().toString())) {
            Toast.makeText(getContext(), "You cannot leave blank fields! Please fill the blanks.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private byte[] picToArray(ImageView imageView) {

        Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap(), 512, 512, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivDress.setImageBitmap(bitmapImage);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
