package com.capstone.sm.doctor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

import retrofit.client.Header;
import retrofit.client.Response;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.CancerTherapyEntry;
import com.capstone.SQL.SQLContract.DoctorEntry;
import com.capstone.SQL.SQLContract.PainTherapyEntry;
import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.PatientEntry;
import com.capstone.SQL.SQLContract.PatientStatusEntry;
import com.capstone.SQL.SQLContract.Question;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.MainActivity;
import com.capstone.sm.R;
import com.capstone.sm.SignInActivity;
import com.capstone.sm.cancertherapy.CancerTherapy;
import com.capstone.sm.cancertherapy.CancerTherapyListActivity;
import com.capstone.sm.cancertherapy.CancerTherapySvc;
import com.capstone.sm.cancertherapy.CancerTherapySvcApi;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.datetime.DateTimeUtils;
import com.capstone.sm.paintherapy.PainTherapy;
import com.capstone.sm.paintherapy.PainTherapyListActivity;
import com.capstone.sm.paintherapy.PainTherapySvc;
import com.capstone.sm.paintherapy.PainTherapySvcApi;
import com.capstone.sm.patient.Patient;
import com.capstone.sm.patient.PatientListAdapter;
import com.capstone.sm.patient.PatientSvc;
import com.capstone.sm.patient.PatientSvcApi;
import com.capstone.sm.patientstatus.PatientStatus;
import com.capstone.sm.patientstatus.PatientStatusListAdapter;
import com.capstone.sm.patientstatus.PatientStatusSvc;
import com.capstone.sm.patientstatus.PatientStatusSvcApi;

public class DoctorViewActivity extends DoctorBaseBarActivity 
{
	private final static int ACTIVITY_SET_PATIENT_LOGIN = 1;
	private final static int ACTIVITY_GET_LIST_PATIENT = 2;

	private final static String STATE_PATIENT_ID = "PatientID";
	
	private final static String ID = "id";
	private final static String LIST_PATIENT_ID = "list_patient_id";	

	
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
        
        Intent intent = getIntent();
        
        if(intent != null)
        {
        	long lID = intent.getLongExtra(DoctorViewActivity.ID, 0);
        	m_dm = DoctorEntry.getDoctor(this, lID);
        	if (m_dm.getId() > 0)
        	{
        		m_alPatienIDToCheck = intent.getLongArrayExtra(LIST_PATIENT_ID);
        	}
        }
        else
        {
        	m_alPatienIDToCheck = null;
        }

       if (savedInstanceState != null) 
        {
        	// Check that the Doctor is the Doctor of this Patient
        	if (m_dm.getId() > 0)
        	{
    		    // Restore state members from saved instance
        		long lPatientID = savedInstanceState.getLong(STATE_PATIENT_ID);
        		long lDoctorID = PatientEntry.getPatientDoctorID(getApplicationContext(), lPatientID);
        		if(lDoctorID > 0)
        		{
            		if(lDoctorID == m_dm.getId())
            		{
                    	m_pm = PatientEntry.getPatient(getApplicationContext(), lPatientID);
            		}
        		}

        		m_alPatienIDToCheck = intent.getLongArrayExtra(DoctorViewActivity.LIST_PATIENT_ID);
        	}
        }	

        
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
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	    // Save the user's current state
	    savedInstanceState.putLong(STATE_PATIENT_ID, m_pm.getId());
	    savedInstanceState.putLongArray(LIST_PATIENT_ID, m_alPatienIDToCheck);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
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
        	DoctorPatientFragment dpf = (DoctorPatientFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(dpf != null)
            {
               	if(dpf.isResumed() == true)
            	{
               		dpf.UpdateFragment();
                }
            }
        }
        if(tab.getPosition() == 1)
        {
        	DoctorPatientStatusTherapyFragment dpstf = (DoctorPatientStatusTherapyFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(dpstf != null)
            {
               	if(dpstf.isResumed() == true)
            	{
               		dpstf.UpdateFragment();
            	}
            }
        }
        if(tab.getPosition() == 2)
        {
        	DoctorChartPatientStatusTherapyFragment dcpstf = (DoctorChartPatientStatusTherapyFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(dcpstf != null)
            {
               	if(dcpstf.isResumed() == true)
            	{
               		dcpstf.UpdateFragment();
                }
            }
        }
            
        if(tab.getPosition() == 3)
        {
        	DoctorCancerTherapyFragment dctf = (DoctorCancerTherapyFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(dctf != null)
            {
               	if(dctf.isResumed() == true)
            	{
               		dctf.UpdateFragment();
                }
            }
        }
        if(tab.getPosition() == 4)
        {
        	DoctorPainTherapyFragment dptf = (DoctorPainTherapyFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(dptf != null)
            {
               	if(dptf.isResumed() == true)
            	{
               		dptf.UpdateFragment();
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
            	fragment = DoctorPatientFragment.newInstance(position + 1);
            }
            if (position == 1) 
            {
        		fragment = DoctorPatientStatusTherapyFragment.newInstance(position + 1);
            }
            if (position == 2) 
            {
        		fragment = DoctorChartPatientStatusTherapyFragment.newInstance(position + 1);
            }
            if (position == 3) 
            {
                fragment = DoctorCancerTherapyFragment.newInstance(position + 1);
            }
            if (position == 4) 
            {
        		fragment = DoctorPainTherapyFragment.newInstance(position + 1);
            }
            return fragment;        	
        }

        @Override
        public int getCount() 
        {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) 
        {
      		Locale l = Locale.getDefault();
            switch (position) {
                case 0:
               		return getString(R.string.title_section_doctor_choose_patient).toUpperCase(l);

                case 1:
                    return getString(R.string.title_section_doctor_patient_status).toUpperCase(l);
                        	
                case 2:
                    return getString(R.string.title_section_doctor_chart_patient_status).toUpperCase(l);

                case 3:
                    return getString(R.string.title_section_doctor_patient_set_cancer_therapy).toUpperCase(l);
                    
                case 4:
                    return getString(R.string.title_section_doctor_patient_set_pain_therapy).toUpperCase(l);

            }
            return null;
        }
        
        private String getFragmentTag(int viewPagerId, int fragmentPosition)
        {
             return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
        }           
    }
        
    /**
     * A DoctorPatientFragment fragment.
     */
    public static class DoctorPatientFragment extends ListFragment 
    {

		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private PatientListAdapter m_adapter;
    	private ArrayList<Patient> m_alpm;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DoctorPatientFragment newInstance(int sectionNumber) 
        {
        	DoctorPatientFragment fragment = new DoctorPatientFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DoctorPatientFragment() 
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
	
    		if(m_bUpdateStatus == false)
    		{
        		// Start Reminder
          		String strUpdateFrequency = Settings.getParameter(getActivity().getApplicationContext(), Parameter.SCHEDULED_UPDATE_FREQUENCY);
          		long lUpdateFrequencyHours = 0;
          		
          		try
          		{
          			lUpdateFrequencyHours = Long.parseLong(strUpdateFrequency);
          		} catch (Exception ex)
          		{

          		}
     			
          		StartUpdate(getActivity().getBaseContext(), lUpdateFrequencyHours);
    		}
 
    		m_adapter = new PatientListAdapter(getActivity());
    		setListAdapter(m_adapter);	    		
    	}
    	
    	@Override
		public void onListItemClick(ListView l, View v, int position, long id) 
    	{
    		super.onListItemClick(l, v, position, id);

    		m_pm = m_alpm.get(position);
    		
    		// When i confirm the Patient to check, i also confirm that i have cheched his status health,
    		// in this way the alarm will be no longer generated for this Patient.
         	if(m_alPatienIDToCheck != null)
      		{
   				// Check if i am in local mode
   		        if(MainActivity.LOCAL_MODE == false)
   		        {
	         		
					// I get the connection
					final PatientStatusSvcApi svc = PatientStatusSvc.get(getActivity().getApplicationContext());
					
					CallableTask.invoke(new Callable<Void>() {
	
						@Override
						public Void call() throws Exception {
							return svc.setPatientStatusAsChecked(m_pm.getId(), m_dm.getId());
						}
					}, new TaskCallback<Void>() 
					{
						@Override
						public void success(Void result) 
						{
							PatientStatusEntry.setPatientStatusAsChecked(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId());

							Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewSetPatientCheckedOk, Toast.LENGTH_LONG).show();
						}
	
						@Override
						public void error(Exception e) 
						{
							Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewSetPatientCheckedError, Toast.LENGTH_LONG).show();
							PatientStatusSvc.close();	
						}
					});       	
   		        }
   	         	else
   	     		// Update App Title
   	          	{
   	 				PatientStatusEntry.setPatientStatusAsChecked(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId());
   	          	}   		        
      		}
         	
     		UpdateFragment();
           
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

    	    // free adapter
    	    setListAdapter(null);
    	}

        private void UpdateFragment()
        {
			// Check if i am in local mode
	        if(MainActivity.LOCAL_MODE == false)
	        {
				// I get the connection
				final PatientSvcApi svc = PatientSvc.get(getActivity().getApplicationContext());
				
				CallableTask.invoke(new Callable<Collection<Patient>>() {

					@Override
					public Collection<Patient> call() throws Exception {
						return svc.getPatientByDoctorid(m_dm.getId());
					}
				}, new TaskCallback<Collection<Patient>>() 
				{
					@Override
					public void success(Collection<Patient> result) 
					{
		        		if(result != null)
		            	{
		        			// i Update the Local Table of the Patients that have choose the actual doctor logged in
					        for (Patient pm : result)
					        {
					        	// I will delete UserName and Password
					        	pm.setUsername("");
					        	pm.setPassword("");
								// Store all Patient in the Local DB if not already stored
			 					if (PatientEntry.IsPatientIDPresent(getActivity().getApplicationContext(), pm.getId()) == false)
			 					{
			 						if (PatientEntry.addPatient(getActivity().getApplicationContext(), pm) == true)
			 						{
			 							// Ok
			 						}
			 						else
			 						{
			 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.textSignUpDoctorAddDoctorError, Toast.LENGTH_LONG).show();
			 	 						break;
			 						}

			 						// Now i get the picture...
				 					final Patient pmTemp = pm;
		      	    				// now i add the Picture of the Patient
			 						// Ok i can add the patient
		 	 			      		CallableTask.invoke(new Callable<Response>() 
		 		      				{
		 		      	    			@Override
		 		      	    			public Response call() throws Exception 
		 		      	    			{
		 		      	    				return svc.getPatienPictureData(pmTemp.getId());
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
				 		   		        			long lPatientID = 0;
				 		   		        			byte[] image = null;
				 		   		        			ArrayList<Header> alh = new ArrayList<Header>(result.getHeaders());
				 		   		        			for(Header h : alh)
				 		   		        			{
				 		   		        				if (h.getName().trim().equals("id") == true)
				 		   		        				{
				 		   		        					try 
				 		   		        					{
					 		   		        					lPatientID = Long.parseLong(h.getValue());
				 		   		        					} catch (Exception ex) 
				 		   		        					{
				 		   		        						lPatientID = 0;
				 		   		        					}			 		   		        					
				 		   		        				}
				 		   		        			}
				 		   		        			if (lPatientID > 0)
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
									 						if (PatientEntry.setPatientImage(getActivity().getApplicationContext(), lPatientID, image) == true)
									 						{
									 							// Ok
									 						}
									 						else
									 						{
									 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.textSignUpPatientAddPatientImageError, Toast.LENGTH_LONG).show();
									 						}					 		   		        				
					 		   		        			}
								 					}
				 				            	}
		 		      	    				}
			 		   		        		
			 			    	        	// Update even if the image doesn't exist
			 			    	         	if(m_adapter != null)
			 			    	        	{
			 				    	         	if(m_alPatienIDToCheck == null)
			 					         		{
			 				    	         		// All Patient
			 						        		m_alpm = PatientEntry.getPatientByDoctorID(getActivity().getApplicationContext(), m_dm.getId());
			 					         		}
			 					         		else
			 					         		{
			 					         			// I get only the list of Patient with Health Problem
			 						        		if(m_alpm == null)
			 						        		{
			 						        			m_alpm = new ArrayList<Patient>();
			 						         			for(int iIndex = 0; iIndex < m_alPatienIDToCheck.length; iIndex++)
			 						         			{
			 							         			m_alpm.add(PatientEntry.getPatient(getActivity().getApplicationContext(), m_alPatienIDToCheck[iIndex]));
			 						         			}
			 						        		}
			 					         		}
			 					        		if(m_alpm != null)
			 					            	{
			 					            		m_adapter.updateData(m_alpm);      	
			 					            	}
			 			    	        	}

			 			    	         	setAppTitle();			 		   		        		
		 		      	    			}
		 		      	   		
		 		      	    			@Override
		 		      	    			public void error(Exception e) 
		 		      	    			{
		 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.textSignUpPatientAddPatientError, Toast.LENGTH_LONG).show();
		 		      	    				PatientSvc.close();	
		 		      	    			}
	
		 		      	    		}); 
			 					}
		 					}	
				        
		    	        	// Update even if the image doesn't exist
		    	         	if(m_adapter != null)
		    	        	{
			    	         	if(m_alPatienIDToCheck == null)
				         		{
			    	         		// All Patient
					        		m_alpm = PatientEntry.getPatientByDoctorID(getActivity().getApplicationContext(), m_dm.getId());
				         		}
				         		else
				         		{
					         		// I get only the list of Patient with Health Problem
					        		if(m_alpm == null)
					        		{
					        			m_alpm = new ArrayList<Patient>();
					         			for(int iIndex = 0; iIndex < m_alPatienIDToCheck.length; iIndex++)
					         			{
						         			m_alpm.add(PatientEntry.getPatient(getActivity().getApplicationContext(), m_alPatienIDToCheck[iIndex]));
					         			}
					        		}
				         		}
				        		if(m_alpm != null)
				            	{
				            		m_adapter.updateData(m_alpm);      	
				            	}
		    	        	}

		    	         	setAppTitle();	

		            	}
					}

