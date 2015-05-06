package moe.feng.luspeed;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import moe.feng.luspeed.ui.AboutFragment;
import moe.feng.luspeed.ui.MainFragment;

public class MainActivity extends Activity {

	private ViewPager mViewPager;

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return MainFragment.newInstance();
				case 1:
					return AboutFragment.newInstance();
				default:
					return null;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

	}

}
