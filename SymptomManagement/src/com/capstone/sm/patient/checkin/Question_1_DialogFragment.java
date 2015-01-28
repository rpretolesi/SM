package com.capstone.sm.patient.checkin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.capstone.SQL.SQLContract.Answer_1;
import com.capstone.sm.R;


public class Question_1_DialogFragment extends DialogFragment 
{
    
    public static Question_1_DialogFragment newInstance(String strTitleParam) {
    	Question_1_DialogFragment frag = new Question_1_DialogFragment();
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
        
        builder.setTitle(getString(R.string.labelQuestion_1) + " " + strTitleParam)
               .setItems(R.array.labelAnswer_1, new DialogInterface.OnClickListener() 
               {
                   @Override
				public void onClick(DialogInterface dialog, int which) 
                   {
                	   Answer_1 a1 = null;
                	   
                	   switch (which)
                	   {
		           	   		case 0:
		        	   			a1 = Answer_1.ANSWER_1;
		        	   			
		        	   			break;

		           	   		case 1:
		        	   			a1 = Answer_1.ANSWER_2;
		        	   			
		        	   			break;

		        	   		case 2:
		        	   			a1 = Answer_1.ANSWER_3;
		        	   			
		        	   			break;
                	   }

                       ((ReminderActivity)getActivity()).doOnClick_Question_1(dialog, a1);
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
