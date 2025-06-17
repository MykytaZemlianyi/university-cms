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
import ua.foxminded.mykyta.zemlianyi.university.dto.Staff;
import ua.foxminded.mykyta.zemlianyi.university.service.StaffService;

@Controller
@RequestMapping("/staff")
public class StaffController {
    private StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String getStaffs(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Staff> staff = staffService.findAll(pageable);

        model.addAttribute("staff", staff);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", staff.hasContent() ? staff.getTotalPages() : 1);

        return "view-all-staff";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateStaffForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "add-new-staff";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createStaff(@Valid @ModelAttribute("staff") Staff staff, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-staff";
        }

        staffService.addNew(staff);
        redirectAttributes.addFlashAttribute("successMessage", "Staff added successfully!");
        return "redirect:/staff";

    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String showEditStaffForm(@PathVariable Long id, Model model) {
        Staff staff = staffService.getByIdOrThrow(id);
        model.addAttribute("staff", staff);
        return "edit-staff";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String updateStaff(@PathVariable Long id, @Valid @ModelAttribute("staff") Staff updatedStaff,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-staff";
        }

        staffService.update(updatedStaff);
        redirectAttributes.addFlashAttribute("successMessage", "Staff updated successfully!");
        return "redirect:/staff";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteStaff(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        staffService.deleteOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Staff deleted successfully!");
        return "redirect:/staff";
    }

}
