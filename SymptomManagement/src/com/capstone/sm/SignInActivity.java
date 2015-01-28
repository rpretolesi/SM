package com.capstone.sm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.DoctorEntry;
import com.capstone.SQL.SQLContract.PatientEntry;
import com.capstone.file.utilities.FileUtilities;
import com.capstone.sm.R;


public class SignInActivity extends Activity 
{
		
	   @Override
	    protected void onCreate(Bundle savedInstanceState) 
	   {
	        super.onCreate(savedInstanceState);
//	        setContentView(R.layout.sign_in_activity);
	        Intent intentLogin = LoginActivity.makeLoginActivity(getApplicationContext());
	        startActivityForResult(intentLogin, LoginActivity.ACTIVITY_RESULT_GET_LOGIN_ID);

	    }

		@Override		
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		    if (requestCode == LoginActivity.ACTIVITY_RESULT_GET_LOGIN_ID) {
		        if(resultCode == RESULT_OK)
		        {
					// Check User Name and Password
		            String strUserName = data.getStringExtra(LoginActivity.USER_NAME);
		            String strPassword = data.getStringExtra(LoginActivity.PASSWORD);
		            long lUserType = data.getIntExtra(LoginActivity.USER_TYPE, 0);
		            
		            // Check if i should Login as Patient or Doctor
					long lUserID = 0;
					// Patient
		            if (lUserType == 0)
		            {
		            	lUserID = PatientEntry.getPatientID(getApplicationContext(), strUserName, strPassword);
		            }
					// Doctor
		            if (lUserType == 1)
		            {
		            	lUserID = DoctorEntry.getDoctorID(getApplicationContext(), strUserName, strPassword);
		            }

					// Check if User Name and Password are ok
			        if (lUserID != 0)
			        {
				    	// Save data for next Sign In
				    	setSignInUserIDAndType(getApplicationContext(), lUserID, lUserType);

				    	// MainActivity is used for initial Activity for choose which of User to run
				        Intent intent = MainActivity.makeMainActivity(getApplicationContext());
				    	if (intent != null)
				    	{
							startActivity(intent);
				    	}
			        }
			        else
			        {
						Toast.makeText(getApplicationContext(), R.string.textUserNameOrPasswordError, Toast.LENGTH_LONG).show();
			        }
		        }
		        
		        if (resultCode == RESULT_CANCELED) 
		        {
					Toast.makeText(getApplicationContext(), R.string.textLoginCanceled, Toast.LENGTH_LONG).show();
		        }
		    }
		    
	        finish();		    		    
		}

		public static void setSignInUserIDAndType(Context context, long lUserID, long lUserType) 
		{
			File loginFile = FileUtilities.getPrivateFile(context, "SignIn.txt");
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(loginFile));
				writer.write(String.valueOf(lUserID));
				writer.newLine();
				writer.write(String.valueOf(lUserType));
				writer.newLine();
				writer.close();
			} catch (Exception e) 
			{
//				Log.e(LOG_TAG, "Problem in loginClicked");
			}
			finally 
			{

			}			    	
			
		}
		/**
		 * Returns the last SignIn input into this activity, or 0 if none is set.
		 */
		public static long getSignInUserID(Context context) 
		{
	        File file = FileUtilities.getPrivateFile(context, "SignIn.txt");
			String out = null;
			
			// If it already exists, read the login ID and return it
			if (file != null && file.exists()) 
			{
				try {
					Scanner sc = new Scanner(file);
					out = sc.nextLine();
					sc.close();
					return Long.parseLong(out);
				} catch (Exception e) {
					// This should never really happen
//					Log.e(LOG_TAG, "Unable to get LoginID from file");
				}
			}

			return 0;
		
		}    
		
		/**
		 * Returns the last password input into this activity, or null if one has not been set
		 */
		public static long getSignInUserType(Context context) {
			// Get the output file for the login information
	        File file = FileUtilities.getPrivateFile(context, "SignIn.txt");
			String out = null;	
		
			// If it already exists, read the login information from the file and display it
			if (file != null && file.exists()) {
				try {
					Scanner sc = new Scanner(file);	// Line 91
					sc.nextLine();
					out = sc.nextLine();
					sc.close();
					return Long.parseLong(out);
				} catch (Exception e) {
					// This should never really happen
//					Log.e(LOG_TAG, "Unable to get password from file.");
				}
			}

			return 0;
		}
		
		public static Intent makeSignInActivity(Context context) 
		{
			Intent intent = new Intent();
			intent.setClass(context, SignInActivity.class);
			return intent;
		}	    
}
