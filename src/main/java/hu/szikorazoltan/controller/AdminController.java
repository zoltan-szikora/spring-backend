package hu.szikorazoltan.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.szikorazoltan.model.Status;
import hu.szikorazoltan.model.User;
import hu.szikorazoltan.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

  @Autowired
	private UserService userService;

  @GetMapping("/users")
  public ResponseEntity<Map<String, Object>> getAllUsersPage(
    @RequestParam(required = false) String username,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int size,
    @RequestParam(defaultValue = "id,asc") String[] sort) {

    Map<String, Object> result = userService.findPaginated(username, page, size, sort);
    if(result == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/users/{id}/details")
	public ResponseEntity<User> showUserdetails(@PathVariable("id") long id) {
		User user = userService.findById(id);
    if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

  @PutMapping("/users/{id}/role")
	public ResponseEntity<Object> addAdminRole(@PathVariable("id") long id) {
		String result = userService.addAdminRoleById(id);
    if(result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(HttpStatus.OK);
	}

  @PutMapping("/users/{id}/status/{newStatus}")
	public ResponseEntity<String> setUserStatus(@PathVariable("id") long id, @PathVariable("newStatus") @Valid Status newStatus) {
		String result = userService.updateUserStatusById(id, newStatus);
    if(result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
