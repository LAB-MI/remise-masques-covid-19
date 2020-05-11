package fr.gouv.interieur.dmgp.remettant.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class DistributionMasque {
    private String hashDemandeur;
    private TypeMasque typeMasque;
    private Integer nbMasques;
    private Integer nbPersonnes;
    private ModeSaisie modeSaisie;
    private LocalDateTime dateDistribution;
    private String codePostal;
    private TypeRemettant typeRemettant;
}
