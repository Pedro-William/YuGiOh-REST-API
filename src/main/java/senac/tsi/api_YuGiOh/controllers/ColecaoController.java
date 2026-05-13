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

import senac.tsi.api_YuGiOh.entities.Colecao;
import senac.tsi.api_YuGiOh.services.ColecaoService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Coleções", description = "Gerenciamento de coleções")
@RestController
@RequestMapping("/colecoes")
@RequiredArgsConstructor
public class ColecaoController {

    private final ColecaoService service;

    @Operation(summary = "Listar coleções")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Colecao>>> listar(@ParameterObject Pageable pageable) {

        Page<Colecao> page = service.listar(pageable);

        List<EntityModel<Colecao>> lista = page.getContent().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ColecaoController.class)
                                .buscar(c.getId())).withSelfRel()
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @Operation(summary = "Buscar coleção por ID")
    @ApiResponse(responseCode = "200", description = "Coleção encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Coleção não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Colecao>> buscar(@PathVariable Long id) {

        Colecao colecao = service.buscarPorId(id);

        return ResponseEntity.ok(EntityModel.of(colecao,

                linkTo(methodOn(ColecaoController.class)
                        .buscar(id)).withSelfRel(),

                linkTo(methodOn(ColecaoController.class)
                        .listar(Pageable.unpaged())).withRel("colecoes"),

                linkTo(methodOn(ColecaoController.class)
                        .atualizar(id, colecao)).withRel("update"),

                linkTo(methodOn(ColecaoController.class)
                        .deletar(id)).withRel("delete")
        ));
    }

    @Operation(summary = "Criar coleção")
    @ApiResponse(responseCode = "201", description = "Coleção criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @PostMapping
    public ResponseEntity<EntityModel<Colecao>> criar(@RequestBody Colecao colecao) {

        Colecao salvo = service.criar(colecao);

        return ResponseEntity
                .created(linkTo(methodOn(ColecaoController.class)
                        .buscar(salvo.getId())).toUri())
                .body(EntityModel.of(salvo,

                        linkTo(methodOn(ColecaoController.class)
                                .buscar(salvo.getId())).withSelfRel()
                ));
    }

    @Operation(summary = "Atualizar coleção")
    @ApiResponse(responseCode = "200", description = "Coleção atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Coleção não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Colecao>> atualizar(
            @PathVariable Long id,
            @RequestBody Colecao colecao) {

        Colecao atualizado = service.atualizar(id, colecao);

        return ResponseEntity.ok(EntityModel.of(atualizado,

                linkTo(methodOn(ColecaoController.class)
                        .buscar(id)).withSelfRel()
        ));
    }

    @Operation(summary = "Deletar coleção")
    @ApiResponse(responseCode = "200", description = "Coleção deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Coleção não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar coleção por nome")
    @ApiResponse(responseCode = "200", description = "Coleção encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Coleção não encontrada")
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Colecao>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<Colecao> pagina = service.buscarPorNome(nome, pageable);

        List<EntityModel<Colecao>> lista = pagina.getContent().stream()
                .map(c -> EntityModel.of(c,

                        linkTo(methodOn(ColecaoController.class)
                                .buscar(c.getId())).withSelfRel()
                ))
                .toList();

        PagedModel<EntityModel<Colecao>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }
}