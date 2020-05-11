package fr.gouv.interieur.dmgp.remettant.application.ui;

import fr.gouv.interieur.dmgp.remettant.application.ui.data.DistributionMasqueUIMapper;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.DistribuerMasques;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie.SCAN;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DistributionMasquesController.class)
class DefaultExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DistributionMasqueUIMapper distributionMasqueUIMapper;
    @MockBean
    private DistribuerMasques distribuerMasques;

    @Test
    void distribuerMasques_devrait_retourner_la_page_d_erreur_quand_il_y_a_une_exception_avec_le_status_500() throws Exception {
        // Given
        String messageException = "Exception durant la distribution de masques";
        doThrow(new RuntimeException(messageException)).when(distribuerMasques).distribuer(anyList());
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, "CCAS");
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, "31000");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/distribution-masques")
                .cookie(cookieTypeRemettant, cookieCodePostal)
                .param("hashIdentite", "08c71b89ac55b6826a2daa9fe89edea26de")
                .param("nombreMineurs", "2")
                .param("modeSaisie", SCAN.name())
                .param("nbMasquesAdulteUsageUnique", "5")
                .param("nbMasquesAdulteReutilisables", "33"))
                .andExpect(view().name("erreur-500"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("500")));
    }
}
