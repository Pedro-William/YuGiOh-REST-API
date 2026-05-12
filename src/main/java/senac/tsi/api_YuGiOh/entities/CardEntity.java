package senac.tsi.api_YuGiOh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import senac.tsi.api_YuGiOh.enums.TipoCarta;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Schema(description = "Representa uma carta do jogo Yu-Gi-Oh, contendo suas informações principais como nome, tipo, atributo, coleção e efeitos")
public class CardEntity {

    @Id
    @GeneratedValue
    @Schema(description = "Identificador único da carta", example = "1")
    private Long id;

    @NotNull
    @NotBlank
    @Schema(description = "Nome da carta", example = "Dragão Branco de Olhos Azuis")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo da carta (MONSTRO, MAGIA ou ARMADILHA)", example = "MONSTRO")
    private TipoCarta tipo;

    @ManyToOne
    @Schema(description = "Atributo da carta (ex: LUZ, TREVAS)")
    private Atributo atributo;

    @ManyToOne
    @JsonIgnore
    @Schema(description = "Coleção à qual a carta pertence")
    private Colecao colecao;

    @ManyToMany
    @Schema(description = "Lista de efeitos associados à carta")
    private List<Efeito> efeitos;

    @ManyToMany(mappedBy = "cartas")
    @JsonIgnore
    @Schema(description = "Lista de decks que contêm esta carta")
    private List<Deck> decks;
}