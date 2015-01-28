package com.capstone.sm.paintherapy;

import com.google.common.base.Objects;

public class PainTherapy 
{
    private  long id;

    private  long patientid;
    private  long doctorid;
    private  String name;
     

	public PainTherapy() 
	{
		// TODO Auto-generated constructor stub
	}

	public PainTherapy(long patientid, long doctorid, String name) 
	{
		super();
		
		this.patientid = patientid;
		this.doctorid = doctorid;
		this.name = name;
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

    public long getId()
    {
    	return this.id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(patientid, doctorid, name);
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PainTherapy) {
			PainTherapy other = (PainTherapy) obj;
			// Google Guava provides great utilities for equals too!
			return patientid == other.patientid
					&& doctorid == other.doctorid			
					&& Objects.equal(name, other.name);			


		} else {
			return false;
		}
	}
}

