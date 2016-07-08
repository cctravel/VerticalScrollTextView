package com.example.verticalscrolltextviewexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private List<String> stringList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for (int i = 0; i < 10; i++) {
			stringList.add("这是第" + String.valueOf(i) + "条信息");
		}
		VerticalScrollTextView textView = (VerticalScrollTextView) findViewById(R.id.test);
		textView.setStringList(stringList);
	}
}
