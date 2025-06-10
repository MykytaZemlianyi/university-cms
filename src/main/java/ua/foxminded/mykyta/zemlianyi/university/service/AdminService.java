package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;

public interface AdminService extends UserService<Admin> {

    Page<Admin> findAll(Pageable pageable);

    public Admin getByIdOrThrow(Long id);
}
