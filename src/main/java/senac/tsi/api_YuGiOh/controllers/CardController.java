package senac.tsi.api_YuGiOh.controllers;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.api_YuGiOh.entities.CardEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// 🔥 SWAGGER
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import senac.tsi.api_YuGiOh.services.CardService;

@Tag(name = "Cartas", description = "Gerenciamento de cartas")
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService service;

    @Operation(summary = "Listar cartas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CardEntity>>> listar(Pageable pageable) {

        Page<CardEntity> page = service.listar(pageable);

        List<EntityModel<CardEntity>> lista = page.getContent().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CardController.class).buscar(c.getId())).withSelfRel()
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @Operation(summary = "Buscar carta por ID")
    @ApiResponse(responseCode = "200", description = "Carta encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Carta não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CardEntity>> buscar(@PathVariable Long id) {

        CardEntity card = service.buscarPorId(id);

        return ResponseEntity.ok(EntityModel.of(card));
    }

    @Operation(summary = "Criar carta")
    @ApiResponse(responseCode = "201", description = "Carta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<EntityModel<CardEntity>> criar(@RequestBody CardEntity card) {

        CardEntity salvo = service.criar(card);

        return ResponseEntity
                .created(linkTo(methodOn(CardController.class).buscar(salvo.getId())).toUri())
                .body(EntityModel.of(salvo));
    }

    @Operation(summary = "Atualizar carta")
    @ApiResponse(responseCode = "200", description = "carta atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "carta nao encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CardEntity>> atualizar(@PathVariable Long id,
                                                             @RequestBody CardEntity card) {

        return ResponseEntity.ok(EntityModel.of(service.atualizar(id, card)));
    }

    @Operation(summary = "Deletar carta")
    @ApiResponse(responseCode = "200", description = "Carta deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Carta não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar carta por nome")
    @ApiResponse(responseCode = "200", description = "Carta encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Carta não encontrada")
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<CardEntity>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<CardEntity> pagina = service.buscarPorNome(nome, pageable);

        List<EntityModel<CardEntity>> lista = pagina.getContent().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CardController.class)
                                .buscar(c.getId())).withSelfRel()
                ))
                .toList();

        PagedModel<EntityModel<CardEntity>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }
}