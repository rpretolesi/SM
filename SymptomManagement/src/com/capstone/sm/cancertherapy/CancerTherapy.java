package com.capstone.sm.cancertherapy;

import com.google.common.base.Objects;

public class CancerTherapy 
{
	
    private  long id;
    
    private  long patientid;
    private  long doctorid;
    private  String name;
    private  String datetimeset;
    private  String datetimeexec;
     

	public CancerTherapy(){
		
	}

	public CancerTherapy(long patientid, long doctorid, String name, String datetimeset, String datetimeexec) 
	{
		super();
		
		this.patientid = patientid;
		this.doctorid = doctorid;
		this.name = name;
		this.datetimeset = datetimeset;
		this.datetimeexec = datetimeexec;
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
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setDatetimeset(String datetimeset)
    {
        this.datetimeset = datetimeset;
    }
    
    public String getDatetimeset()
    {
        return this.datetimeset;
    }
    
    public void setDatetimeexec(String datetimeexec)
    {
        this.datetimeexec = datetimeexec;
    }

    public String getDatetimeexec()
    {
        return this.datetimeexec;
    }

    public long getId()
    {
    	return this.id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }

    // Reset all value
    public void reset()
    {
    	this.id = 0;
    	this.patientid = 0;
    	this.doctorid = 0;
    	this.name = "";
    	this.datetimeset = "";
    	this.datetimeexec = "";
    }

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(patientid, doctorid, name, datetimeset, datetimeexec);
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CancerTherapy) {
			CancerTherapy other = (CancerTherapy) obj;
			// Google Guava provides great utilities for equals too!
			return patientid == other.patientid
					&& doctorid == other.doctorid			
					&& Objects.equal(name, other.name)			
					&& Objects.equal(datetimeset, other.datetimeset)			
					&& Objects.equal(datetimeexec, other.datetimeexec);			


		} else {
			return false;
		}
	}
}
