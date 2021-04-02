package com.example.incubation_planner.models.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "equipment")
public class Equipment extends BaseEntity{

    @Column(name = "equipment_name", unique = true, nullable = false)
    private String equipmentName;

    public String getEquipmentName() {
        return equipmentName;
    }

    public Equipment setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
        return this;
    }
}
