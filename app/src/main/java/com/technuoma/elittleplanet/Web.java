package com.technuoma.elittleplanet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Web extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;

    String title , url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.web);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(title);

        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);

    }
}
