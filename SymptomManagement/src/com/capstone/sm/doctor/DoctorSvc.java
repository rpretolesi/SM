package com.capstone.sm.doctor;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;

import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.comm.client.oauth.SecuredRestBuilder;
import com.capstone.sm.comm.client.unsafe.EasyHttpClient;

public class DoctorSvc 
{
	public static final String CLIENT_ID = "mobile";

	private static DoctorSvcApi m_DoctorSvc_;

	public static synchronized DoctorSvcApi get(Context ctx) {
		if (m_DoctorSvc_ != null) {
			return m_DoctorSvc_;
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

	public static synchronized DoctorSvcApi init(String server, String user,
			String pass) {
	
		m_DoctorSvc_ = new SecuredRestBuilder()
		.setLoginEndpoint(server + DoctorSvcApi.TOKEN_PATH)
		.setUsername(user)
		.setPassword(pass)
		.setClientId(CLIENT_ID)
		.setClient(
				new ApacheClient(new EasyHttpClient()))
		.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
		.create(DoctorSvcApi.class);

		return m_DoctorSvc_;
	}

	public static synchronized void close()
	{
		m_DoctorSvc_ = null;
	}
	
}