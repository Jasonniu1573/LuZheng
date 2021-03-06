package com.miles.maipu.util;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverAllData
{
    public static String TitleName = "路政巡查";
    public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.miles.maipu.luzheng" + File.separator;
    private static BaseMapObject loginInfo = null;
    public static Map<String, Object> Weathermap = null;
    public static String loginPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.miles.maipu.luzheng/login.dat" + File.separator;
    public static String logcat = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.miles.maipu.luzheng/log.txt" + File.separator;
    public static String IMEI = "";
    public static int width;
    public static int height;


    public static void SetLogininfo(BaseMapObject info)
    {
        loginInfo = info;
    }

    /**
     * 获取签到id
     */
    public static String getRecordId()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }

        return ((Map) loginInfo.get("PatorlRecord")).get("ID").toString();
    }

    public static String getLoginTime()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }

        return loginInfo.get("time") + "";
    }

    public static int[] getUndo()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return new int[]{0, 0, 0, 0};
            }
        }

        return new int[]{Integer.parseInt(loginInfo.get("RecordUndo").toString()), Integer.parseInt(loginInfo.get("EventUndo").toString()), Integer.parseInt(loginInfo.get("EvaluateUndo").toString()), Integer.parseInt(loginInfo.get("Notice").toString())};
    }


    public static List<HashMap<String, String>> getpatorLatLng()
    {
        ArrayList list = new ArrayList();

        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return null;
            }
        }

        List<HashMap<String, Object>> Roadlines = (ArrayList) (((Map) loginInfo.get("PatorlRecord")).get("Roadlines"));
        if (Roadlines.size() > 0)
        {
            for(HashMap<String, Object> map : Roadlines)
            {
                list.addAll((ArrayList)(map.get("LatitudeLongitudes")));
            }
        }
        return list;
    }


    public static boolean isNeedUploadEvent()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return true;
            }
        }

        return ((Map) loginInfo.get("Organization")).get("IsFirstDepartment").toString().equals("true") ? false : true;
    }

    public static boolean isFirstDepartment()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return true;
            }
        }

        return ((Map) loginInfo.get("Organization")).get("IsFirstDepartment").toString().equals("true") ? true : false;
    }


    /**获取签到类型
     * */
//	public static int getPatorlType()
//	{
//		if (loginInfo == null)
//		{
//			FileUtils.getMapData4SD();
//			if (loginInfo == null)
//			{
//				return 0;
//			}
//		}
//		try
//		{
//			return Integer.parseInt(((Map) loginInfo.get("PatorlRecord")).get("PatorlType").toString());
//		} catch (Exception e)
//		{
//			return 0;
//		}
//	}


    /**
     * 设置签到id
     */
    @SuppressWarnings("unchecked")
    public static boolean setRecordId(String id)
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return false;
            }
        }
        ((Map) loginInfo.get("PatorlRecord")).put("ID", id);
        FileUtils.setMapData2SD(loginInfo);
        return true;
    }


    /**
     * 获取登陆id
     */
    public static String getLoginId()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }
        return loginInfo.get("ID").toString();
    }

    /**
     * 获取姓名
     */
    public static String getLoginName()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }
        return loginInfo.get("Name").toString();
    }

    public static String getPostionName()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }
        return loginInfo.get("Role").toString();
    }

    public static String getOrgName()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }
        return ((Map) loginInfo.get("Organization")).get("Name").toString();
    }

    public static String getOrgId()
    {
        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }
        return ((Map) loginInfo.get("Organization")).get("ID").toString();
    }


    /**
     * 获取用户权限或者职位
     */
    public static int getPostion()
    {

        if (loginInfo == null)
        {

            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return 0;
            }
        }
        return Integer.parseInt(loginInfo.get("Postion").toString());
    }

    /**
     * 获取用户所属组织id
     */
    public static HashMap<String, Object> getMyOrganization()
    {

        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return null;
            }
        }
        return (HashMap<String, Object>) (loginInfo.get("Organization"));
    }

    /**
     * 获取用户所属组织级别
     */
    public static int getOrganizationLevel()
    {

        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return -1;
            }
        }
        return Integer.parseInt(((Map) loginInfo.get("Organization")).get("Level").toString());
    }


    /**
     * 获取用户所属组织id
     */
    public static String getOrganizationID()
    {

        if (loginInfo == null)
        {
            FileUtils.getMapData4SD();
            if (loginInfo == null)
            {
                return "";
            }
        }
        return ((Map) loginInfo.get("Organization")).get("ID").toString();
    }


    public static boolean isSign()
    {
        if (OverAllData.getRecordId().equals(""))
        {
            return false;
        } else
        {
            return true;
        }
    }


}
