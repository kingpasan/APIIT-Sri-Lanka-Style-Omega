package com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mAdapter.ProductListAdapter;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.Product;

import java.util.ArrayList;


public class category_frag extends Fragment {

    ArrayList<Product> list;
    ProductListAdapter adapter = null;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    GridView gridView;
    TextView textView;


    public category_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_frag, container, false);

        //reference
        gridView = (GridView) view.findViewById(R.id.gridViewCategory);
        textView = (TextView) view.findViewById(R.id.txtCategory);

        databaseHelper = new DatabaseHelper(getContext());
        db = databaseHelper.getReadableDatabase();


        list = new ArrayList<>();
        adapter = new ProductListAdapter(view.getContext(), R.layout.product_list, list);
        gridView.setAdapter(adapter);

        Bundle bundle = getArguments();
        String CATEGORY = bundle.get("CATEGORY").toString();

        textView.setText(CATEGORY);

        Cursor cursor = fetchData(CATEGORY);
        list.clear();

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(Table.PRODUCT_ID));
            String title = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_TITLE));
            String category = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_CATEGORY));
            String description = cursor.getString(cursor.getColumnIndex(Table.PRODUCT_DESCRIPTION));
            String price = "Rs." + cursor.getString(cursor.getColumnIndex(Table.PRODUCT_PRICE))+".00";
            byte[] image = cursor.getBlob(cursor.getColumnIndex(Table.PRODUCT_IMAGE));

            list.add(new Product(id, title, category, description, price, image));
        }

        adapter.notifyDataSetChanged();


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

        return view;
    }

    private Cursor fetchData(String CATEGORY) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table.TABLE_PRODUCT + " WHERE " + Table.PRODUCT_CATEGORY + "=?", new String[]{CATEGORY});
        return cursor;
    }

}
