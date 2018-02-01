package pro.combinat.webview;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import pro.combinat.utils.AESHelper;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String app_url = getString(R.string.app_url);
/*
        if (this.getIntent().getBooleanExtra("NETWORK_STATUS", false))
            show();
*/
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.google.com");

        //setContentView(R.layout.activity_main);

        //**********encrypt
        String seedValue = "I AM UNBREAKABLE";
        //String MESSAGE = "No one can read this message without decrypting me.";

        try {
            String encryptedData = AESHelper.encrypt(seedValue, app_url);
            Log.v("EncryptDecrypt", "Encoded String " + encryptedData);
            String decryptedData = AESHelper.decrypt(seedValue, encryptedData);
            Log.v("EncryptDecrypt", "Decoded String " + decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
        builder.setTitle("Невозможно установить соединение!")
                .setMessage("Проверьте подключение")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

