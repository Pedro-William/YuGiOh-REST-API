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
@Schema(description = "Representa o atributo de uma carta, como LUZ, TREVAS, FOGO, etc.")
public class Atributo {

    @Id
    @GeneratedValue
    @Schema(description = "Identificador único do atributo", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome do atributo", example = "LUZ")
    private String nome;

    @OneToMany(mappedBy = "atributo")
    @JsonIgnore
    @Schema(description = "Lista de cartas associadas a este atributo (não exibido no JSON)")
    private List<CardEntity> cartas;
}