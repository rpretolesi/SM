package com.capstone.sm.paintherapy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capstone.sm.R;
import com.capstone.sm.ViewHolder;

public class PainTherapyListAdapter extends BaseAdapter 
{
	private ArrayList<PainTherapy> m_alptm = new ArrayList<PainTherapy>();
    private final Context context;

    // the context is needed to inflate views in getView()
    public PainTherapyListAdapter(Context context) {
        this.context = context;
    }

    public void updateData(ArrayList<PainTherapy> alptm) 
    {
        this.m_alptm = alptm;
        notifyDataSetChanged();
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_alptm.size();
	}

	@Override
	public PainTherapy getItem(int position) {
		// TODO Auto-generated method stub
        return m_alptm.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
        return position;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        if (convertView == null) 
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.pain_therapy_list_adapter, parent, false);
        }

        TextView tvTextViewName = ViewHolder.get(convertView, R.id.idTexViewtPainTherapyName);

        PainTherapy ptlm = getItem(position);

        tvTextViewName.setText(ptlm.getName());

        return convertView;
        
    }
}
