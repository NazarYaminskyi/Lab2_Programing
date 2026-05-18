package org.gym.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "visitors")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static final int NAME_MAX_LETTERS = 100;

    @Column(length = NAME_MAX_LETTERS)
    private String name;

    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = true)
    private Trainer trainer;

    public Visitor() {}

    public Visitor(String name) {
        setName(name);
    }

    // бізнес логіка

    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.setVisitor(this);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<Visit> getVisits() { return visits; }

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

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
