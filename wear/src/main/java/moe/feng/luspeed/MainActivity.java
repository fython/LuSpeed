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

import moe.feng.luspeed.model.Game;
import moe.feng.luspeed.support.ShakeDetector;

public class MainActivity extends Activity implements WatchViewStub.OnLayoutInflatedListener {

	private WatchViewStub mViewStub;
	private TextView mTextView;

	private ShakeDetector mShakeDetector;

	private boolean isStart = false;
	private Game mGame;

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int MSG_UPDATE_TEXT = 1000;
	private static final String VALUE_TEXT = "text";

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
					mGame.setCount(shakeTimes);
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
			}

		});
		mGame.start();
	}

	private void endGame() {
		Log.i(TAG, "endGame");
		mGame.stop();

		int playedLength = mGame.getGameTime() - mGame.getRemainSecond();
		double speed = mGame.getCount() / playedLength;

		String str = String.format(
				getString(R.string.game_over),
				mGame.getGameTime() - mGame.getRemainSecond(),
				mGame.getCount(),
				new DecimalFormat("#.##").format(speed)
		);
		Log.i(TAG, str);
		updateText(str);

		mShakeDetector.clearTimer();
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
