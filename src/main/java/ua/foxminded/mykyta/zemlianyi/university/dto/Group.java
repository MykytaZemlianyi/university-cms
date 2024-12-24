package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups", schema = "university")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    private Set<Student> students;

    @ManyToMany
    @JoinTable(schema = "university", name = "groups_courses", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

}
