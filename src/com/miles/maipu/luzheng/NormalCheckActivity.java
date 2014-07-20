package com.miles.maipu.luzheng;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.miles.maipu.util.AbsBaseActivity;

public class NormalCheckActivity extends AbsBaseActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_check);
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




	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		super.initView();
		Btn_Right.setBackgroundResource(R.drawable.newnormal);
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.normal_check, menu);
		return true;
	}

}
