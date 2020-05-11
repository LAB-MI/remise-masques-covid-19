package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotFoundController.class)
class NotFoundControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void notFoundController_devrait_toujours_lever_une_NotFoundException_et_afficher_la_page_erreur_404() throws Exception {
        // Given When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/error"))
                .andExpect(view().name("erreur-404"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("404")));
    }
}