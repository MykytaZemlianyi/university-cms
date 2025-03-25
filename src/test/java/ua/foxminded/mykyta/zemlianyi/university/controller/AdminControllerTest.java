package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;
import ua.foxminded.mykyta.zemlianyi.university.service.AdminService;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AdminService service;

    @Test
    void getAdmins_shouldReturnCorrectModel() throws Exception {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Mykyta");
        admin.setSurname("Zemlianyi");
        admin.setEmail("mzeml@gmail.com");

        List<Admin> adminList = new ArrayList<>();
        adminList.add(admin);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Admin> adminPage = new PageImpl<Admin>(adminList, pageable, adminList.size());

        when(service.findAll(pageable)).thenReturn(adminPage);

        mockMvc.perform(
                get("/admins").param("page", "0").param("size", "5").with(user("zemlianoyne@gmail.com").roles("ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("/view-all-admins"))
                .andExpect(model().attributeExists("admins")).andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages")).andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1)).andExpect(model().attribute("admins", adminPage));
    }

}
