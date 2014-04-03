package ctl.dev.extremescanner2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import ctl.dev.api.EasyMessageBox;
import ctl.dev.extremescanner2.list.AddStyleColorFragment;
import ctl.dev.extremescanner2.list.BatchListFragment;
import ctl.dev.extremescanner2.list.HolderFragment;
import ctl.dev.extremescanner2.list.ImageSliderFragment;

public class BrowseActivity extends FragmentActivity {

	Context context;
	String a = null;
	public ImageSliderFragment imageSliderFragment;
	public HolderFragment holderFragment;
	public BatchListFragment batchListFragment;
	public AddStyleColorFragment addStyleColorFragment;

	final String FRAGMENT_IMAGESLIDE = "imageslider";
	final String FRAGMENT_LIST = "FRAGMENT_LIST";
	final String FRAGMENT_ADD = "FRAGMENT_ADD";
	final String FRAGMENT_HOLDER = "FRAGMENT_HOLDEr";
	static boolean firstTime;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Log.e("create", "create");
		context = this;
		setContentView(R.layout.browser);

		if (imageSliderFragment == null) {
			imageSliderFragment = new ImageSliderFragment();
		}
		// if(holderFragment == null)
		// {
		// holderFragment = new HolderFragment();
		// }
		if (batchListFragment == null) {
			batchListFragment = new BatchListFragment();
		}
		if (holderFragment == null) {
			holderFragment = new HolderFragment();
		}
		if (addStyleColorFragment == null) {
			addStyleColorFragment = new AddStyleColorFragment();
		}
		
		if (a == null) {
			a = "exit";
		}

		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();

		fragmentTransaction.add(R.id.browser_fragment_right,
				imageSliderFragment, FRAGMENT_IMAGESLIDE);
		fragmentTransaction.add(R.id.browser_fragment_left, batchListFragment,
				FRAGMENT_LIST);

		fragmentTransaction.add(R.id.browser_fragment_right,
				addStyleColorFragment, FRAGMENT_ADD);
		fragmentTransaction.add(R.id.browser_fragment_right, holderFragment,
				FRAGMENT_HOLDER);
		fragmentTransaction.hide(addStyleColorFragment);
		fragmentTransaction.hide(imageSliderFragment);
		fragmentTransaction.show(holderFragment);
		fragmentTransaction.show(batchListFragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("resultCode", resultCode + "");
		if (resultCode == 12) {
			BatchListFragment batchListFragment = (BatchListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.browser_fragment_left);
			batchListFragment.onActivityResult(requestCode, resultCode, data);
		}
		if (resultCode == RESULT_OK) {
			imageSliderFragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void hideRightFragment(int type) {

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.anim3, R.anim.anim4);

		Log.e("visible1", imageSliderFragment.isVisible() + "");
		fragmentTransaction.hide(imageSliderFragment);
		if (type == 1) {
			fragmentTransaction.show(holderFragment);

		}
		if (type == 2) {
			fragmentTransaction.show(addStyleColorFragment);
		}
		fragmentTransaction.commit();
	}

	public void showImageSliderFragment(String stylecolor) 
	{
		batchListFragment.switchMode(BatchListFragment.TYPE_NORMAL);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		//
		fragmentTransaction.setCustomAnimations(R.anim.anim3, R.anim.anim4);
		Log.e("visible2", imageSliderFragment.isVisible() + "");

		fragmentTransaction.show(imageSliderFragment);
		fragmentTransaction.hide(holderFragment);
		fragmentTransaction.hide(addStyleColorFragment);
		fragmentTransaction.commit();
		
		imageSliderFragment.getImage(stylecolor);
		// }
	}

	public void showAddFragment() {
		batchListFragment.switchMode(BatchListFragment.TYPE_ADD);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		//
		fragmentTransaction.setCustomAnimations(R.anim.anim3, R.anim.anim4);
		Log.e("visible2", imageSliderFragment.isVisible() + "");

		fragmentTransaction.show(addStyleColorFragment);
		fragmentTransaction.hide(holderFragment);
		fragmentTransaction.hide(imageSliderFragment);
		fragmentTransaction.commit();
		// }
	}

	public void showHolderFragment() 
	{
		batchListFragment.switchMode(BatchListFragment.TYPE_NORMAL);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		//
		fragmentTransaction.setCustomAnimations(R.anim.anim3, R.anim.anim4);
		Log.e("visible2", imageSliderFragment.isVisible() + "");

		fragmentTransaction.show(holderFragment);
		fragmentTransaction.hide(addStyleColorFragment);
		fragmentTransaction.hide(imageSliderFragment);
		fragmentTransaction.commit();
		// }
	}

	@Override
	public void onBackPressed() {

		new EasyMessageBox("Do you want to exit", null, this)
				.show2(new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("onDestroy()", "Go here");

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e("onConfigurationChanged", "Go here");
	}
}
