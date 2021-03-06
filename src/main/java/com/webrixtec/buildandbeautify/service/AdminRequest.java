package com.webrixtec.buildandbeautify.service;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import com.webrixtec.buildandbeautify.exception.ExceptionController;
import com.webrixtec.buildandbeautify.model.UserModel;
import com.webrixtec.buildandbeautify.model.AddCustomerRequest;
import com.webrixtec.buildandbeautify.pojo.LoginRequest;
import com.webrixtec.buildandbeautify.repo.clientRepo;
import com.webrixtec.buildandbeautify.util.JwtUtils;


@Service
public class AdminRequest extends ExceptionController {

	@Autowired
	clientRepo clientRepo;
	@Autowired(required=true)
	private AuthenticationManager authenticationManager;
	@Autowired
	JwtUtils jwtUtils;

	public ResponseEntity<Object> createClient(AddCustomerRequest ClientRequest) {
		AddCustomerRequest clientDetails =new AddCustomerRequest();
		clientDetails.setAddress(ClientRequest.getAddress());
		
		ArrayList<String> categoryList = new ArrayList<String>();
		for(int i = 0;i < ClientRequest.getCategory().size(); i++) {
			categoryList.add(ClientRequest.getCategory().get(i)+",");
			System.out.println(ClientRequest.getCategory().get(i));
		}
		
		clientDetails.setCategory(categoryList);
		clientDetails.setComment(ClientRequest.getComment());
		clientDetails.setCustomername(ClientRequest.getCustomername());
		clientDetails.setEmail(ClientRequest.getEmail());
		clientDetails.setId(ClientRequest.getId());
		clientDetails.setLead(ClientRequest.getLead());
		clientDetails.setArea(ClientRequest.getArea());
		clientDetails.setPincode(ClientRequest.getPincode());
		clientDetails.setOthers(ClientRequest.getOthers());
		clientDetails.setPhone(ClientRequest.getPhone());
		clientDetails.setPhone2(ClientRequest.getPhone2());
		clientDetails.setProduct(ClientRequest.getProduct());
		clientDetails.setQuantity(ClientRequest.getQuantity());
		clientDetails.setType(ClientRequest.getType());
		clientDetails.setHandeld(ClientRequest.getHandeld());;
		clientDetails.setCreateDate(new Date());
		clientDetails.setModifiedDate(new Date());
		
		
		clientRepo.save(clientDetails);
		return response(HttpStatus.OK.value(), "client added Succcessfully", clientDetails);
	}

	

	public ResponseEntity<Object> getProductList() {
		List<AddCustomerRequest> productList = clientRepo.findAll();
		Collections.reverse(productList);
		return response(HttpStatus.OK.value(), "client list", productList);
	}



	public ResponseEntity<Object> updateclient( AddCustomerRequest ClientRequest) {
		if (ClientRequest.getId() == null) {
			return failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
					"client id must not to be null");
		}
		AddCustomerRequest clientDetails = clientRepo.findById(ClientRequest.getId()).get();
		if(clientDetails == null) {
			return failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
					"client details not available");
		}
		clientDetails.setAddress(ClientRequest.getAddress());
		
		ArrayList<String> categoryList = new ArrayList<String>();
		for(int i = 0;i < ClientRequest.getCategory().size(); i++) {
			categoryList.add(ClientRequest.getCategory().get(i)+",");
			System.out.println(ClientRequest.getCategory().get(i));
		}
		
		clientDetails.setCategory(categoryList);
		clientDetails.setComment(ClientRequest.getComment());
		clientDetails.setCustomername(ClientRequest.getCustomername());
		clientDetails.setEmail(ClientRequest.getEmail());
		clientDetails.setId(ClientRequest.getId());
		clientDetails.setPhone2(ClientRequest.getPhone2());
		clientDetails.setLead(ClientRequest.getLead());
		clientDetails.setHandeld(ClientRequest.getHandeld());;
		clientDetails.setOthers(ClientRequest.getOthers());
		clientDetails.setPhone(ClientRequest.getPhone());
		clientDetails.setProduct(ClientRequest.getProduct());
		clientDetails.setQuantity(ClientRequest.getQuantity());
		clientDetails.setType(ClientRequest.getType());
		clientDetails.setModifiedDate(new Date());
		
		clientRepo.save(clientDetails);
		return response(HttpStatus.OK.value(), "client updated Succcessfully", clientDetails);
	}



	public ResponseEntity<Object> deleteClient(Long clientId) {
		AddCustomerRequest clientDetail = clientRepo.findById(clientId).get();
		if (clientDetail == null) {
			return failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
					"Please check the Client details");
		}
		clientRepo.delete(clientDetail);
		return response(HttpStatus.OK.value(), "client deleted successfully");
	}









	

	

}
