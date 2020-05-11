package fr.gouv.interieur.dmgp.remettant.domain.use_cases;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.fixtures.DistributionMasqueFixture;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.DistribuerMasques;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.DistributionMasquePersistance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DistribuerMasquesTest {
    @InjectMocks
    private DistribuerMasques distribuerMasques;
    @Mock
    private DistributionMasquePersistance distributionMasquePersistance;

    @Test
    void distribuer_devrait_persister_les_distributions_de_masques() {
        // Given
        DistributionMasque distributionMasque1 = DistributionMasqueFixture.aDistributionMasque(4);
        DistributionMasque distributionMasque2 = DistributionMasqueFixture.aDistributionMasque(20);

        // When
        distribuerMasques.distribuer(List.of(distributionMasque1, distributionMasque2));

        // Then
        verify(distributionMasquePersistance).persister(List.of(distributionMasque1, distributionMasque2));
    }
}
