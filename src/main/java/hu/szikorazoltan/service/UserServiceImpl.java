package hu.szikorazoltan.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.szikorazoltan.model.Role;
import hu.szikorazoltan.model.Status;
import hu.szikorazoltan.model.User;
import hu.szikorazoltan.repo.RoleRepository;
import hu.szikorazoltan.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private EmailService emailService;

	private static final String USER_ROLE = "ROLE_USER";

	private Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }

    return Sort.Direction.ASC;
  }

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findById(Long id) {
		Optional<User> optional = userRepository.findById(id);
		if (optional.isPresent()){
			return optional.get();
		} else return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}

	@Override
	public String registerUser(User userToRegister) {
		Role userRole = roleRepository.findByRole(USER_ROLE);
		if (userRole != null) {
			userToRegister.getRoles().add(userRole);
		} else {
			userToRegister.addRoles(USER_ROLE);
		}

		userToRegister.setEnabled(false);
		String code = generateKey();
		userToRegister.setActivation(code);
		userToRegister.setStatus(Status.A);
		userToRegister.setPassword(passwordEncoder.encode(userToRegister.getPassword()));
		userRepository.save(userToRegister);
		emailService.sendMessage(userToRegister.getEmail(), userToRegister.getFirstName(), code);
		return "ok";
	}

	private String generateKey() {
		Random random = new Random();
		char[] word = new char[16];
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) ('a' + random.nextInt(26));
		}
		return new String(word);
	}

	@Override
	public String userActivation(String code) {
		User user = userRepository.findByActivation(code);
		if (user == null) return "noresult";
		user.setEnabled(true);
		user.setActivation(dateToString());
		userRepository.save(user);
		return "ok";
	}

	private String dateToString() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		Date today = Calendar.getInstance().getTime();
		return df.format(today);
	}

	@Override
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public String update(@Valid User user) {
		Optional<User> userInDb = userRepository.findById(user.getId());
		if (!(passwordEncoder.matches(user.getPassword(), userInDb.get().getPassword()))) {
			return "invalidPassword";
		}

		User userEmailExist = userRepository.findByEmail(user.getEmail());
		if (!userInDb.get().getEmail().equals(user.getEmail()) && userEmailExist != null)
			return "alreadyExistEmail";

		user.setPassword(userInDb.get().getPassword());
		user.setRoles(userInDb.get().getRoles());
		user.setStatus(userInDb.get().getStatus());
		user.setEnabled(true);
		user.setActivation(userInDb.get().getActivation());
		userRepository.save(user);
		return "ok";
	}

	@Override
	public String updateUserStatusById(long id, Status status) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()){
			if(status == Status.D) user.get().setEnabled(false);
				else user.get().setEnabled(true);

			user.get().setStatus(status);
			userRepository.save(user.get());
			return "ok";
		} else return null;
	}

	@Override
	public String addAdminRoleById(long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()){
			Set<Role> roles = user.get().getRoles();
			Role adminRole = roleRepository.findByRole("ROLE_ADMIN");
			roles.add(adminRole);
			user.get().setRoles(roles);
			userRepository.save(user.get());
			return "ok";
		} else return null;
	}

	@Override
	public void changeUserPassword(User user, String password) {
		//user.setPassword(passwordEncoder.encode(password)); //if we don't get bcyrpted password from frontend side
		user.setPassword(password);
		userRepository.save(user);
	}

	@Override
	public Map<String, Object> findPaginated(String username, int page, int size, String[] sort) {
		try {
      List<Order> orders = new ArrayList<Order>();

      if (sort[0].contains(",")) {
        for (String sortOrder : sort) {
          String[] _sort = sortOrder.split(",");
          orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
        }
      } else {
        orders.add(new Order(getSortDirection(sort[1]), sort[0]));
      }

      List<User> users = new ArrayList<User>();
      Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

      Page<User> pageUsers;
      if (username == null) pageUsers = userRepository.findAll(pagingSort);
      else pageUsers = userRepository.findByUsername(username, pagingSort);

      users = pageUsers.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("users", users);
			response.put("currentPage", pageUsers.getNumber());
			response.put("totalItems", pageUsers.getTotalElements());
			response.put("totalPages", pageUsers.getTotalPages());

			return response;
		} catch (Exception e) {
      return null;
    }

	}

}
