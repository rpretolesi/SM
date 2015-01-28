package com.capstone.sm.patient.checkin;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TimePicker;


public class Question_Time_DialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener  
{
	private Calendar m_calendar;
	private int m_iHour, m_iMinute;
    boolean m_Fired = false;

    public static Question_Time_DialogFragment newInstance(String strTitleParam) {
    	Question_Time_DialogFragment frag = new Question_Time_DialogFragment();
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
    	
    	m_iHour = m_calendar.get(Calendar.HOUR_OF_DAY);
    	m_iMinute = m_calendar.get(Calendar.MINUTE);

		// Create a new instance of DatePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, m_iHour, m_iMinute, false);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
	{
		// TODO Auto-generated method stub
		m_iHour = hourOfDay;
		m_iMinute = minute;
     	
    	m_calendar.set(Calendar.HOUR_OF_DAY, m_iHour);
    	m_calendar.set(Calendar.MINUTE, m_iMinute);
    	
        if (m_Fired == true) {
            return;
        } else {
            //first time mFired
        	m_Fired = true;
        }

     ((ReminderActivity)getActivity()).doOnClick_Question_Time(m_calendar);
	}

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ((ReminderActivity)getActivity()).doOnClick_Back(dialog);
    }
}
