package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.Lab;
import com.example.incubation_planner.models.entity.Project;
import com.example.incubation_planner.models.service.LabServiceModel;
import com.example.incubation_planner.repositories.LabRepository;
import com.example.incubation_planner.services.EquipmentService;
import com.example.incubation_planner.services.LabService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class LabServiceImpl implements LabService {

    private final Resource labs;
    private final Gson gson;
    private final LabRepository labRepository;
    private final EquipmentService equipmentService;
    private final ModelMapper modelMapper;

    public LabServiceImpl(
            @Value("classpath:init/labs.json") Resource labs,
            Gson gson,
            LabRepository labRepository,
            EquipmentService equipmentService,
            ModelMapper modelMapper
    ) {

        this.labs = labs;
        this.gson = gson;
        this.labRepository = labRepository;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedLabs() {
        if (labRepository.count() == 0) {
            try {
                LabServiceModel[] labServiceModels = gson.fromJson(Files.readString(Path.of(labs.getURI())), LabServiceModel[].class);
                Arrays.stream(labServiceModels)
                        .forEach(m -> {
                            List<Project> emptyList = new ArrayList<>();
                            Lab current = modelMapper.map(m, Lab.class);
                            current.setEquipment(equipmentService.findEquipment(m.getEquipment()));
                            current.setProjects(emptyList);
                            labRepository.save(current);
                        });

            } catch (IOException e) {
                throw new IllegalStateException("Cannot seed Labs");
            }

        }
    }

    @Override
    public List<String> getAllLabs() {
        return labRepository.findAll().stream().map(Lab::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> findSuitableLabs(String providedEquipment) {
        return
                labRepository
                        .findAllByEquipment_EquipmentName(providedEquipment)
                        .stream()
                        .map(l -> l.getName())
                        .collect(Collectors.toList());
    }

    @Override
    public Lab findLab(String labName) {
        return labRepository.findByName(labName).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Map<String, String> getSuitableLabsWithProjects(String neededEquipment) {

        Map<String, String> info = new TreeMap<>();
        List<Lab> labs = labRepository
                .findAllByEquipment_EquipmentName(neededEquipment);

        labs
                .forEach(l -> {
                    List<Project> projects = l.getProjects();
                    projects.sort((p1,p2) -> p1.getStartDate().compareTo(p2.getStartDate()));
                    StringBuilder sb = new StringBuilder();
                    projects.forEach(p -> {
                        if(p.getEndDate().isAfter(LocalDateTime.now())) {
                            String currentProject =
                                    String.format("%02d %s %s (%02d:%02d) - %02d %s %s (%02d:%02d) <br />",
                                            p.getStartDate().getDayOfMonth(), p.getStartDate().getMonth(), p.getStartDate().getYear(), p.getStartDate().getHour(), p.getStartDate().getMinute(),
                                            p.getEndDate().getDayOfMonth(), p.getEndDate().getMonth(), p.getEndDate().getYear(), p.getEndDate().getHour(), p.getEndDate().getMinute());
                            sb.append(currentProject);
                        }});
                    info.put(l.getName(), sb.toString());
                });

        return info;
    }

    @Override
    public Map<String, String> getAllLabsWithProjects() {
        Map<String, String> info = new TreeMap<>();
        List<Lab> labs = labRepository
                .findAll();
        labs
                .forEach(l -> {
                    List<Project> projects = l.getProjects();
                    projects.sort((p1,p2) -> p1.getStartDate().compareTo(p2.getStartDate()));
                    StringBuilder sb = new StringBuilder();
                    projects.forEach(p -> {
                        if(p.getEndDate().isAfter(LocalDateTime.now())) {
                            String currentProject =
                                    String.format("%02d %s %s (%02d:%02d) - %02d %s %s (%02d:%02d) <br />",
                                            p.getStartDate().getDayOfMonth(), p.getStartDate().getMonth(), p.getStartDate().getYear(), p.getStartDate().getHour(), p.getStartDate().getMinute(),
                                            p.getEndDate().getDayOfMonth(), p.getEndDate().getMonth(), p.getEndDate().getYear(), p.getEndDate().getHour(), p.getEndDate().getMinute());
                            sb.append(currentProject);
                        }});
                    info.put(l.getName(), sb.toString());
                });

        return info;    }

}


//    private final LabRepository labRepository;
//
//    public LabServiceImpl(LabRepository labRepository) {
//        this.labRepository = labRepository;
//    }
//
//
//    @Override
//    public void initLabs() {
//
//        if (labRepository.count() == 0) {
//            for (LabName lab : LabName.values()) {
//                Lab newLab = new Lab();
//                newLab.setName(lab.name());
//                switch (lab.name()) {
//                    case "Leonardo": {
//                        newLab.setEquipment(Equipment.Wood_workshop);
//                        break;
//                    }
//                    case "Tesla": {
//                        newLab.setEquipment(Equipment.Metal_workshop);
//                        break;
//                    }
//                    case "Lumiere": {
//                        newLab.setEquipment(Equipment.Digital_production_workshop);
//                        break;
//                    }
//                    case "Bell": {
//                        newLab.setEquipment(Equipment.Prototyping_space);
//                        break;
//                    }
//                    default : {
//                        newLab.setEquipment(Equipment.Computers_Multimedia_Printers);
//                        break;
//                    }
//                    }
//                   this.labRepository.save(newLab);
//                }
//            }
//        }