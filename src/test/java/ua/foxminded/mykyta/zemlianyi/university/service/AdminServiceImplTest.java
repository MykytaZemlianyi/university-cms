package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.AdminDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.AdminNotFoundException;

@SpringBootTest(classes = { AdminServiceImpl.class })
class AdminServiceImplTest {

    @MockitoBean
    AdminDao adminDao;

    @MockitoBean
    PasswordEncoder encoder;

    @Autowired
    AdminServiceImpl adminService;

    Admin admin = new Admin();

    @BeforeEach
    void setUp() {
        admin.setId(1L);
        admin.setName("Maks");
        admin.setSurname("Maksymov");
        admin.setEmail("maks.maksymov@gmail.com");
        admin.setPassword("123456789");
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenAdminIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenAdminIsNotVerified() {
        Admin invalidAdmin = new Admin();
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.addNew(invalidAdmin);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenAdminWithSameEmailExists() {
        Admin adminWithSameEmail = new Admin();
        adminWithSameEmail.setId(2L);
        adminWithSameEmail.setName("Maksym");
        adminWithSameEmail.setSurname("Maksymenkov");
        adminWithSameEmail.setEmail(admin.getEmail());
        adminWithSameEmail.setPassword("123123123");
        doReturn(true).when(adminDao).existsByEmail(adminWithSameEmail.getEmail());

        assertThrows(AdminDuplicateException.class, () -> {
            adminService.addNew(adminWithSameEmail);
        });
    }

    @Test
    void addNew_shouldEncryptPasswordAndSaveAdmin_whenAdminIsVerified() {
        String rawPassword = admin.getPassword();
        when(encoder.encode(admin.getPassword())).thenReturn("encodedPassword");
        adminService.addNew(admin);

        verify(encoder).encode(rawPassword);
        verify(adminDao).save(admin);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenAdminIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenAdminIsNotVerified() {
        Admin invalidAdmin = new Admin();
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.update(invalidAdmin);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenAdminDoesNotExistsInDb() {
        doThrow(new AdminNotFoundException(admin.getId())).when(adminDao).findById(admin.getId());
        assertThrows(AdminNotFoundException.class, () -> {
            adminService.update(admin);
        });
    }

    @Test
    void update_shouldUpdate_whenAdminIsCorrectAndExistsInDb() {
        doReturn(Optional.of(admin)).when(adminDao).findById(admin.getId());
        doReturn("encodedPassword").when(encoder).encode(admin.getPassword());
        adminService.update(admin);

        verify(adminDao).save(admin);
    }

    @Test
    void update_shouldNotEncryptPassword_whenPasswordDidntChanged() {
        String rawPassword = admin.getPassword();
        doReturn(Optional.of(admin)).when(adminDao).findById(admin.getId());
        doReturn(true).when(encoder).matches(rawPassword, rawPassword);
        Admin updatedAdminWithSamePassword = admin;
        updatedAdminWithSamePassword.setName("Different-Name");

        adminService.update(updatedAdminWithSamePassword);

        verify(encoder, never()).encode(rawPassword);
        verify(adminDao).save(updatedAdminWithSamePassword);
    }

    @Test
    void update_shouldEncryptPassword_whenPasswordChanged() {
        String newPassword = "NewPassword1234";

        Admin updatedAdminWithNewPassword = new Admin();
        updatedAdminWithNewPassword.setId(1L);
        updatedAdminWithNewPassword.setName(admin.getName());
        updatedAdminWithNewPassword.setSurname(admin.getSurname());
        updatedAdminWithNewPassword.setEmail(admin.getEmail());
        updatedAdminWithNewPassword.setPassword(newPassword);

        doReturn(Optional.of(admin)).when(adminDao).findById(updatedAdminWithNewPassword.getId());
        when(encoder.encode(newPassword)).thenReturn("newEncryptedPassword");

        adminService.update(updatedAdminWithNewPassword);

        verify(encoder).encode(newPassword);
        verify(adminDao).save(admin);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_adminIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_adminIsInvalid() {
        Admin invalidAdmin = new Admin();
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.delete(invalidAdmin);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_whenAdminIsNotSavedInDb() {
        doReturn(false).when(adminDao).existsById(admin.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.delete(admin);
        });
    }

    @Test
    void delete_shouldDeleteAdmin_whenAdminIsValidAndExistsInDb() {
        doReturn(true).when(adminDao).existsById(admin.getId());

        adminService.delete(admin);

        verify(adminDao).delete(admin);
    }

    @Test
    void changePassword_shouldChangePasswordSuccessfully_whenCurrentPasswordMatches() {
        String username = "admin@example.com";
        String currentRawPassword = "oldPassword";
        String newRawPassword = "newPassword";

        admin.setEmail(username);
        admin.setPassword("encodedOldPassword");

        when(adminDao.findByEmail(username)).thenReturn(java.util.Optional.of(admin));
        when(encoder.matches(currentRawPassword, admin.getPassword())).thenReturn(true);
        when(encoder.encode(newRawPassword)).thenReturn("encodedNewPassword");
        when(adminDao.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Admin updatedAdmin = adminService.changePassword(username, currentRawPassword, newRawPassword);

        assertEquals("encodedNewPassword", updatedAdmin.getPassword());

        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminDao).save(adminCaptor.capture());
        assertEquals("encodedNewPassword", adminCaptor.getValue().getPassword());
    }

    @Test
    void changePassword_shouldThrowException_whenCurrentPasswordDoesNotMatch() {
        String username = "admin@example.com";
        String wrongCurrentPassword = "wrongPassword";
        String newRawPassword = "newPassword";

        admin.setEmail(username);
        admin.setPassword("encodedOldPassword");

        when(adminDao.findByEmail(username)).thenReturn(java.util.Optional.of(admin));
        when(encoder.matches(wrongCurrentPassword, admin.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> adminService.changePassword(username, wrongCurrentPassword, newRawPassword));

        verify(adminDao, never()).save(any());
    }

}
