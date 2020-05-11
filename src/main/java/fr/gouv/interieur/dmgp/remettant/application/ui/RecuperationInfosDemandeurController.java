package fr.gouv.interieur.dmgp.remettant.application.ui;

import fr.gouv.interieur.dmgp.remettant.application.ui.data.IdentiteDemandeurUI;
import fr.gouv.interieur.dmgp.remettant.application.ui.data.InfosDemandeurUIMapper;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.RecupererDistributionsMasques;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.InvalidParameterException;
import java.util.Map;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;
import static org.apache.tomcat.util.codec.binary.Base64.isBase64;

@Controller
public class RecuperationInfosDemandeurController {
    private final RecupererDistributionsMasques recupererDistributionsMasques;
    private final InfosDemandeurUIMapper infosDemandeurUIMapper;

    public RecuperationInfosDemandeurController(RecupererDistributionsMasques recupererDistributionsMasques, InfosDemandeurUIMapper infosDemandeurUIMapper) {
        this.recupererDistributionsMasques = recupererDistributionsMasques;
        this.infosDemandeurUIMapper = infosDemandeurUIMapper;
    }

    @PostMapping("/saisie-distribution-masques")
    public String recupererInfosDemandeur(@ModelAttribute IdentiteDemandeurUI identiteDemandeur,
                                          @CookieValue(value = NOM_COOKIE_CODE_POSTAL_REMETTANT, defaultValue = "") String codePostal,
                                          @CookieValue(value = NOM_COOKIE_TYPE_REMETTANT, defaultValue = "") String typeRemettant,
                                          Model model) {
        if (StringUtils.isBlank(codePostal) || StringUtils.isBlank(typeRemettant)) {
            return "redirect:/";
        } else if (!hashIdentiteEstValide(identiteDemandeur.getHashIdentite())) {
            throw new InvalidParameterException("Hash invalide");
        }
        Map<TypeMasque, Integer> nbMasquesDistribuesParType = recupererDistributionsMasques.verifier(identiteDemandeur.getHashIdentite());
        model.addAttribute(
                "infosDemandeur",
                infosDemandeurUIMapper.toInfosDemandeurUI(nbMasquesDistribuesParType, identiteDemandeur)
        );
        return "distribution-masques";
    }

    private boolean hashIdentiteEstValide(String hashIdentite) {
        boolean estDeLaBonneLongueur = hashIdentite.length() == 88;
        boolean estBienEnBase64 = isBase64(hashIdentite);
        return estDeLaBonneLongueur && estBienEnBase64;
    }
}
