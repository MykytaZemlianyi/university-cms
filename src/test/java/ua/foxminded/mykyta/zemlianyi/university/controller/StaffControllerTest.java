package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import ua.foxminded.mykyta.zemlianyi.university.dto.Staff;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StaffNotFoundException;
import ua.foxminded.mykyta.zemlianyi.university.service.StaffService;

@SpringBootTest
@AutoConfigureMockMvc
class StaffControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StaffService service;
    Staff staff = new Staff();

    @BeforeEach
    void setUp() {
        staff.setId(1L);
        staff.setName("Mykyta");
        staff.setSurname("Zemlianyi");
        staff.setEmail("mzeml@gmail.com");
        staff.setPassword("12345");

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getStaffs_shouldReturnCorrectModel() throws Exception {

        List<Staff> staffList = new ArrayList<>();
        staffList.add(staff);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Staff> staffPage = new PageImpl<Staff>(staffList, pageable, staffList.size());

        when(service.findAll(pageable)).thenReturn(staffPage);

        mockMvc.perform(get("/staff").param("page", "0").param("size", "5")).andExpect(status().isOk())
                .andExpect(view().name("view-all-staff")).andExpect(model().attributeExists("staff"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("staff", staffPage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateStaffForm_shouldReturnModelWithNewStaff() throws Exception {
        mockMvc.perform(get("/staff/add")).andExpect(status().isOk()).andExpect(view().name("add-new-staff"))
                .andExpect(model().attributeExists("staff"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createStaff_shouldRedirectWithSuccess_whenCreatedValidStaff() throws Exception {
        mockMvc.perform(post("/staff/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Marek").param("surname", "Szepski").param("email", "mszepski@gmail.com")
                .param("password", "12345")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/staff"))
                .andExpect(flash().attribute("successMessage", "Staff added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createStaff_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Staff newStaff = new Staff();
        newStaff.setName("Mykyta");
        newStaff.setSurname("Zemlianyi");
        newStaff.setEmail("mzeml@gmail.com");
        newStaff.setPassword("12345");

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newStaff);

        mockMvc.perform(post("/staff/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newStaff.getName()).param("surname", newStaff.getSurname())
                .param("email", newStaff.getEmail()).param("password", newStaff.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/staff"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createStaff_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Staff newStaff = new Staff();
        newStaff.setName(" ");
        newStaff.setSurname("Zemlianyi");
        newStaff.setEmail("mzeml@gmail.com");
        newStaff.setPassword("12345");

        mockMvc.perform(post("/staff/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newStaff.getName()).param("surname", newStaff.getSurname())
                .param("email", newStaff.getEmail()).param("password", newStaff.getPassword()))
                .andExpect(status().isOk()).andExpect(view().name("add-new-staff"))
                .andExpect(model().attributeHasFieldErrors("staff", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditStaffForm_shouldReturnViewWithStaff_whenStaffExists() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(staff);

        mockMvc.perform(get("/staff/edit/1")).andExpect(status().isOk()).andExpect(view().name("edit-staff"))
                .andExpect(model().attribute("staff", staff));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditStaffForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new StaffNotFoundException(1L));

        mockMvc.perform(get("/staff/edit/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff")).andExpect(
                        flash().attribute("errorMessage", "Error: 1 | Staff with this ID does not exist in database"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateStaff_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        Staff modifiedStaff = new Staff();
        modifiedStaff.setId(1L);
        modifiedStaff.setName("Marek");
        modifiedStaff.setSurname("Szepski");
        modifiedStaff.setEmail("mszepski@gmail.com");
        modifiedStaff.setPassword("12345");

        mockMvc.perform(post("/staff/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedStaff.getName()).param("surname", modifiedStaff.getSurname())
                .param("email", modifiedStaff.getEmail()).param("password", modifiedStaff.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/staff"))
                .andExpect(flash().attribute("successMessage", "Staff updated successfully!"));

        verify(service).update(modifiedStaff);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateStaff_shouldRedirectWithError_whenUpdateFails() throws Exception {

        Staff modifiedStaff = new Staff();
        modifiedStaff.setId(1L);
        modifiedStaff.setName("Marek");
        modifiedStaff.setSurname("Szepski");
        modifiedStaff.setEmail("mszepski@gmail.com");
        modifiedStaff.setPassword("12345");

        when(service.update(modifiedStaff)).thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/staff/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedStaff.getName()).param("surname", modifiedStaff.getSurname())
                .param("email", modifiedStaff.getEmail()).param("password", modifiedStaff.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/staff"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateStaff_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Staff modifiedStaff = new Staff();
        modifiedStaff.setName(" ");
        modifiedStaff.setSurname("Zemlianyi");
        modifiedStaff.setEmail("mzeml@gmail.com");
        modifiedStaff.setPassword("12345");

        mockMvc.perform(post("/staff/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedStaff.getName()).param("surname", modifiedStaff.getSurname())
                .param("email", modifiedStaff.getEmail()).param("password", modifiedStaff.getPassword()))
                .andExpect(status().isOk()).andExpect(view().name("edit-staff"))
                .andExpect(model().attributeHasFieldErrors("staff", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteStaff_shouldRedirectWithSuccess_whenStaffExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(staff));

        mockMvc.perform(delete("/staff/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/staff"))
                .andExpect(flash().attribute("successMessage", "Staff deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteStaff_shouldRedirectWithError_whenStaffDoesNotExistsInDb() throws Exception {
        doThrow(new StaffNotFoundException(1L)).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/staff/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/staff")).andExpect(
                        flash().attribute("errorMessage", "Error: 1 | Staff with this ID does not exist in database"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteStaff_shouldRedirectWithError_whenServiceFails() throws Exception {
        doThrow(new IllegalArgumentException("Service Error")).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/staff/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(
                        redirectedUrl("/staff"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));
    }

}
