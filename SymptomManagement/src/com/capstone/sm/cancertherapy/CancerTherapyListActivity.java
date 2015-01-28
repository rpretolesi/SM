package com.capstone.sm.cancertherapy;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.capstone.SQL.SQLContract.CancerTherapyEntry;

public class CancerTherapyListActivity extends ListActivity 
{
	public final static String CANCER_THERAPY_ID = "Cancer_Therapy_ID";
	private final static String PATIENT_ID = "Patient_ID";
	private final static String DOCTOR_ID = "Doctor_ID";

	private long m_lPatientID; 
	private long m_lDoctorID; 
	private CancerTherapyListAdapter m_adapter;
	private ArrayList<CancerTherapy> m_alctm;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null)
		{
			m_lPatientID = intent.getLongExtra(PATIENT_ID, 0);
			m_lDoctorID = intent.getLongExtra(DOCTOR_ID, 0);
		}
			    
		m_adapter = new CancerTherapyListAdapter(this);
		setListAdapter(m_adapter);		  
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);

		CancerTherapy ctm = m_adapter.getItem(position);
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra(CANCER_THERAPY_ID, ctm.getId());
		setResult(RESULT_OK, returnIntent);
		
		finish();		
	}  
	
	@Override
	public void onResume()
	{
		super.onResume();
				    
		m_alctm = CancerTherapyEntry.getListCancerTherapy(getApplicationContext(),m_lPatientID ,m_lDoctorID); 
		m_adapter.updateData(m_alctm);
	}
	
	@Override
	public void onDestroy()
	{
	    super.onDestroy();

	    // free adapter
	    setListAdapter(null);
	}
	
	public static Intent makeCancerTherapyListActivity(Context context, long lPatientID, long lDoctorID) 
	{
		Intent intent = new Intent();
		intent.putExtra(PATIENT_ID, lPatientID);
		intent.putExtra(DOCTOR_ID, lDoctorID);
		intent.setClass(context, CancerTherapyListActivity.class);
				 
		return intent;
	}
}
