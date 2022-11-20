package com.cybage.login;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class userRepositoryTests {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		User user=new User();
		user.setEmail("shrikantkhurd97@gmail.com");
		user.setPassword("shri@123");
		user.setFirstName("shrikant");
		user.setLastName("khurd");
		
		User savedUser= userRepository.save(user);
		
		User existUser=entityManager.find(User.class, savedUser.getId());
		
		assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
	
		}
	@Test
	public void testFindByEmail() {
		String email = "samir1@gmail.com";
		User user = userRepository.findByEmail(email);
		
		//assertThat(user.getEmail()).isEqualTo(email);
		assertThat(user).isNotNull();
	}
		
}