					@Override
					public void error(Exception e) 
					{
						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewGetPatientListError, Toast.LENGTH_LONG).show();
						PatientSvc.close();	
					}
				});

	        }
	        else
	        {
	        	// Update
	         	if(m_adapter != null)
	        	{
	         		if(m_alPatienIDToCheck == null)
	         		{
		        		m_alpm = PatientEntry.getPatientByDoctorID(getActivity().getApplicationContext(), m_dm.getId());
	         		}
	         		else
	         		{
	         			for(int iIndex = 0; iIndex < m_alPatienIDToCheck.length; iIndex++)
	         			{
		         			m_alpm.add(PatientEntry.getPatient(getActivity().getApplicationContext(), iIndex));
	         			}
	         		}
	        		if(m_alpm != null)
	            	{
	            		m_adapter.updateData(m_alpm);      	
	            	}
	        	}
	         	setAppTitle();
	        }
        }  
        
        private void setAppTitle()
        {
    		// In the App Title, i set also the Doctor User Name
     		String strPatienName = "";
      		
      		if(m_pm != null && m_pm.getId() != 0)
      		{
      			strPatienName = PatientEntry.getPatientName(getActivity().getApplicationContext(), m_pm.getId());
      		}
      		else
      		{
      			strPatienName = getString(R.string.labelTextViewNoChoosenPatient);
      		}

      		// In the App Title, i set also the Patient User Name that i am working to
    		String strAppTitle = getString(R.string.app_name);
    		getActivity().setTitle(strAppTitle + "-Your Patient: " + strPatienName);         	
        	
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
    	           		m_alpm = PatientEntry.getPatientByDoctorID(getActivity().getApplicationContext(), m_dm.getId());
    	        		if(m_alpm != null)
    	            	{
    	            		m_adapter.updateData(m_alpm);      	
    	            	}
    	        	}
                } 
                else 
                {
    	        	// Update Value
    	           	if(m_adapter != null)
    	        	{
    	           		m_alpm = PatientEntry.getPatientListByDoctorIDAndFirstNameAndLastName(getActivity().getApplicationContext(), m_dm.getId(), newText);
    	        		if(m_alpm != null)
    	            	{
    	            		m_adapter.updateData(m_alpm);      	
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
     * A Doctor Patient Status Therapy Fragment fragment.
     */
    public static class DoctorPatientStatusTherapyFragment extends ListFragment 
    {
    	
        private PatientStatusListAdapter m_adapter;
    	private ArrayList<PatientStatus> m_alpsm;
    	
		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DoctorPatientStatusTherapyFragment newInstance(int sectionNumber) 
        {
        	DoctorPatientStatusTherapyFragment fragment = new DoctorPatientStatusTherapyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DoctorPatientStatusTherapyFragment() 
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
    		
       		m_adapter = new PatientStatusListAdapter(getActivity());
    		setListAdapter(m_adapter);	    		
     	} 
    	
    	@Override
		public void onListItemClick(ListView l, View v, int position, long id) 
    	{
    		super.onListItemClick(l, v, position, id);
    		m_psm = m_adapter.getItem(position);
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
				final PatientStatusSvcApi svc = PatientStatusSvc.get(getActivity().getApplicationContext());
				
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
			 					if (PatientStatusEntry.IsPatientStatusIDPresent(getActivity().getApplicationContext(), psm.getId()) == false)
			 					{
			 						if (PatientStatusEntry.addPatientStatus(getActivity().getApplicationContext(), psm) == true)
			 						{
			 							// Ok
			 						}
			 						else
			 						{
			 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewPatientStatusAddPatientStatusError, Toast.LENGTH_LONG).show();
			 							DoctorSvc.close();	
			 	 						break;
			 						}
			 					}
		 					}	

					        // Ok i will retrieve also the updated information about the Cancer Therapy with the
					        // date of the execution...
					        
							// I get the connection
							final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
							
							CallableTask.invoke(new Callable<Collection<CancerTherapy>>() {

								@Override
								public Collection<CancerTherapy> call() throws Exception {
									return svc.getCancerTherapyByPatientidAndDoctorid(m_pm.getId(), m_dm.getId());
								}
							}, new TaskCallback<Collection<CancerTherapy>>() 
							{
								@Override
								public void success(Collection<CancerTherapy> result) 
								{
					        		if(result != null)
					            	{
								        for (CancerTherapy ctm : result)
								        {
											// Store all in the Local DB if not already stored or i update it....
						 					if (CancerTherapyEntry.IsCancerTherapyIDPresent(getActivity().getApplicationContext(), ctm.getId()) == true)
						 					{
							 					if (CancerTherapyEntry.IsCancerTherapyIDExecuted(getActivity().getApplicationContext(), ctm.getId()) == false)
							 					{
							 						// This can happen if i uninstall the app,
							 						// in this case i will resync my data....
							 						if (CancerTherapyEntry.updateCancerTherapy(getActivity().getApplicationContext(), ctm) == true)
							 						{
							 							// Ok
							 						}
							 						else
							 						{
							 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetCancerTherapyUpdateError, Toast.LENGTH_LONG).show();
							 	 					}
							 					}
						 					}
						 					else
						 					{
						 						// This can happen if i uninstall the app,
						 						// in this case i will resync my data....
						 						if (CancerTherapyEntry.addCancerTherapy(getActivity().getApplicationContext(), ctm) == true)
						 						{
						 							// Ok
						 						}
						 						else
						 						{
						 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetCancerTherapyAddError, Toast.LENGTH_LONG).show();
						 						}
						 					}
					 					}
								        
								        // Update Value
							           	if(m_adapter != null)
							        	{
							           		m_alpsm = PatientStatusEntry.getListPatientStatusGroupByDateTimeAnswer(getActivity().getApplicationContext(), m_pm.getId(), m_pm.getDoctorid());
							        		if(m_alpsm != null)
							            	{
							            		m_adapter.updateData(m_alpsm);      	
							            	}
							        	}									        
					            	}					            	
								}

								@Override
								public void error(Exception e) 
								{
									Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewGetDoctorListError, Toast.LENGTH_LONG).show();
									CancerTherapySvc.close();	
								}
							});
							
		            	}
					}

					@Override
					public void error(Exception e) 
					{
						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewPatientStatusGetPatientStatusListError, Toast.LENGTH_LONG).show();
						PatientStatusSvc.close();	
					}
				});
			
	        }
	        else
	        {
	        	// Update
	         	if(m_adapter != null)
	        	{
	           		m_alpsm = PatientStatusEntry.getListPatientStatusGroupByDateTimeAnswer(getActivity().getApplicationContext(), m_pm.getId(), m_pm.getDoctorid());
	        		if(m_alpsm != null)
	            	{
	            		m_adapter.updateData(m_alpsm);      	
	            	}
	        	}
	        }	        
        }      	
    }
    
    /**
     * A Doctor Chart Fragment fragment.
     */
    public static class DoctorChartPatientStatusTherapyFragment extends Fragment 
    {
    	private LinearLayout m_la;
    	private DrawBarChart m_barView;
    	private TextView m_tvTextViewidBarChartTitle;
    	

		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DoctorChartPatientStatusTherapyFragment newInstance(int sectionNumber) 
        {
        	DoctorChartPatientStatusTherapyFragment fragment = new DoctorChartPatientStatusTherapyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DoctorChartPatientStatusTherapyFragment() 
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            View rootView = inflater.inflate(R.layout.doctor_chart_patient_status, container, false);
            return rootView;
        }

    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) 
    	{
    		super.onActivityCreated(savedInstanceState);

    		m_la = (LinearLayout) getActivity().findViewById(R.id.idBarChart);
    		m_tvTextViewidBarChartTitle = (TextView) getActivity().findViewById(R.id.idTextViewidBarChartTitle);
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

            if(m_la != null)
            {
                if(m_barView != null)
                {
                	m_la.removeView(m_barView);
                	m_barView = null;
            	}
            }
    	}
       	
        private void UpdateFragment()
        {
        	
    		// Set the title as the Last Cancer Therapy
    		m_tvTextViewidBarChartTitle.setText("---");
        	// I remove the old one
            if(m_barView != null)
            {
            	m_la.removeView(m_barView);
            	m_barView = null;
        	}
    		
    		if(m_psm != null)
    		{
    			if(m_psm.getLastcancertherapyexecid() > 0)
    			{
	        		CancerTherapy ctm = CancerTherapyEntry.getCancerTherapyByID(getActivity().getApplicationContext(), m_psm.getLastcancertherapyexecid());
	        		if(ctm != null)
	        		{
	            		// Answer string
	            		String strValueAnswer = ctm.getName() + ", " + DateTimeUtils.getInstance().FormatDateTimeFromDBToString(getActivity().getApplicationContext(), ctm.getDatetimeexec());
	            		m_tvTextViewidBarChartTitle.setText(strValueAnswer);
	        		}
	                
	                // Partial Patient Status Information
	                // Here we have the answer Grouped by Last Cancer Therapy.
	                // Now, for each rows, i use the Date Time of the answer for get out the results of all check In linked with this Cancer Therapy 
	                ArrayList<PatientStatus> alpsmGroupByCancerTherapyID = PatientStatusEntry.getListPatientStatusByCancerTherapyIDGroupByDateTimeAnswer(getActivity().getApplicationContext(), m_psm.getPatientid(), m_psm.getDoctorid(), m_psm.getLastcancertherapyexecid());
	                
	                // Full patient Status Information
					if(alpsmGroupByCancerTherapyID != null && alpsmGroupByCancerTherapyID.size() > 0)
					{
		                float[] A1 = new float[alpsmGroupByCancerTherapyID.size()];
		                float[] A2 = new float[alpsmGroupByCancerTherapyID.size()];
		                float[] A3 = new float[alpsmGroupByCancerTherapyID.size()];

	                	// Now for each check in i will get the value for the chart
		                for(int iIndex_1 = 0; iIndex_1 < alpsmGroupByCancerTherapyID.size(); iIndex_1++)
		                {
		                	
		                	// Now for each answer, of each check in i will get the value for the chart
		                	ArrayList<PatientStatus> alpsm =  PatientStatusEntry.getListPatientStatusByDateTimeAnswer(getActivity().getApplicationContext(), alpsmGroupByCancerTherapyID.get(iIndex_1).getPatientid(), alpsmGroupByCancerTherapyID.get(iIndex_1).getDoctorid(), alpsmGroupByCancerTherapyID.get(iIndex_1).getDatetimeanswer());
			                for(int iIndex_2 = 0; iIndex_2 < alpsm.size(); iIndex_2++)
			                {
			                	// Answer about Question 1
				            	if(alpsm.get(iIndex_2).getQuestionid() == Question.QUESTION_1.getID())
				            	{
				            		A1[iIndex_1] = alpsm.get(iIndex_2).getAnswerid();
				            	}
			                	// Answer about Question 2
				            	if(alpsm.get(iIndex_2).getQuestionid() == Question.QUESTION_2.getID())
				            	{
				            		A2[iIndex_1] = A2[iIndex_1] + alpsm.get(iIndex_2).getAnswerid();
				            	}
			                	// Answer about Question 3
				            	if(alpsm.get(iIndex_2).getQuestionid() == Question.QUESTION_3.getID())
				            	{
				            		A3[iIndex_1] = alpsm.get(iIndex_2).getAnswerid() + 1;
				            	}
				            	
			            	}

		                }
		                
		                if(m_la != null)
		                {
		                	m_barView = new DrawBarChart(getActivity(), A1, A2, A3, "Health Status", "Pain Th.", "Food/Drink");
		                    if(m_barView != null)
		                    {
		                    	m_la.addView(m_barView);
		                    }
		                }
    				}
				}
    		}
        }   

        public class DrawBarChart extends View 
        {
        	private Paint m_paint = new Paint();
        	private Canvas m_Canvas = new Canvas();
        	private float[] m_A1;
	        private float[] m_A2;
	        private float[] m_A3;
	        String m_strA1;
	        String m_strA2;
	        String m_strA3;	        
	        Context context;

			public DrawBarChart(Context context) 
			{
				super(context);
				m_paint = new Paint();
				super.draw(m_Canvas);
			}

			public DrawBarChart(Context context, AttributeSet attrs) 
			{
				super(context, attrs);
				m_paint = new Paint();
				super.draw(m_Canvas);
			}

			public DrawBarChart(Context context, float[] A1, float[] A2, float[] A3, String strA1, String strA2, String strA3) 
			{
				super(context);
				m_paint = new Paint();
				m_A1 = A1;
				m_A2 = A2;
				m_A3 = A3;
		        m_strA1 = strA1;
		        m_strA2 = strA2;
		        m_strA3 = strA3;	        
				super.draw(m_Canvas);
			}

			@Override
			public void draw(Canvas canvas) 
			{
				int x = getWidth();
				int y = getHeight();
				float max = getMax();
				m_paint.setColor(Color.parseColor("#78777D"));
				m_paint.setStyle(Style.STROKE);
				m_paint.setStrokeWidth(2);
				canvas.drawRect(0, 0, x - 1, y - 1, m_paint);
				int n = m_A1.length;
				int gap = x / 10;
				m_paint.setStyle(Style.FILL);
				float fI = (float) 0.0;
				for (int i = 0; i < n; i++) 
				{
				        
					//Blue bars.
				    m_paint.setColor(Color.BLUE);
					canvas.drawText(m_strA1, (float) ((fI + (float)i + 0.0) * gap) + 5, y - 29, m_paint);    
				    float A1 = (max == 0) ? y - 20 : y - (m_A1[i] * (y - 22) / max) - 20;
				    canvas.drawRect((float) ((fI + (float)i + 0.0) * gap) + 5, A1, (float) (fI + (float)i + 0.5) * gap + 3, y - 40, m_paint);
				   
				    //Red bars.
				    m_paint.setColor(Color.RED);
					canvas.drawText(m_strA2, (float) ((fI + (float)i + 0.5) * gap) + 5, y - 17, m_paint);    
				    float A2 = (max == 0) ? y - 20: y - (m_A2[i] * (y - 22) / max) - 20;
				    canvas.drawRect((float) ((fI + (float)i + 0.5) * gap) + 5, A2, (float) (fI + (float)i + 1.0) * gap + 3, y - 40, m_paint);

				    //Green bars.
				    m_paint.setColor(Color.GREEN);
					canvas.drawText(m_strA3, (float) ((fI + (float)i + 1.0) * gap) + 5, y - 5, m_paint);    
				    float A3 = (max == 0) ? y - 20: y - (m_A3[i] * (y - 22) / max) - 20;
				    canvas.drawRect((float) ((fI + (float)i + 1.0) * gap) + 5, A3, (float) (fI + (float)i + 1.5) * gap + 3, y - 40, m_paint);

				    fI = (float) (fI + 0.5);
				}
			 }

	         private float getMax() 
	         {
	        	  float max = 0;
	        	  for (float f : m_A1) 
	        	  {
	        		  if (f > max)
	        		  {
	        			  max = f;
	        		  }
	        	  }
	          
	        	  for (float f : m_A2) 
	        	  {
	        		  if (f > max)
	        		  {
	        			  max = f;
	        		  }
	        	  }
	        	  
	        	  for (float f : m_A3) 
	        	  {
	        		  if (f > max)
	        		  {
	        			  max = f;
	        		  }
	        	  }

	        	  return max;
	         
	         }
        }
    }
    
    /**
     * A DoctorCancerTherapyFragment fragment.
     */
    public static class DoctorCancerTherapyFragment extends Fragment 
    {
    	public final static int ACTIVITY_CANCER_THERAPY_LIST_ACTIVITY = 1;
    	
		private TextView m_etEditTextCancerTherapyName; 
		private TextView m_etEditTextCancerTherapyDate; 
		private TextView m_etEditTextCancerTherapyTime; 
		private Button m_bButtonSetCancerTherapyDate;
		private Button m_bButtonSetCancerTherapyTime;
		private Button m_bButtonSetCancerTherapyList;
		private Button m_bButtonSetCancerTherapyAddUpdate;
		private Button m_bButtonSetCancerTherapyDelete;
		private String m_strDateTimeOfTreatmentExec; 

		// Variable for storing current date and time
		private Calendar m_Calendar;
	    private int m_iYear, m_iMonth, m_iDay, m_iHour, m_iMinute;

		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DoctorCancerTherapyFragment newInstance(int sectionNumber) 
        {
        	DoctorCancerTherapyFragment fragment = new DoctorCancerTherapyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DoctorCancerTherapyFragment() 
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            View rootView = inflater.inflate(R.layout.doctor_cancer_therapy_fragment, container, false);
            return rootView;
        }

    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) 
    	{
    		super.onActivityCreated(savedInstanceState);
    		
    		m_etEditTextCancerTherapyName = (TextView) getActivity().findViewById(R.id.idEditTextCancerTherapyName);
    		m_etEditTextCancerTherapyDate = (TextView) getActivity().findViewById(R.id.idEditTextCancerTherapyDate);
    		m_etEditTextCancerTherapyDate.setKeyListener(null);
    		m_etEditTextCancerTherapyTime = (TextView) getActivity().findViewById(R.id.idEditTextCancerTherapyTime);
    		m_etEditTextCancerTherapyTime.setKeyListener(null); 		
    		m_bButtonSetCancerTherapyDate = (Button) getActivity().findViewById(R.id.idButtonSetCancerTherapyDate);
    		m_bButtonSetCancerTherapyTime = (Button) getActivity().findViewById(R.id.idButtonSetCancerTherapyTime);
    		m_bButtonSetCancerTherapyAddUpdate = (Button) getActivity().findViewById(R.id.idButtonSetCancerTherapyAddUpdate);
    		m_bButtonSetCancerTherapyDelete = (Button) getActivity().findViewById(R.id.idButtonSetCancerTherapyDelete);
    		m_bButtonSetCancerTherapyList = (Button) getActivity().findViewById(R.id.idButtonSetCancerTherapyList);
    		m_strDateTimeOfTreatmentExec = "";
    		
    		m_Calendar = Calendar.getInstance();
    		// Set an OnClickListener for the Change the Date Button
    		m_bButtonSetCancerTherapyDate.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    	            // Process to get Current Date
    	            m_iYear = m_Calendar.get(Calendar.YEAR);
    	            m_iMonth = m_Calendar.get(Calendar.MONTH);
    	            m_iDay = m_Calendar.get(Calendar.DAY_OF_MONTH);
    	 
    	            // Launch Date Picker Dialog
    	            DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() 
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

    	                	m_etEditTextCancerTherapyDate.setText(DateTimeUtils.getInstance().FormatDate(getActivity().getApplicationContext(), m_Calendar));
    	                }
    	                
    	            }, m_iYear, m_iMonth, m_iDay);

    	            dpd.show();
    			}
    		});
    		
    		// Set an OnClickListener for the Change the Time Button
    		m_bButtonSetCancerTherapyTime.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    				// Process to get Current Time
    		        m_Calendar.set(Calendar.HOUR_OF_DAY, m_iHour);
    		        m_Calendar.set(Calendar.MINUTE, m_iMinute);

    		        // Launch Time Picker Dialog
    		        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
    		                    new TimePickerDialog.OnTimeSetListener() 
    		        {
    		        	@Override
    		            public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
    		        	{
    		        		m_iHour = hourOfDay;
    		        		m_iMinute = minute;

    	                	// Display Selected time in textbox
    	                	m_Calendar.set(Calendar.HOUR_OF_DAY, m_iHour);
    	                	m_Calendar.set(Calendar.MINUTE, m_iMinute);
    	                	
    	    	            m_etEditTextCancerTherapyTime.setText(DateTimeUtils.getInstance().FormatTime(getActivity().getApplicationContext(), m_Calendar));
    		        	}
    		            
    		        }, m_iHour, m_iMinute, false);
    	            
    		        tpd.show();
    			}
    		});    
    		
    		// Set an OnClickListener
    		m_bButtonSetCancerTherapyAddUpdate.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    				if(m_dm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoDoctorSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				if(m_pm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoPatientSelected, Toast.LENGTH_LONG).show();
    					return;
    				}

    				// Therapy Name
    				final String strTherapyName = m_etEditTextCancerTherapyName.getText().toString();
    				if(strTherapyName.length() < 3)
    				{
	   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyBadNameFormat, Toast.LENGTH_LONG).show();
    					return;
    				}
    				// Check if i am in local mode
			        if(MainActivity.LOCAL_MODE == false)
			        {
			        	// I get the remote Cancer Therapy
						final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
						
						CallableTask.invoke(new Callable<CancerTherapy>() {

							@Override
							public CancerTherapy call() throws Exception {
								return svc.getCancerTherapyByPatientidAndDoctoridAndName(m_pm.getId(), m_dm.getId(), strTherapyName);
							}
						}, new TaskCallback<CancerTherapy>() 
						{
							@Override
							public void success(CancerTherapy result) 
							{
				        		if(result != null && result.getId() > 0)
				            	{
				        			// I can update only cancer therapy that is not executed
				        			if(result.getDatetimeexec() == "")
				        			{
				        				// Update the data that i needs. The name cannot be updated because is in univoche
				        				result.setDatetimeset(DateTimeUtils.getInstance().FormatDateTimeToDB(getActivity().getApplicationContext(), m_etEditTextCancerTherapyDate.getText().toString(), m_etEditTextCancerTherapyTime.getText().toString()));
				        				final CancerTherapy ctm = result;
				        				// I ask for confirm
					    				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					    	            builder.setTitle(getString(R.string.labelQuestion_No_Yes))
					    	            .setItems(R.array.labelAnswer_No_Yes, new DialogInterface.OnClickListener() 
					    	            {
					    	                @Override
					    					public void onClick(DialogInterface dialog, int which) 
					    	                {
					    	             	   switch (which)
					    	             	   {
					    			           	   		case 0:
					    			        	   			
					    			        	   			break;
					
					    			           	   		case 1:

					    			           	   			// I update the remote cancer therapy
					    									final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
					    									
					    									CallableTask.invoke(new Callable<CancerTherapy>() {

					    										@Override
					    										public CancerTherapy call() throws Exception {
					    											return svc.updateCancerTherapy(ctm);
					    										}
					    									}, new TaskCallback<CancerTherapy>() 
					    									{
					    										@Override
					    										public void success(CancerTherapy result) 
					    										{
					    							        		if(result != null)
					    							            	{
					    												if (CancerTherapyEntry.IsCancerTherapyNamePresent(getActivity().getApplicationContext(), result.getPatientid(), result.getDoctorid(), result.getName()) == true)
					    							 					{
						    							        			// Ok, it should be exist
									    			     					if (CancerTherapyEntry.updateCancerTherapy(getActivity().getApplicationContext(), result) == true)
									    			     					{
									        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateOk, Toast.LENGTH_LONG).show();
									    			     					}
									    			     					else
									    			     					{
									        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateError, Toast.LENGTH_LONG).show();
									    			     					}
						    							            	}
					    												else
					    												{
					    													// Strange, but could be that i have delete tha app and i don't yet get the sincronization
					    													// so i will add it
					    		 	 	 		      	    				if(result != null && result.getId() > 0)
					    		 	 	 		      	    				{
					    				 	 	 		      	    			// Ok, i add locally
					    		 	 	 		      	    					addCancerTherapy(getActivity().getApplicationContext(), result);
					    		 	 	 		      	    				}
					    		 	 	 		      	    				else
					    		 	 	 		      	    				{
					    			 	 	 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetCancerTherapyAddError, Toast.LENGTH_LONG).show();	 	 	 		      	    					
					    		 	 	 		      	    				}					    													
					    												}
					    							            	}
					    										}

					    										@Override
					    										public void error(Exception e) 
					    										{
					    											Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetCancerTherapyUpdateError, Toast.LENGTH_LONG).show();
					    											CancerTherapySvc.close();	
					    										}
					    									});

					    									break;
					    									
					    	             	   }
					    	                }
					    	            });
									
					    	            builder.create();
									
					    	            builder.show();						        				
				        			}
				        			else
				        			{
					   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateErrorAlreadyExec, Toast.LENGTH_LONG).show();				        				
				        			}
				            	}
			        			else
			        			{
			        				// Not found, i will add it....
			 	    				final CancerTherapy ctm = new CancerTherapy(m_pm.getId(), m_dm.getId(), strTherapyName, DateTimeUtils.getInstance().FormatDateTimeToDB(getActivity().getApplicationContext(), m_Calendar), "");
			        				
 	 		 						// Ok i can add the patient
 	 	 	 			      		CallableTask.invoke(new Callable<CancerTherapy>() 
 	 	 		      				{
 	 	 		      	    			@Override
 	 	 		      	    			public CancerTherapy call() throws Exception 
 	 	 		      	    			{
 	 	 		      	    				return svc.addCancerTherapy(ctm);
 	 	 		      	    			}
 	 	 		      	    		}, new TaskCallback<CancerTherapy>() 
 	 	 		      	    		{
 	 	 		      	    			@Override
 	 	 		      	    			public void success(CancerTherapy result) 
 	 	 		      	    			{
											if (CancerTherapyEntry.IsCancerTherapyNamePresent(getActivity().getApplicationContext(), result.getPatientid(), result.getDoctorid(), result.getName()) == true)
						 					{
												// Strange, but could be that i have delete the app and i don't yet get the sincronization
												// so i will update it
		    			     					if (CancerTherapyEntry.updateCancerTherapy(getActivity().getApplicationContext(), result) == true)
		    			     					{
		        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateOk, Toast.LENGTH_LONG).show();
		    			     					}
		    			     					else
		    			     					{
		        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateError, Toast.LENGTH_LONG).show();
		    			     					}
							            	}
											else
											{
							        			// Ok, it should not exist
	 	 	 		      	    				if(result != null && result.getId() > 0)
	 	 	 		      	    				{
			 	 	 		      	    			// Ok, i add locally
	 	 	 		      	    					addCancerTherapy(getActivity().getApplicationContext(), result);
	 	 	 		      	    				}
	 	 	 		      	    				else
	 	 	 		      	    				{
		 	 	 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetCancerTherapyAddError, Toast.LENGTH_LONG).show();	 	 	 		      	    					
	 	 	 		      	    				}					    													
											} 	 	 		      	    				
 	 	 		      	    			}

 	 	 		      	    			@Override
 	 	 		      	    			public void error(Exception e) 
 	 	 		      	    			{
 	 	 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetCancerTherapyAddError, Toast.LENGTH_LONG).show();
 	 	 		      	    				CancerTherapySvc.close();	
 	 	 		      	    			}
 	 	 		      	    		}); 				        				
			        			}
							}

							@Override
							public void error(Exception e) 
							{
								Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextGetCancerTherapyListError, Toast.LENGTH_LONG).show();
								CancerTherapySvc.close();	
							}
						});			        	
			        }
			        else
			        {
						if (CancerTherapyEntry.IsCancerTherapyNamePresent(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName) == true)
	 					{
							CancerTherapy ctm = CancerTherapyEntry.getCancerTherapyByPatientidAndDoctoridAndName(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName);
		        			if(ctm.getDatetimeexec() == "")
		        			{
		        				ctm.setDatetimeset(DateTimeUtils.getInstance().FormatDateTimeToDB(getActivity().getApplicationContext(), m_etEditTextCancerTherapyDate.getText().toString(), m_etEditTextCancerTherapyTime.getText().toString()));

		     					if (CancerTherapyEntry.updateCancerTherapy(getActivity().getApplicationContext(), ctm) == true)
		     					{
				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateOk, Toast.LENGTH_LONG).show();
		     					}
		     					else
		     					{
				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateError, Toast.LENGTH_LONG).show();
		     					}
		        			}
		        			else
		        			{
			   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateErrorAlreadyExec, Toast.LENGTH_LONG).show();				        				
		        			}
		            	}
						else
						{
		        			// Ok, it should not exist
	 	    				CancerTherapy ctm = new CancerTherapy(m_pm.getId(), m_dm.getId(), strTherapyName, DateTimeUtils.getInstance().FormatDateTimeToDB(getActivity().getApplicationContext(), m_Calendar), "");
				        	long lID = CancerTherapyEntry.getNextID(getActivity().getApplicationContext());
				        	ctm.setId(lID);
	      	    			// Ok, i add locally
   	    					addCancerTherapy(getActivity().getApplicationContext(), ctm);
						} 	 	 		      	    				
			        }	 				
    			}
       		});    
    		     		
    		// Set an OnClickListener
    		m_bButtonSetCancerTherapyDelete.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    				if(m_dm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoDoctorSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				if(m_pm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoPatientSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				// Therapy Name
    				final String strTherapyName = m_etEditTextCancerTherapyName.getText().toString();
    				if(strTherapyName.length() < 3)
    				{
	   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyBadNameFormat, Toast.LENGTH_LONG).show();
    					return;
    				}
    				// Check if i am in local mode
			        if(MainActivity.LOCAL_MODE == false)
			        {
			        	// I get the remote Cancer Therapy
						final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
						
						CallableTask.invoke(new Callable<CancerTherapy>() {

							@Override
							public CancerTherapy call() throws Exception {
								return svc.getCancerTherapyByPatientidAndDoctoridAndName(m_pm.getId(), m_dm.getId(), strTherapyName);
							}
						}, new TaskCallback<CancerTherapy>() 
						{
							@Override
							public void success(CancerTherapy result) 
							{
				        		if(result != null && result.getId() > 0)
				            	{
				        			// I can delete only cancer therapy that is not executed
				        			
				        			if(result.getDatetimeexec() == "")
				        			{
				        				
				        				final CancerTherapy ctm = result;
				        				// I ask for confirm
					    				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					    	            builder.setTitle(getString(R.string.labelQuestion_No_Yes))
					    	            .setItems(R.array.labelAnswer_No_Yes, new DialogInterface.OnClickListener() 
					    	            {
					    	                @Override
					    					public void onClick(DialogInterface dialog, int which) 
					    	                {
					    	             	   switch (which)
					    	             	   {
					    			           	   		case 0:
					    			        	   			
					    			        	   			break;
					
					    			           	   		case 1:

					    			           	   			// I delete the remote cancer therapy
					    									final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
					    									
					    									CallableTask.invoke(new Callable<Void>() {

					    										@Override
					    										public Void call() throws Exception {
					    											return svc.deleteCancerTherapy(ctm.getId());
					    										}
					    									}, new TaskCallback<Void>() 
					    									{
					    										@Override
					    										public void success(Void result) 
					    										{
							    			     					if (CancerTherapyEntry.deleteCancerTherapy(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName) == true)
							    			     					{
							        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteOk, Toast.LENGTH_LONG).show();
							        				   					resetField();
							    			     					}
							    			     					else
							    			     					{
							        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteError, Toast.LENGTH_LONG).show();
							    			     					}
								
					    										}

					    										@Override
					    										public void error(Exception e) 
					    										{
					    											Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewGetDoctorListError, Toast.LENGTH_LONG).show();
					    											CancerTherapySvc.close();	
					    										}
					    									});

					    									break;
					    									
					    	             	   }
					    	                }
					    	            });
									
					    	            builder.create();
									
					    	            builder.show();						        				
				        			}
				        			else
				        			{
					   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteErrorAlreadyExec, Toast.LENGTH_LONG).show();				        				
				        			}
				            	}
			        			else
			        			{
				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteErrorNotFound, Toast.LENGTH_LONG).show();				        				
			        			}
							}

							@Override
							public void error(Exception e) 
							{
								Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextGetCancerTherapyListError, Toast.LENGTH_LONG).show();
								CancerTherapySvc.close();	
							}
						});
			        }
			        else
				    {
	    				// I will check that the Patient did not make the therapy...
	    				CancerTherapy ctm = CancerTherapyEntry.getCancerTherapyByPatientidAndDoctoridAndName(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName);

	    				if(ctm.getId() > 0)
	    				{
		    				if(ctm.getDatetimeexec() == "")
		    				{    					
			    				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    	            builder.setTitle(getString(R.string.labelQuestion_No_Yes))
			    	            .setItems(R.array.labelAnswer_No_Yes, new DialogInterface.OnClickListener() 
			    	            {
			    	                @Override
			    					public void onClick(DialogInterface dialog, int which) 
			    	                {
			    	             	   switch (which)
			    	             	   {
			    			           	   		case 0:
			    			        	   			
			    			        	   			break;
			
			    			           	   		case 1:
			    				
			    			     					if (CancerTherapyEntry.deleteCancerTherapy(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName) == true)
			    			     					{
			        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteOk, Toast.LENGTH_LONG).show();
			        				   					resetField();
			    			     					}
			    			     					else
			    			     					{
			        				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteError, Toast.LENGTH_LONG).show();
			    			     					}
			
			    			     					break;
			
			    	             	   }
			    	                }
			    	            });
							
			    	            builder.create();
							
			    	            builder.show();		
		        			}
		        			else
		        			{
			   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteErrorAlreadyExec, Toast.LENGTH_LONG).show();
		        			}    				
	        			}    				
	        			else
	        			{
		   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteErrorNotFound, Toast.LENGTH_LONG).show();
	        			}  
				    }
    			}
			});    
    		
    		// Set an OnClickListener
    		m_bButtonSetCancerTherapyList.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    				if(m_dm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoDoctorSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				if(m_pm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoPatientSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				// Check if i am in local mode
    		        if(MainActivity.LOCAL_MODE == false)
    		        {
    					// I get the connection
    					final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
    					
    					CallableTask.invoke(new Callable<Collection<CancerTherapy>>() {

    						@Override
    						public Collection<CancerTherapy> call() throws Exception {
    							return svc.getCancerTherapyByPatientidAndDoctorid(m_pm.getId(), m_dm.getId());
    						}
    					}, new TaskCallback<Collection<CancerTherapy>>() 
    					{
    						@Override
    						public void success(Collection<CancerTherapy> result) 
    						{
    			        		if(result != null)
    			            	{
    						        for (CancerTherapy ctm : result)
    						        {
    									// Store all Cancer Therapy in the Local DB if not already stored
    				 					if (CancerTherapyEntry.IsCancerTherapyIDPresent(getActivity().getApplicationContext(), ctm.getId()) == false)
    				 					{
    				 						if (CancerTherapyEntry.addCancerTherapy(getActivity().getApplicationContext(), ctm) == true)
    				 						{
    				 							// Ok
    				 						}
    				 						else
    				 						{
    				 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetCancerTherapyAddError, Toast.LENGTH_LONG).show();
    				 	 						break;
    				 						}
    				 					}
    				 					else
    				 					{
    					        			// Ok, it should be exist
    				     					if (CancerTherapyEntry.updateCancerTherapy(getActivity().getApplicationContext(), ctm) == true)
    				     					{
    				 							// Ok
    				     					}
    				     					else
    				     					{
    	    				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateError, Toast.LENGTH_LONG).show();
    				 	 						break;
    				     					}			 					
    				 					}
    			 					}
    						        
    		        				Intent intent = CancerTherapyListActivity.makeCancerTherapyListActivity(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId());
    		        		        startActivityForResult(intent, ACTIVITY_CANCER_THERAPY_LIST_ACTIVITY);    		        	
    			            	}
    						}

    						@Override
    						public void error(Exception e) 
    						{
    							Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewGetDoctorListError, Toast.LENGTH_LONG).show();
    							CancerTherapySvc.close();	
    						}
    					});    		        	
    		        }
    		        else
    		        {
         				Intent intent = CancerTherapyListActivity.makeCancerTherapyListActivity(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId());
        		        startActivityForResult(intent, ACTIVITY_CANCER_THERAPY_LIST_ACTIVITY);    		        	
    		        }
    			}
       		});    
    	} 
 
    	private void addCancerTherapy(Context context, CancerTherapy ctm)
    	{
			if (CancerTherapyEntry.addCancerTherapy(getActivity(), ctm) == true)
			{
				Toast.makeText(context, R.string.labelTextSetCancerTherapyAddOk, Toast.LENGTH_LONG).show();
				
				resetField();
			}
			else
			{
				Toast.makeText(context, R.string.labelTextSetCancerTherapyAddError, Toast.LENGTH_LONG).show();
			}
    	}
     	
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
		    if (requestCode == ACTIVITY_CANCER_THERAPY_LIST_ACTIVITY) 
		    {
		        if(resultCode == RESULT_OK)
		        {
		            long lCancerTherapyID = data.getLongExtra(CancerTherapyListActivity.CANCER_THERAPY_ID, 0);
		            if(lCancerTherapyID > 0)
		            {
		            	CancerTherapy ctm = CancerTherapyEntry.getCancerTherapyByID(getActivity().getApplicationContext(), lCancerTherapyID);
		            	Calendar calendar = Calendar.getInstance();
		            	
			    		m_etEditTextCancerTherapyName.setText(ctm.getName());
			    		
			    		calendar = DateTimeUtils.getInstance().FormatDateTimeFromDBToCalendar(getActivity().getApplicationContext(), ctm.getDatetimeset());
			    		m_etEditTextCancerTherapyDate.setText(DateTimeUtils.getInstance().FormatDate(getActivity().getApplicationContext(), calendar));
			    		m_etEditTextCancerTherapyTime.setText(DateTimeUtils.getInstance().FormatTime(getActivity().getApplicationContext(), calendar));
			    		m_strDateTimeOfTreatmentExec = ctm.getDatetimeexec();
		            }
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

     	}

        private void UpdateFragment()
        {

        }    
        
        private void resetField()
        {
    		m_etEditTextCancerTherapyName.setText("");
    		m_etEditTextCancerTherapyDate.setText("");
    		m_etEditTextCancerTherapyTime.setText("");
        }
    }
	
    /**
     * A Doctor Pain Therapy Fragment fragment.
     */
    public static class DoctorPainTherapyFragment extends Fragment 
    {
    	public final static int ACTIVITY_PAIN_THERAPY_LIST_ACTIVITY = 1;
		private TextView m_etEditTextPainTherapyName; 
		private Button m_bButtonSetPainTherapyList;
		private Button m_bButtonSetPainTherapyAddUpdate;
		private Button m_bButtonSetPainTherapyDelete;

		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DoctorPainTherapyFragment newInstance(int sectionNumber) 
        {
        	DoctorPainTherapyFragment fragment = new DoctorPainTherapyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DoctorPainTherapyFragment() 
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            View rootView = inflater.inflate(R.layout.doctor_pain_therapy_fragment, container, false);
            return rootView;
        }

    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) 
    	{
    		super.onActivityCreated(savedInstanceState);
    		
    		m_etEditTextPainTherapyName = (TextView) getActivity().findViewById(R.id.idEditTextPainTherapyName);
    		m_bButtonSetPainTherapyList = (Button) getActivity().findViewById(R.id.idButtonSetPainTherapyList);
    		m_bButtonSetPainTherapyAddUpdate = (Button) getActivity().findViewById(R.id.idButtonSetPainTherapyAddUpdate);
    		m_bButtonSetPainTherapyDelete = (Button) getActivity().findViewById(R.id.idButtonSetPainTherapyDelete);

    		
    		// Set an OnClickListener
    		m_bButtonSetPainTherapyAddUpdate.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    				if(m_dm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyNoDoctorSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				if(m_pm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyNoPatientSelected, Toast.LENGTH_LONG).show();
    					return;
    				}

    				// Therapy Name
    				final String strTherapyName = m_etEditTextPainTherapyName.getText().toString();
    				if(strTherapyName.length() < 3)
    				{
	   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyAddBadNameFormat, Toast.LENGTH_LONG).show();
    					return;
    				}
    
    				// Check if i am in local mode
			        if(MainActivity.LOCAL_MODE == false)
			        {
 						// I get the connection
 						final PainTherapySvcApi svc = PainTherapySvc.get(getActivity().getApplicationContext());

 						// Ok, i will check if remotely exist the same UserName
 						CallableTask.invoke(new Callable<PainTherapy>() {

 							@Override
 							public PainTherapy call() throws Exception {
 								return svc.getPainTherapyByPatientidAndDoctoridAndName(m_pm.getId(), m_dm.getId(), strTherapyName);
 							}
 						}, new TaskCallback<PainTherapy>() 
 						{
 							@Override
 							public void success(PainTherapy result) 
 							{
				        		if(result != null && result.getId() > 0)
				            	{
			        				// Update the data that i needs. The name cannot be updated because is in univoche
				        			// Actually, in Pain Therapy there is nothing to update but in the future, may be 
				        			// that we decide to add some data that could be possible to update
			        				final PainTherapy ptm = result;
			        				// I ask for confirm
				    				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    	            builder.setTitle(getString(R.string.labelQuestion_No_Yes))
				    	            .setItems(R.array.labelAnswer_No_Yes, new DialogInterface.OnClickListener() 
				    	            {
				    	                @Override
				    					public void onClick(DialogInterface dialog, int which) 
				    	                {
				    	             	   switch (which)
				    	             	   {
				    			           	   		case 0:
				    			        	   			
				    			        	   			break;
				
				    			           	   		case 1:

				    			           	   			// I update the remote cancer therapy
				    									final PainTherapySvcApi svc = PainTherapySvc.get(getActivity().getApplicationContext());
				    									
				    									CallableTask.invoke(new Callable<PainTherapy>() {

				    										@Override
				    										public PainTherapy call() throws Exception {
				    											return svc.updatePainTherapy(ptm);
				    										}
				    									}, new TaskCallback<PainTherapy>() 
				    									{
				    										@Override
				    										public void success(PainTherapy result) 
				    										{
				    							        		if(result != null)
				    							            	{
				    												if (PainTherapyEntry.IsPainTherapyNamePresent(getActivity().getApplicationContext(), result.getPatientid(), result.getDoctorid(), result.getName()) == true)
				    							 					{
					    							        			// Ok, it should be exist
								    			     					if (PainTherapyEntry.updatePainTherapy(getActivity().getApplicationContext(), result) == true)
								    			     					{
								        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyUpdateOk, Toast.LENGTH_LONG).show();
								    			     					}
								    			     					else
								    			     					{
								        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyUpdateError, Toast.LENGTH_LONG).show();
								    			     					}
					    							            	}
				    												else
				    												{
				    													// Strange, but could be that i have delete tha app and i don't yet get the sincronization
				    													// so i will add it
				    		 	 	 		      	    				if(result != null && result.getId() > 0)
				    		 	 	 		      	    				{
				    				 	 	 		      	    			// Ok, i add locally
				    		 	 	 		      	    					addPainTherapy(getActivity().getApplicationContext(), result);
				    		 	 	 		      	    				}
				    		 	 	 		      	    				else
				    		 	 	 		      	    				{
				    			 	 	 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetPainTherapyAddError, Toast.LENGTH_LONG).show();	 	 	 		      	    					
				    		 	 	 		      	    				}					    													
				    												}
				    							            	}
				    										}

				    										@Override
				    										public void error(Exception e) 
				    										{
				    											Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetPainTherapyUpdateError, Toast.LENGTH_LONG).show();
				    											PainTherapySvc.close();	
				    										}
				    									});

				    									break;
				    									
				    	             	   }
				    	                }
				    	            });
								
				    	            builder.create();
								
				    	            builder.show();						        				
				            	}
			        			else
			        			{
			        				// Not found, i will add it....
			 	    				final PainTherapy ptm = new PainTherapy(m_pm.getId(), m_dm.getId(), strTherapyName);
			        				
 	 		 						// Ok i can add the patient
 	 	 	 			      		CallableTask.invoke(new Callable<PainTherapy>() 
 	 	 		      				{
 	 	 		      	    			@Override
 	 	 		      	    			public PainTherapy call() throws Exception 
 	 	 		      	    			{
 	 	 		      	    				return svc.addPainTherapy(ptm);
 	 	 		      	    			}
 	 	 		      	    		}, new TaskCallback<PainTherapy>() 
 	 	 		      	    		{
 	 	 		      	    			@Override
 	 	 		      	    			public void success(PainTherapy result) 
 	 	 		      	    			{
											if (PainTherapyEntry.IsPainTherapyNamePresent(getActivity().getApplicationContext(), result.getPatientid(), result.getDoctorid(), result.getName()) == true)
						 					{
												// Strange, but could be that i have delete the app and i don't yet get the sincronization
												// so i will update it
		    			     					if (PainTherapyEntry.updatePainTherapy(getActivity().getApplicationContext(), result) == true)
		    			     					{
		        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyUpdateOk, Toast.LENGTH_LONG).show();
		    			     					}
		    			     					else
		    			     					{
		        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyUpdateError, Toast.LENGTH_LONG).show();
		    			     					}
							            	}
											else
											{
							        			// Ok, it should not exist
	 	 	 		      	    				if(result != null && result.getId() > 0)
	 	 	 		      	    				{
			 	 	 		      	    			// Ok, i add locally
	 	 	 		      	    					addPainTherapy(getActivity().getApplicationContext(), result);
	 	 	 		      	    				}
	 	 	 		      	    				else
	 	 	 		      	    				{
		 	 	 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetPainTherapyAddError, Toast.LENGTH_LONG).show();	 	 	 		      	    					
	 	 	 		      	    				}					    													
											} 
        				   					resetField();											
 	 	 		      	    			}

 	 	 		      	    			@Override
 	 	 		      	    			public void error(Exception e) 
 	 	 		      	    			{
 	 	 		      	    				Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetPainTherapyAddError, Toast.LENGTH_LONG).show();
 	 	 		      	    				PainTherapySvc.close();	
 	 	 		      	    			}
 	 	 		      	    		}); 				        				
			        			}
 							} 					        			
				        			
							@Override
							public void error(Exception e) 
							{
								Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextGetPainTherapyListError, Toast.LENGTH_LONG).show();
								PainTherapySvc.close();	
							}
						});						        			
					}
			        else
			        {
						if (PainTherapyEntry.IsPainTherapyNamePresent(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName) == true)
	 					{
							PainTherapy ptm = PainTherapyEntry.getPainTherapyByPatientidAndDoctoridAndName(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName);
	
	     					if (PainTherapyEntry.updatePainTherapy(getActivity().getApplicationContext(), ptm) == true)
	     					{
			   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateOk, Toast.LENGTH_LONG).show();
	     					}
	     					else
	     					{
			   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyUpdateError, Toast.LENGTH_LONG).show();
	     					}

		            	}
						else
						{
		        			// Ok, it should not exist
	 	    				PainTherapy ptm = new PainTherapy(m_pm.getId(), m_dm.getId(), strTherapyName);
				        	long lID = PainTherapyEntry.getNextID(getActivity().getApplicationContext());
				        	ptm.setId(lID);
	      	    			// Ok, i add locally
   	    					addPainTherapy(getActivity().getApplicationContext(), ptm);
						} 	 	 		      	    				
			        }	 				
		        }	 				
   			});   			        			

    		// Set an OnClickListener
    		m_bButtonSetPainTherapyDelete.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    				if(m_dm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyNoDoctorSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				if(m_pm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyNoPatientSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				// Therapy Name
    				final String strTherapyName = m_etEditTextPainTherapyName.getText().toString();
    				if(strTherapyName.length() < 3)
    				{
	   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyBadNameFormat, Toast.LENGTH_LONG).show();
    					return;
    				}
    				// Check if i am in local mode
			        if(MainActivity.LOCAL_MODE == false)
			        {
			        	// I get the remote Cancer Therapy
						final PainTherapySvcApi svc = PainTherapySvc.get(getActivity().getApplicationContext());
						
						CallableTask.invoke(new Callable<PainTherapy>() {

							@Override
							public PainTherapy call() throws Exception {
								return svc.getPainTherapyByPatientidAndDoctoridAndName(m_pm.getId(), m_dm.getId(), strTherapyName);
							}
						}, new TaskCallback<PainTherapy>() 
						{
							@Override
							public void success(PainTherapy result) 
							{
				        		if(result != null && result.getId() > 0)
				            	{
				        			// I can delete only cancer therapy that is not executed
			        				final PainTherapy ptm = result;
			        				// I ask for confirm
				    				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    	            builder.setTitle(getString(R.string.labelQuestion_No_Yes))
				    	            .setItems(R.array.labelAnswer_No_Yes, new DialogInterface.OnClickListener() 
				    	            {
				    	                @Override
				    					public void onClick(DialogInterface dialog, int which) 
				    	                {
				    	             	   switch (which)
				    	             	   {
				    			           	   		case 0:
				    			        	   			
				    			        	   			break;
				
				    			           	   		case 1:

				    			           	   			// I delete the remote cancer therapy
				    									final PainTherapySvcApi svc = PainTherapySvc.get(getActivity().getApplicationContext());
				    									
				    									CallableTask.invoke(new Callable<Void>() {

				    										@Override
				    										public Void call() throws Exception {
				    											return svc.deletePainTherapy(ptm.getId());
				    										}
				    									}, new TaskCallback<Void>() 
				    									{
				    										@Override
				    										public void success(Void result) 
				    										{
						    			     					if (PainTherapyEntry.deletePainTherapy(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName) == true)
						    			     					{
						        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyDeleteOk, Toast.LENGTH_LONG).show();
						        				   					resetField();
						    			     					}
						    			     					else
						    			     					{
						        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyDeleteError, Toast.LENGTH_LONG).show();
						    			     					}
							
				    										}

				    										@Override
				    										public void error(Exception e) 
				    										{
				    											Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextGetPainTherapyListError, Toast.LENGTH_LONG).show();
				    											PainTherapySvc.close();	
				    										}
				    									});

				    									break;
				    									
				    	             	   }
				    	                }
				    	            });
								
				    	            builder.create();
								
				    	            builder.show();						        				

				            	}
			        			else
			        			{
				   					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyDeleteErrorNotFound, Toast.LENGTH_LONG).show();				        				
			        			}
							}

							@Override
							public void error(Exception e) 
							{
								Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextGetCancerTherapyListError, Toast.LENGTH_LONG).show();
								PainTherapySvc.close();	
							}
						});
			        }
			        else
				    {
	    				// I will check that the Patient did not make the therapy...
			        	PainTherapy ptm = PainTherapyEntry.getPainTherapyByPatientidAndDoctoridAndName(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName);

	    				if(ptm.getId() > 0)
	    				{
		    				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    	            builder.setTitle(getString(R.string.labelQuestion_No_Yes))
		    	            .setItems(R.array.labelAnswer_No_Yes, new DialogInterface.OnClickListener() 
		    	            {
		    	                @Override
		    					public void onClick(DialogInterface dialog, int which) 
		    	                {
		    	             	   switch (which)
		    	             	   {
		    			           	   		case 0:
		    			        	   			
		    			        	   			break;
		
		    			           	   		case 1:
		    				
		    			     					if (PainTherapyEntry.deletePainTherapy(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId(), strTherapyName) == true)
		    			     					{
		        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyDeleteOk, Toast.LENGTH_LONG).show();
		        				   					resetField();
		    			     					}
		    			     					else
		    			     					{
		        				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyDeleteError, Toast.LENGTH_LONG).show();
		    			     					}
		
		    			     					break;
		
		    	             	   }
		    	                }
		    	            });
						
		    	            builder.create();
						
		    	            builder.show();		
   				
	        			}    				
	        			else
	        			{
		   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyDeleteErrorNotFound, Toast.LENGTH_LONG).show();
	        			}  
				    }
    			}
			});    
    		
    		// Set an OnClickListener
    		m_bButtonSetPainTherapyList.setOnClickListener(new View.OnClickListener() 
    		{
     			@Override
				public void onClick(View v) 
    			{
    				if(m_dm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoDoctorSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
    				
    				if(m_pm.getId() == 0)
    				{
    					Toast.makeText(getActivity(), R.string.labelTextSetCancerTherapyNoPatientSelected, Toast.LENGTH_LONG).show();
    					return;
    				}
       				// Check if i am in local mode
    		        if(MainActivity.LOCAL_MODE == false)
    		        {
    					// I get the connection
    					final PainTherapySvcApi svc = PainTherapySvc.get(getActivity().getApplicationContext());
    					
    					CallableTask.invoke(new Callable<Collection<PainTherapy>>() {

    						@Override
    						public Collection<PainTherapy> call() throws Exception {
    							return svc.getPainTherapyByPatientidAndDoctorid(m_pm.getId(), m_dm.getId());
    						}
    					}, new TaskCallback<Collection<PainTherapy>>() 
    					{
    						@Override
    						public void success(Collection<PainTherapy> result) 
    						{
    			        		if(result != null)
    			            	{
    						        for (PainTherapy ctm : result)
    						        {
    									// Store all Cancer Therapy in the Local DB if not already stored
    				 					if (PainTherapyEntry.IsPainTherapyIDPresent(getActivity().getApplicationContext(), ctm.getId()) == false)
    				 					{
    				 						if (PainTherapyEntry.addPainTherapy(getActivity().getApplicationContext(), ctm) == true)
    				 						{
    				 							// Ok
    				 						}
    				 						else
    				 						{
    				 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetPainTherapyAddError, Toast.LENGTH_LONG).show();
    				 	 						break;
    				 						}
    				 					}
    				 					else
    				 					{
    					        			// Ok, it should be exist
    				     					if (PainTherapyEntry.updatePainTherapy(getActivity().getApplicationContext(), ctm) == true)
    				     					{
    				 							// Ok
    				     					}
    				     					else
    				     					{
    	    				   					Toast.makeText(getActivity(), R.string.labelTextSetPainTherapyUpdateError, Toast.LENGTH_LONG).show();
    				 	 						break;
    				     					}			 					
    				 					}
    			 					}
    						        
    		        				Intent intent = PainTherapyListActivity.makePainTherapyListActivity(getActivity().getApplicationContext(), m_pm.getId(), m_dm.getId());
    		        		        startActivityForResult(intent, ACTIVITY_PAIN_THERAPY_LIST_ACTIVITY);
    			            	}
    						}

    						@Override
    						public void error(Exception e) 
    						{
    							Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextViewGetDoctorListError, Toast.LENGTH_LONG).show();
    							PainTherapySvc.close();	
    						}
    					});    		        	
    		        }
    		        else
    		        {
        				Intent intent = PainTherapyListActivity.makePainTherapyListActivity(getActivity().getApplicationContext(), m_dm.getId(), m_pm.getId());
        		        startActivityForResult(intent, ACTIVITY_PAIN_THERAPY_LIST_ACTIVITY);
    		        }    				
    			}
       		});    

    	} 
    	
    	private void addPainTherapy(Context context, PainTherapy ptm)
    	{
			if (PainTherapyEntry.addPainTherapy(getActivity(), ptm) == true)
			{
				Toast.makeText(context, R.string.labelTextSetPainTherapyAddOk, Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(context, R.string.labelTextSetPainTherapyAddError, Toast.LENGTH_LONG).show();
			}
    	}
    	
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
		    if (requestCode == ACTIVITY_PAIN_THERAPY_LIST_ACTIVITY) 
		    {
		        if(resultCode == RESULT_OK)
		        {
		            long lPainTherapyID = data.getLongExtra(PainTherapyListActivity.PAIN_THERAPY_ID, 0);
		            if(lPainTherapyID > 0)
		            {
		            	PainTherapy ptm = PainTherapyEntry.getPainTherapy(getActivity().getApplicationContext(), lPainTherapyID);
		            	
		            	m_etEditTextPainTherapyName.setText(ptm.getName());
		            }
				}  
			}  
		}  
		
    	@Override
    	public void onResume() 
    	{
      		super.onResume();
      		
      		UpdateFragment();
    	}
    	
        private void UpdateFragment()
        {
       	
        }  
        
        private void resetField()
        {
    		m_etEditTextPainTherapyName.setText("");
         }
            
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor, menu);
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
        
        if (id == R.id.action_signin) 
        {
			intent = SignInActivity.makeSignInActivity(getApplicationContext());
			startActivity(intent);
			
			// Close this activity, i will re-create, in SignInActivity Activity, the right one
			finish();
        	
            return true;
        }

        if (id == R.id.idActionSignUpDoctor) 
        {
			intent = DoctorSignUpActivity.makeSignUpDoctorActivity(getApplicationContext());
			startActivity(intent);
        	
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
	public static Intent makeDoctorViewActivity(Context context, long lID, long[] alPatienIDToCheck) 
	{
		Intent intent = new Intent();
		intent.setClass(context, DoctorViewActivity.class);
		intent.putExtra(DoctorViewActivity.ID, lID);	
		intent.putExtra(DoctorViewActivity.LIST_PATIENT_ID, alPatienIDToCheck);	
		return intent;
	}	
 }
