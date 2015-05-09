package moe.feng.luspeed.ui;

import android.app.Fragment;
import android.content.Context;
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

	private Context mContext;

	private static final String TAG = AboutFragment.class.getSimpleName();

	public static AboutFragment newInstance() {
		AboutFragment fragment = new AboutFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_about, container, false);

		mContext = getActivity().getApplicationContext();

		mViewStub = (WatchViewStub) root.findViewById(R.id.watch_view_stub);
		mViewStub.setOnLayoutInflatedListener(this);

		return root;
	}

	@Override
	public void onLayoutInflated(WatchViewStub view) {
		mAboutList = (WearableListView) view.findViewById(R.id.about_list_view);

		String version = "Unknown";
		try {
			version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
			version += " (" + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode + ")";
		} catch (Exception e) {
			// Keep the default value
		}

		ArrayList<CircledItemAdapter.Item> array = new ArrayList<>();
		array.add(createItem(mContext.getString(R.string.about_title_0), version, R.drawable.ic_launcher));
		array.add(createItem(mContext.getString(R.string.about_title_1), mContext.getString(R.string.about_content_1), R.drawable.ic_sina_logo));
		array.add(createItem(mContext.getString(R.string.about_title_2), mContext.getString(R.string.about_content_2), -1));

		mAboutList.setAdapter(new CircledItemAdapter(array));
	}

	private CircledItemAdapter.Item createItem(String title, String content, int drawableId) {
		return new CircledItemAdapter.Item(title, content, drawableId);
	}

}
