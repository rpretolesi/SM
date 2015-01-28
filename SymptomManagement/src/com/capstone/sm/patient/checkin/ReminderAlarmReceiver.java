package com.capstone.sm.patient.checkin;

import java.util.Collection;
import java.util.concurrent.Callable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.CancerTherapyEntry;
import com.capstone.SQL.SQLContract.PainTherapyEntry;
import com.capstone.SQL.SQLContract.PatientEntry;
import com.capstone.sm.BaseBroadcastReceiver;
import com.capstone.sm.MainActivity;
import com.capstone.sm.R;
import com.capstone.sm.cancertherapy.CancerTherapy;
import com.capstone.sm.cancertherapy.CancerTherapySvc;
import com.capstone.sm.cancertherapy.CancerTherapySvcApi;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.paintherapy.PainTherapy;
import com.capstone.sm.paintherapy.PainTherapySvc;
import com.capstone.sm.paintherapy.PainTherapySvcApi;
import com.capstone.sm.patient.PatientViewActivity;

public class ReminderAlarmReceiver extends BaseBroadcastReceiver 
{
	private final static String ID = "id";
	 
	 NotificationManager m_notificationManager;
	 Notification m_ReminderNotification;

	 
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

        if(context != null && intent != null)
        {
        	final Context ctx = context;
        	final Intent intn = intent;
        	final long lPatientID = intent.getLongExtra(ReminderAlarmReceiver.ID, 0);

        	if(lPatientID > 0)
            {
        		final long lDoctorID = PatientEntry.getPatientDoctorID(context, lPatientID);
	
        		// I will update the Cancer and Pain Therapy
    			// Check if i am in local mode
    	        if(MainActivity.LOCAL_MODE == false)
    	        {
    				// I get the connection
    				final CancerTherapySvcApi ctsvc = CancerTherapySvc.get(ctx);
    				
    				CallableTask.invoke(new Callable<Collection<CancerTherapy>>() {

    					@Override
    					public Collection<CancerTherapy> call() throws Exception {
    						return ctsvc.getCancerTherapyByPatientid(lPatientID);
    					}
    				}, new TaskCallback<Collection<CancerTherapy>>() 
    				{
    					@Override
    					public void success(Collection<CancerTherapy> result) 
    					{
    		        		if(result != null)
    		            	{
    				        	// Before to do that, i will delete the old one...
    		        			CancerTherapyEntry.deleteAllCancerTherapy(ctx);
    		        			
    					        for (CancerTherapy ctm : result)
    					        {
    		 						if (CancerTherapyEntry.addCancerTherapy(ctx, ctm) == true)
    		 						{
    		 							// Ok
    		 						}
    		 						else
    		 						{
    		 	 						Toast.makeText(ctx, R.string.labelTextSetCancerTherapyAddError, Toast.LENGTH_LONG).show();
    		 	 						break;
    		 						}
    		 					}	
    		            	}
    		        		
    						// get Pain Therapy
    						getPainTherapy();
    		        		
    					}

    					@Override
    					public void error(Exception e) 
    					{
    						Toast.makeText(ctx, R.string.labelTextViewGetDoctorListError, Toast.LENGTH_LONG).show();
    						CancerTherapySvc.close();	
    						
    						// get Pain Therapy
    						getPainTherapy();
    					}
    					
    					
    					private void getPainTherapy()
    					{
            				// I get the connection
            				final PainTherapySvcApi ptsvc = PainTherapySvc.get(ctx);
            				
            				CallableTask.invoke(new Callable<Collection<PainTherapy>>() {

            					@Override
            					public Collection<PainTherapy> call() throws Exception {
            						return ptsvc.getPainTherapyByPatientid(lPatientID);
            					}
            				}, new TaskCallback<Collection<PainTherapy>>() 
            				{
            					@Override
            					public void success(Collection<PainTherapy> result) 
            					{
            		        		if(result != null)
            		            	{
            				        	// Before to do that, i will delete the old one...
            				        	PainTherapyEntry.deleteAllPainTherapy(ctx);
            		        			
            					        for (PainTherapy ptm : result)
            					        {
            								// Store all Pain Therapy in the Local DB
            					        	if (PainTherapyEntry.addPainTherapy(ctx, ptm) == true)
            		 						{
            		 							// Ok
            		 						}
            		 						else
            		 						{
            		 	 						Toast.makeText(ctx, R.string.labelTextSetPainTherapyAddError, Toast.LENGTH_LONG).show();
            		 	 						break;
            		 						}
            		 					}	
            		            	}
            		        		
            		        		// All synchronized, i will start the Check In
               						StartReminder(ctx,intn, lPatientID ,lDoctorID);

            					}

            					@Override
            					public void error(Exception e) 
            					{
            						Toast.makeText(ctx, R.string.labelTextViewGetDoctorListError, Toast.LENGTH_LONG).show();
            						PainTherapySvc.close();	
            						
            		        		// All synchronized, i will start the Check In
            						StartReminder(ctx,intn, lPatientID ,lDoctorID);
            					}
            				});
    					}
    				});
    	        }
    	        else
    	        {
					StartReminder(ctx,intn, lPatientID ,lDoctorID);
    	        }
			}
		}
	}
	
	private void StartReminder(Context context, Intent intent, long lPatientID, long lDoctorID )
	{
		// Before start the check-in i should to do at least 1 cancer therapy
		if(CancerTherapyEntry.getLastCancerTherapyExecutedID(context, lPatientID, lDoctorID) > 0)
		{
		     Intent intentToStart = ReminderActivity.makeReminderActivity(context);
		     PendingIntent pendingIntentToStart = PendingIntent.getActivity(context, 0, intentToStart, Intent.FLAG_ACTIVITY_NEW_TASK);
		     
		     m_ReminderNotification = new NotificationCompat.Builder(context)
		       .setContentTitle(context.getString(R.string.textReminderNotificationTitle))
		       .setContentText(context.getString(R.string.textReminderNotificationText))
		       .setTicker(context.getString(R.string.textReminderNotificationTicker))
		       .setWhen(System.currentTimeMillis())
		       .setContentIntent(pendingIntentToStart)
		       .setDefaults(Notification.DEFAULT_SOUND)
		       .setAutoCancel(true)
		       .setSmallIcon(R.drawable.android_ic_launcher)
		       .build();
		     
		     m_notificationManager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		     m_notificationManager.notify(REMINDER_NOTIFICATION_ID, m_ReminderNotification);
		}		
	}
	public static Intent makeReminderAlarmReceiver(Context context, long lPatientID) 
	{
		Intent intent = new Intent();
		intent.setClass(context, ReminderAlarmReceiver.class);
		intent.putExtra(ID, lPatientID);	
		return intent;
	}	


}
