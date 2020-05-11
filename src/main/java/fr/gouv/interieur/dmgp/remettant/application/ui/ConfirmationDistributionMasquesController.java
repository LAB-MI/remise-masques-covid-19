package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfirmationDistributionMasquesController {

    @GetMapping("/confirmation-distribution-masques")
    public String confirmerDistribution(Model model) {
        return "confirmation-distribution-masques";
    }
}
