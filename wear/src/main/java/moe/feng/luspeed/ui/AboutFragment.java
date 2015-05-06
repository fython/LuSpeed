package moe.feng.luspeed.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.feng.luspeed.R;

public class AboutFragment extends Fragment implements WatchViewStub.OnLayoutInflatedListener {

	private WatchViewStub mViewStub;

	private static final String TAG = AboutFragment.class.getSimpleName();

	public static AboutFragment newInstance() {
		AboutFragment fragment = new AboutFragment();
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_about, container, false);

		mViewStub = (WatchViewStub) root.findViewById(R.id.watch_view_stub);
		mViewStub.setOnLayoutInflatedListener(this);

		return root;
	}

	@Override
	public void onLayoutInflated(WatchViewStub watchViewStub) {

	}

}
