package senac.tsi.api_YuGiOh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Schema(description = "Representa uma coleção de cartas do Yu-Gi-Oh")
public class Colecao {

    @Id
    @GeneratedValue
    @Schema(description = "Identificador único da coleção", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome da coleção", example = "Starter Deck")
    private String nome;

    @OneToMany(mappedBy = "colecao")
    @Schema(description = "Lista de cartas pertencentes à coleção (não exibido no JSON)")
    private List<CardEntity> cartas;
}