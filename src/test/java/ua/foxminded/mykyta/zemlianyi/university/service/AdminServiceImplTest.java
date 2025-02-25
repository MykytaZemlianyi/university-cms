package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;

@SpringBootTest(classes = { AdminServiceImpl.class })
class AdminServiceImplTest {

    @MockitoBean
    AdminDao adminDao;
    @Autowired
    AdminService adminService;

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

        assertThrows(IllegalArgumentException.class, () -> {
            adminService.addNew(adminWithSameEmail);
        });
    }

    @Test
    void addNew_shouldSaveAdmin_whenAdminIsVerified() {

        adminService.addNew(admin);

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
        doReturn(false).when(adminDao).existsById(admin.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.update(admin);
        });
    }

    @Test
    void update_shouldUpdate_whenAdminIsCorrectAndExistsInDb() {
        doReturn(true).when(adminDao).existsById(admin.getId());

        adminService.update(admin);

        verify(adminDao).save(admin);
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenAdminIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.changePassword(null);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenAdminIsInvalid() {
        Admin invalidAdmin = new Admin();
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.changePassword(invalidAdmin);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenAdminIsNotSavedInDb() {
        doReturn(Optional.empty()).when(adminDao).findById(admin.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            adminService.changePassword(admin);
        });
    }

    @Test
    void changePassword_shouldChangePassword_whenAdminIsValidAndExistsInDb() {
        doReturn(Optional.of(admin)).when(adminDao).findById(admin.getId());

        admin.setPassword("987654321");

        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);

        adminService.changePassword(admin);

        verify(adminDao).save(captor.capture());

        Admin savedAdmin = captor.getValue();
        assertEquals(admin.getId(), savedAdmin.getId());
        assertEquals(admin.getName(), savedAdmin.getName());
        assertEquals(admin.getSurname(), savedAdmin.getSurname());
        assertEquals(admin.getEmail(), savedAdmin.getEmail());

        assertEquals(admin.getPassword(), savedAdmin.getPassword());
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
}
