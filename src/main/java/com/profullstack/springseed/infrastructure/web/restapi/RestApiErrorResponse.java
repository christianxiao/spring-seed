package com.profullstack.springseed.infrastructure.web.restapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by christianxiao on 6/11/17.
 */
@Data
public class RestApiErrorResponse<T> {
	@JsonIgnore
	private int httpResponseCode;

    private String errorCode = "";
    private String message;
    private T response;

    public RestApiErrorResponse(){}

    public RestApiErrorResponse(RestApiException e){
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
		this.httpResponseCode = e.getHttpResponseCode();
    }

	public void write(HttpServletResponse response) throws IOException {
		response.setStatus(this.getHttpResponseCode());
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this));
		out.flush();
	}
}
