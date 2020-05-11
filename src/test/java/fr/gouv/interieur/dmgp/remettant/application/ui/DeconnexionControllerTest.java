package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant.Association;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeconnexionController.class)
class DeconnexionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void seDeconnecter_devrait_vider_les_cookies_et_retourner_sur_la_page_d_accueil() throws Exception {
        // Given
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, "03100");
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, Association.name());

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/deconnexion")
                .cookie(cookieCodePostal, cookieTypeRemettant))
                .andExpect(view().name("redirect:/"))
                .andExpect(expectedEmptyCookie(NOM_COOKIE_CODE_POSTAL_REMETTANT))
                .andExpect(expectedEmptyCookie(NOM_COOKIE_TYPE_REMETTANT))
                .andExpect(status().is3xxRedirection());
    }

    private ResultMatcher expectedEmptyCookie(String nom) {
        return matchAll(
                cookie().value(nom, ""),
                cookie().maxAge(nom, 0)
        );
    }
}
