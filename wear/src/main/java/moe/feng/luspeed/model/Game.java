package moe.feng.luspeed.model;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Game {

	public static final int DEFAULT_ROUND_TIME_SEC = 30, INTERVAL_SECOND = 1000;

	private int gameTime = DEFAULT_ROUND_TIME_SEC, remainSecond = 0, lastCount = 0;
	private Timer mTimer;

	private boolean isStart = false;

	private GameEventListener listener;

	private static final String TAG = Game.class.getSimpleName();

	public Game(GameEventListener listener) {
		this(DEFAULT_ROUND_TIME_SEC, listener);
	}

	public Game(int gameTime, GameEventListener listener) {
		this.gameTime = gameTime;
		this.listener = listener;
	}

	public void start() {
		if (!isStart) {
			isStart = true;
			mTimer = new Timer();
			remainSecond = gameTime;
			mTimer.schedule(mTask, INTERVAL_SECOND, INTERVAL_SECOND);
			listener.onStart();
			listener.onTimeChanged(remainSecond);
		}
	}

	public void stop() {
		if (isStart) {
			isStart = false;
			mTimer.cancel();
			listener.onStop();
		}
	}

	public void increaseCount() {
		lastCount++;
	}

	public void setCount(int count) {
		lastCount = count;
	}

	public boolean isStart() {
		return this.isStart;
	}

	public int getGameTime() {
		return this.gameTime;
	}

	public int getRemainSecond() {
		return this.remainSecond;
	}

	public int getCount() {
		return this.lastCount;
	}

	private TimerTask mTask = new TimerTask() {

		@Override
		public void run() {
			if (isStart) {
				if (remainSecond == 0) {
					stop();
					return;
				}
				Log.i(TAG, "Timer: remains " + remainSecond);
				remainSecond--;
				listener.onTimeChanged(remainSecond);
			}
		}

	};

	public interface GameEventListener {

		public void onTimeChanged(int remainSecond);
		public void onStart();
		public void onStop();

	}

}
