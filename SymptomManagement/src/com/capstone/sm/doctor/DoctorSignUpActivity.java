package com.capstone.sm.doctor;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.Callable;

import retrofit.mime.TypedFile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.DoctorEntry;
import com.capstone.bitmap.utilities.BitmapUtil;
import com.capstone.file.utilities.FileUtilities;
import com.capstone.sm.MainActivity;
import com.capstone.sm.R;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;

public class DoctorSignUpActivity extends Activity 
{
	static final String STATE_PATH_Doctor_PICTURE = "PathDoctorPicture";
	static final int CAMERA_PIC_REQUEST = 1;

	private Doctor m_dm;
    private EditText m_etFirstName; 
    private EditText m_etLastName;  
    private EditText m_etIdentificationID; 
    private EditText m_etUserName;  
    private EditText m_etPassword; 
    private ImageView m_ivDoctorPicture;
    private File m_fPathPicture;
    private String m_strPathPicture;
    private Button m_bGetPicture;
    private Button m_bAddDoctor;
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_doctor_activity);
        m_dm = new Doctor();
        
        m_etFirstName = (EditText) this.findViewById(R.id.idEditTextSignUpDoctorFirstName); 
        m_etLastName = (EditText) this.findViewById(R.id.idEditTextSignUpDoctorLastName);
        m_etIdentificationID = (EditText) this.findViewById(R.id.idEditTextSignUpDoctorIdentificationID); 
        m_etUserName = (EditText) this.findViewById(R.id.idEditTextSignUpDoctorUserName);  
        m_etPassword = (EditText) this.findViewById(R.id.idEditTextSignUpDoctorPassword); 
        
        m_ivDoctorPicture = (ImageView) this.findViewById(R.id.idImageViewSignUpDoctorPicture); ;
		m_strPathPicture = "";

         // Get Picture
        m_bGetPicture = (Button) findViewById(R.id.idButtonSignUpDoctorGetPicture);
        // Add Doctor Picture
        m_bAddDoctor = (Button) findViewById(R.id.idButtonSignUpDoctorAddDoctor);
        	
         // Save
		m_bAddDoctor.setOnClickListener(new OnClickListener() 
 		{
 			@Override
 			public void onClick(View v) 
 			{
 				// I check the lenght of the input
 				if(m_etUserName.getText().length() > 0 && m_etPassword.getText().length() > 0)
 				{
					// First i check if locally exist the same UserName
 					if (DoctorEntry.IsDoctorUserNamePresent(getApplication(), m_etUserName.getText().toString()) == false)
 					{
 						m_dm.setFirstname(m_etFirstName.getText().toString());
 						m_dm.setLastname(m_etLastName.getText().toString());
 						m_dm.setIdentificationid(m_etIdentificationID.getText().toString());
 						m_dm.setUsername(m_etUserName.getText().toString());
 						m_dm.setPassword(m_etPassword.getText().toString());
 						m_dm.setImage(null); // This is used only locally
 						m_dm.setContenttype("picture/jpg");
 						
 						// Check if i am in local mode
 				        if(MainActivity.LOCAL_MODE == false)
 				        {
 	 						// I get the connection
 	 						final DoctorSvcApi svc = DoctorSvc.get(getApplicationContext());

 	 						// Ok, i will check if remotely exist the same UserName
 	 						CallableTask.invoke(new Callable<Collection<Doctor>>() {

 	 							@Override
 	 							public Collection<Doctor> call() throws Exception {
 	 								return svc.getDoctorByUsername(m_dm.getUsername());
 	 							}
 	 						}, new TaskCallback<Collection<Doctor>>() 
 	 						{
 	 							@Override
 	 							public void success(Collection<Doctor> result) 
 	 							{
 	 								if(result != null)
 	 								{
	 	 								// I check if already exist
	 	 								if(result.isEmpty() == true)
	 	 								{
		 	 		 						// Ok i can add the patient
		 	 	 	 			      		CallableTask.invoke(new Callable<Doctor>() 
		 	 	 		      				{
		 	 	 		      	    			@Override
		 	 	 		      	    			public Doctor call() throws Exception 
		 	 	 		      	    			{
		 	 	 		      	    				return svc.addDoctor(m_dm);
		 	 	 		      	    			}
		 	 	 		      	    		}, new TaskCallback<Doctor>() 
		 	 	 		      	    		{
		
		 	 	 		      	    			@Override
		 	 	 		      	    			public void success(Doctor result) 
		 	 	 		      	    			{
		 	 	 		      	    				if(result != null && result.getId() > 0)
		 	 	 		      	    				{
		 	 	 		      	    					final Doctor dm = result;
		 	 	 		      	    					final TypedFile ptf = new TypedFile(result.getContenttype(), m_fPathPicture);
		 	 	 		      	    					
			 	 	 		      	    				// now i add the Picture of the Patient
					 	 		 						// Ok i can add the patient
					 	 	 	 			      		CallableTask.invoke(new Callable<Void>() 
					 	 	 		      				{
					 	 	 		      	    			@Override
					 	 	 		      	    			public Void call() throws Exception 
					 	 	 		      	    			{
					 	 	 		      	    				return svc.setDoctorPictureData(dm.getId(), ptf);
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
					 	 	 		      	    				DoctorSvc.close();	
					 	 	 		      	    			}
					 	 	 		      	    		}); 	 		 	 	 		      	    					
		 	 	 		      	    				}		 	 	 		      	    				
		 	 	 		      	    				
		 	 	 		      	    				if(result != null && result.getId() > 0)
		 	 	 		      	    				{
				 	 	 		      	    			// Ok, i add the doctor locally
			 	 	 		      	    				addDoctor(getApplicationContext(), result);
		 	 	 		      	    				}
		 	 	 		      	    				else
		 	 	 		      	    				{
			 	 	 		      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpDoctorAddDoctorError, Toast.LENGTH_LONG).show();	 	 	 		      	    					
		 	 	 		      	    				}
		 	 	 		      	    			}
		
		 	 	 		      	    			@Override
		 	 	 		      	    			public void error(Exception e) 
		 	 	 		      	    			{
		 	 	 		      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpDoctorAddDoctorError, Toast.LENGTH_LONG).show();
		 	 	 		      	    				DoctorSvc.close();	
		 	 	 		      	    			}
		 	 	 		      	    		}); 	 	 								
	 	 								}
	 	 								else
	 	 								{
	 	 									// Username already present in the remote DB,
	 	 									// this can happen if i uninstall the app.
	 	 									// In this case i will retrieve the information
	 	 									Doctor dm = result.iterator().next();
	 	 		 				        	addDoctor(getApplicationContext(), dm);
	 	 								}
 	 								}
 	 								else
 	 								{
 			      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpDoctorAddDoctorError, Toast.LENGTH_LONG).show();
 			      	    				DoctorSvc.close();	
	 								}
 	 							}

 	 							@Override
 	 							public void error(Exception e) 
 	 							{
		      	    				Toast.makeText(getApplicationContext(), R.string.textSignUpDoctorAddDoctorError, Toast.LENGTH_LONG).show();
		      	    				DoctorSvc.close();	
 	 							}
 	 						});
 	 						
 			        	
 				        }
 				        else
 				        {
 				        	long lID = DoctorEntry.getNextID(getApplicationContext());
 				        	m_dm.setId(lID);
 				        	m_dm.setImage(BitmapUtil.getInstance().getBytes(BitmapUtil.getInstance().getBitmap(m_ivDoctorPicture)));
 				        	addDoctor(getApplicationContext(), m_dm);
 				        } 						
 
 					}
 					else
 					{
 						Toast.makeText(getApplication(), R.string.textUserNameAlreadyPresent, Toast.LENGTH_LONG).show();
 					}
				}
 				else
 				{
 					Toast.makeText(getApplication(), R.string.textInvalidData, Toast.LENGTH_LONG).show();
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
	 				m_fPathPicture = FileUtilities.getPublicCacheFile(getApplicationContext(), "Doctor_" + m_etUserName.getText().toString()+".jpg");
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
	 					Toast.makeText(getApplication(), R.string.textInvalidData, Toast.LENGTH_LONG).show();						 					
	 				}
 	 			}
 				else
 				{
 					Toast.makeText(getApplication(), R.string.textInvalidData, Toast.LENGTH_LONG).show();					
 				}
 			}
 		});  

	}
	
	private void addDoctor(Context context, Doctor dm)
	{
		if (DoctorEntry.addDoctor(getApplicationContext(), dm) == true)
		{
			Toast.makeText(context, R.string.textSignUpDoctorAddDoctorOk, Toast.LENGTH_LONG).show();
			
			finish();
		}
		else
		{
			Toast.makeText(context, R.string.textSignUpDoctorAddDoctorError, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	    // Save the user's current game state
	    savedInstanceState.putString(STATE_PATH_Doctor_PICTURE, m_strPathPicture);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    m_strPathPicture = savedInstanceState.getString(STATE_PATH_Doctor_PICTURE);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == CAMERA_PIC_REQUEST) 
		{
			if (resultCode == RESULT_OK) 
			{
				// m_strPathDoctorPicture is set by CAMERA App
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

		BitmapUtil.getInstance().loadBitmap(getResources(), android.R.drawable.ic_menu_camera, m_strPathPicture, m_ivDoctorPicture, 100, 100);
		
		// Because i don't have understood why it's 90° degree from camera!
		if (m_strPathPicture != "")
		{
			m_ivDoctorPicture.setRotation(270);
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_setting, menu);
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
        
        if (id == R.id.action_settings_doctor) 
        {
			intent = DoctorSettingActivity.makeDoctorSettingActivity(getApplicationContext(), m_dm.getId());
			startActivity(intent);
        	
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
 	
	public static Intent makeSignUpDoctorActivity(Context context) 
	{
		Intent intent = new Intent();
		intent.setClass(context, DoctorSignUpActivity.class);
//		intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
		return intent;
	}	
}
