package ua.foxminded.mykyta.zemlianyi.university.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import ua.foxminded.mykyta.zemlianyi.university.Constants;

@MappedSuperclass
public abstract class User implements Verifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean verify() {
        return verifyString(name) && verifyString(surname) && verifyString(email) && isEmail(email)
                && verifyString(password);
    }

    private boolean verifyString(String str) {
        return str != null && !str.isEmpty() && !str.isBlank();
    }

    public boolean isEmail(String email) {
        return email.matches(Constants.EMAIL_PATTERN_REGEX);
    }

}
