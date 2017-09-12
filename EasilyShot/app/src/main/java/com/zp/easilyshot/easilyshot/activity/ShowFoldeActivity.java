package com.zp.easilyshot.easilyshot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zp.easilyshot.easilyshot.R;
import com.zp.easilyshot.easilyshot.adapter.FoldeMyAdapter;
import com.zp.easilyshot.easilyshot.adapter.MyAdapter;
import com.zp.easilyshot.easilyshot.util.AlertDialogUtil;
import com.zp.easilyshot.easilyshot.util.FileToolUtils;
import com.zp.easilyshot.easilyshot.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

public class ShowFoldeActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView tagTypeRv;
    private FoldeMyAdapter myAdapter;
    private List<String> stuList = new ArrayList<>();
    private ProgressDialogUtil progressDialogUtil;
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    myAdapter.notifyDataSetChanged();
                    if (progressDialogUtil != null)
                        progressDialogUtil.cancelWaiteDialog();
                    break;
                case 2:
                    if (progressDialogUtil != null)
                        progressDialogUtil.showWaiteDialog();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_folder);
        progressDialogUtil = new ProgressDialogUtil(this);

        findViewById(R.id.back_lay).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("用户选择");
        tagTypeRv = (ListView) findViewById(R.id.content_list_view);
        myAdapter = new FoldeMyAdapter(stuList, this);
        tagTypeRv.setAdapter(myAdapter);
        tagTypeRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String folderName = stuList.get(position);
                Intent intent = new Intent();
                intent.setClass(ShowFoldeActivity.this, ShowInfoActivity.class);
                intent.putExtra("folderName", folderName);
                ShowFoldeActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(2);
                ArrayList<String> a = FileToolUtils.getFolderPath(FileToolUtils.getShortFolder());
                stuList.clear();
                stuList.addAll(a);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
            case R.id.img_back:
                finish();
                break;
        }
    }
}
