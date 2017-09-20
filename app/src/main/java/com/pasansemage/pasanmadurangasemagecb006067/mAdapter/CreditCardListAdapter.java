package com.pasansemage.pasanmadurangasemagecb006067.mAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasansemage.pasanmadurangasemagecb006067.R;
import com.pasansemage.pasanmadurangasemagecb006067.mHelper.CreditCard;

import java.util.ArrayList;

/**
 * Created by Pasan .M. Semage on 2017-09-20.
 */

public class CreditCardListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<CreditCard> list;

    public CreditCardListAdapter(Context context, int layout, ArrayList<CreditCard> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtCCNumber, txtCCExpiredDate;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.imageView = (ImageView)row.findViewById(R.id.imageCreditCard);
            holder.txtCCNumber = (TextView)row.findViewById(R.id.txtCreditCardNumber);
            holder.txtCCExpiredDate = (TextView)row.findViewById(R.id.txtCreditCardExpired);

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        CreditCard creditCard = list.get(position);
        holder.txtCCNumber.setText(creditCard.getCC_NUMBER());
        holder.txtCCExpiredDate.setText(creditCard.getEXPIRED_DATE());


        return row;
    }
}
