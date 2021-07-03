package com.appsdeveloperblog.app.ws.io.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		// Prepare UserEntity
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("Sanket");
		userEntity.setLastName("Jain");
		userEntity.setUserId("2e1k5");
		userEntity.setEncryptedPassword("aaa");
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationStatus(true);

		//
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("kwtklspoi");
		addressEntity.setCity("Sagar");
		addressEntity.setCountry("USA");
		addressEntity.setPostalCode("10056");
		addressEntity.setStreetName("123 Street Address");

		List<AddressEntity> addresses = new ArrayList<>();
		addresses.add(addressEntity);

		userEntity.setAddresses(addresses);

		userRepository.save(userEntity);
	}

	@Test
	void test() {
		Pageable pageableRequest = PageRequest.of(0, 2);
		Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		Assertions.assertNotNull(pages);

		List<UserEntity> userEntities = pages.getContent();
		Assertions.assertNotNull(userEntities);
		Assertions.assertTrue(userEntities.size() == 1);
	}

}
