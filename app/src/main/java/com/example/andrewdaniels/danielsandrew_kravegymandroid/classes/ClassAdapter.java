package com.example.andrewdaniels.danielsandrew_kravegymandroid.classes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;

public class ClassAdapter extends BaseAdapter {

    private ArrayList<Athlete> mModel;
    private LayoutInflater mInflator;
    private int mLayoutID;

    public ClassAdapter(Context context, ArrayList<Athlete> mModel, int mLayoutID) {
        this.mModel = mModel;
        this.mLayoutID = mLayoutID;

        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolderItem {
        public ImageView profileImage;

        public ViewHolderItem(View v) {
            profileImage = v.findViewById(R.id.iv_athlete);
        }
    }

    @Override
    public int getCount() {
        if (mModel != null) {
            return mModel.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem vhi;
        if (convertView == null) {
            convertView = mInflator.inflate(mLayoutID, parent, false);
            vhi = new ViewHolderItem(convertView);
            convertView.setTag(vhi);
        } else {
            vhi = (ViewHolderItem) convertView.getTag();
        }

        Athlete a = mModel.get(position);

        //TODO: Setup vhi.profileImage with image from athlete.

        return convertView;
    }
}
