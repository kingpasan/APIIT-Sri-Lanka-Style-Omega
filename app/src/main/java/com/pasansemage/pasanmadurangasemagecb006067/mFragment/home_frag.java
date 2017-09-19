package com.pasansemage.pasanmadurangasemagecb006067.mFragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mAdapter.ProductListAdapter;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.Product;

import java.util.ArrayList;

public class home_frag extends Fragment {

    GridView gridView;
    ArrayList<Product> list;
    ProductListAdapter adapter = null;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    Button btnAddProduct, btnInquiries, btnOrderComplete, btnMen, btnWomen, btnKids, btnJewlery, btnFasion;
    CardView cardView;

    public home_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_frag, container, false);

        //init
        btnAddProduct = (Button)view.findViewById(R.id.btnAddProduct);
        btnInquiries = (Button)view.findViewById(R.id.btnInquiries);
        btnOrderComplete = (Button)view.findViewById(R.id.btnOrderComplete);
        btnMen = (Button)view.findViewById(R.id.btnMen);
        btnWomen = (Button)view.findViewById(R.id.btnWomen);
        btnKids = (Button)view.findViewById(R.id.btnKids);
        btnJewlery = (Button)view.findViewById(R.id.btnJewelry);
        btnFasion = (Button)view.findViewById(R.id.btnFashion);
        cardView = (CardView)view.findViewById(R.id.crdView);

        gridView = (GridView) view.findViewById(R.id.gridViewAll);
        list = new ArrayList<>();
        adapter = new ProductListAdapter(view.getContext(), R.layout.product_list, list);
        gridView.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(getContext());
        db = databaseHelper.getReadableDatabase();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String usertype = sharedPreferences.getString("USERTYPE","");


        if ("Customer".equals(usertype)){
            btnAddProduct.setVisibility(view.GONE);
            btnInquiries.setVisibility(view.GONE);
            btnOrderComplete.setVisibility(view.GONE);
            cardView.setVisibility(view.GONE);
        }



        Cursor cursor = fetchData();

        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Table.PRODUCT_ID));
            String title = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_TITLE));
            String category = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_CATEGORY));
            String description = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_PRICE));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(Table.PRODUCT_IMAGE));

            list.add(new Product(id, title, category, description, price, image));
        }

        adapter.notifyDataSetChanged();


        return view;
    }

    private Cursor fetchData() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table.TABLE_PRODUCT, null);
        return cursor;
    }

}
