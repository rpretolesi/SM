package com.capstone.sm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.doctor.DoctorViewActivity;
import com.capstone.sm.patient.PatientViewActivity;


public class MainActivity extends ActionBarActivity 
{
	// This allow to debug all App locally
	public static boolean LOCAL_MODE; 
	public static String m_strHostAddress; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        LOCAL_MODE = false;
        m_strHostAddress = Settings.getParameter(getApplicationContext(), Parameter.HOST_ADDRESS);
       	if(m_strHostAddress.length() == 0)
    	{
       		//m_strHostAddress = "https://10.0.2.2:8443";
       		m_strHostAddress = "https://192.168.5.31:8443";
    	}

        
        long lUserID = SignInActivity.getSignInUserID(getApplicationContext());
        long lUserType = SignInActivity.getSignInUserType(getApplicationContext());    

        Intent intent = null;
        
        if (lUserID != 0)
        {
	    	// MainActivity is used for initial Activity for choose which of User to run
	        intent = MainActivity.makeIntent(getApplicationContext(), lUserID, lUserType);
        }
        else
        {
	        intent = SignInActivity.makeSignInActivity(getApplicationContext());
      	
        }

        if (intent != null)
    	{
			startActivity(intent);
    	}
        
    	finish();
    }
  		
	public static Intent makeIntent(Context context, long lUserID, long lUserType)
	{
		
    	// we use different logic and layout depending if the User is the Patient or Doctor
		Intent intent = null;
		
    	if(lUserType == 0)
    	{
			intent = PatientViewActivity.makePatientViewActivity(context, lUserID);
    	}
    	// we use different logic and layout depending if the User is the Patient or Doctor
    	if(lUserType == 1)
    	{
			intent = DoctorViewActivity.makeDoctorViewActivity(context, lUserID, null);
    	}		
    	return intent;
	}

	public static Intent makeMainActivity(Context context) 
	{
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		return intent;
	}
}
