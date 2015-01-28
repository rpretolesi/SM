package com.capstone.sm.patient.checkin;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;


public class Question_Date_DialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
	private Calendar m_calendar;
	private int m_iYear, m_iMonth, m_iDay;
    boolean m_Fired = false;

    public static Question_Date_DialogFragment newInstance(String strTitleParam) {
    	Question_Date_DialogFragment frag = new Question_Date_DialogFragment();
        Bundle args = new Bundle();
        args.putString("TitleParam", strTitleParam);
        frag.setArguments(args);
        return frag;
    } 
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
		// Use the current date as the default date in the picker
    	m_calendar = Calendar.getInstance();
    	
    	m_iYear = m_calendar.get(Calendar.YEAR);
    	m_iMonth = m_calendar.get(Calendar.MONTH);
    	m_iDay = m_calendar.get(Calendar.DAY_OF_MONTH);

    	// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, m_iYear, m_iMonth, m_iDay);
	}


	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
		m_iYear = year;
		m_iMonth = monthOfYear;
		m_iDay = dayOfMonth;
		
    	m_calendar.set(Calendar.YEAR, m_iYear);
    	m_calendar.set(Calendar.MONTH, m_iMonth);
    	m_calendar.set(Calendar.MONTH, m_iDay);

        if (m_Fired == true) {
            return;
        } else {
            //first time mFired
        	m_Fired = true;
        }

        ((ReminderActivity)getActivity()).doOnClick_Question_Date(m_calendar);
		
	}
	
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ((ReminderActivity)getActivity()).doOnClick_Back(dialog);
    }
}
