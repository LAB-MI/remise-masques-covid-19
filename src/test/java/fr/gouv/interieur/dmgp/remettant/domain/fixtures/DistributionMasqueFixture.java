package fr.gouv.interieur.dmgp.remettant.domain.fixtures;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;

import java.time.LocalDateTime;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie.MANUEL;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.ADULTE_USAGE_UNIQUE;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant.Mairie;

public class DistributionMasqueFixture {

    public static DistributionMasque aDistributionMasque(String demandeur, LocalDateTime dateDistribution, TypeMasque typeMasque) {
        return DistributionMasque.builder()
                .hashDemandeur(demandeur)
                .dateDistribution(dateDistribution)
                .nbPersonnes(4)
                .typeMasque(typeMasque)
                .nbMasques(11)
                .modeSaisie(MANUEL)
                .codePostal("75001")
                .typeRemettant(Mairie)
                .build();
    }

    public static DistributionMasque aDistributionMasque(Integer nbMasques) {
        return DistributionMasque.builder()
                .hashDemandeur("08c71b89ad55b6828a2daa9fe89edea26de")
                .dateDistribution(LocalDateTime.of(2018, 8, 5, 19, 43))
                .nbPersonnes(1)
                .typeMasque(ADULTE_USAGE_UNIQUE)
                .nbMasques(nbMasques)
                .modeSaisie(MANUEL)
                .codePostal("75001")
                .typeRemettant(Mairie)
                .build();
    }

    public static DistributionMasque aDistributionMasque() {
        return aDistributionMasque(23);
    }
}
