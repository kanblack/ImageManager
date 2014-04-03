package ctl.dev.api.service.object;

import com.google.gson.annotations.Expose;

public class VerifyResponse {

	@Expose
	private String code;
	@Expose
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}