package com.lekwacious.poll.data.repository;

import com.lekwacious.poll.data.models.RoleName;
import com.lekwacious.poll.data.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByName(RoleName roleName);
}
