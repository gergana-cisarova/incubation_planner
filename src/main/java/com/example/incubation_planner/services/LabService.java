package com.example.incubation_planner.services;

import com.example.incubation_planner.models.entity.Lab;

import java.util.List;
import java.util.Map;

public interface LabService {

    void seedLabs();

    List<String> getAllLabs();

    List<String> findSuitableLabs(String providedEquipment);

    Lab findLab(String labName);

    Map<String, String> getSuitableLabsWithProjects(String neededEquipment);

    Map<String, String> getAllLabsWithProjects();
}

