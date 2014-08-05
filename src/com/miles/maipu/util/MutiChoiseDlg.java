package com.miles.maipu.util;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class MutiChoiseDlg
{
	private Context mContext;
	private List<HashMap<String, Object>> contactList;
	boolean[] selected;

	public MutiChoiseDlg(Context contex, List<HashMap<String, Object>> contacts)
	{
		this.mContext = contex;
		contactList = contacts;
		selected = new boolean[contactList.size()];
	}

	public String getDlg(final EditText edit)
	{
		Dialog dialog = null;
		Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("人员选择");
//		builder.setIcon(R.drawable.ic_launcher);
		DialogInterface.OnMultiChoiceClickListener mutiListener = new DialogInterface.OnMultiChoiceClickListener()
		{

			@Override
			public void onClick(DialogInterface dialogInterface, int which,
					boolean isChecked)
			{
				selected[which] = isChecked;
			}
		};
		String[] arrayc = new String[contactList.size()];
		for (int i = 0; i < contactList.size(); i++)
		{
			arrayc[i] = contactList.get(i).get("Name").toString();
		}
		builder.setMultiChoiceItems(arrayc, selected, mutiListener);
		DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int which)
			{
				String selectedStr = "";
				String selectedTag = "";
				for (int i = 0; i < selected.length; i++)
				{
					if (selected[i] == true)
					{
						selectedStr = selectedStr + contactList.get(i).get("Name").toString()+ "," ;
						selectedTag = selectedTag + contactList.get(i).get("ID").toString()+ "|" ;
					}
				}
				edit.setText(selectedStr.equals("")?"":selectedStr.subSequence(0, selectedStr.length()-1));
				edit.setTag(selectedStr.equals("")?"":selectedTag);
			}
		};
		builder.setPositiveButton("确定", btnListener);
		builder.setNegativeButton("取消", null);
		dialog = builder.create();
		dialog.show();
		return null;
	}
}
