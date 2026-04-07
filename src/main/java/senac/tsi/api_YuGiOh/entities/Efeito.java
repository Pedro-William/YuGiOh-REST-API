package senac.tsi.api_YuGiOh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Schema(description = "Representa um efeito aplicado a uma carta")
public class Efeito {

    @Id
    @GeneratedValue
    @Schema(description = "Identificador único do efeito", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Descrição do efeito da carta", example = "Aumenta o ataque em 500 pontos")
    private String descricao;

    @ManyToMany(mappedBy = "efeitos")
    @JsonIgnore
    @Schema(description = "Lista de cartas que possuem este efeito (não exibido no JSON)")
    private List<CardEntity> cartas;
}