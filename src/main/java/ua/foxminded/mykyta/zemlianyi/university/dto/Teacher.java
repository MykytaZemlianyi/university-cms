package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers", schema = "university")
public class Teacher extends User implements Verifiable {

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<Course> courses;

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean verify() {
        // TODO Auto-generated method stub
        return false;
    }

}
