package com.liaofan_01.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.liaofan_01.R;

public class CategoryFragment extends Fragment {

	private TableLayout table;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_category, container,
				false);
		
		table = (TableLayout) view.findViewById(R.id.categoryTable);
		for (int i = 0; i < table.getChildCount(); i++) {
			TableRow row = (TableRow) table.getChildAt(i);
			for (int j = 0; j < 3; j++) {
				ImageView imageView = (ImageView) row.getChildAt(j);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						FragmentManager fm = getActivity().getFragmentManager();
						FragmentTransaction transaction = fm.beginTransaction();
						transaction.replace(R.id.mainContent,
								new PostListFragment(v.getTag().toString()));
						transaction.commit();
					}
				});
			}
		}
		return view;
	}
}
