package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;
import ua.foxminded.mykyta.zemlianyi.university.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static Logger logger = LogManager.getLogger(AdminController.class.getName());
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admins")
    public String getAdmins(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> admins = adminService.findAll(pageable);

        model.addAttribute("admins", admins);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", admins.hasContent() ? admins.getTotalPages() : 1);

        return "view-all-admins";
    }

    @GetMapping("/add-new-admin")
    public String showCreateAdminForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "add-new-admin";
    }

    @PostMapping("/add-admin")
    public String createAdmin(@ModelAttribute("admin") Admin admin, RedirectAttributes redirectAttributes) {
        try {
            adminService.addNew(admin);
            redirectAttributes.addFlashAttribute("successMessage", "Admin added successfully!");
            return "redirect:/admin/admins";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/admin/admins";
        }
    }

    @GetMapping("/edit-admin/{id}")
    public String showEditAdminForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Admin> admin = adminService.findById(id);
        if (admin.isPresent()) {
            model.addAttribute("admin", admin.get());
            return "edit-admin";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Admin doesn't found");
            return "redirect:/admin/admins";
        }
    }

    @PostMapping("/edit-admin/{id}")
    public String updateAdmin(@PathVariable Long id, @ModelAttribute("admin") Admin updatedAdmin,
            @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        try {
            adminService.update(updatedAdmin);

            if (newPassword != null && !newPassword.isBlank()) {
                updatedAdmin.setPassword(newPassword);
                adminService.changePassword(updatedAdmin);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Admin updated successfully!");
            return "redirect:/admin/admins";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/admin/edit-admin/" + id;
        }
    }

    @DeleteMapping("/delete-admin/{id}")
    public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Admin> admin = adminService.findById(id);
            if (admin.isPresent()) {
                adminService.delete(admin.get());
                redirectAttributes.addFlashAttribute("successMessage", "Admin deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Admin does not exists");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/admins";
    }

}
