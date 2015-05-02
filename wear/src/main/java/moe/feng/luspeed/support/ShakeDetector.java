package moe.feng.luspeed.support;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

	private static ShakeDetector INSTANCE;
	private static final int UPDATE_INTERVAL = 100;
	private long mLastUpdateTime;
	private float mLastX, mLastY, mLastZ;
	private SensorManager mSensorManager;
	private ArrayList<OnShakeListener> mListeners;
	public int mShakeThreshold = 5000;

	private int mSensorChangeCount = 0;

	public static ShakeDetector getInstance(Context context){
		if (INSTANCE == null) {
			INSTANCE = new ShakeDetector(context);
		}
		return INSTANCE;
	}

	private ShakeDetector(Context context) {
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		mListeners = new ArrayList<>();
	}

	public interface OnShakeListener {
		void onShake(int shakeTimes);
	}

	public void addOnShakeListener(OnShakeListener listener) {
		if (mListeners.contains(listener))
			return;
		mListeners.add(listener);
	}

	public void removeOnShakeListener(OnShakeListener listener) {
		mListeners.remove(listener);
	}

	public void start() {
		if (mSensorManager == null) {
			throw new UnsupportedOperationException();
		}
		Sensor sensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (sensor == null) {
			throw new UnsupportedOperationException();
		}
		mSensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void stop() {
		if (mSensorManager != null)
			mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		long currentTime = System.currentTimeMillis();
		long diffTime = currentTime - mLastUpdateTime;
		if (diffTime < UPDATE_INTERVAL)
			return;
		mLastUpdateTime = currentTime;
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		float deltaX = x - mLastX;
		float deltaY = y - mLastY;
		float deltaZ = z - mLastZ;
		mLastX = x;
		mLastY = y;
		mLastZ = z;
		double delta = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ diffTime * 10000;
		if (delta > mShakeThreshold) {
			mSensorChangeCount++;
			this.notifyListeners(mSensorChangeCount);
		}
	}

	public void clearTimer() {
		mSensorChangeCount = 0;
	}

	public int getSensorChangeCount() {
		return mSensorChangeCount;
	}

	private void notifyListeners(int shakeTimes) {
		for (OnShakeListener listener : mListeners) {
			listener.onShake(shakeTimes);
		}
	}

}