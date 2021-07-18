package hu.szikorazoltan.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import hu.szikorazoltan.model.Status;
import hu.szikorazoltan.model.User;

public interface UserService {

	User findByEmail(String email);

	User findByUsername(String username);

	User findById(Long id);

	String registerUser(User user);

	String userActivation(String code);

	List<User> findAll();

	String update(@Valid User user);

	String updateUserStatusById(long id, Status status);

	String addAdminRoleById(long id);

	void changeUserPassword(User user, String password);

	Map<String, Object> findPaginated(String username, int page, int size, String[] sort);

}
