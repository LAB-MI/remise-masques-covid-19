package fr.gouv.interieur.dmgp.remettant.application.ui.data;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors.HashGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.*;

@Component
public class DistributionMasqueUIMapper {
    private final Clock clock;
    private final HashGenerator hashGenerator;

    public DistributionMasqueUIMapper(Clock clock, HashGenerator hashGenerator) {
        this.clock = clock;
        this.hashGenerator = hashGenerator;
    }

    public List<DistributionMasque> toDistributionMasques(DistributionMasqueUI distributionMasqueUI, String codePostal, String typeRemettantStr) {
        IdentiteDemandeurUI identiteDemandeur = distributionMasqueUI.getIdentiteDemandeur();
        String hashIdentite = identiteDemandeur.getHashIdentite();
        if (StringUtils.isNotBlank(hashIdentite)) {
            String hashDemandeur = hashGenerator.hasherAvecPepper(hashIdentite);
            ModeSaisie modeSaisie = recupererValeurOuNullDepuisEnum(identiteDemandeur.getModeSaisie(), ModeSaisie.class);
            TypeRemettant typeRemettant = recupererValeurOuNullDepuisEnum(typeRemettantStr, TypeRemettant.class);
            return recupererMasquesDistribues(distributionMasqueUI, hashDemandeur, identiteDemandeur.getNombrePersonnes(), modeSaisie, codePostal, typeRemettant);
        }
        return List.of();
    }

    private <E extends Enum<E>> E recupererValeurOuNullDepuisEnum(String valeur, Class<E> enumClass) {
        return StringUtils.isNotBlank(valeur) ? Enum.valueOf(enumClass, valeur) : null;
    }

    private List<DistributionMasque> recupererMasquesDistribues(DistributionMasqueUI distributionMasqueUI, String hashDemandeur, Integer nombrePersonnes, ModeSaisie modeSaisie, String codePostal, TypeRemettant typeRemettant) {
        List<DistributionMasque> distributionMasques = new ArrayList<>();
        if (distributionMasqueUI.getNbMasquesAdulteUsageUnique() > 0) {
            distributionMasques.add(construireDistributionMasque(hashDemandeur, nombrePersonnes, distributionMasqueUI.getNbMasquesAdulteUsageUnique(), modeSaisie, ADULTE_USAGE_UNIQUE, codePostal, typeRemettant));
        }
        if (distributionMasqueUI.getNbMasquesAdulteReutilisable() > 0) {
            distributionMasques.add(construireDistributionMasque(hashDemandeur, nombrePersonnes, distributionMasqueUI.getNbMasquesAdulteReutilisable(), modeSaisie, ADULTE_REUTILISABLE, codePostal, typeRemettant));
        }
        if (distributionMasqueUI.getNbMasquesEnfantUsageUnique() > 0) {
            distributionMasques.add(construireDistributionMasque(hashDemandeur, nombrePersonnes, distributionMasqueUI.getNbMasquesEnfantUsageUnique(), modeSaisie, ENFANT_USAGE_UNIQUE, codePostal, typeRemettant));
        }
        if (distributionMasqueUI.getNbMasquesEnfantReutilisable() > 0) {
            distributionMasques.add(construireDistributionMasque(hashDemandeur, nombrePersonnes, distributionMasqueUI.getNbMasquesEnfantReutilisable(), modeSaisie, ENFANT_REUTILISABLE, codePostal, typeRemettant));
        }
        return distributionMasques;
    }

    private DistributionMasque construireDistributionMasque(String hashDemandeur, Integer nombrePersonnes, Integer nbMasques, ModeSaisie modeSaisie, TypeMasque typeMasque, String codePostal, TypeRemettant typeRemettant) {
        return DistributionMasque.builder()
                .hashDemandeur(hashDemandeur)
                .nbPersonnes(nombrePersonnes)
                .nbMasques(nbMasques)
                .modeSaisie(modeSaisie)
                .typeMasque(typeMasque)
                .dateDistribution(LocalDateTime.now(clock))
                .codePostal(codePostal)
                .typeRemettant(typeRemettant)
                .build();
    }
}
