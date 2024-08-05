package br.com.williamschonarth.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private IUserRepository userRepository;

	@PostMapping("/")
	public ResponseEntity<?> create(@RequestBody UserModel user) {
		UserModel userByUsername = this.userRepository.findByUsername(user.getUsername());

		if (userByUsername != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already registered");
		}

		String passwordHashed = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
		user.setPassword(passwordHashed);

		UserModel createdUser = this.userRepository.save(user);
		createdUser.setPassword(null);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

}
