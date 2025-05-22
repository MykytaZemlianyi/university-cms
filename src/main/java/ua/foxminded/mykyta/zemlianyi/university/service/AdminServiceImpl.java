package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.AdminDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.AdminNotFoundException;

@Service
public class AdminServiceImpl extends UserServiceImpl<Admin> implements AdminService {
    private static Logger logger = LogManager.getLogger(AdminServiceImpl.class.getName());

    public AdminServiceImpl(AdminDao adminDao, PasswordEncoder passwordEncoder) {
        super(adminDao, passwordEncoder);
    }

    @Override
    protected void uniqueEmailOrThrow(String email) {
        if (dao.existsByEmail(email)) {
            throw new AdminDuplicateException(email);
        }
    }

    @Override
    protected Admin mergeWithExisting(Admin newAdmin) {
        ObjectChecker.checkNullAndId(newAdmin);
        Optional<Admin> existingAdminOpt = dao.findById(newAdmin.getId());

        if (existingAdminOpt.isPresent()) {
            Admin existingAdmin = existingAdminOpt.get();

            existingAdmin.setName(newAdmin.getName());
            existingAdmin.setSurname(newAdmin.getSurname());
            existingAdmin.setEmail(newAdmin.getEmail());
            existingAdmin.setPassword(choosePassword(newAdmin.getPassword(), existingAdmin.getPassword()));

            return existingAdmin;
        } else {
            throw new AdminNotFoundException(newAdmin.getId());
        }
    }

    private String choosePassword(String newPassword, String existingPassword) {
        if (newPassword == null || newPassword.isBlank() || passwordEncoder.matches(newPassword, existingPassword)) {
            return existingPassword;
        } else {
            return passwordEncoder.encode(newPassword);
        }
    }

    public Admin getByIdOrThrow(Long id) {
        return dao.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
    }

}
