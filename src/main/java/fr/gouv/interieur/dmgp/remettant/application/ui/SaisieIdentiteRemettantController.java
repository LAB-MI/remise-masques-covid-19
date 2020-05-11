package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;

@Controller
public class SaisieIdentiteRemettantController {
    @GetMapping("/")
    public String accueil(@CookieValue(value = NOM_COOKIE_CODE_POSTAL_REMETTANT, defaultValue = "") String codePostal,
                          @CookieValue(value = NOM_COOKIE_TYPE_REMETTANT, defaultValue = "") String typeRemettant) {
        if (StringUtils.isBlank(codePostal) || StringUtils.isBlank(typeRemettant)) {
            return "accueil";
        } else {
            return "redirect:/saisie-identite-demandeur";
        }
    }
}
