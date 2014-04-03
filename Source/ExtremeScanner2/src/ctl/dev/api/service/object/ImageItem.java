package ctl.dev.api.service.object;

import com.google.gson.annotations.Expose;

public class ImageItem {

	@Expose
	private String id;
	@Expose
	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}