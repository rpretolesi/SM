package com.capstone.sm.doctor;

import java.util.Collection;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

public interface DoctorSvcApi 
{
	
	public static final String USERNAME_PARAMETER = "username";
	public static final String PASSWORD_PARAMETER = "password";
	public static final String FIRST_NAME_PARAMETER = "first_name";
	public static final String LAST_NAME_PARAMETER = "last_name";
	public static final String IDENTIFICATION_ID_PARAMETER = "identification_id";
	public static final String IMAGE_PARAMETER = "image";
	
	public static final String TOKEN_PATH = "/oauth/token";
	
	// The path where we expect the PatientSvc to live
	public static final String DOCTOR_SVC_PATH = "/doctor";

	// The path to search patients by User Name
	public static final String DOCTOR_USER_NAME_SEARCH_PATH = DOCTOR_SVC_PATH + "/search/findByUsername";
	
	@GET(DOCTOR_SVC_PATH)
	public Collection<Doctor> getDoctorList();
	
	@GET(DOCTOR_SVC_PATH + "/{id}")
	public Doctor getDoctorById(@Path("id") long id);
	
	@POST(DOCTOR_SVC_PATH)
	public Doctor addDoctor(@Body Doctor dm);
	
	@Multipart
	@POST(DOCTOR_SVC_PATH + "/{id}/picture")
//	public PictureStatus setPatienPictureData(@Path("id") long id, @Part("picture") TypedFile pictureData);
	public Void setDoctorPictureData(@Path("id") long id, @Part("picture") TypedFile pictureData);

	@Streaming
    @GET(DOCTOR_SVC_PATH + "/{id}/picture")
    Response getDoctorPictureData(@Path("id") long id);
	
	@GET(DOCTOR_USER_NAME_SEARCH_PATH + "/{username}")
	public Collection<Doctor> getDoctorByUsername(@Path("username") String username);
}
