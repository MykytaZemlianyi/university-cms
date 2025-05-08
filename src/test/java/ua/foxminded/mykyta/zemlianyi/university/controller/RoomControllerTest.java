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

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.service.RoomService;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RoomService service;

    Room room = new Room();

    @BeforeEach
    void setUp() {
        room.setId(1L);
        room.setNumber(123);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getRooms_shouldReturnCorrectModel() throws Exception {

        List<Room> roomList = new ArrayList<>();
        roomList.add(room);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Room> roomPage = new PageImpl<Room>(roomList, pageable, roomList.size());

        when(service.findAll(pageable)).thenReturn(roomPage);

        mockMvc.perform(get("/admin/rooms").param("page", "0").param("size", "5")).andExpect(status().isOk())
                .andExpect(view().name("view-all-rooms")).andExpect(model().attributeExists("rooms"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("rooms", roomPage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateRoomForm_shouldReturnModelWithNewRoom() throws Exception {
        mockMvc.perform(get("/admin/add-room")).andExpect(status().isOk()).andExpect(view().name("add-new-room"))
                .andExpect(model().attributeExists("room"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createRoom_shouldRedirectWithSuccess_whenCreatedValidRoom() throws Exception {
        mockMvc.perform(post("/admin/add-room").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", "100")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("successMessage", "Room added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createRoom_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Room newRoom = new Room();
        newRoom.setNumber(100);

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newRoom);

        mockMvc.perform(post("/admin/add-room").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", newRoom.getNumber().toString())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createRoom_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Room newRoom = new Room();
        newRoom.setNumber(null);

        mockMvc.perform(post("/admin/add-room").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", "")).andExpect(status().isOk()).andExpect(view().name("add-new-room"))
                .andExpect(model().attributeHasFieldErrors("room", "number"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditRoomForm_shouldReturnViewWithRoom_whenRoomExists() throws Exception {
        Optional<Room> roomOpt = Optional.of(room);
        when(service.findById(1L)).thenReturn(roomOpt);

        mockMvc.perform(get("/admin/edit-room/1")).andExpect(status().isOk()).andExpect(view().name("edit-room"))
                .andExpect(model().attribute("room", room));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditRoomForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        Optional<Room> emptyRoomOpt = Optional.empty();
        when(service.findById(1L)).thenReturn(emptyRoomOpt);

        mockMvc.perform(get("/admin/edit-room/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("errorMessage", "Error: " + Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateRoom_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        Room modifiedRoom = new Room();
        modifiedRoom.setId(1L);
        modifiedRoom.setNumber(101);

        mockMvc.perform(post("/admin/edit-room/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", modifiedRoom.getNumber().toString())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("successMessage", "Room updated successfully!"));

        verify(service).update(modifiedRoom);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateRoom_shouldRedirectWithError_whenUpdateFails() throws Exception {

        Room modifiedRoom = new Room();
        modifiedRoom.setId(1L);
        modifiedRoom.setNumber(101);

        when(service.update(modifiedRoom)).thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/admin/edit-room/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", modifiedRoom.getNumber().toString())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateRoom_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Room modifiedRoom = new Room();
        modifiedRoom.setNumber(null);

        mockMvc.perform(post("/admin/edit-room/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", "")).andExpect(status().isOk()).andExpect(view().name("edit-room"))
                .andExpect(model().attributeHasFieldErrors("room", "number"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteRoom_shouldRedirectWithSuccess_whenRoomExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(room));

        mockMvc.perform(delete("/admin/delete-room/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("successMessage", "Room deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteRoom_shouldRedirectWithError_whenRoomDoesNotExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/admin/delete-room/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("errorMessage", "Error: Room does not exists"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteRoom_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.findById(1L)).thenThrow(new IllegalArgumentException("Service error"));

        mockMvc.perform(delete("/admin/delete-room/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/rooms"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }

}
