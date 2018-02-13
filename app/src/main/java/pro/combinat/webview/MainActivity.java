package pro.combinat.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.concurrent.ExecutionException;

import javax.crypto.SecretKey;

import pro.combinat.utils.AESHelper;
import pro.combinat.utils.Crypto;
import pro.combinat.utils.HttpGetRequest;

import static android.content.ContentValues.TAG;

public class MainActivity  extends Activity {

    private WebView webView;
    private String url;
    private Encryptor encryptor;

    final String password = "I AM UNBREAKABLE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encryptor = PADDING_ENCRYPTOR;

        boolean connected = this.getIntent().getBooleanExtra("NETWORK_STATUS", false);

        if (!connected) {
            findViewById(R.id.textView).setVisibility(View.VISIBLE);
            showWarning();
        } else {
            findViewById(R.id.webView).setVisibility(View.VISIBLE);

            //String encrypted = encryptor.encrypt(url123, password);
            //String decrypted = encryptor.decrypt(encrypted, password);
            //String encrypted = Crypto.encrypt(plaintext, key, null);
            //String url1 = AESHelper.getUrl(getString(R.string.app_url));
            String encrypted = getString(R.string.app_url_enc);
            String urlMain = encryptor.decrypt(encrypted, password);
            //************************************
            /*
            new CryptoTask() {

                @Override
                protected String doCrypto() {
                    return encryptor.encrypt(url123, password);
                }

                @Override
                protected void updateUi(String ciphertext) {
                    String rawKeyText = encryptor.getRawKey();
                    String encryptedText = ciphertext;
                }

                @Override
                protected String doDeCrypto() {
                    return encryptor.decrypt(url123, password);
                }

            }.execute();*/
            //*************************************
            //String urlMain = "https://yandex.ru/";

            if(getRequestResult(urlMain).equalsIgnoreCase("OK")){
                url = getString(R.string.x_url);
            } else {
                url = getString(R.string.y_url);
            }

            webView = (WebView) findViewById(R.id.webView);
            webView.setWebViewClient(new MyBrowser());

            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            //webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
        }
    }

    private String getRequestResult(String urlParam) {
        String result = null;
        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(urlParam).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void showWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    //encryptor
    abstract class Encryptor {
        SecretKey key;

        abstract SecretKey deriveKey(String passpword, byte[] salt);

        abstract String encrypt(String plaintext, String password);

        abstract String decrypt(String ciphertext, String password);

        String getRawKey() {
            if (key == null) {
                return null;
            }

            return Crypto.toHex(key.getEncoded());
        }
    }

    private final Encryptor PADDING_ENCRYPTOR = new Encryptor() {

        @Override
        public SecretKey deriveKey(String password, byte[] salt) {
            return Crypto.deriveKeyPad(password);
        }

        @Override
        public String encrypt(String plaintext, String password) {
            key = deriveKey(password, null);
            Log.d(TAG, "Generated key: " + getRawKey());

            return Crypto.encrypt(plaintext, key, null);
        }

        @Override
        public String decrypt(String ciphertext, String password) {
            SecretKey key = deriveKey(password, null);

            return Crypto.decryptNoSalt(ciphertext, key);
        }
    };
/*
    abstract class CryptoTask extends AsyncTask<Void, Void, String> {

        Exception error;

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
            //toggleControls(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return doCrypto();
            } catch (Exception e) {
                error = e;
                Log.e(TAG, "Error: " + e.getMessage(), e);

                return null;
            }
        }

        protected abstract String doCrypto();

        @Override
        protected void onPostExecute(String result) {
            setProgressBarIndeterminateVisibility(false);
            //toggleControls(true);

            if (error != null) {
                Toast.makeText(MainActivity.this,
                        "Error: " + error.getMessage(), Toast.LENGTH_LONG)
                        .show();

                return;
            }


            updateUi(result);
        }

        protected abstract void updateUi(String result);
    }*/

}
