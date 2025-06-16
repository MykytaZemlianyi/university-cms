package ua.foxminded.mykyta.zemlianyi.university.controller;

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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String getRooms(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Room> rooms = roomService.findAll(pageable);

        model.addAttribute("rooms", rooms);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", rooms.hasContent() ? rooms.getTotalPages() : 1);

        return "view-all-rooms";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "add-new-room";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String createRoom(@Valid @ModelAttribute Room room, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-room";
        }

        roomService.addNew(room);
        redirectAttributes.addFlashAttribute("successMessage", "Room added successfully!");
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String showEditRoomForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Room room = roomService.getByIdOrThrow(id);
        model.addAttribute("room", room);
        return "edit-room";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String updateRoom(@PathVariable Long id, @Valid @ModelAttribute("room") Room updatedRoom,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-room";
        }

        roomService.update(updatedRoom);
        redirectAttributes.addFlashAttribute("successMessage", "Room updated successfully!");
        return "redirect:/rooms";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        roomService.deleteByIdOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Room deleted successfully!");
        return "redirect:/rooms";
    }

    @PostMapping("/roomSelectRadioList")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String getRoomRadioList(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Long selectedRoomId,
            Model model) {
        Page<Room> roomPage = roomService.findAll(PageRequest.of(page, size));
        model.addAttribute("roomPage", roomPage);
        model.addAttribute("selectedRoomId", selectedRoomId);
        return "fragments/room_fragments :: roomSelectRadioList";
    }
}
