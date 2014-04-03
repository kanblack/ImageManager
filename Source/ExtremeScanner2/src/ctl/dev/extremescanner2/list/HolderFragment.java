package ctl.dev.extremescanner2.list;

import ctl.dev.extremescanner2.BrowseActivity;
import ctl.dev.extremescanner2.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class HolderFragment extends Fragment {
	CheckBox cb_archive;
	boolean archive;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.holder, container, false);
		cb_archive = (CheckBox) v.findViewById(R.id.cb_archive);
		
		cb_archive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				archive = isChecked;
				((BrowseActivity) getActivity()).batchListFragment.Refesh(archive);
			}
		});
		return v;
	}
}
