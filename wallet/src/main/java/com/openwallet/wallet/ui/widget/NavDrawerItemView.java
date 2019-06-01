package com.openwallet.wallet.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openwallet.core.wallet.WalletAccount;
import com.openwallet.wallet.Constants;
import com.openwallet.wallet.R;
import com.openwallet.wallet.util.WalletUtils;

/**
 * @author John L. Jegutanis
 */
public class NavDrawerItemView extends LinearLayout implements Checkable {
    private final TextView title;
    private final ImageView icon;
    private final View view;

    private boolean isChecked = false;

    public NavDrawerItemView(Context context, Boolean theme) {
        super(context);
        if (theme) {
            view = LayoutInflater.from(context).inflate(R.layout.nav_drawer_item, this, true);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.nav_drawer_item_n, this, true);
        }
        title = (TextView) findViewById(R.id.item_text);
        icon = (ImageView) findViewById(R.id.item_icon);
    }

    public void setData(String titleStr, int iconRes) {
        title.setText(titleStr);
        icon.setImageResource(iconRes);
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;

        if (isChecked) {
//            view.setBackgroundResource(R.color.primary_100);
            view.setBackgroundResource(0);
        } else {
            view.setBackgroundResource(0);
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
