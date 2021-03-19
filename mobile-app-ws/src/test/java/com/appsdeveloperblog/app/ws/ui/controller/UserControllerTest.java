package com.appsdeveloperblog.app.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.appsdeveloperblog.app.ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserControllerTest {

	// Instantiate the userController and then inject mocks like userService in
	// userController
	@InjectMocks
	UserController userController;

	@Mock
	UserServiceImpl userService;

	UserDto userDto;
	final String USER_ID = "sdjfltekou3ste";

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		userDto = new UserDto();
		userDto.setFirstName("Sanket");
		userDto.setLastName("Jain");
		userDto.setEmail("mr.sanketjain@gmail.com");
		userDto.setEmailVerificationStatus(Boolean.FALSE);
		userDto.setEmailVerificationToken(null);
		userDto.setUserId(USER_ID);
		userDto.setAddresses(getAddressesDto());
		userDto.setEncryptedPassword("xlkitoneds");
	}

	@Test
	void testGetUserString() {
		when(userService.getUserByUserId(anyString())).thenReturn(userDto);
	}

	private List<AddressDTO> getAddressesDto() {
		AddressDTO addressDto = new AddressDTO();
		addressDto.setType("shipping");
		addressDto.setCity("Sagar");
		addressDto.setCountry("India");
		addressDto.setPostalCode("470001");
		addressDto.setStreetName("Chota Karila");

		AddressDTO billingAddressDto = new AddressDTO();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("Sagar");
		billingAddressDto.setCountry("India");
		billingAddressDto.setPostalCode("470001");
		billingAddressDto.setStreetName("Chota Karila");

		List<AddressDTO> addresses = new ArrayList<AddressDTO>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);

		return addresses;
	}

}
