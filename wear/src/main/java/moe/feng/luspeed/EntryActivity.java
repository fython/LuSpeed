package moe.feng.luspeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import moe.feng.luspeed.support.Settings;

public class EntryActivity extends Activity {

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		Settings settings = Settings.getInstance(getApplicationContext());
		if (!settings.getBoolean(Settings.KEY_FIRST_RUN, true)) {
			WizardActivity.launch(this, WizardActivity.FLAG_THRESOLD, true);
		} else {
			startActivity(new Intent(this, MainActivity.class));
		}

		finish();
	}

}
