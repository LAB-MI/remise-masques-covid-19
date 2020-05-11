package fr.gouv.interieur.dmgp.remettant.infrastructure.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "distribution_masque")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties({"id", "demandeur"})
@JsonPropertyOrder({"dateDistribution", "nbMasques", "typeMasque", "nbPersonnes", "modeSaisie"})
public class DistributionMasqueDatabase {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    private String demandeur;
    private LocalDateTime dateDistribution;
    private Integer nbPersonnes;
    private String typeMasque;
    private Integer nbMasques;
    private String modeSaisie;
    private String codePostal;
    private String typeRemettant;
}
