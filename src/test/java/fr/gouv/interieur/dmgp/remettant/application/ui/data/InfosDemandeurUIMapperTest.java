package fr.gouv.interieur.dmgp.remettant.application.ui.data;

import fr.gouv.interieur.dmgp.remettant.domain.entities.TypeMasque;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InfosDemandeurUIMapperTest {
    @InjectMocks
    private InfosDemandeurUIMapper infosDemandeurUiMapper;

    @Test
    void toInfosDemandeurUI_devrait_retourner_un_InfosDemandeurUI_a_partir_d_une_map_de_nb_masques_par_type_et_d_une_IdentiteDemandeurUI() {
        // Given
        IdentiteDemandeurUI identiteDemandeurUI = IdentiteDemandeurUI.builder().build();
        Map<TypeMasque, Integer> nbMasquesDistribuesParType = new HashMap<>();
        nbMasquesDistribuesParType.put(TypeMasque.ADULTE_REUTILISABLE, 2);
        nbMasquesDistribuesParType.put(TypeMasque.ENFANT_USAGE_UNIQUE, 3);

        // When
        InfosDemandeurUI infosDemandeurUI = infosDemandeurUiMapper.toInfosDemandeurUI(nbMasquesDistribuesParType, identiteDemandeurUI);

        // Then
        assertThat(infosDemandeurUI.getIdentite()).isEqualTo(identiteDemandeurUI);
        assertThat(infosDemandeurUI.getNbMasquesAdulteReutilisableDejaDistribues()).isEqualTo(2);
        assertThat(infosDemandeurUI.getNbMasquesAdulteUsageUniqueDejaDistribues()).isEqualTo(0);
        assertThat(infosDemandeurUI.getNbMasquesEnfantReutilisableDejaDistribues()).isEqualTo(0);
        assertThat(infosDemandeurUI.getNbMasquesEnfantUsageUniqueDejaDistribues()).isEqualTo(3);
    }
}
