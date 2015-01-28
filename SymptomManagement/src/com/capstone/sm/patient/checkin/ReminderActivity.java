package com.capstone.sm.patient.checkin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.Answer_1;
import com.capstone.SQL.SQLContract.Answer_2;
import com.capstone.SQL.SQLContract.Answer_3;
import com.capstone.SQL.SQLContract.CancerTherapyEntry;
import com.capstone.SQL.SQLContract.PainTherapyEntry;
import com.capstone.SQL.SQLContract.PatientEntry;
import com.capstone.SQL.SQLContract.PatientStatusEntry;
import com.capstone.SQL.SQLContract.Question;
import com.capstone.SQL.SQLContract.User;
import com.capstone.sm.R;
import com.capstone.sm.SignInActivity;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.datetime.DateTimeUtils;
import com.capstone.sm.paintherapy.PainTherapy;
import com.capstone.sm.patient.Patient;
import com.capstone.sm.patient.PatientSvc;
import com.capstone.sm.patientstatus.PatientStatus;
import com.capstone.sm.patientstatus.PatientStatusSvc;
import com.capstone.sm.patientstatus.PatientStatusSvcApi;

public class ReminderActivity extends Activity
{

	// Dialog Pain Therapy List
	private ArrayList<PainTherapy> m_alptm;
	private int m_ialptmIndex;

	private Calendar m_CalendarAnsw;	
	private Calendar m_CalendarTemp;	
    private int m_iYearTemp, m_iMonthTemp, m_iDayTemp, m_iHourTemp, m_iMinuteTemp;

