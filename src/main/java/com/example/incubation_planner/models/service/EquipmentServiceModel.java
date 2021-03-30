package com.example.incubation_planner.models.service;

import com.google.gson.annotations.Expose;

public class EquipmentServiceModel {
    private String id;
    @Expose
    private String equipmentName;

    public String getId() {
        return id;
    }

    public EquipmentServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public EquipmentServiceModel setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
        return this;
    }
}
