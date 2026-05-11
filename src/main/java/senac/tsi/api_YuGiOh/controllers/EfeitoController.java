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

import senac.tsi.api_YuGiOh.entities.Efeito;
import senac.tsi.api_YuGiOh.services.EfeitoService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Efeitos", description = "Gerenciamento de efeitos das cartas")
@RestController
@RequestMapping("/efeitos")
@RequiredArgsConstructor
public class EfeitoController {

    private final EfeitoService service;

    @Operation(summary = "Listar efeitos")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Efeito>>> listar(Pageable pageable) {

        Page<Efeito> page = service.listar(pageable);

        List<EntityModel<Efeito>> lista = page.getContent().stream()
                .map(e -> EntityModel.of(e,
                        linkTo(methodOn(EfeitoController.class)
                                .buscar(e.getId())).withSelfRel()
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @Operation(summary = "Buscar efeito por ID")
    @ApiResponse(responseCode = "200", description = "Efeito encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Efeito não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Efeito>> buscar(@PathVariable Long id) {

        Efeito efeito = service.buscarPorId(id);

        return ResponseEntity.ok(EntityModel.of(efeito));
    }

    @Operation(summary = "criar efeito")
    @ApiResponse(responseCode = "201", description = "Efeito criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @PostMapping
    public ResponseEntity<EntityModel<Efeito>> criar(@RequestBody Efeito efeito) {

        Efeito salvo = service.criar(efeito);

        return ResponseEntity
                .created(linkTo(methodOn(EfeitoController.class)
                        .buscar(salvo.getId())).toUri())
                .body(EntityModel.of(salvo));
    }

    @Operation(summary = "Atualizar efeito")
    @ApiResponse(responseCode = "200", description = "Efeito atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Efeito nao encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Efeito>> atualizar(
            @PathVariable Long id,
            @RequestBody Efeito efeito) {

        return ResponseEntity.ok(
                EntityModel.of(service.atualizar(id, efeito))
        );
    }

    @Operation(summary = "Deletar efeito")
    @ApiResponse(responseCode = "200", description = "Efeito deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Efeito nao encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar efeito por descricao")
    @ApiResponse(responseCode = "200", description = "Efeito encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Efeito não encontrado")
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Efeito>>> buscarPorDescricao(
            @RequestParam String descricao,
            @ParameterObject Pageable pageable) {

        Page<Efeito> pagina = service.buscarPorDescricao(descricao, pageable);

        List<EntityModel<Efeito>> lista = pagina.getContent().stream()
                .map(e -> EntityModel.of(e,
                        linkTo(methodOn(EfeitoController.class)
                                .buscar(e.getId())).withSelfRel()
                ))
                .toList();

        PagedModel<EntityModel<Efeito>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }
}