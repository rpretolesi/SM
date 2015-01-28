package com.capstone.sm.patient.checkin;

import java.util.Collection;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.PatientStatusEntry;
import com.capstone.sm.BaseBroadcastReceiver;
import com.capstone.sm.R;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.patient.PatientSvc;
import com.capstone.sm.patient.PatientViewActivity;
import com.capstone.sm.patientstatus.PatientStatus;
import com.capstone.sm.patientstatus.PatientStatusSvc;
import com.capstone.sm.patientstatus.PatientStatusSvcApi;

public class ReminderUpdateAlarmReceiver extends BaseBroadcastReceiver 
{
	private final static String ID = "id";
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

        if(context != null && intent != null)
        {
        	final Context ctx = context;
        	final long lPatientID = intent.getLongExtra(ReminderUpdateAlarmReceiver.ID, 0);

        	if(lPatientID > 0)
            {
        		addPatientStatusAsync(ctx, lPatientID);
			}
		}
	}
	
	
	//Add Patients Status
	private void addPatientStatusAsync(Context context, long lPatientID)
	{
    	final Context ctx = context;
		final Collection<PatientStatus> cpsm = PatientStatusEntry.getPatientStatusByRemoteIdEgualZero(ctx, lPatientID);
		if(ctx != null && cpsm != null && cpsm.isEmpty() == false)
		{
			// I get the connection
			final PatientStatusSvcApi svc = PatientStatusSvc.get(ctx);
	
			// Ok, i will save it remotely
			CallableTask.invoke(new Callable <Collection<PatientStatus>>() {
	
				@Override
				public Collection<PatientStatus> call() throws Exception 
				{				
					return svc.addPatientStatus(cpsm);
				}
			}, new TaskCallback<Collection<PatientStatus>>() 
			{
				@Override
				public void success(Collection<PatientStatus> result) 
				{
					if(result != null && result.isEmpty() == false)
					{
						for (PatientStatus psm : result)
						{
							if(psm != null)
							{
								// Ok, I update the remote id....
								PatientStatusEntry.updatePatientStatusID(ctx, psm);
							}
						}
						Toast.makeText(ctx, R.string.textReminderNotificationPatientStatusSyncroOk, Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(ctx, R.string.textReminderNotificationPatientStatusSyncroError, Toast.LENGTH_LONG).show();
	    				PatientSvc.close();	
					}
				}
	
				@Override
				public void error(Exception e) 
				{
					Toast.makeText(ctx, R.string.textReminderNotificationPatientStatusSyncroError, Toast.LENGTH_LONG).show();
					PatientSvc.close();	
				}
			});
		}
	}		
	
	public static Intent makeReminderUpdateAlarmReceiver(Context context, long lPatientID) 
	{
		Intent intent = new Intent();
		intent.setClass(context, ReminderUpdateAlarmReceiver.class);
		intent.putExtra(ID, lPatientID);	
		return intent;
	}	


}
