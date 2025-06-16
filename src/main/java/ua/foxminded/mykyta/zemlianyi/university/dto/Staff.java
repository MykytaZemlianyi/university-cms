package ua.foxminded.mykyta.zemlianyi.university.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff", schema = "university")
public class Staff extends User {

}
