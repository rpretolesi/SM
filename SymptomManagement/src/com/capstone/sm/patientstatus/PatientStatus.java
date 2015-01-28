package com.capstone.sm.patientstatus;

import com.google.common.base.Objects;

public class PatientStatus 
{
    private long id;
    
	private long patientid;
	private long doctorid;
    private long lastcancertherapyexecid;
    private long paintherapyid;
    private String paintherapydatetimeexec;
	private long questionid;
    private long answerid;
	private String datetimeanswer;
    private long checkedbydoctor;
     
	public PatientStatus() {
	}

	public PatientStatus(long patientid, long doctorid, long lastcancertherapyexecid, long paintherapyid, String paintherapydatetimeexec, 
			long questionid, long answerid, String datetimeanswer, long checkedbydoctor) 
	{
		super();
		
		this.patientid = patientid;
		this.doctorid = doctorid;
		this.lastcancertherapyexecid = lastcancertherapyexecid;
		this.paintherapyid = paintherapyid;
		this.paintherapydatetimeexec = paintherapydatetimeexec;
		this.questionid = questionid;
		this.answerid = answerid;
		this.datetimeanswer = datetimeanswer;
		this.checkedbydoctor = checkedbydoctor;
	}
     
    public void setPatientid(long patientid)
    {
        this.patientid = patientid;
    }
    
    public long getPatientid()
    {
    	return this.patientid;
    }

    public void setDoctorid(long doctorid)
    {
        this.doctorid = doctorid;
    }
    
    public long getDoctorid()
   {
    	return this.doctorid;
   }

    public void setLastcancertherapyexecid(long lastcancertherapyexecid)
    {
        this.lastcancertherapyexecid = lastcancertherapyexecid;
    }
    
    public long getLastcancertherapyexecid()
    {
    	return this.lastcancertherapyexecid;
    }
    
    public void setPaintherapyid(long paintherapyid)
    {
        this.paintherapyid = paintherapyid;
    }

    public long getPaintherapyid()
    {
    	return this.paintherapyid;
    }
    
    public void setPaintherapydatetimeexec(String paintherapydatetimeexec)
    {
        this.paintherapydatetimeexec = paintherapydatetimeexec;
    }

    public String getPaintherapydatetimeexec()
    {
    	return this.paintherapydatetimeexec;
    }
    
    public void setQuestionid(long questionid)
    {
        this.questionid = questionid;
    }
    
    public long getQuestionid()
    {
    	return this.questionid;
    }
    
    public void setAnswerid(long answerid)
    {
        this.answerid = answerid;
    }

    public long getAnswerid()
    {
    	return this.answerid;
    }
    
    public void setDatetimeanswer(String datetimeanswer)
    {
        this.datetimeanswer = datetimeanswer;
    }

    public String getDatetimeanswer()
    {
    	return this.datetimeanswer;
    }  
    
    public void setCheckedbydoctor(long checkedbydoctor)
    {
        this.checkedbydoctor = checkedbydoctor;
    }

    public long getCheckedbydoctor()
    {
    	return this.checkedbydoctor;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }

    public long getId()
    {
    	return this.id;
    }
 
    // Reset all value
    public void reset()
    {
    	this.id = 0;
    	this.patientid = 0;
    	this.doctorid = 0;
    	this.lastcancertherapyexecid = 0;
    	this.paintherapyid = 0;
    	this.paintherapydatetimeexec = "";
        this.questionid = 0;
        this.answerid = 0;
        this.datetimeanswer = "";
        this.checkedbydoctor = 0;
    }
    
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(patientid, doctorid, lastcancertherapyexecid, paintherapyid, paintherapydatetimeexec, questionid, datetimeanswer);
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PatientStatus) {
			PatientStatus other = (PatientStatus) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(patientid, other.patientid)
					&& doctorid == other.doctorid			
					&& lastcancertherapyexecid == other.lastcancertherapyexecid			
					&& paintherapyid == other.paintherapyid			
					&& Objects.equal(paintherapydatetimeexec, other.paintherapydatetimeexec)		
					&& questionid == other.questionid			
					&& Objects.equal(datetimeanswer, other.datetimeanswer);			


		} else {
			return false;
		}
	}
}
