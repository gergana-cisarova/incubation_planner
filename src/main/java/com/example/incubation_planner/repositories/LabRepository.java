package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface LabRepository extends JpaRepository<Lab, String> {

    List<Lab> findAllByEquipment_EquipmentName(String equipmentName);

    Optional<Lab> findByName(String labName);
}
