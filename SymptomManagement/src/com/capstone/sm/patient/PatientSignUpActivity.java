package com.capstone.sm.patient;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.PatientEntry;
import com.capstone.bitmap.utilities.BitmapUtil;
import com.capstone.file.utilities.FileUtilities;
import com.capstone.sm.MainActivity;
import com.capstone.sm.R;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.datetime.DateTimeUtils;
import retrofit.mime.TypedFile;

public class PatientSignUpActivity extends Activity 
{
	static final String STATE_PATH_PATIENT_PICTURE = "PathPatientPicture";
	static final int CAMERA_PIC_REQUEST = 1;

	private Patient m_pm;
    private EditText m_etFirstName; 
    private EditText m_etLastName;  
    private EditText m_etDateOfBirth; 
    private Button m_bSetDateOfBirth; 
    private EditText m_etMedicalRecordID; 
    private EditText m_etUserName;  
    private EditText m_etPassword; 
    private ImageView m_ivPatientPicture;
    private File m_fPathPicture;
    private String m_strPathPicture;
    private Button m_bGetPicture;
    private Button m_bAddPatient;
    
	// Variable for storing date
	private Calendar m_Calendar;
    private int m_iYear, m_iMonth, m_iDay;

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_patient_activity);
        m_pm = new Patient();
        
        m_etFirstName = (EditText) this.findViewById(R.id.idEditTextSignUpPatientFirstName); 
        m_etLastName = (EditText) this.findViewById(R.id.idEditTextSignUpPatientLastName);
        m_etDateOfBirth = (EditText) this.findViewById(R.id.idEditTextSignUpPatientDateOfBirth);
        m_etDateOfBirth.setKeyListener(null);
        m_etMedicalRecordID = (EditText) this.findViewById(R.id.idEditTextSignUpPatientMedicalRecordID); 
        m_etUserName = (EditText) this.findViewById(R.id.idEditTextSignUpPatientUserName);  
        m_etPassword = (EditText) this.findViewById(R.id.idEditTextSignUpPatientPassword); 
        
        m_ivPatientPicture = (ImageView) this.findViewById(R.id.idImageViewSignUpPatientPicture); ;
		m_strPathPicture = "";

        // Get Picture
		m_bSetDateOfBirth = (Button) findViewById(R.id.idButtonSignUpSetPatientDateOfBirth);
        // Get Picture
        m_bGetPicture = (Button) findViewById(R.id.idButtonSignUpPatientGetPicture);
        // Add Patient Picture
        m_bAddPatient = (Button) findViewById(R.id.idButtonSignUpPatientAddPatient);

        m_Calendar = Calendar.getInstance();
      	
        // Set Date
        m_bSetDateOfBirth.setOnClickListener(new OnClickListener() 
 		{
 			@Override
 			public void onClick(View v) 
 			{
   	            // Process to get Current Date
   	            m_iYear = m_Calendar.get(Calendar.YEAR);
	            m_iMonth = m_Calendar.get(Calendar.MONTH);
	            m_iDay = m_Calendar.get(Calendar.DAY_OF_MONTH);
	 
	            // Launch Date Picker Dialog
	            DatePickerDialog dpd = new DatePickerDialog(PatientSignUpActivity.this,
	                    new DatePickerDialog.OnDateSetListener() 
	            {
	                @Override
	                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	                {
	                	m_iYear = year;
	                	m_iMonth = monthOfYear;
	                	m_iDay = dayOfMonth;
	                	
	                	// Display Selected date in textbox
   	       	            m_Calendar.set(Calendar.YEAR, m_iYear);
    	    	        m_Calendar.set(Calendar.MONTH, m_iMonth);
    	    	        m_Calendar.set(Calendar.DAY_OF_MONTH, m_iDay);

    	    	        m_etDateOfBirth.setText(DateTimeUtils.getInstance().FormatDate(getApplicationContext(), m_Calendar));	 
	                }
	                
	            }, m_iYear, m_iMonth, m_iDay);

	            dpd.show();
 			}
 		});  
		
        // Save
		m_bAddPatient.setOnClickListener(new OnClickListener() 
 		{
 			@Override
 			public void onClick(View v) 
 			{
 				// I check the lenght of the input
 				if(m_etUserName.getText().length() > 0 && m_etPassword.getText().length() > 0)
 				{
 					// First i check if locally exist the same UserName
 					if (PatientEntry.IsPatientUserNamePresent(getApplication(), m_etUserName.getText().toString()) == false)
 					{
 						// i compile the data for the patient
						m_pm.setFirstname(m_etFirstName.getText().toString());
 						m_pm.setLastname(m_etLastName.getText().toString());
 						m_pm.setDateofbirth(DateTimeUtils.getInstance().FormatDateTimeToDB(getApplicationContext(), m_Calendar));
 						m_pm.setMedicalrecordid(m_etMedicalRecordID.getText().toString());
 						m_pm.setUsername(m_etUserName.getText().toString());
 						m_pm.setPassword(m_etPassword.getText().toString());
 						m_pm.setImage(null); // This is used only locally
  						m_pm.setContenttype("picture/jpg");
 						
 						// Check if i am in local mode
 				        if(MainActivity.LOCAL_MODE == false)
 				        {
 	 						// I get the connection
 	 						final PatientSvcApi svc = PatientSvc.get(getApplicationContext());

 	 						// Ok, i will check if remotely exist the same UserName
 	 						CallableTask.invoke(new Callable<Collection<Patient>>() {

 	 							@Override
 	 							public Collection<Patient> call() throws Exception {
 	 								return svc.getPatientByUsername(m_pm.getUsername());
 	 							}
 	 						}, new TaskCallback<Collection<Patient>>() 
 	 						{
 	 							@Override
 	 							public void success(Collection<Patient> result) 
 	 							{
 	 								if(result != null)
 	 								{
	 	 								// I check if already exist
	 	 								if(result.isEmpty() == true)
	 	 								{
		 	 		 						// Ok i can add the patient
		 	 	 	 			      		CallableTask.invoke(new Callable<Patient>() 
		 	 	 		      				{
		 	 	 		      	    			@Override
		 	 	 		      	    			public Patient call() throws Exception 
		 	 	 		      	    			{
		 	 	 		      	    				return svc.addPatient(m_pm);
		 	 	 		      	    			}
		 	 	 		      	    		}, new TaskCallback<Patient>() 
		 	 	 		      	    		{
		
		 	 	 		      	    			@Override
		 	 	 		      	    			public void success(Patient result) 
		 	 	 		      	    			{
		 	 	 		      	    				if(result != null && result.getId() > 0)
		 	 	 		      	    				{
		 	 	 		      	    					final Patient pm = result;
		 	 	 		      	    					final TypedFile ptf = new TypedFile(result.getContenttype(), m_fPathPicture);
		 	 	 		      	    					
			 	 	 		      	    				// now i add the Picture of the Patient
					 	 		 						// Ok i can add the patient
					 	 	 	 			      		CallableTask.invoke(new Callable<Void>() 
					 	 	 		      				{
					 	 	 		      	    			@Override
					 	 	 		      	    			public Void call() throws Exception 
					 	 	 		      	    			{
					 	 	 		      	    				return svc.setPatienPictureData(pm.getId(), ptf);
					 	 	 		      	    			}
					 	 	 		      	    		}, new TaskCallback<Void>() 
					 	 	 		      	    		{
					
					 	 	 		      	    			@Override
					 	 	 		      	    			public void success(Void result) 
					 	 	 		      	    			{

					 	 	 		      	    			}
					 	 	 		      	   		
					 	 	 		      	    			@Override
					 	 	 		      	    			public void error(Exception e) 
					 	 	 		      	    			{
					 	 	 		      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
					 	 	 		      	    				PatientSvc.close();	
					 	 	 		      	    			}
					 	 	 		      	    		}); 	 		 	 	 		      	    					
		 	 	 		      	    				}		 	 	 		      	    				

		 	 	 		      	    				if(result != null && result.getId() > 0)
		 	 	 		      	    				{
				 	 	 		      	    			// Ok, i add the patient locally
			 	 	 		      	    				addPatient(getApplicationContext(), result);
		 	 	 		      	    				}
		 	 	 		      	    				else
		 	 	 		      	    				{
			 	 	 		      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();	 	 	 		      	    					
		 	 	 		      	    				}
		 	 	 		      	    			}
		
		 	 	 		      	    			@Override
		 	 	 		      	    			public void error(Exception e) 
		 	 	 		      	    			{
		 	 	 		      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
		 	 	 		      	    				PatientSvc.close();	
		 	 	 		      	    			}
		 	 	 		      	    		}); 	 	 								
	 	 								}
	 	 								else
	 	 								{
	 	 									// Username already present in the remote DB,
	 	 									// this can happen if i uninstall the app.
	 	 									// In this case i will retrieve the information
	 	 									// In this case also the image will be present, so i don't send it again
	 	 									Patient pm = result.iterator().next();
	 	 		 				        	addPatient(getApplicationContext(), pm);
	 	 								}
 	 								}
 	 								else
 	 								{
 			      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
 	 		      	    				PatientSvc.close();	
	 								}
 	 							}

 	 							@Override
 	 							public void error(Exception e) 
 	 							{
		      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
 		      	    				PatientSvc.close();	
 	 							}
 	 						});
 	 						
 			        	
 				        }
 				        else
 				        {
 				        	long lID = PatientEntry.getNextID(getApplicationContext());
 				        	m_pm.setId(lID);
 							m_pm.setImage(BitmapUtil.getInstance().getBytes(BitmapUtil.getInstance().getBitmap(m_ivPatientPicture)));
 				        	addPatient(getApplicationContext(), m_pm);
 				        }
						
					}
 					else
 					{
 						Toast.makeText(getApplicationContext(), R.string.textUserNameAlreadyPresent, Toast.LENGTH_LONG).show();
 					}
				}
 				else
 				{
 					Toast.makeText(getApplicationContext(), R.string.textInvalidData, Toast.LENGTH_LONG).show();
 				} 			 
 				
 			}
 		});  
 		
		m_bGetPicture.setOnClickListener(new OnClickListener() 
 		{
 			@Override
 			public void onClick(View v) 
 			{
 				if(m_etUserName.getText().length() > 0 && m_etPassword.getText().length() > 0)
 				{
	 				// Create an intent that asks for an image to be captured
	 				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	 				
	 				// Tell the capturing activity where to store the image
	 				m_fPathPicture = FileUtilities.getPublicCacheFile(getApplicationContext(), "Patient_" + m_etUserName.getText().toString()+".jpg");
	 				m_strPathPicture = m_fPathPicture.getPath();
	 				// If the file already exist, i will delete it.
	 				m_fPathPicture.delete();

	 				Uri uriPath = Uri.fromFile(m_fPathPicture);
 					
	 				if (uriPath != null)
	 				{
	 	 				cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriPath);
	 	 				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	 				}
	 				else
	 				{
	 					Toast.makeText(getApplicationContext(), R.string.textInvalidData, Toast.LENGTH_LONG).show();						 					
	 				}
 	 			}
 				else
 				{
 					Toast.makeText(getApplicationContext(), R.string.textInvalidData, Toast.LENGTH_LONG).show();					
 				}
 			}
 		});  

	}
	
	private void addPatient(Context context, Patient pm)
	{
		if (PatientEntry.addPatient(context, pm) == true)
		{
			Toast.makeText(context, R.string.textSignUpPatientAddPatientOk, Toast.LENGTH_LONG).show();
			
			finish();
		}
		else
		{
			Toast.makeText(context, R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	    // Save the user's current state
	    savedInstanceState.putString(STATE_PATH_PATIENT_PICTURE, m_strPathPicture);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    m_strPathPicture = savedInstanceState.getString(STATE_PATH_PATIENT_PICTURE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == CAMERA_PIC_REQUEST) 
		{
			if (resultCode == RESULT_OK) 
			{
				// m_strPathPatientPicture is set by CAMERA App
			} 
			else
			{
				m_strPathPicture = "";
			}
		} 
	}
	
	@Override
	public void onResume()
	{
		super.onResume();

		BitmapUtil.getInstance().loadBitmap(getResources(), android.R.drawable.ic_menu_camera, m_strPathPicture, m_ivPatientPicture, 100, 100);
		
		// Because i don't have understood why it's 90° degree from camera!
		if (m_strPathPicture != "")
		{
			m_ivPatientPicture.setRotation(270);
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_setting, menu);
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
        
        if (id == R.id.action_settings_patient) 
        {
			intent = PatientSettingActivity.makePatientSettingActivity(getApplicationContext(), m_pm.getId());
			startActivity(intent);
        	
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
	
	public static Intent makeSignUpPatientActivity(Context context) 
	{
		Intent intent = new Intent();
		intent.setClass(context, PatientSignUpActivity.class);
		return intent;
	}	
}
