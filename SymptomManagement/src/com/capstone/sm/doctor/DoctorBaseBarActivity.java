package com.capstone.sm.doctor;

import java.util.Calendar;

import com.capstone.sm.patient.Patient;
import com.capstone.sm.patientstatus.PatientStatus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;

public class DoctorBaseBarActivity extends ActionBarActivity implements ActionBar.TabListener
{
	private final static int UPDATE_ALARM_ID = 10;
	protected static boolean m_bUpdateStatus;

	protected static Doctor m_dm;
	protected static Patient m_pm;
	
    // Partial Patient Status Information
	protected static PatientStatus m_psm;
	protected static long[] m_alPatienIDToCheck;// List of patient that answered with problem in status health
	

	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);

        if (m_dm == null)
        {
            m_dm = new Doctor();
        }

        if (m_pm == null)
        {
            m_pm = new Patient();
        }
        else
        {
        	m_pm.reset();
        }
        
        if (m_psm == null)
        {
        	m_psm = new PatientStatus();
        }
        else
        {
        	m_psm.reset();
        }
    }
    
	@Override
	public void onResume() 
	{
  		super.onResume();
		
	}
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}	
	
	protected static void StartUpdate(Context context, long lUpdateFrequencyMins)
	{
		//ConvertDateTimeLocalToCalendar(context,)
  		if(lUpdateFrequencyMins > 0)
  		{
      		// Converto in ore
  			lUpdateFrequencyMins = lUpdateFrequencyMins * 60000;
			// I Set the Alarm frequency
			// Scheduling management
			Calendar c = Calendar.getInstance();
			
		    Intent intent = new Intent(context, DoctorUpdateAlarmReceiver.class);
			PendingIntent alarmIntent = PendingIntent.getBroadcast(context, UPDATE_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),  lUpdateFrequencyMins, alarmIntent);
			
			m_bUpdateStatus = true;
  		}
		
	}
	
	protected static void StopUpdate(Context context)
	{
		// I Set the Alarm frequency
		// Scheduling management
	    Intent intent = new Intent(context, DoctorUpdateAlarmReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, UPDATE_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent);

		m_bUpdateStatus = false;
	}
}
