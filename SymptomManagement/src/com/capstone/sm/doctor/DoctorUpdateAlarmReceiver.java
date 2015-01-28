package com.capstone.sm.doctor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.Callable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.Answer_1;
import com.capstone.SQL.SQLContract.DoctorEntry;
import com.capstone.SQL.SQLContract.PatientStatusEntry;
import com.capstone.SQL.SQLContract.User;
import com.capstone.sm.BaseBroadcastReceiver;
import com.capstone.sm.R;
import com.capstone.sm.SignInActivity;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.datetime.DateTimeUtils;
import com.capstone.sm.patientstatus.PatientStatus;
import com.capstone.sm.patientstatus.PatientStatusSvc;
import com.capstone.sm.patientstatus.PatientStatusSvcApi;

public class DoctorUpdateAlarmReceiver extends BaseBroadcastReceiver 
{
	
	NotificationManager m_notificationManager;
	Notification m_UpdateNotification;
	private Doctor m_dm;
	private ArrayList<Long> m_allPatienIDToCheck;// List of patient that answered with problem in status health
	 
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		// TODO Auto-generated method stub

        if (m_dm == null)
        {
        	m_dm = new Doctor();
        }

        if (m_allPatienIDToCheck == null)
        {
        	m_allPatienIDToCheck = new ArrayList<Long>();
        }
        
