package senac.tsi.api_YuGiOh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import senac.tsi.api_YuGiOh.entities.Deck;
import senac.tsi.api_YuGiOh.exceptions.ResourceNotFoundException;
import senac.tsi.api_YuGiOh.repositories.DeckRepository;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository repo;

    public Page<Deck> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Deck buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck não encontrado"));
    }

    public Deck criar(Deck d) {
        return repo.save(d);
    }

    public Deck atualizar(Long id, Deck d) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Deck não encontrado");
        }
        d.setId(id);
        return repo.save(d);
    }

    public void deletar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Deck não encontrado");
        }
        repo.deleteById(id);
    }

    public Page<Deck> buscarPorNome(String nome, Pageable pageable) {

        Page<Deck> resultado =
                repo.findByNomeContainingIgnoreCase(nome, pageable);

        if (resultado.getTotalElements() == 0) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhum deck encontrado com esse nome"
            );
        }

        return resultado;
    }
}
