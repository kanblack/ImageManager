package ctl.dev.extremescanner2.list;

import ctl.dev.extremescanner2.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StyleColorListFragment extends Fragment 
{
	private View view;
	ListView lv_listUpc;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.stycolor_list, container, false);
		lv_listUpc = (ListView) view.findViewById(R.id.stycolor_lv_list);
		
		return view;
	}
	
	class UpcAdapter extends BaseAdapter
	{
		class ViewHolder
		{
			TextView tv_color;
			TextView tv_stylecolor;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
