package fr.gouv.interieur.dmgp.remettant.application.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_CODE_POSTAL_REMETTANT;
import static fr.gouv.interieur.dmgp.remettant.application.ui.SaisieIdentiteDemandeurController.NOM_COOKIE_TYPE_REMETTANT;

@Controller
public class DeconnexionController {

    @PostMapping("/deconnexion")
    public String seDeconnecter(HttpServletResponse httpServletResponse, HttpSession session) {
        httpServletResponse.addCookie(creerCookieInvalide(NOM_COOKIE_CODE_POSTAL_REMETTANT));
        httpServletResponse.addCookie(creerCookieInvalide(NOM_COOKIE_TYPE_REMETTANT));
        return "redirect:/";
    }

    private Cookie creerCookieInvalide(String nomCookie) {
        Cookie cookie = new Cookie(nomCookie, "");
        cookie.setMaxAge(0);
        return cookie;
    }
}
