package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers", schema = "university")
public class Teacher extends User {

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        clearCourses();

        for (Course course : this.courses) {
            course.setTeacher(this);
        }
        this.courses = courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
        course.setTeacher(this);
    }

    public void removeCourse(Course course) {
        boolean successRemoval = this.courses.remove(course);
        if (successRemoval) {
            course.setTeacher(null);
        }
    }

    public void clearCourses() {
        for (Course course : new HashSet<>(this.courses)) {
            course.setTeacher(null);
        }
        this.courses = new HashSet<>();
    }

}
