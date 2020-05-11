package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gouv.interieur.dmgp.remettant.domain.entities.DistributionMasque;
import fr.gouv.interieur.dmgp.remettant.domain.fixtures.ClockFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.ADULTE_REUTILISABLE;
import static fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque.ADULTE_USAGE_UNIQUE;
import static fr.gouv.interieur.dmgp.remettant.domain.fixtures.DistributionMasqueFixture.aDistributionMasque;
import static fr.gouv.interieur.dmgp.remettant.infrastructure.fixtures.DistributionMasqueDatabaseFixture.aDistributionMasqueDatabase;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributionMasquePersistanceJdbcTest {
    private DistributionMasquePersistanceJdbc distributionMasquePersistanceJdbc;
    @Mock
    private DistributionMasqueDatabaseRepository distributionMasqueDatabaseRepository;
    @Mock
    private DistributionMasqueDatabaseMapper distributionMasqueDatabaseMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Appender<ILoggingEvent> loggingEventAppender;
    @Captor
    ArgumentCaptor<ILoggingEvent> loggingEventArgumentCaptor;

    @BeforeEach
    void setUp() {
        ((Logger) LoggerFactory.getLogger(DistributionMasquePersistanceJdbc.class)).addAppender(loggingEventAppender);
        distributionMasquePersistanceJdbc = new DistributionMasquePersistanceJdbc(
                distributionMasqueDatabaseRepository,
                distributionMasqueDatabaseMapper,
                objectMapper,
                ClockFixture.clock());
    }

    @AfterEach
    void tearDown() {
        ((Logger) LoggerFactory.getLogger(DistributionMasquePersistanceJdbc.class)).detachAppender(loggingEventAppender);
    }

    @Test
    void recupererParDemandeur_devrait_recuperer_les_distributions_masques_du_demandeur() {
        // Given
        String demandeur = "08c71b89ac55b6826a2daa9fe89edea26de";
        DistributionMasqueDatabase distributionMasqueDatabase1 = aDistributionMasqueDatabase(demandeur, ADULTE_USAGE_UNIQUE);
        DistributionMasqueDatabase distributionMasqueDatabase2 = aDistributionMasqueDatabase(demandeur, ADULTE_REUTILISABLE);
        DistributionMasque distributionMasque1 = aDistributionMasque();
        DistributionMasque distributionMasque2 = aDistributionMasque();
        LocalDateTime dateDistribution = ClockFixture.now().minusDays(15);
        when(distributionMasqueDatabaseRepository.findByDemandeurAndDateDistributionIsGreaterThanEqual(demandeur, dateDistribution))
                .thenReturn(List.of(distributionMasqueDatabase1, distributionMasqueDatabase2));
        when(distributionMasqueDatabaseMapper.toDistributionMasque(distributionMasqueDatabase1)).thenReturn(distributionMasque1);
        when(distributionMasqueDatabaseMapper.toDistributionMasque(distributionMasqueDatabase2)).thenReturn(distributionMasque2);

        // When
        List<DistributionMasque> distributionMasques = distributionMasquePersistanceJdbc.recupererParDemandeurSurLes15DerniersJours(demandeur);

        // Then
        assertThat(distributionMasques).containsExactlyInAnyOrder(distributionMasque1, distributionMasque2);
    }

    @Test
    void persister_devrait_sauvegarder_les_distributions_de_masques_en_base() {
        // Given
        DistributionMasque distributionMasque1 = aDistributionMasque();
        DistributionMasque distributionMasque2 = aDistributionMasque();
        DistributionMasqueDatabase distributionMasqueDatabase1 = aDistributionMasqueDatabase("demandeur1");
        DistributionMasqueDatabase distributionMasqueDatabase2 = aDistributionMasqueDatabase("demandeur2");
        when(distributionMasqueDatabaseMapper.toDistributionMasqueDatabase(distributionMasque1)).thenReturn(distributionMasqueDatabase1);
        when(distributionMasqueDatabaseMapper.toDistributionMasqueDatabase(distributionMasque2)).thenReturn(distributionMasqueDatabase2);

        // When
        distributionMasquePersistanceJdbc.persister(List.of(distributionMasque1, distributionMasque2));

        // Then
        verify(distributionMasqueDatabaseRepository).save(distributionMasqueDatabase1);
        verify(distributionMasqueDatabaseRepository).save(distributionMasqueDatabase2);
    }

    @Test
    void persister_devrait_logger_la_distribution_de_masques_en_json() throws JsonProcessingException {
        // Given
        DistributionMasque distributionMasque = aDistributionMasque();
        DistributionMasqueDatabase distributionMasqueDatabase = aDistributionMasqueDatabase();
        when(distributionMasqueDatabaseMapper.toDistributionMasqueDatabase(distributionMasque)).thenReturn(distributionMasqueDatabase);
        String distributionMasqueDatabaseJson = "{\"dateDistribution\":\"2018-08-05T19:43:00\",\"nbMasques\":23,\"typeMasque\":\"USAGE_UNIQUE\",\"nbPersonnes\":1,\"modeSaisie\":\"MANUEL\",\"codePostal\":\"75001\",\"typeRemettant\":\"Mairie\"}";
        when(objectMapper.writeValueAsString(distributionMasqueDatabase)).thenReturn(distributionMasqueDatabaseJson);

        // When
        distributionMasquePersistanceJdbc.persister(List.of(distributionMasque));

        // Then
        verify(loggingEventAppender).doAppend(loggingEventArgumentCaptor.capture());
        assertThat(loggingEventArgumentCaptor.getValue().getMessage()).isEqualTo(distributionMasqueDatabaseJson);
    }

    @Test
    void persister_devrait_logger_une_erreur_quand_le_log_de_distribution_echoue() throws JsonProcessingException {
        // Given
        DistributionMasque distributionMasque = aDistributionMasque();
        DistributionMasqueDatabase distributionMasqueDatabase = aDistributionMasqueDatabase();
        when(distributionMasqueDatabaseMapper.toDistributionMasqueDatabase(distributionMasque)).thenReturn(distributionMasqueDatabase);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(distributionMasqueDatabase);

        // When
        distributionMasquePersistanceJdbc.persister(List.of(distributionMasque));

        // Then
        verify(loggingEventAppender).doAppend(loggingEventArgumentCaptor.capture());
        assertThat(loggingEventArgumentCaptor.getValue().getMessage()).isEqualTo("Erreur lors de la production de log de distribution de masque");
    }

}
