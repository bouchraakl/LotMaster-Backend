/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@Controller
@RequestMapping(value = "/api/modelo")
public class ModeloController {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;


    /* -------------------get by id--------------------------- */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Modelo modelo = this.modeloRepository.findById(id).orElse(null);
        return modelo == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(modelo);
    }

    /* -------------------get by all--------------------------- */
    @GetMapping("/all")
    public ResponseEntity<?> findByAllRequest() {
        return ResponseEntity.ok(this.modeloRepository.findAll());
    }

    /* -------------------get by ativos--------------------------- */
    @GetMapping("/ativos")
    public ResponseEntity<?> findModeloAtivos() {
        return ResponseEntity.ok(this.modeloRepository.findAllByActive(true));
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerModelo(@RequestBody final Modelo modelo) {
        try {
            this.modeloRepository.save(modelo);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarModelo(
            @RequestParam("id") final Long id,
            @RequestBody final Modelo modelo
    ) {

        try {
            final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
            if (modeloBanco == null || modeloBanco.getId().equals(modelo.getId())) {
                throw new RuntimeException("Nao foi possivel identificar o registro informado");
            }
            this.modeloRepository.save(modelo);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }

    }

    /* -------------------delete--------------------------- */
    @DeleteMapping
    public ResponseEntity<?> exluirModelo(@RequestParam("id") final Long id) {
        try {
            final Modelo modeloBanco = this.modeloRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Modelo não encontrado"));
            if (!this.veiculoRepository.findByModeloId(id).isEmpty()) {
                modeloBanco.setAtivo(false);
                this.modeloRepository.save(modeloBanco);
                return ResponseEntity.ok("Registro Desativado com sucesso!");
            } else {
                this.modeloRepository.delete(modeloBanco);
                return ResponseEntity.ok("Registro apagado com sucesso!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
