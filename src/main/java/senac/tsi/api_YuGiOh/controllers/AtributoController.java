package senac.tsi.api_YuGiOh.controllers;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.api_YuGiOh.entities.Atributo;
import senac.tsi.api_YuGiOh.services.AtributoService;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Atributos", description = "Gerenciamento de atributos das cartas")
@RestController
@RequestMapping("/atributos")
@RequiredArgsConstructor
public class AtributoController {

    private final AtributoService service;

    @Operation(
            summary = "Listar atributos",
            description = "Retorna uma lista paginada de atributos com links HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Atributo>>> listar(@ParameterObject Pageable pageable) {

        Page<Atributo> page = service.listar(pageable);

        List<EntityModel<Atributo>> atributos = page.getContent().stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AtributoController.class).buscar(a.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(atributos,
                        linkTo(methodOn(AtributoController.class).listar(pageable)).withSelfRel()
                )
        );
    }

    @Operation(
            summary = "Buscar atributo por ID",
            description = "Retorna um atributo específico"
    )
    @ApiResponse(responseCode = "200", description = "Atributo encontrado")
    @ApiResponse(responseCode = "404", description = "Atributo não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Atributo>> buscar(@PathVariable Long id) {

        Atributo atributo = service.buscarPorId(id);

        return ResponseEntity.ok(
                EntityModel.of(atributo,
                        linkTo(methodOn(AtributoController.class).buscar(id)).withSelfRel(),
                        linkTo(methodOn(AtributoController.class).listar(Pageable.unpaged())).withRel("atributos"),
                        linkTo(methodOn(AtributoController.class).atualizar(id, atributo)).withRel("update"),
                        linkTo(methodOn(AtributoController.class).deletar(id)).withRel("delete")
                )
        );
    }

    @Operation(
            summary = "Criar atributo",
            description = "Cria um novo atributo no sistema"
    )
    @ApiResponse(responseCode = "201", description = "Atributo criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<EntityModel<Atributo>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto atributo a ser criado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Atributo.class),
                            examples = @ExampleObject(value = "{\"nome\": \"LUZ\"}")
                    )
            )
            @RequestBody Atributo atributo) {

        Atributo salvo = service.criar(atributo);

        return ResponseEntity
                .created(linkTo(methodOn(AtributoController.class).buscar(salvo.getId())).toUri())
                .body(EntityModel.of(salvo,
                        linkTo(methodOn(AtributoController.class).buscar(salvo.getId())).withSelfRel()
                ));
    }

    @Operation(
            summary = "Atualizar atributo",
            description = "Atualiza um atributo existente pelo ID"
    )
    @ApiResponse(responseCode = "200", description = "Atributo atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Atributo não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Atributo>> atualizar(@PathVariable Long id,
                                                           @RequestBody Atributo atributo) {

        Atributo atualizado = service.atualizar(id, atributo);

        return ResponseEntity.ok(
                EntityModel.of(atualizado,
                        linkTo(methodOn(AtributoController.class).buscar(id)).withSelfRel()
                )
        );
    }

    @Operation(
            summary = "Deletar atributo",
            description = "Remove um atributo pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Atributo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Atributo não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar atributos por nome",
            description = "Retorna atributos filtrados pelo nome"
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Atributo não encontrado")
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Atributo>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<Atributo> pagina = service.buscarPorNome(nome, pageable);

        List<EntityModel<Atributo>> lista = pagina.getContent().stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AtributoController.class)
                                .buscar(a.getId())).withSelfRel()
                ))
                .toList();

        PagedModel<EntityModel<Atributo>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }
}