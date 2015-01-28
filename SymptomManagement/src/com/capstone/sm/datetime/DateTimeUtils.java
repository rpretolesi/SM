package com.capstone.sm.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;


public class DateTimeUtils 
{
    private DateTimeUtils()
    {     	
    }	
    
    private static class LazyHolder 
    {
        private static final DateTimeUtils INSTANCE = new DateTimeUtils();
    }
    
    public static DateTimeUtils getInstance() 
    {
        return LazyHolder.INSTANCE;
    }

    public String FormatDateTime(Context context, Calendar calendar)
    {
    	String strDate, strTime;

        java.text.DateFormat df = android.text.format.DateFormat.getDateFormat(context);
           		
        java.text.DateFormat tf = android.text.format.DateFormat.getTimeFormat(context);
        strDate = df.format(calendar.getTime());
        strTime = tf.format(calendar.getTime());

       return strDate + " " + strTime;

    }

    public String FormatDateTimeToDB(Context context, Calendar calendar)
    {
  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return dateFormat.format(calendar.getTime());
    }
    
	public String FormatDateTimeToDB(Context context, String strDate, String strTime)
    {
        Date dateDate = null;
        Date dateTime = null;

        if(strDate != null)
        {
           java.text.DateFormat df = android.text.format.DateFormat.getDateFormat(context);        	
           try {
        	   dateDate = df.parse(strDate + " " + strTime);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    		} 
        }
        
        if(strTime != null)
        {
           java.text.DateFormat tf = android.text.format.DateFormat.getTimeFormat(context);
           try {
        	   dateTime = tf.parse(strTime);
    		} catch (ParseException e2) {
    			// TODO Auto-generated catch block
    		} 
        }

        if(dateDate != null && dateTime != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String strDateTemp = null;
            try {
            	strDateTemp = dateFormat.format(dateDate);;
     		} catch (Exception e3) {
     			// TODO Auto-generated catch block
     		} 
            String strTimeTemp = null;
            try {
            	strTimeTemp = timeFormat.format(dateTime);;
     		} catch (Exception e3) {
     			// TODO Auto-generated catch block
     		} 

            
            return strDateTemp + " " + strTimeTemp;
        }
        
        return "--- ---"; 
    }

    public String FormatDateTimeFromDBToString(Context context, String strDateTime)
    {
    	String strDate = "---";
    	String strTime = "---";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // parse the date string into Date object 
        if(strDateTime != null)
        {
           Date dateDate = null;
           try {
            	dateDate = dateFormat.parse(strDateTime);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    		} 

            java.text.DateFormat df = android.text.format.DateFormat.getDateFormat(context);
            java.text.DateFormat tf = android.text.format.DateFormat.getTimeFormat(context);
               		        
            if(dateDate != null)
            {
                strDate = df.format(dateDate);
                strTime = tf.format(dateDate);
            }
        }

       return strDate + " " + strTime;

    }
    
    public String FormatDateFromDBToString(Context context, String strDateTime)
    {
    	String strDate = "---";
 
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // parse the date string into Date object 
        if(strDateTime != null)
        {
           Date dateDate = null;
           try {
            	dateDate = dateFormat.parse(strDateTime);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    		} 

            java.text.DateFormat df = android.text.format.DateFormat.getDateFormat(context);
               		        
            if(dateDate != null)
            {
                strDate = df.format(dateDate);
            }
        }

       return strDate;

    }
    
    public Calendar FormatDateTimeFromDBToCalendar(Context context, String strDateTime)
    {
    	Calendar calendar = Calendar.getInstance();
    	 
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // parse the date string into Date object 
        if(strDateTime != null)
        {
           Date dateDate = null;
           try {
            	dateDate = dateFormat.parse(strDateTime);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    		} 

//            java.text.DateFormat df = android.text.format.DateFormat.getDateFormat(context);
//            java.text.DateFormat tf = android.text.format.DateFormat.getTimeFormat(context);
               		        
            if(dateDate != null)
            {
                calendar.setTime(dateDate);
            }
        }

        return calendar;
        
    }

    public String FormatDate(Context context, Calendar calendar)
    {
    	String strDate;

        java.text.DateFormat df = android.text.format.DateFormat.getDateFormat(context);          		
        strDate = df.format(calendar.getTime());
 
       return strDate;

    }
    
    public String FormatTime(Context context, Calendar calendar)
    {
    	String strTime;

        java.text.DateFormat tf = android.text.format.DateFormat.getTimeFormat(context);
        strTime = tf.format(calendar.getTime());

       return strTime;

    }


}
