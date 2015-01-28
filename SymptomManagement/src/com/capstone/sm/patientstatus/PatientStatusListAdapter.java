package com.capstone.sm.patientstatus;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capstone.SQL.SQLContract.Answer_1;
import com.capstone.SQL.SQLContract.Answer_2;
import com.capstone.SQL.SQLContract.Answer_3;
import com.capstone.SQL.SQLContract.CancerTherapyEntry;
import com.capstone.SQL.SQLContract.PainTherapyEntry;
import com.capstone.SQL.SQLContract.PatientStatusEntry;
import com.capstone.SQL.SQLContract.Question;
import com.capstone.sm.R;
import com.capstone.sm.ViewHolder;
import com.capstone.sm.cancertherapy.CancerTherapy;
import com.capstone.sm.datetime.DateTimeUtils;
import com.capstone.sm.paintherapy.PainTherapy;



public class PatientStatusListAdapter extends BaseAdapter 
{
	private ArrayList<PatientStatus> m_lpsm = new ArrayList<PatientStatus>();
	private ArrayList<PainTherapy> m_lptm;

    private final Context m_context;
    
    private static final int START_ID_REF = 100;

    // the context is needed to inflate views in getView()
    public PatientStatusListAdapter(Context context) {
        this.m_context = context;
    }

    public void updateData(ArrayList<PatientStatus> psm) 
    {
        this.m_lpsm = psm;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return m_lpsm.size();
    }

