package hu.szikorazoltan.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.szikorazoltan.dto.UserRegistrationDto;
import hu.szikorazoltan.model.User;
import hu.szikorazoltan.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("api")
public class MainController {

	@Autowired
	private UserService userService;

	@PostMapping("/reg")
	public ResponseEntity<String> register(@RequestBody @Valid UserRegistrationDto user) {
		userService.registerUser(user.convertToUser());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/activation/{code}")
	public ResponseEntity<String> activation(@PathVariable("code") String code) {
		String res = userService.userActivation(code);
		if(res.equals("noresult")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/login")
  public ResponseEntity<User> login(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    if (principal == null || principal.getName() == null) {
      return new ResponseEntity<>(HttpStatus.OK);
    }
    User user = userService.findByUsername(principal.getName());

    if (user.getEnabled()) return new ResponseEntity<>(user, HttpStatus.OK);
     else return new ResponseEntity<>(HttpStatus.CONFLICT);
  }

}
