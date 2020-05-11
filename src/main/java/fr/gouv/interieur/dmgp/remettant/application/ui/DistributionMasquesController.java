package fr.gouv.interieur.dmgp.remettant.application.ui;

import fr.gouv.interieur.dmgp.remettant.application.ui.data.DistributionMasqueUI;
import fr.gouv.interieur.dmgp.remettant.application.ui.data.DistributionMasqueUIMapper;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.DistribuerMasques;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;

@Controller
public class DistributionMasquesController {
    private final DistribuerMasques distribuerMasques;
    private final DistributionMasqueUIMapper distributionMasqueUIMapper;

    public DistributionMasquesController(DistribuerMasques distribuerMasques, DistributionMasqueUIMapper distributionMasqueUIMapper) {
        this.distribuerMasques = distribuerMasques;
        this.distributionMasqueUIMapper = distributionMasqueUIMapper;
    }

    @PostMapping("/distribution-masques")
    public String distribuerMasques(@ModelAttribute DistributionMasqueUI distributionMasqueUI,
                                    @CookieValue(value = NOM_COOKIE_CODE_POSTAL_REMETTANT, defaultValue = "") String codePostal,
                                    @CookieValue(value = NOM_COOKIE_TYPE_REMETTANT, defaultValue = "") String typeRemettant) {
        if (StringUtils.isBlank(codePostal) || StringUtils.isBlank(typeRemettant)) {
            return "redirect:/";
        }
        distribuerMasques.distribuer(distributionMasqueUIMapper.toDistributionMasques(distributionMasqueUI, codePostal, typeRemettant));
        return "confirmation-distribution-masques";
    }
}
