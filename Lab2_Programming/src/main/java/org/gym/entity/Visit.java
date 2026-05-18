package org.gym.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime visitTime;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    public Visit() {}

    public Visit(LocalDateTime visitTime) {
        this.visitTime = visitTime;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }
}