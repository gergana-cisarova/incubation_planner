package com.example.incubation_planner.services;

import com.example.incubation_planner.models.entity.Equipment;

import java.util.List;

public interface EquipmentService {
    void seedEquipment();

    Equipment findEquipment(String equipmentName);

    List<String> getAllEquipments();
}
