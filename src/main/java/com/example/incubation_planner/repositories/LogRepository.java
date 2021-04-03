package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.Idea;
import com.example.incubation_planner.models.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, String> {

    List<LogEntity> findAllByProjectNotNullOrderByTimeDesc();

    List<LogEntity> findAllByIdeaNotNullOrderByTimeDesc();

    List<LogEntity> findByIdea_Id(String id);

    List<LogEntity> findByProject_Id(String id);

    List<LogEntity> findByUser_Id(String id);
}
