package pro.combinat.webview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.concurrent.ExecutionException;

import pro.combinat.utils.AESHelper;
import pro.combinat.utils.HttpGetRequest;

public class MainActivity  extends AppCompatActivity {

private WebView webView;
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
}
