package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaisieIdentiteDemandeurController.class)
class SaisieIdentiteDemandeurControllerTest {
    private static final String CODE_POSTAL = "92000";
    private static final String TYPE_REMETTANT = "Autre";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void saisirInfosIdentiteDepuisAccueil_devrait_retourner_la_page_de_saisie_d_identite_quand_le_cookie_code_postal_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/saisie-identite-demandeur")
                .cookie(cookieTypeRemettant))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void saisirInfosIdentiteDepuisAccueil_devrait_retourner_la_page_de_saisie_d_identite_quand_le_cookie_type_remettant_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/saisie-identite-demandeur")
                .cookie(cookieCodePostal))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void saisirInfosIdentiteDepuisAccueil_devrait_retourner_la_page_de_saisie_d_identite_quand_les_cookies_sont_presents() throws Exception {
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/saisie-identite-demandeur")
                .cookie(cookieCodePostal, cookieTypeRemettant))
                .andExpect(view().name("saisie-identite-demandeur"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Informations sur le demandeur de masques")));
    }

    @Test
    void saisirInfosIdentite_devrait_creer_les_cookies_remettant_et_retourner_la_page_de_saisie_d_identite() throws Exception {
        // When Then
        String codePostal = "97420";
        String typeRemettant = "Mairie";
        mockMvc.perform(MockMvcRequestBuilders.post("/saisie-identite-demandeur")
                .param("code-postal", codePostal)
                .param("type-remettant", typeRemettant))
                .andExpect(view().name("saisie-identite-demandeur"))
                .andExpect(expectedCookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, codePostal))
                .andExpect(expectedCookie(NOM_COOKIE_TYPE_REMETTANT, typeRemettant))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Informations sur le demandeur de masques")));
    }

    private ResultMatcher expectedCookie(String nom, String valeur) {
        return matchAll(
                cookie().value(nom, valeur),
                cookie().httpOnly(nom, true),
                cookie().path(nom, "/"),
                cookie().secure(nom, false),
                cookie().maxAge(nom, DUREE_DE_VIE_COOKIE_JOURS * 24 * 60 * 60)
        );
    }
}
