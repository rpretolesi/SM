package com.capstone.sm.patient.checkin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.capstone.SQL.SQLContract.Answer_3;
import com.capstone.sm.R;


public class Question_3_DialogFragment extends DialogFragment 
{
    public static Question_3_DialogFragment newInstance(String strTitleParam) {
    	Question_3_DialogFragment frag = new Question_3_DialogFragment();
        Bundle args = new Bundle();
        args.putString("TitleParam", strTitleParam);
        frag.setArguments(args);
        return frag;
    } 
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	String strTitleParam = getArguments().getString("TitleParam");
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.labelQuestion_3) + " " + strTitleParam)
               .setItems(R.array.labelAnswer_3, new DialogInterface.OnClickListener() {
                   @Override
				public void onClick(DialogInterface dialog, int which) 
                   {
                	   Answer_3 a3 = null;
                	   
                	   switch (which)
                	   {
		           	   		case 0:
		           	   		a3 = Answer_3.ANSWER_1;
		        	   			
		        	   			break;

		           	   		case 1:
		           	   		a3 = Answer_3.ANSWER_2;
		        	   			
		        	   			break;
                       }
                       ((ReminderActivity)getActivity()).doOnClick_Question_3(dialog, a3);
                   }
        });
        
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) 
            {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) 
                {
                    ((ReminderActivity)getActivity()).doOnClick_Back(dialog);
                }
                return false;
            }
        });
        
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
