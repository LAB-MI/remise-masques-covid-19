package fr.gouv.interieur.dmgp.remettant.application.ui;

import fr.gouv.interieur.dmgp.remettant.application.ui.data.IdentiteDemandeurUI;
import fr.gouv.interieur.dmgp.remettant.application.ui.data.InfosDemandeurUI;
import fr.gouv.interieur.dmgp.remettant.application.ui.data.InfosDemandeurUIMapper;
import fr.gouv.interieur.dmgp.remettant.application.ui.fixtures.InfosDemandeurUIFixture;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.RecupererDistributionsMasques;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie.SCAN;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(RecuperationInfosDemandeurController.class)
class RecuperationInfosDemandeurControllerTest {
    private static final String CODE_POSTAL = "98450";
    private static final String TYPE_REMETTANT = "mairie";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecupererDistributionsMasques recupererDistributionsMasques;
    @MockBean
    private InfosDemandeurUIMapper infosDemandeurUIMapper;

    @Test
    void recupererInfosDemandeur_devrait_rediriger_vers_la_page_d_accueil_de_l_application_quand_le_cookie_code_postal_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/saisie-distribution-masques")
                .cookie(cookieTypeRemettant))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void recupererInfosDemandeur_devrait_rediriger_vers_la_page_d_accueil_de_l_application_quand_le_cookie_type_remettant_n_est_pas_present() throws Exception {
        // Given
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/saisie-distribution-masques")
                .cookie(cookieCodePostal))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void recupererInfosDemandeur_devrait_retourner_la_page_de_saisie_de_distribution_de_masques_quand_les_cookies_sont_presents() throws Exception {
        // Given
        String hashIdentite = "4ed6o/Jcn8puliRsyKswvY8/afhZ4RpVQJwsPjIWxsSinWEzBAO6UAmpA1GEOLUBRx0rArLaVFtCM4Fk/ZX8ig==";
        String nombreMineurs = "2";
        String modeSaisie = SCAN.name();
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);
        Map<TypeMasque, Integer> distributionMasques = new HashMap<>();
        distributionMasques.put(TypeMasque.ADULTE_REUTILISABLE, 2);
        distributionMasques.put(TypeMasque.ADULTE_USAGE_UNIQUE, 3);
        distributionMasques.put(TypeMasque.ENFANT_REUTILISABLE, 10);
        distributionMasques.put(TypeMasque.ENFANT_USAGE_UNIQUE, 8);
        when(recupererDistributionsMasques.verifier(hashIdentite)).thenReturn(distributionMasques);
        InfosDemandeurUI infosDemandeurUI = InfosDemandeurUIFixture.aInfosDemandeurUI();
        when(infosDemandeurUIMapper.toInfosDemandeurUI(eq(distributionMasques), any(IdentiteDemandeurUI.class)))
                .thenReturn(infosDemandeurUI);

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/saisie-distribution-masques")
                .cookie(cookieCodePostal)
                .cookie(cookieTypeRemettant)
                .param("hashIdentite", hashIdentite)
                .param("nombreMineurs", nombreMineurs)
                .param("modeSaisie", modeSaisie))
                .andExpect(model().attribute("infosDemandeur", equalTo(infosDemandeurUI)))
                .andExpect(view().name("distribution-masques"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Procéder à une remise de masques")));
    }

    @Test
    void recupererInfosDemandeur_ne_devrait_pas_fonctionner_si_le_hash_nest_pas_de_la_bonne_longueur() throws Exception {
        // Given
        String hashIdentite = "4ed6o/Jcn8puliRsyKswvY8/afhZ4RpVQJwsPjIWxsSinWEzBAO6UAmpA1GEOL";
        String nombreMineurs = "2";
        String modeSaisie = SCAN.name();
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);
        Map<TypeMasque, Integer> distributionMasques = new HashMap<>();
        distributionMasques.put(TypeMasque.ADULTE_REUTILISABLE, 2);
        distributionMasques.put(TypeMasque.ADULTE_USAGE_UNIQUE, 3);
        distributionMasques.put(TypeMasque.ENFANT_REUTILISABLE, 10);
        distributionMasques.put(TypeMasque.ENFANT_USAGE_UNIQUE, 8);
        when(recupererDistributionsMasques.verifier(hashIdentite)).thenReturn(distributionMasques);
        InfosDemandeurUI infosDemandeurUI = InfosDemandeurUIFixture.aInfosDemandeurUI();
        when(infosDemandeurUIMapper.toInfosDemandeurUI(eq(distributionMasques), any(IdentiteDemandeurUI.class)))
                .thenReturn(infosDemandeurUI);


        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/saisie-distribution-masques")
                .cookie(cookieCodePostal)
                .cookie(cookieTypeRemettant)
                .param("hashIdentite", hashIdentite)
                .param("nombreMineurs", nombreMineurs)
                .param("modeSaisie", modeSaisie))
                .andExpect(view().name("erreur-400"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recupererInfosDemandeur_ne_devrait_pas_fonctionner_si_le_hash_nest_pas_un_base64_valide() throws Exception {
        // Given
        String hashIdentite = "ééééé/Jcn8puliRsyKswvY8/afhZ4RpVQJwsPjIWxsSinWEzBAO6UAmpA1GEOLUBRx0rArLaVFtCM4Fk/ZX8ig==";
        String nombreMineurs = "2";
        String modeSaisie = SCAN.name();
        Cookie cookieCodePostal = new Cookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, CODE_POSTAL);
        Cookie cookieTypeRemettant = new Cookie(NOM_COOKIE_TYPE_REMETTANT, TYPE_REMETTANT);
        Map<TypeMasque, Integer> distributionMasques = new HashMap<>();
        distributionMasques.put(TypeMasque.ADULTE_REUTILISABLE, 2);
        distributionMasques.put(TypeMasque.ADULTE_USAGE_UNIQUE, 3);
        distributionMasques.put(TypeMasque.ENFANT_REUTILISABLE, 10);
        distributionMasques.put(TypeMasque.ENFANT_USAGE_UNIQUE, 8);
        when(recupererDistributionsMasques.verifier(hashIdentite)).thenReturn(distributionMasques);
        InfosDemandeurUI infosDemandeurUI = InfosDemandeurUIFixture.aInfosDemandeurUI();
        when(infosDemandeurUIMapper.toInfosDemandeurUI(eq(distributionMasques), any(IdentiteDemandeurUI.class)))
                .thenReturn(infosDemandeurUI);


        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/saisie-distribution-masques")
                .cookie(cookieCodePostal)
                .cookie(cookieTypeRemettant)
                .param("hashIdentite", hashIdentite)
                .param("nombreMineurs", nombreMineurs)
                .param("modeSaisie", modeSaisie))
                .andExpect(view().name("erreur-400"))
                .andExpect(status().isBadRequest());
    }
}