        if(context != null)
        {
            // Load the Doctor who did last Login Successfully
            long lUserID = SignInActivity.getSignInUserID(context);
            long lUserType = SignInActivity.getSignInUserType(context);    
            if (lUserType == User.DOCTOR.getValue())
            {
            	// First of all, i will update the data
    			// I get the connection
            	final Context ctx = context;
    			final PatientStatusSvcApi svc = PatientStatusSvc.get(ctx);

    			m_dm = DoctorEntry.getDoctor(ctx, lUserID);

    			
    			CallableTask.invoke(new Callable<Collection<PatientStatus>>() {

    				@Override
    				public Collection<PatientStatus> call() throws Exception {
    					return svc.getPatientStatusByDoctorid(m_dm.getId());
    				}
    			}, new TaskCallback<Collection<PatientStatus>>() 
    			{
    				@Override
    				public void success(Collection<PatientStatus> result) 
    				{
    	        		if(result != null)
    	            	{
    				        for (PatientStatus psm : result)
    				        {
    							// Store all Patient Status in the Local DB if not already stored
    		 					if (PatientStatusEntry.IsPatientStatusIDPresent(ctx, psm.getId()) == false)
    		 					{
    		 						if (PatientStatusEntry.addPatientStatus(ctx, psm) == true)
    		 						{
    		 							// Ok
    		 						}
    		 						else
    		 						{
    		 	 						Toast.makeText(ctx, R.string.labelTextViewPatientStatusAddPatientStatusError, Toast.LENGTH_LONG).show();
    		 							DoctorSvc.close();	
    		 	 						break;
    		 						}
    		 					}
    	 					}	
    	            	}

    	        		// Now i check
    					CheckPatientStatusHealth(ctx);
    				}

    				@Override
    				public void error(Exception e) 
    				{
    					Toast.makeText(ctx, R.string.labelTextViewPatientStatusGetPatientStatusListError, Toast.LENGTH_LONG).show();
    					PatientStatusSvc.close();

    					// Now i check
    					CheckPatientStatusHealth(ctx);
    				}
    			});
            }        	
        }
    }
	
	private void CheckPatientStatusHealth(Context context)
	{
    	ArrayList<PatientStatus> alpsm = new ArrayList<PatientStatus>();

    	if(context != null)
        {
    		// i get the list of all answer to Question 1 Regarding the Health Status...
        	alpsm = PatientStatusEntry.getListPatientStatusByQuestion1(context, m_dm.getId());
        	// Now i search if there is some answer that are older of 12 Hours of severe pain.
        	// The list is ordered by Date and Time Answer DESC, so the first is the newest
        	//, Answer_1.ANSWER_2
        	Calendar cLastStatusHealthWithAnswer2 = Calendar.getInstance();
        	Calendar cLastStatusHealthWithAnswer3 = Calendar.getInstance();       
        	boolean bPatientAlreadyPresent = false;
    		for (PatientStatus psm : alpsm)
    		{
    			if(psm != null)
    			{
    				// I search if the PatientStatus if refered to a Patient that is already on the List.
    				// I know that there are many optimized way to do this, but the optimization is not required on the
    				// Capstone specific
    				// Remember that for this list i need only the Patient ID info
    				if(m_allPatienIDToCheck != null)
    				{
    		    		for (long lPatientID : m_allPatienIDToCheck)
    		    		{
    		    			// Remember that for this list i need only the Patient ID info
    		    			if(psm.getPatientid() == lPatientID)
    		    			{
    		    				// Already present
    		    				bPatientAlreadyPresent = true;
    		    				break;
    		    			}
    		    			
    		    		}
    		
    		    		if(bPatientAlreadyPresent == false)
    		    		{
    		    			if(psm.getAnswerid() == Answer_1.ANSWER_1.getID())
    		    			{
    		    				// In case of Status Health good, i will reset the Date and Time of Answer 2 and 3
    		    				cLastStatusHealthWithAnswer2 = Calendar.getInstance();
    		    				cLastStatusHealthWithAnswer3 = Calendar.getInstance();
    		        		}
    		    			
    		    			if(psm.getAnswerid() == Answer_1.ANSWER_2.getID())
    		    			{
    		    				// Store the last Date and Time Answer
    		    				cLastStatusHealthWithAnswer2 = DateTimeUtils.getInstance().FormatDateTimeFromDBToCalendar(context, psm.getDatetimeanswer());
    		    				long lDiffInMilliSecs = Calendar.getInstance().getTimeInMillis() - cLastStatusHealthWithAnswer2.getTimeInMillis(); //result in millis
    		    				// 16 Hours = (16 x 60 x 60 x 1000) = 57600000 ms 
    		    				if(lDiffInMilliSecs > 57600000)
    		    				{
    			    				// reset the Date and Time of Answer 2
    			    				cLastStatusHealthWithAnswer2 = Calendar.getInstance();
    		    					// Add (one of)the patient reference that match with the our Values
    			    				// Check if it's not already checked by this Doctor
    			    				if(psm.getCheckedbydoctor() == 0)
    			    				{
    				    				m_allPatienIDToCheck.add(psm.getPatientid());
    			    				}
    		    				}
    		    			}
    		    			if(psm.getAnswerid() == Answer_1.ANSWER_3.getID())
    		    			{
    		    				// Store the last Date and Time Answer
    		    				cLastStatusHealthWithAnswer3 = DateTimeUtils.getInstance().FormatDateTimeFromDBToCalendar(context, psm.getDatetimeanswer());
    		    				long lDiffInMilliSecs = Calendar.getInstance().getTimeInMillis() - cLastStatusHealthWithAnswer3.getTimeInMillis(); //result in millis
    		    				// 12 Hours = (12 x 60 x 60 x 1000) = 43200000 ms 
    		    				if(lDiffInMilliSecs > 43200000)
    		    				{
    			    				// reset the Date and Time of Answer 3
    			    				cLastStatusHealthWithAnswer3 = Calendar.getInstance();
    		    					// Add (one of)the patient reference that match with the our Values
    			    				// Check if it's not already checked by this Doctor
    			    				if(psm.getCheckedbydoctor() == 0)
    			    				{
    			    					m_allPatienIDToCheck.add(psm.getPatientid());
    			    				}
    		    				}
    		    			}
    		    		}
    		    		else
    		    		{
    		    			// Reset for the next
    		    			bPatientAlreadyPresent = false;
    		    		}
    	    		}
    			}
    		}		
    		
            if(m_allPatienIDToCheck.isEmpty() == false)
            {
            	 long[] m_alPatienIDToCheck = new long[m_allPatienIDToCheck.size()];
      			 for(int iIndex = 0; iIndex < m_alPatienIDToCheck.length; iIndex++)
      			 {
      				m_alPatienIDToCheck[iIndex] = m_allPatienIDToCheck.get(iIndex);
      			 }
      			 Intent intentToStart = DoctorViewActivity.makeDoctorViewActivity(context, m_dm.getId(), m_alPatienIDToCheck);
    		     PendingIntent pendingIntentToStart = PendingIntent.getActivity(context, 0, intentToStart, Intent.FLAG_ACTIVITY_NEW_TASK);
    		     
    		     m_UpdateNotification = new NotificationCompat.Builder(context)
    		       .setContentTitle(context.getString(R.string.textReminderNotificationDoctorUpdateTitle))
    		       .setContentText(context.getString(R.string.textReminderNotificationDoctorUpdateText))
    		       .setTicker(context.getString(R.string.textReminderNotificationDoctorUpdateTicker))
    		       .setWhen(System.currentTimeMillis())
    		       .setContentIntent(pendingIntentToStart)
    		       .setDefaults(Notification.DEFAULT_SOUND)
    		       .setAutoCancel(true)
    		       .setSmallIcon(R.drawable.android_ic_launcher)
    		       .build();
    		     
    		     m_notificationManager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    		     m_notificationManager.notify(UPDATE_NOTIFICATION_ID, m_UpdateNotification);

    	    }	
        }
	}
}
