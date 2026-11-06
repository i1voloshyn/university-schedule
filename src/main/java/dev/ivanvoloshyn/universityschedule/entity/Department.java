package dev.ivanvoloshyn.universityschedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq")
    @SequenceGenerator(name = "department_seq", sequenceName = "department_sequence", allocationSize = 1)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "department_description", nullable = false)
    private String departmentDescription;

    @OneToMany(mappedBy = "department")
    private Set<Group> groups;

    @OneToMany(mappedBy = "department")
    private Set<Teacher> teachers;

    @OneToMany(mappedBy = "department")
    private Set<Room> rooms;

    @OneToMany(mappedBy = "department")
    private Set<Course> courses;

}
