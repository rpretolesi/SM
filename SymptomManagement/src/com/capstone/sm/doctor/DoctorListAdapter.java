package com.capstone.sm.doctor;

import java.util.ArrayList;

import com.capstone.SQL.SQLContract.DoctorEntry;
import com.capstone.bitmap.utilities.BitmapUtil;
import com.capstone.sm.R;
import com.capstone.sm.ViewHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class DoctorListAdapter extends BaseAdapter 
{
	private ArrayList<Doctor> m_aldm = new ArrayList<Doctor>();

    private final Context context;

    // the context is needed to inflate views in getView()
    public DoctorListAdapter(Context context) {
        this.context = context;
    }

    public void updateData(ArrayList<Doctor> aldm) 
    {
        this.m_aldm = aldm;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return m_aldm.size();
    }

    @Override
    public Doctor getItem(int position) {
        return m_aldm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        if (convertView == null) 
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.doctor_list_activity, parent, false);
        }

        ImageView ivImageView = ViewHolder.get(convertView, R.id.idImageViewDoctor);
        TextView tvTextView = ViewHolder.get(convertView, R.id.idTextViewDoctorName);

        Doctor dm = getItem(position);

        if (dm != null)
        {
        	byte[] ByteArray = dm.getImage();
        	Bitmap bitmap = BitmapUtil.getInstance().getBitmap(ByteArray);
			ivImageView.setImageBitmap(bitmap);
			ivImageView.setRotation(270);

	        String strDoctorName = DoctorEntry.getDoctorName(context, dm.getId());
	        tvTextView.setText(strDoctorName);
        }

        return convertView;
        
    }
}
