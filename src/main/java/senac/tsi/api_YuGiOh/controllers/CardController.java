package senac.tsi.api_YuGiOh.controllers;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.api_YuGiOh.entities.CardEntity;
import senac.tsi.api_YuGiOh.repositories.CardRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// 🔥 SWAGGER
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cartas", description = "Gerenciamento de cartas do Yu-Gi-Oh")
@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardRepository repo;

    public CardController(CardRepository repo) {
        this.repo = repo;
    }

    @Operation(summary = "Listar cartas", description = "Retorna lista paginada de cartas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CardEntity>>> listar(Pageable pageable) {

        Page<CardEntity> page = repo.findAll(pageable);

        List<EntityModel<CardEntity>> cartas = page.getContent().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CardController.class).buscar(c.getId())).withSelfRel()
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(cartas));
    }

    @Operation(summary = "Buscar carta por ID", description = "Retorna uma carta específica")
    @ApiResponse(responseCode = "200", description = "Carta encontrada")
    @ApiResponse(responseCode = "404", description = "Carta não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CardEntity>> buscar(@PathVariable Long id) {

        CardEntity carta = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta não encontrada"));

        return ResponseEntity.ok(EntityModel.of(carta,
                linkTo(methodOn(CardController.class).listar(Pageable.unpaged())).withRel("cartas"),
                linkTo(methodOn(CardController.class).atualizar(id, carta)).withRel("update"),
                linkTo(methodOn(CardController.class).deletar(id)).withRel("delete")
        ));
    }

    @Operation(summary = "Criar carta", description = "Cria uma nova carta")
    @ApiResponse(responseCode = "201", description = "Carta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<EntityModel<CardEntity>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto carta a ser criado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CardEntity.class),
                            examples = @ExampleObject(
                                    value = "{\"nome\": \"Dragão Branco\", \"tipo\": \"MONSTRO\"}"
                            )
                    )
            )
            @RequestBody CardEntity carta) {

        CardEntity salvo = repo.save(carta);

        return ResponseEntity
                .created(linkTo(methodOn(CardController.class).buscar(salvo.getId())).toUri())
                .body(EntityModel.of(salvo));
    }

    @Operation(summary = "Atualizar carta", description = "Atualiza uma carta existente")
    @ApiResponse(responseCode = "200", description = "Carta atualizada")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CardEntity>> atualizar(@PathVariable Long id,
                                                             @RequestBody CardEntity carta) {

        carta.setId(id);
        return ResponseEntity.ok(EntityModel.of(repo.save(carta)));
    }

    @Operation(summary = "Deletar carta", description = "Remove uma carta pelo ID")
    @ApiResponse(responseCode = "204", description = "Carta removida")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar cartas por nome",
            description = "Retorna cartas filtradas pelo nome com paginação"
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<Page<CardEntity>> buscarPorNome(@RequestParam String nome, Pageable pageable) {

        return ResponseEntity.ok(repo.findByNomeContaining(nome, pageable));
    }

}