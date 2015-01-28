package com.capstone.SQL;

import java.util.ArrayList;
import java.util.Collection;

import com.capstone.sm.R;
import com.capstone.sm.cancertherapy.CancerTherapy;
import com.capstone.sm.doctor.Doctor;
import com.capstone.sm.paintherapy.PainTherapy;
import com.capstone.sm.patient.Patient;
import com.capstone.sm.patientstatus.PatientStatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

// contract Class for the use of the SQL Database.
// As suggested from Google Documentation, i use a inner clas for each table
public class SQLContract 
{
	public static final String DATABASE_NAME = "SymptomManagement.db";	
    public static final int DATABASE_VERSION = 1;
    public static final String TEXT_TYPE = " TEXT";
	public static final String INT_TYPE = " INT";
	public static final String IMAGE_TYPE = " BLOB";
	public static final String COMMA_SEP = ",";
	
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
	public SQLContract() 
	{
	}
	
	public enum User 
	{
		PATIENT(0), DOCTOR(1);
		private int value;
 
		private User(int value) 
		{
			this.value = value;
		}
		
	    public int getValue() 
	    {
	        return value;
	    }	
	}
	
	public enum Parameter 
	{
		SCHEDULED_REMINDER_FREQUENCY(0),
		SCHEDULED_UPDATE_FREQUENCY(1),
		HOST_ADDRESS(2);

		private int value;
 
		private Parameter(int value) 
		{
			this.value = value;
		}
		
	    public int getValue() 
	    {
	        return value;
	    }	
	}

	// In a real implementation you should to create a Table on the DB where to store these reference
	// in order to add Dynamically others Questions...
	// QUESTION_10 is as Reference for Display Date and Time of the Question
	public enum Question 
	{
		QUESTION_1(1, R.string.labelQuestion_1),
		QUESTION_2(2, R.string.labelQuestion_2),
		QUESTION_3(3, R.string.labelQuestion_3),
		QUESTION_10(10, R.string.labelQuestion_10),
		QUESTION_20(20, R.string.labelQuestion_20);

		private int ID;
		private int value;
 
		private Question(int ID, int value) 
		{
			this.ID = ID;
			this.value = value;
		}
		
	    public int getID() 
	    {
	        return ID;
	    }	

	    public int getValue() 
	    {
	        return value;
	    }	
	}
	
	public enum Answer_1 
	{
		ANSWER_1(1, R.string.labelAnswer_1_1),
		ANSWER_2(2, R.string.labelAnswer_1_2),
		ANSWER_3(3, R.string.labelAnswer_1_3);

		private int ID;
		private int value;
 
		private Answer_1(int ID, int value) 
		{
			this.ID = ID;
			this.value = value;
		}
		
	    public int getID() 
	    {
	        return ID;
	    }	

	    public int getValue() 
	    {
	        return value;
	    }	
	}
	
	public enum Answer_2 
	{
		ANSWER_1(1, R.string.labelAnswer_2_1),
		ANSWER_2(2, R.string.labelAnswer_2_2);

		private int ID;
		private int value;
 
		private Answer_2(int ID, int value) 
		{
			this.ID = ID;
			this.value = value;
		}
		
	    public int getID() 
	    {
	        return ID;
	    }	

	    public int getValue() 
	    {
	        return value;
	    }	
	}
	
	public enum Answer_3 
	{
		ANSWER_1(1, R.string.labelAnswer_3_1),
		ANSWER_2(2, R.string.labelAnswer_3_2);

		private int ID;
		private int value;
 
		private Answer_3(int ID, int value) 
		{
			this.ID = ID;
			this.value = value;
		}
		
	    public int getID() 
	    {
	        return ID;
	    }	

	    public int getValue() 
	    {
	        return value;
	    }	
	}

	public enum Answer_No_Yes 
	{
		ANSWER_NO(1, R.string.labelAnswer_No),
		ANSWER_YES(2, R.string.labelAnswer_Yes);

		private int ID;
		private int value;
 
		private Answer_No_Yes(int ID, int value) 
		{
			this.ID = ID;
			this.value = value;
		}
		
	    public int getID() 
	    {
	        return ID;
	    }	

	    public int getValue() 
	    {
	        return value;
	    }	
	}
	
	/* 
	 * This Table define the Settings of the application
	 * Each Row it's a different Parameter, with a Different "Parameter_ID".
	 * Although, in this case, the primary Key could be the "Parameter_ID", i use howether
	 * the Standard Autoincremental "_ID", for semplicity
	*/
    public static abstract class Settings implements BaseColumns 
    {
      	public static final String TABLE_NAME = "Settings";
        public static final String COLUMN_NAME_PARAMETER_ID = "Parameter_ID";
        public static final String COLUMN_NAME_PARAMETER_VALUE = "Parameter_Value";
  
    	public static final String SQL_CREATE_ENTRIES =
    			"CREATE TABLE " + TABLE_NAME + 
    			" (" +
    			_ID + " INTEGER PRIMARY KEY," +
    			COLUMN_NAME_PARAMETER_ID + INT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_PARAMETER_VALUE + TEXT_TYPE +
    			" )";

    	public static final String SQL_DELETE_ENTRIES =
    		    "DROP TABLE IF EXISTS " + TABLE_NAME;
    	
    	synchronized public static boolean setParameter(Context context, Parameter pType, String strpValue )
        {
            if(context != null && pType != null && strpValue != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_PARAMETER_ID, pType.getValue());      
                values.put(COLUMN_NAME_PARAMETER_VALUE, strpValue);      
                
                String selection = COLUMN_NAME_PARAMETER_ID + " = ?";
               	
            	String[] selectionArgs = { String.valueOf(pType.getValue())  };
    			
    			// Update the Parameter
    			if (db.update(TABLE_NAME, values, selection, selectionArgs) == 0)
    			{
    				
    				// The Parameter doesn't exist, i will add it
    				if(db.insert(TABLE_NAME, null, values) > 0)
    				{
    					return true;
    				}
    			}
    			else
    			{
    				return true;				
    			}
            }
			return false;
        }   
    	
