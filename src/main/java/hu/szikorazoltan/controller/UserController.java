package hu.szikorazoltan.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.szikorazoltan.model.Status;
import hu.szikorazoltan.model.User;
import hu.szikorazoltan.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("api/user")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class UserController {

  @Autowired
	private UserService userService;

  @PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
		String res = userService.update(user);
    if (res.equals("invalidPassword")) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		if (res.equals("alreadyExistEmail")) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

  @PatchMapping("/update-password")
	public ResponseEntity<User> changeUserPassword (HttpServletRequest request, @RequestBody String newPassword) {
    Principal principal = request.getUserPrincipal();
    if (principal == null || principal.getName() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    User user = userService.findByUsername(principal.getName());
    userService.changeUserPassword(user, newPassword);
    return new ResponseEntity<>(user, HttpStatus.OK);
	}

  @PatchMapping("/{id}/status/{newStatus}")
	public ResponseEntity<User> updateStatus(
    HttpServletRequest request,
    @PathVariable("id") long id,
    @PathVariable("newStatus") @Valid Status newStatus) {

    Principal principal = request.getUserPrincipal();
    if (principal == null || principal.getName() == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    String res = userService.updateUserStatusById(id, newStatus);
		if (res.equals("ok")) {
      User user = userService.findByUsername(principal.getName());
      user.setStatus(newStatus);
      return new ResponseEntity<>(user, HttpStatus.OK);
		} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
