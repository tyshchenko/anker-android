package com.openwallet.wallet.ui.adaptors;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.openwallet.wallet.R;
import com.openwallet.wallet.listitem;

import java.util.ArrayList;

public class ListitemAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<listitem> Lstarray;

    public ListitemAdapter(Activity context, ArrayList<listitem> Lstarray){
        super(context, R.layout.listitem_row , Lstarray);
        this.context=context;
        this.Lstarray = Lstarray;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listitem_row, null,true);


        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);


        nameTextField.setText(Lstarray.get(position).Name);
        infoTextField.setText(Lstarray.get(position).Info);
        imageView.setImageResource(Lstarray.get(position).Image);

        return rowView;

    };
}
