package ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonAT extends Button {
	public ButtonAT(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ButtonAT(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ButtonAT(Context context) {
		super(context);
	}

	
	@SuppressWarnings("deprecation")
	@Override
	  public void setBackgroundDrawable(Drawable d) {
	    // Replace the original background drawable (e.g. image) with a LayerDrawable that
	    // contains the original drawable.
	    SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(d);
	    
//	    int sdk = android.os.Build.VERSION.SDK_INT;
//	    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
	    	super.setBackgroundDrawable(layer);
//	    } else {
//	    	super.setBackground(layer);
//	    }
	  }

	  /**
	   * The stateful LayerDrawable used by this button.
	   */
	  protected class SAutoBgButtonBackgroundDrawable extends LayerDrawable {

	    // The color filter to apply when the button is pressed
	    protected ColorFilter _pressedFilter = new LightingColorFilter(Color.GRAY, 1);
	    // Alpha value when the button is disabled
	    protected int _disabledAlpha2 = 150;
	    protected int _disabledAlpha1 = 90;
	    
	    public SAutoBgButtonBackgroundDrawable(Drawable d) {
	      super(new Drawable[] { d });
	    }

	    @Override
	    protected boolean onStateChange(int[] states) {
	      boolean enabled = false;
	      boolean pressed = false;

	      for (int state : states) {
	        if (state == android.R.attr.state_enabled)
	          enabled = true;
	        else if (state == android.R.attr.state_pressed)
	          pressed = true;
	      }

	      mutate();
	      if (enabled && pressed) {
	        setColorFilter(_pressedFilter);
//	        setAlpha(_disabledAlpha2);
	      } else if (!enabled) {
	        setColorFilter(null);
//	        setAlpha(_disabledAlpha1);
	      } else {
	        setColorFilter(null);
	      }

	      invalidateSelf();

	      return super.onStateChange(states);
	    }

	    @Override
	    public boolean isStateful() {
	      return true;
	    }
	  }
}
