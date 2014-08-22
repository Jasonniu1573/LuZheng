package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navi.sdkdemo.BNavigatorActivity;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.BaseMapObject;
import com.miles.maipu.util.DemoApplication;
import com.miles.maipu.util.ImageUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UGallery;

public class TaskInfoActivity extends AbsBaseActivity implements OnGetGeoCoderResultListener
{

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private String id = "";
	private ImageView img_Photo;
	private LatLng latlng = null;
	private HashMap<String, Object> res;
	private Button Btn_Callback;
	private Button Btn_Comfirm;
	private LinearLayout Linear_Step;
	private UGallery gallery_photo;
	private UGallery gallery_After;
	
	private LinearLayout linear_Remark;
	
	private TextView text_remark;
	private HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>(); // 图片缓存
	private HashMap<String, Bitmap> AfterimagesCache = new HashMap<String, Bitmap>(); // 图片缓存
	private boolean isNeedRefresh = false;
	private LinearLayout gallery_Linear;
	private LinearLayout gallery_Linearafter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_task_info);
		super.onCreate(savedInstanceState);
		img_Photo = (ImageView) findViewById(R.id.img_photo);
		id = getIntent().getStringExtra("id");
		initView();
		initNavi();
		isNeedRefresh = true;
	}

	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		if(isNeedRefresh)
		{
			getallotData();
		}
		isNeedRefresh = false;
		super.onResume();
	}

	private void getallotData()
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				res = (HashMap<String, Object>) result;
				String[] strlatlng = res.get("LatitudeLongitude").toString().split(",");
//				((TextView) findViewById(R.id.text_code)).setText("上报编号：" + res.get("SubmitCode").toString());
				((TextView) findViewById(R.id.text_time)).setText(res.get("AllotedDate").toString());
				((TextView) findViewById(R.id.text_name)).setText(res.get("Name").toString());
				((TextView) findViewById(R.id.text_unitname)).setText(res.get("Organization").toString());
				
				
				((TextView) findViewById(R.id.text_mark)).setText(res.get("Mark").toString());
//				
				((TextView) findViewById(R.id.text_category)).setText(res.get("PatorlCateGory").toString());
				((TextView) findViewById(R.id.text_project)).setText( res.get("PatorlItem").toString());
//				
				((TextView) findViewById(R.id.text_line)).setText(res.get("RoadLine").toString());
				((TextView) findViewById(R.id.text_lane)).setText(res.get("Lane").toString());
				
				((TextView) findViewById(R.id.text_status)).setText(res.get("HandleStatus").toString());
				((TextView) findViewById(R.id.text_conntext)).setText(res.get("EventContent").toString());
				
				 if(res.get("IsFeedBack").toString().equals("true"))
				 {
					 Btn_Callback.setVisibility(View.GONE);
				 }
				 
				 if(res.get("PersonInformationID").toString().equals(OverAllData.getLoginId())&&res.get("HandleStatus").toString().equals("已处理"))
				 {
					 Btn_Comfirm.setVisibility(View.VISIBLE);
				 }
				 else
				 {
					 Btn_Comfirm.setVisibility(View.GONE);
				 }
				 
//				if(OverAllData.getPostion()>0)
//				{
					List<HashMap<String, Object>> stepList = (List<HashMap<String, Object>>) res.get("Step");
					InputStep(stepList);
//				}
				// 初始化搜索模块，注册事件监听
				mSearch = GeoCoder.newInstance();
				mSearch.setOnGetGeoCodeResultListener(TaskInfoActivity.this);
				latlng = new LatLng(Double.parseDouble(strlatlng[1]), Double.parseDouble(strlatlng[0]));
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
				
				String[] path = res.get("Picture").toString().split("\\|");
				
				
				ComposeImg(gallery_photo,gallery_Linear, path, imagesCache);
				
				if(!(res.get("HandleStatus")+"").equals("已处理"))
				{
					linear_Remark.setVisibility(View.GONE);
				}
				else
				{
					linear_Remark.setVisibility(View.VISIBLE);
					text_remark.setText(res.get("FeedBackContent")+"");
					
					String[] pathf = res.get("FeedBackPic").toString().split("\\|");
					
					
					ComposeImg(gallery_After,gallery_Linearafter, pathf, AfterimagesCache);
					
//					ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl+res.get("AfterPicture")+"", img_After);
				}
				
				
