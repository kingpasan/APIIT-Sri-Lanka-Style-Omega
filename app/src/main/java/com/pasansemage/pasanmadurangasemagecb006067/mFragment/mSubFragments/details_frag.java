package com.pasansemage.pasanmadurangasemagecb006067.mFragment.mSubFragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
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

    String PRODUCT_ID, USER_NAME, USER_ID, pPrice;
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
                pPrice = cursorProduct.getString(cursorProduct.getColumnIndex(Table.PRODUCT_PRICE));

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
        while (cursorSize.moveToNext()) {
            sizes = sizes + " [" + cursorSize.getString(cursorSize.getColumnIndex(Table.SIZE_SIZE)) + "] ";
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


        //review adding
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

        //product add to cart
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get available sizes
                String[] AvailableSizeArray;
                int s;
                int count = 0;

                Cursor getSizeCountcursor = fetchAvailableSize();
                s = getSizeCountcursor.getCount();

                AvailableSizeArray = new String[s];

                while (getSizeCountcursor.moveToNext()) {
                    AvailableSizeArray[count] = getSizeCountcursor.getString(getSizeCountcursor.getColumnIndex(Table.SIZE_SIZE));
                    count++;
                }


                //reference
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(1, 1, 1, 1);

                final EditText qty = new EditText(getContext());
                final Spinner selectSize = new Spinner(getContext(), Spinner.MODE_DIALOG);
                TextView txtQty = new TextView(getContext());
                TextView txtsize = new TextView(getContext());

                //Qty edit text
                qty.setHint("Enter Quantity");
                qty.setHintTextColor(getResources().getColor(R.color.colorAccent));
                qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                qty.setText("1");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    qty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                qty.setFocusable(true);
                qty.setTextColor(getResources().getColor(R.color.colorAccent));
                qty.setLayoutParams(layoutParams);

                //txt textview
                txtQty.setText("Enter Quantity :");
                txtsize.setText("Select Size");
                txtQty.setTextColor(getResources().getColor(R.color.colorAccent));
                txtsize.setTextColor(getResources().getColor(R.color.colorAccent));
                txtQty.setGravity(Gravity.CENTER);
                txtsize.setGravity(Gravity.CENTER);
                txtQty.setPadding(0, 5, 0, 5);
                txtsize.setPadding(0, 5, 0, 5);


                //selectsize spinner
                ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item, AvailableSizeArray);
                selectSize.setAdapter(adapter);
                selectSize.setGravity(Gravity.CENTER);
                selectSize.setLayoutParams(layoutParams);

                //add into layout
                layout.addView(txtQty);
                layout.addView(qty);
                layout.addView(txtsize);
                layout.addView(selectSize);


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add to Cart");
                builder.setView(layout);
                builder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String getSize, getQty;

                        getQty = qty.getText().toString();
                        getSize = selectSize.getSelectedItem().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Table.CART_USER_ID, USER_ID);
                        contentValues.put(Table.CART_PRODUCT_ID, PRODUCT_ID);
                        contentValues.put(Table.CART_PRODUCT_QTY, getQty);
                        contentValues.put(Table.CART_PRODUCT_SIZE, getSize);

                        long result = dbWrite.insert(Table.TABLE_CART, null, contentValues);

                        if (result == -1) {
                            Toast.makeText(getContext(), "Product is not added to Cart", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Product is added to Cart", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getAvailable Size
                String[] AvailableSizeArray;
                int s;
                int count = 0;

                Cursor getSizeCountcursor = fetchAvailableSize();
                s = getSizeCountcursor.getCount();

                AvailableSizeArray = new String[s];

                while (getSizeCountcursor.moveToNext()) {
                    AvailableSizeArray[count] = getSizeCountcursor.getString(getSizeCountcursor.getColumnIndex(Table.SIZE_SIZE));
                    count++;
                }

                //get Credit card
                String[] CreditCardArray;
                int si;
                int countCC = 0;

                Cursor getCreditCardscusor = fetchCreditCards();
                si = getCreditCardscusor.getCount();

                CreditCardArray = new String[si];

                while (getCreditCardscusor.moveToNext()) {
                    CreditCardArray[countCC] = getCreditCardscusor.getString(getCreditCardscusor.getColumnIndex(Table.CC_NUMBER));
                    countCC++;
                }

                //Check credit card is available

                if (getCreditCardscusor.getCount() == 0) {
                    //credit card is not available

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("No Credit Card");
                    builder.setTitle("Please add credit card to buy item from style omega app!");
                    builder.setPositiveButton("Got It!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //reference
                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CENTER);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(1, 1, 1, 1);

                    final EditText qty = new EditText(getContext());
                    final Spinner selectSize = new Spinner(getContext(), Spinner.MODE_DIALOG);
                    final Spinner selectCreditCard = new Spinner(getContext(), Spinner.MODE_DIALOG);
                    TextView txtQty = new TextView(getContext());
                    TextView txtsize = new TextView(getContext());
                    TextView txtCC = new TextView(getContext());

                    //Qty edit text
                    qty.setHint("Enter Quantity");
                    qty.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                    qty.setText("1");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        qty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    qty.setFocusable(true);
                    qty.setTextColor(getResources().getColor(R.color.colorAccent));
                    qty.setLayoutParams(layoutParams);

                    //txt textview
                    txtQty.setText("Enter Quantity :");
                    txtsize.setText("Select Size");
                    txtCC.setText("Select Credit Card");
                    txtCC.setTextColor(getResources().getColor(R.color.colorAccent));
                    txtQty.setTextColor(getResources().getColor(R.color.colorAccent));
                    txtsize.setTextColor(getResources().getColor(R.color.colorAccent));
                    txtCC.setGravity(Gravity.CENTER);
                    txtQty.setGravity(Gravity.CENTER);
                    txtsize.setGravity(Gravity.CENTER);
                    txtCC.setPadding(0, 5, 0, 5);
                    txtQty.setPadding(0, 5, 0, 5);
                    txtsize.setPadding(0, 5, 0, 5);


                    //selectsize spinner
                    ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item, AvailableSizeArray);
                    selectSize.setAdapter(adapter);
                    selectSize.setGravity(Gravity.CENTER);
                    selectSize.setLayoutParams(layoutParams);

                    //selectCredit Card spinner
                    ArrayAdapter adapter1 = new ArrayAdapter(getContext(), R.layout.spinner_item, CreditCardArray);
                    selectCreditCard.setAdapter(adapter1);
                    selectCreditCard.setGravity(Gravity.CENTER);
                    selectCreditCard.setLayoutParams(layoutParams);

                    //add into layout
                    layout.addView(txtQty);
                    layout.addView(qty);
                    layout.addView(txtsize);
                    layout.addView(selectSize);
                    layout.addView(txtCC);
                    layout.addView(selectCreditCard);


                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Buy Now");
                    builder.setView(layout);
                    builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String getSize, getQty, getCC;

                            getQty = qty.getText().toString();
                            getSize = selectSize.getSelectedItem().toString();
                            getCC = selectCreditCard.getSelectedItem().toString();

                            //To Purchase hitory table
                            ContentValues purchase = new ContentValues();
                            purchase.put(Table.PURCHASE_USER_ID, USER_ID);
                            purchase.put(Table.PURCHASE_PRODUCT_ID, PRODUCT_ID);
                            purchase.put(Table.PURCHASE_PRODUCT_QTY, getQty);
                            purchase.put(Table.PURCHASE_PRODUCT_SIZE, getSize);
                            purchase.put(Table.PURCHASE_PRODUCT_STATUS, "PAID");

                            long result1 = dbWrite.insert(Table.TABLE_PURCHASE, null, purchase);

                            //To Transaction Table
                            ContentValues transaction = new ContentValues();
                            transaction.put(Table.TRANSACTION_USER_ID, USER_ID);
                            transaction.put(Table.TRANSACTION_CC_NUMBER, getCC);
                            transaction.put(Table.TRANSACTION_TOTAL, pPrice);

                            long result2 = dbWrite.insert(Table.TABLE_TRANSACTION, null, transaction);


                            //To Product Tranaction Table

                            Cursor transactionID = fetchTransactionID(getCC);
                            transactionID.moveToNext();
                            String TRANS_ID = transactionID.getString(transactionID.getColumnIndex(Table.TRANSACTION_ID));

                            ContentValues ptrans = new ContentValues();
                            ptrans.put(Table.P_T_TRANSACTION_ID, TRANS_ID);
                            ptrans.put(Table.P_T_PRODUCT_ID, TRANS_ID);
                            ptrans.put(Table.P_T_QTY, TRANS_ID);
                            ptrans.put(Table.P_T_SIZE, TRANS_ID);

                            long result3 = dbWrite.insert(Table.TABLE_PRODUCT_TRANSACTION, null, ptrans);

                            if (result1 == -1 || result2 == -1 || result3 == -1) {
                                Toast.makeText(getContext(), "Product transaction is not complete", Toast.LENGTH_LONG).show();
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setTitle("Style Omega");
                                builder1.setMessage("Payment Received complete. Shipment will arrived in 2 week days! You can view shipment details on order history");
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            }


                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


            }
        });


        return view;
    }

    private Cursor fetchTransactionID(String getCC) {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_TRANSACTION + " WHERE " + Table.TRANSACTION_USER_ID + "=? AND " + Table.TRANSACTION_CC_NUMBER + "=? AND " + Table.TRANSACTION_TOTAL + "=?", new String[]{USER_ID, getCC, pPrice});
        return cursor;
    }


    private Cursor fetchCreditCards() {
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + Table.TABLE_CC + " WHERE " + Table.CC_USERNAME + "=?", new String[]{USER_NAME});
        return cursor;
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
