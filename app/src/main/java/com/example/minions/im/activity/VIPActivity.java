package com.example.minions.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.minions.im.R;

public class VIPActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        LinearLayout layout = (LinearLayout) findViewById(R.id.lin_left_blue);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIPActivity.this.finish();
            }
        });

    }
    
}
