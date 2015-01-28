package com.capstone.sm.paintherapy;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface PainTherapySvcApi 
{
	
	public static final String TOKEN_PATH = "/oauth/token";
	
	// The path where we expect the PatientSvc to live
	public static final String PAIN_THERAPY_STATUS_SVC_PATH = "/paintherapy";
	
	@GET(PAIN_THERAPY_STATUS_SVC_PATH)
	public Collection<PainTherapy> getPainTherapyList();
	
	@POST(PAIN_THERAPY_STATUS_SVC_PATH)
	public PainTherapy addPainTherapy(@Body PainTherapy ptm);

	@PUT(PAIN_THERAPY_STATUS_SVC_PATH)
	public PainTherapy updatePainTherapy(@Body PainTherapy ptm);	

	@DELETE(PAIN_THERAPY_STATUS_SVC_PATH + "/{id}")
	public Void deletePainTherapy(@Path("id") long id);

	@GET(PAIN_THERAPY_STATUS_SVC_PATH + "/{id}")
	public PainTherapy getPainTherapyById(@Path("id") long id);
	
	public static final String PAIN_THERAPY_PATIENT_ID_SEARCH_PATH = PAIN_THERAPY_STATUS_SVC_PATH + "/search/findByPatientid";
	@GET(PAIN_THERAPY_PATIENT_ID_SEARCH_PATH + "/{patientid}")
	public Collection<PainTherapy> getPainTherapyByPatientid(@Path("patientid") long patientid);
	
	public static final String PAIN_THERAPY_PATIENT_ID_DOCTOR_ID_AND_NAME_SEARCH_PATH = PAIN_THERAPY_STATUS_SVC_PATH + "/search/findByPatientidAndDoctoridAndName";
	@GET(PAIN_THERAPY_PATIENT_ID_DOCTOR_ID_AND_NAME_SEARCH_PATH + "/{patientid}" + "/{doctorid}" + "/{name}")
	public PainTherapy getPainTherapyByPatientidAndDoctoridAndName(@Path("patientid") long patientid, @Path("doctorid") long doctorid, @Path("name") String name);

	public static final String PAIN_THERAPY_PATIENT_ID_DOCTOR_ID_SEARCH_PATH = PAIN_THERAPY_STATUS_SVC_PATH + "/search/findByPatientidAndDoctorid";
	@GET(PAIN_THERAPY_PATIENT_ID_DOCTOR_ID_SEARCH_PATH + "/{patientid}" + "/{doctorid}")
	public Collection<PainTherapy> getPainTherapyByPatientidAndDoctorid(@Path("patientid") long patientid, @Path("doctorid") long doctorid);

	public static final String PAIN_THERAPY_PATIENT_ID_AND_NAME_SEARCH_PATH = PAIN_THERAPY_STATUS_SVC_PATH + "/search/findByPatientidAndName";
	@GET(PAIN_THERAPY_PATIENT_ID_AND_NAME_SEARCH_PATH + "/{patientid}" + "/{name}")
	public Collection<PainTherapy> getPainTherapyByPatientidAndName(@Path("patientid") long patientid, @Path("name") String name);

}
