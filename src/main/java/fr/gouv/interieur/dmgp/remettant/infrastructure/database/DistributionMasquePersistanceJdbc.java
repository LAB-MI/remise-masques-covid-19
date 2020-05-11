package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.DistributionMasquePersistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DistributionMasquePersistanceJdbc implements DistributionMasquePersistance {

    private final DistributionMasqueDatabaseRepository distributionMasqueDatabaseRepository;
    private final DistributionMasqueDatabaseMapper distributionMasqueDatabaseMapper;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributionMasquePersistanceJdbc.class);

    public DistributionMasquePersistanceJdbc(DistributionMasqueDatabaseRepository distributionMasqueDatabaseRepository, DistributionMasqueDatabaseMapper distributionMasqueDatabaseMapper, ObjectMapper objectMapper, Clock clock) {
        this.distributionMasqueDatabaseRepository = distributionMasqueDatabaseRepository;
        this.distributionMasqueDatabaseMapper = distributionMasqueDatabaseMapper;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @Override
    public List<DistributionMasque> recupererParDemandeurSurLes15DerniersJours(String demandeur) {
        LocalDateTime dateDistribution = LocalDateTime.now(clock).minusDays(15);
        return distributionMasqueDatabaseRepository.findByDemandeurAndDateDistributionIsGreaterThanEqual(demandeur, dateDistribution)
                .stream()
                .map(distributionMasqueDatabaseMapper::toDistributionMasque)
                .collect(toList());
    }

    @Override
    @Transactional
    public void persister(List<DistributionMasque> distributionMasques) {
        distributionMasques.stream()
                .map(distributionMasqueDatabaseMapper::toDistributionMasqueDatabase)
                .forEach(d -> {
                    distributionMasqueDatabaseRepository.save(d);
                    produireLog(d);
                });
    }

    private void produireLog(DistributionMasqueDatabase distributionMasqueDatabase) {
        try {
            LOGGER.info(objectMapper.writeValueAsString(distributionMasqueDatabase));
        } catch (JsonProcessingException e) {
            LOGGER.error("Erreur lors de la production de log de distribution de masque", e);
        }
    }

}
