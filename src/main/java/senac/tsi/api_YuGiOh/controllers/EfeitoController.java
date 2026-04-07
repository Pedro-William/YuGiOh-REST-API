package senac.tsi.api_YuGiOh.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.api_YuGiOh.entities.Efeito;
import senac.tsi.api_YuGiOh.repositories.EfeitoRepository;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Efeitos", description = "Gerenciamento de efeitos das cartas")
@RestController
@RequestMapping("/efeitos")
@RequiredArgsConstructor
public class EfeitoController {

    private final EfeitoRepository repo;

    @Operation(summary = "Listar efeitos", description = "Retorna lista paginada de efeitos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Efeito>>> listar(Pageable pageable) {

        Page<Efeito> page = repo.findAll(pageable);

        List<EntityModel<Efeito>> efeitos = page.getContent().stream()
                .map(e -> EntityModel.of(e))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(efeitos));
    }

    @Operation(
            summary = "Buscar efeito por ID",
            description = "Retorna um efeito específico pelo ID"
    )
    @ApiResponse(responseCode = "200", description = "Efeito encontrado")
    @ApiResponse(responseCode = "404", description = "Efeito não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Efeito>> buscar(@PathVariable Long id) {

        Efeito efeito = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Efeito não encontrado"));

        return ResponseEntity.ok(EntityModel.of(efeito,
                linkTo(methodOn(EfeitoController.class).listar(Pageable.unpaged())).withRel("efeitos"),
                linkTo(methodOn(EfeitoController.class).atualizar(id, efeito)).withRel("update"),
                linkTo(methodOn(EfeitoController.class).deletar(id)).withRel("delete")
        ));
    }

    @Operation(summary = "Criar efeito", description = "Cria um novo efeito")
    @ApiResponse(responseCode = "201", description = "Efeito criado com sucesso")
    @PostMapping
    public ResponseEntity<EntityModel<Efeito>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto efeito a ser criado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Efeito.class),
                            examples = @ExampleObject(
                                    value = "{\"descricao\": \"Aumenta ataque\"}"
                            )
                    )
            )
            @RequestBody Efeito efeito) {

        Efeito salvo = repo.save(efeito);

        return ResponseEntity
                .created(linkTo(methodOn(EfeitoController.class).listar(Pageable.unpaged())).toUri())
                .body(EntityModel.of(salvo));
    }

    @Operation(
            summary = "Atualizar efeito",
            description = "Atualiza um efeito existente"
    )
    @ApiResponse(responseCode = "200", description = "Efeito atualizado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Efeito>> atualizar(@PathVariable Long id,
                                                         @RequestBody Efeito efeito) {

        efeito.setId(id);
        Efeito atualizado = repo.save(efeito);

        return ResponseEntity.ok(EntityModel.of(atualizado,
                linkTo(methodOn(EfeitoController.class).buscar(id)).withSelfRel()
        ));
    }

    @Operation(
            summary = "Deletar efeito",
            description = "Remove um efeito pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Efeito removido")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar efeitos por descrição",
            description = "Retorna efeitos filtrados pela descrição com paginação"
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<Page<Efeito>> buscarPorDescricao(@RequestParam String descricao, Pageable pageable) {

        return ResponseEntity.ok(repo.findByDescricaoContaining(descricao, pageable));
    }


}