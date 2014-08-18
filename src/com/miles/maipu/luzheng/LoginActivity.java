package com.miles.maipu.luzheng;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.BaseMapObject;
import com.miles.maipu.util.FileUtils;
import com.miles.maipu.util.OverAllData;

public class LoginActivity extends AbsBaseActivity
{
	
	private EditText edit_account;
	private EditText edit_pwd;
	private Button Btn_login;
	private CheckBox check_remenber;
	
	public static SharedPreferences sp;
	public final String spuname = "uname";
	public final String sppwd = "pwd";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		 initView();
	}
	
	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button)findViewById(R.id.bt_left);
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
		edit_account = (EditText)findViewById(R.id.edit_account);
		edit_pwd = (EditText)findViewById(R.id.edit_pwd);
		Btn_login = (Button)findViewById(R.id.bt_login);
		check_remenber = (CheckBox)findViewById(R.id.check_remenber);
		Btn_login.setOnClickListener(this);
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		sp = getPreferences(MODE_PRIVATE);
		
//		if (!sp.getString(spuname, "").equals(""))
		{
			//已经记住
			edit_account.setText(sp.getString(spuname, ""));
			edit_pwd.setText(sp.getString(sppwd, ""));
			if(!sp.getString(spuname, "").equals(""))
			{
				check_remenber.setChecked(true);
			}
		}	
//		else
//		{
//			//没有记住
//		}
		
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		
		if(v==Btn_login)
		{
			String account = edit_account.getText().toString();
			String pwd = edit_pwd.getText().toString();
			if(account.equals("")||pwd.equals(""))
			{
				Toast.makeText(mContext, "请输入有效的账号和密码...", 0).show();
				return;
			}

			SharedPreferences.Editor editor = sp.edit();
			if(check_remenber.isChecked())
			{
				// 修改数据
				editor.putString(spuname, String.valueOf(account));
				editor.putString(sppwd, String.valueOf(pwd));
				
			}
			else
			{
				editor.putString(spuname, "");
				editor.putString(sppwd, "");
				
			}
			editor.commit();
			showprogressdialog();
			new SendDataTask()
			{

				@SuppressWarnings("unchecked")
				@Override
				protected void onPostExecute(Object result)
				{
					// TODO Auto-generated method stub
					hideProgressDlg();
					
					JPushInterface.init(getApplicationContext());
					BaseMapObject res = BaseMapObject.HashtoMyself((HashMap<String, Object>)result) ;
					FileUtils.setMapData2SD(res);
					if(result!=null&&res.get("ID")!=null)
					{
						OverAllData.SetLogininfo(res);
						startActivity(new Intent(mContext, IndexActivity.class));
//						goActivity(IndexActivity.class, "");
						LoginActivity.this.finish();
					}
					else
					{
						Toast.makeText(mContext, res==null?"输入信息有误或网络连接失败！":res.get("Message")+"", 0).show();
						return;
					}
					super.onPostExecute(result);
				}
				
			}.execute(new ParamData(ApiCode.login, account,pwd));
		}
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
