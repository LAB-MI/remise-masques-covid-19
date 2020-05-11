package fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors;

import fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors.CreerEmpreinteIdentite;
import fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors.HashGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreerEmpreinteIdentiteTest {
    @InjectMocks
    CreerEmpreinteIdentite creerEmpreinteIdentite;
    @Mock
    HashGenerator hashGenerator;

    @Test
    void creerEmpreinte_devrait_appeler_hashgenerator_pour_lancer_la_production_du_hash() {
        // Given
        String hashInput = "hash";
        String hashOutput = "hash2";
        when(hashGenerator.hasherAvecPepper(hashInput)).thenReturn(hashOutput);

        // When
        String empreinte = creerEmpreinteIdentite.creer(hashInput);

        // Then
        assertThat(empreinte).isEqualTo(hashOutput);
    }
}
