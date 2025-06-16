package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Staff;

public interface StaffService extends UserService<Staff> {

    Page<Staff> findAll(Pageable pageable);

    public Staff getByIdOrThrow(Long id);
}
