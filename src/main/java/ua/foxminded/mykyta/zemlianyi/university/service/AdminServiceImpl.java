package ua.foxminded.mykyta.zemlianyi.university.service;

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
    public Admin getByIdOrThrow(Long id) {
        return dao.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
    }

}
