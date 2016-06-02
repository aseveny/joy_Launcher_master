package laucher.joy.com.JOYLauncher.utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import laucher.joy.com.JOYLauncher.entity.AdBean;

public class WeatherUtil
{
    final static String BAIDU_AK = "HkkXb3xBoUDHGBWkWSR7ZYgq";
    final static String BAIDU_WEATHER_API_HEADER = "http://api.map.baidu.com/telematics/v3/weather?";

    //final static String WEATHER_URL_HEADER = "http://www.weather.com.cn/data/cityinfo/";
    //final static String WEATHER_URL_HEADER ="http://www.weather.com.cn/adat/sk/";

  public static final String CITY_CODE = "city_code";
  public static final String VALID_TIME = "validtime";
  public static final String WEATHER_INFO = "weatherinfo";
  
  public static String getTopLeftInfo(String paramString)
  {
    String[] arrayOfString = paramString.split("  ");
    StringBuilder localStringBuilder = new StringBuilder();
    if (arrayOfString.length > 0) {
      return DateUtil.getWeekDate(Calendar.getInstance()) + "  " + arrayOfString[1];
    }
    return null;
  }

    private static  String getStringFromAssert(Context mContext, String fileName) {
        String content = null; //结果字符串
        try {
            InputStream is = mContext.getResources().getAssets().open(fileName); //打开文件
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream(); //实现了一个输出流
            while ((is.read(buffer)) != -1) {
                out.write(buffer); //将指定的字节写入此 byte 数组输出流
            }
            byte[] buff = out.toByteArray();//以 byte 数组的形式返回此输出流的当前内容
            out.close(); //关闭流
            is.close(); //关闭流
            content = new String(buff, "UTF-8"); //设置字符串编码
        }catch(Exception e){
            System.out.println("没有找到指定文件！");
        }
        return content;
    }


