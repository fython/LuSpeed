package moe.feng.luspeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WizardActivity extends Activity {

	public static final int FLAG_THRESOLD = 0;

	private static final String EXTRA_FLAG = "flag", EXTRA_WIZARD_MODE = "wizard_mode";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static void launch(Activity activity, int flag, boolean wizardMode) {
		Intent intent = new Intent(activity, WizardActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		intent.putExtra(EXTRA_FLAG, flag);
		intent.putExtra(EXTRA_WIZARD_MODE, wizardMode);
		activity.startActivity(intent);
	}

}
