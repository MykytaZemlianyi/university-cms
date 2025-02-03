package ua.foxminded.mykyta.zemlianyi.university.service;

import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;

public interface AdminService {

    Admin addNew(Admin admin);

    Admin update(Admin admin);

    Admin changePassword(Admin admin);

}
