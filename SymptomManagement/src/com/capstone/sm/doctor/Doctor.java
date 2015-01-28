package com.capstone.sm.doctor;

import com.google.common.base.Objects;

public class Doctor 
{
    private  long id;
    
    private  String firstname;
	private  String lastname;
	private  String identificationid;
	private  String username;
	private  String password;
	private  byte image[];
     
	private String contenttype;
	
	private String dataUrl;
	
	public Doctor() {
	}

	public Doctor(String firstname, String lastname, String identificationid, String username, 
			String password, byte[] image, String contenttype) 
	{
		super();
		
		this.firstname = firstname;
		this.lastname = lastname;
		this.identificationid = identificationid;
		this.username = username;
		this.password = password;
		this.image = image;
		this.contenttype = contenttype;
	}

	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getIdentificationid() {
		return identificationid;
	}
	
	public void setIdentificationid(String identificationid) {
		this.identificationid = identificationid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	
	
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	
    // Reset all value
    public void reset()
    {
    	this.id = 0;
    	this.firstname = "";
        this.lastname = "";
        this.identificationid = "";
        this.username = "";
    	this.password = "";
    	this.image = null;
    	
    }

	/**
	 * Two Patients will generate the same hashcode if they have exactly the same
	 * values for their Username.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(username);
	}

	/**
	 * Two Patients are considered equal if they have exactly the same values for
	 * their Username.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Doctor) {
			Doctor other = (Doctor) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(username, other.username);
		} else {
			return false;
		}
	}
}
