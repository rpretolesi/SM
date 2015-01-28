package com.capstone.sm.paintherapy;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;

import com.capstone.SQL.SQLContract.Parameter;
import com.capstone.SQL.SQLContract.Settings;
import com.capstone.sm.comm.client.oauth.SecuredRestBuilder;
import com.capstone.sm.comm.client.unsafe.EasyHttpClient;
import com.capstone.sm.doctor.DoctorSvcApi;

public class PainTherapySvc 
{
	public static final String CLIENT_ID = "mobile";

	private static PainTherapySvcApi m_PainTherapySvc_;

	public static synchronized PainTherapySvcApi get(Context ctx) {
		if (m_PainTherapySvc_ != null) {
			return m_PainTherapySvc_;
		} else {
	        String strHostAddress = Settings.getParameter(ctx, Parameter.HOST_ADDRESS);
	        if(strHostAddress.equals("") == true)
	        {
	        	strHostAddress = "https://10.0.2.2:8443";
	        }
			return init(strHostAddress, "admin", "pass");
		}
	}

	public static synchronized PainTherapySvcApi init(String server, String user,
			String pass) {
	
		m_PainTherapySvc_ = new SecuredRestBuilder()
		.setLoginEndpoint(server + DoctorSvcApi.TOKEN_PATH)
		.setUsername(user)
		.setPassword(pass)
		.setClientId(CLIENT_ID)
		.setClient(
				new ApacheClient(new EasyHttpClient()))
		.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
		.create(PainTherapySvcApi.class);

		return m_PainTherapySvc_;
	}

	public static synchronized void close()
	{
		m_PainTherapySvc_ = null;
	}
	
}