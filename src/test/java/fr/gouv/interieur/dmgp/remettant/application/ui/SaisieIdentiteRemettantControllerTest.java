package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaisieIdentiteRemettantController.class)
class SaisieIdentiteRemettantControllerTest {
    private static final String CODE_POSTAL = "98450";
    private static final String TYPE_REMETTANT = "mairie";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void accueil_devrait_retourner_la_page_d_accueil_de_l_application_quand_le_cookie_code_postal_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .cookie(cookieTypeRemettant))
                .andExpect(view().name("accueil"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Étapes à suivre afin de transmettre les masques")));
    }

    @Test
    void accueil_devrait_retourner_la_page_d_accueil_de_l_application_quand_le_cookie_type_remettant_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .cookie(cookieCodePostal))
                .andExpect(view().name("accueil"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Étapes à suivre afin de transmettre les masques")));
    }

    @Test
    void accueil_devrait_rediriger_vers_la_page_de_saisie_d_identite_quand_les_cookies_sont_presents() throws Exception {
        // Given
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .cookie(cookieCodePostal, cookieTypeRemettant))
                .andExpect(view().name("redirect:/saisie-identite-demandeur"))
                .andExpect(status().is3xxRedirection());
    }
}
