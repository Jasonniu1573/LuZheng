package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;

public class IndexActivity extends AbsBaseActivity
	{

		ImageView img_Singin = null;
		ImageView img_NormalCheck = null;
		ImageView img_TaskManager = null;

		ImageView img_MapView = null;
		ImageView img_Upload = null;
		ImageView img_Premiss = null;

		ImageView img_Notice = null;
		ImageView img_Law = null;
		ImageView img_Setting = null;
		ImageView img_Mycenter = null;

		private TextView tv_xuncha;
		private TextView tv_task;
		private TextView tv_jidu;
		private TextView tv_notice;

		private int[] Undo = null;
		private TextView text_NormalName = null;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_index);
			initView();

			new getweather().execute("");
		}

		private boolean isSign()
		{
			if (OverAllData.getRecordId().equals(""))
			{
				return false;
			} else
			{
				return true;
			}
		}

		// protected void Exitdialog()
		// {
		// AlertDialog.Builder builder = new Builder(mContext);
		// builder.setMessage("确定注销本次巡查吗？");
		//
		// builder.setTitle("提示");
		//
		// builder.setPositiveButton("是", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// dialog.dismiss();
		//
		// }
		// }).setNegativeButton("否", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();
		// }
		//

		// @Override
		// public boolean onKeyDown(int keyCode, KeyEvent event)
		// {
		// // TODO Auto-generated method stub
		// if (keyCode == KeyEvent.KEYCODE_BACK) // Back键实现Home键返回，activity后台压栈
		// {
		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
		// intent.addCategory(Intent.CATEGORY_HOME);
		// this.startActivity(intent);
		// return true;
		// }
		// return super.onKeyDown(keyCode, event);
		// }

		@Override
		protected void onDestroy()
		{
			// TODO Auto-generated method stub
			// 服务永不停止
			// SimpleDateFormat df = new SimpleDateFormat("HHmmss");//设置日期格式
			// if(Integer.parseInt(df.format(new Date()))>173000)
			// {
			// mContext.stopService(new Intent(mContext,
			// UploadLatLngService.class));
			// }
			// System.exit(0);
			super.onDestroy();
		}

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			super.onClick(v);
			if (OverAllData.getPostion() == 100 && v == img_Notice)
			{
				startActivity(new Intent(mContext, JiduTaskManagerActivity.class));
				return;
			} else if (OverAllData.getPostion() == 100 && v != img_Notice)
			{
				Toast.makeText(mContext, "此账号暂未开通本功能...", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent inten = new Intent();
			switch (v.getId())
			{

			case R.id.img_singin:
				if (isSign())
				{
					Toast.makeText(mContext, "您今天已经签到，无须重复签到...", Toast.LENGTH_SHORT).show();
					return;
				} else
				{
					inten.setClass(mContext, SinginActivity.class);

					// goActivity(SinginActivity.class, "");
				}

				break;
			case R.id.img_normalcheck:
				if (isSign())
				{
					inten.setClass(mContext, NormalCheckActivity.class);
				} else
				{
					Toast.makeText(mContext, "请签到后再使用本功能...", Toast.LENGTH_SHORT).show();
					return;
				}
				// goActivity(NormalCheckActivity.class, "");
				break;
			case R.id.img_taskmanager:
				// if (isSign()||OverAllData.getPostion()>0)
				// {
				inten.setClass(mContext, TaskManagerActivity.class);
				// } else
				// // {
				// Toast.makeText(mContext, "请签到后再使用本功能...", 0).show();
				// return;
				// }
				// goActivity(TaskManagerActivity.class, "");
				break;
			case R.id.img_mapview:
				// if (isSign()||OverAllData.getPostion()>0)
				// {
				inten.setClass(mContext, MapViewActivity.class);
				// } else
				// {
				// Toast.makeText(mContext, "请签到后再使用本功能...", 0).show();
				// return;
				// }
				// goActivity(MapViewActivity.class, "");
				break;
			case R.id.img_upload:
				if (isSign() || OverAllData.getPostion() > 0)
				{
					if (OverAllData.getPostion() == 0)
					{
						inten.setClass(mContext, UplaodEventActivity.class);
					} else
					{
						inten.setClass(mContext, EventListActivity.class);
					}
				} else
				{
					Toast.makeText(mContext, "请签到后再使用本功能...", Toast.LENGTH_SHORT).show();
					return;
				}
				// goActivity(TaskManagerActivity.class, "");
				break;
			case R.id.img_premiss:
				// if (isSign())
				// {
				inten.setClass(mContext, PromissActivity.class);
				// } else
				// {
				// Toast.makeText(mContext, "请签到后再使用本功能...", 0).show();
				// return;
				// }
				// goActivity(TaskManagerActivity.class, "");
				break;
			case R.id.img_notice:
				inten.setClass(mContext, JiduTaskManagerActivity.class);
				break;
			case R.id.img_law:
				inten.setClass(mContext, LawActivity.class);
				break;
			case R.id.img_setting:
				inten.setClass(mContext, SettingActivity.class);
				inten.putExtra("unread", Undo[3]);
				// goActivity(TaskManagerActivity.class, "");
				break;
			case R.id.img_center:
				inten.setClass(mContext, MyCenterActivity.class);
				break;
			}
			mContext.startActivity(inten);
		}

		@Override
		protected void onResume()
		{
			if (OverAllData.getPostion() != 100)
			{
				getUndo();
			}
			super.onResume();
		}

		private void getUndo()
		{
			new SendDataTask()
			{
				@Override
				protected void onPostExecute(Object o)
				{
					HashMap<String, Object> res = (HashMap<String, Object>) o;
					Undo = new int[]
					{ Integer.parseInt(res.get("RecordUndo").toString()), Integer.parseInt(res.get("EventUndo").toString()), Integer.parseInt(res.get("EvaluateUndo").toString()),
							Integer.parseInt(res.get("Notice").toString()) };
					if (Undo[0] == 0)
					{
						tv_xuncha.setVisibility(View.INVISIBLE);
					} else
					{
						tv_xuncha.setVisibility(View.VISIBLE);
						tv_xuncha.setText(Undo[0] + "");
					}

					if (Undo[1] == 0)
					{
						tv_task.setVisibility(View.INVISIBLE);
					} else
					{
						tv_task.setVisibility(View.VISIBLE);
						tv_task.setText(Undo[1] + "");
					}

					if (Undo[2] == 0)
					{
						tv_jidu.setVisibility(View.INVISIBLE);
					} else
					{
						tv_jidu.setVisibility(View.VISIBLE);
						tv_jidu.setText(Undo[2] + "");
					}

					if (Undo[3] == 0)
					{
						tv_notice.setVisibility(View.INVISIBLE);
					} else
					{
						tv_notice.setVisibility(View.VISIBLE);
						tv_notice.setText(Undo[3] + "");
					}

					super.onPostExecute(o);
				}
			}.execute(new ParamData(ApiCode.GetBubbleCount, OverAllData.getLoginId()));
		}

		public void initView()
		{
			// TODO Auto-generated method stub
			Btn_Left = (Button) findViewById(R.id.bt_left);
			Btn_Right = (Button) findViewById(R.id.bt_right);
			text_title = (TextView) findViewById(R.id.title_text);
			List_Content = (ListView) findViewById(R.id.list_content);
			tv_xuncha = (TextView) findViewById(R.id.text_xunchaunread);
			tv_task = (TextView) findViewById(R.id.text_taskunread);
			tv_jidu = (TextView) findViewById(R.id.text_jiduunread);
			tv_notice = (TextView) findViewById(R.id.text_noticeunread);
			OverAllData.getpatorLatLng();
			if (Btn_Left != null)
			{
				Btn_Left.setOnClickListener(this);
			}
			if (Btn_Right != null)
			{
				Btn_Right.setOnClickListener(this);
			}
			img_Singin = (ImageView) findViewById(R.id.img_singin);
			img_Singin.setOnClickListener(this);
			img_NormalCheck = (ImageView) findViewById(R.id.img_normalcheck);
			img_NormalCheck.setOnClickListener(this);
			img_TaskManager = (ImageView) findViewById(R.id.img_taskmanager);
			img_TaskManager.setOnClickListener(this);
			img_MapView = (ImageView) findViewById(R.id.img_mapview);
			img_Upload = (ImageView) findViewById(R.id.img_upload);
			img_Premiss = (ImageView) findViewById(R.id.img_premiss);
			img_Notice = (ImageView) findViewById(R.id.img_notice);
			img_Law = (ImageView) findViewById(R.id.img_law);
			img_Setting = (ImageView) findViewById(R.id.img_setting);
			img_Mycenter = (ImageView) findViewById(R.id.img_center);
			text_NormalName = (TextView) findViewById(R.id.text_xunchaname);

			img_MapView.setOnClickListener(this);
			img_Upload.setOnClickListener(this);
			img_Premiss.setOnClickListener(this);
			img_Notice.setOnClickListener(this);
			img_Law.setOnClickListener(this);
			img_Setting.setOnClickListener(this);
			img_Mycenter.setOnClickListener(this);
			if (OverAllData.getPostion() > 1)
			{
				text_NormalName.setText("监管巡查");
			} else
			{
				text_NormalName.setText("路政巡查");
			}
		}

		class getweather extends AsyncTask<String, String, String>
			{
				@Override
				protected String doInBackground(String... params)
				{
					// TODO Auto-generated method stub
					// HttpGetUtil.httpUrlConnection(ApiCode.login,"admin","admin");
					// BaseMapObject m = new BaseMapObject();
					// m.put("PatorlRecord", "1");
					// m.put("PatorlItem", "1");
					// m.put("RoadLine", "1");
					// m.put("Mark", "1");
					// m.put("HandleDescription", "fasgas");
					// m.put("AfterPicture", "fasfds");
					// m.put("Lane", "1");
					// m.put("LatitudeLongitude", "119.2344,31.234");

					// HttpPostUtil.httpUrlConnection(JSONUtil.toJson(m));
					return NetApiUtil.GetWeather();
				}

				@SuppressWarnings("unchecked")
				@SuppressLint("ShowToast")
				@Override
				protected void onPostExecute(String result)
				{
					// TODO Auto-generated method stub
					try
					{
						if (result.equals("false"))
						{
							Toast.makeText(mContext, "访问失败...", Toast.LENGTH_SHORT).show();
							IndexActivity.this.finish();
							return;
						}
						OverAllData.Weathermap = (Map) JSONUtil.getMapFromJson(result).get("weatherinfo");
						if (OverAllData.Weathermap != null)
						{
							((TextView) findViewById(R.id.text_weather)).setText(OverAllData.Weathermap.get("weather").toString());
							((TextView) findViewById(R.id.text_temp)).setText(OverAllData.Weathermap.get("temp1").toString() + "~" + OverAllData.Weathermap.get("temp2").toString());
							int imgid = R.drawable.a00;
							imgid += Integer.parseInt(OverAllData.Weathermap.get("img2").toString().substring(1, 2));
							((ImageView) findViewById(R.id.image_weather)).setImageResource(imgid);
						}
					} catch (Exception e)
					{
						e.toString();
					}
					super.onPostExecute(result);
				}
			}

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			// Inflate the menu; this adds items to the action bar if it is
			// present.
			getMenuInflater().inflate(R.menu.index, menu);
			return true;
		}

	}
