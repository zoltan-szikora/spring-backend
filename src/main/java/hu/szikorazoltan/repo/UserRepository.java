package hu.szikorazoltan.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.szikorazoltan.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByUsername(String username);

	User findByActivation(String code);

	Optional<User> findById(long id);

	List<User> findAll();

	Page<User> findAll(Pageable pageable);

	Page<User> findByUsername(String username, Pageable pageable);

}
