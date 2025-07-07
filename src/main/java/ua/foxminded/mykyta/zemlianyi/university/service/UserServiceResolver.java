package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.User;

@Service
public class UserServiceResolver {
    private ApplicationContext applicationContext;

    public UserServiceResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public User getUserByEmailAndRole(String email, String role) {
        UserService<?> userService = applicationContext.getBean(Constants.USER_SERVICES.get(role));
        return userService.getByEmailOrThrow(email);
    }

    /**
     * Changes the password for a user identified by their email and role.
     *
     * @param email           the email of the user
     * @param role            the role of the user
     * @param currentPassword the current password of the user
     * @param newPassword     the new password to set for the user
     * @return the updated User object after changing the password
     */
    public User changePasswordForUserByEmailAndRole(String email, String role, String currentPassword,
            String newPassword) {
        UserService<?> userService = applicationContext.getBean(Constants.USER_SERVICES.get(role));
        return userService.changePassword(email, currentPassword, newPassword);
    }

}
