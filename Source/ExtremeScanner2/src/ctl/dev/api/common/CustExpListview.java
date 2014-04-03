package ctl.dev.api.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

public class CustExpListview extends ExpandableListView {

	private View epl_Choice;

//	public CustExpListview(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//	}
//
//	public CustExpListview(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//	}
//
	public CustExpListview(Context context,View epl_Choice) {
		super(context);
		this.epl_Choice = epl_Choice;
		// TODO Auto-generated constructor stub
	}
	
	

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(epl_Choice.getWidth(),
				MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}