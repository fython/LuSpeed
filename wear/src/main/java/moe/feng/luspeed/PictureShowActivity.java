package moe.feng.luspeed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.wearable.view.BoxInsetLayout;
import android.widget.ImageView;

public class PictureShowActivity extends Activity {

	private BoxInsetLayout mRootView;
	private ImageView mImageView;

	private static final String TAG = PictureShowActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_picture_show);

		mRootView = (BoxInsetLayout) findViewById(R.id.root_layout);
		mImageView = (ImageView) findViewById(R.id.image_view);

		Intent intent = getIntent();
		if (intent.hasExtra("id")) {
			mImageView.setImageResource(intent.getIntExtra("id", -1));
		} else if (intent.hasExtra("uri")) {
			mImageView.setImageURI(Uri.parse(intent.getStringExtra("uri")));
		}
		if (intent.hasExtra("background")) {
			mRootView.setBackgroundColor(intent.getIntExtra("background", Color.WHITE));
		}
	}

	public static void showImage(Activity activity, @DrawableRes int drawableId, int backgroundColor) {
		Intent intent = new Intent(activity, PictureShowActivity.class);
		intent.putExtra("id", drawableId);
		intent.putExtra("background", backgroundColor != -1 ? backgroundColor : Color.WHITE);
		activity.startActivity(intent);
	}

	public static void showImage(Activity activity, String uri, int backgroundColor) {
		Intent intent = new Intent(activity, PictureShowActivity.class);
		intent.putExtra("uri", uri);
		intent.putExtra("background", backgroundColor != -1 ? backgroundColor : Color.WHITE);
		activity.startActivity(intent);
	}

}
