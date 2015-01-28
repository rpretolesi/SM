package com.capstone.sm.patient;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.bitmap.utilities.BitmapUtil;
import com.capstone.sm.R;
import com.capstone.sm.ViewHolder;
import com.capstone.sm.datetime.DateTimeUtils;



public class PatientListAdapter extends BaseAdapter 
{
	private ArrayList<Patient> m_alpm = new ArrayList<Patient>();

    private final Context m_context;

    // the context is needed to inflate views in getView()
    public PatientListAdapter(Context context) {
        this.m_context = context;
    }

    public void updateData(ArrayList<Patient> alpm) 
    {
        this.m_alpm = alpm;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return m_alpm.size();
    }

    @Override
    public Patient getItem(int position) {
        return m_alpm.get(position);
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
            convertView = LayoutInflater.from(m_context).inflate(R.layout.patient_list_adapter, parent, false);
        }

        ImageView ivImageView = ViewHolder.get(convertView, R.id.idImageViewPatient);
        TextView tvTextViewName = ViewHolder.get(convertView, R.id.idTextViewPatientName);
        TextView tvTextViewDateOfBirth = ViewHolder.get(convertView, R.id.idTextViewPatientDateOfBirth);

        Patient pm = getItem(position);

        if (pm != null)
        {
        	byte[] ByteArray = pm.getImage();
        	Bitmap bitmap = BitmapUtil.getInstance().getBitmap(ByteArray);
			ivImageView.setImageBitmap(bitmap);
			ivImageView.setRotation(270);

	        tvTextViewName.setText(m_context.getText(R.string.hintTextSignUpPatientFirstName)+ ": " + pm.getFirstname() + ", " + m_context.getText(R.string.hintTextSignUpPatientLastName)+ ": " + pm.getLastname());
	        
	        String strDateTIme = DateTimeUtils.getInstance().FormatDateFromDBToString(m_context, pm.getDateofbirth());
	        
	        tvTextViewDateOfBirth.setText(m_context.getText(R.string.hintTextSignUpPatientDateOfBirth)+ ": " + strDateTIme);
        }

        return convertView;
        
    }
}
