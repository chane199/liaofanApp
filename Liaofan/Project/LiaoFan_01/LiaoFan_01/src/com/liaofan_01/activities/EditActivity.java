package com.liaofan_01.activities;

import android.app.Fragment;

import com.liaofan_01.base.FragmentActivity;
import com.liaofan_01.kindmanage.EditPostActivity;

public class EditActivity extends FragmentActivity {

	@Override
	protected Fragment getFragment() {
		// TODO Auto-generated method stub
		return new EditPostActivity();
	}

}
