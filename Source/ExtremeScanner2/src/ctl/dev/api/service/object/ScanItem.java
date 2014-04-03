package ctl.dev.api.service.object;

import com.google.gson.annotations.Expose;

public class ScanItem {

	@Expose
	private String id;
	@Expose
	private String style;
	@Expose
	private String color;
	@Expose
	private String stylecolor;
	@Expose
	private String batch;
	@Expose
	private String product;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getStylecolor() {
		return stylecolor;
	}

	public void setStylecolor(String stylecolor) {
		this.stylecolor = stylecolor;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

}