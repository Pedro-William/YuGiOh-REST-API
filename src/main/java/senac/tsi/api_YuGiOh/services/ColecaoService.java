package senac.tsi.api_YuGiOh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import senac.tsi.api_YuGiOh.entities.Colecao;
import senac.tsi.api_YuGiOh.exceptions.ResourceNotFoundException;
import senac.tsi.api_YuGiOh.repositories.ColecaoRepository;

@Service
@RequiredArgsConstructor
public class ColecaoService {

    private final ColecaoRepository repo;

    public Page<Colecao> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Colecao buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coleção não encontrada"));
    }

    public Colecao criar(Colecao c) {
        return repo.save(c);
    }

    public Colecao atualizar(Long id, Colecao c) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Coleção não encontrada");
        }
        c.setId(id);
        return repo.save(c);
    }

    public void deletar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Coleção não encontrada");
        }
        repo.deleteById(id);
    }

    public Page<Colecao> buscarPorNome(String nome, Pageable pageable) {

        Page<Colecao> resultado =
                repo.findByNomeContainingIgnoreCase(nome, pageable);

        if (resultado.getTotalElements() == 0) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma coleção encontrada com esse nome"
            );
        }

        return resultado;
    }
}
