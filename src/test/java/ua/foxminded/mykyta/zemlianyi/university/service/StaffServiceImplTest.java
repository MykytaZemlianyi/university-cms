package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import ua.foxminded.mykyta.zemlianyi.university.dao.StaffDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Staff;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StaffDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StaffNotFoundException;

@SpringBootTest(classes = { StaffServiceImpl.class })
class StaffServiceImplTest {

    @MockitoBean
    StaffDao staffDao;

    @MockitoBean
    PasswordEncoder encoder;

    @Autowired
    StaffServiceImpl staffService;

    Staff staff = new Staff();

    @BeforeEach
    void setUp() {
        staff.setId(1L);
        staff.setName("Maks");
        staff.setSurname("Maksymov");
        staff.setEmail("maks.maksymov@gmail.com");
        staff.setPassword("123456789");
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenStaffIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenStaffIsNotVerified() {
        Staff invalidStaff = new Staff();
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.addNew(invalidStaff);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenStaffWithSameEmailExists() {
        Staff staffWithSameEmail = new Staff();
        staffWithSameEmail.setId(2L);
        staffWithSameEmail.setName("Maksym");
        staffWithSameEmail.setSurname("Maksymenkov");
        staffWithSameEmail.setEmail(staff.getEmail());
        staffWithSameEmail.setPassword("123123123");
        doReturn(true).when(staffDao).existsByEmail(staffWithSameEmail.getEmail());

        assertThrows(StaffDuplicateException.class, () -> {
            staffService.addNew(staffWithSameEmail);
        });
    }

    @Test
    void addNew_shouldEncryptPasswordAndSaveStaff_whenStaffIsVerified() {
        String rawPassword = staff.getPassword();
        when(encoder.encode(staff.getPassword())).thenReturn("encodedPassword");
        staffService.addNew(staff);

        verify(encoder).encode(rawPassword);
        verify(staffDao).save(staff);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenStaffIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenStaffIsNotVerified() {
        Staff invalidStaff = new Staff();
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.update(invalidStaff);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenStaffDoesNotExistsInDb() {
        doThrow(new StaffNotFoundException(staff.getId())).when(staffDao).findById(staff.getId());
        assertThrows(StaffNotFoundException.class, () -> {
            staffService.update(staff);
        });
    }

    @Test
    void update_shouldUpdate_whenStaffIsCorrectAndExistsInDb() {
        doReturn(Optional.of(staff)).when(staffDao).findById(staff.getId());
        doReturn("encodedPassword").when(encoder).encode(staff.getPassword());
        staffService.update(staff);

        verify(staffDao).save(staff);
    }

    @Test
    void update_shouldNotEncryptPassword_whenPasswordDidntChanged() {
        String rawPassword = staff.getPassword();
        doReturn(Optional.of(staff)).when(staffDao).findById(staff.getId());
        doReturn(true).when(encoder).matches(rawPassword, rawPassword);
        Staff updatedStaffWithSamePassword = staff;
        updatedStaffWithSamePassword.setName("Different-Name");

        staffService.update(updatedStaffWithSamePassword);

        verify(encoder, never()).encode(rawPassword);
        verify(staffDao).save(updatedStaffWithSamePassword);
    }

    @Test
    void update_shouldEncryptPassword_whenPasswordChanged() {
        String newPassword = "NewPassword1234";

        Staff updatedStaffWithNewPassword = new Staff();
        updatedStaffWithNewPassword.setId(1L);
        updatedStaffWithNewPassword.setName(staff.getName());
        updatedStaffWithNewPassword.setSurname(staff.getSurname());
        updatedStaffWithNewPassword.setEmail(staff.getEmail());
        updatedStaffWithNewPassword.setPassword(newPassword);

        doReturn(Optional.of(staff)).when(staffDao).findById(updatedStaffWithNewPassword.getId());
        when(encoder.encode(newPassword)).thenReturn("newEncryptedPassword");

        staffService.update(updatedStaffWithNewPassword);

        verify(encoder).encode(newPassword);
        verify(staffDao).save(staff);
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenStaffIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.changePassword(null);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenStaffIsInvalid() {
        Staff invalidStaff = new Staff();
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.changePassword(invalidStaff);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenStaffIsNotSavedInDb() {
        doReturn(Optional.empty()).when(staffDao).findById(staff.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            staffService.changePassword(staff);
        });
    }

    @Test
    void changePassword_shouldChangePassword_whenStaffIsValidAndExistsInDb() {
        doReturn(Optional.of(staff)).when(staffDao).findById(staff.getId());

        staff.setPassword("987654321");

        ArgumentCaptor<Staff> captor = ArgumentCaptor.forClass(Staff.class);

        staffService.changePassword(staff);

        verify(staffDao).save(captor.capture());

        Staff savedStaff = captor.getValue();
        assertEquals(staff.getId(), savedStaff.getId());
        assertEquals(staff.getName(), savedStaff.getName());
        assertEquals(staff.getSurname(), savedStaff.getSurname());
        assertEquals(staff.getEmail(), savedStaff.getEmail());

        assertEquals(staff.getPassword(), savedStaff.getPassword());
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_staffIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_staffIsInvalid() {
        Staff invalidStaff = new Staff();
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.delete(invalidStaff);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_whenStaffIsNotSavedInDb() {
        doReturn(false).when(staffDao).existsById(staff.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            staffService.delete(staff);
        });
    }

    @Test
    void delete_shouldDeleteStaff_whenStaffIsValidAndExistsInDb() {
        doReturn(true).when(staffDao).existsById(staff.getId());

        staffService.delete(staff);

        verify(staffDao).delete(staff);
    }
}
