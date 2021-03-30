package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.Idea;
import com.example.incubation_planner.models.entity.Project;
import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.view.ProjectBasicViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findAllByActiveTrueOrderByStartDateAsc();
    List<Project> findAllByActiveAndPromoterOrderByStartDate(boolean active, UserEntity promoter);

    @Query("SELECT p FROM Project p WHERE p.result is not null and p.sector= :sector ")
    List<Project> findAllResultsBySector(@Param("sector") Sector sector );
    List<Project> findAllByPromoterId(String id);

}
