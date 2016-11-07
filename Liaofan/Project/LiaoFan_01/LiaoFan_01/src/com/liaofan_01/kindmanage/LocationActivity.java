package com.liaofan_01.kindmanage;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Text;
import com.amap.api.maps2d.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.liaofan_01.R;

public class LocationActivity extends Activity implements LocationSource,
		AMapLocationListener, OnCheckedChangeListener, OnMarkerDragListener,
		OnMapLoadedListener, OnMarkerClickListener, OnInfoWindowClickListener,
		InfoWindowAdapter, OnMapClickListener, OnMapLongClickListener,
		OnGeocodeSearchListener {

	private Marker marker2, marker;// 有跳动效果的marker对象
	boolean selfdef = false;
	private LatLng latlng = new LatLng(36.061, 103.834);
	LatLng mTarget = null;
	String country, province, city, street, locale, area;
	private MarkerOptions markerOption;
	private TextView markerText;
	JSONObject json;

	Button back = null, reset = null, selfDef = null;
	private MapView mapView;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private RadioGroup mGPSModeGroup;

	TextView log2 = null;
	TextView lat2 = null;
	LatLng postLatLng = null;

	TextView mTapTextView;
	LinearLayout bottomLinear;
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private LatLonPoint latLonPoint = new LatLonPoint(40.003662, 116.465271);;
	private String addressName;
	RegeocodeAddress address;
	private Marker geoMarker;
	private Marker regeoMarker;
	MarkerOptions mo = new MarkerOptions();
	int status = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_location);
		Intent interIntent = getIntent();// 用于接收传来的参数
		status = interIntent.getIntExtra("status", 0);
		if (interIntent != null && status == 0)
			try {

				json = new JSONObject(interIntent.getStringExtra("json"));
				postLatLng = new LatLng(Double.valueOf(
						json.getString("lng_lat").substring(11)).doubleValue(),
						Double.valueOf(
								json.getString("lng_lat").substring(0, 10))
								.doubleValue());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		back = (Button) findViewById(R.id.back);
		reset = (Button) findViewById(R.id.reset);
		selfDef = (Button) findViewById(R.id.selfDef);
		bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);

		mapView = (MapView) findViewById(R.id.map);
		log2 = (TextView) findViewById(R.id.log);
		lat2 = (TextView) findViewById(R.id.lat);
		// markerText = (TextView) findViewById(R.id.mark_listenter_text);
		mapView.onCreate(savedInstanceState);// 必须要写

		mTapTextView = (TextView) findViewById(R.id.tap_text);

		initMapView();

		aMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				// TODO Auto-generated method stub

				log2.setText(Double.toString(aMap.getCameraPosition().target.longitude));
				lat2.setText(Double.toString(aMap.getCameraPosition().target.latitude));
				// if(status==0)
				// destroyMarkers();

				// 设置中心点和缩放比例
				// 重载方法 CameraUpdateFactory.newLatLngBounds
				// (LatLngBounds bounds, int width, int height, int padding)
				// 允许您指定矩形区域的长和宽（像素为单位）。
			}

			@Override
			public void onCameraChangeFinish(CameraPosition arg0) {
				// TODO Auto-generated method stub

				log2.setText(Double.toString(aMap.getCameraPosition().target.longitude));
				lat2.setText(Double.toString(aMap.getCameraPosition().target.latitude));
				if (status == 0)
					drawMarkers();

			}

		});

		selfDef.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (selfDef.getText().equals("手动定位")) {
					selfDef.setText(R.string.cancel);
					mTapTextView.setVisibility(View.VISIBLE);
					bottomLinear.setVisibility(View.VISIBLE);
					selfdef = true;

				} else {
					selfDef.setText(R.string.selfDef);
					mTapTextView.setVisibility(View.GONE);
					bottomLinear.setVisibility(View.GONE);
					selfdef = false;
				}
			}

		});
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				regeoMarker.remove();
				// 为下一次定位重新初始化
				regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f,
						0.5f).icon(
						BitmapDescriptorFactory.defaultMarker(R.drawable.map)));

			}

		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				if (selfdef == false) {

					intent.putExtra("lat", mTarget.latitude);
					intent.putExtra("lng", mTarget.longitude);
					intent.putExtra("country", country);
					intent.putExtra("province", province);
					intent.putExtra("city", city);
					// intent.putExtra("street", street);
					intent.putExtra("locale", locale);
					intent.putExtra("area", area);

				} else {
					intent.putExtra("lat", latLonPoint.getLatitude());
					intent.putExtra("lng", latLonPoint.getLongitude());
					intent.putExtra("country", country);
					intent.putExtra("province", address.getProvince());
					intent.putExtra("city", address.getCity());
					// intent.putExtra("street", street);
					intent.putExtra("locale", address.getDistrict());
					intent.putExtra("area", address.getNeighborhood());

				}
				DialogUtil.showDialog(v.getContext(), "success Position", false);
				LocationActivity.this.setResult(0, intent);
				LocationActivity.this.finish();

			}

		});
		//

		// aMap.moveCamera(CameraUpdateFactory.changeLatLng(postLatLng));
	}

	public void initMapView() {

		aMap = mapView.getMap();

		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		// aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

		// 定位到自己
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层

		aMap.getUiSettings().setScaleControlsEnabled(true);// 缩放比例尺
		aMap.getUiSettings().setCompassEnabled(true);// 指南针

		setUpMapBluePoint();
		aMap.setOnMapClickListener(this);// 添加地图点击监听
		aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);

		regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory.defaultMarker(R.drawable.map)));

		if (status == 0)// 在看帖中定位帖子位置设置
			setUpMap();
		else// 在发帖时设置当前缩放级别{
		{
			aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
			selfDef.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMapBluePoint() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()
	}

	private void setUpMap() {
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器

		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		addMarkersToMap();// 往地图上添加marker
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {

		// 文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度,Z值等
		TextOptions textOptions = new TextOptions()
				.position(Constants.BEIJING)
				.text("Text")
				.fontColor(Color.BLACK)
				.backgroundColor(Color.BLUE)
				.fontSize(30)
				.rotate(20)
				.align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
				.zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);
		aMap.addText(textOptions);

		aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(Constants.CHENGDU).title("成都市")
				.snippet("成都市:30.679879, 104.064855").draggable(true));

		markerOption = new MarkerOptions();
		markerOption.position(Constants.XIAN);
		markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
		markerOption.draggable(true);
		markerOption.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.arrow));
		marker2 = aMap.addMarker(markerOption);
		// marker旋转90度
		marker2.setRotateAngle(90);

		// 动画效果
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(Constants.ZHENGZHOU).title("郑州市").icons(giflist)
				.draggable(true).period(5));

	}

	/**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void drawMarkers() {

		try {

			marker = aMap.addMarker(new MarkerOptions()
					.position(postLatLng)
					.title((Html.fromHtml("<font color='#78b6fb'>"
							+ json.getString("content") + "</font>"))
							.toString())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
					.draggable(true));
			marker.showInfoWindow();// 设置默认显示一个infowinfow

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// marker = aMap.addMarker(new MarkerOptions()
		// .position(mTarget)
		// .title("好好学习")
		// .icon(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		// .draggable(true));
		// marker.showInfoWindow();// 设置默认显示一个infowinfow

	}

	public void destroyMarkers() {
		marker.destroy();
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (marker.equals(marker2)) {
			if (aMap != null) {
				jumpPoint(marker);
			}
		}
		markerText.setText("你点击的是" + marker.getTitle());
		return false;
	}

	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(Constants.XIAN);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * Constants.XIAN.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * Constants.XIAN.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				aMap.invalidate();// 刷新地图
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			}
		}
		if (amapLocation != null) {
			this.mTarget = new LatLng(amapLocation.getLatitude(),
					amapLocation.getLongitude());// 判断超时机制
			// 获取当前地图中心点的坐标

			country = amapLocation.getCountry();
			province = amapLocation.getProvince();
			city = amapLocation.getCity();
			street = amapLocation.getStreet();
			locale = amapLocation.getDistrict();// 区
			area = amapLocation.getRoad();

			if (status == 0) {
				// 设置所有maker显示在当前可视区域地图中
				LatLngBounds bounds = new LatLngBounds.Builder()
						.include(postLatLng).include(mTarget).build();
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用destroy()方法
			// 其中如果间隔时间为-1，则定位只定一次
			// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, -1, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub

		View infoContent = getLayoutInflater().inflate(
				R.layout.custom_info_contents, null);
		render(marker, infoContent);
		return infoContent;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {

		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(20);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		View infoContent = getLayoutInflater().inflate(
				R.layout.custom_info_contents, null);
		render(marker, infoContent);
		return infoContent;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		ToastUtil.show(this, "你点击了infoWindow窗口" + marker.getTitle());
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	/**
	 * 对单击地图事件回调
	 */
	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub

		if (selfdef == true)
			mTapTextView.setText("click map, point=" + point);

	}

	/**
	 * 对长按地图事件回调
	 */
	@Override
	public void onMapLongClick(LatLng point) {
		if (selfdef == true) {
			mTapTextView.setText("long pressed, point=" + point);
			// 动画效果

			latLonPoint.setLatitude(point.latitude);
			latLonPoint.setLongitude(point.longitude);

			getAddress(latLonPoint);
		}
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);// GeocodeAddress
																				// address
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				geoMarker.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();

				ToastUtil.show(this, addressName);
				mTapTextView.setText(addressName);
			} else {
				ToastUtil.show(this, R.string.no_result);
			}

		} else if (rCode == 27) {
			ToastUtil.show(this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(this, R.string.error_key);
		} else {
			ToastUtil.show(this, getString(R.string.error_other) + rCode);
		}
	}

	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = "地址 ："
						+ result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				address = result.getRegeocodeAddress();
				// aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				// AMapUtil.convertToLatLng(latLonPoint), 15));
				LatLng re = AMapUtil.convertToLatLng(latLonPoint);
				regeoMarker.setPosition(re);
				regeoMarker.setTitle(addressName);
				regeoMarker.showInfoWindow();

				ToastUtil.show(this, addressName);
				mTapTextView.setText(addressName);

			} else {
				ToastUtil.show(this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(this, R.string.error_key);
		} else {
			ToastUtil.show(this, getString(R.string.error_other) + rCode);
		}
	}

}
