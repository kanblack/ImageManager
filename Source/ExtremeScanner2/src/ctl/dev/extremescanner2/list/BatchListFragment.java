package ctl.dev.extremescanner2.list;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import ctl.dev.api.ConnectListener2;
import ctl.dev.api.EasyDialogManager;
import ctl.dev.api.EasyMessageBox;
import ctl.dev.api.service.object.Batch;
import ctl.dev.api.service.object.Product;
import ctl.dev.api.service.object.ScanItem;
import ctl.dev.api.service.object.Style_color;
import ctl.dev.api.service.param.AddStylecolorParam;
import ctl.dev.api.service.param.GetBatchesParam;
import ctl.dev.api.service.param.ScanUpcParam;
import ctl.dev.api.service.param.VerifyUpcParam;
import ctl.dev.api.service.receiver.AddProuctReceiver;
import ctl.dev.api.service.receiver.AddStylecolorReceiver;
import ctl.dev.api.service.receiver.GetBatchesReceiver;
import ctl.dev.api.service.receiver.ScanUpcReceiver;
import ctl.dev.api.service.receiver.VerifyUpcReceiver;
import ctl.dev.api.service.request.AddBatchRequest;
import ctl.dev.api.service.request.AddProductRequest;
import ctl.dev.api.service.request.GetBatchesRequest;
import ctl.dev.api.service.request.ScanUpcRequest;
import ctl.dev.api.service.request.VerifyUpcRequest;
import ctl.dev.extremescanner2.BrowseActivity;
import ctl.dev.extremescanner2.R;
import ctl.dev.extremescanner2.camera.CameraActivity;

public class BatchListFragment extends Fragment implements OnClickListener {
	Button btn_scan, btn_add, btn_addbatch;
	View view;
	ExpandableListView epl_batch;
	ExpandableListView epl_product;

	ScanUpcRequest scanUpcRequest;
	GetBatchesRequest getBatchesRequest;
	EasyDialogManager easyDialogManager;
	ArrayList<Batch> arrayBatchs;

	LinearLayout ll_normal;
	BatchAdapter batchAdapter;
	ProductAdapter productAdapter;
	private VerifyUpcRequest verifyUpcRequest;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// if(view == null)

