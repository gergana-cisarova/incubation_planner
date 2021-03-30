package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.models.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

   Optional<UserEntity> findByUsername(String username);
   List<UserEntity> findByRolesNotContaining(UserRoleEntity role);
}
