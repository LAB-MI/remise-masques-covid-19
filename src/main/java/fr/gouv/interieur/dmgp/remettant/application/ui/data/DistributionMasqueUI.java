package fr.gouv.interieur.dmgp.remettant.application.ui.data;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistributionMasqueUI {

    private IdentiteDemandeurUI identiteDemandeur;
    private Integer nbMasquesAdulteUsageUnique;
    private Integer nbMasquesAdulteReutilisable;
    private Integer nbMasquesEnfantUsageUnique;
    private Integer nbMasquesEnfantReutilisable;

    public int getNbMasquesAdulteUsageUnique() {
        return getNombreOuZero(nbMasquesAdulteUsageUnique);
    }

    public int getNbMasquesAdulteReutilisable() {
        return getNombreOuZero(nbMasquesAdulteReutilisable);
    }

    public int getNbMasquesEnfantUsageUnique() {
        return getNombreOuZero(nbMasquesEnfantUsageUnique);
    }

    public int getNbMasquesEnfantReutilisable() {
        return getNombreOuZero(nbMasquesEnfantReutilisable);
    }

    private int getNombreOuZero(Integer nombre) {
        return nombre == null ? 0 : nombre;
    }
}
