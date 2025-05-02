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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "courses", schema = "university")
public class Course implements Verifiable, Dto {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Lenght < 50")
    @Column(name = "course_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Lecture> lectures = new HashSet<>();

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
        this.teacher = teacher;
    }

    public void clearTeacher() {
        this.teacher.removeCourse(this);
    }

    public void setGroups(Set<Group> newGroups) {
        if (newGroups != null) {

            for (Group oldGroup : new HashSet<>(this.groups)) {
                oldGroup.getCourses().remove(this);
            }

            for (Group newGroup : newGroups) {
                newGroup.getCourses().add(this);
            }
        }
        this.groups = newGroups;

    }

    public void addGroup(Group group) {
        this.groups.add(group);
        group.addCourse(this);
    }

    public void removeGroup(Group group) {
        boolean successRemoval = this.groups.remove(group);
        if (successRemoval) {
            group.removeCourse(this);
        }
    }

    public void clearGroups() {
        for (Group group : new HashSet<>(groups)) {
            group.getCourses().remove(this);
        }
        this.groups = new HashSet<>();
    }

    public void clearRelations() {
        clearGroups();
        clearTeacher();
        clearLectures();
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.setCourse(this);
    }

    public void removeLecture(Lecture lecture) {
        this.lectures.remove(lecture);
        lecture.setCourse(null);
    }

    public void clearLectures() {
        for (Lecture lecture : new HashSet<>(this.lectures)) {
            lecture.setCourse(null);
        }
        this.lectures = new HashSet<>();
    }

    @Override
    public boolean verify() {
        return verifyName(this.name);
    }

    public boolean verifyName(String name) {
        return name != null && !name.isEmpty() && !name.isBlank();
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
        Course other = (Course) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

}
