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

		// ִ�г�ʼ�����������ص�ַ��10.0.2.2
		init();
//		loadurl(webView, "http://60.190.203.85:8088/himsweb/login/mobileLogin");
//		loadurl(webView, "http://10.0.2.2:8080/himsweb/login/mobileLogin");
		loadurl(webView, "http://10.71.16.60:8080/himsweb/login/mobileLogin");

		handler = new Handler() {
//			public void handleMessage(Message msg) {
//				// ����һ��Handler�����ڴ��������߳���UI��ͨѶ
//				if (!Thread.currentThread().isInterrupted()) {
//					switch (msg.what) {
//					case 0:
//						pd.show();// ��ʾ���ȶԻ���
//						break;
//					case 1:
//						pd.hide();// ���ؽ��ȶԻ��򣬲���ʹ��dismiss()��cancel(),�����ٴε���show()ʱ����ʾ�ĶԻ���СԲȦ���ᶯ��
//						break;
//					}
//				}
//				super.handleMessage(msg);
//			}
		};
	}

	@Override
	// Ĭ�ϵ���˼������˳�Activity�����������������ʹ������WebView�ڷ���
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConfirmExit();// ���˷��ؼ������Ѿ����ܷ��أ���ִ���˳�ȷ��
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void ConfirmExit() {// �˳�ȷ��
		AlertDialog.Builder ad = new AlertDialog.Builder(MainWebView.this);
		ad.setTitle("�˳�");
		ad.setMessage("�Ƿ��˳����?");
		ad.setPositiveButton("��", new DialogInterface.OnClickListener() {// �˳���ť
					@Override
					public void onClick(DialogInterface dialog, int i) {
						MainWebView.this.finish();// �ر�activity

					}
				});
		ad.setNegativeButton("��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// ���˳�����ִ���κβ���
			}
		});
		ad.show();// ��ʾ�Ի���
	}

	// ��ʼ��
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public void init() {
		webView = (WebView) findViewById(R.id.mainWebView);
		webView.getSettings().setJavaScriptEnabled(true);// ����JS
		
		webView.addJavascriptInterface(this, "androidPhone");
		
		webView.setScrollBarStyle(0);// ���������Ϊ0���ǲ������������ռ䣬��������������ҳ��
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				loadurl(view, url);// ������ҳ
				return true; // true��ʾ���¼��ڴ˴�����������Ҫ�ٹ㲥
			}

			@Override
			// ת�����ʱ�Ĵ���
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(MainWebView.this, "Rosten no ! " + description,
						Toast.LENGTH_SHORT).show();
			}

		});
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {// ������ȸı������
				if (progress == 100) {
					handler.sendEmptyMessage(1);// ���ȫ������,���ؽ��ȶԻ���
				}
				super.onProgressChanged(view, progress);
			}
		});

//		pd = new ProgressDialog(MainWebView.this);
//		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		pd.setMessage("���������У����Ժ�");
	}

	public void loadurl(final WebView view, final String url) {
		view.loadUrl(url);// ������ҳ
		
//		new Thread() {
//			public void run() {
//				handler.sendEmptyMessage(0);
//				view.loadUrl(url);// ������ҳ
//			}
//		}.start();
	}
}
