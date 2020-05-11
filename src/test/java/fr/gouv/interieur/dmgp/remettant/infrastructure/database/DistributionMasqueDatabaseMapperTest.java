package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.ModeSaisie;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeRemettant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static fr.gouv.interieur.dmgp.remettant.domain.fixtures.DistributionMasqueFixture.aDistributionMasque;
import static fr.gouv.interieur.dmgp.remettant.infrastructure.fixtures.DistributionMasqueDatabaseFixture.aDistributionMasqueDatabase;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DistributionMasqueDatabaseMapperTest {
    @InjectMocks
    private DistributionMasqueDatabaseMapper distributionMasqueDatabaseMapper;

    @Test
    void toDistributionMasqueDatabaseDatabase_devrait_retourner_un_DistributionMasque_depuis_un_DistributionMasqueDatabase() {
        // Given
        DistributionMasqueDatabase distributionMasqueDatabase = aDistributionMasqueDatabase();

        // When
        DistributionMasque distributionMasque = distributionMasqueDatabaseMapper.toDistributionMasque(distributionMasqueDatabase);

        // Then
        assertThat(distributionMasque.getHashDemandeur()).isEqualTo(distributionMasqueDatabase.getDemandeur());
        assertThat(distributionMasque.getDateDistribution()).isEqualTo(distributionMasqueDatabase.getDateDistribution());
        assertThat(distributionMasque.getNbPersonnes()).isEqualTo(distributionMasqueDatabase.getNbPersonnes());
        assertThat(distributionMasque.getTypeMasque()).isEqualTo(TypeMasque.valueOf(distributionMasqueDatabase.getTypeMasque()));
        assertThat(distributionMasque.getNbMasques()).isEqualTo(distributionMasqueDatabase.getNbMasques());
        assertThat(distributionMasque.getModeSaisie()).isEqualTo(ModeSaisie.valueOf(distributionMasqueDatabase.getModeSaisie()));
        assertThat(distributionMasque.getCodePostal()).isEqualTo(distributionMasqueDatabase.getCodePostal());
        assertThat(distributionMasque.getTypeRemettant()).isEqualTo(TypeRemettant.valueOf(distributionMasqueDatabase.getTypeRemettant()));
    }

    @Test
    void toDistributionMasqueDatabase_devrait_retourner_un_DistributionMasqueDatabase_depuis_un_DistributionMasque() {
        // Given
        DistributionMasque distributionMasque = aDistributionMasque();

        // When
        DistributionMasqueDatabase distributionMasqueDatabase = distributionMasqueDatabaseMapper.toDistributionMasqueDatabase(distributionMasque);

        // Then
        assertThat(distributionMasqueDatabase.getDemandeur()).isEqualTo(distributionMasque.getHashDemandeur());
        assertThat(distributionMasqueDatabase.getDateDistribution()).isEqualTo(distributionMasque.getDateDistribution());
        assertThat(distributionMasqueDatabase.getNbPersonnes()).isEqualTo(distributionMasque.getNbPersonnes());
        assertThat(distributionMasqueDatabase.getTypeMasque()).isEqualTo(distributionMasque.getTypeMasque().name());
        assertThat(distributionMasqueDatabase.getNbMasques()).isEqualTo(distributionMasque.getNbMasques());
        assertThat(distributionMasqueDatabase.getModeSaisie()).isEqualTo(distributionMasque.getModeSaisie().name());
        assertThat(distributionMasqueDatabase.getTypeRemettant()).isEqualTo(distributionMasque.getTypeRemettant().name());
        assertThat(distributionMasqueDatabase.getCodePostal()).isEqualTo(distributionMasque.getCodePostal());
    }
}
