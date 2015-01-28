package com.capstone.sm.patient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;

import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.comm.client.oauth.SecuredRestBuilder;
import com.capstone.sm.comm.client.unsafe.EasyHttpClient;

public class PatientSvc 
{
	public static final String CLIENT_ID = "mobile";

	private static PatientSvcApi m_PatientSvc_;

	public static synchronized PatientSvcApi get(Context ctx) {
		if (m_PatientSvc_ != null) {
			return m_PatientSvc_;
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

	public static synchronized PatientSvcApi init(String server, String user,
			String pass) {
	
		m_PatientSvc_ = new SecuredRestBuilder()
		.setLoginEndpoint(server + PatientSvcApi.TOKEN_PATH)
		.setUsername(user)
		.setPassword(pass)
		.setClientId(CLIENT_ID)
		.setClient(
				new ApacheClient(new EasyHttpClient()))
		.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
		.create(PatientSvcApi.class);

		return m_PatientSvc_;
	}

	public static synchronized void close()
	{
		m_PatientSvc_ = null;
	}
	
}