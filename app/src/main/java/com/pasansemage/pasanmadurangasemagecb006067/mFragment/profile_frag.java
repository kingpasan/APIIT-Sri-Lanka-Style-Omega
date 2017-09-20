package com.pasansemage.pasanmadurangasemagecb006067.mFragment;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mAdapter.CreditCardListAdapter;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;
import com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments.addCreditCard_frag;
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.CreditCard;
import com.pasansemage.pasanmadurangasemagecb006067.mWidgets.ExpandableHeightGridView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class profile_frag extends Fragment {


    //reference
    ImageView ivProfile;
    Button btnChoosePhoto, btnSavePhoto, btnChangeInfo, btnChangeAddress, btnAddCreditCard, btnChangePassword;
    EditText fullname, mobilenumber, billingaddress, currentpassword, newpassword, reenterpassword;
    TextView username, gender, email, address;
    ExpandableHeightGridView gridView;

    DatabaseHelper databaseHelper;
    SQLiteDatabase dbRead, dbWrite;
    String USER_ID, USERNAME;
    String Cname, Cfullname, Cgender, Caddress, Cmobile, Cpass;
    byte[] image;

    ArrayList<CreditCard> list;
    CreditCardListAdapter adapter = null;


    public profile_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_frag, container, false);

        ivProfile = (ImageView) view.findViewById(R.id.ivChangeProfilePic);
        btnChoosePhoto = (Button) view.findViewById(R.id.btnChangePhoto);
        btnSavePhoto = (Button) view.findViewById(R.id.btnChangeSavePhoto);
        btnChangeInfo = (Button) view.findViewById(R.id.btnChangeInfo);
        btnChangeAddress = (Button) view.findViewById(R.id.btnChangBillingAddress);
        btnAddCreditCard = (Button) view.findViewById(R.id.btnChangeAddCreditCard);
        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        fullname = (EditText) view.findViewById(R.id.txtFullName);
        mobilenumber = (EditText) view.findViewById(R.id.txtNumber);
        billingaddress = (EditText) view.findViewById(R.id.txtChangeBiliingAdress);
        currentpassword = (EditText) view.findViewById(R.id.txtCurrentPassword);
        newpassword = (EditText) view.findViewById(R.id.txtNewPassword);
        reenterpassword = (EditText) view.findViewById(R.id.txtReEnterNewPassword);
        username = (TextView) view.findViewById(R.id.txtUser);
        gender = (TextView) view.findViewById(R.id.txtGender);
        email = (TextView) view.findViewById(R.id.txtEmail);
        address = (TextView) view.findViewById(R.id.txtAdress);
        gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridViewCeditCard);

        databaseHelper = new DatabaseHelper(getContext());
        dbRead = databaseHelper.getReadableDatabase();
        dbWrite = databaseHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        USER_ID = sharedPreferences.getString("USERID", "");
        USERNAME = sharedPreferences.getString("USERNAME", "");

        gridView.setExpanded(true);
        gridView.setFocusable(false);

        list = new ArrayList<>();
        adapter = new CreditCardListAdapter(getContext(), R.layout.credit_card, list);
        gridView.setAdapter(adapter);

        fetchCreditCardDetails();
        fetchUserDetails();

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
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

        btnSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] newProfilePIC = picToArray(ivProfile);

                ContentValues contentValues = new ContentValues();
                contentValues.put(Table.USER_IMAGE, newProfilePIC);

                dbWrite.update(Table.TABLE_USER, contentValues, Table.USER_ID + "=?", new String[]{USER_ID});
                Toast.makeText(getContext(), "Profile Photo changed successfully!", Toast.LENGTH_LONG).show();
                btnSavePhoto.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSavePhoto.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                }

            }
        });

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getFullName, getMobileNumber;

                getFullName = fullname.getText().toString();
                getMobileNumber = mobilenumber.getText().toString();

                if (Cfullname.equals(getFullName) && Cmobile.equals(getMobileNumber)) {
                    Toast.makeText(getContext(), "Info is not changed", Toast.LENGTH_LONG).show();
                } else {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Table.USER_FULL_NAME, getFullName);
                    contentValues.put(Table.USER_MOBILE_NUMBER, getMobileNumber);

                    dbWrite.update(Table.TABLE_USER, contentValues, Table.USER_ID + "=?", new String[]{USER_ID});
                    Toast.makeText(getContext(), "Info Update Completed!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getAddress = billingaddress.getText().toString();

                if (Caddress.equals(getAddress)) {
                    Toast.makeText(getContext(), "Address not changed!", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Table.USER_BILLING_ADDRESS, getAddress);

                    dbWrite.update(Table.TABLE_USER, contentValues, Table.USER_ID + "=?", new String[]{USER_ID});
                    Toast.makeText(getContext(), "Address Update Completed!", Toast.LENGTH_LONG).show();
                    fetchUserDetails();
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cPassword, nPassword, rnPassword;

                cPassword = currentpassword.getText().toString();
                nPassword = newpassword.getText().toString();
                rnPassword = reenterpassword.getText().toString();

                if ("".equals(cPassword) || "".equals(nPassword) || "".equals(rnPassword)) {
                    Toast.makeText(getContext(), "You can not leave blank fields. Please fill and Try Again!", Toast.LENGTH_LONG).show();
                } else {
                    if (cPassword.equals(Cpass)) {
                        if (nPassword.equals(rnPassword)) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Table.USER_PASSWORD, nPassword);

                            dbWrite.update(Table.TABLE_USER, contentValues, Table.USER_ID + "=?", new String[]{USER_ID});
                            Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_LONG).show();
                            currentpassword.setText("");
                            newpassword.setText("");
                            reenterpassword.setText("");

                        } else {
                            Toast.makeText(getContext(), "New Password and Re-entered Password is not matching. Please Try Again!", Toast.LENGTH_LONG).show();
                            currentpassword.setText("");
                            newpassword.setText("");
                            reenterpassword.setText("");
                        }
                    } else {
                        Toast.makeText(getContext(), "Password Incorrect! Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        btnAddCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCreditCard_frag addCreditCard_frag = new addCreditCard_frag();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, addCreditCard_frag, null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    private void fetchCreditCardDetails() {
        list.clear();

        Cursor cursor1 = fetchCreditCard();

        while (cursor1.moveToNext()) {
            int CC_ID = cursor1.getInt(cursor1.getColumnIndex(Table.CC_ID));
            String USERNAME = cursor1.getString(cursor1.getColumnIndex(Table.CC_USERNAME));
            String CC_NUMBER = cursor1.getString(cursor1.getColumnIndex(Table.CC_NUMBER));
            String EXPIRED_DATE = cursor1.getString(cursor1.getColumnIndex(Table.CC_EXPIRED_DATE));
            String CCV_CODE = cursor1.getString(cursor1.getColumnIndex(Table.CC_CCV_CODE));

            list.add(new CreditCard(CC_ID, USERNAME, CC_NUMBER, EXPIRED_DATE, CCV_CODE));

        }

        adapter.notifyDataSetChanged();
    }

    private void fetchUserDetails() {
        Cursor cursor = fetchUser();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                Cname = cursor.getString(cursor.getColumnIndex(Table.USER_NAME));
                Cpass = cursor.getString(cursor.getColumnIndex(Table.USER_PASSWORD));
                Cfullname = cursor.getString(cursor.getColumnIndex(Table.USER_FULL_NAME));
                Cgender = cursor.getString(cursor.getColumnIndex(Table.USER_GENDER));
                Caddress = cursor.getString(cursor.getColumnIndex(Table.USER_BILLING_ADDRESS));
                Cmobile = cursor.getString(cursor.getColumnIndex(Table.USER_MOBILE_NUMBER));
                image = cursor.getBlob(cursor.getColumnIndex(Table.USER_IMAGE));

                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                ivProfile.setImageBitmap(bitmap);
                username.setText(Cname);
                fullname.setText(Cfullname);
                gender.setText(Cgender);
                mobilenumber.setText(Cmobile);
                address.setText(Caddress);
                billingaddress.setText(Caddress);
                billingaddress.selectAll();
            }
        }

    }


    private byte[] picToArray(ImageView imageView) {

        Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap(), 512, 512, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private Cursor fetchUser() {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_USER + " WHERE " + Table.USER_ID + "=?", new String[]{USER_ID});
        return cursor;
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
                ivProfile.setImageBitmap(bitmapImage);
                btnSavePhoto.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSavePhoto.setBackground(getResources().getDrawable(R.drawable.round_button));
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Cursor fetchCreditCard() {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_CC + " WHERE " + Table.CC_USERNAME + "=?", new String[]{USERNAME});
        return cursor;
    }
}
