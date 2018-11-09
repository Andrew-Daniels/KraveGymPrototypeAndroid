package com.example.andrewdaniels.danielsandrew_kravegymandroid.classes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.StringFormatter;

public class ClassAdapter extends BaseAdapter {

    private List<Athlete> mModel;
    private LayoutInflater mInflator;
    private int mLayoutID;

    public ClassAdapter(Context context, List<Athlete> mModel, int mLayoutID) {
        this.mModel = mModel;
        this.mLayoutID = mLayoutID;

        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolderItem {
        private ImageView profileImage;
        private TextView initials;

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
        if (a.getProfileImage() != null) {
            vhi.profileImage.setImageBitmap(a.getProfileImage());
            vhi.initials.setText("");
        } else {
            vhi.initials.setText(StringFormatter.getInitials(a));
        }
        vhi.profileImage.setClipToOutline(true);
        return convertView;
    }
}
