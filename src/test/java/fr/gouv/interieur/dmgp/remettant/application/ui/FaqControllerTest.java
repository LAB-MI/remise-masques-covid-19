package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(FaqController.class)
class FaqControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void afficherFaq_devrait_retourner_la_page_de_faq() throws Exception {
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/faq"))
                .andExpect(view().name("faq"))
                .andExpect(status().isOk());
    }
}
