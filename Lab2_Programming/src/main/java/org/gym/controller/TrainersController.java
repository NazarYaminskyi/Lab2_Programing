package org.gym.controller;

import org.gym.entity.Trainer;
import org.gym.service.GymService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TrainersController {

    private final GymService gymService;

    public TrainersController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping("/trainers")
    public String trainers(Model model) {
        model.addAttribute("trainers", gymService.getAllTrainers());
        return "trainers";
    }

    @PostMapping("/trainers/add")
    public String addTrainer(@RequestParam String name,
                             @RequestParam String specialization,
                             Model model) {

        try {

            gymService.addTrainer(name, specialization);

            return "redirect:/trainers";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error",
                    e.getMessage());

            model.addAttribute("trainers",
                    gymService.getAllTrainers());

            return "trainers";
        }
    }

    @PostMapping("/trainers/delete/{id}")
    public String deleteTrainer(@PathVariable Long id) {
        gymService.deleteTrainer(id);
        return "redirect:/trainers";
    }

    @GetMapping("/trainers/search")
    public String searchTrainers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            Model model) {

        if (id != null) {

            Trainer trainer = gymService.getTrainerById(id);

            if (trainer != null) {
                model.addAttribute("trainers", List.of(trainer));
            } else {
                model.addAttribute("trainers", List.of());
            }

        } else if (name != null && !name.isBlank()) {

            model.addAttribute("trainers",
                    gymService.getTrainerByName(name));

        } else {

            model.addAttribute("trainers",
                    gymService.getAllTrainers());
        }

        return "trainers";
    }

    @GetMapping("/trainers/{id}")
    public String trainerDetails(
            @PathVariable Long id,
            Model model) {

        Trainer trainer = gymService.getTrainerByIdOrThrow(id);

        model.addAttribute("trainer", trainer);

        return "trainer-details";
    }

    @PostMapping("/trainers/{id}/update-specialization")
    public String updateSpecialization(
            @PathVariable Long id,
            @RequestParam String specialization,
            RedirectAttributes redirectAttributes) {

        try {

            gymService.updateTrainerSpecialization(id, specialization);

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/trainers/" + id;
    }
}