package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.ActivityType;
import com.example.incubation_planner.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, String> {
    Optional<ActivityType> findByActivityName(String activityName);
List<ActivityType> findAll ();
}
