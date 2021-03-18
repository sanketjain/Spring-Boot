package com.appsdeveloperblog.app.ws.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
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

	String userId = "akl53tklh";
	String encryptedPassword = "adstwekslwetjettt";
	
	UserEntity userEntity = new UserEntity();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity.setId(1L);
		userEntity.setFirstName("Sanket");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("mr.sanketjain@gmail.com");
		userEntity.setEmailVerificationToken("7sadkfotiu6");
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
		
		AddressDTO addressDto = new AddressDTO();
		addressDto.setType("shipping");
		
		List<AddressDTO> addresses = new ArrayList<AddressDTO>();
		addresses.add(addressDto);
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(addresses);
		
		UserDto storedUserDetails = userService.createUser(userDto);
		Assertions.assertNotNull(storedUserDetails);
		Assertions.assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
	}

}
