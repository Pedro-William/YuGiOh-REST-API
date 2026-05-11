package senac.tsi.api_YuGiOh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import senac.tsi.api_YuGiOh.entities.Atributo;
import senac.tsi.api_YuGiOh.repositories.AtributoRepository;
import senac.tsi.api_YuGiOh.exceptions.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class AtributoService {

    private final AtributoRepository repo;

    public Page<Atributo> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Atributo buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atributo não encontrado"));
    }

    public Atributo criar(Atributo atributo) {
        return repo.save(atributo);
    }

    public Atributo atualizar(Long id, Atributo atributo) {

        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Atributo não encontrado");
        }

        atributo.setId(id);
        return repo.save(atributo);
    }

    public void deletar(Long id) {

        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Atributo não encontrado");
        }

        repo.deleteById(id);
    }

    public Page<Atributo> buscarPorNome(String nome, Pageable pageable) {

        Page<Atributo> resultado =
                repo.findByNomeContainingIgnoreCase(nome, pageable);

        if (resultado.getTotalElements() == 0) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhum atributo encontrado com esse nome"
            );
        }

        return resultado;
    }
}