package com.pasansemage.pasanmadurangasemagecb006067.mAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.Product;

import java.util.ArrayList;

/**
 * Created by Pasan .M. Semage on 2017-09-19.
 */

public class ProductListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Product> productList;

    public ProductListAdapter(Context context, int layout, ArrayList<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView ivProduct;
        TextView txtProductTitle, txtProductPrice, txtProductCategory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.ivProduct = (ImageView) row.findViewById(R.id.imageProduct);
            holder.txtProductTitle = (TextView) row.findViewById(R.id.txtProductTitle);
            holder.txtProductPrice = (TextView) row.findViewById(R.id.txtProductPrice);
            holder.txtProductCategory = (TextView)row.findViewById(R.id.txtProductCategory);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Product product = productList.get(position);

        holder.txtProductTitle.setText(product.getPRODUCT_TITLE());
        holder.txtProductPrice.setText(product.getPRODUCT_PRICE());
        holder.txtProductCategory.setText(product.getPRODUCT_CATEGORY());

        byte[] productImage = product.getPRODUCT_IMAGE();
        Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
        holder.ivProduct.setImageBitmap(bitmap);

        return row;
    }
}
