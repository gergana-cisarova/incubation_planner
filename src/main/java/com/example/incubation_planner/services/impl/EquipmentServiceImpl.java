package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.ActivityType;
import com.example.incubation_planner.models.entity.Equipment;
import com.example.incubation_planner.models.service.EquipmentServiceModel;
import com.example.incubation_planner.repositories.EquipmentRepository;
import com.example.incubation_planner.services.EquipmentService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {


        private final Resource equipment;
        private final Gson gson;
        private final EquipmentRepository equipmentRepository;
        private final ModelMapper modelMapper;

    public EquipmentServiceImpl(
                @Value("classpath:init/equipment.json") Resource equipment,
                Gson gson,
                EquipmentRepository equipmentRepository,
                ModelMapper modelMapper
        ) {

            this.equipment = equipment;
            this.gson = gson;
            this.equipmentRepository = equipmentRepository;
            this.modelMapper = modelMapper;
        }

        @Override
        public void seedEquipment() {
            if (equipmentRepository.count() == 0) {
                try {
                    EquipmentServiceModel[] equipmentServiceModels = gson.fromJson(Files.readString(Path.of(equipment.getURI())), EquipmentServiceModel[].class);
                    Arrays.stream(equipmentServiceModels)
                            .forEach(e -> {
                                Equipment current = modelMapper.map(e, Equipment.class);
                                equipmentRepository.save(current);
                            });

                } catch (IOException e) {
                    throw new IllegalStateException("Cannot seed equipment");
                }

            }
        }

    @Override
    public Equipment findEquipment(String equipmentName) {
        return equipmentRepository.findByEquipmentName(equipmentName).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<String> getAllEquipments() {
           return equipmentRepository.findAll().stream().map(Equipment::getEquipmentName).collect(Collectors.toList());

    }

}
