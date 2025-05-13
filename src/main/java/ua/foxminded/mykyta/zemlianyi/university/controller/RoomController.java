package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String getRooms(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Room> rooms = roomService.findAll(pageable);

        model.addAttribute("rooms", rooms);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rooms.hasContent() ? rooms.getTotalPages() : 1);

        return "view-all-rooms";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "add-new-room";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createRoom(@Valid @ModelAttribute Room room, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-room";
        }

        try {
            roomService.addNew(room);
            redirectAttributes.addFlashAttribute("successMessage", "Room added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showEditRoomForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Room> room = roomService.findById(id);
        if (room.isPresent()) {
            model.addAttribute("room", room.get());
            return "edit-room";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: " + Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST);
            return "redirect:/rooms";
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String updateRoom(@PathVariable Long id, @Valid @ModelAttribute("room") Room updatedRoom,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-room";
        }

        try {
            roomService.update(updatedRoom);
            redirectAttributes.addFlashAttribute("successMessage", "Room updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/rooms";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Room> room = roomService.findById(id);
            if (room.isPresent()) {
                roomService.delete(room.get());
                redirectAttributes.addFlashAttribute("successMessage", "Room deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Room does not exists");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/rooms";
    }
}
