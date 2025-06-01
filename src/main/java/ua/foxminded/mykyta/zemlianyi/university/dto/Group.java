package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "groups", schema = "university")
public class Group implements Verifiable, Dto {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 5, message = "Lenght < 5")
    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = "group", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Student> students = new HashSet<>();

    @ManyToMany
    @JoinTable(schema = "university", name = "groups_courses", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

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

    public void setStudents(Set<Student> newStudents) {
        this.students = newStudents;
    }

    public void assignStudents(Set<Student> students) {
        this.students = students;
        for (Student student : students) {
            student.setGroup(this);
        }
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.setGroup(this);
    }

    public void removeStudent(Student student) {
        boolean successRemoval = this.students.remove(student);
        if (successRemoval) {
            student.setGroup(null);
        }
    }

    public void clearStudents() {
        this.students = new HashSet<>();
    }

    public void setCourses(Set<Course> newCourses) {
        if (newCourses != null) {

            clearCourses();

            for (Course course : newCourses) {
                course.getGroups().add(this);
            }

            this.courses = newCourses;
        }
    }

    public void addCourse(Course course) {
        if (this.courses.add(course)) {
            course.getGroups().add(this);
        }
    }

    public void removeCourse(Course course) {
        if (this.courses.remove(course)) {
            course.getGroups().remove(this);
        }
    }

    public void clearCourses() {
        for (Course course : new HashSet<>(courses)) {
            course.getGroups().remove(this);
        }
        this.courses = new HashSet<>();
    }

    public void clearRelations() {
        clearCourses();
        clearStudents();
    }

    @Override
    public boolean verify() {
        return this.name != null && !this.name.isEmpty() && !this.name.isBlank();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

}
