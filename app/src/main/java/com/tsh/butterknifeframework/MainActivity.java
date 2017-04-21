package com.tsh.butterknifeframework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.BindView;
import com.tsh.inject.InjectView;

public class MainActivity extends AppCompatActivity {

	@BindView(R.id.txt1)
	TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InjectView.bind(this);
		System.out.println("mTextView:" +(mTextView == null));
	}
}
