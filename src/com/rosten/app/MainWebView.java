package com.rosten.app;

import com.rosten.rostenoa.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainWebView extends Activity {

	private WebView webView;
	private ProgressBar progressBar; 
	
	public void call(String tele) {
		Intent intent=new Intent(); 
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:"+tele));
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_webview);

		// 执行初始化函数，本地地址：10.0.2.2
		init();
		loadurl(webView, "http://60.190.203.85:8088/himsweb/login/mobileLogin");
//		loadurl(webView, "http://10.0.2.2:8080/himsweb/login/mobileLogin");
//		loadurl(webView, "http://192.168.0.106:8080/himsweb/login/mobileLogin");
	}

	@Override
	// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConfirmExit();// 按了返回键，但已经不能返回，则执行退出确认
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void ConfirmExit() {// 退出确认
		AlertDialog.Builder ad = new AlertDialog.Builder(MainWebView.this);
		ad.setTitle("退出");
		ad.setMessage("是否退出软件?");
		ad.setPositiveButton("是", new DialogInterface.OnClickListener() {// 退出按钮
					@Override
					public void onClick(DialogInterface dialog, int i) {
						MainWebView.this.finish();// 关闭activity

					}
				});
		ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// 不退出不用执行任何操作
			}
		});
		ad.show();// 显示对话框
	}

	// 初始化
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public void init() {
		webView = (WebView) findViewById(R.id.mainWebView);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		webView.getSettings().setJavaScriptEnabled(true );// 可用JS
		webView.addJavascriptInterface(this, "androidPhone");
		
		webView.setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				loadurl(view, url);// 载入网页
				return true; // true表示此事件在此处被处理，不需要再广播
			}

			@Override
			// 转向错误时的处理
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(MainWebView.this, "Rosten no ! " + description,
						Toast.LENGTH_SHORT).show();
			}

		});
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
				setTitle("页面加载中，请稍候..." + progress + "%");
				MainWebView.this.setProgress(progress * 100);
				if (progress == 100) {
					setTitle(R.string.app_name);
					progressBar.setVisibility(4);
				}
				super.onProgressChanged(view, progress);
			}
		});
	}

	public void loadurl(final WebView view, final String url) {
		view.loadUrl(url);// 载入网页
	}
}
