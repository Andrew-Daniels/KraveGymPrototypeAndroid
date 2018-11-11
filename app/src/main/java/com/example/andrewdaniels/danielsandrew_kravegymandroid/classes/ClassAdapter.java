package com.example.andrewdaniels.danielsandrew_kravegymandroid.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.BitmapHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.StringFormatter;

public class ClassAdapter extends BaseAdapter {

    private final List<Athlete> mModel;
    private final LayoutInflater mInflator;
    private final int mLayoutID;
    private final Context mContext;

    public ClassAdapter(Context context, List<Athlete> mModel, int mLayoutID) {
        this.mModel = mModel;
        this.mLayoutID = mLayoutID;
        this.mContext = context;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolderItem {
        private final ImageView profileImage;
        private final TextView initials;

        private ViewHolderItem(View v) {
            profileImage = v.findViewById(R.id.iv_athlete);
            initials = v.findViewById(R.id.tv_initials);
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
        vhi.profileImage.setTag(a);
        if (a.hasProfileImage()) {
            Bitmap profileImage = BitmapHelper.loadImage(mContext, a.getUsername());
            vhi.profileImage.setImageBitmap(profileImage);
            vhi.initials.setText("");
        } else {
            vhi.initials.setText(StringFormatter.getInitials(a));
        }
        vhi.profileImage.setClipToOutline(true);
        return convertView;
    }
}
