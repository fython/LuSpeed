package moe.feng.luspeed;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import moe.feng.luspeed.support.ShakeDetector;

public class MainActivity extends Activity implements WatchViewStub.OnLayoutInflatedListener {

	private WatchViewStub mViewStub;
	private TextView mTextView;

	private ShakeDetector mShakeDetector;

	private boolean isStart = false;

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int MSG_UPDATE_TEXT = 1000;
	private static final String VALUE_TEXT = "text";
	private static final int ROUND_TIME_SEC = 30, INTERVAL_SECOND = 1000;

	private int remainSecond = 0, lastCount = 0;
	private Timer mTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mViewStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		mViewStub.setOnLayoutInflatedListener(this);

		mShakeDetector = ShakeDetector.getInstance(getApplicationContext());
		mShakeDetector.mShakeThreshold = 3500;
		mShakeDetector.addOnShakeListener(new ShakeDetector.OnShakeListener() {
			@Override
			public void onShake(int shakeTimes) {
				if (isStart) {
					updateData();
				}
			}
		});
		mShakeDetector.start();
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
					endGame();
					remainSecond = 0;
				}
			}
		});
	}

	@Override
	protected void onStop() {
		mShakeDetector.stop();
		endGame();
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mShakeDetector.start();
	}

	private void startGame() {
		Log.i(TAG, "startGame");
		mShakeDetector.clearTimer();
		isStart = true;
		remainSecond = ROUND_TIME_SEC;
		mTimer = new Timer();
		mTimer.schedule(mTask, INTERVAL_SECOND, INTERVAL_SECOND);
	}

	private void endGame() {
		Log.i(TAG, "endGame");
		isStart = false;
		mTimer.cancel();
		lastCount = mShakeDetector.getSensorChangeCount();
		mShakeDetector.clearTimer();

		double speed = lastCount / (ROUND_TIME_SEC - remainSecond);

		String str = String.format(
				getString(R.string.game_over),
				(ROUND_TIME_SEC - remainSecond),
				lastCount,
				new DecimalFormat("#.00").format(speed)
		);
		Log.i(TAG, str);
		updateText(str);
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
		int times = mShakeDetector.getSensorChangeCount();
		String str = String.format(getString(R.string.title_luing),
				remainSecond,
				times
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

	private TimerTask mTask = new TimerTask() {

		@Override
		public void run() {
			if (isStart) {
				if (remainSecond == 0) {
					endGame();
					return;
				}
				Log.i(TAG, "Timer: remains " + remainSecond);
				remainSecond--;
				updateData();
			}
		}

	};

}
