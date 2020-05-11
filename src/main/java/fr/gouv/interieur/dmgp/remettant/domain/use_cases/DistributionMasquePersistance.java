package fr.gouv.interieur.dmgp.remettant.domain.use_cases;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;

import java.util.List;

public interface DistributionMasquePersistance {

    List<DistributionMasque> recupererParDemandeurSurLes15DerniersJours(String demandeur);

    void persister(List<DistributionMasque> distributionMasques);

}