    @Override
    public PatientStatus getItem(int position) {
        return m_lpsm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        TextView tvTextView;
        int iID;
        // Full patient Status Information
        ArrayList<PatientStatus> alpsm;
    	 	
        // ATTENTION, this value contain only a reference to the Last Cancer Therapy Executed, 
        // Patient, Doctor and DateTime that i need for retrieve all informations
        // for the grouped rows
        PatientStatus psm = getItem(position);       
        
        // Now i retrieve all Pain Therapy
        m_lptm = PainTherapyEntry.getListPainTherapy(m_context, psm.getPatientid(), psm.getDoctorid());

         if (convertView == null) 
         {
            convertView = LayoutInflater.from(m_context).inflate(R.layout.patient_status_list_adapter, parent, false);
            if (psm != null)
            {
	            // I am building dynamically all fields that i need
	            // I get the Question ID as reference

            	// Reference to the Last Cancer Therapy
            	iID = START_ID_REF + Question.QUESTION_20.getID();
	            tvTextView = new TextView(m_context.getApplicationContext());
	            tvTextView.setId(iID);
	            ((ViewGroup) convertView).addView(tvTextView);
            	
	            // First Question, just 1 field
            	iID = START_ID_REF + Question.QUESTION_1.getID();
	            tvTextView = new TextView(m_context.getApplicationContext());
	            tvTextView.setId(iID);
	            ((ViewGroup) convertView).addView(tvTextView);
	            
	            // Second Question, variable field
	            // I get the nr of the Pain Therapy
	            if(m_lptm != null)
	            {
		            for(PainTherapy ptm : m_lptm)
		            {
		            	iID = START_ID_REF + Question.QUESTION_2.getID() + (int)ptm.getId() * 1000;
			            tvTextView = new TextView(m_context.getApplicationContext());
			            tvTextView.setId(iID);
			            ((ViewGroup) convertView).addView(tvTextView);
		            }
	            }

	            // Third Question, just 1 field
            	iID = START_ID_REF + Question.QUESTION_3.getID();
	            tvTextView = new TextView(m_context.getApplicationContext());
	            tvTextView.setId(iID);
	            ((ViewGroup) convertView).addView(tvTextView);

	            // Date Time answer, just 1 field
            	iID = START_ID_REF + Question.QUESTION_10.getID();
	            tvTextView = new TextView(m_context.getApplicationContext());
	            tvTextView.setId(iID);
	            ((ViewGroup) convertView).addView(tvTextView);
            }
        }

        int iTextViewCount = ((ViewGroup) convertView).getChildCount();
        tvTextView =  ViewHolder.get(convertView, START_ID_REF);
        String strValueQuestion;
        String strValueAnswer;
        // Reset the old data
    	for(int iIndex = 0; iIndex < iTextViewCount; iIndex++)
    	{
    		tvTextView = (TextView) ((ViewGroup) convertView).getChildAt(iIndex);
            tvTextView.setText("");
    	}

        if (psm != null)
        {
            // Full patient Status Information
        	alpsm = PatientStatusEntry.getListPatientStatusByDateTimeAnswer(m_context, psm.getPatientid(), psm.getDoctorid(), psm.getDatetimeanswer());
			if(alpsm != null)
			{
	        	for(int iIndex = 0; iIndex < iTextViewCount; iIndex++)
	        	{
	        		// The Cancer Therapy ID is the same for the same Date and Time of the answer, do i get the First because it's the same
	        		// of the others answer.
	            	iID = START_ID_REF + Question.QUESTION_20.getID();
	        		tvTextView = (TextView) ((ViewGroup) convertView).getChildAt(iIndex);
	        		if(tvTextView.getId() == iID)
	        		{
	        			strValueQuestion = "---";
	        			strValueAnswer = "---";
	        			
	            		// Question String
	            		strValueQuestion = m_context.getString(Question.QUESTION_20.getValue());
	            		
	            		CancerTherapy ctm = CancerTherapyEntry.getCancerTherapyByID(m_context, psm.getLastcancertherapyexecid());
	
	            		if(ctm != null)
	            		{
		            		// Answer string
		            		strValueAnswer = ctm.getName() + ", " + DateTimeUtils.getInstance().FormatDateTimeFromDBToString(m_context, ctm.getDatetimeexec());
	            		}
	
	    				tvTextView.setTextColor(Color.BLACK);

	    				// Show
			            tvTextView.setText(strValueQuestion + ": \n" + strValueAnswer);
	
	        		}
	        		
	               	iID = START_ID_REF + Question.QUESTION_1.getID();
	        		tvTextView = (TextView) ((ViewGroup) convertView).getChildAt(iIndex);
	        		if(tvTextView.getId() == iID)
	        		{
	        			strValueQuestion = "---";
	        			strValueAnswer = "---";
    		            for(PatientStatus psmTemp : alpsm)
    		            {
    		            	if(psmTemp.getQuestionid() == Question.QUESTION_1.getID())
    		            	{
    		            		// Question String
    		            		strValueQuestion = m_context.getString(Question.QUESTION_1.getValue());
    		            		
    		            		// Answer string
    		            		for (Answer_1 ia1 : Answer_1.values())
	            				{
    		            			if(ia1.getID() == psmTemp.getAnswerid())
    		            			{
    		            				strValueAnswer = m_context.getString(ia1.getValue());
    		            				
    		            				switch (ia1)
    		            				{
    		            					case ANSWER_1:
    	    		            				tvTextView.setTextColor(Color.GREEN);
    		            						break;
    		            					
    		            					case ANSWER_2:
    	    		            				tvTextView.setTextColor(Color.YELLOW);
    		            						break;

    		            					case ANSWER_3:
    	    		            				tvTextView.setTextColor(Color.RED);
    		            						break;
    		            				}

    		            				// Show
    		        		            tvTextView.setText(strValueQuestion + ": \n" + strValueAnswer);
    		        		            
		    		            		break;    		        		            
    		            			}
	            				}
    		            		
    		            		break;    		            		
    		            	}
	        			}
	        		}
	        		
		            if(m_lptm != null)
		            {
			            for(PainTherapy ptm : m_lptm)
			            {
			            	iID = START_ID_REF + Question.QUESTION_2.getID() + (int)ptm.getId() * 1000  ;
			        		tvTextView = (TextView) ((ViewGroup) convertView).getChildAt(iIndex);
			        		if(tvTextView.getId() == iID)
			        		{
			        			strValueQuestion = "---";
			        			strValueAnswer = "---";

			        			for(PatientStatus psmTemp : alpsm)
		    		            {
		    		            	if(psmTemp.getQuestionid() == Question.QUESTION_2.getID())
		    		            	{
	    		            			if(psmTemp.getPaintherapyid() == ptm.getId())
	    		            			{

			    		            		strValueQuestion = m_context.getString(Question.QUESTION_2.getValue()) +
			    		            				" - " +
			    		            				ptm.getName();

			    		            		// Answer string
			    		            		for (Answer_2 ia2 : Answer_2.values())
				            				{
			    		            			if(ia2.getID() == psmTemp.getAnswerid())
			    		            			{
			    		            				strValueAnswer = m_context.getString(ia2.getValue()) + 
			    		            						", " + 
			    		            						DateTimeUtils.getInstance().FormatDateTimeFromDBToString(m_context, psmTemp.getPaintherapydatetimeexec());

			    		            				switch (ia2)
			    		            				{
			    		            					case ANSWER_1:
			    	    		            				tvTextView.setTextColor(Color.GREEN);
			    		            						break;
			    		            					
			    		            					case ANSWER_2:
			    	    		            				tvTextView.setTextColor(Color.YELLOW);
			    		            						break;

			    		            				}

			    		            				// Show
					    		            		tvTextView.setText(strValueQuestion + ": \n" + strValueAnswer);
			    		        		            
					    		            		break;    		        		            			    		            				
			    		            			}
				            				}
			    		            		
			    		            		break;
			    		            	}
		    		            	}
			    		            
			        			}
			        			
			            		break;
			        		}
			            }
		            }
		            
	            	iID = START_ID_REF + Question.QUESTION_3.getID();
	        		tvTextView = (TextView) ((ViewGroup) convertView).getChildAt(iIndex);
	        		if(tvTextView.getId() == iID)
	        		{
	        			strValueQuestion = "---";
	        			strValueAnswer = "---";
    		            for(PatientStatus psmTemp : alpsm)
    		            {
    		            	if(psmTemp.getQuestionid() == Question.QUESTION_3.getID())
    		            	{
    		            		// Question String
    		            		strValueQuestion = m_context.getString(Question.QUESTION_3.getValue());
    		            		
    		            		// Answer string
    		            		for (Answer_3 ia3 : Answer_3.values())
	            				{
    		            			if(ia3.getID() == psmTemp.getAnswerid())
    		            			{
    		            				strValueAnswer = m_context.getString(ia3.getValue());
    		            				
    		            				switch (ia3)
    		            				{
    		            					case ANSWER_1:
    	    		            				tvTextView.setTextColor(Color.GREEN);
    		            						break;
    		            					
    		            					case ANSWER_2:
    	    		            				tvTextView.setTextColor(Color.RED);
    		            						break;

    		            				}

    		            				// Show
    		        		            tvTextView.setText(strValueQuestion + ": \n" + strValueAnswer);

    	    		            		break;    		            		
    		            			}
	            				}

    		            		break;    		            		
    		            	}
	        			}        			
	        		}
	        		
	            	iID = START_ID_REF + Question.QUESTION_10.getID();
	        		tvTextView = (TextView) ((ViewGroup) convertView).getChildAt(iIndex);
	        		if(tvTextView.getId() == iID)
	        		{
	        			strValueQuestion = "---";
	        			strValueAnswer = "---";
	        			
	            		// Question String
	            		strValueQuestion = m_context.getString(Question.QUESTION_10.getValue());
	
	            		// Answer string
	            		strValueAnswer = DateTimeUtils.getInstance().FormatDateTimeFromDBToString(m_context, psm.getDatetimeanswer());
	        	        
	    				tvTextView.setTextColor(Color.BLACK);
	
	    				// Show
			            tvTextView.setText(strValueQuestion + ": \n" + strValueAnswer);
	
	        		}
	        	}       
        	}       
        }        
     
        return convertView;
        
    }
}
