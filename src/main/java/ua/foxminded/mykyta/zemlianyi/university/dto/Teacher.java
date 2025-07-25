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

    @OneToMany(mappedBy = "teacher", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Set<Course> courses = new HashSet<>();

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        clearCourses();

        for (Course course : courses) {
            course.setTeacher(this);
        }
        this.courses = courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
        course.setTeacher(this);
    }

    public void clearCourses() {
        for (Course course : new HashSet<>(this.courses)) {
            course.setTeacher(null);
        }
        this.courses = new HashSet<>();
    }

}
