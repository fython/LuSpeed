package moe.feng.luspeed.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import moe.feng.luspeed.R;
import moe.feng.luspeed.ui.adapter.CircledItemAdapter;

public class AboutFragment extends Fragment implements WatchViewStub.OnLayoutInflatedListener {

	private WatchViewStub mViewStub;
	private WearableListView mAboutList;

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
	public void onLayoutInflated(WatchViewStub view) {
		mAboutList = (WearableListView) view.findViewById(R.id.about_list_view);
		mAboutList.setAdapter(new CircledItemAdapter(new ArrayList<CircledItemAdapter.Item>()));
	}

}
