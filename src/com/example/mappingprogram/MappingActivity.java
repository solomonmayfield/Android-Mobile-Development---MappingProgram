package com.example.mappingprogram;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MappingActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new MappingFragment();
	}

}

