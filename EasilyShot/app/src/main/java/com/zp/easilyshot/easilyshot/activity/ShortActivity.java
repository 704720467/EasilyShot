package com.zp.easilyshot.easilyshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zp.easilyshot.easilyshot.R;
import com.zp.easilyshot.easilyshot.adapter.MyAdapter;
import com.zp.easilyshot.easilyshot.util.AlertDialogUtil;
import com.zp.easilyshot.easilyshot.util.FileToolUtils;
import com.zp.easilyshot.easilyshot.util.ProgressDialogUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 拍摄界面
 */
public class ShortActivity extends AppCompatActivity implements View.OnClickListener {
    private String folderName;
    private static final int REQ_THUMB = 222;
    private static final int REQ_GALLERY = 333;
    private static final String TAG = "ShortActivity";
    private static final int REQ_TAKE_PHOTO = 444;
    private List<String> stuList = new ArrayList<>();
    private EditText mLocalNameInput;
    private EditText mUserInput;
    private TextView mTvSure;
    private TextView mTvReste;
    private TextView mTvIcCardBefore;
    private TextView mTvIcCardAfter;
    private TextView mTvHukoub;
    private TextView mTvZhaopian;
    private TextView mTvDangan;
    private TextView mUser;
    private ProgressDialogUtil progressDialogUtil;
    private ScrollView scrooll;


    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
//            myAdapter.notifyDataSetChanged();
            switch (msg.what) {
                case 0:
                    changeState();
                    if (progressDialogUtil != null)
                        progressDialogUtil.cancelWaiteDialog();
                    break;
                case 1:
                    if (progressDialogUtil != null)
                        progressDialogUtil.cancelWaiteDialog();
                    finish();
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
        setContentView(R.layout.activity_short);
        progressDialogUtil = new ProgressDialogUtil(this);
        initView();
    }

    private void initView() {
        findViewById(R.id.back_lay).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        mLocalNameInput = (EditText) findViewById(R.id.local_name_input);
        scrooll = (ScrollView) findViewById(R.id.scrooll);
        mUserInput = (EditText) findViewById(R.id.user_input);
        mUser = (TextView) findViewById(R.id.user);
        mTvSure = (TextView) findViewById(R.id.tv_sure);
        mTvReste = (TextView) findViewById(R.id.tv_reste);
        mTvIcCardBefore = (TextView) findViewById(R.id.tv_ic_card_before);
        mTvIcCardAfter = (TextView) findViewById(R.id.tv_ic_card_after);
        mTvHukoub = (TextView) findViewById(R.id.tv_hukoub);
        mTvZhaopian = (TextView) findViewById(R.id.tv_zhaopian);
        mTvDangan = (TextView) findViewById(R.id.tv_dangan);
        mTvSure.setOnClickListener(this);
        mTvReste.setOnClickListener(this);
        mTvIcCardBefore.setOnClickListener(this);
        mTvIcCardAfter.setOnClickListener(this);
        mTvHukoub.setOnClickListener(this);
        mTvZhaopian.setOnClickListener(this);
        mTvDangan.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
            case R.id.img_back:
                mergeBitmap();
                break;
            case R.id.tv_sure:
                String fileName = getLocalName() + getUserName();
                if (TextUtils.isEmpty(fileName)) {
                    scrooll.setVisibility(View.GONE);
                    Toast.makeText(this, "请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    load();
                    scrooll.setVisibility(View.VISIBLE);
                    mLocalNameInput.setText("");
                    mUserInput.setText("");
                    mUser.setText(fileName);
                }
                break;
            case R.id.tv_ic_card_before:
                go(IDCardBefore);
                break;
            case R.id.tv_ic_card_after:
                go(IDCardAfter);
                break;
            case R.id.tv_hukoub:
                go(HuKouBu + huKouBenCount);
                break;
            case R.id.tv_zhaopian:
                go(ZhaoPian + zhaoPianCount);
                break;
            case R.id.tv_dangan:
                go(DangAn + dangAnCount);
                break;
        }
    }


    public String getLocalName() {
        return mLocalNameInput.getText().toString().trim();
    }

    public String getUserName() {
        return mUserInput.getText().toString().trim();
    }

