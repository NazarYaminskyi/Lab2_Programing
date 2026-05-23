package org.gym.controller;

import org.gym.entity.Visitor;
import org.gym.service.GymService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VisitorsController {

    private final GymService gymService;
    public VisitorsController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping("/visitors")
    public String visitors(Model model) {
        model.addAttribute("visitors", gymService.getAllVisitors());
        return "visitors";
    }

    @PostMapping("/visitors/add")
    public String addVisitor(
            @RequestParam String name,
            Model model) {

        try {

            gymService.addVisitor(name);

            return "redirect:/visitors";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error",
                    e.getMessage());

            model.addAttribute("visitors",
                    gymService.getAllVisitors());

            return "visitors";
        }
    }

    @PostMapping("/visitors/delete/{id}")
    public String deleteVisitor(@PathVariable Long id) {
        gymService.deleteVisitor(id);
        return "redirect:/visitors";
    }

    @GetMapping("/visitors/search")
    public String searchVisitors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            Model model) {

        if (id != null) {

            Visitor visitor = gymService.getVisitorById(id);

            if (visitor != null) {
                model.addAttribute("visitors",
                        List.of(visitor));
            }
            else {
                model.addAttribute("visitors",
                        List.of());
            }

        }
        else if (name != null && !name.isBlank()) {

            model.addAttribute("visitors",
                    gymService.getVisitorByName(name));
        }
        else {

            model.addAttribute("visitors",
                    gymService.getAllVisitors());
        }

        return "visitors";
    }

    @GetMapping("/visitors/{id}")
    public String visitorDetails(
            @PathVariable Long id,
            Model model) {

        Visitor visitor = gymService.getVisitorByIdOrThrow(id);

        model.addAttribute("visitor", visitor);
        model.addAttribute("trainers",
                gymService.getAllTrainers());

        return "visitor-details";
    }

    @PostMapping("/visitors/{id}/assign-trainer")
    public String assignTrainer(
            @PathVariable Long id,
            @RequestParam Long trainerId,
            RedirectAttributes redirectAttributes) {

        try {

            gymService.assignTrainer(id, trainerId);

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/visitors/" + id;
    }

    @PostMapping("/visitors/{id}/visit")
    public String addVisit(@PathVariable Long id) {

        gymService.addVisit(id);

        return "redirect:/visitors/" + id;
    }

    @PostMapping("/visitors/{id}/remove-trainer")
    public String removeTrainer(@PathVariable Long id) {

        gymService.removeTrainerFromVisitor(id);

        return "redirect:/visitors/" + id;
    }
}
