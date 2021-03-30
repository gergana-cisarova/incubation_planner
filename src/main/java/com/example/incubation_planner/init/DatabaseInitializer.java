package com.example.incubation_planner.init;

import com.example.incubation_planner.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final ActivityTypeService activityTypeService;
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final LabService labService;
    private final EquipmentService equipmentService;

    public DatabaseInitializer(ActivityTypeService activityTypeService, UserRoleService userRoleService, UserService userService, LabService labService, EquipmentService equipmentService) {
        this.activityTypeService = activityTypeService;
        this.userRoleService = userRoleService;
        this.userService = userService;
        this.labService = labService;
        this.equipmentService = equipmentService;
    }

    @Override
    public void run(String... args) throws Exception {

        userRoleService.initUserRoles();
        userService.initUsers();
        activityTypeService.seedActivityTypes();
        equipmentService.seedEquipment();
        labService.seedLabs();

    }


}
