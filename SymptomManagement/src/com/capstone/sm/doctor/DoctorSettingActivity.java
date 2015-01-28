package com.capstone.sm.doctor;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.MainActivity;
import com.capstone.sm.R;

public class DoctorSettingActivity extends DoctorBaseBarActivity 
{
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

        if (savedInstanceState != null) 
        {

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
        	DoctorSettingFragment dsf = (DoctorSettingFragment)getSupportFragmentManager().findFragmentByTag(strTAG);
            if(dsf != null)
            {
            	if(dsf.isResumed() == true)
            	{
            		dsf.UpdateFragment();
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
            	fragment = DoctorSettingFragment.newInstance(position + 1);
            }

            return fragment;        	
        }

        @Override
        public int getCount() 
        {
            // Show 5 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) 
        {
      		Locale l = Locale.getDefault();
            switch (position) {
                case 0:
               		return getString(R.string.title_section_doctor_setting).toUpperCase(l);

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
    public static class DoctorSettingFragment extends Fragment 
    {
    	private EditText m_etEditTextHostAddressSetting;
    	private EditText m_etEditTextDoctorSettingUpdateFrequency; 
    	private TextView m_tvTextViewDoctorSettingUpdateIsRunning; 
    	
    	private Button m_bButtonDoctorSettingSave;
    	private Button m_bButtonDoctorSettingStartUpdate;
    	private Button m_bButtonDoctorSettingStopUpdate;
    	

		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DoctorSettingFragment newInstance(int sectionNumber) 
        {
        	DoctorSettingFragment fragment = new DoctorSettingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DoctorSettingFragment() 
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            View rootView = inflater.inflate(R.layout.doctor_setting_fragment, container, false);

            return rootView;
       	}
        
    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) 
    	{
    		super.onActivityCreated(savedInstanceState);
    		
    		m_etEditTextHostAddressSetting = (EditText) getActivity().findViewById(R.id.idEditTextHostAddressSetting);
    		m_etEditTextDoctorSettingUpdateFrequency = (EditText) getActivity().findViewById(R.id.idEditTextDoctorSettingUpdateFrequency);
    		m_tvTextViewDoctorSettingUpdateIsRunning = (TextView) getActivity().findViewById(R.id.idTextViewDoctorSettingUpdateIsRunning);            
    		m_bButtonDoctorSettingSave = (Button) getActivity().findViewById(R.id.idButtonDoctorSettingSave);
    		m_bButtonDoctorSettingStartUpdate = (Button) getActivity().findViewById(R.id.idButtonDoctorSettingStartUpdate);
    		m_bButtonDoctorSettingStopUpdate = (Button) getActivity().findViewById(R.id.idButtonDoctorSettingStopUpdate);

            // Set an OnClickListener
    		m_bButtonDoctorSettingSave.setOnClickListener(new View.OnClickListener() 
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

    				String strUpdateFrequency = m_etEditTextDoctorSettingUpdateFrequency.getText().toString();
    				long lUpdateFrequencyMins = 0;
    				try 
    				{
    					lUpdateFrequencyMins = Long.parseLong(strUpdateFrequency);
    				} catch (Exception e) 
    				{
    					
    				}
    				// set a Parameter
    				if(Settings.setParameter(getActivity().getApplicationContext(), Parameter.SCHEDULED_UPDATE_FREQUENCY, String.valueOf(lUpdateFrequencyMins)) == false)
    				{
    					bSaveStatus = false;    					
    				}

    				if(bSaveStatus == true)
    				{
       					MainActivity.m_strHostAddress = strHostAddress;
     					Toast.makeText(getActivity().getApplicationContext(), R.string.labelButtonDoctorSettingSaveOk, Toast.LENGTH_LONG).show();
    				}
    				else
    				{
      					Toast.makeText(getActivity().getApplicationContext(), R.string.labelButtonDoctorSettingSaveError, Toast.LENGTH_LONG).show();
    				}

    				UpdateFragment();
    			}
       		}); 
    		
            // Set an OnClickListener
    		m_bButtonDoctorSettingStartUpdate.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{
    	      		String strUpdateFrequency = Settings.getParameter(getActivity().getApplicationContext(), Parameter.SCHEDULED_UPDATE_FREQUENCY);
    	      		long lUpdateFrequencyMins = 0;
    	      		
    	      		try
    	      		{
    	      			lUpdateFrequencyMins = Long.parseLong(strUpdateFrequency);
    	      		} catch (Exception ex)
    	      		{

    	      		}
         			
    	      		StartUpdate(getActivity().getBaseContext(), lUpdateFrequencyMins);
          			
          			UpdateFragment();
    			}
       		});    
            
            // Set an OnClickListener
    		m_bButtonDoctorSettingStopUpdate.setOnClickListener(new View.OnClickListener() 
    		{
    			@Override
				public void onClick(View v) 
    			{

   	      			StopUpdate(getActivity().getBaseContext());
   	      		 
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
        	
      		String strUpdateFrequency = Settings.getParameter(getActivity().getApplicationContext(), Parameter.SCHEDULED_UPDATE_FREQUENCY);
	  		m_etEditTextDoctorSettingUpdateFrequency.setText(strUpdateFrequency);  		
	  		if(m_bUpdateStatus == false)
	  		{
	  			m_tvTextViewDoctorSettingUpdateIsRunning.setText(getString(R.string.labelTextViewDoctorSettingUpdateStatus) + getString(R.string.labelTextViewDoctorSettingUpdateStop));  	
	  		}
	  		else
	  		{
	  			m_tvTextViewDoctorSettingUpdateIsRunning.setText(getString(R.string.labelTextViewDoctorSettingUpdateStatus) + getString(R.string.labelTextViewDoctorSettingUpdateRunning));  	
	  		}   
        }          
    }
   
	public static Intent makeDoctorSettingActivity(Context context, long lID) 
	{
		Intent intent = new Intent();
		intent.setClass(context, DoctorSettingActivity.class);
		intent.putExtra(DoctorSettingActivity.ID, lID);	
		return intent;
	}	
 }
