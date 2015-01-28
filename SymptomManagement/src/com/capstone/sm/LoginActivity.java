package com.capstone.sm;

import com.capstone.SQL.SQLContract.User;
import com.capstone.sm.R;
import com.capstone.sm.doctor.DoctorSignUpActivity;
import com.capstone.sm.patient.PatientSignUpActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LoginActivity extends Activity 
{
	// ID of the user current Logged in
	public final static int ACTIVITY_RESULT_GET_LOGIN_ID = 1;
	public final static String USER_NAME = "user_name";
	public final static String PASSWORD = "password";
	public final static String USER_TYPE = "user_type";
	
	EditText m_etUserName;
	EditText m_etPassword;
	
	Button m_bLogin;
	Button m_bCancel;

	Spinner m_sUserType;

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;
        
        if (id == R.id.idActionSignUpPatient) 
        {
			intent = PatientSignUpActivity.makeSignUpPatientActivity(this);
			startActivity(intent);
        	
            return true;
        }
        
        if (id == R.id.idActionSignUpDoctor) 
        {
			intent = DoctorSignUpActivity.makeSignUpDoctorActivity(this);
			startActivity(intent);
        	
            return true;
        }
      
        return super.onOptionsItemSelected(item);
    }
    	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        
		m_bLogin = (Button) findViewById(R.id.idButtonLogin);
		m_bCancel = (Button) findViewById(R.id.idButtonCancel);
	
		m_sUserType = (Spinner) findViewById(R.id.spinnerLoginUserType); 
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, User.values());		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		m_sUserType.setAdapter(adapter);
		
		m_bLogin.setOnClickListener(new OnClickListener() 
		{
	    	// I Save new new User On DB
			@Override
			public void onClick(View v) 
			{
				// 	Check User Name and Password
				m_etUserName = (EditText) findViewById(R.id.idEditTextLoginUserName); 
				m_etPassword = (EditText) findViewById(R.id.idEditTextLoginPassword); ; 

				Intent returnIntent = new Intent();
				returnIntent.putExtra(USER_NAME, m_etUserName.getText().toString());
				returnIntent.putExtra(PASSWORD, m_etPassword.getText().toString());
				returnIntent.putExtra(USER_TYPE, m_sUserType.getSelectedItemPosition());
				setResult(RESULT_OK, returnIntent);
				
				finish();
			}
		});
		
		m_bCancel.setOnClickListener(new OnClickListener() 
		{
	    	// I Save new new User On DB
			@Override
			public void onClick(View v) 
			{
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				
				finish();
			}
		});		
    }
   
	public static Intent makeLoginActivity(Context context) 
	{
		Intent intent = new Intent();
		intent.setClass(context, LoginActivity.class);
		return intent;
	}		   
}
