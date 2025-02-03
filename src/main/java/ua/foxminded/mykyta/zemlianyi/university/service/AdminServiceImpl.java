package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;

public class AdminServiceImpl implements AdminService {
    private static Logger logger = LogManager.getLogger(AdminServiceImpl.class.getName());

    private AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Admin addNew(Admin admin) {
        verifyObject(admin);
        logger.info("Adding new admin - {}", admin);
        return adminDao.save(admin);
    }

    @Override
    public Admin update(Admin admin) {
        verifyObject(admin);
        if (adminDao.existsById(admin.getId())) {
            logger.info("Updating admin - {}", admin);
            return adminDao.save(admin);
        } else {
            throw new IllegalArgumentException(Constants.ADMIN_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    @Override
    public Admin changePassword(Admin admin) {
        verifyObject(admin);

        Optional<Admin> managedAdminOptional = adminDao.findById(admin.getId());

        if (managedAdminOptional.isPresent()) {
            Admin managedAdmin = managedAdminOptional.get();
            managedAdmin.setPassword(admin.getPassword());
            logger.info("Changed password for admin - {}", admin);
            return adminDao.save(managedAdmin);
        } else {
            throw new IllegalArgumentException(Constants.ADMIN_PASSWORD_CHANGE_ERROR);
        }
    }

    private void verifyObject(Admin admin) {
        if (admin == null || !admin.verify()) {
            throw new IllegalArgumentException(Constants.ADMIN_INVALID);
        }
    }
}
