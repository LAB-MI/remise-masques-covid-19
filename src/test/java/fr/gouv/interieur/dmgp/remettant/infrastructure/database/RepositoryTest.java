package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    DistributionMasqueDatabaseRepository distributionMasqueDatabaseRepository;

    @BeforeEach
    public void setUp() {
        this.cleanAll();
    }

    @AfterClass
    public void tearDown() {
        this.cleanAll();
    }

    private void cleanAll() {
        distributionMasqueDatabaseRepository.deleteAllInBatch();
    }

}
