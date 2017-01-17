package se.group.tasksystem.api.exception;

import javax.ws.rs.core.Response.Status;

public final class ExceptionResponse {

	private Status status;
	private String message;

	public ExceptionResponse(Status badRequest, String message) {
		this.status = badRequest;
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
	protected ExceptionResponse(){
		
	}

}
