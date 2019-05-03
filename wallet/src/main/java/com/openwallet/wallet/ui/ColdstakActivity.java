package com.openwallet.wallet.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.openwallet.core.coins.CoinID;
import com.openwallet.core.coins.CoinType;
import com.openwallet.core.coins.Value;
import com.openwallet.core.messages.TxMessage;
import com.openwallet.core.wallet.AbstractAddress;
import com.openwallet.core.wallet.SendRequest;
import com.openwallet.core.wallet.Wallet;
import com.openwallet.core.wallet.WalletAccount;
import com.openwallet.wallet.R;
import com.openwallet.wallet.WalletApplication;
import com.openwallet.wallet.listitem;
import com.openwallet.wallet.ui.adaptors.ListitemAdapter;

import org.bitcoinj.crypto.KeyCrypter;
import org.bitcoinj.crypto.KeyCrypterException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.openwallet.core.Preconditions.checkNotNull;


public class ColdstakActivity extends BaseWalletActivity {
    private WalletApplication application;
    private String password = "";
    private Context context;
    ListitemAdapter coldstaks;
    ListView listView;
    private ArrayList<listitem> coldstackarray  = new ArrayList<listitem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coldstak);
        ButterKnife.bind(this);

        SharedPreferences prefs = getSharedPreferences("ankersettings", Context.MODE_PRIVATE);
        String jsonText = prefs.getString("ankersavins", null);
        if (jsonText != null) {
            try {
                JSONArray jarr = new JSONArray(jsonText);
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject o = (JSONObject) jarr.get(i);
                    coldstackarray.add(new listitem(o.getString("Name"), o.getString("Info"), o.getInt("Image")));
                }
            } catch (Exception e) {
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        coldstaks = new ListitemAdapter(this, coldstackarray);
        //listView = (ListView) findViewById(R.id.coldstak_list);
        //listView.setAdapter(coldstaks);

    }

    public void onClick(View v) {
		final CoinType type = CoinID.ANKCOIN_MAIN.getCoinType();
        application = getWalletApplication();
        List<WalletAccount> sendFromAccounts = application.getAccounts(type);
        context =this;
		if (sendFromAccounts.size() == 1) {
            final WalletAccount account = sendFromAccounts.get(0);
			Value balance = account.getBalance();
            final Value amount = CoinID.ANKCOIN_MAIN.getCoinType().value(100000000000L);
			if (! balance.isLessThan(amount) ) {
				new AlertDialog.Builder(this)
						.setTitle(getString(R.string.coldstak_add_title))
                        .setMessage(R.string.coldstak_add)
						.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
								    //getnewaddress
                                    AbstractAddress receiveAddress =account.getReceiveAddress();

								    //create transaction
                                    //TxMessage txMessage = type.getMessagesFactory().createPublicMessage("AnkerSavings");
                                    try {
                                        final SendRequest sendRequest;
                                        sendRequest = account.getSendToRequest(receiveAddress, amount);
                                        //sendRequest.txMessage = txMessage;
                                        sendRequest.signTransaction = false;
                                        account.completeTransaction(sendRequest);
                                        final Wallet wallet = application.getWallet();
                                        if (wallet.isEncrypted()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("Unlock your Wallet");
                                            final EditText input = new EditText(context);
                                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                            builder.setView(input);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    application.maybeConnectAccount(account);
                                                    password = input.getText().toString();
                                                    KeyCrypter crypter = checkNotNull(wallet.getKeyCrypter());
                                                    try {
                                                        sendRequest.aesKey = crypter.deriveKey(password);
                                                        try {
                                                            CompleteTx(sendRequest, context, account);
                                                        } catch (WalletAccount.WalletAccountException e) {
                                                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    } catch (KeyCrypterException e) {
                                                        showPasswordRetryDialog();
                                                    }
                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.show();
                                        } else {
                                            application.maybeConnectAccount(account);
                                            CompleteTx(sendRequest, context, account);
                                        }
                                    } catch (WalletAccount.WalletAccountException e) {
                                        Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
								}
							})
						.setNegativeButton(R.string.button_cancel, null)
						.create().show();
			} else {
				new AlertDialog.Builder(this)
						.setTitle(getString(R.string.coldstak_not1000_title))
                        .setMessage(R.string.coldstak_not1000)
						.setPositiveButton(R.string.button_ok, null)
						.create().show();
			}
		} else {
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.not_haveank_title))
					.setMessage(R.string.not_haveank)
					.setPositiveButton(R.string.button_ok, null)
					.create().show();
		}
    }
    private void CompleteTx(SendRequest sendRequest, Context context, WalletAccount account)  throws WalletAccount.WalletAccountException{

        sendRequest.signTransaction = true;
        account.completeAndSignTx(sendRequest);
        try {
            if (!account.broadcastTxSync(sendRequest.tx)) {
                account.broadcastTx(sendRequest.tx);
               // throw new Exception("Error broadcasting transaction: " + sendRequest.tx.getHashAsString());
            }
            //sendRequest.tx.getHash();


            //add tolist
            coldstackarray.add(new listitem("AnkerSavings", sendRequest.tx.getHash().toString(), R.drawable.spinner));
            saveColdList();
            coldstaks.notifyDataSetChanged();
            //wait one confirm
            //sendmessage to server
            //wait and check for active
            new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.coldstak_ok_title))
                    .setMessage(R.string.coldstak_ok)
                    .setPositiveButton(R.string.button_ok, null)
                    .create().show();
            Toast.makeText(context,  "Complete", Toast.LENGTH_LONG).show();
        } catch (Exception e) {  Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_LONG).show(); }
    }

    private void showPasswordRetryDialog() {
        DialogBuilder.warn(context, R.string.unlocking_wallet_error_title)
                .setMessage(R.string.unlocking_wallet_error_detail)
                .setPositiveButton(R.string.button_retry, null)
                .create().show();
    }
    private void saveColdList() {
        JSONArray jsonArray = new JSONArray();
        for (int i=0; i < coldstackarray.size(); i++) {
            jsonArray.put(coldstackarray.get(i).getJSONObject());
        }
        SharedPreferences.Editor editor = getSharedPreferences("ankersettings", MODE_PRIVATE).edit();
        editor.putString("ankersavins", jsonArray.toString());
        editor.apply();
    }

}
