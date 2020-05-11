package fr.gouv.interieur.dmgp.remettant.application.ui.data;

import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import org.springframework.stereotype.Component;

import java.util.Map;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.*;

@Component
public class InfosDemandeurUIMapper {

    public InfosDemandeurUI toInfosDemandeurUI(Map<TypeMasque, Integer> nbMasquesDistribuesParType, IdentiteDemandeurUI identiteDemandeurUI) {
        return InfosDemandeurUI.builder()
                .identite(identiteDemandeurUI)
                .nbMasquesAdulteReutilisableDejaDistribues(nbMasquesDistribuesParType.getOrDefault(ADULTE_REUTILISABLE, 0))
                .nbMasquesAdulteUsageUniqueDejaDistribues(nbMasquesDistribuesParType.getOrDefault(ADULTE_USAGE_UNIQUE, 0))
                .nbMasquesEnfantReutilisableDejaDistribues(nbMasquesDistribuesParType.getOrDefault(ENFANT_REUTILISABLE, 0))
                .nbMasquesEnfantUsageUniqueDejaDistribues(nbMasquesDistribuesParType.getOrDefault(ENFANT_USAGE_UNIQUE, 0))
                .build();
    }

}
