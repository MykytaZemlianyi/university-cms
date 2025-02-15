package ua.foxminded.mykyta.zemlianyi.university.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins", schema = "university")
public class Admin extends User implements Dto {

}
