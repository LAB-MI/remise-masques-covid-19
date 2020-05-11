package fr.gouv.interieur.dmgp.remettant.application.ui.fixtures;

import fr.gouv.interieur.dmgp.remettant.application.ui.data.InfosDemandeurUI;

import static fr.gouv.interieur.dmgp.remettant.application.ui.fixtures.IdentiteDemandeurUIFixture.aIdentiteDemandeurUI;

public class InfosDemandeurUIFixture {

    public static InfosDemandeurUI aInfosDemandeurUI() {
        return InfosDemandeurUI.builder()
                .nbMasquesAdulteReutilisableDejaDistribues(2)
                .nbMasquesAdulteUsageUniqueDejaDistribues(3)
                .nbMasquesEnfantReutilisableDejaDistribues(10)
                .nbMasquesEnfantUsageUniqueDejaDistribues(8)
                .identite(aIdentiteDemandeurUI())
                .build();
    }

}
