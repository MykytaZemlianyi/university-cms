package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses", schema = "university")
public class Course implements Verifiable {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses")
    private Set<Group> groups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setId(Long id) {
        if (id != null && id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    public void setName(String name) {
        if (verifyName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public void setTeacher(Teacher teacher) {
        if (teacher != null) {
            this.teacher = teacher;
        } else {
            throw new IllegalArgumentException("Teacher is null");
        }
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean verify() {
        return verifyName(this.name);
    }

    public boolean verifyName(String name) {
        return name != null && !name.isEmpty() && !name.isBlank();
    }

}
