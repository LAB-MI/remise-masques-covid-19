package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DistributionMasqueDatabaseRepository
        extends JpaRepository<DistributionMasqueDatabase, UUID> {

    List<DistributionMasqueDatabase> findByDemandeurAndDateDistributionIsGreaterThanEqual(String demandeur, LocalDateTime dateDistribution);

}
