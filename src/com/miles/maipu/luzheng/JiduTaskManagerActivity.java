package com.miles.maipu.luzheng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.WebImageBuilder;

public class JiduTaskManagerActivity extends AbsBaseActivity implements OnScrollListener
{
	private ListView list_Cotent;
	private List<HashMap<String, Object>> taskList = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> taskorgList = new Vector<HashMap<String, Object>>();
	private NormalAdapter adapter;
	public static boolean isNeedrefresh = false;
	private Button Btn_More;
	private LinearLayout linear_more;
	private TextView text_All;
	private TextView text_zishu;
	private TextView text_wujin;
	private TextView text_jintan;
	private TextView text_liyang;
	private LinearLayout linear_selct;
	private TextView text_upload;
	private TextView text_fenpei;
	private int type = 0;
	private boolean isorg;
	private String oid;
	private String status;
	private boolean isAllot = true;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jidutask_manager);
		isorg = getIntent().getBooleanExtra("isorg", false);
		status = getIntent().getStringExtra("status");
		oid = getIntent().getStringExtra("id");
		if (status != null)
		{
			type = Integer.parseInt(status);
		}
		initView();

		isNeedrefresh = true;
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		if (isorg)
		{
			Btn_Right.setVisibility(View.INVISIBLE);
			Btn_More.setVisibility(View.INVISIBLE);
		}
		if (isNeedrefresh)
		{
			currentpage = 1;
			taskList.clear();
			getData(true);
		}
		isNeedrefresh = false;
		super.onResume();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub

		switch (v.getId())
		{
		case R.id.bt_right:
			if (!isAllot)
			{
				Toast.makeText(mContext, "当前不允许新增检查案件", Toast.LENGTH_SHORT).show();
				return;
			}
			if (OverAllData.isSign() || OverAllData.getPostion() > 1) // 中队人员必须签到才能
			{
				isNeedrefresh = true;
				startActivity(new Intent(mContext, CreatTaskActivity.class).putExtra("type", "0").putExtra("isjidu", true));
			}
			else
			{
				Toast.makeText(mContext, "请签到后再使用本功能...", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.text_upload:
			isNeedrefresh = true;
			startActivity(new Intent(mContext, CreatTaskActivity.class).putExtra("type", "0"));
			linear_selct.setVisibility(View.GONE);
			break;
		case R.id.text_fenpei:
			isNeedrefresh = true;
			startActivity(new Intent(mContext, CreatTaskActivity.class).putExtra("type", "1"));
			linear_selct.setVisibility(View.GONE);
			break;
		case R.id.bt_more:
			if (linear_more.getVisibility() == View.GONE)
			{
				Btn_More.setBackgroundResource(R.drawable.btmoreup);
				linear_more.setVisibility(View.VISIBLE);
			}
			else
			{
				Btn_More.setBackgroundResource(R.drawable.btmore);
				linear_more.setVisibility(View.GONE);
			}
			break;
		case R.id.text_all:
		case R.id.text_zhishu:
		case R.id.text_wujin:
		case R.id.text_jintan:
		case R.id.text_liyang:
			changeMoreText(v);
			linear_more.setVisibility(View.GONE);
			Btn_More.setBackgroundResource(R.drawable.btmore);
			break;
		}
		super.onClick(v);
	}

	private void changeMoreText(View v)
	{
		text_All.setTextColor(getResources().getColor(R.color.graytext));
		text_zishu.setTextColor(getResources().getColor(R.color.graytext));
		text_wujin.setTextColor(getResources().getColor(R.color.graytext));
		text_jintan.setTextColor(getResources().getColor(R.color.graytext));
		text_liyang.setTextColor(getResources().getColor(R.color.graytext));
		((TextView) v).setTextColor(getResources().getColor(R.color.black));
		currentpage = 1;
		moreView.setVisibility(View.GONE);
		taskList.clear();
		switch (v.getId())
		{
		case R.id.text_all:
			changeListContent(-1);
			type = 0;
			break;
		case R.id.text_zhishu:
			type = 1;
			changeListContent(0);
			break;
		case R.id.text_wujin:
			changeListContent(3);
			type = 2;
			break;
		case R.id.text_jintan:
			changeListContent(5);
			type = 3;
			break;
		case R.id.text_liyang:
			changeListContent(6);
			type = 4;
			break;
		}

	}

	private void getData(boolean isshowpro)
	{
		if (currentpage == 1)// 非加载更多，刷新
		{
			taskorgList.clear();
			taskList.clear();
		}
		if (isshowpro) showprogressdialog();

		ParamData pdata = null;
		pdata = new ParamData(ApiCode.GetAllEvaluateEvent, OverAllData.getLoginId());

		new SendDataTask()
		{
			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				// taskList = ;M、
				Map m = (Map) result;
				isAllot = !Boolean.parseBoolean(m.get("IsAllot").toString());
				refreshList((ArrayList) (m.get("list")));
				super.onPostExecute(result);
			}

		}.execute(pdata);
	}

	private void changeListContent(int area)
	{
		taskList.clear();
		for (HashMap<String, Object> map : taskorgList)
		{
			if (Integer.parseInt(map.get("Area").toString()) == area || area == -1)
			{
				taskList.addAll(((ArrayList) map.get("List")));
			}
		}

		adapter.notifyDataSetChanged();

	}

	private void refreshList(List<HashMap<String, Object>> data)
	{
		if (data == null)
		{
			Toast.makeText(mContext, "未取得任何数据...", Toast.LENGTH_SHORT).show();
			list_Cotent.removeFooterView(moreView);
			list_Cotent.setOnScrollListener(null);
			return;
		}
		taskorgList.addAll(data);
		for (HashMap<String, Object> map : taskorgList)
		{
			taskList.addAll(((ArrayList) map.get("List")));
		}

		count = taskList.size();
		if (data.size() == pagesize)
		{
			list_Cotent.removeFooterView(moreView);
			list_Cotent.addFooterView(moreView); // 添加底部view，一定要在setAdapter之前添加，否则会报错。
			list_Cotent.setOnScrollListener(this);
		}
		else
		{
			list_Cotent.removeFooterView(moreView);
			list_Cotent.setOnScrollListener(null);
		}

		new Thread()
		{
			@Override
			public void run()
			{
				for (int i = 0; i < taskList.size(); i++)
				{
					Map<String, Object> buss = taskList.get(i);
					if (buss.get("bitmap") == null)
					{
						String path = buss.get("Picture").toString().split("\\|")[0];
						buss.put("bitmap", new WebImageBuilder().returnBitMap(NetApiUtil.thumbImgBaseUrl + path, WebImageBuilder.MINSIZE));

						// buss.put("bitmap", new SoftReference<Bitmap>(new
						// WebImageBuilder().returnBitMap(NetApiUtil.thumbImgBaseUrl
						// + path, WebImageBuilder.MINSIZE)));
					}
				}
				handler.sendEmptyMessage(1);
			}
		}.start();

		if (currentpage > 1 && adapter != null)
		{
			adapter.notifyDataSetChanged();
			return;
		}

		adapter = new NormalAdapter(mContext, taskList, AdapterCode.taskjudumannger);
		list_Cotent.setAdapter(adapter);
		list_Cotent.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				isNeedrefresh = false;
				startActivity(new Intent(mContext, JiduTaskInfoActivity.class).putExtra("id", taskList.get(arg2).get("ID") + ""));
			}
		});
		list_Cotent.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
		{
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
			{
				// TODO Auto-generated method stub
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
				int ListItem = info.position;
				HashMap<String, Object> item = taskList.get(ListItem);
				menu.setHeaderTitle("交办列表");
				menu.add(0, 0, 0, "查看详情");

				switch (Integer.parseInt(item.get("IsMine").toString()))
				{
				case 0:
					menu.add(0, 1, 1, "案件上报");
					break;
				case 1:
					menu.add(0, 1, 1, "案件上报");
					menu.add(0, 2, 2, "案件交办");
					break;
				case 2:
					menu.add(0, 2, 2, "案件交办");
					break;
				case 3:
					menu.add(0, 2, 2, "案件交办");
					// menu.add(0, 3, 3, "案件反馈");
					break;
				case 4:
					// menu.add(0, 3, 3, "案件反馈");
					break;
				}
				// if(OverAllData.getPostion()>0&&item.get("ReceiverID").toString().equals(OverAllData.getLoginId())&&item.get("State").toString().equals("未交办")&&item.get("IsMine").toString().equals("true"))
				// {
				//
				// }
				menu.add(0, 5, 5, "取消");
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		for (HashMap<String, Object> item : taskList)
		{
			if (item.get("bitmap") != null)
			{
				Bitmap bitmap = ((Bitmap) item.get("bitmap"));
				if (bitmap != null && !bitmap.isRecycled())
				{
					// 回收并且置为null
					bitmap.recycle();
					bitmap = null;
				}

			}
		}
		System.gc();
		super.onDestroy();
	}

	// 根据自己机构的人员 0，下属，1上属
	private void getSubordPerson(String upordown)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				personlist.clear();

				personlist = (List<HashMap<String, Object>>) result;

				String[] arraystr = new String[personlist.size()];
				for (int i = 0; i < personlist.size(); i++)
				{
					arraystr[i] = personlist.get(i).get("Name") + "";
				}
				sp_Person.setAdapter(new MySpinnerAdapter(mContext, arraystr));

				hideProgressDlg();

				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetSubordinate, OverAllData.getLoginId(), upordown));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int ListItem = info.position;
		switch (item.getItemId())
		{
		case 0:
			isNeedrefresh = false;
			startActivity(new Intent(mContext, JiduTaskInfoActivity.class).putExtra("id", taskList.get(ListItem).get("ID") + ""));
			break;
		case 1: // 上报
			showFenPei(taskList.get(ListItem).get("ID") + "", "案件上报", "0");
			break;
		case 2: // 交办
			showFenPei(taskList.get(ListItem).get("ID") + "", "案件交办", "1");
			break;
		}
		return super.onContextItemSelected(item);
	}

	private Spinner sp_Organization;
	private Spinner sp_Person;
	private List<HashMap<String, Object>> organizalist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> personlist = new Vector<HashMap<String, Object>>();
	private AlertDialog builder;
	private EditText edit_jiaoban;

	private void showFenPei(final String tid, final String dlgtitle, final String type)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dlg_fenpeitask, null);
				sp_Organization = (Spinner) layout.findViewById(R.id.sp_organi);
				sp_Person = (Spinner) layout.findViewById(R.id.sp_person);
				LinearLayout linear = (LinearLayout) layout.findViewById(R.id.linear_jiaoban);
				edit_jiaoban = (EditText) layout.findViewById(R.id.edit_jiaoban);
				linear.setVisibility(View.VISIBLE);
				if (dlgtitle.equals("案件上报"))
				{
					linear.setVisibility(View.GONE);
				}
				else
				{
					linear.setVisibility(View.VISIBLE);
				}
				TextView title = new TextView(mContext);
				title.setText(dlgtitle);
				builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle(dlgtitle).setPositiveButton("确定", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						String jiaoban = edit_jiaoban.getText().toString();
						jiaoban = jiaoban.equals("") ? "null" : jiaoban;
						HashMap<String, Object> person = personlist.get(sp_Person.getSelectedItemPosition());
						FenPeiToAlloted(person.get("ID") + "", tid, jiaoban, type);
						// Toast.makeText(mContext, tid, 0).show();
					}
				}).setNegativeButton("取消", null).show();

				hideProgressDlg();
				organizalist = (List<HashMap<String, Object>>) result;
				// organizalist.add(0,
				// OverAllData.getMyOrganization());//添加同一级机构，同级机构间可以分配给下属
				// String[] arraystr = new String[organizalist.size()];
				// for (int i = 0; i < organizalist.size(); i++)
				// {
				// arraystr[i] = organizalist.get(i).get("Name") + "";
				// }
				//

				String[] arraystr = null;
				if (OverAllData.getPostion() == 1 && dlgtitle.equals("案件交办")) // 中队长可以分配给巡查员
				{
					organizalist.add(0, OverAllData.getMyOrganization());// 添加同一级机构，同级机构间可以分配给下属
				}

				arraystr = new String[organizalist.size()];
				for (int i = 0; i < organizalist.size(); i++)
				{
					arraystr[i] = organizalist.get(i).get("Name") + "";
				}

				sp_Organization.setAdapter(new MySpinnerAdapter(mContext, arraystr));

				sp_Organization.setOnItemSelectedListener(new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						// 获取机构下人员
						if (OverAllData.getPostion() == 1 && dlgtitle.equals("案件交办")) // 中队长分配给下属人员
						{
							getSubordPerson("0");
						}
						else
						// 获取对应结构的人员
						{
							getPerson(organizalist.get(arg2).get("ID").toString());
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub

					}
				});
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetOrganizationUpOrDown, OverAllData.getLoginId(), dlgtitle.equals("案件上报") ? "1" : "0"));// 0，获取下属机构
	}

	private void FenPeiToAlloted(String pid, String tid, String option, String type)
	{
		Map<String, Object> PatorlRecord = new HashMap<String, Object>();
		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put("ID", pid);
		PatorlRecord.put("ReceivePersonInformation", p1);
		Map<String, Object> p2 = new HashMap<String, Object>();
		p2.put("ID", tid);
		PatorlRecord.put("EvaluateEvent", p2);
		PatorlRecord.put("Opinion", option);

		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				currentpage = 1;

				getData(true);
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.AddEvaluateReceive, JSONUtil.toJson(PatorlRecord)));
	}

	// 根据机构获取机构下人员
	private void getPerson(String oid)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				personlist.clear();

				personlist = (List<HashMap<String, Object>>) result;

				String[] arraystr = new String[personlist.size()];
				for (int i = 0; i < personlist.size(); i++)
				{
					arraystr[i] = personlist.get(i).get("Name") + "";
				}
				sp_Person.setAdapter(new MySpinnerAdapter(mContext, arraystr));

				hideProgressDlg();

				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetPersonInformationByOrganization, oid));
	}

	public void initView()
	{
		// TODO Auto-generated method stub
		list_Cotent = (ListView) findViewById(R.id.list_content);

		Btn_Left = (Button) findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		Btn_More = (Button) findViewById(R.id.bt_more);
		Btn_More.setVisibility(View.VISIBLE);
		Btn_More.setBackgroundResource(R.drawable.btmore);
		linear_more = (LinearLayout) findViewById(R.id.linear_more);
		linear_selct = (LinearLayout) findViewById(R.id.linear_selecttype);
		Btn_More.setOnClickListener(this);

		text_All = (TextView) findViewById(R.id.text_all);
		text_zishu = (TextView) findViewById(R.id.text_zhishu);
		text_wujin = (TextView) findViewById(R.id.text_wujin);
		text_jintan = (TextView) findViewById(R.id.text_jintan);
		text_upload = (TextView) findViewById(R.id.text_upload);
		text_fenpei = (TextView) findViewById(R.id.text_fenpei);
		text_liyang = (TextView) findViewById(R.id.text_liyang);

		text_All.setOnClickListener(this);
		text_zishu.setOnClickListener(this);
		text_wujin.setOnClickListener(this);
		text_jintan.setOnClickListener(this);
		text_upload.setOnClickListener(this);
		text_fenpei.setOnClickListener(this);
		text_liyang.setOnClickListener(this);
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		Btn_Right.setBackgroundResource(R.drawable.newnormal);
		// if(OverAllData.getPostion()>1)
		// {
		//
		// }
		// else
		// {
		// Btn_Right.setVisibility(View.GONE);
		// }
		text_title.setText("季度检查");

		if (OverAllData.getPostion() == 100 || OverAllData.isFirstDepartment())
		{
			Btn_Right.setVisibility(View.VISIBLE);
		}
		else
		{
			Btn_Right.setVisibility(View.GONE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_manager, menu);
		return true;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// TODO Auto-generated method stub
		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE)
		{
			// Log.i(TAG, "拉到最底部");
			moreView.setVisibility(View.VISIBLE);
			// getData(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

}
