package com.rosten.app;

import com.rosten.rostenoa.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainWebView extends Activity {

	private WebView webView;
	private Handler handler;
	private ProgressDialog pd;
	
	public void call(String tele) {
		Intent intent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + tele));
		startActivity(intent);
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_webview);

		// 执行初始化函数，本地地址：10.0.2.2
		init();
//		loadurl(webView, "http://60.190.203.85:8088/himsweb/login/mobileLogin");
//		loadurl(webView, "http://10.0.2.2:8080/himsweb/login/mobileLogin");
		loadurl(webView, "http://10.71.16.60:8080/himsweb/login/mobileLogin");

		handler = new Handler() {
//			public void handleMessage(Message msg) {
//				// 定义一个Handler，用于处理下载线程与UI间通讯
//				if (!Thread.currentThread().isInterrupted()) {
//					switch (msg.what) {
//					case 0:
//						pd.show();// 显示进度对话框
//						break;
//					case 1:
//						pd.hide();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
//						break;
//					}
//				}
//				super.handleMessage(msg);
//			}
		};
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
		webView.getSettings().setJavaScriptEnabled(true);// 可用JS
		
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
				if (progress == 100) {
					handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
				}
				super.onProgressChanged(view, progress);
			}
		});

//		pd = new ProgressDialog(MainWebView.this);
//		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		pd.setMessage("数据载入中，请稍候！");
	}

	public void loadurl(final WebView view, final String url) {
		view.loadUrl(url);// 载入网页
		
//		new Thread() {
//			public void run() {
//				handler.sendEmptyMessage(0);
//				view.loadUrl(url);// 载入网页
//			}
//		}.start();
	}
}
