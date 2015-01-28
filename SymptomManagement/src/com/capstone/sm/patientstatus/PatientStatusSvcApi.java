package com.capstone.sm.patientstatus;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface PatientStatusSvcApi 
{
	public static final String TOKEN_PATH = "/oauth/token";
	
	// The path where we expect the PatientSvc to live
	public static final String PATIENT_STATUS_SVC_PATH = "/patientstatus";
	
	// The path to search Patient Status by Doctor ID
	public static final String PATIENT_STATUS_DOCTOR_ID_SEARCH_PATH = PATIENT_STATUS_SVC_PATH + "/search/findByDoctorid";

	@GET(PATIENT_STATUS_SVC_PATH)
	public Collection<PatientStatus> getPatientStatusList();
	
	@GET(PATIENT_STATUS_DOCTOR_ID_SEARCH_PATH + "/{doctorid}")
	public Collection<PatientStatus> getPatientStatusByDoctorid(@Path("doctorid") long doctorid);

	@GET(PATIENT_STATUS_SVC_PATH + "/{id}")
	public PatientStatus getPatientStatusById(@Path("id") long id);
	
	@POST(PATIENT_STATUS_SVC_PATH)
	public Collection<PatientStatus> addPatientStatus(@Body Collection<PatientStatus> cpsm);

	@PUT(PATIENT_STATUS_SVC_PATH + "/{patientid}" + "/{doctorid}")
	public Void setPatientStatusAsChecked(@Path("patientid") long patientid, @Path("doctorid") long doctorid);
}
