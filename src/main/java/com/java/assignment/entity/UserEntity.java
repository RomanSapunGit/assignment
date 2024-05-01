package com.java.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table(name = "users")
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false, name = "first_name")
    String firstName;
    @Column(nullable = false, name = "last_name")
    String lastName;
    @Column(nullable = false, name = "birth_date")
    Date birthDate;
    String address;
    String phoneNumber;
}
