package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import fr.gouv.interieur.dmgp.remettant.domain.fixtures.ClockFixture;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static fr.gouv.interieur.dmgp.remettant.infrastructure.fixtures.DistributionMasqueDatabaseFixture.aDistributionMasqueDatabase;
import static org.assertj.core.api.Assertions.assertThat;

class DistributionMasqueDatabaseRepositoryTest extends RepositoryTest {

    @Test
    void save_devrait_enregistrer_une_distribution_masque_avec_un_identifiant_technique() {
        // Given
        DistributionMasqueDatabase distributionMasqueDatabase = aDistributionMasqueDatabase();

        // When
        DistributionMasqueDatabase result = distributionMasqueDatabaseRepository.save(distributionMasqueDatabase);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualToIgnoringGivenFields(distributionMasqueDatabase, "id");
    }

    @Test
    void findByDemandeur_devrait_retourner_uniquement_les_distributions_masques_du_demandeur_des_15_derniers_jours() {
        // Given
        String demandeur1 = "08c71b89ac55b6826a2daa9fe89edea26de";
        String demandeur2 = "89c71b89ac55b6826a2daa9fe89edea26de";
        distributionMasqueDatabaseRepository.save(aDistributionMasqueDatabase(demandeur1));
        distributionMasqueDatabaseRepository.save(aDistributionMasqueDatabase(demandeur1));
        DistributionMasqueDatabase distributionMasqueDatabaseHorsPeriode = aDistributionMasqueDatabase(demandeur1);
        distributionMasqueDatabaseHorsPeriode.setDateDistribution(ClockFixture.now().minusDays(16));
        distributionMasqueDatabaseRepository.save(distributionMasqueDatabaseHorsPeriode);
        distributionMasqueDatabaseRepository.save(aDistributionMasqueDatabase(demandeur2));

        LocalDateTime dateDistribution = ClockFixture.now().minusDays(15);

        // When
        List<DistributionMasqueDatabase> result = distributionMasqueDatabaseRepository.findByDemandeurAndDateDistributionIsGreaterThanEqual(demandeur1, dateDistribution);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).extracting("demandeur").isEqualTo(demandeur1);
        assertThat(result.get(1)).extracting("demandeur").isEqualTo(demandeur1);
    }

}