	private Patient m_pm;
	private PatientStatus m_psm;
	private long m_LastCancerTherapyExecID;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_activity);

        if (m_pm == null)
        {
            m_pm = new Patient();
        }

        if (m_psm == null)
        {
        	m_psm = new PatientStatus();
        }

        // Load the Patien who did last Login Successfully
        // From now on all answer will referred to this Patient
        long lUserID = SignInActivity.getSignInUserID(this);
        long lUserType = SignInActivity.getSignInUserType(this);    
        if (lUserType == User.PATIENT.getValue())
        {
        	m_pm = PatientEntry.getPatient(this, lUserID);
        	
        	m_alptm = PainTherapyEntry.getListPainTherapy(getApplicationContext(), m_pm.getId(), m_pm.getDoctorid());
        	m_ialptmIndex = 0;
        	
        	m_LastCancerTherapyExecID = CancerTherapyEntry.getLastCancerTherapyExecutedID(getApplicationContext(), m_pm.getId(), m_pm.getDoctorid());

        }
        else
        {
        	// No user was Login
        	finish();
        }

  		// In the App Title, i set also the Patient User Name that i am working to
		String strAppTitle = getString(R.string.app_name);
		setTitle(strAppTitle + " - " + m_pm.getFirstname() + " " + m_pm.getLastname());     

        // Process to get Current Date
		// Having same DateTime for the answers, it's easiest group by DateTime
		m_CalendarAnsw = Calendar.getInstance();
		m_CalendarTemp = Calendar.getInstance();
        m_iYearTemp = m_CalendarTemp.get(Calendar.YEAR);
        m_iMonthTemp = m_CalendarTemp.get(Calendar.MONTH);
        m_iDayTemp = m_CalendarTemp.get(Calendar.DAY_OF_MONTH);
        m_iHourTemp = m_CalendarTemp.get(Calendar.HOUR_OF_DAY);
        m_iMinuteTemp = m_CalendarTemp.get(Calendar.MINUTE);

    	/*
    	 * First Question
    	 */      
        showQuestion_1_Dialog(""); 
    }

    void showQuestion_1_Dialog(String strTitleParam) 
    {
        DialogFragment newFragment = Question_1_DialogFragment.newInstance(strTitleParam);
        newFragment.show(getFragmentManager(), "Question_1_DialogFragment");
    }
    
    void showQuestion_2_Dialog(String strTitleParam) 
    {
        DialogFragment newFragment = Question_2_DialogFragment.newInstance(strTitleParam);
        newFragment.show(getFragmentManager(), "Question_2_DialogFragment");
    }
    
    void showQuestion_3_Dialog(String strTitleParam) 
    {
        DialogFragment newFragment = Question_3_DialogFragment.newInstance(strTitleParam);
        newFragment.show(getFragmentManager(), "Question_3_DialogFragment");
    }

    void showQuestion_Date_Dialog(String strTitleParam) 
    {
        DialogFragment newFragment = Question_Date_DialogFragment.newInstance(strTitleParam);
        newFragment.show(getFragmentManager(), "Question_Date_DialogFragment");
    }

    void showQuestion_Time_Dialog(String strTitleParam) 
    {
        DialogFragment newFragment = Question_Time_DialogFragment.newInstance(strTitleParam);
        newFragment.show(getFragmentManager(), "Question_Time_DialogFragment");
    }

    public void doOnClick_Question_1(DialogInterface dialog, Answer_1 a1) 
    {
    	/*
    	 * First Dialog Answer
    	 */
		// Add the Data to the Database
        m_psm.setPatientid(m_pm.getId());
        m_psm.setDoctorid(m_pm.getDoctorid());
        m_psm.setLastcancertherapyexecid(m_LastCancerTherapyExecID);
        m_psm.setQuestionid(Question.QUESTION_1.getID());
        if(a1 != null)
        {
        	m_psm.setAnswerid(a1.getID());
        }
        m_psm.setDatetimeanswer(DateTimeUtils.getInstance().FormatDateTimeToDB(getApplicationContext(), m_CalendarAnsw));
        
		if(PatientStatusEntry.addPatientStatus(getApplicationContext(), m_psm) == true)
		{
			// Reset Data
			m_psm.reset();
			
			// First check in dialog done
			if(m_alptm != null && m_alptm.size() > 0)
			{
		    	/*
		    	 * Second Dialog
		    	 */
				showQuestion_2_Dialog(m_alptm.get(m_ialptmIndex).getName());
			}
			else
			{
		    	/*
		    	 * Third Dialog
		    	 */
				showQuestion_3_Dialog(""); 					
			}    	
			
			Toast.makeText(getApplicationContext(), R.string.labelAnswerThankYou, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), R.string.labelAnswerError, Toast.LENGTH_LONG).show();
			
			finish();
		}

    	// For safety
    	dialog.dismiss();
    }
    
    public void doOnClick_Question_2(DialogInterface dialog, Answer_2 a2) 
    {
     	/*
    	 * Second Dialog Answer
    	 */
    	m_psm.setPatientid(m_pm.getId());
        m_psm.setDoctorid(m_pm.getDoctorid());
        m_psm.setLastcancertherapyexecid(m_LastCancerTherapyExecID);
        m_psm.setPaintherapyid(m_alptm.get(m_ialptmIndex).getId());
        m_psm.setQuestionid(Question.QUESTION_2.getID());
        m_psm.setAnswerid(a2.getID());
        m_psm.setDatetimeanswer(DateTimeUtils.getInstance().FormatDateTimeToDB(getApplicationContext(), m_CalendarAnsw));

		// Answer Yes, we should set Date and Time
    	if(a2 == Answer_2.ANSWER_2)
    	{
    		// Date 
    		showQuestion_Date_Dialog("");
          		
    	}
    	else
    	{
    		if(PatientStatusEntry.addPatientStatus(getApplicationContext(), m_psm) == true)
			{
    			// Reset Data
    			m_psm.reset();
    			
    	   		// The answer is No, Let's go with the next Question
    			// Check for Another Pain Therapy
    			m_ialptmIndex++;    		
        		if(m_alptm.size() > m_ialptmIndex)
    			{
    				showQuestion_2_Dialog(m_alptm.get(m_ialptmIndex).getName());
    			}
    			else
    			{
    		    	/*
    		    	 * Third Dialog
    		    	 */
    	            showQuestion_3_Dialog(""); 
    			} 
        		
    			Toast.makeText(getApplicationContext(), R.string.labelAnswerThankYou, Toast.LENGTH_SHORT).show();
			}
    		else
    		{
				Toast.makeText(getApplicationContext(), R.string.labelAnswerError, Toast.LENGTH_LONG).show();
    		}   		
    	
    	}
    	
    	// For safety
    	dialog.dismiss();
    }
    
    public void doOnClick_Question_Date(Calendar calendar) 
    {
    	if (calendar != null)
    	{
            m_iYearTemp = calendar.get(Calendar.YEAR);
            m_iMonthTemp = calendar.get(Calendar.MONTH);
            m_iDayTemp = calendar.get(Calendar.DAY_OF_MONTH);
            
    		// Time 
    		showQuestion_Time_Dialog("");
        }
		else
		{
			Toast.makeText(getApplicationContext(), R.string.labelAnswerError, Toast.LENGTH_LONG).show();
		}
    	
    }

    public void doOnClick_Question_Time(Calendar calendar) 
    {
    	if (calendar != null)
    	{
    		m_iHourTemp = calendar.get(Calendar.HOUR_OF_DAY);
    		m_iMinuteTemp = calendar.get(Calendar.MINUTE);
            
    		// Set Date and Time
        	Calendar cDateTimeSet = Calendar.getInstance();
        	cDateTimeSet.set(Calendar.YEAR, m_iYearTemp);
        	cDateTimeSet.set(Calendar.MONTH, m_iMonthTemp);
        	cDateTimeSet.set(Calendar.DAY_OF_MONTH, m_iDayTemp);
        	cDateTimeSet.set(Calendar.HOUR, m_iHourTemp);
        	cDateTimeSet.set(Calendar.MINUTE, m_iMinuteTemp);

    		// Ok, i finish to set Date and Time and i will save 
            m_psm.setPaintherapydatetimeexec(DateTimeUtils.getInstance().FormatDateTimeToDB(getApplicationContext(), cDateTimeSet));
            
    		if(PatientStatusEntry.addPatientStatus(getApplicationContext(), m_psm) == true)
			{
    			// Reset Data
    			m_psm.reset();

    			// Check for Another Pain Therapy
				m_ialptmIndex++;
    			if(m_alptm.size() > m_ialptmIndex)
    			{
    				showQuestion_2_Dialog(m_alptm.get(m_ialptmIndex).getName());
    			}
    			else
    			{
   		    		/*
    		    	 * Third Dialog
    		    	 */
    	            showQuestion_3_Dialog(""); 
    			}
    			
    			Toast.makeText(getApplicationContext(), R.string.labelAnswerThankYou, Toast.LENGTH_SHORT).show();
			}
    		else
    		{
				Toast.makeText(getApplicationContext(), R.string.labelAnswerError, Toast.LENGTH_LONG).show();
    		}
    	}
		else
		{
			Toast.makeText(getApplicationContext(), R.string.labelAnswerError, Toast.LENGTH_LONG).show();
		}
    }
    	

	public void doOnClick_Question_3(DialogInterface dialog, Answer_3 a3) 
	{
    	/*
    	 * Third Dialog Answer
    	 */
    	m_psm.setPatientid(m_pm.getId());
        m_psm.setDoctorid(m_pm.getDoctorid());
        m_psm.setLastcancertherapyexecid(m_LastCancerTherapyExecID);
        m_psm.setQuestionid(Question.QUESTION_3.getID());
        m_psm.setAnswerid(a3.getID());
        m_psm.setDatetimeanswer(DateTimeUtils.getInstance().FormatDateTimeToDB(getApplicationContext(), m_CalendarAnsw));
        
		if(PatientStatusEntry.addPatientStatus(getApplicationContext(), m_psm) == true)
		{
			Toast.makeText(getApplicationContext(), R.string.labelAnswerThankYou, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), R.string.labelAnswerError, Toast.LENGTH_LONG).show();
		}

		// For safety
		dialog.dismiss();
	
		// Close the activity
		addPatientStatusAsync(getApplicationContext(), m_pm.getId());
 	}

	public void doOnClick_Back(DialogInterface dialog) 
	{
		// Close the activity too
		addPatientStatusAsync(getApplicationContext(), m_pm.getId());
	}
    
	//Add Patients Status
	private void addPatientStatusAsync(Context context, long lPatientID)
	{
		// Check if i am in local mode
    	final Context ctx = context;
		final Collection<PatientStatus> cpsm = PatientStatusEntry.getPatientStatusByRemoteIdEgualZero(ctx, lPatientID);
		if(cpsm != null && cpsm.isEmpty() == false)
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
							// Ok, I update the remote id....
							PatientStatusEntry.updatePatientStatusID(ctx, psm);
						}
						Toast.makeText(ctx, R.string.textReminderNotificationPatientStatusSyncroOk, Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(ctx, R.string.textReminderNotificationPatientStatusSyncroError, Toast.LENGTH_LONG).show();
	    				PatientSvc.close();	
					}

					finish(); 
					
				}
	
				@Override
				public void error(Exception e) 
				{
					Toast.makeText(ctx, R.string.textReminderNotificationPatientStatusSyncroError, Toast.LENGTH_LONG).show();
					PatientSvc.close();	

					finish(); 
					
				}
			});
		}
	}	

	@Override
	public void onResume() 
	{
  		super.onResume();
    }   
	
	public static Intent makeReminderActivity(Context context) 
	{
		Intent intent = new Intent();
		intent.setClass(context, ReminderActivity.class);
		return intent;
	}
}