		// {
		try {
			view = inflater.inflate(R.layout.batch_list, container, false);
			epl_batch = (ExpandableListView) view
					.findViewById(R.id.batch_epl_batch);
			epl_product = (ExpandableListView) view
					.findViewById(R.id.batch_list_epl_product);

			ViewTreeObserver vto = epl_batch.getViewTreeObserver();

			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					epl_batch.setIndicatorBounds(epl_batch.getRight()
							- (int) getResources().getDimension(R.dimen.space),
							epl_batch.getWidth());
				}
			});

			ViewTreeObserver vto2 = epl_product.getViewTreeObserver();

			vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					epl_product.setIndicatorBounds(epl_product.getRight()
							- (int) getResources().getDimension(R.dimen.space),
							epl_product.getWidth());
				}
			});

			epl_product.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					productAdapter.childSelected = childPosition;
					productAdapter.notifyDataSetChanged();
					String stylecolor = productAdapter.getChild(
							productAdapter.groupSelected, childPosition)
							.getStylecolor();
					((BrowseActivity) getActivity())
							.showImageSliderFragment(stylecolor);
					return false;
				}
			});

			epl_product.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					if (Mode == TYPE_ADD) {

						((BrowseActivity) getActivity()).addStyleColorFragment
								.setCombo(
										batchAdapter.getGroup(
												batchAdapter.groupSelected).getId(),
										batchAdapter.getGroup(
												batchAdapter.groupSelected)
												.getName(), productAdapter
												.getGroup(groupPosition).getTitle());
					}
					return false;
				}
			});

			ll_normal = (LinearLayout) view
					.findViewById(R.id.batch_list_normal_controler_group);
			btn_addbatch = (Button) view.findViewById(R.id.batch_btn_addnewbatch);
			btn_add = (Button) view.findViewById(R.id.batch_btn_add);
			btn_scan = (Button) view.findViewById(R.id.batch_btn_scan);
			btn_add.setOnClickListener(this);
			btn_scan.setOnClickListener(this);
			btn_addbatch.setOnClickListener(this);
			ll_normal.setOnClickListener(this);

			if (scanUpcRequest == null) {
				scanUpcRequest = new ScanUpcRequest(new ScanUpcParam(),
						new ScanUpcReceiver());
				scanUpcRequest
						.setOnConnectListener(new ConnectListener2<ScanUpcReceiver>() {

							@Override
							public void onSuccess(Object tag, JSONArray response,
									ScanUpcReceiver object) {
								easyDialogManager.hideDialog();
								if (object.getResult().size() > 0) {
									ScanItem scanItem = object.getResult().get(0);
									for (int i = 0; i < arrayBatchs.size(); i++) {
										Batch batch = arrayBatchs.get(i);
										if (batch.getId().equals(
												scanItem.getBatch())) {
											Log.e("Find batch id", batch.getId()
													+ "");
											for (int j = 0; j < batch.getProducts().size();j++){
												Product product = batch
														.getProducts().get(j);
												if (scanItem.getProduct().equals(
														product.getTitle())) {
													Log.e("Find product title",
															product.getTitle());
													for (int k = 0; k < product
															.getStyle_color()
															.size(); k++) {

														Style_color style_color = product
																.getStyle_color()
																.get(k);
														if (style_color
																.getStylecolor()
																.equals(scanItem
																		.getStylecolor())) {
															Log.e("Find style color",
																	style_color
																			.getStylecolor());
															Log.e("One - Two - Three",
																	i + " - " + j
																			+ " - "
																			+ k);
															if(batchAdapter.groupSelected != -1)
															{
																Log.e("Go here", batchAdapter.groupSelected+"");
																epl_batch.collapseGroup(0);
																productAdapter.notifyDataSetChanged();
																
															}
															
//														epl_batch.collapseGroup(epl_batch.get)
															epl_batch
																	.expandGroup(i);

															productAdapter.childSelected = k;
															epl_product
																	.expandGroup(j);
															((BrowseActivity) getActivity())
																	.showImageSliderFragment(style_color
																			.getStylecolor());
															break;
														}

													}
													break;
												}
//												break;
											}
											break;
										}
									}
								} else {
									new EasyMessageBox(
											"Can't find item, do you want add to the database",
											null, getActivity())
											.show2(new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													((BrowseActivity) getActivity())
															.showAddFragment();

												}
											});
								}

							}

							@Override
							public void onPrepare(Object tag) {
								easyDialogManager = new EasyDialogManager();
								easyDialogManager.setCancelable(false);
								easyDialogManager.showDialog(getActivity());
							}

							@Override
							public void onError(Object tag, VolleyError error) {
								easyDialogManager.hideDialog();
								new EasyMessageBox(error.getMessage(), null,
										getActivity()).show();
								Log.e("ERROR", error.getMessage() + "");
							}
						});
			}

			if (getBatchesRequest == null) {
				getBatchesRequest = new GetBatchesRequest(new GetBatchesParam(),
						new GetBatchesReceiver());
				getBatchesRequest
						.setOnConnectListener(new ConnectListener2<GetBatchesReceiver>() {

							@Override
							public void onSuccess(Object tag, JSONArray response,
									GetBatchesReceiver object) {
								easyDialogManager.hideDialog();
								// Log.e("Result",
								// object.getResult().get(0).getName()
								// + "");

								arrayBatchs = (ArrayList<Batch>) object.getResult();
								batchAdapter = new BatchAdapter();
								epl_batch.setAdapter(batchAdapter);

								productAdapter = new ProductAdapter();
								epl_product.setAdapter(productAdapter);
							}

							@Override
							public void onPrepare(Object tag) {
								easyDialogManager = new EasyDialogManager();
								easyDialogManager.setCancelable(false);
								easyDialogManager.showDialog(getActivity());
							}

							@Override
							public void onError(Object tag, VolleyError error) {
								easyDialogManager.hideDialog();
								new EasyMessageBox(error.getMessage(), null,
										getActivity()).show();
								Log.e("ERROR", error.getMessage() + "");
							}
						});
				getBatchesRequest.excute(false);
			}

			if (epl_batch != null && batchAdapter != null && arrayBatchs != null) {

				epl_batch.setAdapter(batchAdapter);
				epl_product.setAdapter(productAdapter);

			}

			if (verifyUpcRequest == null) {
				verifyUpcRequest = new VerifyUpcRequest(new VerifyUpcParam(),
						new VerifyUpcReceiver());
				verifyUpcRequest
						.setOnConnectListener(new ConnectListener2<VerifyUpcReceiver>() {

							@Override
							public void onSuccess(Object tag, JSONArray response,
									VerifyUpcReceiver object) {
								easyDialogManager.hideDialog();
								if (object.getResult().size() > 0) {
									if (object.getResult().get(0).getCode()
											.equals("1")) {

									} else {

									}
									new EasyMessageBox(object.getResult().get(0)
											.getMessage()
											+ "", null, getActivity()).show();
								}

							}

							@Override
							public void onPrepare(Object tag) {
								easyDialogManager = new EasyDialogManager();
								easyDialogManager.setCancelable(false);
								easyDialogManager.showDialog(getActivity());
							}

							@Override
							public void onError(Object tag, VolleyError error) {
								easyDialogManager.hideDialog();
								new EasyMessageBox(error.getMessage(), null,
										getActivity()).show();

							}
						});

			}

			if (addBatchRequest == null) {
				addBatchRequest = new AddBatchRequest(new AddStylecolorParam(),
						new AddStylecolorReceiver());
				addBatchRequest
						.setOnConnectListener(new ConnectListener2<AddStylecolorReceiver>() {

							@Override
							public void onSuccess(Object tag, JSONArray response,
									AddStylecolorReceiver object) {
								easyDialogManager.hideDialog();
								if (object.getResult().size() > 0) {
									if (object.getResult().get(0).getCode()
											.equals("1")) {
										arrayBatchs.add(new Batch(object
												.getResult().get(0).getId()
												+ "", nameBatchUpdate,
												new ArrayList<Product>()));
										batchAdapter.notifyDataSetChanged();
									} else {

									}
									new EasyMessageBox(object.getResult().get(0)
											.getMessage()
											+ "", null, getActivity()).show();
								}
							}

							@Override
							public void onPrepare(Object tag) {
								easyDialogManager = new EasyDialogManager();
								easyDialogManager.setCancelable(false);
								easyDialogManager.showDialog(getActivity());
							}

							@Override
							public void onError(Object tag, VolleyError error) {
								easyDialogManager.hideDialog();
								new EasyMessageBox(
										error.getLocalizedMessage() + "", null,
										getActivity());

							}
						});
			}

			if (addProductRequest == null) {
				addProductRequest = new AddProductRequest(new AddStylecolorParam(),
						new AddProuctReceiver());
				addProductRequest
						.setOnConnectListener(new ConnectListener2<AddProuctReceiver>() {

							@Override
							public void onSuccess(Object tag, JSONArray response,
									AddProuctReceiver object) {
								easyDialogManager.hideDialog();
								if (object.getResult().size() > 0) {
									if (object.getResult().get(0).getCode()
											.equals("1")) {
										// object.getResult().get(0).getId();
										arrayBatchs
												.get(batchAdapter.groupSelected)
												.getProducts()
												.add(new Product(
														object.getResult().get(0)
																.getTitle(),
														new ArrayList<Style_color>()));
										batchAdapter.notifyDataSetChanged();
									} else {

									}
									new EasyMessageBox(object.getResult().get(0)
											.getMessage()
											+ "", null, getActivity()).show();
								}
							}

							@Override
							public void onPrepare(Object tag) {
								easyDialogManager = new EasyDialogManager();
								easyDialogManager.setCancelable(false);
								easyDialogManager.showDialog(getActivity());
							}

							@Override
							public void onError(Object tag, VolleyError error) {
								easyDialogManager.hideDialog();
								new EasyMessageBox(
										error.getLocalizedMessage() + "", null,
										getActivity());

							}
						});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	void Refesh(boolean arrchive)
	{
		
		if(arrchive)
		{
			getBatchesRequest.excute(arrchive);
		}
		else
		{
			getBatchesRequest.excute(arrchive);
		}
	}
	
	public void UpdateListAfterChange() {
		getBatchesRequest.excute(((BrowseActivity)getActivity()).holderFragment.archive);
	}

	String nameBatchUpdate, nameProductUpdate;

	class BatchAdapter extends BaseExpandableListAdapter {
		boolean groupExpand;
		boolean groupChildExpand;

		int groupSelected = -1;

		@Override
		public void onGroupCollapsed(int groupPosition) {

			super.onGroupCollapsed(groupPosition);
			try {
				groupSelected = -1;
				groupExpand = false;
				epl_product.setVisibility(View.GONE);
				epl_product.collapseGroup(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onGroupExpanded(int groupPosition) {

			super.onGroupExpanded(groupPosition);
			try {
				groupSelected = groupPosition;
				groupExpand = true;
				notifyDataSetChanged();
				productAdapter.notifyDataSetChanged();
				epl_product.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		class ViewHolderParent {
			TextView tv_batchName;
			Button btn_addNewProduct;
		}

		class ViewHolderChild {
			TextView tv_productTitle;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 0;
		}

		@Override
		public int getGroupCount() {
			if (groupExpand) {
				return 1;
			} else {
				Log.e("CHECK", "COUNT");
				return arrayBatchs.size();
			}
		}

		@Override
		public Product getChild(int groupPosition, int childPosition) {
			if (groupExpand) {
				groupPosition = groupSelected;
			}
			return arrayBatchs.get(groupPosition).getProducts()
					.get(childPosition);
		}

		@Override
		public Batch getGroup(int groupPosition) {
			if (groupExpand) {
				groupPosition = groupSelected;
				Log.e("groupPosition", arrayBatchs.get(groupPosition).getName()
						+ "");
			}

			return arrayBatchs.get(groupPosition);

		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			if (groupExpand) {
				groupPosition = groupSelected;
			}
			return childPosition;
		}

		@Override
		public long getGroupId(int groupPosition) {
			if (groupExpand) {
				groupPosition = groupSelected;

			}

			return groupPosition;
		}

		@Override
		public boolean hasStableIds() {

			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return false;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			return convertView;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			try {
				ViewHolderParent holderParent;
				if (convertView == null) {
					convertView = ((LayoutInflater) getActivity().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE)).inflate(
							R.layout.row_batches, null);
					holderParent = new ViewHolderParent();
					holderParent.btn_addNewProduct = (Button) convertView
							.findViewById(R.id.row_batch_addnewProduct);
					holderParent.tv_batchName = (TextView) convertView
							.findViewById(R.id.row_batches_batchName);
					convertView.setTag(holderParent);
				} else {
					holderParent = (ViewHolderParent) convertView.getTag();
				}
				if (groupExpand && Mode == TYPE_ADD) {
					holderParent.btn_addNewProduct.setVisibility(View.VISIBLE);
					holderParent.btn_addNewProduct
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									showDialogAdd(2);
								}
							});
				} else {
					holderParent.btn_addNewProduct.setVisibility(View.GONE);
				}
				holderParent.tv_batchName.setText(getGroup(groupPosition).getName()
						+ "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

	}

	public class ProductAdapter extends BaseExpandableListAdapter {

		// CustExpListview secondLevelexplv;

		class ViewHolderParent {
			TextView tv_productTitle;
		}

		class ViewHolderChild {
			TextView tv_upc;
			Button btn_verify;
			TextView tv_color;
			LinearLayout ll_main;
		}

		int childSelected = -1;
		int groupSelected;

		// int currentBatchesPosition;

		@Override
		public void onGroupCollapsed(int groupPosition) {

			super.onGroupCollapsed(groupPosition);

			try {
				childSelected = -1;
				batchAdapter.groupChildExpand = false;
				if (Mode == TYPE_NORMAL) {
					((BrowseActivity) getActivity()).hideRightFragment(1);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// groupExpand = false;
		}

		@Override
		public void onGroupExpanded(int groupPosition) {

			super.onGroupExpanded(groupPosition);

			groupSelected = groupPosition;
			batchAdapter.groupChildExpand = true;
			// batchAdapter.notifyDataSetChanged();
			notifyDataSetChanged();
		}

		public ProductAdapter() {

			// this.currentBatchesPosition = batchAdapter.groupSelected;
			// this.secondLevelexplv = secondLevelexplv;
		}

		@Override
		public Style_color getChild(int groupPosition, int childPosition) {
			if (batchAdapter.groupChildExpand) {
				groupPosition = groupSelected;
			}
			return arrayBatchs.get(batchAdapter.groupSelected).getProducts()
					.get(groupPosition).getStyle_color().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			if (batchAdapter.groupChildExpand) {
				groupPosition = groupSelected;
			}
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (batchAdapter.groupChildExpand) {
				groupPosition = groupSelected;
			}
			if (Mode == TYPE_ADD) {
				return 0;
			} else {
				return arrayBatchs.get(batchAdapter.groupSelected)
						.getProducts().get(groupPosition).getStyle_color()
						.size();
			}
		}

		@Override
		public Product getGroup(int groupPosition) {
			if (batchAdapter.groupChildExpand && Mode == TYPE_NORMAL) {
				Log.e("getGroup", groupSelected + "");
				groupPosition = groupSelected;
			}
			Log.e("getGroup", groupSelected + "");
			if(batchAdapter.groupSelected == -1)
			{
				return arrayBatchs.get(0).getProducts()
						.get(groupPosition);
			}
			return arrayBatchs.get(batchAdapter.groupSelected).getProducts()
					.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			if (batchAdapter.groupChildExpand && Mode == TYPE_NORMAL) {
				// Log.e("GO here", "GROUP EXPAN");
				return 1;
			} else {
				if(batchAdapter.groupSelected == -1)
				{
					return arrayBatchs.get(0).getProducts().size();
				}
				else
				{
				return arrayBatchs.get(batchAdapter.groupSelected)
						.getProducts().size();
				}
			}
		}

		@Override
		public long getGroupId(int groupPosition) {
			if (batchAdapter.groupChildExpand) {
				groupPosition = groupSelected;
			}
			return groupPosition;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			try {
				ViewHolderParent holderParent;
				if (convertView == null) {
					convertView = ((LayoutInflater) getActivity().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE)).inflate(
							R.layout.row_product, null);
					holderParent = new ViewHolderParent();
					holderParent.tv_productTitle = (TextView) convertView
							.findViewById(R.id.row_product_productTitle);
					convertView.setTag(holderParent);
				} else {
					holderParent = (ViewHolderParent) convertView.getTag();
				}

				holderParent.tv_productTitle.setText(getGroup(groupPosition)
						.getTitle());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			try {
				ViewHolderChild holderChild;
				if (convertView == null) {
					convertView = ((LayoutInflater) getActivity().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE)).inflate(
							R.layout.row_stylecolor, null);
					holderChild = new ViewHolderChild();
					holderChild.tv_color = (TextView) convertView
							.findViewById(R.id.row_stylecolor_tv_color);
					holderChild.tv_upc = (TextView) convertView
							.findViewById(R.id.row_stylecolor_tv_upc);
					holderChild.ll_main = (LinearLayout) convertView
							.findViewById(R.id.row_stylecolor_main);
					holderChild.btn_verify = (Button) convertView
							.findViewById(R.id.row_stylecorlor_verify);
					convertView.setTag(holderChild);
				} else {
					holderChild = (ViewHolderChild) convertView.getTag();
				}
				holderChild.tv_color.setText(getChild(groupPosition, childPosition)
						.getColor() + "");
				holderChild.tv_upc.setText(getChild(groupPosition, childPosition)
						.getStylecolor());
				holderChild.btn_verify.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						isVerifying = true;
						Intent intent = new Intent(getActivity(),
								CameraActivity.class);
						startActivityForResult(intent, 3);
					}
				});
				if (childPosition == childSelected) {
					// convertView.setBackgroundColor(Color.MAGENTA);
					holderChild.btn_verify.setVisibility(View.VISIBLE);
					holderChild.ll_main
							.setBackgroundResource(R.drawable.item_selected);
				} else {
					holderChild.ll_main.setBackgroundResource(R.drawable.bg_main);
					holderChild.btn_verify.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	AddProductRequest addProductRequest;
	AddBatchRequest addBatchRequest;

	void showDialogAdd(final int type) {

		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_addnew);

		Button btn_submit = (Button) dialog
				.findViewById(R.id.dialog_add_btn_sumit);
		Button btn_cancel = (Button) dialog
				.findViewById(R.id.dialog_add_btn_cancel);
		final EditText edt_name = (EditText) dialog
				.findViewById(R.id.dialog_add_edt_name);
		TextView tv_title = (TextView) dialog
				.findViewById(R.id.dialog_addnew_tv_title);
		if (type == 1) {
			tv_title.setText("Enter batch name:");
		}
		if (type == 2) {
			tv_title.setText("Enter product title:");
		}
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// batch
				if (type == 1) {
					Log.e("EDT", edt_name.getText().length() + "");
					if (edt_name.getText().toString().length() != 0) {
						nameBatchUpdate = edt_name.getText() + "";
						addBatchRequest.excute(edt_name.getText() + "");
						dialog.dismiss();
					} else {
						Toast.makeText(getActivity(),
								"Batch name must not be blank", 2000).show();
					}
				}
				// product
				if (type == 2) {
					Log.e("EDT", edt_name.getText().length() + "");
					nameProductUpdate = edt_name.getText() + "";
					if (edt_name.getText().toString().length() != 0) {
						addProductRequest.excute(
								batchAdapter.getGroup(
										batchAdapter.groupSelected).getId()
										+ "", edt_name.getText() + "");
						dialog.dismiss();
					} else {
						Toast.makeText(getActivity(),
								"Product title must not be blank", 2000).show();
					}
				}
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	public void onClick(View v) {
		if (v == btn_addbatch) {
			showDialogAdd(1);
		}
		if (v == btn_add) {
			((BrowseActivity) getActivity()).showAddFragment();
			btn_addbatch.setVisibility(View.VISIBLE);
			ll_normal.setVisibility(View.GONE);
		}
		if (v == btn_scan) {
			Intent intent = new Intent(getActivity(), CameraActivity.class);
			startActivityForResult(intent, 3);
		}
	}

	boolean isVerifying;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.e("CHECK", resultCode + "" + requestCode + "" + data);
		if (resultCode == 12 && data != null) {

			String upc = data.getExtras().getString("QRCode");

			Log.e("UPC - isverifying " + isVerifying, upc + "");
			if (isVerifying) {
				verifyUpcRequest.excute(
						upc,
						productAdapter.getChild(productAdapter.groupSelected,
								productAdapter.childSelected).getStylecolor()
								+ "");
			} else {
				scanUpcRequest.excute(upc);
			}
		}
		isVerifying = false;

	}

	final static public int TYPE_NORMAL = 3;
	final static public int TYPE_ADD = 4;

	int Mode = 3;

	public void switchMode(int type) {

		if (type == TYPE_ADD) {
			try {
				epl_product.setGroupIndicator(null);
				if (batchAdapter.getGroupCount() > 0) {
					epl_batch.collapseGroup(batchAdapter.getGroupCount() - 1);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (type == TYPE_NORMAL) {
			try {
				if (Mode == TYPE_ADD) {
					batchAdapter = new BatchAdapter();
					productAdapter = new ProductAdapter();
					epl_batch.setAdapter(batchAdapter);
					epl_product.setAdapter(productAdapter);
				}
				btn_addbatch.setVisibility(View.GONE);
				ll_normal.setVisibility(View.VISIBLE);
				epl_product.setGroupIndicator(getResources().getDrawable(
						R.drawable.custom_indicator2));
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Mode = type;
	}
}
