package com.webrixtec.buildandbeautify.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.webrixtec.buildandbeautify.exception.ExceptionController;
import com.webrixtec.buildandbeautify.model.ServiceRequest;
import com.webrixtec.buildandbeautify.model.UserModel;
import com.webrixtec.buildandbeautify.model.ProductRequest;
import com.webrixtec.buildandbeautify.pojo.clientServicePojo;
import com.webrixtec.buildandbeautify.pojo.clientServiceUpdatePojo;
import com.webrixtec.buildandbeautify.pojo.clientdashboardRequest;
import com.webrixtec.buildandbeautify.repo.LoginClientRepo;
import com.webrixtec.buildandbeautify.repo.UserRepo;
import com.webrixtec.buildandbeautify.repo.clientServiceRepo;
import com.twilio.type.PhoneNumber;

@Service
public class Request extends ExceptionController {
	@Value("${file.upload-dir}")
	String dir;
	@Autowired
	LoginClientRepo LCR;
	@Autowired
	clientServiceRepo CSR;
	@Autowired
	UserRepo userRepo;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	FileStorageService FileStorageService;

	public static final String ACCOUNT_SID = "ACb836d8b8dd2f619e91253731498b3c8c";
	public static final String AUTH_TOKEN = "2e62967715dea11b3098104848a5b3c1";
	int notifycation = 0;
	int serviceNotify = 0;

	public ResponseEntity<Object> createEnquiry(clientdashboardRequest productRequest) throws MessagingException {

		UserModel user = userRepo.findById(productRequest.getClientId()).get();
		ProductRequest LCM = new ProductRequest();
		LCM.setUserModel(user);
		LCM.setComment(productRequest.getComment());
		LCM.setProduct2(productRequest.getProduct2());
		LCM.setProduct(productRequest.getProduct());
		ArrayList<String> categoryList = new ArrayList<String>();
		for (int i = 0; i < productRequest.getCategory().size(); i++) {
			categoryList.add(productRequest.getCategory().get(i) + ",");
			System.out.println(productRequest.getCategory().get(i));
		}

		LCM.setCategory(categoryList);
		LCM.setQuantity(productRequest.getQuantity());
		LCM.setOther(productRequest.getOther());
		LCM.setCreateDate(new Date());
		LCM.setModifiedDate(new Date());
		LCM.setRefName(productRequest.getRefName());
		LCM.setRefNo(productRequest.getRefNo());
		notifycation += 1;
		LCR.save(LCM);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		String htmlMsg = "Hi " + user.getName() + "<br /> "
				+ " Thanks for choosing<b> Srinivasaka Enterprises.</b><br /> "
				+ "<img src='http://3.110.219.120/images/logo.png'/>";
		helper.setText(htmlMsg, true); // Use this or above line.
		helper.setTo(user.getEmail());
		helper.setSubject("your Enqurie added succesFully");
		helper.setFrom("BuildAndBeautify <webrixtec@gamil.com>");
		mailSender.send(mimeMessage);

//		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//		Message message = Message.creator(
//                new com.twilio.type.PhoneNumber("+91"+user.getPhone()),
//                new com.twilio.type.PhoneNumber("(845) 668-6401"),
//                "your Enqurie added succesFully.Thanks for choosing Srinivasaka Enterprises. http://wa.me/+14155238886?text=join%20sing-from")
//            .create();
//
//        System.out.println(message);
//    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Message message1 = Message.creator(
//        		   new com.twilio.type.PhoneNumber("whatsapp:+91"+user.getPhone()), 
//                   new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),  
//                   "Your appointment is coming up on July 21 at 3PM")
//            .create();
//        System.out.println(message1);
		return response(HttpStatus.OK.value(), "Enquiry added Succcessfully", LCM);
	}

	public ResponseEntity<Object> getEnquiry() {
		List<ProductRequest> enquirys = LCR.findAll();
		Collections.reverse(enquirys);
		return response(HttpStatus.OK.value(), "enquirys list", enquirys);
	}

	public ResponseEntity<Object> getnotify() {
		return response(HttpStatus.OK.value(), "notify No:", notifycation);
	}

	public ResponseEntity<Object> delnotify() {
		notifycation = 0;
		return response(HttpStatus.OK.value(), "notify No:", notifycation);
	}

	public ResponseEntity<Object> createService(clientServicePojo request) throws IOException {
		ServiceRequest serviceModel = new ServiceRequest();
		if (request.getMfile() == null) {
			return failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
					"File must not empty");
		}
		UserModel user = userRepo.findById(request.getClientId()).get();
		serviceModel.setUserModel(user);
		serviceModel.setBillno(request.getBillno());
		serviceModel.setComment(request.getComment());
		serviceModel.setProductName(request.getProductName());
		serviceModel.setService(request.getService());
		serviceModel.setFilename(request.getMfile().getOriginalFilename());
		serviceNotify+=1;
//		// local save Images
//		String f = Path.of(dir, request.getMfile().getOriginalFilename()).toString();
//		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(f)));
//		stream.write(request.getMfile().getBytes());
//		stream.close();
//		System.err.println(f);
//		serviceModel.setImageURL(f);
		

		serviceModel.setImageURL(FileStorageService.uploadFile(request.getMfile(),"bill"));
		serviceModel.setCreateDate(new Date());
		serviceModel.setModifiedDate(new Date());
		CSR.save(serviceModel);
		return response(HttpStatus.OK.value(), "Request added Succcessfully", serviceModel);

	}

	public ResponseEntity<Object> getService() {
		List<ServiceRequest> Service = CSR.findAll();
		Collections.reverse(Service);
		return response(HttpStatus.OK.value(), "Service list", Service);

	}

	public ResponseEntity<Object> updateService(@Valid clientServiceUpdatePojo request) {

		ServiceRequest serviceModel = CSR.findById(request.getId()).get();
		if (serviceModel == null) {
			return failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
					"service Id is empty");
		}
		serviceModel.setStatus(request.getStatus());
		serviceModel.setDesc(request.getDesc());
//		// local save Images
//		String f = Path.of(dir, request.getMfile().getOriginalFilename()).toString();
//		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(f)));
//		stream.write(request.getMfile().getBytes());
//		stream.close();
//		System.err.println(f);
//		serviceModel.setImageURL(f);
		

//		serviceModel.setImageURL(FileStorageService.uploadFile(request.getMfile(),"bill"));
//		serviceModel.setCreateDate(new Date());
		serviceModel.setModifiedDate(new Date());
		CSR.save(serviceModel);
		return response(HttpStatus.OK.value(), "Request updated Succcessfully", serviceModel);
	}
	
	public ResponseEntity<Object> getServicenotify() {
		return response(HttpStatus.OK.value(), "notify No:", serviceNotify);
	}

	public ResponseEntity<Object> delSerivcenotify() {
		serviceNotify = 0;
		return response(HttpStatus.OK.value(), "notify No:", serviceNotify);
	}

}