    public static ArrayList<String> getProvinceList(Context mContext){
        ArrayList<String> provinceList = new ArrayList<String>();
        String rawContent = getStringFromAssert(mContext, "WeatherData.json");
        try
        {
            JSONObject localJSONObject=new JSONObject(rawContent);
            JSONArray localJSONArray=localJSONObject.getJSONArray("城市代码");
            for(int i=0; i<localJSONArray.length(); i++)
            {
                JSONObject object = localJSONArray.getJSONObject(i);
                if(object!=null) {
                    provinceList.add(object.getString("省"));
                }
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return provinceList;
    }

    public static ArrayList<String> getProvinceCityList(Context mContext, String province){
        ArrayList<String> cityList = new ArrayList<String>();
        String rawContent = getStringFromAssert(mContext, "WeatherData.json");
        try
        {
            JSONObject localJSONObject=new JSONObject(rawContent);
            JSONArray localJSONArray=localJSONObject.getJSONArray("城市代码");
            for(int i=0; i<localJSONArray.length(); i++)
            {
                JSONObject object = localJSONArray.getJSONObject(i);
                if(object!=null) {
                    if( province.equals( object.getString("省") ) ){
                        JSONArray array = object.getJSONArray("市");
                        if(array!=null){
                            for(int j=0;j<array.length();j++){
                                cityList.add(array.getJSONObject(j).getString("市名"));
                                //System.out.println(array.getJSONObject(j).getString("市名"));
                            }
                        }
                    }
                }
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return cityList;
    }

/*    public static String getCityCode(Context mContext, String city){
        String rawContent = getStringFromAssert(mContext, "WeatherData.json");
        try
        {
            JSONObject localJSONObject=new JSONObject(rawContent);
            JSONArray localJSONArray=localJSONObject.getJSONArray("城市代码");
            for(int i=0; i<localJSONArray.length(); i++)
            {
                JSONObject object = localJSONArray.getJSONObject(i);
                if(object!=null) {
                    JSONArray array = object.getJSONArray("市");
                    if(array!=null){
                        for(int j=0;j<array.length();j++){
                            if( city.equals( array.getJSONObject(j).getString("市名")) ){
                               return array.getJSONObject(j).getString("编码");
                            }
                        }
                    }
                }
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return "";
    }*/
/*
  public static String getWeatherInfo(Context mContext, String cityCode)
  {
    String weatherUrlStr = WEATHER_URL_HEADER + cityCode + ".html";
      System.out.println("@@@@@@@@@@@@@@@@@@@@" + weatherUrlStr);
    String weatherInfoStr = validCity(mContext, cityCode);
    if (weatherInfoStr == null){
      String weatherContentStr = NetUtil.getContentString(weatherUrlStr);
      if (weatherContentStr != null) {
        try
        {
          JSONObject localJSONObject = new JSONObject(weatherContentStr).getJSONObject("weatherinfo");
          if (localJSONObject != null)
          {
              weatherInfoStr = localJSONObject.getString("city") + "  " + localJSONObject.getString("weather") + "  " +localJSONObject.getString("temp2") + "/"+ localJSONObject.getString("temp1");
              System.out.println(weatherInfoStr);
              SPUtils.setString(mContext, "validtime", DateUtil.setValidTime());
              SPUtils.setString(mContext, "city_code", cityCode);
              SPUtils.setString(mContext, "weatherinfo", weatherInfoStr);
              //System.out.println("@@@@@@@@@@@@@@@@@@@@" + weatherInfoStr);
          }
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
          return null;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    }
    return weatherInfoStr;
  }
  */

    public static String getWeatherInfo(Context mContext, String city)
    {
        String weatherUrlStr = BAIDU_WEATHER_API_HEADER + "location="+URLEncoder.encode(city)+"&output=json"+ "&ak="+BAIDU_AK;
        System.out.println("@@@@@@@@@@@@@@@@@@@@" + weatherUrlStr);
        String weatherInfoStr = validCity(mContext, city);
        if (weatherInfoStr == null){
            String weatherContentStr = NetUtil.getContentString(weatherUrlStr);
            if (weatherContentStr != null) {
                try
                {
                    JSONArray result = new JSONObject(weatherContentStr).getJSONArray("results");
                    if (result != null)
                    {
                        JSONArray weather_data = result.getJSONObject(0).getJSONArray("weather_data");
                        JSONObject today_weather = weather_data.getJSONObject(0);
                        weatherInfoStr = city+" "+today_weather.getString("weather") + "  " + today_weather.getString("wind") + "  " +today_weather.getString("temperature");
                        System.out.println(weatherInfoStr);
                        SPUtils.setString(mContext, "validtime", DateUtil.setValidTime());
                        SPUtils.setString(mContext, "city", city);
                        SPUtils.setString(mContext, "weatherinfo", weatherInfoStr);
                        System.out.println("@@@@@@@@@@@@@@@@@@@@" + weatherInfoStr);
                    }
                }
                catch (JSONException localJSONException)
                {
                    localJSONException.printStackTrace();
                    return null;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        }
        return weatherInfoStr;
    }
/*
  private static String validCity(Context mContext, String cityCode)
  {
        if ((cityCode.equals(SPUtils.getString(mContext, "city_code"))) && (Long.parseLong(SPUtils.getString(mContext, "validtime")) > System.currentTimeMillis())) {
            return SPUtils.getString(mContext, "weatherinfo");
        }
        return null;
  }
   */
    private static String validCity(Context mContext, String city)
    {
        if ((city.equals(SPUtils.getString(mContext, "city"))) && (Long.parseLong(SPUtils.getString(mContext, "validtime")) > System.currentTimeMillis())) {
            return SPUtils.getString(mContext, "weatherinfo");
        }
        return null;
    }

    public static String getProvinceByCity(Context mContext, String city){
        ArrayList<String> provinceList = null;
        provinceList = getProvinceList(mContext);

        for(int i=0; i<provinceList.size(); i++)
        {
            String province = provinceList.get(i);
            ArrayList<String>cityList = null;
            cityList = getProvinceCityList(mContext, province);
            for(int j=0; j<cityList.size(); j++)
            {
                if(city.equals(cityList.get(j)))
                    return province;
            }

        }
        return null;
    }

}
