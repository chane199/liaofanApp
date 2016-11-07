package com.liaofan_01.kindmanage;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.example.liaofan_01.R;

@SuppressLint("ResourceAsColor")
public class EditPostActivity extends Fragment implements OnItemClickListener {

	private String path = "";
	private Uri photoUri;

	private GridView gridview;
	private GridAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();

	List<String> urList = new ArrayList<String>();
	private ProgressDialog pd;// 添加loading条
	private LinearLayout activity_selectimg_send;

	private float dp;
	// private ScrollView activity_selectimg_scrollView;
	private HorizontalScrollView selectimg_horizontalScrollView;

	LatLng mTarget = null;
	String country, province, city, street, locale, area;

	// 顶部5个选项
	String SKind = null, SPay = null, SSupport_need = null, SLocation = null,
			SAvail_time = null;

	// 交互界面各个视图控件
	PopupMenu popup = null;

	private LinearLayout bnAdd, bnCancel;
	TextView post_kind;
	TextView payment;
	TextView support_need;
	TextView location;
	TextView avail_time;

	// 关于帖子 输入文本

	EditText itemName = null;// EditText
	EditText itemRemark = null;
	EditText contact = null;
	EditText itemDesc = null;

	CheckBox CB1, CB2, CB3;
	private SystemBarTintManager mTintManager;
	// 报酬一栏的显示控制布局
	LinearLayout payment1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_edit_post,
				container, false);
		// 顶部5个选项
		post_kind = (TextView) rootView.findViewById(R.id.post_kind);
		payment = (TextView) rootView.findViewById(R.id.payment);
		support_need = (TextView) rootView.findViewById(R.id.support_need);
		location = (TextView) rootView.findViewById(R.id.location);
		avail_time = (TextView) rootView.findViewById(R.id.avail_time);
		// 获取界面中的两个按钮
		bnAdd = (LinearLayout) rootView.findViewById(R.id.bnAdd);
		bnCancel = (LinearLayout) rootView.findViewById(R.id.bnCancel);

		// 输入文本
		itemName = (EditText) rootView.findViewById(R.id.itemName);
		itemRemark = (EditText) rootView.findViewById(R.id.itemRemark);
		contact = (EditText) rootView.findViewById(R.id.contact);
		itemDesc = (EditText) rootView.findViewById(R.id.itemDesc);

		// 复选框
		CB1 = (CheckBox) rootView.findViewById(R.id.checkbox1);
		CB2 = (CheckBox) rootView.findViewById(R.id.checkbox2);
		CB3 = (CheckBox) rootView.findViewById(R.id.checkbox3);

		// 上传图片 控件资源初始化
		dp = getResources().getDimension(R.dimen.dp);
		selectimg_horizontalScrollView = (HorizontalScrollView) rootView
				.findViewById(R.id.selectimg_horizontalScrollView);
		gridview = (GridView) rootView.findViewById(R.id.noScrollgridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridviewInit();

		pd = new ProgressDialog(getActivity());
		pd.setMessage("正在提交请求...");

		activity_selectimg_send = (LinearLayout) rootView
				.findViewById(R.id.activity_selectimg_send);

		// 为取消按钮的单击事件绑定事件监听器
		bnCancel.setOnClickListener(new HomeListener(getActivity()));

		// // 创建状态栏的管理实例
		// SystemBarTintManager tintManager = new
		// SystemBarTintManager(getActivity());
		// // 激活状态栏设置
		// tintManager.setStatusBarTintEnabled(true);
		// // 激活导航栏设置
		// tintManager.setNavigationBarTintEnabled(true);
		//
		// // 设置一个颜色给系统栏
		// tintManager.setTintColor(R.color.blue);
		//
		// mTintManager = new SystemBarTintManager(getActivity());
		// mTintManager.setStatusBarTintEnabled(true);
		// mTintManager.setNavigationBarTintEnabled(true);
		// mTintManager.setTintColor(R.color.blue);
		//
		// //设定状态栏的颜色，当版本大于4.4时起作用
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// SystemBarTintManager tintManager = new
		// SystemBarTintManager(getActivity().getParent());
		// tintManager.setStatusBarTintEnabled(true);
		// //此处可以重新指定状态栏颜色
		// tintManager.setStatusBarTintResource(R.color.blue);
		// }
		// 添加按钮
		bnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bmp.size() > 3) {

					Toast.makeText(getActivity(), "每次发布最多三张图片",
							Toast.LENGTH_SHORT).show();

				} else {
					for (int i = 0; i < drr.size(); i++) {
						urList.add(drr.get(i));
					}
					// 输入校验
					// if (validate())

					// try
					// {
					// Toast.makeText(getActivity(),
					// "您点击"+SAvail_time+SPay+SKind+SSupport_need+SLocation,
					// Toast.LENGTH_SHORT).show();
					// 添加物品种类
					// String result =addPost(SKind, SPay);

					// 帖子发布成功之后才能上传照片
					// if( result.substring(9,10).equals("1") )
					// {
					// 使用对话框来显示添加结果
					// DialogUtil.showDialog(getActivity(),"帖子添加成功！", true);
					// String post_id=null;
					// post_id=queryPost();
					// if(!(post_id.equals(null)))
					Toast.makeText(getActivity(), drr.get(0).toString(),
							Toast.LENGTH_SHORT).show();
					upload_photo(drr, "1071");// 发布帖子相关的图片

					// else
					// {
					// //帖子未上传成功
					// DialogUtil.showDialog(getActivity(),"帖子发布失败！", false);
					// }
					// }
					//
					// else
					// DialogUtil.showDialog(getActivity(),"添加失败！", false);
				}
				// catch (Exception e)
				// {
				// DialogUtil.showDialog(getActivity()
				// , "服务器响应异常，请稍后再试！" , false);
				// e.printStackTrace();
				// }

				// }
			}

			private String queryPost() {
				String result = null;
				JSONObject jsonArray;
				// TODO Auto-generated method stub
				// 使用Map封装请求参数
				Map<String, String> map = new HashMap<String, String>();
				map.put("author_id", "2");// 用户ID
				map.put("author_nickname", "zzy");// 用户ID
				map.put("category1", "test");
				map.put("title", itemName.getText().toString());
				map.put("phone", contact.getText().toString());

				map.put("content", itemDesc.getText().toString());

				// 定义发送请求的URL
				String url = HttpUtil.BASE_URL + "job/queryJobBase.php";

				try {
					// 发送请求

					result = HttpUtil.postRequest(url, map);

					jsonArray = new JSONObject(result); // 请求
					DialogUtil.showDialog(getActivity(),
							jsonArray.getString("id"), false);
					return jsonArray.getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;

			}

			// 上传帖子图片
			private void upload_photo(final List<String> urList, String post_id) {
				JSONObject jsonArray;
				String result = null;
				// activity_selectimg_send.setEnabled(false);
				pd.show();
				DialogUtil.showDialog(getActivity(), "正在上传照片！", false);

				System.out.println("==================" + urList.size());
				// RequestParams params = new RequestParams();
				// for(int i=0;i<urList.size();i++){
				// File file = new File(urList.get(i));
				// try {
				// params.put("pic"+i, file,"image/jpeg");
				// } catch (FileNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }

				// 使用Map封装请求参数
				Map<String, String> map = new HashMap<String, String>();
				// for(int i=0;i<urList.size();i++){
				map.put("post_id", post_id);
				try {
					map.put("photo_address", MD5.fileMD5(urList.get(0)));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				map.put("type", "image/jpeg");

				// 定义发送请求的URL
				String url = HttpUtil.BASE_URL + "job/photo.php";

				try {
					// 发送请求

					result = HttpUtil.postRequest(url, map);

					jsonArray = new JSONObject(result); // 请求
					DialogUtil.showDialog(getActivity(), result, false);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					DialogUtil.showDialog(getActivity(), "111服务器响应异常，请稍后再试！",
							false);
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pd.dismiss();
			}

			// }

		});

		// 种类
		post_kind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 创建PopupMenu对象
				popup = new PopupMenu(getActivity(), v);

				// 将R.menu.popup_menu菜单资源加载到popup菜单中
				getActivity().getMenuInflater().inflate(R.menu.kindlist,
						popup.getMenu());
				// 为popup菜单的菜单项单击事件绑定事件监听器
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						default:
							SKind = item.getTitle().toString();
						}
						return true;
					}
				});
				popup.show();
			}

		});
		// 报酬方式
		payment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 创建PopupMenu对象
				popup = new PopupMenu(getActivity(), v);
				popup.getMenu().add(1, 1, 1, "有偿");// add(groupId, itemId,
													// order, title)
				popup.getMenu().add(1, 2, 2, "无偿");

				// 将R.menu.popup_menu菜单资源加载到popup菜单中
				// getActivity().getMenuInflater().inflate(R.menu.kindlist,popup.getMenu());
				// 为popup菜单的菜单项单击事件绑定事件监听器
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case 1:
							// 报酬一栏的显示控制布局
							payment1 = (LinearLayout) rootView
									.findViewById(R.id.payment1);
							payment1.setVisibility(View.VISIBLE);
							SPay = item.getTitle().toString();
							break;
						case 2:
							// 报酬一栏的显示控制布局
							payment1 = (LinearLayout) rootView
									.findViewById(R.id.payment1);
							payment1.setVisibility(View.GONE);
							SPay = item.getTitle().toString();
							break;
						default:
							SPay = item.getTitle().toString();

						}
						return true;
					}
				});

				popup.show();
			}
		});
		// 供？求
		support_need.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 创建PopupMenu对象
				popup = new PopupMenu(getActivity(), v);
				popup.getMenu().add(1, 1, 1, "供");
				popup.getMenu().add(1, 2, 2, "求");
				// 为popup菜单的菜单项单击事件绑定事件监听器
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						default:
							SSupport_need = item.getTitle().toString();
						}
						return true;
					}
				});
				popup.show();
			}
		});
		location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 创建PopupMenu对象
				popup = new PopupMenu(getActivity(), v);

				popup.getMenu().add(1, 1, 1, "全国");// add(groupId, itemId,
													// order, title)
				popup.getMenu().add(1, 2, 2, "同城");
				popup.getMenu().add(1, 3, 3, "自定义");

				// 为popup菜单的菜单项单击事件绑定事件监听器
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {

						case 3:
							// 调用地图
							Intent intent = new Intent(getActivity(),
									LocationActivity.class);
							intent.putExtra("status", 1);
							startActivityForResult(intent, 0);
							//
							break;
						default:
							SLocation = item.getTitle().toString();
						}
						return true;
					}

				});
				popup.show();
			}
		});
		avail_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 创建PopupMenu对象
				popup = new PopupMenu(getActivity(), v);

				// 将R.menu.popup_menu菜单资源加载到popup菜单中
				getActivity().getMenuInflater().inflate(R.menu.avail_time,
						popup.getMenu());
				// 为popup菜单的菜单项单击事件绑定事件监听器
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						default:
							SAvail_time = item.getTitle().toString();
						}
						return true;
					}
				});
				popup.show();
			}
		});

		return rootView;
	}

	protected String addPost(String sKind2, String sPay2) throws Exception {
		// TODO Auto-generated method stub

		// 顶部5个选项
		String SKind = null, SPay = null, SSupport_need = null, SAvail_time = null;
		// 关于帖子 输入文本

		EditText itemRemark = null;

		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("author_id", "2");// 用户ID
		map.put("author_nickname", "zzy");// 用户ID
		map.put("category1", "test");
		map.put("title", itemName.getText().toString());
		map.put("phone", contact.getText().toString());

		map.put("content", itemDesc.getText().toString());
		map.put("publish_time",
				(new Timestamp(System.currentTimeMillis()).toString()));

		// 定位信息country,province,city,street,locale,area;

		map.put("country", country);
		map.put("province", province);
		map.put("city", city);
		// map.put("street", street);
		map.put("locale", locale);
		map.put("area", area);
		map.put("lng_lat", SLocation);

		map.put("paymethod", "0");

		map.put("end_status", "0");
		map.put("require_count", "0");
		map.put("enroll_count", "0");
		map.put("sign_count", "0");
		map.put("hot_status", "0");
		map.put("push_top_status", "0");

		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "job/publishJob.php";
		// 发送请求
		return HttpUtil.postRequest(url, map);
	}

	// 对用户输入的种类名称进行校验
	private boolean validate() {
		// 顶部五个选项检查
		if (SKind == null) {
			DialogUtil.showDialog(getActivity(), "类别  未选！", false);
			post_kind.callOnClick();
			return false;
		}
		if (SPay == null) {
			DialogUtil.showDialog(getActivity(), "报酬方式  未选！", false);
			payment.callOnClick();
			return false;
		}
		if (SSupport_need == null) {
			DialogUtil.showDialog(getActivity(), "供？求  未选！", false);
			support_need.callOnClick();
			return false;
		}
		if (SLocation == null) {
			DialogUtil.showDialog(getActivity(), "地理位置 未选！", false);
			location.callOnClick();
			return false;
		}
		if (SAvail_time == null) {
			DialogUtil.showDialog(getActivity(), "有效时间 未选！", false);
			avail_time.callOnClick();
			return false;
		}

		// 帖子正文输入文本检查

		if (itemName.getText().toString().trim().equals("")) {
			DialogUtil.showDialog(getActivity(), "帖子名称必填！", false);
			itemName.callOnClick();
			return false;
		}
		if (itemRemark.getText().toString().trim().equals("")) {
			DialogUtil.showDialog(getActivity(), "帖子标签必填！", false);
			itemRemark.callOnClick();
			return false;
		}
		if (contact.getText().toString().trim().equals("")) {
			DialogUtil.showDialog(getActivity(), "您的联系方式必填！", false);
			contact.callOnClick();
			return false;
		}
		if (itemDesc.getText().toString().trim().equals("")) {
			DialogUtil.showDialog(getActivity(), "帖子描述必填！", false);
			itemDesc.callOnClick();
			return false;
		}

		String name1 = post_kind.getText().toString().trim();

		if (name1.equals("")) {
			DialogUtil.showDialog(getActivity(), "种类名称是必填项！", false);
			return false;
		}

		if (bmp.size() > 3) {
			Toast.makeText(getActivity(), "每次发布最多三张图片", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			for (int i = 0; i < drr.size(); i++) {
				urList.add(drr.get(i));
			}

			// System.out.println(urList.toString());

		}

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

	public void testmethod() {

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

	}

	public void gridviewInit() {
		adapter = new GridAdapter(getActivity());
		adapter.setSelectedPosition(0);
		int size = 0;
		if (bmp.size() < 6) {
			size = bmp.size() + 1;
		} else {
			size = bmp.size();
		}
		LayoutParams params = gridview.getLayoutParams();
		final int width = size * (int) (dp * 9.4f);
		params.width = width;
		gridview.setLayoutParams(params);
		gridview.setColumnWidth((int) (dp * 9.4f));
		gridview.setStretchMode(GridView.NO_STRETCH);
		gridview.setNumColumns(size);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);

		selectimg_horizontalScrollView.getViewTreeObserver()
				.addOnPreDrawListener(// 绘制完毕
						new OnPreDrawListener() {
							public boolean onPreDraw() {
								selectimg_horizontalScrollView.scrollTo(width,
										0);
								selectimg_horizontalScrollView
										.getViewTreeObserver()
										.removeOnPreDrawListener(this);
								return false;
							}
						});
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater listContainer;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public class ViewHolder {
			public ImageView image;
			public Button bt;
		}

		public GridAdapter(Context context) {
			listContainer = LayoutInflater.from(context);
		}

		public int getCount() {
			if (bmp.size() < 6) {
				return bmp.size() + 1;
			} else {
				return bmp.size();
			}
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int sign = position;
			// 自定义视图
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// 获取list_item布局文件的视图

				convertView = listContainer.inflate(
						R.layout.gushi_item_published_grida, null);

				// 获取控件对象
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.bt = (Button) convertView
						.findViewById(R.id.item_grida_bt);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				holder.bt.setVisibility(View.GONE);
				if (position == 6) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(bmp.get(position));
				holder.bt.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						PhotoActivity.bitmap.remove(sign);
						bmp.get(sign).recycle();
						bmp.remove(sign);
						drr.remove(sign);

						gridviewInit();
					}
				});
			}

			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		if (position == bmp.size()) {
			String sdcardState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				new PopupWindows(getActivity(), gridview);
			} else {
				Toast.makeText(getActivity(), "sdcard已拔出，不能选择照片",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 上传照片弹出的三个选项
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.select_popupwindows,
					null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			// R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(
							// 相册
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}

		public void photo() {
			try {
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				String sdcardState = Environment.getExternalStorageState();
				String sdcardPathDir = android.os.Environment
						.getExternalStorageDirectory().getPath()
						+ Constants.CACHE_DIR + "/photo/";
				// String sdcardPathDir = FileUtils.SDPATH1;
				File file = null;
				if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
					// 有sd卡，是否有myImage文件夹
					File fileDir = new File(sdcardPathDir);
					if (!fileDir.exists()) {
						fileDir.mkdirs();
					}
					// 是否有headImg文件
					file = new File(sdcardPathDir + System.currentTimeMillis()
							+ ".jpg");
				}
				if (file != null) {
					path = file.getPath();
					photoUri = Uri.fromFile(file);
					openCameraIntent
							.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

					startActivityForResult(openCameraIntent,
							Constants.TAKE_PICTURE);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 已经分配的请求码
		// private static final int TAKE_PICTURE = 2;
		// private static final int RESULT_LOAD_IMAGE = 3;
		// private static final int CUT_PHOTO_REQUEST_CODE = 4;
		// private static final int SELECTIMG_SEARCH = 5;
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 0) {
			DialogUtil.showDialog(getActivity(), "success get coordinate!",
					false);
			mTarget = new LatLng(data.getDoubleExtra("lat", 0),
					data.getDoubleExtra("lng", 0));
			SLocation = mTarget.longitude + "#" + mTarget.latitude;
			country = data.getStringExtra("country");
			province = data.getStringExtra("province");
			city = data.getStringExtra("city");
			// street=data.getStringExtra("street");
			locale = data.getStringExtra("locale");// 区
			area = data.getStringExtra("area");
		}

		switch (requestCode) {
		case Constants.TAKE_PICTURE:
			if (drr.size() < 6 && resultCode == -1) {// 拍照
				startPhotoZoom(photoUri);
			}
			break;
		case Constants.RESULT_LOAD_IMAGE:
			if (drr.size() < 6 && resultCode == Constants.RESULT_OK
					&& null != data) {// 相册返回
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);

					Toast.makeText(getActivity(), uri.toString(),
							Toast.LENGTH_SHORT).show();

				}
			}
			break;

		case Constants.CUT_PHOTO_REQUEST_CODE:
			if (resultCode == Constants.RESULT_OK && null != data) {// 裁剪返回
				Bitmap bitmap = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
				PhotoActivity.bitmap.add(bitmap);
				bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
						(int) (dp * 1.6f));
				bmp.add(bitmap);
				gridviewInit();
			}

			break;

		}
	}

	private void startPhotoZoom(Uri uri) {
		final String CACHE_DIR = "/liaofan/";
		try {
			/* 判断目录是否已经存在 */
			String sdcardPathDir = android.os.Environment
					.getExternalStorageDirectory().getPath()
					+ CACHE_DIR
					+ "/photo";
			// String sdcardPathDir = FileUtils.SDPATH1
			File file = null;
			// 有sd卡，是否有myImage文件夹
			File fileDir = new File(sdcardPathDir);
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}

			/** 判断目录是否存在 **/
			// 获取系统时间，然后裁剪后的图片保存至指定的文件夹
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyymmddhhmmss");
			String address = sDateFormat.format(new java.util.Date());

			if (!FileUtils.isFileExist("")) {
				FileUtils.createSDDir("");
			}

			drr.add(FileUtils.SDPATH + address + ".jpg");
			// Uri imageUri = Uri.parse(
			Uri imageUri = Uri.parse("file:///sdcard/" + Constants.CACHE_DIR
					+ "/photo/thumb" + address + ".jpg");
			System.out.println("uri====" + FileUtils.SDPATH + address + ".jpg");

			final Intent intent = new Intent("com.android.camera.action.CROP");
			// 照片URL地址
			intent.setDataAndType(uri, "image/*");

			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 480);
			intent.putExtra("outputY", 480);

			// 输出路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			// 输出格式
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());

			intent.putExtra("noFaceDetection", false);
			intent.putExtra("return-data", false);

			startActivityForResult(intent, Constants.CUT_PHOTO_REQUEST_CODE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
