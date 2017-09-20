package com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mAdapter.ReviewListAdapter;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.DatabaseHelper;
import com.pasansemage.pasanmadurangasemagecb006067.mDatabase.Table;
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.Review;
import com.pasansemage.pasanmadurangasemagecb006067.mWidgets.ExpandableHeightGridView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class details_frag extends Fragment {

    String PRODUCT_ID, USER_NAME, USER_ID;
    byte[] userImage;
    Bundle bundle;
    SharedPreferences sharedPreferences;
    DatabaseHelper databaseHelper;
    SQLiteDatabase dbRead, dbWrite;
    ArrayList<Review> list;
    ReviewListAdapter adapter = null;


    ExpandableHeightGridView gridViewReview;
    ImageView ivProduct;
    TextView title, price, size, desc, reviewinfo, infoWriteReview;
    EditText review;
    Button btnAddToCart, btnBuyNow, btnAddReview;
    RatingBar ratingProduct, userRatingProduct;


    public details_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_details_frag, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        dbRead = databaseHelper.getReadableDatabase();
        dbWrite = databaseHelper.getWritableDatabase();

        //init
        ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
        title = (TextView) view.findViewById(R.id.txtPTitile);
        price = (TextView) view.findViewById(R.id.txtPPrice);
        size = (TextView) view.findViewById(R.id.txtPSize);
        desc = (TextView) view.findViewById(R.id.txtPDescription);
        reviewinfo = (TextView) view.findViewById(R.id.txtReviewInfo);
        infoWriteReview = (TextView) view.findViewById(R.id.txtInfoWriteReview);
        review = (EditText) view.findViewById(R.id.txtPReview);
        btnAddReview = (Button) view.findViewById(R.id.btnAddReview);
        btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);
        btnBuyNow = (Button) view.findViewById(R.id.btnBuyNow);
        ratingProduct = (RatingBar) view.findViewById(R.id.ratingProduct);
        userRatingProduct = (RatingBar) view.findViewById(R.id.userRatingProduct);
        gridViewReview = (ExpandableHeightGridView) view.findViewById(R.id.gridViewReview);

        gridViewReview.setExpanded(true);
        gridViewReview.setFocusable(false);

        list = new ArrayList<>();
        adapter = new ReviewListAdapter(view.getContext(), R.layout.review_list, list);
        gridViewReview.setAdapter(adapter);

        bundle = getArguments();
        PRODUCT_ID = bundle.get("PRODUCT_ID").toString();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        USER_NAME = sharedPreferences.getString("USERNAME", "");
        USER_ID = sharedPreferences.getString("USERID", "");

        //get and set product details
        Cursor cursorProduct = fetchProductData();

        if (cursorProduct != null) {
            if (cursorProduct.getCount() > 0) {
                cursorProduct.moveToNext();
                byte[] image = cursorProduct.getBlob(cursorProduct.getColumnIndex(Table.PRODUCT_IMAGE));
                String ptitle = cursorProduct.getString(cursorProduct.getColumnIndex(Table.PRODUCT_TITLE));
                String pdesc = cursorProduct.getString(cursorProduct.getColumnIndex(Table.PRODUCT_DESCRIPTION));
                String pPrice = cursorProduct.getString(cursorProduct.getColumnIndex(Table.PRODUCT_PRICE));

                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                ivProduct.setImageBitmap(bitmap);
                title.setText(ptitle);
                desc.setText(pdesc);
                price.setText("Rs." + pPrice + ".00");
            }
        }

        //get available size
        Cursor cursorSize = fetchAvailableSize();
        String sizes = "";
        while (cursorSize.moveToNext()){
            sizes = sizes +" ["+ cursorSize.getString(cursorSize.getColumnIndex(Table.SIZE_SIZE)) + "] ";
        }

        size.setText("Available Size : " + sizes);


        //set review adapter
        list.clear();

        Cursor cursorReview = fetchReviewData();

        if (cursorReview.getCount() == 0) {
            reviewinfo.setVisibility(view.VISIBLE);
        }

        while (cursorReview.moveToNext()) {
            int id = cursorReview.getInt(cursorReview.getColumnIndex(Table.REVIEW_ID));
            String username = cursorReview.getString(cursorReview.getColumnIndex(Table.REVIEW_USERNAME));
            String productID = cursorReview.getString(cursorReview.getColumnIndex(Table.REVIEW_PRODUCT_ID));
            String rate = cursorReview.getString(cursorReview.getColumnIndex(Table.REVIEW_RATE));
            String review = cursorReview.getString(cursorReview.getColumnIndex(Table.REVIEW_REVIEW));

            Cursor cursorUserImage = fetchUserImage(username);
            if (cursorUserImage != null) {
                if (cursorUserImage.getCount() > 0) {
                    cursorUserImage.moveToNext();
                    userImage = cursorUserImage.getBlob(cursorUserImage.getColumnIndex(Table.USER_IMAGE));
                }
            }

            list.add(new Review(id, username, productID, rate, review, userImage));


        }

        adapter.notifyDataSetChanged();

        //check user review
        Cursor cursorCheckUserReview = fetchCheckUserReviews();

        if (cursorCheckUserReview.getCount() == 0) {
            userRatingProduct.setIsIndicator(false);
            review.setEnabled(true);
            review.setHintTextColor(getResources().getColor(R.color.colorAccent));
            review.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            btnAddReview.setEnabled(true);
            btnAddReview.setBackgroundResource(R.drawable.round_button);

        } else {
            infoWriteReview.setVisibility(view.VISIBLE);
        }


        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rate = userRatingProduct.getRating();
                String userReview = review.getText().toString();

                if (rate == 0.0) {
                    Toast.makeText(getContext(), "Please Select a Rate!", Toast.LENGTH_LONG).show();
                } else if (userReview.equals("")) {
                    Toast.makeText(getContext(), "Please Write Review!", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Table.REVIEW_USERNAME, USER_NAME);
                    contentValues.put(Table.REVIEW_PRODUCT_ID, PRODUCT_ID);
                    contentValues.put(Table.REVIEW_RATE, rate);
                    contentValues.put(Table.REVIEW_REVIEW, userReview);

                    long result = dbWrite.insert(Table.TABLE_REVIEW, null, contentValues);

                    if (result == -1) {
                        Toast.makeText(getContext(), "Review not added!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Review added successfully!", Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                        userRatingProduct.setIsIndicator(true);
                        review.setEnabled(false);
                        btnAddReview.setEnabled(false);
                    }
                }
            }
        });


        return view;
    }

    private Cursor fetchUserImage(String username) {
        Cursor cursor = dbRead.rawQuery("SELECT " + Table.USER_IMAGE + " FROM " + Table.TABLE_USER + " WHERE " + Table.USER_NAME + "=?", new String[]{username});
        return cursor;
    }


    private Cursor fetchProductData() {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_PRODUCT + " WHERE " + Table.PRODUCT_ID + "=?", new String[]{PRODUCT_ID});
        return cursor;
    }

    private Cursor fetchReviewData() {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_REVIEW + " WHERE " + Table.REVIEW_PRODUCT_ID + "=?", new String[]{PRODUCT_ID});
        return cursor;
    }

    private Cursor fetchCheckUserReviews() {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_REVIEW + " WHERE " + Table.REVIEW_PRODUCT_ID + "=? AND " + Table.REVIEW_USERNAME + "=?", new String[]{PRODUCT_ID, USER_NAME});
        return cursor;
    }

    private Cursor fetchAvailableSize() {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_SIZE + " WHERE " + Table.SIZE_PRODUCT_ID + "=?", new String[]{PRODUCT_ID});
        return cursor;
    }

}
