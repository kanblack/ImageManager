package ctl.dev.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class EasyDialogManager {
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	private ProgressDialog progressDialog;

	public EasyDialogManager() {

	}

	public EasyDialogManager(String title, String message, boolean cancelable,
			boolean indterminate, OnCancelListener onCancelListener) {
		setTitle(title);

		setMessage(message);
		setCancelable(cancelable);
		setOnCancelListener(onCancelListener);
		setIndterminate(indterminate);
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public boolean isIndterminate() {
		return Indterminate;
	}

	public void setIndterminate(boolean indterminate) {
		Indterminate = indterminate;
	}

	public OnCancelListener getOnCancelListener() {
		return onCancelListener;
	}

	public void setOnCancelListener(OnCancelListener onCancelListener) {
		this.onCancelListener = onCancelListener;
	}

	private String Title = "", Message = "";
	private boolean Indterminate = false, Cancelable = false;

	public boolean isCancelable() {
		return Cancelable;
	}

	public void setCancelable(boolean cancelable) {
		Cancelable = cancelable;
	}

	private OnCancelListener onCancelListener = new OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {

		}
	};

	public void hideDialog() {

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}

	}

	public void showDialog(Activity activity, final Context context) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (progressDialog == null) {
					progressDialog = ProgressDialog.show(context, getTitle(),
							getMessage(), isIndterminate(), isCancelable(),
							getOnCancelListener());
				}
			}
		});
	}

	public void showDialog(final Context context) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (progressDialog == null) {
					progressDialog = ProgressDialog.show(context, getTitle(),
							getMessage(), isIndterminate(), isCancelable(),
							getOnCancelListener());
				}
			}
		});
	}
}
