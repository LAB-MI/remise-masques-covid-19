package fr.gouv.interieur.dmgp.remettant.application.ui.fixtures;

import fr.gouv.interieur.dmgp.remettant.application.ui.data.IdentiteDemandeurUI;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie.SCAN;

public class IdentiteDemandeurUIFixture {

    public static IdentiteDemandeurUI aIdentiteDemandeurUI() {
        return IdentiteDemandeurUI.builder()
                .hashIdentite("2PPldoQsNqT/GKGuoEFyUljjl2Y244+LOm+RYMw5HCd56b2tyqvGeSpyfT7kBgw/ZtncOr/w4vj/vzyVM6H53g==")
                .modeSaisie(SCAN.name())
                .nombreMineurs(3)
                .build();
    }
}
