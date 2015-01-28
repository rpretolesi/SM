package com.capstone.sm.patientstatus;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;

import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.comm.client.oauth.SecuredRestBuilder;
import com.capstone.sm.comm.client.unsafe.EasyHttpClient;
import com.capstone.sm.doctor.DoctorSvcApi;

public class PatientStatusSvc 
{
	public static final String CLIENT_ID = "mobile";

	private static PatientStatusSvcApi m_PatientStatusSvc_;

	public static synchronized PatientStatusSvcApi get(Context ctx) {
		if (m_PatientStatusSvc_ != null) {
			return m_PatientStatusSvc_;
		} else {
	        String strHostAddress = Settings.getParameter(ctx, Parameter.HOST_ADDRESS);
	        if(strHostAddress.equals("") == true)
	        {
	        	strHostAddress = "https://10.0.2.2:8443";
	        }
			return init(strHostAddress, "admin", "pass");
//			return init(MainActivity.m_strHostAddress, "admin", "pass");
		}
	}

	public static synchronized PatientStatusSvcApi init(String server, String user,
			String pass) {
	
		m_PatientStatusSvc_ = new SecuredRestBuilder()
		.setLoginEndpoint(server + DoctorSvcApi.TOKEN_PATH)
		.setUsername(user)
		.setPassword(pass)
		.setClientId(CLIENT_ID)
		.setClient(
				new ApacheClient(new EasyHttpClient()))
		.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
		.create(PatientStatusSvcApi.class);

		return m_PatientStatusSvc_;
	}

	public static synchronized void close()
	{
		m_PatientStatusSvc_ = null;
	}
	
}