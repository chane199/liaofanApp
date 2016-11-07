package com.liaofan_01.kindmanage;

import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.liaofan_01.utils.Config;
import com.liaofan_01.utils.NetWorkThread;

/**
 * Created by Administrator on 2015/7/31.
 */
public class ManageKindFragment extends Fragment {
	public static final int ADD_KIND = 0x1007;
	Button bnHome, bnAdd;
	ListView kindList;
	Callbacks mCallbacks;

	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// View rootView = inflater.inflate(R.layout.manage_kind, container ,
		// false);
		// ��ȡ���沼���ϵ�������ť
		// bnHome = (Button) rootView.findViewById(R.id.bn_home);
		// bnAdd = (Button) rootView.findViewById(R.id.bnAdd);
		// kindList = (ListView) rootView.findViewById(R.id.kindList);
		// Ϊ���ذ�ť�ĵ����¼����¼�������
		bnHome.setOnClickListener(new HomeListener(getActivity()));
		// Ϊ��Ӱ�ť�ĵ����¼����¼�������
		bnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View source) {
				// ����Ӱ�ť������ʱ�����ø�Fragment����Activity��onItemSelected����
				mCallbacks.onItemSelected(ADD_KIND, null);
			}
		});
		// ���巢�������URL HttpUtil.BASE_URL + "job/queryAllJobs.php"
		String url = "http://115.28.65.99/collegeAPI/job/queryAllJobs.php";
		Map<String, String> rawParams = null;
		try {
			String param = null;
			// // ��ָ��URL�������󣬲�����Ӧ��װ��JSONArray����
			// final JSONArray jsonArray = new JSONArray(
			// NetWorkThread.postRequest(url));
			// // ��JSONArray�����װ��Adapter
			// kindList.setAdapter(new KindArrayAdapter(jsonArray,
			// getActivity()));

			String str = "";
			NetWorkThread ntThread = new NetWorkThread(str);
			ntThread.setUrl(Config.getInstance().getUrl()
					+ "job/queryAllJobs.php");
			ntThread.setHandler(handler);
			ntThread.start();
			Message msg = handler.obtainMessage();
			handler.handleMessage(msg);

		} catch (Exception e) {

			DialogUtil.showDialog(getActivity(),
					"ManageKindFragment Exception for connection!", false);
			e.printStackTrace();
		}
		return null;
	}

	// ����Fragment����ӡ���ʾ��Activityʱ���ص��÷���
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// ���Activityû��ʵ��Callbacks�ӿڣ��׳��쳣
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"ManageKindFragment���ڵ�Activity����ʵ��Callbacks�ӿ�!");
		}
		// �Ѹ�Activity����Callbacks����
		mCallbacks = (Callbacks) activity;
	}

	// ����Fragment����������Activity�б�ɾ��ʱ�ص��÷���
	@Override
	public void onDetach() {
		super.onDetach();
		// ��mCallbacks��Ϊnull��
		mCallbacks = null;
	}
}
