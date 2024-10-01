package ru.egorov.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.userservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
