package com.chandora.androidy.newwebviewdemo;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.my_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.addJavascriptInterface(new MyWebAppInterface(),"AndroidInterface");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {

                StringBuilder sb = new StringBuilder();


//                sb.append("function loadData() ");

//                function returnCredentials(){
//                    username = document.getElementsByTagName('input')[0].value
//                    password = document.getElementsByTagName('input')[1].value
//                    window.MYOBJECT.processHTML(userid,password);
//
//                }
//
//                document.getElementsByTagName('button')[0].addEventListener('click',returnCredentials);




                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("(function(){document.getElementsByTagName('button')[0].addEventListener('click',function(){" +
                            "" +
                            "username = document.getElementsByTagName('input')[0].value;" +
                            "password = document.getElementsByTagName('input')[1].value;" +
                            " console.log(username);" +
                            " console.log(password);" +
                            "AndroidInterface.showToast();});})()",null);
                }

//                String javaScript ="javascript:(function() {alert();})()";
//                webView.loadUrl(javaScript);
            }



            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i("ERROR", "onReceivedError: "+error.toString());
            }
        });


    }

    public void onButtonClicked(View view) {
        webView.loadUrl("https://kite.zerodha.com/");

    }

    public class MyWebChromeClient extends WebChromeClient{

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.i("MESSAGE_TYPE", "onConsoleMessage: "+consoleMessage.message() +consoleMessage.lineNumber());
            Toast.makeText(MainActivity.this, ""+consoleMessage.message(), Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    public class MyWebAppInterface {



        @JavascriptInterface
        public void  showToast(){

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(MainActivity.this, "Hurray!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
