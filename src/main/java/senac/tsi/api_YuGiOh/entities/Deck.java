package senac.tsi.api_YuGiOh.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
@Entity
@Data
public class Deck {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String nome;

    @ManyToMany
    @JoinTable(name = "deck_cartas")
    private List<CardEntity> cartas;
}
