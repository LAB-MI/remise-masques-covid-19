package fr.gouv.interieur.dmgp.remettant.application.ui.data;

import fr.gouv.interieur.dmgp.remettant.application.ui.fixtures.IdentiteDemandeurUIFixture;
import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant;
import fr.gouv.interieur.dmgp.remettant.domain.fixtures.ClockFixture;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors.HashGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.*;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant.Association;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistributionMasqueUIMapperTest {

    private DistributionMasqueUIMapper distributionMasqueUIMapper;
    @Mock
    private HashGenerator hashGenerator;
    private static final String CODE_POSTAL = "31490";
    private static final TypeRemettant TYPE_REMETTANT = Association;

    @BeforeEach
    void setUp() {
        distributionMasqueUIMapper = new DistributionMasqueUIMapper(ClockFixture.clock(), hashGenerator);
    }

    @Test
    void toDistributionMasques_devrait_retourner_une_liste_de_DistributionMasque_a_partir_d_une_DistributionMasqueUI() {
        // Given
        IdentiteDemandeurUI identiteDemandeur = IdentiteDemandeurUIFixture.aIdentiteDemandeurUI();
        String hashIdentite = identiteDemandeur.getHashIdentite();
        DistributionMasqueUI distributionMasqueUI = DistributionMasqueUI.builder()
                .identiteDemandeur(identiteDemandeur)
                .nbMasquesAdulteUsageUnique(40)
                .nbMasquesAdulteReutilisable(4)
                .nbMasquesEnfantUsageUnique(12)
                .nbMasquesEnfantReutilisable(2)
                .build();
        String hashDemandeur = "f7e40706eceac692d8f82fabeb24b35a3d8b93e9e971d63afed99566152bce1c";
        when(hashGenerator.hasherAvecPepper(hashIdentite)).thenReturn(hashDemandeur);

        // When
        List<DistributionMasque> distributionMasques = distributionMasqueUIMapper.toDistributionMasques(distributionMasqueUI, CODE_POSTAL, TYPE_REMETTANT.name());

        // Then
        assertThat(distributionMasques.size()).isEqualTo(4);
        DistributionMasque distributionMasqueAdulteUsageUnique = distributionMasques.get(0);
        assertThat(distributionMasqueAdulteUsageUnique.getHashDemandeur()).isEqualTo(hashDemandeur);
        assertThat(distributionMasqueAdulteUsageUnique.getDateDistribution()).isEqualTo(ClockFixture.now());
        assertThat(distributionMasqueAdulteUsageUnique.getNbMasques()).isEqualTo(40);
        assertThat(distributionMasqueAdulteUsageUnique.getTypeMasque()).isEqualTo(ADULTE_USAGE_UNIQUE);
        assertThat(distributionMasqueAdulteUsageUnique.getNbPersonnes()).isEqualTo(identiteDemandeur.getNombreMineurs() + 1);
        DistributionMasque distributionMasqueAdulteReutilisable = distributionMasques.get(1);
        assertThat(distributionMasqueAdulteReutilisable.getHashDemandeur()).isEqualTo(hashDemandeur);
        assertThat(distributionMasqueAdulteReutilisable.getDateDistribution()).isEqualTo(ClockFixture.now());
        assertThat(distributionMasqueAdulteReutilisable.getNbMasques()).isEqualTo(4);
        assertThat(distributionMasqueAdulteReutilisable.getTypeMasque()).isEqualTo(ADULTE_REUTILISABLE);
        assertThat(distributionMasqueAdulteReutilisable.getNbPersonnes()).isEqualTo(identiteDemandeur.getNombreMineurs() + 1);
        DistributionMasque distributionMasqueEnfantUsageUnique = distributionMasques.get(2);
        assertThat(distributionMasqueEnfantUsageUnique.getHashDemandeur()).isEqualTo(hashDemandeur);
        assertThat(distributionMasqueEnfantUsageUnique.getDateDistribution()).isEqualTo(ClockFixture.now());
        assertThat(distributionMasqueEnfantUsageUnique.getNbMasques()).isEqualTo(12);
        assertThat(distributionMasqueEnfantUsageUnique.getTypeMasque()).isEqualTo(ENFANT_USAGE_UNIQUE);
        assertThat(distributionMasqueEnfantUsageUnique.getNbPersonnes()).isEqualTo(identiteDemandeur.getNombreMineurs() + 1);
        DistributionMasque distributionMasqueEnfantReutilisable = distributionMasques.get(3);
        assertThat(distributionMasqueEnfantReutilisable.getHashDemandeur()).isEqualTo(hashDemandeur);
        assertThat(distributionMasqueEnfantReutilisable.getDateDistribution()).isEqualTo(ClockFixture.now());
        assertThat(distributionMasqueEnfantReutilisable.getNbMasques()).isEqualTo(2);
        assertThat(distributionMasqueEnfantReutilisable.getTypeMasque()).isEqualTo(ENFANT_REUTILISABLE);
        assertThat(distributionMasqueEnfantReutilisable.getNbPersonnes()).isEqualTo(identiteDemandeur.getNombreMineurs() + 1);
    }

    @Test
    void toDistributionMasques_devrait_filtrer_les_distributions_dont_le_nombre_de_masques_est_egal_a_zero() {
        // Given
        IdentiteDemandeurUI identiteDemandeur = IdentiteDemandeurUIFixture.aIdentiteDemandeurUI();
        String hashIdentite = identiteDemandeur.getHashIdentite();
        DistributionMasqueUI distributionMasqueUI = DistributionMasqueUI.builder()
                .identiteDemandeur(identiteDemandeur)
                .nbMasquesAdulteUsageUnique(0)
                .nbMasquesAdulteReutilisable(0)
                .build();
        String hashDemandeur = "f7e40706eceac692d8f82fabeb24b35a3d8b93e9e971d63afed99566152bce1c";
        when(hashGenerator.hasherAvecPepper(hashIdentite)).thenReturn(hashDemandeur);

        // When
        List<DistributionMasque> distributionMasques = distributionMasqueUIMapper.toDistributionMasques(distributionMasqueUI, CODE_POSTAL, TYPE_REMETTANT.name());

        // Then
        assertThat(distributionMasques).isEmpty();
    }

    @Test
    void toDistributionMasques_devrait_filtrer_les_distributions_dont_le_nombre_de_masques_est_null() {
        // Given
        IdentiteDemandeurUI identiteDemandeur = IdentiteDemandeurUIFixture.aIdentiteDemandeurUI();
        String hashIdentite = identiteDemandeur.getHashIdentite();
        DistributionMasqueUI distributionMasqueUI = DistributionMasqueUI.builder()
                .identiteDemandeur(identiteDemandeur)
                .nbMasquesAdulteUsageUnique(null)
                .nbMasquesAdulteReutilisable(null)
                .build();
        String hashDemandeur = "f7e40706eceac692d8f82fabeb24b35a3d8b93e9e971d63afed99566152bce1c";
        when(hashGenerator.hasherAvecPepper(hashIdentite)).thenReturn(hashDemandeur);

        // When
        List<DistributionMasque> distributionMasques = distributionMasqueUIMapper.toDistributionMasques(distributionMasqueUI, CODE_POSTAL, TYPE_REMETTANT.name());

        // Then
        assertThat(distributionMasques).isEmpty();
    }

    @Test
    void toDistributionMasques_devrait_mettre_le_mode_de_saisie_et_le_type_remettant_a_null_quand_ils_sont_vides() {
        // Given
        IdentiteDemandeurUI identiteDemandeur = IdentiteDemandeurUIFixture.aIdentiteDemandeurUI();
        identiteDemandeur.setModeSaisie("");
        String hashIdentite = identiteDemandeur.getHashIdentite();
        DistributionMasqueUI distributionMasqueUI = DistributionMasqueUI.builder()
                .identiteDemandeur(identiteDemandeur)
                .nbMasquesAdulteUsageUnique(40)
                .nbMasquesAdulteReutilisable(4)
                .build();
        String hashDemandeur = "f7e40706eceac692d8f82fabeb24b35a3d8b93e9e971d63afed99566152bce1c";
        when(hashGenerator.hasherAvecPepper(hashIdentite)).thenReturn(hashDemandeur);

        // When
        List<DistributionMasque> distributionMasques = distributionMasqueUIMapper.toDistributionMasques(distributionMasqueUI, CODE_POSTAL, null);

        // Then
        assertThat(distributionMasques.get(0).getModeSaisie()).isNull();
        assertThat(distributionMasques.get(0).getTypeRemettant()).isNull();
    }
}
