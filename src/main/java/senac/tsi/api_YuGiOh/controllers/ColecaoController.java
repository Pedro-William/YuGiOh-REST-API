package senac.tsi.api_YuGiOh.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.api_YuGiOh.entities.Colecao;
import senac.tsi.api_YuGiOh.repositories.ColecaoRepository;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Coleções", description = "Gerenciamento de coleções de cartas")
@RestController
@RequestMapping("/colecoes")
@RequiredArgsConstructor
public class ColecaoController {

    private final ColecaoRepository repo;

    @Operation(summary = "Listar coleções", description = "Retorna lista paginada de coleções")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Colecao>>> listar(Pageable pageable) {

        Page<Colecao> page = repo.findAll(pageable);

        List<EntityModel<Colecao>> colecoes = page.getContent().stream()
                .map(c -> EntityModel.of(c))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(colecoes));
    }

    @Operation(
            summary = "Buscar coleção por ID",
            description = "Retorna uma coleção específica pelo ID"
    )
    @ApiResponse(responseCode = "200", description = "Coleção encontrada")
    @ApiResponse(responseCode = "404", description = "Coleção não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Colecao>> buscar(@PathVariable Long id) {

        Colecao colecao = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Coleção não encontrada"));

        return ResponseEntity.ok(EntityModel.of(colecao,
                linkTo(methodOn(ColecaoController.class).listar(Pageable.unpaged())).withRel("colecoes"),
                linkTo(methodOn(ColecaoController.class).atualizar(id, colecao)).withRel("update"),
                linkTo(methodOn(ColecaoController.class).deletar(id)).withRel("delete")
        ));
    }

    @Operation(summary = "Criar coleção", description = "Cria uma nova coleção")
    @ApiResponse(responseCode = "201", description = "Coleção criada")
    @PostMapping
    public ResponseEntity<EntityModel<Colecao>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto coleção a ser criado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Colecao.class),
                            examples = @ExampleObject(
                                    value = "{\"nome\": \"Starter Deck\"}"
                            )
                    )
            )
            @RequestBody Colecao colecao) {

        Colecao salvo = repo.save(colecao);

        return ResponseEntity
                .created(linkTo(methodOn(ColecaoController.class).listar(Pageable.unpaged())).toUri())
                .body(EntityModel.of(salvo));
    }

    @Operation(
            summary = "Atualizar coleção",
            description = "Atualiza os dados de uma coleção existente"
    )
    @ApiResponse(responseCode = "200", description = "Coleção atualizada")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Colecao>> atualizar(@PathVariable Long id,
                                                          @RequestBody Colecao colecao) {

        colecao.setId(id);
        Colecao atualizado = repo.save(colecao);

        return ResponseEntity.ok(EntityModel.of(atualizado,
                linkTo(methodOn(ColecaoController.class).buscar(id)).withSelfRel()
        ));
    }

    @Operation(
            summary = "Deletar coleção",
            description = "Remove uma coleção pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Coleção removida")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar coleções por nome",
            description = "Retorna coleções filtradas pelo nome com paginação"
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<Page<Colecao>> buscarPorNome(@RequestParam String nome, Pageable pageable) {

        return ResponseEntity.ok(repo.findByNomeContaining(nome, pageable));
    }
}