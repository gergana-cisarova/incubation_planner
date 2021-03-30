package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.Idea;
import com.example.incubation_planner.models.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, String> {

    List<LogEntity> findAllByProjectNotNull();

    List<LogEntity> findAllByIdeaNotNull();
}
