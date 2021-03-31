package com.example.incubation_planner.service.impl;

import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.models.entity.UserRoleEntity;
import com.example.incubation_planner.models.entity.enums.UserRole;
import com.example.incubation_planner.repositories.UserRepository;
import com.example.incubation_planner.services.impl.IncubationUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class IncubationUserServiceTest {

    private IncubationUserService serviceToTest;

    @Mock
    UserRepository mockUserRepository;

    @BeforeEach
    public void setUp() {
        serviceToTest = new IncubationUserService(mockUserRepository);
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class, () -> {
                    serviceToTest.loadUserByUsername("user_does_not_exists");
                });
    }

    @Test
    void testExistingUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("guest_user");
        userEntity.setPassword("123456789");
        UserRoleEntity roleUser = new UserRoleEntity();
        roleUser.setRole(UserRole.USER);
        UserRoleEntity roleAdmin = new UserRoleEntity();
        roleAdmin.setRole(UserRole.ADMIN);
        userEntity.setRoles(List.of(roleUser, roleAdmin));

        Mockito.when(mockUserRepository.findByUsername("guest_user"))
                .thenReturn(Optional.of(userEntity));
        UserDetails userDetails = serviceToTest.loadUserByUsername("guest_user");
        Assertions.assertEquals(userEntity.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(2, userDetails.getAuthorities().size());
        Assertions.assertEquals(2, userDetails.getAuthorities().size());
        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());
        Assertions.assertTrue(authorities.contains("ROLE_ADMIN"));
        Assertions.assertTrue(authorities.contains("ROLE_USER"));
    }
}
