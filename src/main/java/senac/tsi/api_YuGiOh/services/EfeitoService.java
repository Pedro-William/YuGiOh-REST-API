package senac.tsi.api_YuGiOh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import senac.tsi.api_YuGiOh.entities.Efeito;
import senac.tsi.api_YuGiOh.exceptions.ResourceNotFoundException;
import senac.tsi.api_YuGiOh.repositories.EfeitoRepository;

@Service
@RequiredArgsConstructor
public class EfeitoService {

    private final EfeitoRepository repo;

    public Page<Efeito> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Efeito buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Efeito não encontrado"));
    }

    public Efeito criar(Efeito e) {
        return repo.save(e);
    }

    public Efeito atualizar(Long id, Efeito e) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Efeito não encontrado");
        }
        e.setId(id);
        return repo.save(e);
    }

    public void deletar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Efeito não encontrado");
        }
        repo.deleteById(id);
    }

    public Page<Efeito> buscarPorDescricao(String descricao, Pageable pageable) {

        Page<Efeito> resultado =
                repo.findByDescricaoContainingIgnoreCase(descricao, pageable);

        if (resultado.getTotalElements() == 0) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhum efeito encontrado"
            );
        }

        return resultado;
    }
}
