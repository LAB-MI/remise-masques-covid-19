package fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors;

import org.springframework.stereotype.Component;

@Component
public class CreerEmpreinteIdentite {
    private final HashGenerator hashGenerator;

    public CreerEmpreinteIdentite(HashGenerator hashGenerator) {
        this.hashGenerator = hashGenerator;
    }

    public String creer(String hashEncode) {
        return hashGenerator.hasherAvecPepper(hashEncode);
    }

}
