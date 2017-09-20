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
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.Review;

import java.util.ArrayList;

/**
 * Created by Pasan .M. Semage on 2017-09-20.
 */

public class ReviewListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Review> reviewList;

    public ReviewListAdapter(Context context, int layout, ArrayList<Review> reviewList) {
        this.context = context;
        this.layout = layout;
        this.reviewList = reviewList;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView ivUserImage;
        TextView txtUserReview, txtUsername;
        RatingBar ratingFromUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.ivUserImage = (ImageView) row.findViewById(R.id.imageUser);
            holder.txtUsername = (TextView)row.findViewById(R.id.txtUserUserName);
            holder.txtUserReview = (TextView) row.findViewById(R.id.txtUsereview);
            holder.ratingFromUser = (RatingBar) row.findViewById(R.id.ratingFromUser);



            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Review review = reviewList.get(position);

        byte[] userImage = review.getUSER_IMAGE();
        Bitmap bitmap = BitmapFactory.decodeByteArray(userImage, 0, userImage.length);

        holder.ivUserImage.setImageBitmap(bitmap);
        holder.txtUsername.setText(review.getREVIEW_USERNAME());
        holder.ratingFromUser.setRating(Float.valueOf(review.getREVIEW_RATE()));
        holder.txtUserReview.setText(review.getREVIEW_REVIEW());


        return row;
    }
}
