package com.openwallet.wallet.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.openwallet.wallet.R;
import com.openwallet.wallet.util.Fonts;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MasternodeActivity extends BaseWalletActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_masternode);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    public void onClick(View v) {
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.masternode_add, null);

// now set layout to dialog
//        dialogBuilder.setView(dialogView);

        new DialogBuilderLight(this)
                .setTitle(getString(R.string.masternode_add_title))
                .setMessage(R.string.masternode_add)
                .setPositiveButton(R.string.button_ok, null)
                .create().show();
    }

}
