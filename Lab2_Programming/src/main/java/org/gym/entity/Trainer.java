package org.gym.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static final int MAX_CLIENTS = 5;
    private static final int NAME_MAX_LETTERS = 100;
    private static final int SPEC_MAX_LETTERS = 50;

    @OneToMany(mappedBy = "trainer")
    private List<Visitor> clients = new ArrayList<>();

    @Column(length = NAME_MAX_LETTERS)
    private String name;

    @Column(length = SPEC_MAX_LETTERS)
    private String specialization;

    public Trainer() {
        // для Hibernate
    }

    public Trainer(String name, String specialization) {
        setName(name);
        setSpecialization(specialization);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public List<Visitor> getClients() {
        return clients;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Name cannot be empty");
        }
        if (name.length() > NAME_MAX_LETTERS) {
            throw new IllegalArgumentException(
                    "Name too long");
        }
        this.name = name.trim();
    }

    public void setSpecialization(String specialization) {

        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException(
                    "Specialization cannot be empty");
        }

        if (specialization.length() > SPEC_MAX_LETTERS) {
            throw new IllegalArgumentException(
                    "Specialization is too long");
        }

        this.specialization = specialization.trim();
    }


    public boolean canAcceptMoreClients() {
        return clients.size() < MAX_CLIENTS;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}