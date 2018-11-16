package com.example.andrewdaniels.danielsandrew_kravegymandroid.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.WorkoutLogListener;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.models.WorkoutLogView;

public class WorkoutLogAdapter extends BaseAdapter {

    private int mAddLayoutID = R.layout.workout_log_add;
    private int mSaveLayoutID = R.layout.workout_log_save;
    private int mSetLayoutID = R.layout.workout_log_set;

    private ArrayList<WorkoutLogView> mModel;
    private LayoutInflater mLayoutInflator;
    private Context mContext;

    public WorkoutLogAdapter(Context c, ArrayList<WorkoutLogView> mModel) {
        this.mModel = mModel;
        this.mContext = c;
        this.mLayoutInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class VHI {

    }

    private class AddVHI extends VHI {
        private Button addSet;

        private AddVHI(View v) {
            addSet = v.findViewById(R.id.btn_add_set);
        }
    }

    private class SetVHI extends VHI {
        private TextView set;
        private EditText rep;
        private TextView workoutType;

        private SetVHI(View v) {
            set = v.findViewById(R.id.tv_set);
            rep = v.findViewById(R.id.et_rep);
            workoutType = v.findViewById(R.id.tv_workout_type);
        }
    }

    private class UndoVHI extends VHI {
        private Button undo;

        private UndoVHI(View v) {
            undo = v.findViewById(R.id.btn_undo);
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

    private WorkoutLogView.ViewType getViewType(int position) {
        return mModel.get(position).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WorkoutLogView view = mModel.get(position);
        VHI vhi = new VHI();

        WorkoutLogView.ViewType viewType = getViewType(position);

        if (convertView == null) {
            switch(viewType) {
                case ADD:
                    convertView = mLayoutInflator.inflate(mAddLayoutID, parent, false);
                    vhi = new AddVHI(convertView);
                    convertView.setTag(vhi);
                    break;
                case SET:
                    convertView = mLayoutInflator.inflate(mSetLayoutID, parent, false);
                    vhi = new SetVHI(convertView);
                    convertView.setTag(vhi);
                    break;
                case SAVE:
                    convertView = mLayoutInflator.inflate(mSaveLayoutID, parent, false);
                    vhi = new UndoVHI(convertView);
                    convertView.setTag(vhi);
                    break;
            }
        } else {
           vhi = (VHI)convertView.getTag();
        }

        switch(viewType) {
            case ADD:
                AddVHI addVHI = (AddVHI)vhi;
                addVHI.addSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext instanceof WorkoutLogListener) {
                            ((WorkoutLogListener)mContext).addSetButtonClicked();
                        }
                    }
                });
                break;
            case SET:
                SetVHI setVHI = (SetVHI)vhi;
                setVHI.set.setText(view.getSet());
                setVHI.rep.setText(view.getRep());
                setVHI.workoutType.setText(view.getWorkoutType());
                break;
            case SAVE:
                UndoVHI undoVHI = (UndoVHI)vhi;
                undoVHI.undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext instanceof WorkoutLogListener) {
                            ((WorkoutLogListener)mContext).undoButtonClicked();
                        }
                    }
                });
                break;
        }

        return convertView;
    }
}
