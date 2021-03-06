package com.webrixtec.buildandbeautify.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webrixtec.buildandbeautify.exception.ExceptionController;
import com.webrixtec.buildandbeautify.model.AddCustomerRequest;
import com.webrixtec.buildandbeautify.model.ProductRequest;
import com.webrixtec.buildandbeautify.pojo.clientdashboardRequest;
import com.webrixtec.buildandbeautify.service.Request;

@RestController
@CrossOrigin
public class RequestController extends ExceptionController {
	
	@Autowired
	Request LCS;
	
	@PostMapping(value = "/create-client-enquiry")
	public ResponseEntity<Object> createEnquiry(@Valid @RequestBody clientdashboardRequest productRequest) throws Exception {
		return LCS.createEnquiry(productRequest);
	}
	
	@GetMapping(value = "/get-client-enquiry")
	public ResponseEntity<Object> getEnquiry() throws Exception {
		return LCS.getEnquiry();
	}
	
	@GetMapping(value = "/notify")
	public ResponseEntity<Object> getnotify() throws Exception {
		return LCS.getnotify();
	}
	@GetMapping(value ="/delnotify")
	public ResponseEntity<Object> delnotify(){
		return LCS.delnotify();
	}
}