//				ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl + res.get("Picture") + "", img_Photo);
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetEventAllotDetails, id));
	}
	
	
	private void InputStep(List<HashMap<String, Object>> stepList)
	{
		for(HashMap<String, Object> map : stepList)
		{    
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2);  
			layoutParams.setMargins(5, 5, 5, 5);
			
			ImageView img = new ImageView(mContext);
			img.setBackgroundResource(R.drawable.diver);
			Linear_Step.addView(img, layoutParams);
			
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			View view = mInflater.inflate(R.layout.listitem_stepevent, null);
			((TextView)view.findViewById(R.id.text_unit)).setText(map.get("OrganizationName").toString());
			((TextView)view.findViewById(R.id.text_name)).setText(map.get("ReceivePerson").toString());
			((TextView)view.findViewById(R.id.text_time)).setText(map.get("ReceiveDateTime").toString());
			((TextView)view.findViewById(R.id.text_jiaoban)).setText(map.get("Opinion").toString());

			
//			TextView text = new TextView(mContext);
//			text.setText("受理单位："+map.get("OrganizationName").toString()+"\r\n接收人："+map.get("ReceivePerson").toString()+"\r\n接收时间："+map.get("ReceiveDateTime").toString()+"\r\n交办意见："+map.get("Opinion")+"");
//			text.setTextColor(getResources().getColor(R.color.black));
			Linear_Step.addView(view);
		}
	}
	
	

	/**
	 * 指定导航起终点启动GPS导航.起终点可为多种类型坐标系的地理坐标。 前置条件：导航引擎初始化成功
	 */
	private void launchNavigator2()
	{
		// 这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
		BNaviPoint startPoint = new BNaviPoint(DemoApplication.myLocation.getLongitude(), DemoApplication.myLocation.getLatitude(), "我的位置", BNaviPoint.CoordinateType.BD09_MC);
		BNaviPoint endPoint = new BNaviPoint(latlng.longitude, latlng.latitude, "目的地", BNaviPoint.CoordinateType.BD09_MC);
		BaiduNaviManager.getInstance().launchNavigator(this, startPoint, // 起点（可指定坐标系）
				endPoint, // 终点（可指定坐标系）
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
				true, // 真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
				new OnStartNavigationListener()
				{ // 跳转监听

					@Override
					public void onJumpToNavigator(Bundle configParams)
					{
						Intent intent = new Intent(mContext, BNavigatorActivity.class);
						intent.putExtras(configParams);
						isNeedRefresh = false;
						startActivity(intent);
					}

					@Override
					public void onJumpToDownloader()
					{
					}
				});
	}

	private void initNavi()
	{
		// 初始化导航引擎
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener, new LBSAuthManagerListener()
		{
			@Override
			public void onAuthResult(int status, String msg)
			{
				String str = null;
				if (0 == status)
				{
					str = "key校验成功!";
				} else
				{
					str = "key校验失败, " + msg;
				}
			}
		});
		// 创建Demo视图
		// initViews();
	}

	private String getSdcardDir()
	{
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener()
	{
		public void engineInitSuccess()
		{
		}

		public void engineInitStart()
		{
		}

		public void engineInitFail()
		{
		}
	};

	public void initView()
	{
		// TODO Auto-generated method stub

		Btn_Callback = (Button)findViewById(R.id.bt_callback);
		Btn_Callback.setOnClickListener(this);
		Btn_Comfirm = (Button)findViewById(R.id.bt_comfirm);
		Btn_Comfirm.setOnClickListener(this);
		Linear_Step = (LinearLayout)findViewById(R.id.linear_step);
		if(OverAllData.getPostion()>0)
		{
			Btn_Callback.setVisibility(View.GONE);
//			Linear_Step.setVisibility(View.VISIBLE);
		}
		else
		{
			Btn_Callback.setVisibility(View.VISIBLE);
//			Linear_Step.setVisibility(View.GONE);
		}
		Linear_Step.setVisibility(View.VISIBLE);
		Btn_Left = (Button) findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		List_Content = (ListView) findViewById(R.id.list_content);
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		text_title.setText("交办详情");
		Btn_Right.setBackgroundResource(R.drawable.navi);
		gallery_photo = (UGallery)findViewById(R.id.gallery_photo);
		gallery_Linear = (LinearLayout) findViewById(R.id.grally_llinar);
		
		linear_Remark = (LinearLayout)findViewById(R.id.linear_remark);
		text_remark = (TextView)findViewById(R.id.text_remark);
		gallery_After = (UGallery)findViewById(R.id.gallery_after);
		gallery_Linearafter = (LinearLayout)findViewById(R.id.grally_llinarafter);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.bt_right:
			launchNavigator2();
			break;
		case R.id.bt_callback:
			DothisTaskActivity.res = res;
			isNeedRefresh = true;
			startActivity(new Intent(mContext, DothisTaskActivity.class));
			break;
		case R.id.bt_comfirm:
			toComfirm();
			break;
		}
	}

	
	private void toComfirm()
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				HashMap<String, Object> res = (HashMap<String, Object>) result;
				if (res.get("IsSuccess").toString().equals("true"))
				{
					TaskManagerActivity.isNeedrefresh = true;
					Toast.makeText(mContext, "处理成功", 0).show();
					getallotData();
				} else
				{
					Toast.makeText(mContext, res.get("Message").toString(), 0).show();
					return;
				}
				
				super.onPostExecute(result);
			}
		}.execute(new ParamData(ApiCode.AuditEventAllot, OverAllData.getLoginId()));
	}
	
	@Override
	public void onDestroy()
	{
		try
		{
			BitmapDrawable bitmapDrawable = (BitmapDrawable) img_Photo.getDrawable();
			if(bitmapDrawable!=null&&bitmapDrawable.getBitmap().isRecycled())
			{
				bitmapDrawable.getBitmap().recycle();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		super.onDestroy();
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_info, menu);
		return true;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result)
	{
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR)
		{
			Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
		}

		String strInfo = String.format("纬度：%f 经度：%f", result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result)
	{
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR)
		{
			Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
		}
		// Toast.makeText(mContext, result.getAddress(),
		// Toast.LENGTH_LONG).show();
		((TextView) findViewById(R.id.text_address)).setText(result.getAddress());

	}

}
