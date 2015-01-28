package com.capstone.sm.patient;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ListView;

import com.capstone.SQL.SQLContract.PatientEntry;

public class PatientListActivity extends ListActivity
{
	// ID of the user current Logged in
	public final static int ACTIVITY_RESULT_GET_PATIENT_ID = 2;
	private long m_lDoctorID; 
	private PatientListAdapter m_adapter;
	private ArrayList<Patient> m_alpm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null)
		{
			m_lDoctorID = intent.getLongExtra(PatientEntry.COLUMN_NAME_DOCTOR_ID, 0);
		}
			    
		m_adapter = new PatientListAdapter(this);
		setListAdapter(m_adapter);		  
	}

	@Override
	public void onResume()
	{
		super.onResume();

		m_alpm = PatientEntry.getPatientByDoctorID(getApplicationContext(), m_lDoctorID);
		m_adapter.updateData(m_alpm);
	}
	  
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		
		Intent returnIntent = new Intent();
		long lPatientID = 0;
		if (m_alpm != null)
		{
			lPatientID = m_alpm.get(position).getId();
			returnIntent.putExtra(BaseColumns._ID, lPatientID);
		}
		if (lPatientID != 0)
		{
			setResult(RESULT_OK, returnIntent);
		}
		else
		{
			setResult(RESULT_CANCELED, returnIntent);
		}
		
		finish();		
		
	}
	  	
	public static Intent makePatientListActivity(Context context, long lDoctorID) 
	{
		Intent intent = new Intent();
		intent.putExtra(PatientEntry.COLUMN_NAME_DOCTOR_ID, lDoctorID);
		intent.setClass(context, PatientListActivity.class);
				 
		return intent;
	}
}
