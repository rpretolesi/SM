package com.capstone.sm.paintherapy;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.capstone.SQL.SQLContract.PainTherapyEntry;

public class PainTherapyListActivity extends ListActivity 
{
	public final static String PAIN_THERAPY_ID = "Pain_Therapy_ID";
	private final static String PATIENT_ID = "Patient_ID";
	private final static String DOCTOR_ID = "Doctor_ID";

	private long m_lPatientID; 
	private long m_lDoctorID; 
	private PainTherapyListAdapter m_adapter;
	private ArrayList<PainTherapy> m_alptm;

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
			    
		m_adapter = new PainTherapyListAdapter(this);
		setListAdapter(m_adapter);		  
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);

		PainTherapy ctm = m_adapter.getItem(position);
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra(PAIN_THERAPY_ID, ctm.getId());
		setResult(RESULT_OK, returnIntent);
		
		finish();		
	}  
	
	@Override
	public void onResume()
	{
		super.onResume();
				    
		m_alptm = PainTherapyEntry.getListPainTherapy(getApplicationContext(),m_lPatientID ,m_lDoctorID); 
		m_adapter.updateData(m_alptm);
	}
	
	@Override
	public void onDestroy()
	{
	    super.onDestroy();

	    // free adapter
	    setListAdapter(null);
	}
	
	public static Intent makePainTherapyListActivity(Context context, long lPatientID, long lDoctorID) 
	{
		Intent intent = new Intent();
		intent.putExtra(PATIENT_ID, lPatientID);
		intent.putExtra(DOCTOR_ID, lDoctorID);
		intent.setClass(context, PainTherapyListActivity.class);
				 
		return intent;
	}
}
