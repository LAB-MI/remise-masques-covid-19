package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfirmationDistributionMasquesController.class)
class ConfirmationDistributionMasquesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void confirmerDistribution_devrait_retourner_la_page_de_confirmation_de_distribution_de_masques() throws Exception {
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/confirmation-distribution-masques"))
                .andExpect(view().name("confirmation-distribution-masques"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Nouvelle remise de masques")));
    }
}
