package eu.learn.ro.cloudvault.repository;

import eu.learn.ro.cloudvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}