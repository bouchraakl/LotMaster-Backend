package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.CondutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/condutor")
public class CondutorController {

    @Autowired
    private CondutorRepository condutorRepository;

    @Autowired
    private CondutorService condutorService;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Retrieves a Condutor by ID.
     *
     * @param id The ID of the Condutor to retrieve.
     * @return ResponseEntity with the Condutor if found, otherwise a bad request response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdRequest(@PathVariable("id") Long id) {
        final Condutor condutor = condutorRepository.findById(id).orElse(null);
        return condutor == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(condutor);
    }

    /**
     * Retrieves all Condutores.
     *
     * @return ResponseEntity with a list of all Condutores.
     */
    @GetMapping
    public ResponseEntity<Page<Condutor>> getAllRequest(Pageable pageable) {
        return ResponseEntity.ok(this.condutorService.listAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Condutor>> getAll() {
        return ResponseEntity.ok(
                this.condutorRepository.findAll()
        );
    }

    @GetMapping("/nome")
    public ResponseEntity<?> getByNome(@RequestParam("nome") String nome) {
        final Condutor condutor = this.condutorRepository.findByNome(nome);

        if (condutor == null || condutor.getNome() == null) {
            return ResponseEntity.badRequest().body("nome inválido");
        }

        return ResponseEntity.ok(condutor);
    }

    @GetMapping("/cpf")
    public ResponseEntity<?> getByCPF(@RequestParam("cpf") String cpf) {
        final Condutor condutor = this.condutorRepository.findbyCPF(cpf);

        if (condutor == null || condutor.getCpf() == null) {
            return ResponseEntity.badRequest().body("cpf inválido");
        }

        return ResponseEntity.ok(condutor);
    }

    /**
     * Retrieves all active Condutores.
     *
     * @return ResponseEntity with a list of active Condutores.
     */
    @GetMapping("/ativos")
    public ResponseEntity<?> findActiveCondutores() {
        return ResponseEntity.ok(condutorRepository.findAllAtivo());
    }

    /**
     * Creates a new Condutor.
     *
     * @param condutor The Condutor object to create.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<?> cadastrarCondutor(@RequestBody @Validated final Condutor condutor) {
        try {
            condutorService.validarCadastroCondutor(condutor);
            return ResponseEntity.ok("Condutor cadastrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing Condutor.
     *
     * @param condutor The updated Condutor object.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PutMapping
    public ResponseEntity<?> editarCondutor(@RequestBody @Validated final Condutor condutor) {
        try {
            condutorService.validarUpdateCondutor(condutor);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * Deletes a Condutor by ID.
     *
     * @param id The ID of the Condutor to delete.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @DeleteMapping
    public ResponseEntity<?> excluirCondutor(@RequestParam("id") final Long id) {
        try {
            condutorService.validarDeleteCondutor(id);
            return ResponseEntity.ok("Registro apagado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
