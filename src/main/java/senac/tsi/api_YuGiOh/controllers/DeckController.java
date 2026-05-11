package senac.tsi.api_YuGiOh.controllers;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import senac.tsi.api_YuGiOh.entities.Deck;
import senac.tsi.api_YuGiOh.services.DeckService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Decks", description = "Gerenciamento de decks de cartas")
@RestController
@RequestMapping("/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService service;

    @Operation(summary = "Listar decks")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Deck>>> listar(Pageable pageable) {

        Page<Deck> page = service.listar(pageable);

        List<EntityModel<Deck>> lista = page.getContent().stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DeckController.class)
                                .buscar(d.getId())).withSelfRel()
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @Operation(summary = "Buscar deck por ID")
    @ApiResponse(responseCode = "200", description = "Deck encontrado")
    @ApiResponse(responseCode = "404", description = "Deck não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Deck>> buscar(@PathVariable Long id) {

        Deck deck = service.buscarPorId(id);

        return ResponseEntity.ok(EntityModel.of(deck,
                linkTo(methodOn(DeckController.class)
                        .buscar(id)).withSelfRel(),

                linkTo(methodOn(DeckController.class)
                        .listar(Pageable.unpaged())).withRel("decks"),

                linkTo(methodOn(DeckController.class)
                        .atualizar(id, deck)).withRel("update"),

                linkTo(methodOn(DeckController.class)
                        .deletar(id)).withRel("delete")
        ));
    }

    @Operation(summary = "Criar deck")
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
                                    value = """
                                            {
                                              "nome": "Deck Dragão Branco",
                                              "cartas": [
                                                { "id": 1 },
                                                { "id": 2 }
                                              ]
                                            }
                                            """
                            )
                    )
            )

            @RequestBody Deck deck) {

        Deck salvo = service.criar(deck);

        return ResponseEntity
                .created(linkTo(methodOn(DeckController.class)
                        .buscar(salvo.getId())).toUri())
                .body(EntityModel.of(salvo,
                        linkTo(methodOn(DeckController.class)
                                .buscar(salvo.getId())).withSelfRel()
                ));
    }

    @Operation(summary = "Atualizar deck")
    @ApiResponse(responseCode = "200", description = "Deck atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Deck não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Deck>> atualizar(
            @PathVariable Long id,
            @RequestBody Deck deck) {

        Deck atualizado = service.atualizar(id, deck);

        return ResponseEntity.ok(
                EntityModel.of(atualizado,
                        linkTo(methodOn(DeckController.class)
                                .buscar(id)).withSelfRel()
                )
        );
    }

    @Operation(summary = "Deletar deck")
    @ApiResponse(responseCode = "204", description = "Deck removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Deck não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar decks por nome")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum deck encontrado")
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Deck>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<Deck> pagina = service.buscarPorNome(nome, pageable);

        List<EntityModel<Deck>> lista = pagina.getContent().stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DeckController.class)
                                .buscar(d.getId())).withSelfRel()
                ))
                .toList();

        PagedModel<EntityModel<Deck>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }
}