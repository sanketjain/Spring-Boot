package com.appsdeveloperblog.app.ws.shared;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

public class AmazonSES {

	final String FROM = "mr.sanketjain@gmail.com";

	final String SUBJECT = "One last step to complete your registration with PhotoApp";

	final String HTMLBODY = "<h1>Please verify your email address</h1>"
			+ "<p>Thank you for registering with our mobile app. To complete registration process and be able to login"
			+ "Click on the following link:"
			+ "<a href = 'http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
			+ "Final step to complete your registration" + "</a><br></br>"
			+ "Thank you! and we are waiting for you inside";

	final String TEXTBODY = "Please verify your email address"
			+ "Thank you for registering with our mobile app. To complete registration process and be able to login,"
			+ "open then the following link in your browser window:"
			+ "http://localhost:8080/verification-service/email-verification.html?token=$tokenValue"
			+ "Thank you! and we are waiting for you inside";

	public void verifyEmail(UserDto userDto) {
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_2)
				.build();

		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);

		client.sendEmail(request);
		System.out.println("Email sent!");
	}

}
