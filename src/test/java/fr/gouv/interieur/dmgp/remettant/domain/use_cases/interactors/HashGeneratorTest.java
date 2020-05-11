package fr.gouv.interieur.dmgp.remettant.domain.use_cases.interactors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HashGeneratorTest {
    private static String PEPPER_VALUE = "1234";
    private HashGenerator hashGenerator;

    @BeforeEach
    void setUp() {
        hashGenerator = new HashGenerator(PEPPER_VALUE);
    }

    @Test
    void hasher_devrait_retourner_un_hash_de_donnees_avec_un_pepper() {
        // Given
        String chaine = "DUPONTJEAN1990-12-12PARIS";

        // When
        String resultat = hashGenerator.hasherAvecPepper(chaine);

        // Then
        assertThat(resultat).isEqualTo("4ed6o/Jcn8puliRsyKswvY8/afhZ4RpVQJwsPjIWxsSinWEzBAO6UAmpA1GEOLUBRx0rArLaVFtCM4Fk/ZX8ig==");
    }

    @Test
    void hasher_devrait_retourner_toujours_le_meme_hash_de_donnees_pour_une_meme_chaine() {
        // Given
        String chaine = "DUPONTJEAN1990-12-12PARIS";

        // When
        String result1 = hashGenerator.hasherAvecPepper(chaine);
        String result2 = hashGenerator.hasherAvecPepper(chaine);

        // Then
        assertThat(result1).isEqualTo("4ed6o/Jcn8puliRsyKswvY8/afhZ4RpVQJwsPjIWxsSinWEzBAO6UAmpA1GEOLUBRx0rArLaVFtCM4Fk/ZX8ig==");
        assertThat(result2).isEqualTo(result1);
    }
}
