package pro.combinat.webview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pro.combinat.utils.AESHelper;

public class MainActivity  extends AppCompatActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean connected = this.getIntent().getBooleanExtra("NETWORK_STATUS", false);
        if (!connected)
            showWarning();
    }
*/
private WebView mWebView;
private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean connected = this.getIntent().getBooleanExtra("NETWORK_STATUS", false);

        if (!connected) {
            findViewById(R.id.textView).setVisibility(View.VISIBLE);
            showWarning();
        } else {
            findViewById(R.id.webView).setVisibility(View.VISIBLE);

            //String url1 = AESHelper.getUrl(getString(R.string.app_url));
            String urlMain = AESHelper.getUrl(getString(R.string.app_url_enc));
            if(getRequestResult(urlMain)){
                url = getString(R.string.x_url);
            } else {
                url = getString(R.string.y_url);
            }
            mWebView = (WebView) findViewById(R.id.webView);
            mWebView.setWebViewClient(new MyBrowser());

            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebView.loadUrl(url);
        }
    }

    private boolean getRequestResult(String urlParam){
        URL url = null;
        HttpURLConnection urlConnection = null;
        boolean result = false;
        try {
            url = new URL(urlParam);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = true;

        } catch (MalformedURLException e) {
            result = false;
        } catch (IOException e) {
            result = false;
        } finally {
            urlConnection.disconnect();
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
}