    public void go(String imageFileName) {
        folderName = mUser.getText().toString();
        if (TextUtils.isEmpty(folderName))
            return;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {//判断是否有相机应用
            File photoFile = null;
            try {
                photoFile = createImageFile(imageFileName);//创建临时图片文件
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                Log.i(TAG, "photoURI:" + photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQ_TAKE_PHOTO);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile(String imageFileName) throws IOException {
        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
        //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        File storageDir = FileToolUtils.getShortFolder(folderName);
        File image = new File(storageDir + "/" + imageFileName + ".jpg");
        if (!image.exists()) {
            image.createNewFile();
        }
        //创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
//        File image = File.createTempFile(
//                imageFileName,  /* 前缀 */
//                ".jpg",         /* 后缀 */
//                storageDir      /* 文件夹 */
//        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_TAKE_PHOTO://返回结果
                if (resultCode != Activity.RESULT_OK) return;
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        if (mUser != null && !TextUtils.isEmpty(mUser.getText().toString())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(2);
                    ArrayList<String> a = FileToolUtils.getFilePath(FileToolUtils.getShortFolderPath(folderName));
                    stuList.clear();
                    stuList.addAll(a);
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        }
    }

    int idBeforCount = 0;
    int iDAfterCount = 0;
    int huKouBenCount = 0;
    int zhaoPianCount = 0;
    int dangAnCount = 0;
    String IDCardBefore = "身份证(正)";
    String IDCardAfter = "身份证(反)";
    String HuKouBu = "户口本";
    String ZhaoPian = "照片";
    String DangAn = "档案";

    private void changeState() {
        idBeforCount = 0;
        iDAfterCount = 0;
        huKouBenCount = 0;
        zhaoPianCount = 0;
        dangAnCount = 0;
        for (String s : stuList) {
            if (s.contains(IDCardBefore)) {
                idBeforCount++;
            } else if (s.contains(IDCardAfter)) {
                iDAfterCount++;
            } else if (s.contains(HuKouBu)) {
                huKouBenCount++;
            } else if (s.contains(ZhaoPian)) {
                zhaoPianCount++;
            } else if (s.contains(DangAn)) {
                dangAnCount++;
            }
        }
        mTvIcCardBefore.setText("身份证(正" + ((idBeforCount > 0) ? "，已拍摄" : "") + ")");
        mTvIcCardAfter.setText("身份证(反" + ((iDAfterCount > 0) ? "已拍摄" : "") + ")");
        mTvHukoub.setText("户口本" + ((huKouBenCount > 0) ? huKouBenCount : ""));
        mTvZhaopian.setText("照片" + ((zhaoPianCount > 0) ? zhaoPianCount : ""));
        mTvDangan.setText("档案" + ((dangAnCount > 0) ? dangAnCount : ""));
    }

    boolean matrixSucess = false;

    private void mergeBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(2);
                matrixSucess = false;
                Bitmap firstBitmap = null;
                Bitmap secondBitmap = null;
                try {
                    folderName = mUser.getText().toString();
                    if (TextUtils.isEmpty(folderName)) {
                        new RuntimeException("数据不全folderName==null");
                    }
                    FileInputStream firstFis = new FileInputStream(FileToolUtils.getShortFolder(folderName).getAbsolutePath() + File.separator + IDCardBefore + ".jpg");
                    firstBitmap = BitmapFactory.decodeStream(firstFis);
                    FileInputStream fis = new FileInputStream(FileToolUtils.getShortFolder(folderName).getAbsolutePath() + File.separator + IDCardAfter + ".jpg");
                    secondBitmap = BitmapFactory.decodeStream(fis);
                } catch (Exception e) {
                    Log.e("ShortActivity", "读入图片报错！" + e.getMessage());
                }
                if (firstBitmap == null || secondBitmap == null)
                    finish();
                try {
                    int width = firstBitmap.getWidth() > secondBitmap.getWidth() ? firstBitmap.getWidth() : secondBitmap.getWidth();
                    int height = firstBitmap.getHeight() + secondBitmap.getHeight() + 20;
                    Bitmap bitmap = Bitmap.createBitmap(width, height, firstBitmap.getConfig());
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawBitmap(firstBitmap, new Matrix(), null);
                    canvas.drawBitmap(secondBitmap, (width - secondBitmap.getWidth()) / 2.0f, firstBitmap.getHeight() + 20, null);
                    Log.e("ShortActivity", "合成图片！1");
                    saveMyBitmap(bitmap);
                    Log.e("ShortActivity", "合成图片！1");
                    matrixSucess = true;
                } catch (Exception e) {
                    Log.e("ShortActivity", "合成图片报错！" + e.getMessage());
                } finally {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private void saveMyBitmap(Bitmap mBitmap) throws Exception {
        folderName = mUser.getText().toString();
        if (TextUtils.isEmpty(folderName)) {
            new RuntimeException("数据不全folderName==null");
        }
        File f = new File(FileToolUtils.getShortFolder(folderName).getAbsolutePath() + File.separator + "/合成图片.jpg");
        f.createNewFile();
        FileOutputStream fOut = null;
        fOut = new FileOutputStream(f);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();
    }

}
