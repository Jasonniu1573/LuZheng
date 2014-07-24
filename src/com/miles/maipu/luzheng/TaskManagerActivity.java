package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

public class TaskManagerActivity extends AbsBaseActivity
{
	
	private ListView list_Cotent;
	private List<HashMap<String,Object>> taskList = new Vector<HashMap<String,Object>>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		getData();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v==Btn_Right)
		{
			goActivity(CreatNormalActivity.class, "");
		}
		super.onClick(v);
	}
	
	private void getData()
	{
		showprogressdialog();
		new SendDataTask()
		{
			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				taskList = (List<HashMap<String, Object>>) result;
				list_Cotent.setAdapter(new NormalAdapter(mContext, taskList,AdapterCode.taskManger));
				list_Cotent.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						startActivity(new Intent(mContext, TaskInfoActivity.class).putExtra("id", taskList.get(arg2).get("ID")+""));
					}
				});
				super.onPostExecute(result);
			}
	
		}.execute(new ParamData(ApiCode.GetEventsByPersonID,OverAllData.getLoginId(),currentpage+"",pagesize+""));
	}
	
	
	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		super.initView();
		list_Cotent = (ListView)findViewById(R.id.list_content);
		text_title.setText("任务列表");
		if(OverAllData.getPatorlType()>0)
		{
			Btn_Right.setBackgroundResource(R.drawable.newnormal);
		}
		else
		{
			Btn_Right.setVisibility(View.INVISIBLE);
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_manager, menu);
		return true;
	}

}