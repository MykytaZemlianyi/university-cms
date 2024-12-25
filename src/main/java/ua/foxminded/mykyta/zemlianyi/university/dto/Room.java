package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms", schema = "university")
public class Room implements Verifiable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_number")
    private Integer number;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<Lecture> lectures;

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }

    @Override
    public boolean verify() {
        // TODO Auto-generated method stub
        return false;
    }

}
