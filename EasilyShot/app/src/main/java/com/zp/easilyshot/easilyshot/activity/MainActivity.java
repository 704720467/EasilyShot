package com.zp.easilyshot.easilyshot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zp.easilyshot.easilyshot.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_get_info).setOnClickListener(this);
        findViewById(R.id.tv_show_info).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_get_info:
                startActivity(new Intent(this, ShortActivity.class));
                break;
            case R.id.tv_show_info:
                startActivity(new Intent(this, ShowFoldeActivity.class));
                break;
        }
    }

}
