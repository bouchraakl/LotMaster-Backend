package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelo")
public class ModeloController {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ModeloService modeloService;

    /**
     * Retrieves a Modelo by ID.
     *
     * @param id The ID of the Modelo to retrieve.
     * @return ResponseEntity with the Modelo if found, otherwise a bad request response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdRequest(@PathVariable("id") Long id) {
        final Modelo modelo = modeloRepository.findById(id).orElse(null);
        return modelo == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(modelo);
    }

    /**
     * Retrieves all Modelos.
     *
     * @return ResponseEntity with a list of all Modelos.
     */
    @GetMapping
    public ResponseEntity<Page<Modelo>> getAllRequest(Pageable pageable) {
        return ResponseEntity.ok(this.modeloService.listAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.modeloRepository.findAll());
    }

    @GetMapping("/nome")
    public ResponseEntity<?> getByNome(@RequestParam("nome") String nome) {
        final Modelo modelo = this.modeloRepository.findByNome(nome);

        if (modelo == null || modelo.getNome() == null) {
            return ResponseEntity.badRequest().body("nome inválido");
        }

        return ResponseEntity.ok(modelo);
    }


    /**
     * Retrieves active Modelos.
     *
     * @return ResponseEntity with a list of active Modelos.
     */
    @GetMapping("/ativos")
    public ResponseEntity<?> findModelosAtivos() {
        List<Modelo> modeloList = modeloRepository.findAllAtivo();
        if (modeloList == null || modeloList.isEmpty()) {
            return ResponseEntity.badRequest().body("Não há nenhum modelo ativo");
        } else {
            return ResponseEntity.ok(modeloList);
        }
    }

    /**
     * Registers a new Modelo.
     *
     * @param modelo The Modelo object to register.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<?> registerModelo(@RequestBody @Validated final Modelo modelo) {
        try {
            modeloService.validarCadastroModelo(modelo);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing Modelo.
     *
     * @param modelo The updated Modelo object.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PutMapping
    public ResponseEntity<?> editarModelo(@RequestBody @Validated final Modelo modelo) {
        try {
            modeloService.validarUpdateModelo(modelo);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Deletes a Modelo by ID.
     *
     * @param id The ID of the Modelo to delete.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @DeleteMapping
    public ResponseEntity<?> excluirModelo(@RequestParam("id") final Long id) {
        try {
            modeloService.validarDeleteModelo(id);
            return ResponseEntity.ok("Registro apagado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
