package laucher.joy.com.JOYLauncher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import laucher.joy.com.JOYLauncher.R;
import laucher.joy.com.JOYLauncher.utils.SharedPreferenceUtil;
import laucher.joy.com.JOYLauncher.utils.WeatherUtil;

public class WeatherSettingActivity
        extends Activity {
    protected static final String TAG = "WeatherSettingActivity";
    //private static final String jsonFile = "WeatherData.json";
    String city;
    private Spinner city_spinner;
    Context context;
    private Button okButton;
    private Spinner province_spinner;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.setting_weather);
        this.province_spinner = ((Spinner) findViewById(R.id.province_spinner));
        ArrayAdapter localArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, WeatherUtil.getProvinceList(WeatherSettingActivity.this));
        this.province_spinner.setAdapter(localArrayAdapter);
        this.province_spinner.setOnItemSelectedListener(new SelectProvince());
        this.city_spinner = ((Spinner) findViewById(R.id.city_spinner));
        this.city_spinner.setOnItemSelectedListener(new SelectCity());

        city = SharedPreferenceUtil.getString("city", "深圳");
        String province = WeatherUtil.getProvinceByCity(WeatherSettingActivity.this, city);
        setSpinnerItemSelectedByValue(province_spinner, province);

        this.okButton = ((Button) findViewById(R.id.query_button));
        this.okButton.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                SharedPreferenceUtil.putString("city", city);
                Intent localIntent = new Intent();
                localIntent.putExtra("city", city);
                setResult(-1, localIntent);
                WeatherSettingActivity.this.finish();
            }
        });
    }

    class SelectCity
            implements OnItemSelectedListener {
        SelectCity() {
        }

        public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
            city = paramAdapterView.getItemAtPosition(paramInt).toString();
            //city_id = WeatherUtil.getCityCode(WeatherSettingActivity.this, str);
        }

        public void onNothingSelected(AdapterView<?> paramAdapterView) {
        }
    }

    class SelectProvince
            implements OnItemSelectedListener {
        SelectProvince() {
        }

        public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
            String str = province_spinner.getSelectedItem().toString();
            ArrayAdapter localArrayAdapter = new ArrayAdapter(WeatherSettingActivity.this, android.R.layout.simple_expandable_list_item_1, WeatherUtil.getProvinceCityList(WeatherSettingActivity.this, str));
            city_spinner.setAdapter(localArrayAdapter);
            setSpinnerItemSelectedByValue(city_spinner, city);
        }

        public void onNothingSelected(AdapterView<?> paramAdapterView) {
        }
    }

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        if(apsAdapter!=null) {
            int k = apsAdapter.getCount();
            for (int i = 0; i < k; i++) {
                if (value.equals(apsAdapter.getItem(i).toString())) {
                    spinner.setSelection(i, true);// 默认选中项
                    return;
                }
            }
            spinner.setSelection(0, true);// 默认选中项
        }
    }
}
