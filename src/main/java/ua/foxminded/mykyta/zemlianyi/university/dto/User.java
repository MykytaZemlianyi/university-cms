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
        if (id != null && id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("invalid ID");
        }
    }

    public void setName(String name) {
        if (verifyString(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public void setSurname(String surname) {
        if (verifyString(surname)) {
            this.surname = surname;
        } else {
            throw new IllegalArgumentException("Invalid surname");
        }
    }

    public void setEmail(String email) {
        if (isEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public void setPassword(String password) {
        if (verifyString(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
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
