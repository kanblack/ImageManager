package ctl.dev.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Receiver2 {

	public class Response<T>
	{
		 @Expose
		    private Header header;
		    @Expose
		    private Body<T> body;

		    public Header getHeader() {
		        return header;
		    }

		    public void setHeader(Header header) {
		        this.header = header;
		    }

		    public Body<T> getBody() {
		        return body;
		    }

		    public void setBody(Body<T> body) {
		        this.body = body;
		    }
		
	}
	
	public class Header 
	{
		@SerializedName("Code")
		private String Code;
		@SerializedName("Message")
		private String Message;
		public String getCode() {
			return Code;
		}
		public void setCode(String code) {
			Code = code;
		}
		public String getMessage() {
			return Message;
		}
		public void setMessage(String message) {
			Message = message;
		}
		
		
	}
	
	public class Body<T>
	{
		private T Result ;

		public T getResult() {
			return Result;
		}

		public void setResult(T result) {
			Result = result;
		}
	}
}
