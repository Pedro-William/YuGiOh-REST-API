package senac.tsi.api_YuGiOh.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.api_YuGiOh.entities.Deck;
import senac.tsi.api_YuGiOh.repositories.DeckRepository;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Decks", description = "Gerenciamento de decks de cartas do jogador")
@RestController
@RequestMapping("/decks")
public class DeckController {

    private final DeckRepository repo;

    public DeckController(DeckRepository repo) {
        this.repo = repo;
    }

    @Operation(
            summary = "Listar decks",
            description = "Retorna uma lista paginada de decks com suas cartas associadas"
    )
    @ApiResponse(responseCode = "200", description = "Lista de decks retornada com sucesso")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Deck>>> listar(Pageable pageable) {

        Page<Deck> page = repo.findAll(pageable);

        List<EntityModel<Deck>> decks = page.getContent().stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DeckController.class).buscar(d.getId())).withSelfRel()
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(decks));
    }

    @Operation(
            summary = "Buscar deck por ID",
            description = "Retorna um deck específico com suas cartas"
    )
    @ApiResponse(responseCode = "200", description = "Deck encontrado")
    @ApiResponse(responseCode = "404", description = "Deck não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Deck>> buscar(@PathVariable Long id) {

        Deck deck = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck não encontrado"));

        return ResponseEntity.ok(EntityModel.of(deck,
                linkTo(methodOn(DeckController.class).listar(Pageable.unpaged())).withRel("decks"),
                linkTo(methodOn(DeckController.class).deletar(id)).withRel("delete")
        ));
    }

    @Operation(
            summary = "Atualizar deck",
            description = "Atualiza um deck existente, incluindo suas cartas"
    )
    @ApiResponse(responseCode = "200", description = "Deck atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Deck não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Deck>> atualizar(@PathVariable Long id,
                                                       @RequestBody Deck deck) {

        deck.setId(id);
        Deck atualizado = repo.save(deck);

        return ResponseEntity.ok(EntityModel.of(atualizado,
                linkTo(methodOn(DeckController.class).buscar(id)).withSelfRel()
        ));
    }

    @Operation(
            summary = "Criar deck",
            description = "Cria um novo deck contendo uma lista de cartas"
    )
    @ApiResponse(responseCode = "201", description = "Deck criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<EntityModel<Deck>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto deck a ser criado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Deck.class),
                            examples = @ExampleObject(
                                    value = "{\"nome\": \"Deck Dragão\"}"
                            )
                    )
            )
            @RequestBody Deck deck) {

        Deck salvo = repo.save(deck);

        return ResponseEntity
                .created(linkTo(methodOn(DeckController.class).buscar(salvo.getId())).toUri())
                .body(EntityModel.of(salvo));
    }

    @Operation(
            summary = "Deletar deck",
            description = "Remove um deck do sistema pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Deck removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar decks por nome",
            description = "Retorna decks filtrados pelo nome com paginação"
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<Page<Deck>> buscarPorNome(@RequestParam String nome, Pageable pageable) {

        return ResponseEntity.ok(repo.findByNomeContaining(nome, pageable));
    }

}