package com.liaofan_01.fragments;

import com.example.liaofan_01.R;
import com.example.liaofan_01.R.layout;
import com.liaofan_01.kindmanage.EditPostActivity;
import com.liaofan_01.kindmanage.HomeListener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PostFragment extends Fragment {

	
	LinearLayout bnAdd,bnCancel;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_post, container,
				false);
		
		bnAdd=(LinearLayout)rootView.findViewById(R.id.bnAdd);
		bnCancel=(LinearLayout)rootView.findViewById(R.id.bnCancel);
		
		
		// 为取消按钮的单击事件绑定事件监听器
		bnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.mainContent, new HomeFragment());
				transaction.commit();
			}});
				
		return rootView;
	}
	




}
