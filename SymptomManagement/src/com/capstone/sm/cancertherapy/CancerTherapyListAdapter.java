package com.capstone.sm.cancertherapy;

import java.util.ArrayList;

import com.capstone.sm.R;
import com.capstone.sm.ViewHolder;
import com.capstone.sm.datetime.DateTimeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CancerTherapyListAdapter extends BaseAdapter 
{
	private ArrayList<CancerTherapy> m_aictm =  new ArrayList<CancerTherapy>();
    private final Context m_context;

    // the context is needed to inflate views in getView()
    public CancerTherapyListAdapter(Context context) {
        this.m_context = context;
    }

    public void updateData(ArrayList<CancerTherapy> alctm) 
    {
        this.m_aictm = alctm;
        notifyDataSetChanged();
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_aictm.size();
	}

	@Override
	public CancerTherapy getItem(int position) {
		// TODO Auto-generated method stub
        return m_aictm.get(position);
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
            convertView = LayoutInflater.from(m_context).inflate(R.layout.cancer_therapy_list_adapter, parent, false);
        }

        TextView tvTextViewName = ViewHolder.get(convertView, R.id.idTexViewtCancerTherapyName);
        TextView tvTextViewDateTimeSet = ViewHolder.get(convertView, R.id.idTextViewCancerTherapyDateTimeSet);
        TextView tvTextViewDateTimeExec = ViewHolder.get(convertView, R.id.idTextViewCancerTherapyDateTimeExec);

        CancerTherapy ctlm = getItem(position);

        tvTextViewName.setText(ctlm.getName());
        String strDateTIme;
        
        strDateTIme = DateTimeUtils.getInstance().FormatDateTimeFromDBToString(m_context, ctlm.getDatetimeset());
        tvTextViewDateTimeSet.setText("Scheduled Date and Time: " + strDateTIme);
        strDateTIme = DateTimeUtils.getInstance().FormatDateTimeFromDBToString(m_context, ctlm.getDatetimeexec());
        tvTextViewDateTimeExec.setText("Executed Date and Time: " + strDateTIme);

        return convertView;
        
    }
}
