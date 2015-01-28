package com.capstone.sm.patient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

import retrofit.client.Header;
import retrofit.client.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.DoctorEntry;
import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.PatientEntry;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.SQL.SQLContract.User;
import com.capstone.sm.LoginActivity;
import com.capstone.sm.MainActivity;
import com.capstone.sm.R;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.doctor.Doctor;
import com.capstone.sm.doctor.DoctorListAdapter;
import com.capstone.sm.doctor.DoctorSvc;
import com.capstone.sm.doctor.DoctorSvcApi;

public class PatientSettingActivity extends PatientBaseBarActivity 
{
	public final static int ACTIVITY_SET_DOCTOR_LOGIN = 1;

	private final static String ID = "id";

	   /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
        {
            @Override
            public void onPageSelected(int position) 
            {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
    {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        
        String strTAG = mSectionsPagerAdapter.getFragmentTag(mViewPager.getId(), tab.getPosition());

        // Update data in single fragment
        if(tab.getPosition() == 0)
        {
            PatientDoctorFragment pdf = (PatientDoctorFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(pdf != null)
            {
            	if(pdf.isResumed() == true)
            	{
            		pdf.UpdateFragment();
            	}
            }
        }
        if(tab.getPosition() == 1)
        {
        	PatientSettingFragment psf = (PatientSettingFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(psf != null)
            {
            	if(psf.isResumed() == true)
            	{
            		psf.UpdateFragment();
                }
            }
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
    {
    }
    
    @Override
	public void onResume() 
	{
  		super.onResume();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter 
    {

        public SectionsPagerAdapter(FragmentManager fm) 
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) 
        {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragment = null;
            if (position == 0) 
            {
            	fragment = PatientDoctorFragment.newInstance(position + 1);
            }
            if (position == 1) 
            {
           		fragment = PatientSettingFragment.newInstance(position + 1);
            }

            return fragment;        	
        }

        @Override
        public int getCount() 
        {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) 
        {
      		Locale l = Locale.getDefault();
            switch (position) {
                case 0:
               		return getString(R.string.title_section_patient_choose_doctor).toUpperCase(l);
                        	
                case 1:
                    return getString(R.string.title_section_patient_setting).toUpperCase(l);
                    
            }
            return null;
        }
        
        private String getFragmentTag(int viewPagerId, int fragmentPosition)
        {
             return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
        }        
    }
    
    /**
     * A DoctorFragment fragment.
     */
    public static class PatientDoctorFragment extends ListFragment 
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private int iSelectedListItemPosition;
        private DoctorListAdapter m_adapter;
    	private ArrayList<Doctor> m_aldm;
        
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PatientDoctorFragment newInstance(int sectionNumber) 
        {
        	PatientDoctorFragment fragment = new PatientDoctorFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PatientDoctorFragment() 
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
      		return super.onCreateView(inflater, container, savedInstanceState);
        }
        
    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) 
    	{
    		super.onActivityCreated(savedInstanceState);

    		setHasOptionsMenu(true);

    		m_adapter = new DoctorListAdapter(getActivity());
    		setListAdapter(m_adapter);	    		

    	}
    	
       	@Override
		public void onListItemClick(ListView l, View v, int position, long id) 
    	{
    		super.onListItemClick(l, v, position, id);
    		
    		// Store the position
    		iSelectedListItemPosition = position;
    		
    		// For set the Doctor i must Login
	        Intent intentLogin = LoginActivity.makeLoginActivity(getActivity());
	        startActivityForResult(intentLogin, ACTIVITY_SET_DOCTOR_LOGIN);

    	}
    	
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
		    if (requestCode == ACTIVITY_SET_DOCTOR_LOGIN) 
		    {
		        if(resultCode == RESULT_OK)
		        {
					// Check User Name and Password
		            String strUserName = data.getStringExtra(PatientEntry.COLUMN_NAME_USER_NAME);
		            String strPassword = data.getStringExtra(PatientEntry.COLUMN_NAME_PASSWORD);
		            long lUserType = data.getIntExtra(LoginActivity.USER_TYPE, 0);
					long lUserID = PatientEntry.getPatientID(getActivity().getApplication(), strUserName, strPassword);

					// Check if User Name and Password are ok
			        if (lUserID > 0 && m_pm != null && lUserID == m_pm.getId() && lUserType == User.PATIENT.getValue())
			        {
			        	// Update the Patient remotely
				        if(MainActivity.LOCAL_MODE == false)
				        {
							// I get the connection
							final PatientSvcApi svc = PatientSvc.get(getActivity().getApplicationContext());
							
							CallableTask.invoke(new Callable<Patient>() {
	
								@Override
								public Patient call() throws Exception {
									m_pm.setDoctorid(m_aldm.get(iSelectedListItemPosition).getId());
									return svc.updatePatient(m_pm);
								}
							}, new TaskCallback<Patient>() 
							{
								@Override
								public void success(Patient result) 
								{
					        		if(result != null)
					            	{
					        			//Update ok
										// Set Doctor ID
										boolean bResult = PatientEntry.setPatientDoctorID(getActivity().getApplicationContext(), m_pm.getId(), m_aldm.get(iSelectedListItemPosition).getId());
										
										if(bResult == true)
										{
											// Double check
											long lDoctorID = PatientEntry.getPatientDoctorID(getActivity().getApplicationContext(), m_pm.getId());
				
											// Double check
											m_pm.setDoctorid(lDoctorID);
											m_dm = DoctorEntry.getDoctor(getActivity().getApplicationContext(), m_pm.getDoctorid());

											Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewChoosenDoctorOk, Toast.LENGTH_LONG).show();

											// Aggiorno
								    		// Update App Title
								    		UpdateFragment();
										}
										else
										{
											Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewChoosenDoctorError, Toast.LENGTH_LONG).show();
										}
					            	}
								}
	
								@Override
								public void error(Exception e) 
								{
						        	Toast.makeText(getActivity().getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
						        	PatientSvc.close();	
								}
							});			        	
			        	
				        }
				        else
				        {
							// Set Doctor ID
							boolean bResult = PatientEntry.setPatientDoctorID(getActivity().getApplicationContext(), m_pm.getId(), m_aldm.get(iSelectedListItemPosition).getId());
							
							if(bResult == true)
							{
								// Double check
								long lDoctorID = PatientEntry.getPatientDoctorID(getActivity().getApplicationContext(), m_pm.getId());
	
								// Double check
								m_pm.setDoctorid(lDoctorID);
								m_dm = DoctorEntry.getDoctor(getActivity().getApplicationContext(), m_pm.getDoctorid());

								Toast.makeText(getActivity(), R.string.labelTextViewChoosenDoctorOk, Toast.LENGTH_LONG).show();
								
								// Aggiorno
					    		// Update App Title
					    		UpdateFragment();
							}
							else
							{
								Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewChoosenDoctorError, Toast.LENGTH_LONG).show();
							}
				        }
			        }
			        else
			        {
						Toast.makeText(getActivity().getApplicationContext(), R.string.textUserNameOrPasswordError, Toast.LENGTH_LONG).show();
			        }
		        }
		        
		        if (resultCode == RESULT_CANCELED) 
		        {
					Toast.makeText(getActivity().getApplicationContext(), R.string.textLoginCanceled, Toast.LENGTH_LONG).show();
		        }
		    }
		        		    
		}    	
		
    	@Override
    	public void onResume() 
    	{
      		super.onResume();
      		
      		UpdateFragment();
    	}
        
       	@Override
    	public void onDestroyView()
    	{
    	    super.onDestroyView();

    	    // free adapter
    	    setListAdapter(null);
    	}
       	
        private void UpdateFragment()
        {
			// Check if i am in local mode
	        if(MainActivity.LOCAL_MODE == false)
	        {
				// I get the connection
				final DoctorSvcApi svc = DoctorSvc.get(getActivity().getApplicationContext());
				
				CallableTask.invoke(new Callable<Collection<Doctor>>() {

					@Override
					public Collection<Doctor> call() throws Exception {
						return svc.getDoctorList();
					}
				}, new TaskCallback<Collection<Doctor>>() 
				{
					@Override
					public void success(Collection<Doctor> result) 
					{
		        		if(result != null)
		            	{
		        			// i Update the Local Table of the Doctors
					        for (Doctor dm : result)
					        {
					        	// I will delete UserName and Password
					        	dm.setUsername("");
					        	dm.setPassword("");
								// Store all Doctor in the Local DB if not already stored
			 					if (DoctorEntry.IsDoctorIDPresent(getActivity().getApplicationContext(), dm.getId()) == false)
			 					{
			 						if (DoctorEntry.addDoctor(getActivity().getApplicationContext(), dm) == true)
			 						{
			 							// Ok
			 						}
			 						else
			 						{
			 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.textSignUpDoctorAddDoctorError, Toast.LENGTH_LONG).show();
			 	 						break;
			 						}
			 						
			 						// Now i get the picture...
				 					final Doctor dmTemp = dm;
		      	    				// now i add the Picture of the Patient
			 						// Ok i can add the patient
		 	 			      		CallableTask.invoke(new Callable<Response>() 
		 		      				{
		 		      	    			@Override
		 		      	    			public Response call() throws Exception 
		 		      	    			{
		 		      	    				return svc.getDoctorPictureData(dmTemp.getId());
		 		      	    			}
		 		      	    		}, new TaskCallback<Response>() 
		 		      	    		{
		 		      	    			@Override
		 		      	    			public void success(Response result) 
		 		      	    			{
			 		   		        		if(result != null)
			 				            	{
			 		      	    				if(result.getStatus() == 200)
			 		      	    				{
				 		   		        			long lDoctorID = 0;
				 		   		        			byte[] image = null;
				 		   		        			ArrayList<Header> alh = new ArrayList<Header>(result.getHeaders());
				 		   		        			for(Header h : alh)
				 		   		        			{
				 		   		        				if (h.getName().trim().equals("id") == true)
				 		   		        				{
				 		   		        					try 
				 		   		        					{
				 		   		        						lDoctorID = Long.parseLong(h.getValue());
				 		   		        					} catch (Exception ex) 
				 		   		        					{
				 		   		        						lDoctorID = 0;
				 		   		        					}			 		   		        					
				 		   		        				}
				 		   		        			}
				 		   		        			if (lDoctorID > 0)
				 		   		        			{
						 		   		        		InputStream pictureData = null;
														try {
															pictureData = result.getBody().in();
														} catch (IOException e1) {
															// TODO Auto-generated catch block
															e1.printStackTrace();
														}
							 		   		    		try {
															image = IOUtils.toByteArray(pictureData);
														} catch (IOException e2) {
															// TODO Auto-generated catch block
															e2.printStackTrace();
														}
					 		   		        			if (image != null)
					 		   		        			{
									 						if (DoctorEntry.setDoctorImage(getActivity().getApplicationContext(), lDoctorID, image) == true)
									 						{
									 							// Ok
									 						}
									 						else
									 						{
									 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.textSignUpDoctorAddDoctorImageError, Toast.LENGTH_LONG).show();
									 						}					 		   		        				
					 		   		        			}
								 					}
				 				            	}
		 		      	    				}
			 		   		        		
			 			    	        	// Update even if the image doesn't exist
			 						        // Update Value
			 					           	if(m_adapter != null)
			 					        	{
			 					        		m_aldm = DoctorEntry.getDoctorList(getActivity().getApplicationContext());
			 					        		if(m_aldm != null)
			 					            	{
			 					            		m_adapter.updateData(m_aldm);      	
			 					            	}
			 					        	}

			 			    	         	setAppTitle();			 		   		        		
		 		      	    			}
		 		      	   		
		 		      	    			@Override
		 		      	    			public void error(Exception e) 
		 		      	    			{
		 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
		 		      	    				DoctorSvc.close();	
		 		      	    			}
	
		 		      	    		}); 			 						
			 					}
		 					}	

					        // Update Value
				           	if(m_adapter != null)
				        	{
				        		m_aldm = DoctorEntry.getDoctorList(getActivity().getApplicationContext());
				        		if(m_aldm != null)
				            	{
				            		m_adapter.updateData(m_aldm);      	
				            	}
				        	}
			           	
				           	setAppTitle();	           	
				           	
		            	}
					}

					@Override
					public void error(Exception e) 
					{
						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewGetDoctorListError, Toast.LENGTH_LONG).show();
						DoctorSvc.close();	
					}
				});
			
	        }
	        else
	        {
	        	// Update Value
	           	if(m_adapter != null)
	        	{
	        		m_aldm = DoctorEntry.getDoctorList(getActivity().getApplicationContext());
	        		if(m_aldm != null)
	            	{
	            		m_adapter.updateData(m_aldm);      	
	            	}
	        	}
	           	
	           	setAppTitle();	           	
	        }         	
        }
        
        private void setAppTitle()
        {
     		// In the App Title, i set also the Doctor User Name
     		String strDoctorName = "";
      		
      		if(m_dm != null && m_dm.getId() != 0)
      		{
      			strDoctorName = DoctorEntry.getDoctorName(getActivity().getApplicationContext(), m_dm.getId());
      		}
      		else
      		{
      			strDoctorName = getString(R.string.labelTextViewNoChoosenDoctor);
      		}

      		// In the App Title, i set also the Patient User Name that i am working to
    		String strAppTitle = getString(R.string.app_name);
    		getActivity().setTitle(strAppTitle + "-Your Doctor is: " + strDoctorName);          	
        }

        final private OnQueryTextListener queryListener = new OnQueryTextListener() {       

            @Override
            public boolean onQueryTextChange(String newText) 
            {
                if (TextUtils.isEmpty(newText)) 
                {
    	        	// Update Value
    	           	if(m_adapter != null)
    	        	{
    	        		m_aldm = DoctorEntry.getDoctorList(getActivity().getApplicationContext());
    	        		if(m_aldm != null)
    	            	{
    	            		m_adapter.updateData(m_aldm);      	
    	            	}
    	        	}
                } 
                else 
                {
    	        	// Update Value
    	           	if(m_adapter != null)
    	        	{
    	        		m_aldm = DoctorEntry.getDoctorListByFirstNameAndLastName(getActivity().getApplicationContext(), newText);
    	        		if(m_aldm != null)
    	            	{
    	            		m_adapter.updateData(m_aldm);      	
    	            	}
    	        	}
                }   

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {            
                Toast.makeText(getActivity(), "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
                return false;
            }
        };
        
        @Override 
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            SearchView sv = new SearchView(getActivity());
            sv.setOnQueryTextListener(queryListener);
            item.setActionView(sv);
        }
       
    }

    /**
     * A PatientCancerTherapy fragment.
     */
    public static class PatientSettingFragment extends Fragment 
    {
     	private EditText m_etEditTextHostAddressSetting;
    	private EditText m_etEditTextPatientSettingReminderFrequency; 
    	private TextView m_tvTextViewPatientSettingReminderIsRunning; 
    	
    	private Button m_bButtonPatientSettingSave;
    	private Button m_bButtonPatientSettingStartReminder;
    	private Button m_bButtonPatientSettingStopReminder;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    	
        private static final String ARG_SECTION_NUMBER = "section_number";
        
    
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PatientSettingFragment newInstance(int sectionNumber) 
        {
        	PatientSettingFragment fragment = new PatientSettingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PatientSettingFragment() 
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            View rootView = inflater.inflate(R.layout.patient_setting_fragment, container, false);

            return rootView;
        }
        
    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) 
    	{
    		super.onActivityCreated(savedInstanceState);

    		m_etEditTextHostAddressSetting = (EditText) getActivity().findViewById(R.id.idEditTextHostAddressSetting);
            m_etEditTextPatientSettingReminderFrequency = (EditText) getActivity().findViewById(R.id.idEditTextPatientSettingReminderFrequency);
            m_tvTextViewPatientSettingReminderIsRunning = (TextView) getActivity().findViewById(R.id.idTextViewPatientSettingReminderIsRunning);            
            m_bButtonPatientSettingSave = (Button) getActivity().findViewById(R.id.idButtonPatientSettingSave);
            m_bButtonPatientSettingStartReminder = (Button) getActivity().findViewById(R.id.idButtonPatientSettingStartReminder);
            m_bButtonPatientSettingStopReminder = (Button) getActivity().findViewById(R.id.idButtonPatientSettingStopReminder);

            // Set an OnClickListener
            m_bButtonPatientSettingSave.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    				boolean bSaveStatus = true;
    	        	String strHostAddress = m_etEditTextHostAddressSetting.getText().toString();
       				// set a Parameter
    				if(Settings.setParameter(getActivity().getApplicationContext(), Parameter.HOST_ADDRESS, String.valueOf(strHostAddress)) == false)
    				{
    					bSaveStatus = false;    					
    				}

    				String strReminderFrequency = m_etEditTextPatientSettingReminderFrequency.getText().toString();
    				long lReminderFrequencyHours = 0;
    				try 
    				{
    					lReminderFrequencyHours = Long.parseLong(strReminderFrequency);
    				} catch (Exception e) 
    				{
    					
    				}
    				// set a Parameter
    				if(Settings.setParameter(getActivity().getApplicationContext(), Parameter.SCHEDULED_REMINDER_FREQUENCY, String.valueOf(lReminderFrequencyHours)) == false)
    				{
    					bSaveStatus = false;    					
    				}

    				if(bSaveStatus == true)
    				{
    					MainActivity.m_strHostAddress = strHostAddress;
      					Toast.makeText(getActivity().getApplicationContext(), R.string.labelButtonPatientSettingSaveOk, Toast.LENGTH_LONG).show();
    				}
    				else
    				{
      					Toast.makeText(getActivity().getApplicationContext(), R.string.labelButtonPatientSettingSaveError, Toast.LENGTH_LONG).show();
    				}

    				UpdateFragment();

    			}
       		});    
            
            // Set an OnClickListener
            m_bButtonPatientSettingStartReminder.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    	      		String strReminderFrequency = Settings.getParameter(getActivity().getApplicationContext(), Parameter.SCHEDULED_REMINDER_FREQUENCY);
    	      		long lReminderFrequencyHours = 0;
    	      		
    	      		try
    	      		{
    	      			lReminderFrequencyHours = Long.parseLong(strReminderFrequency);
    	      		} catch (Exception ex)
    	      		{

    	      		}
         			
    	      		StartReminder(getActivity().getBaseContext(), lReminderFrequencyHours);
    	      		StartReminderUpdate(getActivity().getBaseContext());
          			
          			UpdateFragment();
    			}
       		});    
            
            // Set an OnClickListener
            m_bButtonPatientSettingStopReminder.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{

   	      			StopReminder(getActivity().getBaseContext());
   	      			StopReminderUpdate(getActivity().getBaseContext());
   	      		 
   	      			UpdateFragment();
    			}
       		});    
            
    	}
	
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) 
		{

		}    	
		
    	@Override
    	public void onResume() 
    	{
      		super.onResume();
      		
      		UpdateFragment();
    	}
        
       	@Override
    	public void onDestroyView()
    	{
    	    super.onDestroyView();
     	}
       	
        private void UpdateFragment()
        {
           	String strHostAddress = Settings.getParameter(getActivity().getApplicationContext(), Parameter.HOST_ADDRESS);
        	if(strHostAddress.length() == 0)
        	{
        		strHostAddress = "https://10.0.2.2:8443";
        	}
        	m_etEditTextHostAddressSetting.setText(strHostAddress);  		

        	String strReminderFrequency = Settings.getParameter(getActivity().getApplicationContext(), Parameter.SCHEDULED_REMINDER_FREQUENCY);
      		m_etEditTextPatientSettingReminderFrequency.setText(strReminderFrequency);  		
      		if(m_bReminderStatus == false)
      		{
      			m_tvTextViewPatientSettingReminderIsRunning.setText(getString(R.string.labelTextViewPatientSettingReminderStatus) + getString(R.string.labelTextViewPatientSettingReminderStop));  	
      		}
      		else
      		{
      			m_tvTextViewPatientSettingReminderIsRunning.setText(getString(R.string.labelTextViewPatientSettingReminderStatus) + getString(R.string.labelTextViewPatientSettingReminderRunning));  	
      		}
        }			
    }
    
	public static Intent makePatientSettingActivity(Context context, long lID) 
	{
		Intent intent = new Intent();
		intent.setClass(context, PatientSettingActivity.class);
		intent.putExtra(PatientSettingActivity.ID, lID);	
		return intent;
	}

}
