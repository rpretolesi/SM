package com.capstone.sm.patient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.Callable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.CancerTherapyEntry;
import com.capstone.SQL.SQLContract.DoctorEntry;
import com.capstone.SQL.SQLContract.PainTherapyEntry;
import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.PatientEntry;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.MainActivity;
import com.capstone.sm.R;
import com.capstone.sm.SignInActivity;
import com.capstone.sm.cancertherapy.CancerTherapy;
import com.capstone.sm.cancertherapy.CancerTherapyListAdapter;
import com.capstone.sm.cancertherapy.CancerTherapySvc;
import com.capstone.sm.cancertherapy.CancerTherapySvcApi;
import com.capstone.sm.comm.client.CallableTask;
import com.capstone.sm.comm.client.TaskCallback;
import com.capstone.sm.datetime.DateTimeUtils;
import com.capstone.sm.paintherapy.PainTherapyListAdapter;
import com.capstone.sm.paintherapy.PainTherapy;
import com.capstone.sm.paintherapy.PainTherapySvc;
import com.capstone.sm.paintherapy.PainTherapySvcApi;

public class PatientViewActivity extends PatientBaseBarActivity 
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

    	Intent intent = getIntent();
        
        if(intent != null)
        {
        	long lID = intent.getLongExtra(PatientViewActivity.ID, 0);
        	m_pm = PatientEntry.getPatient(this, lID);
			m_dm = DoctorEntry.getDoctor(this, m_pm.getDoctorid());
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
    {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        
        String strTAG = mSectionsPagerAdapter.getFragmentTag(mViewPager.getId(), tab.getPosition());

        // Update data in single fragment
        if(tab.getPosition() == 0)
        {
        	PatientCancerTherapyFragment pctf = (PatientCancerTherapyFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(pctf != null)
            {
            	if(pctf.isResumed() == true)
            	{
            		pctf.UpdateFragment();
                }
            }
        }
        if(tab.getPosition() == 1)
        {
        	PatientPainTherapyFragment pptf = (PatientPainTherapyFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(pptf != null)
            {
            	if(pptf.isResumed() == true)
            	{
            		pptf.UpdateFragment();
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
           		fragment = PatientCancerTherapyFragment.newInstance(position + 1);
            }
            if (position == 1) 
            {
           		fragment = PatientPainTherapyFragment.newInstance(position + 1);
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
                    return getString(R.string.title_section_patient_cancer_therapy).toUpperCase(l);
                    
                case 1:
                    return getString(R.string.title_section_patient_pain_therapy).toUpperCase(l);
            }
            return null;
        }
        
        private String getFragmentTag(int viewPagerId, int fragmentPosition)
        {
             return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
        }        
    }
    
    
    /**
     * A PatientCancerTherapy fragment.
     */
    public static class PatientCancerTherapyFragment extends ListFragment 
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    	
        private static final String ARG_SECTION_NUMBER = "section_number";
    	private CancerTherapyListAdapter m_adapter;
    	private ArrayList<CancerTherapy> m_alctm;
        
    
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PatientCancerTherapyFragment newInstance(int sectionNumber) 
        {
        	PatientCancerTherapyFragment fragment = new PatientCancerTherapyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PatientCancerTherapyFragment() 
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
    		
    		if(m_bReminderStatus == false)
    		{
        		// Start Reminder
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
    		}
    		
    		m_adapter = new CancerTherapyListAdapter(getActivity());
    		setListAdapter(m_adapter);	    		
    	}
       	@Override
		public void onListItemClick(ListView l, View v, int position, long id) 
    	{
    		super.onListItemClick(l, v, position, id);
    		
    		final int iPosition = position;
    		
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
		           	   			
		           				// Check if i am in local mode
		           		        if(MainActivity.LOCAL_MODE == false)
		           		        {
		           					// I get the connection
		           					final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
		           					
		           					CallableTask.invoke(new Callable<CancerTherapy>() {

		           						@Override
		           						public CancerTherapy call() throws Exception {
		           							// Update date and time exec.
					           	    		CancerTherapy ctm = m_alctm.get(iPosition);
					           	    		ctm.setDatetimeexec(DateTimeUtils.getInstance().FormatDateTimeToDB(getActivity().getApplicationContext(), Calendar.getInstance()));
		           							return svc.updateCancerTherapy(ctm);
		           						}
		           					}, new TaskCallback<CancerTherapy>() 
		           					{
		           						@Override
		           						public void success(CancerTherapy result) 
		           						{
		           			        		if(result != null)
		           			            	{

		    			           	    		if (CancerTherapyEntry.setCancerTherapyDateTimeExec(getActivity().getApplicationContext(), result.getId(), result.getDatetimeexec()) == true)
		    			           	    		{
			    			           	    		Toast.makeText(getActivity().getApplicationContext(), R.string.labelAnswerThankYou, Toast.LENGTH_LONG).show();		    			           	    		
		    			           	    		}
		    			           	    		else
		    			           	    		{
			    			           	    		Toast.makeText(getActivity().getApplicationContext(), R.string.labelAnswerError, Toast.LENGTH_LONG).show();		    			           	    		
		    			           	    		}
		    			           	    		

		    			           	    		UpdateFragment();	
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
			           	    		CancerTherapyEntry.setCancerTherapyDateTimeExec(getActivity().getApplicationContext(), m_alctm.get(iPosition).getId(), DateTimeUtils.getInstance().FormatDateTimeToDB(getActivity().getApplicationContext(), Calendar.getInstance()));

			           	    		Toast.makeText(getActivity().getApplicationContext(), R.string.labelAnswerThankYou, Toast.LENGTH_LONG).show();
			           	    		
			           	    		UpdateFragment();
			        	   						        	   			
		           		        }
		           		        
		        	   			break;

             	   }
                }
            });
            
            builder.create();
            
            builder.show();
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
				final CancerTherapySvcApi svc = CancerTherapySvc.get(getActivity().getApplicationContext());
				
				CallableTask.invoke(new Callable<Collection<CancerTherapy>>() {

					@Override
					public Collection<CancerTherapy> call() throws Exception {
						return svc.getCancerTherapyByPatientid(m_pm.getId());
					}
				}, new TaskCallback<Collection<CancerTherapy>>() 
				{
					@Override
					public void success(Collection<CancerTherapy> result) 
					{
		        		if(result != null)
		            	{
				        	// Before to do that, i will delete the old one...
		        			CancerTherapyEntry.deleteAllCancerTherapy(getActivity().getApplicationContext());
		        			
					        for (CancerTherapy ctm : result)
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

					        // Update Value
				        	if(m_adapter != null)
				        	{
				            	m_alctm = CancerTherapyEntry.getListCancerTherapy(getActivity().getApplication(),m_pm.getId() ,m_pm.getDoctorid());
				            	if(m_alctm != null)
				            	{
				            		m_adapter.updateData(m_alctm);      	
				            	}
				        	}		
				        	
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
	        	if(m_adapter != null)
	        	{
	            	m_alctm = CancerTherapyEntry.getListCancerTherapy(getActivity().getApplication(),m_pm.getId() ,m_pm.getDoctorid());
	            	if(m_alctm != null)
	            	{
	            		m_adapter.updateData(m_alctm);      	
	            	}
	        	}
	        	
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
        }      
    }
    
    /**
     * A PatientPainTherapy fragment.
     */
    public static class PatientPainTherapyFragment extends ListFragment 
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    	
        private static final String ARG_SECTION_NUMBER = "section_number";
    	private PainTherapyListAdapter m_adapter;
    	private ArrayList<PainTherapy> m_alptm;
        
    
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PatientPainTherapyFragment newInstance(int sectionNumber) 
        {
        	PatientPainTherapyFragment fragment = new PatientPainTherapyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PatientPainTherapyFragment() 
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

    		m_adapter = new PainTherapyListAdapter(getActivity());
    		setListAdapter(m_adapter);	    		
    	}
    	
       	@Override
		public void onListItemClick(ListView l, View v, int position, long id) 
    	{
    		super.onListItemClick(l, v, position, id);
	    		
    		Toast.makeText(getActivity().getApplicationContext(), R.string.labelClickOnPainTherapyList, Toast.LENGTH_LONG).show();
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
				final PainTherapySvcApi svc = PainTherapySvc.get(getActivity().getApplicationContext());
				
				CallableTask.invoke(new Callable<Collection<PainTherapy>>() {

					@Override
					public Collection<PainTherapy> call() throws Exception {
						return svc.getPainTherapyByPatientid(m_pm.getId());
					}
				}, new TaskCallback<Collection<PainTherapy>>() 
				{
					@Override
					public void success(Collection<PainTherapy> result) 
					{
		        		if(result != null)
		            	{
				        	// Before to do that, i will delete the old one...
				        	PainTherapyEntry.deleteAllPainTherapy(getActivity().getApplicationContext());
		        			
					        for (PainTherapy ptm : result)
					        {
								// Store all Pain Therapy in the Local DB
					        	if (PainTherapyEntry.addPainTherapy(getActivity().getApplicationContext(), ptm) == true)
		 						{
		 							// Ok
		 						}
		 						else
		 						{
		 	 						Toast.makeText(getActivity().getApplicationContext(), R.string.labelTextSetPainTherapyAddError, Toast.LENGTH_LONG).show();
		 	 						break;
		 						}
		 					}	

					        // Update Value
				        	if(m_adapter != null)
				        	{
				            	m_alptm = PainTherapyEntry.getListPainTherapy(getActivity().getApplication(),m_pm.getId() ,m_pm.getDoctorid());
				            	if(m_alptm != null)
				            	{
				            		m_adapter.updateData(m_alptm);      	
				            	}
				        	}	
				        	
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
	           	if(m_adapter != null)
	        	{
	            	m_alptm = PainTherapyEntry.getListPainTherapy(getActivity().getApplication(),m_pm.getId() ,m_pm.getDoctorid()); 
	            	if(m_alptm != null)
	            	{
	            		m_adapter.updateData(m_alptm);      	
	            	}
	        	}
	        	
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
        }      
    }
 
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient, menu);
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
			intent = PatientSettingActivity.makePatientSettingActivity(this, m_pm.getId());
			startActivity(intent);
        	
            return true;
        }
        
        if (id == R.id.action_signin) 
        {
			intent = SignInActivity.makeSignInActivity(this);
			startActivity(intent);
			
			// Close this activity, i will re-create, in SignInActivity Activity, the right one
			finish();
        	
            return true;
        }

        if (id == R.id.idActionSignUpPatient) 
        {
			intent = PatientSignUpActivity.makeSignUpPatientActivity(this);
			startActivity(intent);
        	
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
     
	public static Intent makePatientViewActivity(Context context, long lID) 
	{
		Intent intent = new Intent();
		intent.setClass(context, PatientViewActivity.class);
		intent.putExtra(PatientViewActivity.ID, lID);	
		return intent;
	}
}
