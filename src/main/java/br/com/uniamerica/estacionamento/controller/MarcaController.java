package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/marca")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private MarcaService marcaService;

    /**
     * Retrieves a Marca by ID.
     *
     * @param id The ID of the Marca to retrieve.
     * @return ResponseEntity with the Marca if found, otherwise a bad request response.
     */
    @GetMapping
    public ResponseEntity<?> getByIdRequest(@RequestParam("id") final Long id) {
        final Marca marca = marcaRepository.findById(id).orElse(null);
        return marca == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(marca);
    }

    /**
     * Retrieves all Marcas.
     *
     * @return ResponseEntity with a list of all Marcas.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllRequest() {
        return ResponseEntity.ok(marcaRepository.findAll());
    }

    /**
     * Retrieves active Marcas.
     *
     * @return ResponseEntity with a list of active Marcas.
     */
    @GetMapping("/ativos")
    public ResponseEntity<?> findMarcasAtivas() {
        return ResponseEntity.ok(marcaRepository.findAllAtivo());
    }

    /**
     * Registers a new Marca.
     *
     * @param marca The Marca object to register.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<?> registerMarca(@RequestBody @Validated final Marca marca) {
        try {
            marcaService.validarCadastroMarca(marca);
            return ResponseEntity.ok("Marca registrada com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing Marca.
     *
     * @param marca The updated Marca object.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PutMapping
    public ResponseEntity<?> editarMarca(@RequestBody @Validated final Marca marca) {
        try {
            marcaService.validarUpdateMarca(marca);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * Deletes a Marca by ID.
     *
     * @param id The ID of the Marca to delete.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @DeleteMapping
    public ResponseEntity<?> excluirCondutor(@RequestParam("id") final Long id) {
        try {
            marcaService.validarDeleteMarca(id);
            return ResponseEntity.ok("Registro apagado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