        synchronized public static String getParameter(Context context, Parameter pType)
        {
            if(context != null && pType != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
             	// you will actually use after this query.
             	String[] projection = 
             		{
             			COLUMN_NAME_PARAMETER_VALUE
             		};
                
                String selection = COLUMN_NAME_PARAMETER_ID + " = ?";
               	
            	String[] selectionArgs = { String.valueOf(pType.getValue())  };

             	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";
            	
            	Cursor cursor = db.query(
                		TABLE_NAME,  // The table to query
                	    projection,                               // The columns to return
                	    selection,                                // The columns for the WHERE clause
                	    selectionArgs,                            // The values for the WHERE clause
                	    null,                                     // don't group the rows
                	    null,                                     // don't filter by row groups
                	    sortOrder                                 // The sort order
                	    );
            	
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		return cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PARAMETER_VALUE));
            	}
            	
            }
            
			return "";
        }           
    }
    
	/* Inner class that defines the table contents */
    public static abstract class PatientEntry implements BaseColumns 
    {
      	public static final String TABLE_NAME = "Patient";
        public static final String COLUMN_NAME_REMOTE_ID = "Patient_Rem_ID";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_DATE_OF_BIRTH = "date_of_birth";
        public static final String COLUMN_NAME_MEDICAL_RECORD_ID = "medical_record_id";
        public static final String COLUMN_NAME_USER_NAME = "user_name";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DOCTOR_ID = "doctor_ID";
  
    	public static final String SQL_CREATE_ENTRIES =
    			"CREATE TABLE " + TABLE_NAME + 
    			" (" +
    			_ID + " INTEGER PRIMARY KEY," +
    			COLUMN_NAME_REMOTE_ID + INT_TYPE +  COMMA_SEP +
    	   		COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_DATE_OF_BIRTH + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_MEDICAL_RECORD_ID + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP +
    			COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
    			COLUMN_NAME_IMAGE + IMAGE_TYPE + COMMA_SEP +
    			COLUMN_NAME_DOCTOR_ID + INT_TYPE +
    			" )";

    	public static final String SQL_DELETE_ENTRIES =
    		    "DROP TABLE IF EXISTS " + TABLE_NAME;
    	
    	public static long getNextID(Context context)
    	{
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                // Define a projection that specifies which columns from the database
             	// you will actually use after this query.
             	String[] projection = {COLUMN_NAME_REMOTE_ID};
             	
            	// How you want the results sorted in the resulting Cursor
               	String sortOrder = COLUMN_NAME_REMOTE_ID + " DESC";

            	Cursor cursor = db.query(
                		TABLE_NAME,  // The table to query
                	    projection,                               // The columns to return
                	    null,                                	  // The columns for the WHERE clause
                	    null,                            		  // The values for the WHERE clause
                	    null,                                     // don't group the rows
                	    null,                                     // don't filter by row groups
                	    sortOrder                                 // The sort order
                	    );
            	
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		long lID = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
            		lID = lID + 1;
            		return lID;
            	}
                return 1;
            }
            return 0;
    	}
    	
        synchronized public static boolean addPatient(Context context, Patient pm)
        {
            if(context != null && pm != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_REMOTE_ID, pm.getId());
				values.put(COLUMN_NAME_FIRST_NAME, pm.getFirstname());
				values.put(COLUMN_NAME_LAST_NAME, pm.getLastname());
				values.put(COLUMN_NAME_DATE_OF_BIRTH, pm.getDateofbirth());
				values.put(COLUMN_NAME_MEDICAL_RECORD_ID, pm.getMedicalrecordid());
				values.put(COLUMN_NAME_USER_NAME, pm.getUsername());
				values.put(COLUMN_NAME_PASSWORD, pm.getPassword());
				values.put(COLUMN_NAME_IMAGE, pm.getImage());
				values.put(COLUMN_NAME_DOCTOR_ID, pm.getDoctorid());
				
				// Insert the new row, returning the primary key value of the new row
				if(db.insert(TABLE_NAME, null, values) > 0)
				{
					return true;
				}
            }
			
			return false;
        }   

        synchronized public static Patient getPatient(Context context, long lPatientID)
        {
            Patient pm = new Patient();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                // Define a projection that specifies which columns from the database
             	// you will actually use after this query.
             	String[] projection = 
             		{
             			COLUMN_NAME_REMOTE_ID, 
             			COLUMN_NAME_FIRST_NAME, 
             			COLUMN_NAME_LAST_NAME, 
             			COLUMN_NAME_DATE_OF_BIRTH, 
             			COLUMN_NAME_MEDICAL_RECORD_ID, 
             			COLUMN_NAME_USER_NAME, 
             			COLUMN_NAME_PASSWORD, 
             			COLUMN_NAME_IMAGE, 
             			COLUMN_NAME_DOCTOR_ID
             		};
             	
            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID) };
            	
            	Cursor cursor = db.query(
                		TABLE_NAME,  // The table to query
                	    projection,                               // The columns to return
                	    selection,                                // The columns for the WHERE clause
                	    selectionArgs,                            // The values for the WHERE clause
                	    null,                                     // don't group the rows
                	    null,                                     // don't filter by row groups
                	    sortOrder                                 // The sort order
                	    );
            	
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		pm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
            		pm.setFirstname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME)));
            		pm.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME)));
            		pm.setDateofbirth(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_OF_BIRTH)));
            		pm.setMedicalrecordid(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MEDICAL_RECORD_ID)));
            		pm.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER_NAME)));
            		pm.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PASSWORD)));
            		pm.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_IMAGE)));
            		pm.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
          		
            	}            	
            }
        	
            return pm;
        }
        
        synchronized public static boolean IsPatientIDPresent(Context context, long lPatientID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lPatientID) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c == null) || (c.getCount() == 0))
            	{
            		return false;
            	}
            }
        	return true;        	
        }        
        
        synchronized public static boolean IsPatientUserNamePresent(Context context, String strUserName)
        {
            if(context != null && strUserName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_USER_NAME + " = ?";

            	String[] selectionArgs = { String.valueOf(strUserName) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
    		return false;
        }        
        
        synchronized public static long getPatientID(Context context, String strUserName, String strPassword)
        {
            if(context != null && strUserName != null && strPassword != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
            	
            	// Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_USER_NAME + " = ? AND " +
            			COLUMN_NAME_PASSWORD + " = ?" ;

            	String[] selectionArgs = { String.valueOf(strUserName), String.valueOf(strPassword) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		
               		return cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
            	}
            }
        	return 0;
        } 
        
        synchronized public static boolean setPatientImage(Context context, long lPatientID, byte[] image)
        {
            if(context != null && image != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                String selection = 
                		COLUMN_NAME_REMOTE_ID + " = ?";
               	
            	String[] selectionArgs = { String.valueOf(lPatientID)  };
                   	
            	
    			ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_IMAGE, image);

    			// Update the User Doctor
    			if (db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
    			{
    		       	return true;   				
    			}
            }
    		return false;
        }       
        
        synchronized public static boolean setPatientDoctorID(Context context, long lPatientID, long lDoctorID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                String selection = 
                		COLUMN_NAME_REMOTE_ID + " = ?";
               	
            	String[] selectionArgs = { String.valueOf(lPatientID)  };
                   	
            	
    			ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_DOCTOR_ID, lDoctorID);
    			
    			// Update the User Doctor
    			if (db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
    			{
    		       	return true;   				
    			}
            }
    		return false;
        }       
        
        synchronized public static long getPatientDoctorID(Context context, long lPatientID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_DOCTOR_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lPatientID) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		
            		return cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID));
            	}
            }
        	return 0;
        }      
        
        synchronized public static ArrayList<Patient> getPatientByDoctorID(Context context, long lDoctorID)
        {
            ArrayList<Patient> plm = new ArrayList<Patient>();

            if(context != null)
            {
	            SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
		
                Patient pm = null;
           	 
	            // Define a projection that specifies which columns from the database
	        	// you will actually use after this query.
	        	String[] projection = {COLUMN_NAME_REMOTE_ID};
	
	        	// How you want the results sorted in the resulting Cursor
	        	String sortOrder = "";
	
	        	// Which row to get based on WHERE
	        	String selection = COLUMN_NAME_DOCTOR_ID + " = ?" ;
	
	        	String[] selectionArgs = { String.valueOf(lDoctorID) };
	        	
	        	Cursor cursor = db.query(
	        		TABLE_NAME,  // The table to query
	        	    projection,                               // The columns to return
	        	    selection,                                // The columns for the WHERE clause
	        	    selectionArgs,                            // The values for the WHERE clause
	        	    null,                                     // don't group the rows
	        	    null,                                     // don't filter by row groups
	        	    sortOrder                                 // The sort order
	        	    );
	        	if((cursor != null) && (cursor.getCount() > 0))
	        	{
	    			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
	    			{
	    				long lID = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
	    				pm = getPatient(context, lID);
	    				if (pm != null)
	    				{
		    				plm.add(pm);
	    				}
	    			}        		
	        	}
        	}

        	return plm;
        }    
        
        synchronized public static String getPatientName(Context context, long lPatientID)
        {
       		String strName = "";
        	
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID, PatientEntry.COLUMN_NAME_FIRST_NAME, DoctorEntry.COLUMN_NAME_LAST_NAME};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lPatientID) };
           	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();

            		strName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME)) + " " +cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME));
            	}
            	
            }
        	return strName;
        }      
        
    	synchronized public static ArrayList<Patient> getPatientListByDoctorIDAndFirstNameAndLastName(Context context, long lDoctorID, String strName)
        {
            ArrayList<Patient> alpm = new ArrayList<Patient>();

            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                Patient pm = null;
                		
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
             	String[] projection = 
             		{
             			COLUMN_NAME_REMOTE_ID, 
             			COLUMN_NAME_FIRST_NAME, 
             			COLUMN_NAME_LAST_NAME, 
             			COLUMN_NAME_DATE_OF_BIRTH, 
             			COLUMN_NAME_MEDICAL_RECORD_ID, 
             			COLUMN_NAME_USER_NAME, 
             			COLUMN_NAME_PASSWORD, 
             			COLUMN_NAME_IMAGE, 
             			COLUMN_NAME_DOCTOR_ID
             		};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";
            	
            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_DOCTOR_ID + " = ?" + " AND ( " + COLUMN_NAME_FIRST_NAME + " like ? OR " + COLUMN_NAME_LAST_NAME + " like ? ) ";

            	String[] selectionArgs = { String.valueOf(lDoctorID), String.valueOf("%" + strName + "%"), String.valueOf("%" + strName + "%") };

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                             		  // The columns for the WHERE clause
            	    selectionArgs,                            		  // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        			{
                        pm = new Patient();
                		pm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
                		pm.setFirstname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME)));
                		pm.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME)));
                		pm.setDateofbirth(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_OF_BIRTH)));
                		pm.setMedicalrecordid(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MEDICAL_RECORD_ID)));
                		pm.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER_NAME)));
                		pm.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PASSWORD)));
                		pm.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_IMAGE)));
                		pm.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
        				alpm.add(pm);
        			}        		
            	}
            }

        	return alpm;
        }         
    }	

    public static abstract class DoctorEntry implements BaseColumns 
    {
      	public static final String TABLE_NAME = "Doctor";
        public static final String COLUMN_NAME_REMOTE_ID = "Doctor_Rem_ID";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_IDENTIFICATION_ID = "identification_id";
        public static final String COLUMN_NAME_USER_NAME = "user_name";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_IMAGE = "image";
  
    	public static final String SQL_CREATE_ENTRIES =
    			"CREATE TABLE " + TABLE_NAME + 
    			" (" +
    			_ID + " INTEGER PRIMARY KEY," +
    			COLUMN_NAME_REMOTE_ID + INT_TYPE +  COMMA_SEP +
    			COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_IDENTIFICATION_ID + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP +
    			COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
    			COLUMN_NAME_IMAGE + IMAGE_TYPE +
    			" )";

    	public static final String SQL_DELETE_ENTRIES =
    		    "DROP TABLE IF EXISTS " + TABLE_NAME;
    	
    	synchronized public static long getNextID(Context context)
    	{
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                // Define a projection that specifies which columns from the database
             	// you will actually use after this query.
             	String[] projection = {COLUMN_NAME_REMOTE_ID};
             	
            	// How you want the results sorted in the resulting Cursor
               	String sortOrder = COLUMN_NAME_REMOTE_ID + " DESC";

            	Cursor cursor = db.query(
                		TABLE_NAME,  // The table to query
                	    projection,                               // The columns to return
                	    null,                                	  // The columns for the WHERE clause
                	    null,                            		  // The values for the WHERE clause
                	    null,                                     // don't group the rows
                	    null,                                     // don't filter by row groups
                	    sortOrder                                 // The sort order
                	    );
            	
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		long lID = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
            		lID = lID + 1;
            		return lID;
            	}
                return 1;
            }
            return 0;
    	}
    	
    	synchronized public static boolean addDoctor(Context context, Doctor dm)
        {
            if(context != null && dm != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_REMOTE_ID, dm.getId());
				values.put(COLUMN_NAME_FIRST_NAME, dm.getFirstname());
				values.put(COLUMN_NAME_LAST_NAME, dm.getLastname());
				values.put(COLUMN_NAME_IDENTIFICATION_ID, dm.getIdentificationid());
				values.put(COLUMN_NAME_USER_NAME, dm.getUsername());
				values.put(COLUMN_NAME_PASSWORD, dm.getPassword());
				values.put(COLUMN_NAME_IMAGE, dm.getImage());
				
				// Insert the new row, returning the primary key value of the new row
				if(db.insert(TABLE_NAME, null, values) > 0)
				{
					return true;
				}
            }
			
			return false;
        }   
        
    	synchronized public static Doctor getDoctor(Context context, long lDoctorID)
        {
            Doctor dm = new Doctor();
    		
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                // Define a projection that specifies which columns from the database
             	// you will actually use after this query.
             	String[] projection = 
             		{
             			COLUMN_NAME_REMOTE_ID, 
             			COLUMN_NAME_FIRST_NAME, 
             			COLUMN_NAME_LAST_NAME, 
             			COLUMN_NAME_IDENTIFICATION_ID, 
             			COLUMN_NAME_USER_NAME, 
             			COLUMN_NAME_PASSWORD, 
             			COLUMN_NAME_IMAGE, 
             		};
             	
            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lDoctorID) };
            	
            	Cursor cursor = db.query(
            			TABLE_NAME,  // The table to query
                	    projection,                               // The columns to return
                	    selection,                                // The columns for the WHERE clause
                	    selectionArgs,                            // The values for the WHERE clause
                	    null,                                     // don't group the rows
                	    null,                                     // don't filter by row groups
                	    sortOrder                                 // The sort order
                	    );
            	
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		dm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
            		dm.setFirstname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME)));
            		dm.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME)));
            		dm.setIdentificationid(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDENTIFICATION_ID)));
            		dm.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER_NAME)));
            		dm.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PASSWORD)));
            		dm.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_IMAGE)));
              		
             	}
            	
            }
            
            return dm;
        }
        
    	synchronized public static boolean IsDoctorUserNamePresent(Context context, String strUserName)
        {
            if(context != null && strUserName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_USER_NAME + " = ?";

            	String[] selectionArgs = { String.valueOf(strUserName) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
    		return false;
        }        
        
    	synchronized public static boolean IsDoctorIDPresent(Context context, long lDoctorID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lDoctorID) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}            	
            }
    		return false;
        }        
        
    	synchronized public static long getDoctorID(Context context, String strUserName, String strPassword)
        {
            if(context != null && strUserName != null && strPassword != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
            	
            	// Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_USER_NAME + " = ? AND " +
            			COLUMN_NAME_PASSWORD + " = ?" ;

            	String[] selectionArgs = { String.valueOf(strUserName), String.valueOf(strPassword) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		
               		return cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
            	}            
            }
        	return 0;
        }  
    	
        synchronized public static boolean setDoctorImage(Context context, long lDoctorID, byte[] image)
        {
            if(context != null && image != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                String selection = 
                		COLUMN_NAME_REMOTE_ID + " = ?";
               	
            	String[] selectionArgs = { String.valueOf(lDoctorID)  };
                   	
            	
    			ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_IMAGE, image);

    			// Update the User Doctor
    			if (db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
    			{
    		       	return true;   				
    			}
            }
    		return false;
        }           	
               
    	synchronized public static String getDoctorName(Context context, long lDoctorID)
        {
        	String strName = "";

        	if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
        		
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID, COLUMN_NAME_FIRST_NAME, COLUMN_NAME_LAST_NAME};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lDoctorID) };
           	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		strName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME)) + " " +cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME));
               	        	
            	}
            }

        	return strName;
        }   
        
    	synchronized public static ArrayList<Doctor> getDoctorList(Context context)
        {
            ArrayList<Doctor> aldm = new ArrayList<Doctor>();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                Doctor dm = null; 
                
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    null,                             		  // The columns for the WHERE clause
            	    null,                            		  // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        			{
        				long lID = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
        				dm = getDoctor(context, lID);
        				if(dm != null)
        				{
        					aldm.add(dm);
        				}
        			}        		
            	}
            }
        	return aldm;
        }            

    	synchronized public static ArrayList<Doctor> getDoctorListByFirstNameAndLastName(Context context, String strName)
        {
            ArrayList<Doctor> aldm = new ArrayList<Doctor>();

            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                Doctor dm = null; 
                		
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
             	String[] projection = 
             		{
             			COLUMN_NAME_REMOTE_ID, 
             			COLUMN_NAME_FIRST_NAME, 
             			COLUMN_NAME_LAST_NAME, 
             			COLUMN_NAME_IDENTIFICATION_ID, 
             			COLUMN_NAME_USER_NAME, 
             			COLUMN_NAME_PASSWORD, 
             			COLUMN_NAME_IMAGE, 
             		};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";
            	
            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_FIRST_NAME + " like ? OR " + COLUMN_NAME_LAST_NAME + " like ? ";

            	String[] selectionArgs = { String.valueOf("%" + strName + "%"), String.valueOf("%" + strName + "%") };

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                             		  // The columns for the WHERE clause
            	    selectionArgs,                            		  // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        			{
                        dm = new Doctor();
                		dm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
                		dm.setFirstname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME)));
                		dm.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME)));
                		dm.setIdentificationid(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDENTIFICATION_ID)));
                		dm.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER_NAME)));
                		dm.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PASSWORD)));
                		dm.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_IMAGE)));
        				aldm.add(dm);
        			}        		
            	}
            }
        	return aldm;
        }            
    }	

    
    public static abstract class CancerTherapyEntry implements BaseColumns 
    {
        public static final String TABLE_NAME = "CancerTherapy";
        public static final String COLUMN_NAME_REMOTE_ID = "CancerTherapy_Rem_ID";
        public static final String COLUMN_NAME_PATIENT_ID = "patient_ID";
        public static final String COLUMN_NAME_DOCTOR_ID = "doctor_ID";
        public static final String COLUMN_NAME_THERAPY = "therapy_name";
        public static final String COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET = "date_time_of_treatment_set";
        public static final String COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC = "date_time_of_treatment_exec";

       	public static final String SQL_CREATE_ENTRIES =
    			"CREATE TABLE " + TABLE_NAME + 
    			" (" +
    	   		_ID + " INTEGER PRIMARY KEY," +
    			COLUMN_NAME_REMOTE_ID + INT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_PATIENT_ID + INT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_DOCTOR_ID + INT_TYPE + COMMA_SEP +
    			COLUMN_NAME_THERAPY + TEXT_TYPE + COMMA_SEP +
    			COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET + TEXT_TYPE + COMMA_SEP +
    			COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC + TEXT_TYPE +
    			" )";

    	public static final String SQL_DELETE_ENTRIES =
    		    "DROP TABLE IF EXISTS " + TABLE_NAME;
    	
    	public static long getNextID(Context context)
    	{
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                // Define a projection that specifies which columns from the database
             	// you will actually use after this query.
             	String[] projection = {COLUMN_NAME_REMOTE_ID};
             	
            	// How you want the results sorted in the resulting Cursor
               	String sortOrder = COLUMN_NAME_REMOTE_ID + " DESC";

            	Cursor cursor = db.query(
                		TABLE_NAME,  // The table to query
                	    projection,                               // The columns to return
                	    null,                                	  // The columns for the WHERE clause
                	    null,                            		  // The values for the WHERE clause
                	    null,                                     // don't group the rows
                	    null,                                     // don't filter by row groups
                	    sortOrder                                 // The sort order
                	    );
            	
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		long lID = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
            		lID = lID + 1;
            		return lID;
            	}
                return 1;
            }
            return 0;
    	}
    	
    	synchronized public static boolean IsCancerTherapyIDPresent(Context context, long lID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lID) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
        	return false;
        }       
    	
    	synchronized public static boolean IsCancerTherapyIDExecuted(Context context, long lID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

             	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_REMOTE_ID + " = ? AND " +
            			COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC + " <> ?" ;

            	String[] selectionArgs = { String.valueOf(lID),  String.valueOf("")};
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
    		return false;
        }     
    	   	
    	synchronized public static boolean IsCancerTherapyNamePresent(Context context, long lPatientID, long lDoctorID, String strName)
        {
            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
               	String selection = 
               			COLUMN_NAME_PATIENT_ID + " = ? AND " +
               			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
              			COLUMN_NAME_THERAPY + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID), String.valueOf(strName) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
    		return false;
        }                
        
       	synchronized public static boolean addCancerTherapy(Context context, CancerTherapy ctm)
        {
            if(context != null && ctm != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_REMOTE_ID, ctm.getId());
    			values.put(COLUMN_NAME_PATIENT_ID, ctm.getPatientid());
    			values.put(COLUMN_NAME_DOCTOR_ID, ctm.getDoctorid());
    			values.put(COLUMN_NAME_THERAPY, ctm.getName());
       			values.put(COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET, ctm.getDatetimeset());
       			values.put(COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC, ctm.getDatetimeexec());
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.insert(TABLE_NAME, null, values) > 0)
    			{
    				return true;
    			}
            }

            return false;
        }
       	
       	synchronized public static boolean deleteCancerTherapy(Context context, long lPatientID, long lDoctorID, String strName)
        {
            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
            	// Which row to get based on WHERE
               	String selection = 
               			COLUMN_NAME_PATIENT_ID + " = ? AND " +
               			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
              			COLUMN_NAME_THERAPY + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID), String.valueOf(strName) };
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.delete(TABLE_NAME, selection, selectionArgs) > 0)
    			{
    				return true;
    			}
            }
			return false;
        }
       	
       	synchronized public static boolean deleteAllCancerTherapy(Context context)
        {
            if(context != null)
            {
	            SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
	
				db.delete(TABLE_NAME, null, null);
	
				return true;
            }
			return false;
        }
       	
       	synchronized public static boolean updateCancerTherapy(Context context, CancerTherapy ctm)
        {
            if(context != null && ctm != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET, ctm.getDatetimeset());
    			values.put(COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC, ctm.getDatetimeexec());
    			
    	       	// Which row to get based on WHERE
              	String selection = 
               			COLUMN_NAME_PATIENT_ID + " = ? AND " +
               			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
               			COLUMN_NAME_THERAPY + " = ?" ;
               	
            	String[] selectionArgs = { String.valueOf(ctm.getPatientid()), String.valueOf(ctm.getDoctorid()), String.valueOf(ctm.getName()) };
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
    			{
    				return true;
    			}            	
            }
			return false;
        }
 
       	synchronized public static CancerTherapy getCancerTherapyByID(Context context, long lID)
        {
            CancerTherapy ctm = new CancerTherapy();

            if(context != null)
            {
	            SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
	
	
	            // Define a projection that specifies which columns from the database
	        	// you will actually use after this query.
	        	String[] projection =
	        	{
	        		COLUMN_NAME_REMOTE_ID, 
	    			COLUMN_NAME_PATIENT_ID, 
	    			COLUMN_NAME_DOCTOR_ID, 
	    			COLUMN_NAME_THERAPY, 
	    			COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET, 
	    			COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC 
	        	};
	
	        	// How you want the results sorted in the resulting Cursor
	        	String sortOrder = "";
	
	        	// Which row to get based on WHERE
	        	String selection = COLUMN_NAME_REMOTE_ID + " = ?" ;
	
	        	String[] selectionArgs = { String.valueOf(lID) };
	        	
	        	Cursor cursor = db.query(
	        		TABLE_NAME,  // The table to query
	        	    projection,                               // The columns to return
	        	    selection,                                // The columns for the WHERE clause
	        	    selectionArgs,                            // The values for the WHERE clause
	        	    null,                                     // don't group the rows
	        	    null,                                     // don't filter by row groups
	        	    sortOrder                                 // The sort order
	        	    );
	        	if((cursor != null) && (cursor.getCount() > 0))
	        	{
	        		cursor.moveToFirst();
	
	        		ctm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
	        		ctm.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
	        		ctm.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
	        		ctm.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_THERAPY)));
	        		ctm.setDatetimeset(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET)));
	        		ctm.setDatetimeexec(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC)));
	        		
	        	}
        	}
	        	
        	return ctm;
        }
       	
       	synchronized public static CancerTherapy getCancerTherapyByPatientidAndDoctoridAndName(Context context, long lPatientID, long lDoctorID, String strName)
        {
            CancerTherapy ctm = new CancerTherapy();

            if(context != null && strName != null)
            {
	            SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
	
	
	            // Define a projection that specifies which columns from the database
	        	// you will actually use after this query.
	        	String[] projection =
	        	{
	        		COLUMN_NAME_REMOTE_ID, 
	    			COLUMN_NAME_PATIENT_ID, 
	    			COLUMN_NAME_DOCTOR_ID, 
	    			COLUMN_NAME_THERAPY, 
	    			COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET, 
	    			COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC 
	        	};
	
	        	// How you want the results sorted in the resulting Cursor
	        	String sortOrder = "";
	
	        	// Which row to get based on WHERE
	           	String selection = 
	           			COLUMN_NAME_PATIENT_ID + " = ? AND " +
	           			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
	          			COLUMN_NAME_THERAPY + " = ?" ;
	
	        	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID), String.valueOf(strName) };
	        	
	        	Cursor cursor = db.query(
	        		TABLE_NAME,  // The table to query
	        	    projection,                               // The columns to return
	        	    selection,                                // The columns for the WHERE clause
	        	    selectionArgs,                            // The values for the WHERE clause
	        	    null,                                     // don't group the rows
	        	    null,                                     // don't filter by row groups
	        	    sortOrder                                 // The sort order
	        	    );
	        	if((cursor != null) && (cursor.getCount() > 0))
	        	{
	        		cursor.moveToFirst();
	
	        		ctm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
	        		ctm.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
	        		ctm.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
	        		ctm.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_THERAPY)));
	        		ctm.setDatetimeset(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET)));
	        		ctm.setDatetimeexec(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC)));
	        		
	        	}
            }
            
        	return ctm;
        }
       	
    	synchronized public static boolean setCancerTherapyDateTimeExec(Context context, long lID, String strDateTime)
        {
            if(context != null && strDateTime != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC, strDateTime);      
                
                String selection = COLUMN_NAME_REMOTE_ID + " = ?";
               	
            	String[] selectionArgs = { String.valueOf(lID)  };
    			
    			// Update the Parameter
    			if (db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
    			{
    				return true;
    			}
            }
			return false;      
        
        }
        
    	synchronized public static long getLastCancerTherapyExecutedID(Context context, long lPatient_ID, long lDoctor_ID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = { COLUMN_NAME_REMOTE_ID };

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "datetime(\"" + COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC + "\") DESC";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_PATIENT_ID + " = ? AND " +
            			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
            			COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC + " IS NOT '' " ;

            	String[] selectionArgs = { String.valueOf(lPatient_ID), String.valueOf(lDoctor_ID) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
               		cursor.moveToFirst();
             		
               		return cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
            	}
            }
        	return 0;
        }
        
    	synchronized public static ArrayList<CancerTherapy> getListCancerTherapy(Context context, long lPatientID, long lDoctorID)
        {
            ArrayList<CancerTherapy> alctm = new ArrayList<CancerTherapy>();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                CancerTherapy ctmTemp = null;

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID, 
        			COLUMN_NAME_PATIENT_ID, 
        			COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_THERAPY, 
        			COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET, 
        			COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC 
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_PATIENT_ID + " = ? AND " +
            			COLUMN_NAME_DOCTOR_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
        			{
        				ctmTemp = new CancerTherapy();
        				ctmTemp.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
        				ctmTemp.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
        				ctmTemp.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
        				ctmTemp.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_THERAPY)));
        				ctmTemp.setDatetimeset(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_OF_TREATMENT_SET)));
        				ctmTemp.setDatetimeexec(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_OF_TREATMENT_EXEC)));
        				alctm.add(ctmTemp);
        			}        		
            	}
            }
        	return alctm;
        }     
    } 

    public static abstract class PainTherapyEntry implements BaseColumns 
    {
        public static final String TABLE_NAME = "PainTherapy";
        public static final String COLUMN_NAME_REMOTE_ID = "PainTherapy_Rem_ID";
        public static final String COLUMN_NAME_PATIENT_ID = "patient_ID";
        public static final String COLUMN_NAME_DOCTOR_ID = "doctor_ID";
        public static final String COLUMN_NAME_THERAPY = "therapy_name";

      	public static final String SQL_CREATE_ENTRIES =
    			"CREATE TABLE " + TABLE_NAME + 
    			" (" +
    	   		_ID + " INTEGER PRIMARY KEY," +
    	   		COLUMN_NAME_REMOTE_ID + INT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_PATIENT_ID + INT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_DOCTOR_ID + INT_TYPE + COMMA_SEP +
    			COLUMN_NAME_THERAPY + TEXT_TYPE +
    			" )";

    	public static final String SQL_DELETE_ENTRIES =
    		    "DROP TABLE IF EXISTS " + TABLE_NAME;
    	
    	public static long getNextID(Context context)
    	{
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                // Define a projection that specifies which columns from the database
             	// you will actually use after this query.
             	String[] projection = {COLUMN_NAME_REMOTE_ID};
             	
            	// How you want the results sorted in the resulting Cursor
               	String sortOrder = COLUMN_NAME_REMOTE_ID + " DESC";

            	Cursor cursor = db.query(
                		TABLE_NAME,  // The table to query
                	    projection,                               // The columns to return
                	    null,                                	  // The columns for the WHERE clause
                	    null,                            		  // The values for the WHERE clause
                	    null,                                     // don't group the rows
                	    null,                                     // don't filter by row groups
                	    sortOrder                                 // The sort order
                	    );
            	
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		long lID = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID));
            		lID = lID + 1;
            		return lID;
            	}
                return 1;
            }
            return 0;
    	}
    	
    	synchronized public static boolean IsPainTherapyIDPresent(Context context, long lID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lID) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
    		return false;
        } 
    	
       	synchronized public static boolean IsPainTherapyNamePresent(Context context, long lPatientID, long lDoctorID, String strName)
        {
            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
               	String selection = 
               			COLUMN_NAME_PATIENT_ID + " = ? AND " +
               			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
              			COLUMN_NAME_THERAPY + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID), String.valueOf(strName) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }

    		return false;
        }                
       	
    	synchronized public static boolean addPainTherapy(Context context, PainTherapy ptm)
        {
            if(context != null && ptm != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_REMOTE_ID, ptm.getId());
    			values.put(COLUMN_NAME_PATIENT_ID, ptm.getPatientid());
    			values.put(COLUMN_NAME_DOCTOR_ID, ptm.getDoctorid());
    			values.put(COLUMN_NAME_THERAPY, ptm.getName());
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.insert(TABLE_NAME, null, values) > 0)
    			{
    				return true;
    			}
    			
            }

			return false;
        }
    	
       	synchronized public static boolean deletePainTherapy(Context context, long lPatientID, long lDoctorID, String strName)
        {
            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
            	// Which row to get based on WHERE
               	String selection = 
               			COLUMN_NAME_PATIENT_ID + " = ? AND " +
               			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
              			COLUMN_NAME_THERAPY + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID), String.valueOf(strName) };
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.delete(TABLE_NAME, selection, selectionArgs) > 0)
    			{
    				return true;
    			}
            }
			return false;
        }

       	synchronized public static boolean deleteAllPainTherapy(Context context)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

    			db.delete(TABLE_NAME, null, null);

    			return true;
            }
			return false;
        }
       	
       	synchronized public static boolean updatePainTherapy(Context context, PainTherapy ctm)
        {
      		// ACtually there is no value to update because the name it's univoque, this is for the future...
/*      		
            SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
            
            if(ctm != null)
            {
	            ContentValues values = new ContentValues();
				values.put( , ctm.);
				
		       	// Which row to get based on WHERE
	          	String selection = 
	           			COLUMN_NAME_PATIENT_ID + " = ? AND " +
               			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
               			COLUMN_NAME_THERAPY + " = ?" ;
	           	
	        	String[] selectionArgs = { String.valueOf(ctm.getPatientid()), String.valueOf(ctm.getDoctorid()), String.valueOf(ctm.getName()) };
				
				// Insert the new row, returning the primary key value of the new row
				if(db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
				{
					return true;
				}
            }
			
			return false;
*/			
			return true;
       }
 
    	synchronized public static boolean IsPainTherapyNamePresent(Context context, long lPatientID, String strName)
        {
            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
               	String selection = 
               			COLUMN_NAME_PATIENT_ID + " = ? AND " +
              			COLUMN_NAME_THERAPY + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(strName) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
    		return false;
        }   
    	
    	synchronized public static String getPainTherapyName(Context context, long lPatient_ID, long lDoctor_ID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_THERAPY};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_PATIENT_ID + " = ? AND " +
            			COLUMN_NAME_DOCTOR_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatient_ID), String.valueOf(lDoctor_ID) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();
            		
               		return cursor.getString(cursor.getColumnIndex(COLUMN_NAME_THERAPY));
            	}
            }
        	return "";
        }    
    	
       	synchronized public static PainTherapy getPainTherapyByPatientidAndDoctoridAndName(Context context, long lPatientID, long lDoctorID, String strName)
        {
            PainTherapy ptm = new PainTherapy();

            if(context != null && strName != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID, 
        			COLUMN_NAME_PATIENT_ID, 
        			COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_THERAPY 
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
               	String selection = 
               			COLUMN_NAME_PATIENT_ID + " = ? AND " +
               			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
              			COLUMN_NAME_THERAPY + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID), String.valueOf(strName) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();

            		ptm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
            		ptm.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
            		ptm.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
            		ptm.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_THERAPY)));
            		
            	}
            }
        	return ptm;
        }
       
    	synchronized public static ArrayList<PainTherapy> getListPainTherapy(Context context, long lPatientID, long lDoctorID)
        {
            ArrayList<PainTherapy> ctlm = new ArrayList<PainTherapy>();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                PainTherapy ctlmTemp = null; 
                
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID, 
        			COLUMN_NAME_PATIENT_ID, 
        			COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_THERAPY 
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_PATIENT_ID + " = ? AND " +
            			COLUMN_NAME_DOCTOR_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
        			{
        				ctlmTemp = new PainTherapy();
        				ctlmTemp.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
        				ctlmTemp.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
        				ctlmTemp.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
        				ctlmTemp.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_THERAPY)));
        				ctlm.add(ctlmTemp);
        			}        		

            	}
            }
        	return ctlm;
        }  
    	
      	synchronized public static PainTherapy getPainTherapy(Context context, long lID)
        {
            PainTherapy ptm = new PainTherapy();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID, 
        			COLUMN_NAME_PATIENT_ID, 
        			COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_THERAPY, 
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lID) };
            	
            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
            		cursor.moveToFirst();

            		ptm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
            		ptm.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
            		ptm.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
            		ptm.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_THERAPY)));
            	}
            }
        	return ptm;
        }    	
    }
 
    /*
     * This Table rappresent the Status of Healt of the Patient. 
     *  Each Row is the answer to a Specific Question. The Date and Time are referred when the Patient Answer 
     *  Regarding the Pain Therapy, however the Date and Time when the patient got the Therapy 
     *  is stored on the Table:"PainTherapyExecEntry".
     */
    public static abstract class PatientStatusEntry implements BaseColumns 
    {
      	public static final String TABLE_NAME = "PatientStatusEntry";
        public static final String COLUMN_NAME_REMOTE_ID = "PatientStatusEntry_Rem_ID";      	
        public static final String COLUMN_NAME_PATIENT_ID = "patient_ID";
        public static final String COLUMN_NAME_DOCTOR_ID = "doctor_ID";
        public static final String COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID = "last_cancer_therapy_exec_ID";
        public static final String COLUMN_NAME_PAIN_THERAPY_ID = "pain_therapy_ID";
        public static final String COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC = "pain_therapy_date_time_exec";
        public static final String COLUMN_NAME_QUESTION_ID = "question_ID";
        public static final String COLUMN_NAME_ANSWER_ID = "answer_ID";
        public static final String COLUMN_NAME_DATE_TIME_ANSWER = "date_time_answer";
        public static final String COLUMN_NAME_CHECKED_BY_DOCTOR = "checked_by_doctor";
  
    	public static final String SQL_CREATE_ENTRIES =
    			"CREATE TABLE " + TABLE_NAME + 
    			" (" +
    			_ID + " INTEGER PRIMARY KEY," +
    			COLUMN_NAME_REMOTE_ID + INT_TYPE +  COMMA_SEP +
    			COLUMN_NAME_PATIENT_ID + INT_TYPE + COMMA_SEP +
    			COLUMN_NAME_DOCTOR_ID + INT_TYPE + COMMA_SEP +
    			COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID + INT_TYPE + COMMA_SEP +
    			COLUMN_NAME_PAIN_THERAPY_ID + INT_TYPE + COMMA_SEP +
    			COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC + TEXT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_QUESTION_ID + INT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_ANSWER_ID + INT_TYPE + COMMA_SEP +
    	   		COLUMN_NAME_DATE_TIME_ANSWER + TEXT_TYPE + COMMA_SEP +
    			COLUMN_NAME_CHECKED_BY_DOCTOR + INT_TYPE +
    			" )";

    	public static final String SQL_DELETE_ENTRIES =
    		    "DROP TABLE IF EXISTS " + TABLE_NAME;
    	
    	synchronized public static boolean addPatientStatus(Context context, PatientStatus psm)
        {
            if(context != null && psm != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_REMOTE_ID, psm.getId());
    			values.put(COLUMN_NAME_PATIENT_ID, psm.getPatientid());
    			values.put(COLUMN_NAME_DOCTOR_ID, psm.getDoctorid());
    			values.put(COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID, psm.getLastcancertherapyexecid());
    			values.put(COLUMN_NAME_PAIN_THERAPY_ID, psm.getPaintherapyid());
    			values.put(COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC, psm.getPaintherapydatetimeexec());
    			values.put(COLUMN_NAME_QUESTION_ID, psm.getQuestionid());
    			values.put(COLUMN_NAME_ANSWER_ID, psm.getAnswerid());
    			values.put(COLUMN_NAME_DATE_TIME_ANSWER, psm.getDatetimeanswer());
    			values.put(COLUMN_NAME_CHECKED_BY_DOCTOR, psm.getCheckedbydoctor());
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.insert(TABLE_NAME, null, values) > 0)
    			{
    				return true;
    			}
            }

            return false;
        }   
    	
    	synchronized public static boolean updatePatientStatusID(Context context, PatientStatus psm)
        {
            if(context != null && psm != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_REMOTE_ID, psm.getId());
    			
    	       	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_PATIENT_ID + " = ? AND " +
            			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
            			COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID + " = ? AND " +
            			COLUMN_NAME_QUESTION_ID + " = ? AND " +
            			COLUMN_NAME_ANSWER_ID + " = ? AND " +
            			COLUMN_NAME_DATE_TIME_ANSWER + " = ? AND " +
            			COLUMN_NAME_CHECKED_BY_DOCTOR + " = ?" ;

            	String[] selectionArgs = { 
            			String.valueOf(psm.getPatientid()),
            			String.valueOf(psm.getDoctorid()),
            			String.valueOf(psm.getLastcancertherapyexecid()),
            			String.valueOf(psm.getQuestionid()),
            			String.valueOf(psm.getAnswerid()),
            			String.valueOf(psm.getDatetimeanswer()),
            			String.valueOf(psm.getCheckedbydoctor())
            			};
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
    			{
    				return true;
    			}
            }
			return false;

        }   
    	
    	synchronized public static boolean setPatientStatusAsChecked(Context context, long lPatientID, long lDoctorID)
        {
            if(context != null)
            {
    	        SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
    	        ContentValues values = new ContentValues();
    			values.put(COLUMN_NAME_CHECKED_BY_DOCTOR, 1);
    			
    	       	// Which row to get based on WHERE
    	    	String selection = 
    	    			COLUMN_NAME_PATIENT_ID + " = ? AND " +
    	    			COLUMN_NAME_DOCTOR_ID + " = ?";
    	       	
    	    	String[] selectionArgs = {
    	    			String.valueOf(lPatientID),
    	    			String.valueOf(lDoctorID) };
    			
    			// Insert the new row, returning the primary key value of the new row
    			if(db.update(TABLE_NAME, values, selection, selectionArgs) > 0)
    			{
    				return true;
    			}
            }
			return false;
        }   
    	
    	synchronized public static boolean IsPatientStatusIDPresent(Context context, long lID)
        {
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection = {COLUMN_NAME_REMOTE_ID};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = COLUMN_NAME_REMOTE_ID + " = ?";

            	String[] selectionArgs = { String.valueOf(lID) };
            	
            	Cursor c = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((c != null) && (c.getCount() > 0))
            	{
                	return true;        	
            	}
            }
    		return false;
        }        
        
    	synchronized public static ArrayList<PatientStatus> getListPatientStatusGroupByDateTimeAnswer(Context context, long lPatientID, long lDoctorID)
        {
            ArrayList<PatientStatus> alpsm = new ArrayList<PatientStatus>();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                PatientStatus alpsmTemp = null; 
                		
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID,        			
            		COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID, 
               		COLUMN_NAME_PATIENT_ID, 
            		COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_DATE_TIME_ANSWER,
        			COLUMN_NAME_CHECKED_BY_DOCTOR    			
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
    				COLUMN_NAME_PATIENT_ID + " = ? AND " +
    				COLUMN_NAME_DOCTOR_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID) };

            	// GROUP BY
            	String strGroupBy = COLUMN_NAME_DATE_TIME_ANSWER;

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    strGroupBy,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
        			{
        				alpsmTemp = new PatientStatus();
        				alpsmTemp.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
        				alpsmTemp.setLastcancertherapyexecid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID)));
        				alpsmTemp.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
        				alpsmTemp.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
           				alpsmTemp.setDatetimeanswer(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_ANSWER)));
           				alpsmTemp.setCheckedbydoctor(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CHECKED_BY_DOCTOR)));
        				alpsm.add(alpsmTemp);
        			}        		
            	}
            }
        	return alpsm;
        }      
        
    	synchronized public static ArrayList<PatientStatus> getListPatientStatusByDateTimeAnswer(Context context, long lPatientID, long lDoctorID, String strDateTimeAnswer)
        {
            ArrayList<PatientStatus> alpsm = new ArrayList<PatientStatus>();
            
            if(context != null && strDateTimeAnswer != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                PatientStatus alpsmTemp = null; 
                		
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID, 
        			COLUMN_NAME_PATIENT_ID, 
        			COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID, 
        			COLUMN_NAME_PAIN_THERAPY_ID,
        			COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC,
        			COLUMN_NAME_QUESTION_ID,
        			COLUMN_NAME_ANSWER_ID,
        			COLUMN_NAME_DATE_TIME_ANSWER,
        			COLUMN_NAME_CHECKED_BY_DOCTOR
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_PATIENT_ID + " = ? AND " +
            			COLUMN_NAME_DOCTOR_ID + " = ? AND " +
            			COLUMN_NAME_DATE_TIME_ANSWER + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lPatientID), String.valueOf(lDoctorID), String.valueOf(strDateTimeAnswer) };

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
        			{
        				alpsmTemp = new PatientStatus();
        				alpsmTemp.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
        				alpsmTemp.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
        				alpsmTemp.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
        				alpsmTemp.setLastcancertherapyexecid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID)));
        				alpsmTemp.setPaintherapyid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PAIN_THERAPY_ID)));
        				alpsmTemp.setPaintherapydatetimeexec(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC)));
        				alpsmTemp.setQuestionid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_QUESTION_ID)));
        				alpsmTemp.setAnswerid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ANSWER_ID)));
        				alpsmTemp.setDatetimeanswer(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_ANSWER)));
          				alpsmTemp.setCheckedbydoctor(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CHECKED_BY_DOCTOR)));
          				alpsm.add(alpsmTemp);
      			
        			}        		
            	}
            }
        	return alpsm;
        }
        
    	synchronized public static ArrayList<PatientStatus> getListPatientStatusByCancerTherapyIDGroupByDateTimeAnswer(Context context, long lPatientID, long lDoctorID, long lCancerTherapyExecID)
        {
            ArrayList<PatientStatus> alpsm = new ArrayList<PatientStatus>();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                PatientStatus alpsmTemp = null;
                
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID,
            		COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID, 
               		COLUMN_NAME_PATIENT_ID, 
            		COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_DATE_TIME_ANSWER
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = "";

            	// Which row to get based on WHERE
            	String selection = 
            		COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID + " = ? AND " +
        			COLUMN_NAME_PATIENT_ID + " = ? AND " +
    				COLUMN_NAME_DOCTOR_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(lCancerTherapyExecID), String.valueOf(lPatientID), String.valueOf(lDoctorID) };

            	// GROUP BY
            	String strGroupBy = COLUMN_NAME_DATE_TIME_ANSWER;

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    strGroupBy,                               // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
        			{
        				alpsmTemp = new PatientStatus();
        				alpsmTemp.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
        				alpsmTemp.setLastcancertherapyexecid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID)));
        				alpsmTemp.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
        				alpsmTemp.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
        				alpsmTemp.setDatetimeanswer(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_ANSWER)));
        				alpsm.add(alpsmTemp);
        			}        		

            	}
            }
        	return alpsm;
        }  
    	
    	synchronized public static Collection<PatientStatus> getPatientStatusByRemoteIdEgualZero(Context context, long lPatientID)
        {
            Collection<PatientStatus> cpsm = new ArrayList<PatientStatus>();

            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();

                PatientStatus psm = null;
                
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID, 
        			COLUMN_NAME_PATIENT_ID, 
        			COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID, 
        			COLUMN_NAME_PAIN_THERAPY_ID,
        			COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC,
        			COLUMN_NAME_QUESTION_ID,
        			COLUMN_NAME_ANSWER_ID,
        			COLUMN_NAME_DATE_TIME_ANSWER,
        			COLUMN_NAME_CHECKED_BY_DOCTOR
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = COLUMN_NAME_DATE_TIME_ANSWER + " DESC";

            	// Which row to get based on WHERE
            	String selection = 
            			COLUMN_NAME_REMOTE_ID + " = ? AND " +
            			COLUMN_NAME_PATIENT_ID + " = ?" ;

            	String[] selectionArgs = { String.valueOf(0), String.valueOf(lPatientID) };

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
        			{
        				psm = new PatientStatus();
        				psm.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
        				psm.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
        				psm.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
        				psm.setLastcancertherapyexecid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID)));
        				psm.setPaintherapyid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PAIN_THERAPY_ID)));
        				psm.setPaintherapydatetimeexec(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC)));
        				psm.setQuestionid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_QUESTION_ID)));
        				psm.setAnswerid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ANSWER_ID)));
        				psm.setDatetimeanswer(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_ANSWER))); 
        				psm.setCheckedbydoctor(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CHECKED_BY_DOCTOR)));
        				cpsm.add(psm);
        			}        		
            	}
            }
        	return cpsm;
        }
    	
    	synchronized public static ArrayList<PatientStatus> getListPatientStatusByQuestion1(Context context, long lDoctorID)
        {
            ArrayList<PatientStatus> alpsm = new ArrayList<PatientStatus>();
            
            if(context != null)
            {
                SQLiteDatabase db = SQLHelper.getInstance(context).getDB();
                
                PatientStatus alpsmTemp = null;
                
                // Define a projection that specifies which columns from the database
            	// you will actually use after this query.
            	String[] projection =
            	{
            		COLUMN_NAME_REMOTE_ID, 
        			COLUMN_NAME_PATIENT_ID, 
        			COLUMN_NAME_DOCTOR_ID, 
        			COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID, 
        			COLUMN_NAME_PAIN_THERAPY_ID,
        			COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC,
        			COLUMN_NAME_QUESTION_ID,
        			COLUMN_NAME_ANSWER_ID,
        			COLUMN_NAME_DATE_TIME_ANSWER,
        			COLUMN_NAME_CHECKED_BY_DOCTOR
            	};

            	// How you want the results sorted in the resulting Cursor
            	String sortOrder = COLUMN_NAME_DATE_TIME_ANSWER + " DESC";

            	// Which row to get based on WHERE
            	String selection = 
                    	COLUMN_NAME_DOCTOR_ID + " = ? AND " +
                    	COLUMN_NAME_QUESTION_ID + " = ? ";

            	String[] selectionArgs = { String.valueOf(lDoctorID), String.valueOf(Question.QUESTION_1.getID()) };

            	Cursor cursor = db.query(
            		TABLE_NAME,  // The table to query
            	    projection,                               // The columns to return
            	    selection,                                // The columns for the WHERE clause
            	    selectionArgs,                            // The values for the WHERE clause
            	    null,                                     // don't group the rows
            	    null,                                     // don't filter by row groups
            	    sortOrder                                 // The sort order
            	    );
            	if((cursor != null) && (cursor.getCount() > 0))
            	{
        			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
        			{
        				alpsmTemp = new PatientStatus();
        				alpsmTemp.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_REMOTE_ID)));
        				alpsmTemp.setPatientid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PATIENT_ID)));
        				alpsmTemp.setDoctorid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID)));
        				alpsmTemp.setLastcancertherapyexecid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LAST_CANCER_THERAPY_EXEC_ID)));
        				alpsmTemp.setPaintherapyid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PAIN_THERAPY_ID)));
        				alpsmTemp.setPaintherapydatetimeexec(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PAIN_THERAPY_DATE_TIME_EXEC)));
        				alpsmTemp.setQuestionid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_QUESTION_ID)));
        				alpsmTemp.setAnswerid(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ANSWER_ID)));
        				alpsmTemp.setDatetimeanswer(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_TIME_ANSWER)));
        				alpsmTemp.setCheckedbydoctor(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CHECKED_BY_DOCTOR)));
        				alpsm.add(alpsmTemp);
      			
        			}        		
            	}
            }
        	return alpsm;
        }    	
            	
    }	    
}
