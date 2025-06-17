package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.StaffDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Staff;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StaffDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StaffNotFoundException;

@Service
public class StaffServiceImpl extends UserServiceImpl<Staff> implements StaffService {
    private static Logger logger = LogManager.getLogger(StaffServiceImpl.class.getName());

    public StaffServiceImpl(StaffDao staffDao, PasswordEncoder passwordEncoder) {
        super(staffDao, passwordEncoder);
    }

    @Override
    protected void uniqueEmailOrThrow(String email) {
        if (dao.existsByEmail(email)) {
            throw new StaffDuplicateException(email);
        }
    }

    @Override
    public Staff getByIdOrThrow(Long id) {
        return dao.findById(id).orElseThrow(() -> new StaffNotFoundException(id));
    }

}
