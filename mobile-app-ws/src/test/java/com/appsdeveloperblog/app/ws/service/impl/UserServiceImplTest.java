package com.appsdeveloperblog.app.ws.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.AmazonSES;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock 
	AmazonSES amazonSES;

	String userId = "akl53tklh";
	String encryptedPassword = "adstwekslwetjettt";

	UserEntity userEntity = new UserEntity();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity.setId(1L);
		userEntity.setFirstName("Sanket");
		userEntity.setLastName("Jain");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("mr.sanketjain@gmail.com");
		userEntity.setEmailVerificationToken("7sadkfotiu6");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	void testGetUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser("test@test.com");

		Assertions.assertNotNull(userDto);
		Assertions.assertEquals("Sanket", userDto.getFirstName());
	}

	@Test
	void testGetUser_UsernameNotFoundException() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});
	}

	@Test
	void testCreateUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("adaatwerhltk");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Sanket");
		userDto.setLastName("Jain");
		userDto.setPassword("12345678");
		userDto.setEmail("mr.sanketjain@gmail.com");

		UserDto storedUserDetails = userService.createUser(userDto);
		Assertions.assertNotNull(storedUserDetails);
		Assertions.assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		Assertions.assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		Assertions.assertNotNull(storedUserDetails.getUserId());
		Assertions.assertEquals(userEntity.getAddresses().size(), storedUserDetails.getAddresses().size());
		//verify(utils, times(2)).generateAddressId(30);
		//verify(bCryptPasswordEncoder, times(1)).encode("12345678");
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

	private List<AddressEntity> getAddressesEntity() {
		List<AddressDTO> addresses = getAddressesDto();

		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		return new ModelMapper().map(addresses, listType);
	}

}
