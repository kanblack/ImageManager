package ctl.dev.api.service.object;

import com.google.gson.annotations.Expose;

public class AddStyleObject {

	@Expose
	private String code;
	@Expose
	private Integer id;
	@Expose
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}