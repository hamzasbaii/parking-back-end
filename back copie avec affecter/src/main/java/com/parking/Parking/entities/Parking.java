package com.parking.Parking.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private Boolean full;
    private String position;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL)
    private Set<Place> places;

    @ManyToMany(mappedBy = "managedParkings")
    @JsonIgnoreProperties("managedParkings")
    private Set<User> managers;


    public void setUser(User user) {
    }
}

