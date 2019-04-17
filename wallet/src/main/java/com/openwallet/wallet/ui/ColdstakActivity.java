package com.openwallet.wallet.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.openwallet.wallet.R;
import com.openwallet.wallet.util.Fonts;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ColdstakActivity extends BaseWalletActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coldstak);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    public void onClick(View v) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.coin_already_added_title))
                .setMessage(R.string.coin_already_added)
                .setPositiveButton(R.string.button_ok, null)
                .create().show();
    }
}
