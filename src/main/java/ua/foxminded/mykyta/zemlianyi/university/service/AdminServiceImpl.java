package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;

@Service
public class AdminServiceImpl extends UserServiceImpl<Admin> implements AdminService {
    private static Logger logger = LogManager.getLogger(AdminServiceImpl.class.getName());

    public AdminServiceImpl(AdminDao adminDao, PasswordEncoder passwordEncoder) {
        super(adminDao, passwordEncoder);
    }

    @Override
    protected Admin mergeWithExisting(Admin newAdmin) {
        ObjectChecker.checkNull(newAdmin);
        Optional<Admin> existingAdminOpt = dao.findById(newAdmin.getId());

        if (existingAdminOpt.isPresent()) {
            Admin existingAdmin = existingAdminOpt.get();

            existingAdmin.setName(chooseString(newAdmin.getName(), existingAdmin.getName()));
            existingAdmin.setSurname(chooseString(newAdmin.getSurname(), existingAdmin.getSurname()));
            existingAdmin.setEmail(chooseString(newAdmin.getEmail(), existingAdmin.getEmail()));
            existingAdmin.setPassword(choosePassword(newAdmin.getPassword(), existingAdmin.getPassword()));

            return existingAdmin;
        } else {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND_ERROR);
        }
    }

    private String chooseString(String newString, String existingString) {
        if (newString == null || newString.isBlank() || newString.equals(existingString)) {
            return existingString;
        } else {
            return newString;
        }
    }

    private String choosePassword(String newPassword, String existingPassword) {
        if (newPassword == null || newPassword.isBlank() || newPassword.equals(existingPassword)) {
            return existingPassword;
        } else {
            return passwordEncoder.encode(newPassword);
        }
    }

}
