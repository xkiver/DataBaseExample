package cl.telematica.databaseexample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class DetailsActivity extends Activity {
	
	private String url = null;
	private WebView webView;
	private ProgressBar mProgressBar;
	private RelativeLayout hPBarLayout;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		url = getIntent().getStringExtra("link");
		
		webView = (WebView) findViewById(R.id.webView);
		hPBarLayout = (RelativeLayout) findViewById(R.id.hPBarLayout);
		mProgressBar = (ProgressBar) findViewById(R.id.legacy_navigation_progressBar);
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		
		webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                mProgressBar.setProgress(progress);
            }
        });
  
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                // Handle the error
            }
  
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
            	hPBarLayout.setVisibility(View.VISIBLE);
            	super.onPageStarted(view, url, favicon);
            }
            
            @Override
            public void onPageFinished(WebView view, String url){
            	hPBarLayout.setVisibility(View.GONE);
            	super.onPageFinished(view, url);
            }
        });
		
		webView.loadUrl(url);
	}

}
