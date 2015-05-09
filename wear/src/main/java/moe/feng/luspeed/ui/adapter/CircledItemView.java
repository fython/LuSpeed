package moe.feng.luspeed.ui.adapter;

import android.content.Context;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import moe.feng.luspeed.R;

public class CircledItemView extends LinearLayout implements WearableListView.OnCenterProximityListener {

	private CircledImageView mImageButton;
	private TextView mTitle, mContent;

	private final float mFadedTextAlpha;
	private final float mChosenCircleScale;

	public CircledItemView(Context context) {
		this(context, null);
	}

	public CircledItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircledItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mFadedTextAlpha = 0.4f;
		mChosenCircleScale = 1.4f;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mImageButton = (CircledImageView) findViewById(R.id.image_view);
		mTitle = (TextView) findViewById(R.id.title);
		mContent = (TextView) findViewById(R.id.content);
	}

	@Override
	public void onCenterPosition(boolean animate) {
		mImageButton.setScaleX(mChosenCircleScale);
		mImageButton.setScaleY(mChosenCircleScale);
		mTitle.setAlpha(1f);
		mContent.setAlpha(1f);
	}

	@Override
	public void onNonCenterPosition(boolean animate) {
		mImageButton.setScaleX(1f);
		mImageButton.setScaleY(1f);
		mTitle.setAlpha(mFadedTextAlpha);
		mContent.setAlpha(mFadedTextAlpha);
	}

}