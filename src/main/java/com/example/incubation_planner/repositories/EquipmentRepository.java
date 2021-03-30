package com.example.incubation_planner.repositories;

import com.example.incubation_planner.models.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
    Optional<Equipment> findByEquipmentName(String equipmentName);
    List<Equipment> findAll ();
}
