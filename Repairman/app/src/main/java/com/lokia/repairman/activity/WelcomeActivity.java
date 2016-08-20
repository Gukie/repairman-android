package com.lokia.repairman.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyInfo;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.lokia.repairman.R;


public class WelcomeActivity extends Activity  implements RadarSearchListener {

    private MapView mMapView = null;

    private BaiduMap mBaiduMap = null;

    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    boolean isFirstLoc = true; // 是否首次定位

    private LatLng pt = null;
    private int pageIndex = 0;
    private RadarSearchManager mManager;
    private BitmapDescriptor markerIcon = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_welcome);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

        showMyLocation();


    }

    private void showMyLocation() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        setLocationOption();
        mLocClient.start();
    }

    /**
     * 显示附近的维修点
     */
    private void showNearByRepairMen() {

        mManager = RadarSearchManager.getInstance();
        // upload my radar info
        uploadMyRadarInfo();
        // search nearby
        searchNearby();


    }

    private void searchNearby() {
        //构造请求参数，其中centerPt是自己的位置坐标
        RadarNearbySearchOption option = new RadarNearbySearchOption().centerPt(pt).pageNum(pageIndex).radius(2000);
        //发起查询请求
        mManager.nearbyInfoRequest(option);
    }

    private void uploadMyRadarInfo() {
        mManager.addNearbyInfoListener(this);
        //周边雷达设置用户身份标识，id为空默认是设备标识
        mManager.setUserID("");
        //上传位置
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = "备注";
        info.pt = pt;
        mManager.uploadInfoRequest(info);
    }


    @Override
    public void onGetNearbyInfoList(RadarNearbyResult result,
                                    RadarSearchError error) {
        // TODO Auto-generated method stub
        if (error == RadarSearchError.RADAR_NO_ERROR) {
            Log.e("repaireman", "onGetNearbyInfoList - 查询周边成功");
            parseResultToMap(result);
        } else {
            //获取失败
            Log.e("repaireman", "onGetNearbyInfoList - 查询周边失败");
        }
    }

    private void parseResultToMap(RadarNearbyResult res) {

        mBaiduMap.clear();
        if (res != null && res.infoList != null && res.infoList.size() > 0) {

            String myDeviceId = SysOSUtil.getDeviceID();
            for(RadarNearbyInfo nearbyInfo: res.infoList){

                String userID = nearbyInfo.userID;
                if(userID.equals(myDeviceId)){
                    System.out.println("*****************GUSHU: same device, userId is: "+userID);
                    continue;
                }

                MarkerOptions option = new MarkerOptions().icon(markerIcon).position(nearbyInfo.pt);
                Bundle des = new Bundle();
                if (nearbyInfo.comments == null || nearbyInfo.comments.equals("")) {
                    des.putString("des", "没有备注");
                } else {
                    des.putString("des", nearbyInfo.comments);
                }
                option.extraInfo(des);
                mBaiduMap.addOverlay(option);
            }
        }
    }

    @Override
    public void onGetUploadState(RadarSearchError radarSearchError) {
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            // 上传成功
//           Toast.makeText(WelcomeActivity.this, "单次上传位置成功", Toast.LENGTH_LONG)
//                        .show();
            Log.e("repaireman", "onGetUploadState - 单次上传位置成功");
        } else {
            // 上传失败
//            Toast.makeText(WelcomeActivity.this, "单次上传位置失败", Toast.LENGTH_LONG)
//                        .show();
            Log.e("repaireman", "onGetUploadState - 单次上传位置失败");
        }
    }

    @Override
    public void onGetClearInfoState(RadarSearchError radarSearchError) {
        if(mManager!=null){
            mManager.clearUserInfo();
        }
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            // 清除成功
            Log.e("repaireman", "onGetClearInfoState - 清除位置成功");
        } else {
            // 清除失败
            Log.e("repaireman", "onGetClearInfoState - 清除位置失败");
        }
    }


    private void setLocationOption() {
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        mLocClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMapInfo();
        clearMapRadarInfo();
    }

    private void clearMapInfo() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView = null;
    }

    private void clearMapRadarInfo() {
        //移除监听
        mManager.removeNearbyInfoListener(this);
        //清除用户信息
        mManager.clearUserInfo();
        //释放资源
        mManager.destroy();
        mManager = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            pt = new LatLng(location.getLatitude(), location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(pt).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_geo);
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            showNearByRepairMen();

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


}
