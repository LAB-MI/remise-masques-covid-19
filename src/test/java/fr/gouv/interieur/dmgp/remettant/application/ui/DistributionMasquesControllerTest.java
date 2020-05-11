package fr.gouv.interieur.dmgp.remettant.application.ui;

import fr.gouv.interieur.dmgp.remettant.application.ui.data.DistributionMasqueUI;
import fr.gouv.interieur.dmgp.remettant.application.ui.data.DistributionMasqueUIMapper;
import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.fixtures.DistributionMasqueFixture;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.DistribuerMasques;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;
import java.util.List;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie.SCAN;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DistributionMasquesController.class)
class DistributionMasquesControllerTest {
    private static final String CODE_POSTAL = "98450";
    private static final String TYPE_REMETTANT = "mairie";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DistribuerMasques distribuerMasques;
    @MockBean
    private DistributionMasqueUIMapper distributionMasqueUIMapper;

    @Test
    void distribuerMasques_devrait_rediriger_vers_la_page_d_accueil_de_l_application_quand_le_cookie_code_postal_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/distribution-masques")
                .cookie(cookieTypeRemettant))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void distribuerMasques_devrait_rediriger_vers_la_page_d_accueil_de_l_application_quand_le_cookie_type_remettant_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/distribution-masques")
                .cookie(cookieCodePostal))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void distribuerMasques_devrait_distribuer_les_masques_et_retourner_la_page_de_confirmation_quand_les_cookies_sont_presents() throws Exception {
        // Given
        List<DistributionMasque> distributionMasques = List.of(DistributionMasqueFixture.aDistributionMasque(), DistributionMasqueFixture.aDistributionMasque());
        when(distributionMasqueUIMapper.toDistributionMasques(any(DistributionMasqueUI.class), eq(CODE_POSTAL), eq(TYPE_REMETTANT))).thenReturn(distributionMasques);
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/distribution-masques")
                .cookie(cookieCodePostal, cookieTypeRemettant)
                .param("hashIdentite", "08c71b89ac55b6826a2daa9fe89edea26de")
                .param("nombreMineurs", "2")
                .param("modeSaisie", SCAN.name())
                .param("nbMasquesAdulteUsageUnique", "5")
                .param("nbMasquesAdulteReutilisables", "33"))
                .andExpect(view().name("confirmation-distribution-masques"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Remise de masques validée")));

        verify(distribuerMasques).distribuer(distributionMasques);
    }
}
