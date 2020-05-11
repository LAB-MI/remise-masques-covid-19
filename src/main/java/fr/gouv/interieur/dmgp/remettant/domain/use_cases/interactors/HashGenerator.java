package fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class HashGenerator {
    private final String pepper;

    public HashGenerator(@Value("${hash.pepper}") String pepper) {
        if (StringUtils.isBlank(pepper)) {
            throw new IllegalArgumentException("La variable hash.pepper doit être valorisée");
        }
        this.pepper = pepper;
    }

    public String hasherAvecPepper(String chaine) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hash = messageDigest.digest(pepper.concat(chaine).getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new TypeDeHashNonPresentException(e);
        }
    }
}
