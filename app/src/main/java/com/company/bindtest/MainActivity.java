package com.company.bindtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.company.libapi.LCJViewBinder;
import com.example.BindView;

public class MainActivity extends Activity {
    @BindView(R.id.test)
    Button mButton;
    @BindView(R.id.test1)
    TextView mTv;
    @BindView(R.id.test2)
    Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LCJViewBinder.bind(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LCJViewBinder.unBind(this);
    }
}
