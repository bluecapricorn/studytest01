package biz.mybaidumap.mybaidumap.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import biz.mybaidumap.mybaidumap.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ice Wu on 2017/9/16.
 */

public class LocationActivity extends AppCompatActivity {

    @BindView(R.id.address_text)
    TextView addressText;

    @BindView(R.id.location_view)
    MapView locationView;

    public LocationClient mLocationClient;
    private BaiduMap baiduMap;
    private BMapManager mapManager;
    

//    private BDAbstractLocationListener myListener = new MyLocationListener();

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LocationActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        baiduMap = locationView.getMap();
        baiduMap.setMyLocationEnabled(true);

        if (getIntent() != null) {
            Intent intent = getIntent();
            String city = intent.getStringExtra("city");
            String district = intent.getStringExtra("district");
            String streetName = intent.getStringExtra("street_name");
            String streetNumber = intent.getStringExtra("street_number");
            BDLocation location = new BDLocation();
            Address.Builder adBuilder = new Address.Builder();
            adBuilder.city(city);
            adBuilder.district(district);
            adBuilder.street(streetName + streetNumber);
            location.setAddr(adBuilder.build());
        } else {
            Toast.makeText(this, "Unknow Failed.", Toast.LENGTH_SHORT).show();
            finish();
        }

        List<String> permissionsList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissionsList.isEmpty()) {
            String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            //发起定位请求
            requestLocation();
        }
    }

    private void requestLocation() {
        initLoaciton();
        mLocationClient.start();
    }

    private void navigateTo(BDLocation location) {
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(update);

        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        locationBuilder.accuracy(location.getRadius());
        MyLocationData data = locationBuilder.build();
        baiduMap.setMyLocationData(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "相关权限被拒，无法获取位置。", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    //发起定位请求
                    requestLocation();

                } else {
                    Toast.makeText(this, "发生未知错误。", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                break;
            default:
        }
    }

    private void initLoaciton() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder builder = new StringBuilder();
            builder.append(String.valueOf(bdLocation.getLatitude()));
            builder.append(", ");
            builder.append(String.valueOf(bdLocation.getLongitude()));
            addressText.setText(builder);

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        locationView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
