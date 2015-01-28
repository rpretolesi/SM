package com.capstone.sm.cancertherapy;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface CancerTherapySvcApi 
{
	public static final String TOKEN_PATH = "/oauth/token";
	
	// The path where we expect the PatientSvc to live
	public static final String CANCER_THERAPY_STATUS_SVC_PATH = "/cancertherapy";

	@GET(CANCER_THERAPY_STATUS_SVC_PATH)
	public Collection<CancerTherapy> getCancerTherapyList();

	@POST(CANCER_THERAPY_STATUS_SVC_PATH)
	public CancerTherapy addCancerTherapy(@Body CancerTherapy ctm);

	@PUT(CANCER_THERAPY_STATUS_SVC_PATH)
	public CancerTherapy updateCancerTherapy(@Body CancerTherapy ctm);	

	@DELETE(CANCER_THERAPY_STATUS_SVC_PATH + "/{id}")
	public Void deleteCancerTherapy(@Path("id") long id);

	@GET(CANCER_THERAPY_STATUS_SVC_PATH + "/{id}")
	public CancerTherapy getCancerTherapyById(@Path("id") long id);
	
	public static final String CANCER_THERAPY_PATIENT_ID_SEARCH_PATH = CANCER_THERAPY_STATUS_SVC_PATH + "/search/findByPatientid";
	@GET(CANCER_THERAPY_PATIENT_ID_SEARCH_PATH + "/{patientid}")
	public Collection<CancerTherapy> getCancerTherapyByPatientid(@Path("patientid") long patientid);

	public static final String CANCER_THERAPY_DOCTOR_ID_SEARCH_PATH = CANCER_THERAPY_STATUS_SVC_PATH + "/search/findByDoctorid";
	@GET(CANCER_THERAPY_PATIENT_ID_SEARCH_PATH + "/{doctorid}")
	public Collection<CancerTherapy> getCancerTherapyByDoctorid(@Path("doctorid") long doctorid);

	public static final String CANCER_THERAPY_PATIENT_ID_DOCTOR_ID_AND_NAME_SEARCH_PATH = CANCER_THERAPY_STATUS_SVC_PATH + "/search/findByPatientidAndDoctoridAndName";
	@GET(CANCER_THERAPY_PATIENT_ID_DOCTOR_ID_AND_NAME_SEARCH_PATH + "/{patientid}" + "/{doctorid}" + "/{name}")
	public CancerTherapy getCancerTherapyByPatientidAndDoctoridAndName(@Path("patientid") long patientid, @Path("doctorid") long doctorid, @Path("name") String name);

	public static final String CANCER_THERAPY_PATIENT_ID_DOCTOR_ID_SEARCH_PATH = CANCER_THERAPY_STATUS_SVC_PATH + "/search/findByPatientidAndDoctorid";
	@GET(CANCER_THERAPY_PATIENT_ID_DOCTOR_ID_SEARCH_PATH + "/{patientid}" + "/{doctorid}")
	public Collection<CancerTherapy> getCancerTherapyByPatientidAndDoctorid(@Path("patientid") long patientid, @Path("doctorid") long doctorid);

	public static final String CANCER_THERAPY_PATIENT_ID_AND_NAME_SEARCH_PATH = CANCER_THERAPY_STATUS_SVC_PATH + "/search/findByPatientidAndName";
	@GET(CANCER_THERAPY_PATIENT_ID_AND_NAME_SEARCH_PATH + "/{patientid}" + "/{name}")
	public Collection<CancerTherapy> getCancerTherapyByPatientidAndName(@Path("patientid") long patientid, @Path("name") String name);
		
}
