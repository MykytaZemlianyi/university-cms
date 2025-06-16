package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.GroupNotFoundException;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupService;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GroupService service;

    Group group = new Group();

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher();
        teacher.setName("Marek");
        teacher.setSurname("Szepski");

        Course course = new Course();
        course.setTeacher(teacher);

        Student student = new Student();

        group.setId(1L);
        group.setName("AA-11");
        group.addCourse(course);
        group.addStudent(student);
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void getGroups_shouldReturnCorrectModel(String username, String role) throws Exception {

        List<Group> groupList = new ArrayList<>();
        groupList.add(group);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Group> groupPage = new PageImpl<Group>(groupList, pageable, groupList.size());

        when(service.findAll(pageable)).thenReturn(groupPage);

        mockMvc.perform(get("/groups").param("page", "0").param("size", "5").with(user(username).roles(role)))
                .andExpect(status().isOk()).andExpect(view().name("view-all-groups"))
                .andExpect(model().attributeExists("groups")).andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages")).andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1)).andExpect(model().attribute("groups", groupPage));
    }

    private static Stream<Arguments> userRoles() {
        return Stream.of(Arguments.of("admin@gmail.com", "ADMIN"), Arguments.of("student@gmail.com", "STUDENT"),
                Arguments.of("teacher@gmail.com", "TEACHER"), Arguments.of("staff@gmail.com", "STAFF"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateGroupForm_shouldReturnModelWithNewGroup() throws Exception {
        mockMvc.perform(get("/groups/add")).andExpect(status().isOk()).andExpect(view().name("add-new-group"))
                .andExpect(model().attributeExists("group")).andExpect(model().attributeExists("studentPage"))
                .andExpect(model().attributeExists("coursePage"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createGroup_shouldRedirectWithSuccess_whenCreatedValidGroup() throws Exception {
        mockMvc.perform(post("/groups/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "AA-11")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("successMessage", "Group added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createGroup_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Group newGroup = new Group();
        newGroup.setName("AA-11");

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newGroup);

        mockMvc.perform(post("/groups/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newGroup.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createGroup_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Group newGroup = new Group();
        newGroup.setName(" ");

        mockMvc.perform(post("/groups/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newGroup.getName())).andExpect(status().isOk()).andExpect(view().name("add-new-group"))
                .andExpect(model().attributeHasFieldErrors("group", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditGroupForm_shouldReturnViewWithGroup_whenGroupExists() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(group);

        mockMvc.perform(get("/groups/edit/1")).andExpect(status().isOk()).andExpect(view().name("edit-group"))
                .andExpect(model().attribute("group", group));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditGroupForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new GroupNotFoundException(1L));

        mockMvc.perform(get("/groups/edit/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("errorMessage", "Error: Group with ID: 1 not found"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateGroup_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        Group modifiedGroup = new Group();
        modifiedGroup.setId(1L);
        modifiedGroup.setName("BB-22");

        mockMvc.perform(post("/groups/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedGroup.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("successMessage", "Group updated successfully!"));

        verify(service).update(modifiedGroup);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateGroup_shouldRedirectWithError_whenUpdateFails() throws Exception {

        Group modifiedGroup = new Group();
        modifiedGroup.setId(1L);
        modifiedGroup.setName("BB-22");

        when(service.update(modifiedGroup)).thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/groups/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedGroup.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateGroup_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Group modifiedGroup = new Group();
        modifiedGroup.setName(" ");

        mockMvc.perform(post("/groups/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedGroup.getName())).andExpect(status().isOk()).andExpect(view().name("edit-group"))
                .andExpect(model().attributeHasFieldErrors("group", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteGroup_shouldRedirectWithSuccess_whenGroupExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(group));

        mockMvc.perform(delete("/groups/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("successMessage", "Group deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteGroup_shouldRedirectWithError_whenGroupDoesNotExistsInDb() throws Exception {
        doThrow(new GroupNotFoundException(1L)).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/groups/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("errorMessage", "Error: Group with ID: 1 not found"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteGroup_shouldRedirectWithError_whenServiceFails() throws Exception {
        doThrow(new IllegalArgumentException("Service error")).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/groups/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }

}
