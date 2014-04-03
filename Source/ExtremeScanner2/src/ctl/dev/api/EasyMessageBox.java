package ctl.dev.api;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Window;

public class EasyMessageBox {
	AlertDialog alertDialog;

	private boolean Show;

	public boolean isShow() {
		return Show;
	}

	public void setShow(boolean show) {
		Show = show;
	}

	private String Message, Title;

	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public EasyMessageBox() {
	}

	/**
	 * @param message
	 * @param title
	 * @param context
	 */
	public EasyMessageBox(String message, String title, Context context) {
		super();
		Message = message;
		Title = title;
		this.context = context;
	}

	public void show(OnClickListener onClickListener) {
		if (context != null) {
			if (alertDialog == null) {
				try {
					alertDialog = new AlertDialog.Builder(context).create();
					if (getTitle() == null) {
						alertDialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
					} else {
						alertDialog.setTitle(getTitle());
					}
					alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alertDialog.setMessage(getMessage() + "");

					alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
							"OK", onClickListener);
					alertDialog.setCancelable(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!alertDialog.isShowing() && !isShow()) {
					setShow(true);

					try {
						alertDialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void show() {
		if (context != null) {
			if (alertDialog == null && context != null) {
				alertDialog = new AlertDialog.Builder(context).create();
				if (getTitle() == null) {
					alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				} else {
					alertDialog.setTitle(getTitle());
				}
				alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDialog.setMessage(getMessage() + "");

				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								setShow(false);
								alertDialog.dismiss();
							}
						});
				alertDialog.setCancelable(true);
				if (!alertDialog.isShowing()&&!isShow()) {
					setShow(true);

					alertDialog.show();
				}
			}
		}
	}

	public void show2(OnClickListener ok) {
		if (context != null) {
			if (alertDialog == null) {
				alertDialog = new AlertDialog.Builder(context).create();
				if (getTitle() == null) {
					alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				} else {
					alertDialog.setTitle(getTitle());
				}
				alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDialog.setMessage(getMessage() + "");

				alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ok", ok);
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
						"Cancel", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								setShow(false);
								alertDialog.dismiss();
							}
						});
				alertDialog.setCancelable(true);

			}
			if (!alertDialog.isShowing() && !isShow()) {
				setShow(true);

				alertDialog.show();
			}
		}
	}

	public void hide() {
		if (alertDialog != null) {
			alertDialog.dismiss();
			setShow(false);

			alertDialog = null;
		}
	}

}
