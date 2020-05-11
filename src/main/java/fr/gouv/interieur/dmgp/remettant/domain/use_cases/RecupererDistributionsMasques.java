package fr.gouv.interieur.dmgp.remettant.domain.use_cases;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors.CreerEmpreinteIdentite;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RecupererDistributionsMasques {

    private final CreerEmpreinteIdentite creerEmpreinteIdentite;
    private final DistributionMasquePersistance distributionMasquePersistance;

    public RecupererDistributionsMasques(CreerEmpreinteIdentite creerEmpreinteIdentite, DistributionMasquePersistance distributionMasquePersistance) {
        this.creerEmpreinteIdentite = creerEmpreinteIdentite;
        this.distributionMasquePersistance = distributionMasquePersistance;
    }

    public Map<TypeMasque, Integer> verifier(String hashIdentite) {
        String hashDemandeur = creerEmpreinteIdentite.creer(hashIdentite);
        List<DistributionMasque> distributionMasques = distributionMasquePersistance.recupererParDemandeurSurLes15DerniersJours(hashDemandeur);
        return recuperer(distributionMasques);
    }

    private Map<TypeMasque, Integer> recuperer(List<DistributionMasque> distributionMasques) {
        return distributionMasques.stream()
                .collect(
                        Collectors.groupingBy(
                                DistributionMasque::getTypeMasque,
                                Collectors.summingInt(DistributionMasque::getNbMasques)
                        )
                );
    }

}
