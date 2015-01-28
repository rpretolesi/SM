package com.capstone.sm.patient;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

import com.capstone.sm.doctor.Doctor;
import com.capstone.sm.patient.checkin.ReminderAlarmReceiver;
import com.capstone.sm.patient.checkin.ReminderUpdateAlarmReceiver;

public class PatientBaseBarActivity extends ActionBarActivity implements ActionBar.TabListener
{
	private final static int REMINDER_ALARM_ID = 1;
	private final static int REMINDER_UPDATE_ALARM_ID = 2;
	protected static boolean m_bReminderStatus;

	protected static Patient m_pm;
	protected static Doctor m_dm;

	protected PatientSvcApi m_pmsvc;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        if (m_pm == null)
        {
            m_pm = new Patient();
        }
        if (m_dm == null)
        {
            m_dm = new Doctor();
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
	
	protected static void StartReminder(Context context, long lReminderFrequencyMins)
	{
  				
  		if(lReminderFrequencyMins > 0)
  		{
      		// Convert to minute
  			lReminderFrequencyMins = lReminderFrequencyMins * 60000;
			// I Set the Alarm frequency
			// Scheduling management
			Calendar c = Calendar.getInstance();
			
		    Intent intent = ReminderAlarmReceiver.makeReminderAlarmReceiver(context, m_pm.getId());
			PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REMINDER_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),  lReminderFrequencyMins, alarmIntent);
			
			m_bReminderStatus = true;
  		}
		
	}

	protected static void StartReminderUpdate(Context context)
	{
		// Every 30 Minute i will check if all Answer to the question Check In are update
//		long lReminderUpdateFrequencyMins = 30 * 60 * 1000;
		long lReminderUpdateFrequencyMins = 1 * 60 * 1000;
		Calendar c = Calendar.getInstance();
	    Intent intent = ReminderUpdateAlarmReceiver.makeReminderUpdateAlarmReceiver(context, m_pm.getId());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REMINDER_UPDATE_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),  lReminderUpdateFrequencyMins, alarmIntent);
	}

	protected static void StopReminder(Context context)
	{
		// I Set the Alarm frequency
		// Scheduling management
	    Intent intent = new Intent(context, ReminderAlarmReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REMINDER_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent);

		m_bReminderStatus = false;
	}

	protected static void StopReminderUpdate(Context context)
	{
	    Intent intent = new Intent(context, ReminderUpdateAlarmReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REMINDER_UPDATE_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent);
	}
}
