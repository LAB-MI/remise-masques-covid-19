package fr.gouv.interieur.dmgp.remettant.domain.use_cases;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors.CreerEmpreinteIdentite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.*;
import static fr.gouv.interieur.dmgp.remettant.domain.fixtures.DistributionMasqueFixture.aDistributionMasque;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecupererDistributionsMasquesTest {
    private static final String IDENTITE = "2PPldoQsNqT/GKGuoEFyUljjl2Y244+LOm+RYMw5HCd56b2tyqvGeSpyfT7kBgw/ZtncOr/w4vj/vzyVM6H53g==";
    private static final String DEMANDEUR = "cVVxpoQsNqT/GKGuoEFyUljjl2Y566+LOm+RYMw5HCd56b2tyqvGeSpyfT7kBgw/ZtncOr/w4vj/vzyVM6H53g==";
    @InjectMocks
    private RecupererDistributionsMasques recupererDistributionsMasques;
    @Mock
    private CreerEmpreinteIdentite creerEmpreinteIdentite;
    @Mock
    private DistributionMasquePersistance distributionMasquePersistance;

    @BeforeEach
    public void setUp() {
        when(creerEmpreinteIdentite.creer(IDENTITE)).thenReturn(DEMANDEUR);
    }

    @Test
    void recuperer_devrait_retourner_une_liste_vide_quand_aucune_distribution_masque_en_base() {
        // Given
        when(distributionMasquePersistance.recupererParDemandeurSurLes15DerniersJours(DEMANDEUR)).thenReturn(emptyList());

        // When
        Map<TypeMasque, Integer> result = recupererDistributionsMasques.verifier(IDENTITE);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void recuperer_devrait_retourner_une_liste_distribution_masque_contenant_la_derniere_distribution_masque_usage_unique() {
        // Given
        DistributionMasque distributionMasqueUsageUnique1 = aDistributionMasque(DEMANDEUR,
                LocalDateTime.of(2020, 4, 19, 10, 32, 45, 43),
                ADULTE_USAGE_UNIQUE);
        DistributionMasque distributionMasqueUsageUnique2 = aDistributionMasque(DEMANDEUR,
                LocalDateTime.of(2020, 4, 20, 10, 32, 45, 43),
                ENFANT_USAGE_UNIQUE);
        when(distributionMasquePersistance.recupererParDemandeurSurLes15DerniersJours(DEMANDEUR))
                .thenReturn(List.of(distributionMasqueUsageUnique1, distributionMasqueUsageUnique2));

        // When
        Map<TypeMasque, Integer> result = recupererDistributionsMasques.verifier(IDENTITE);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(ADULTE_USAGE_UNIQUE)).isEqualTo(11);
        assertThat(result.get(ENFANT_USAGE_UNIQUE)).isEqualTo(11);
    }

    @Test
    void recuperer_devrait_retourner_une_liste_distribution_masque_contenant_la_derniere_distribution_masque_usage_unique_et_reutilisable() {
        // Given
        DistributionMasque distributionMasqueUsageUnique1 = aDistributionMasque(DEMANDEUR,
                LocalDateTime.of(2020, 4, 19, 10, 32, 45, 43),
                ADULTE_USAGE_UNIQUE);
        DistributionMasque distributionMasqueReutilisable1 = aDistributionMasque(DEMANDEUR,
                LocalDateTime.of(2020, 4, 19, 10, 32, 45, 43),
                ADULTE_REUTILISABLE);
        DistributionMasque distributionMasqueUsageUnique2 = aDistributionMasque(DEMANDEUR,
                LocalDateTime.of(2020, 4, 20, 10, 32, 45, 43),
                ADULTE_USAGE_UNIQUE);
        DistributionMasque distributionMasqueReutilisable2 = aDistributionMasque(DEMANDEUR,
                LocalDateTime.of(2020, 4, 21, 10, 32, 45, 43),
                ENFANT_REUTILISABLE);
        when(distributionMasquePersistance.recupererParDemandeurSurLes15DerniersJours(DEMANDEUR))
                .thenReturn(List.of(distributionMasqueReutilisable1, distributionMasqueReutilisable2,
                        distributionMasqueUsageUnique1, distributionMasqueUsageUnique2));

        // When
        Map<TypeMasque, Integer> result = recupererDistributionsMasques.verifier(IDENTITE);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(ADULTE_USAGE_UNIQUE)).isEqualTo(22);
        assertThat(result.get(ADULTE_REUTILISABLE)).isEqualTo(11);
        assertThat(result.get(ENFANT_REUTILISABLE)).isEqualTo(11);
    }
}
