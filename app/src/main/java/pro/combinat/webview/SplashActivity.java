package pro.combinat.webview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        if(isNetworkAvailable()) {
            //isConnected();
            //showWarning();
            //show();
        }*/

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("NETWORK_STATUS", isNetworkAvailable());
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
/*
    private boolean isConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else{
            connected = false;
        }
        return connected;
    }*/
public void show() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
    builder.setTitle("Важное сообщение!")
            .setMessage("Покормите кота!")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .setNegativeButton("ОК, иду на кухню",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
    AlertDialog alert = builder.create();
    alert.show();
}

private void showWarning(){
    AlertDialog.Builder builder;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder = new AlertDialog.Builder(getApplicationContext(),
                /*android.R.style.Theme_Material_Dialog_Alert*/R.style.warningDialog);
    } else {
        builder = new AlertDialog.Builder(getApplicationContext());
    }

    builder.setTitle("Delete entry")
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
}

   /*
    private void showDialogPleaseConnect(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_summary_scan_info);

        TextView scannedApps = (TextView) dialog.findViewById(R.id.txt_summary_scan_apps_num);
        TextView suspiciousApps = (TextView) dialog.findViewById(R.id.txt_summary_scan_suspicious_apps_num);
        TextView spywareList = (TextView) dialog.findViewById(R.id.txt_spyware_list);

        spywareList.setText(String.valueOf(getBlackListPackagesDefs().length));
        scannedApps.setText(String.valueOf(getApplist().size()));
        suspiciousApps.setText(String.valueOf(getBannedApplist().size()));

        Button btnDone = (Button) dialog.findViewById(R.id.btnOkReport);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getBannedApplist().size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), BannedAppsActivity.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }*/
}
