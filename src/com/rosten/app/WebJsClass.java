package com.rosten.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class WebJsClass extends Activity{
	public void call(String tele) {
		Intent intent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + tele));
		startActivity(intent);
	}
}
