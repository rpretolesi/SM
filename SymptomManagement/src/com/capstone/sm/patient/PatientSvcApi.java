package com.capstone.sm.patient;

import java.util.Collection;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

public interface PatientSvcApi 
{
	public static final String USERNAME_PARAMETER = "username";
	public static final String PASSWORD_PARAMETER = "password";
	public static final String FIRST_NAME_PARAMETER = "first_name";
	public static final String LAST_NAME_PARAMETER = "last_name";
	public static final String DATE_OF_BIRTH_PARAMETER = "date_of_birth";
	public static final String MEDICAL_RECORD_ID_PARAMETER = "medical_record_id";
	public static final String IMAGE_PARAMETER = "image";
	public static final String DOCTOR_ID_PARAMETER = "doctor_id";
	
	public static final String TOKEN_PATH = "/oauth/token";
	
	// The path where we expect the PatientSvc to live
	public static final String PATIENT_SVC_PATH = "/patient";

	// The path to search patients by User Name
	public static final String PATIENT_USER_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByUsername";
	
	// The path to search videos by Doctor ID
	public static final String PATIENT_DOCTOR_ID_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByDoctorid";
	
	@GET(PATIENT_SVC_PATH)
	public Collection<Patient> getPatientList();

	@GET(PATIENT_SVC_PATH + "/{id}")
	public Patient getPatientById(@Path("id") long id);
	
	@POST(PATIENT_SVC_PATH)
	public Patient addPatient(@Body Patient pm);

	@Multipart
	@POST(PATIENT_SVC_PATH + "/{id}/picture")
//	public PictureStatus setPatienPictureData(@Path("id") long id, @Part("picture") TypedFile pictureData);
	public Void setPatienPictureData(@Path("id") long id, @Part("picture") TypedFile pictureData);

	@Streaming
    @GET(PATIENT_SVC_PATH + "/{id}/picture")
    Response getPatienPictureData(@Path("id") long id);

	@PUT(PATIENT_SVC_PATH)
	public Patient updatePatient(@Body Patient pm);
	
	@GET(PATIENT_USER_NAME_SEARCH_PATH + "/{username}")
	public Collection<Patient> getPatientByUsername(@Path("username") String username);

	@GET(PATIENT_DOCTOR_ID_SEARCH_PATH + "/{doctorid}")
	public Collection<Patient> getPatientByDoctorid(@Path("doctorid") long doctorid);	
}
