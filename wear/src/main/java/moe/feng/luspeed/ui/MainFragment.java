package moe.feng.luspeed.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import moe.feng.luspeed.R;
import moe.feng.luspeed.model.Game;
import moe.feng.luspeed.support.ShakeDetector;

public class MainFragment extends Fragment implements WatchViewStub.OnLayoutInflatedListener {

	private WatchViewStub mViewStub;
	private TextView mTextView;

	private ShakeDetector mShakeDetector;

	private boolean isStart = false;
	private Game mGame;

	private static final int MSG_UPDATE_TEXT = 1000;
	private static final String VALUE_TEXT = "text";

	private static final String TAG = MainFragment.class.getSimpleName();

	public static MainFragment newInstance() {
		MainFragment fragment = new MainFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_main, container, false);

		mViewStub = (WatchViewStub) root.findViewById(R.id.watch_view_stub);
		mViewStub.setOnLayoutInflatedListener(this);

		return root;
	}

	@Override
	public void onLayoutInflated(WatchViewStub watchViewStub) {
		mTextView = (TextView) watchViewStub.findViewById(R.id.text);

		CircledImageView mCircleBtn = (CircledImageView) watchViewStub.findViewById(R.id.btn_circle);
		mCircleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!isStart) {
					startGame();
				} else {
					if (mGame != null) {
						endGame();
					}
				}
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mShakeDetector = ShakeDetector.getInstance(activity.getApplicationContext());
		mShakeDetector.mShakeThreshold = 3500;
		mShakeDetector.addOnShakeListener(new ShakeDetector.OnShakeListener() {
			@Override
			public void onShake(int shakeTimes) {
				if (isStart) {
					mGame.setCount(shakeTimes);
					updateData();
				}
			}
		});
		mShakeDetector.start();
	}

	@Override
	public void onStop() {
		if (mShakeDetector != null) {
			mShakeDetector.stop();
		}
		if (mGame != null) {
			endGame();
		}
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mShakeDetector != null) {
			mShakeDetector.start();
		}
	}

	private void startGame() {
		Log.i(TAG, "startGame");
		if (mShakeDetector != null) {
			mShakeDetector.clearTimer();
		}

		mGame = new Game(new Game.GameEventListener() {

			@Override
			public void onTimeChanged(int remainSecond) {
				updateData();
			}

			@Override
			public void onStart() {
				isStart = true;
			}

			@Override
			public void onStop() {
				isStart = false;
				endGame();
			}

		});
		mGame.start();
	}

	private void endGame() {
		Log.i(TAG, "endGame");
		mGame.stop();

		int playedLength = mGame.getGameTime() - mGame.getRemainSecond();

		String str = String.format(
				getString(R.string.game_over),
				playedLength,
				mGame.getCount(),
				String.format("%.3f", mGame.getCount() / (float) playedLength)
		);
		Log.i(TAG, str);
		updateText(str);

		if (mShakeDetector != null) {
			mShakeDetector.clearTimer();
		}
	}

	private void updateText(String text) {
		Message msg = new Message();
		Bundle data = new Bundle();
		msg.what = MSG_UPDATE_TEXT;
		data.putString(VALUE_TEXT, text);
		msg.setData(data);
		mHandler.sendMessage(msg);
	}

	private void updateData() {
		String str = String.format(getString(R.string.title_luing),
				mGame.getRemainSecond(),
				mGame.getCount()
		);
		Log.i(TAG, str);
		updateText(str);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_UPDATE_TEXT:
					String text = msg.getData().getString(VALUE_TEXT);
					if (text != null && !text.isEmpty()) {
						mTextView.setVisibility(mTextView.VISIBLE);
						mTextView.setText(text);
					} else {
						mTextView.setVisibility(mTextView.GONE);
					}
					break;
			}
		}

	};

}
