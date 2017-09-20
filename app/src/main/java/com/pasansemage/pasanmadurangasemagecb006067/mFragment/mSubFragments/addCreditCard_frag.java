package com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;
import com.pasansemage.pasanmadurangasemagecb006067.mFragment.profile_frag;

public class addCreditCard_frag extends Fragment {

    DatabaseHelper databaseHelper;
    SQLiteDatabase dbRead, dbWrite;

    EditText txt1, txt2, txt3, txt4, txtValidThru, txtCCV;
    Button btnAddCC;
    String USERNAME;

    public addCreditCard_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_credit_card_frag, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        dbRead = databaseHelper.getReadableDatabase();
        dbWrite = databaseHelper.getWritableDatabase();

        txt1 = (EditText) view.findViewById(R.id.txt1);
        txt2 = (EditText) view.findViewById(R.id.txt2);
        txt3 = (EditText) view.findViewById(R.id.txt3);
        txt4 = (EditText) view.findViewById(R.id.txt4);
        txtValidThru = (EditText) view.findViewById(R.id.txtValidThru);
        txtCCV = (EditText) view.findViewById(R.id.txtCCSV);
        btnAddCC = (Button) view.findViewById(R.id.btnAddCC);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        USERNAME = sharedPreferences.getString("USERNAME", "");


        btnAddCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String CC_NUMBER = txt1.getText().toString() + "-" + txt2.getText().toString() + "-" + txt3.getText().toString() + "-" + txt4.getText().toString();
                    String EXPIREDDATE = txtValidThru.getText().toString();
                    String CCV = txtCCV.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Table.CC_USERNAME, USERNAME);
                    contentValues.put(Table.CC_NUMBER, CC_NUMBER);
                    contentValues.put(Table.CC_EXPIRED_DATE, EXPIREDDATE);
                    contentValues.put(Table.CC_CCV_CODE, CCV);

                    long result = dbWrite.insert(Table.TABLE_CC, null, contentValues);

                    if (result == -1) {
                        Toast.makeText(getContext(), "Credit Card is not added!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Credit Card is added!", Toast.LENGTH_LONG).show();

                        profile_frag profile_frag = new profile_frag();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, profile_frag, null);
                        fragmentTransaction.commit();
                    }

                }
            }
        });


        return view;
    }

    private boolean validate() {

        String CC_NUMBER = txt1.getText().toString() + "-" + txt2.getText().toString() + "-" + txt3.getText().toString() + "-" + txt4.getText().toString();

        if ("".equals(txt1.getText().toString()) || "".equals(txt2.getText().toString()) || "".equals(txt3.getText().toString()) || "".equals(txt4.getText().toString())) {
            Toast.makeText(getContext(), "Please complete Credit Card number!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (txt1.length() != 4 || txt2.length() != 4 || txt3.length() != 4 || txt4.length() != 4) {
            Toast.makeText(getContext(), "Please complete Credit Card number!", Toast.LENGTH_LONG).show();
            return false;
        }
        if ("".equals(txtCCV.getText().toString()) || txtCCV.length() != 3) {
            Toast.makeText(getContext(), "Please complete CCV Code!", Toast.LENGTH_LONG).show();
            return false;
        }

        if ("".equals(txtValidThru.getText().toString()) || txtValidThru.length() != 5) {
            Toast.makeText(getContext(), "Please complete Vaid Thru DATE!", Toast.LENGTH_LONG).show();
            return false;
        }

        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_CC + " WHERE " + Table.CC_NUMBER + "=? AND " + Table.CC_USERNAME + "=?", new String[]{CC_NUMBER, USERNAME});

        if (cursor.getCount() != 0) {
            Toast.makeText(getContext(), "Credit Card is Already in List", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
