package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    Admin admin = new Admin();

    @BeforeEach
    void setUp() {
        admin.setId(1L);
        admin.setName("Mykyta");
        admin.setSurname("Zemlianyi");
        admin.setEmail("mzeml@gmail.com");
        admin.setPassword("12345");

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getAdmins_shouldReturnCorrectModel() throws Exception {

        List<Admin> adminList = new ArrayList<>();
        adminList.add(admin);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Admin> adminPage = new PageImpl<Admin>(adminList, pageable, adminList.size());

        when(service.findAll(pageable)).thenReturn(adminPage);

        mockMvc.perform(get("/admin/admins").param("page", "0").param("size", "5")).andExpect(status().isOk())
                .andExpect(view().name("view-all-admins")).andExpect(model().attributeExists("admins"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("admins", adminPage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateAdminForm_shouldReturnModelWithNewAdmin() throws Exception {
        mockMvc.perform(get("/admin/add-new-admin")).andExpect(status().isOk()).andExpect(view().name("add-new-admin"))
                .andExpect(model().attributeExists("admin"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createAdmin_shouldRedirectWithSuccess_whenCreatedValidAdmin() throws Exception {
        mockMvc.perform(post("/admin/add-admin").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Marek").param("surname", "Szepski").param("email", "mszepski@gmail.com")
                .param("password", "12345")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/admins"))
                .andExpect(flash().attribute("successMessage", "Admin added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createAdmin_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Admin newAdmin = new Admin();
        newAdmin.setName("Mykyta");
        newAdmin.setSurname("Zemlianyi");
        newAdmin.setEmail("mzeml@gmail.com");
        newAdmin.setPassword("12345");

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newAdmin);

        mockMvc.perform(post("/admin/add-admin").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newAdmin.getName()).param("surname", newAdmin.getSurname())
                .param("email", newAdmin.getEmail()).param("password", newAdmin.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/admins"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

}
