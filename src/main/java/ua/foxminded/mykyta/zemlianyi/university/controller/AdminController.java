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
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;
import ua.foxminded.mykyta.zemlianyi.university.service.AdminService;

@Controller
@RequestMapping("/admins")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AdminController {
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String getAdmins(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Admin> admins = adminService.findAll(pageable);

        model.addAttribute("admins", admins);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", admins.hasContent() ? admins.getTotalPages() : 1);

        return "view-all-admins";
    }

    @GetMapping("/add")
    public String showCreateAdminForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "add-new-admin";
    }

    @PostMapping("/add")
    public String createAdmin(@Valid @ModelAttribute("admin") Admin admin, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-admin";
        }

        adminService.addNew(admin);
        redirectAttributes.addFlashAttribute("successMessage", "Admin added successfully!");
        return "redirect:/admins";

    }

    @GetMapping("/edit/{id}")
    public String showEditAdminForm(@PathVariable Long id, Model model) {
        Admin admin = adminService.getByIdOrThrow(id);
        model.addAttribute("admin", admin);
        return "edit-admin";
    }

    @PostMapping("/edit/{id}")
    public String updateAdmin(@PathVariable Long id, @Valid @ModelAttribute("admin") Admin updatedAdmin,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-admin";
        }

        adminService.update(updatedAdmin);
        redirectAttributes.addFlashAttribute("successMessage", "Admin updated successfully!");
        return "redirect:/admins";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Admin deleted successfully!");
        return "redirect:/admins";
    }

}
