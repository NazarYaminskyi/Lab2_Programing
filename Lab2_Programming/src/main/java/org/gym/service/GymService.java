package org.gym.service;

import org.gym.entity.Visit;
import org.gym.entity.Visitor;
import org.gym.entity.Trainer;
import org.gym.repository.VisitorRepository;
import org.gym.repository.TrainerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GymService {

    private final VisitorRepository visitorRepository;
    private final TrainerRepository trainerRepository;

    public GymService(VisitorRepository visitorRepository,
                      TrainerRepository trainerRepository) {
        this.visitorRepository = visitorRepository;
        this.trainerRepository = trainerRepository;
    }

    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public List<Visitor> getVisitorByName(String name) {
        return visitorRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Trainer> getTrainerByName(String name) {
        return trainerRepository.findByNameContainingIgnoreCase(name);
    }

    public Visitor getVisitorById(Long id) {
        return visitorRepository.findById(id).orElse(null);
    }

    public Visitor getVisitorByIdOrThrow(Long id) {
        return visitorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Visitor not found"
                ));
    }

    public Visitor addVisitor(String name) {
        Visitor visitor = new Visitor(name);
        return visitorRepository.save(visitor);
    }

    public void addVisit(Long visitorId) {

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Visitor not found"
                ));

        Visit visit = new Visit(LocalDateTime.now());

        visitor.addVisit(visit);

        visitorRepository.save(visitor);
    }

    public void deleteVisitor(Long id) {
        visitorRepository.deleteById(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id).orElse(null);
    }

    public Trainer getTrainerByIdOrThrow(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Trainer not found"
                ));
    }

    public Trainer addTrainer(String name, String specialization) {
        Trainer trainer = new Trainer(name, specialization);
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }

    public void assignTrainer(Long visitorId, Long trainerId) {

        Visitor visitor = getVisitorByIdOrThrow(visitorId);

        Trainer trainer = getTrainerByIdOrThrow(trainerId);
        if (!trainer.canAcceptMoreClients()) {
            throw new IllegalStateException(
                    "Trainer already has maximum clients");
        }
        visitor.setTrainer(trainer);

        visitorRepository.save(visitor);
    }

    public void removeTrainerFromVisitor(Long visitorId) {

        Visitor visitor = getVisitorByIdOrThrow(visitorId);

        visitor.setTrainer(null);

        visitorRepository.save(visitor);
    }

    public void updateTrainerSpecialization(Long id, String specialization) {

        Trainer trainer = getTrainerByIdOrThrow(id);

        trainer.setSpecialization(specialization);

        trainerRepository.save(trainer);
    }
}