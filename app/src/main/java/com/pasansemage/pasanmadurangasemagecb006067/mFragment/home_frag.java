package com.pasansemage.pasanmadurangasemagecb006067.mFragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mAdapter.ProductListAdapter;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;
import com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments.addProduct_frag;
import com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments.category_frag;
import com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments.details_frag;
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.Product;
import com.pasansemage.pasanmadurangasemagecb006067.mWidgets.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.List;

public class home_frag extends Fragment {

    ExpandableHeightGridView gridView;
    ArrayList<Product> list;
    ProductListAdapter adapter = null;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    TextView adminPanel;
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
        btnAddProduct = (Button) view.findViewById(R.id.btnAddProduct);
        btnInquiries = (Button) view.findViewById(R.id.btnInquiries);
        btnOrderComplete = (Button) view.findViewById(R.id.btnOrderComplete);
        btnMen = (Button) view.findViewById(R.id.btnMen);
        btnWomen = (Button) view.findViewById(R.id.btnWomen);
        btnKids = (Button) view.findViewById(R.id.btnKids);
        btnJewlery = (Button) view.findViewById(R.id.btnJewelry);
        btnFasion = (Button) view.findViewById(R.id.btnFashion);
        cardView = (CardView) view.findViewById(R.id.crdView);
        adminPanel = (TextView) view.findViewById(R.id.txtAdminPanel);


        gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridViewAll);
        gridView.setExpanded(true);
        gridView.setFocusable(false);


        list = new ArrayList<>();
        adapter = new ProductListAdapter(view.getContext(), R.layout.product_list, list);
        gridView.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(getContext());
        db = databaseHelper.getReadableDatabase();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String usertype = sharedPreferences.getString("USERTYPE", "");


        if ("Customer".equals(usertype)) {
            btnAddProduct.setVisibility(view.GONE);
            btnInquiries.setVisibility(view.GONE);
            btnOrderComplete.setVisibility(view.GONE);
            cardView.setVisibility(view.GONE);
            adminPanel.setVisibility(view.GONE);
        }

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct_frag addProduct_frag = new addProduct_frag();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, addProduct_frag, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("PRODUCT_ID", list.get(position).getPRODUCT_ID());

                details_frag details_frag = new details_frag();
                details_frag.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, details_frag, null);
                fragmentTransaction.commit();

            }
        });


        Cursor cursor = fetchData();

        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Table.PRODUCT_ID));
            String title = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_TITLE));
            String category = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_CATEGORY));
            String description = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_DESCRIPTION));
            String price = "Rs." + cursor.getString(cursor.getColumnIndex(Table.PRODUCT_PRICE)) + ".00";
            byte[] image = cursor.getBlob(cursor.getColumnIndex(Table.PRODUCT_IMAGE));

            list.add(new Product(id, title, category, description, price, image));
        }

        adapter.notifyDataSetChanged();

        btnMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Men");
            }
        });

        btnWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Women");
            }
        });

        btnKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Kids");
            }
        });

        btnFasion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Fashion Accessories");
            }
        });

        btnJewlery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Jewelry and Watches");
            }
        });


        return view;
    }

    private Cursor fetchData() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table.TABLE_PRODUCT, null);
        return cursor;
    }

    private void openCategory(String CATEGORY) {

        Bundle bundle = new Bundle();
        bundle.putString("CATEGORY", CATEGORY);

        category_frag category_frag = new category_frag();
        category_frag.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,category_frag,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

}
