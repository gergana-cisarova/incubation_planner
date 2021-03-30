package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.UserRoleEntity;
import com.example.incubation_planner.models.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {

   Optional< UserRoleEntity> findByRole(UserRole role);

}
