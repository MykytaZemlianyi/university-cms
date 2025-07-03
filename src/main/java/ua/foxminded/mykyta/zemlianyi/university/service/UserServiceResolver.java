package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

@Service
public class UserServiceResolver {
    private AdminService adminService;
    private StaffService staffService;
    private StudentService studentService;
    private TeacherService teacherService;

    public UserServiceResolver(AdminService adminService, StaffService staffService, StudentService studentService,
            TeacherService teacherService) {
        this.adminService = adminService;
        this.staffService = staffService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    public UserService resolveUserService(String role) {
        switch (role) {
        case Constants.ROLE_ADMIN:
            return adminService;
        case Constants.ROLE_STAFF:
            return staffService;
        case Constants.ROLE_STUDENT:
            return studentService;
        case Constants.ROLE_TEACHER:
            return teacherService;
        default:
            throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

}
