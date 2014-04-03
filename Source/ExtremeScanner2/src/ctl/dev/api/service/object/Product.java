package ctl.dev.api.service.object;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Product {

	public Product(String title, List<Style_color> style_color) {
		super();
		this.title = title;
		this.style_color = style_color;
	}

	@Expose
	private Integer id;
	@Expose
	private String title;
	@Expose
	private List<Style_color> style_color = new ArrayList<Style_color>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Style_color> getStyle_color() {
		return style_color;
	}

	public void setStyle_color(List<Style_color> style_color) {
		this.style_color = style_color;
	}

}
