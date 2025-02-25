package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;

public class AdminServiceImpl extends UserServiceImpl<Admin> implements AdminService {
    private static Logger logger = LogManager.getLogger(AdminServiceImpl.class.getName());

    public AdminServiceImpl(AdminDao adminDao) {
        super(adminDao);
    }

}
