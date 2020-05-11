package fr.gouv.interieur.dmgp.remettant.infrastructure.fixtures;

import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.fixtures.ClockFixture;
import fr.gouv.interieur.dmgp.remettant.infrastructure.database.DistributionMasqueDatabase;

import java.time.LocalDateTime;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie.SCAN;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.ADULTE_REUTILISABLE;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant.Mairie;

public class DistributionMasqueDatabaseFixture {

    public static DistributionMasqueDatabase aDistributionMasqueDatabase(String demandeur, TypeMasque typeMasque) {
        return DistributionMasqueDatabase.builder()
                .demandeur(demandeur)
                .dateDistribution(LocalDateTime.now(ClockFixture.clock()))
                .nbPersonnes(2)
                .typeMasque(typeMasque.name())
                .nbMasques(5)
                .modeSaisie(SCAN.name())
                .codePostal("75001")
                .typeRemettant(Mairie.name())
                .build();
    }

    public static DistributionMasqueDatabase aDistributionMasqueDatabase(String demandeur) {
        return aDistributionMasqueDatabase(demandeur, ADULTE_REUTILISABLE);
    }

    public static DistributionMasqueDatabase aDistributionMasqueDatabase() {
        return aDistributionMasqueDatabase("08c71b89ac55b6826a2daa9fe89edea26de");
    }
}
