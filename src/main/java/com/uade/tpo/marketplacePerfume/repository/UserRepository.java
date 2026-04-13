package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByActiveTrue();

    List<User> findByActiveFalse();

    List<User> findByRoleAndActiveTrue(Role role);
}
