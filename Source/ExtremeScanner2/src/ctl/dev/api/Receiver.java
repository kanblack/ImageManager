package ctl.dev.api;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ctl.dev.api.service.object.Batch;

public class Receiver<T> {

	@Expose
	private List<T> Result = new ArrayList<T>();

	public List<T> getResult() {
		return Result;
	}

	public void setResult(List<T> Result) {
		this.Result = Result;
	}
}
