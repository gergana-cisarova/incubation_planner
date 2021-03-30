package com.example.incubation_planner.web;

import com.example.incubation_planner.models.entity.UserRoleEntity;
import com.example.incubation_planner.models.entity.enums.UserRole;
import com.example.incubation_planner.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roles")
public class RolesController {
    private final UserService userService;

    public RolesController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("names", userService.findAllUsernamesExceptCurrent());

        return "role-add";
    }

    @PostMapping("/add")
    public String addConfirm(@RequestParam String username,
                             @RequestParam Optional<String> role) {
        List<String> roles = new java.util.ArrayList<>(List.of("USER"));
        if (role.isPresent()) {
           roles.add(role.get());
        }
        userService.changeRole(username, roles);

        return"redirect:/";

    }

}
