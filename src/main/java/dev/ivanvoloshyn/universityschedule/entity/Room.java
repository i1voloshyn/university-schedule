package dev.ivanvoloshyn.universityschedule.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @SequenceGenerator(name = "room_seq", sequenceName = "room_sequence", allocationSize = 1)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private Long roomNumber;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "room_description", nullable = false)
    private String roomDescription;

    @OneToMany(mappedBy = "room", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
