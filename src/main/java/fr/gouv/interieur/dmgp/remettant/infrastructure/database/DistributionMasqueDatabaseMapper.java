package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant;
import org.springframework.stereotype.Component;

@Component
public class DistributionMasqueDatabaseMapper {

    public DistributionMasque toDistributionMasque(DistributionMasqueDatabase distributionMasqueDatabase) {
        return DistributionMasque.builder()
                .hashDemandeur(distributionMasqueDatabase.getDemandeur())
                .dateDistribution(distributionMasqueDatabase.getDateDistribution())
                .nbPersonnes(distributionMasqueDatabase.getNbPersonnes())
                .typeMasque(TypeMasque.valueOf(distributionMasqueDatabase.getTypeMasque()))
                .nbMasques(distributionMasqueDatabase.getNbMasques())
                .modeSaisie(ModeSaisie.valueOf(distributionMasqueDatabase.getModeSaisie()))
                .codePostal(distributionMasqueDatabase.getCodePostal())
                .typeRemettant(TypeRemettant.valueOf(distributionMasqueDatabase.getTypeRemettant()))
                .build();
    }

    public DistributionMasqueDatabase toDistributionMasqueDatabase(DistributionMasque distributionMasque) {
        return DistributionMasqueDatabase.builder()
                .demandeur(distributionMasque.getHashDemandeur())
                .dateDistribution(distributionMasque.getDateDistribution())
                .nbPersonnes(distributionMasque.getNbPersonnes())
                .typeMasque(distributionMasque.getTypeMasque().name())
                .nbMasques(distributionMasque.getNbMasques())
                .modeSaisie(distributionMasque.getModeSaisie().name())
                .codePostal(distributionMasque.getCodePostal())
                .typeRemettant(distributionMasque.getTypeRemettant().name())
                .build();
    }
}
