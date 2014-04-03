//package ui;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.widget.ExpandableListView;
//
//public class MyExpandableListView extends ExpandableListView 
//{
//
//	public MyExpandableListView(Context context, AttributeSet attrs,
//			int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//	}
//
//	public MyExpandableListView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//	}
//
//	public MyExpandableListView(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
//	
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//	    super.onWindowFocusChanged(hasFocus);
//	    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//	       mExpandableListView.setIndicatorBounds(myLeft, myRight);
//	    } else {
//	       mExpandableListView.setIndicatorBoundsRelative(myLeft, myRight);
//	    }
//	}
//
//}
