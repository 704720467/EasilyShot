package com.zp.easilyshot.easilyshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zp.easilyshot.easilyshot.R;

import java.io.File;
import java.util.List;

/**
 * Created by zhang on 2017/8/6.
 */

public class MyAdapter extends BaseAdapter {

    private List<String> stuList;
    private LayoutInflater inflater;
    private ViewHolder vh;
    private Context context;

    public MyAdapter(List<String> stuList, Context context) {
        this.stuList = stuList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stuList == null ? 0 : stuList.size();
    }

    @Override
    public String getItem(int position) {
        return stuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_item, null);
            vh = new ViewHolder();
            vh.image_photo = (ImageView) convertView.findViewById(R.id.img);
            vh.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String path = stuList.get(position);
        vh.name.setText(path.substring(path.lastIndexOf("/") + 1));
        vh.name.setTag(position);
        File file = new File(stuList.get(position));
        //加载图片
        Glide.with(context).load(file).into(vh.image_photo);
        return convertView;
    }

    public class ViewHolder {
        public ImageView image_photo;
        public TextView name;
    }
}