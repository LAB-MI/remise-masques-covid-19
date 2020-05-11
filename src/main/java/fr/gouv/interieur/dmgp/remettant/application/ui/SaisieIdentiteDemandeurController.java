package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SaisieIdentiteDemandeurController {
    public static final String NOM_COOKIE_CODE_POSTAL_REMETTANT = "remettant-code-postal";
    public static final String NOM_COOKIE_TYPE_REMETTANT = "remettant-type";
    public static final int DUREE_DE_VIE_COOKIE_JOURS = 365;

    @Value("${cookie.secure}")
    private Boolean cookieSecure;

    @GetMapping("/saisie-identite-demandeur")
    public String saisirIdentiteDemandeurDepuisAccueil(@CookieValue(value = NOM_COOKIE_CODE_POSTAL_REMETTANT, defaultValue = "") String codePostal,
                                                       @CookieValue(value = NOM_COOKIE_TYPE_REMETTANT, defaultValue = "") String typeRemettant) {
        if (StringUtils.isBlank(codePostal) || StringUtils.isBlank(typeRemettant)) {
            return "redirect:/";
        }
        return "saisie-identite-demandeur";
    }

    @PostMapping("/saisie-identite-demandeur")
    public String saisirIdentiteDemandeur(@RequestParam(name = "code-postal") String codePostal,
                                          @RequestParam(name = "type-remettant") String typeRemettant,
                                          HttpServletResponse httpServletResponse) {
        Cookie cookieCodePostal = genererCookie(NOM_COOKIE_CODE_POSTAL_REMETTANT, codePostal);
        httpServletResponse.addCookie(cookieCodePostal);
        Cookie cookieTypeRemettant = genererCookie(NOM_COOKIE_TYPE_REMETTANT, typeRemettant);
        httpServletResponse.addCookie(cookieTypeRemettant);
        return "saisie-identite-demandeur";
    }

    private Cookie genererCookie(String cle, String valeur) {
        Cookie cookie = new Cookie(cle, valeur);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(cookieSecure);
        cookie.setMaxAge(DUREE_DE_VIE_COOKIE_JOURS * 24 * 60 * 60);
        return cookie;
    }
}
