package fr.gouv.interieur.dmgp.remettant.application.ui;

import javassist.NotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotFoundController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() throws NotFoundException {
        throw new NotFoundException(null);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
