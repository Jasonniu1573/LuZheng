package com.miles.maipu.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.miles.maipu.luzheng.LoginActivity;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UnixTime;

public class UploadLatLngService extends Service
{

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	public BDLocation myLocation = null;
	private double lastLat = 0;
	private double lastLng = 0;

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		System.out.println("UPLOADstart Service");
		InitLocation();
	}


	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// Receive Location
			myLocation = location;
			if(location.getLatitude()==lastLat&&location.getLongitude()==lastLng)
			{
				Log.i("UPLOADpause", "[经纬度相同，暂不上传]");
				return;
			}
			
			
			lastLat = location.getLatitude();
			lastLng = location.getLongitude();
			
			if (location == null || location.getLocType() == 162 || "4.9E-324".equals(location.getLongitude() + "") || "4.9E-324".equals(location.getLongitude() + ""))
			{
				return;
			}
			if(!OverAllData.getRecordId().equals(""))
			{
				HashMap<String, Object> senddata = new HashMap<String, Object>();
				senddata.put("PatorlRecord", OverAllData.getRecordId());
				senddata.put("CreateTime", UnixTime.getStrCurrentSimleTime());
				senddata.put("LatitudeLongitude", location.getLongitude()+","+location.getLatitude());
				Log.i("UPLOADgo", "["+JSONUtil.toJson(senddata)+"]");
				
				SimpleDateFormat df = new SimpleDateFormat("HHmmss");//设置日期格式
				if(Integer.parseInt(df.format(new Date()))<173000)
				{
//					mLocationClient.stop();
//					mLocationClient = null;
//					UploadLatLngService.this.stopSelf();
//					Log.i("UPLOADgo", "[Stop Self]");
					
					new SendDataTask()
					{

						@Override
						protected void onPostExecute(Object result)
						{
							// TODO Auto-generated method stub
							Log.i("UPLOADresult", result.toString());
							super.onPostExecute(result);
						}
					}.execute(new ParamData(ApiCode.SaveTrajectory,"["+JSONUtil.toJson(senddata)+"]"));
				
				}
				
			}
			
		}
	}

	public void InitLocation()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		int span = 3000;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mLocationClient.stop();
		mLocationClient = null;
		System.out.println("UPLOADend Service");
	}

}
