/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@Controller
@RequestMapping(value = "api/marca")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    /* -------------------get by id--------------------------- */
    @GetMapping
    public ResponseEntity<?> getByIdRequest(@RequestParam("id") final Long id) {
        final Marca marca = this.marcaRepository.findById(id).orElse(null);
        return marca == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(marca);
    }

    /* -------------------get all--------------------------- */
    @GetMapping("/all")
    public ResponseEntity<?> getallRequest() {
        return ResponseEntity.ok(this.marcaRepository.findAll());
    }

    /* -------------------get by ativo--------------------------- */
    @GetMapping("/ativos")
    public ResponseEntity<?> findMarcasAtivas() {
        return ResponseEntity.ok(this.marcaRepository.findAllByActive(true));
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerMarca(@RequestBody final Marca marca) {
        try {
            this.marcaRepository.save(marca);
            return ResponseEntity.ok("Marca Registrada Com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ERRO :" + e.getCause().getCause().getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarMarca(
            @RequestParam("id") final Long id,
            @RequestBody final Marca marca
            ) {
        try {
            final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
            if (marcaBanco == null || marcaBanco.getId().equals(marca.getId())) {
                throw new RuntimeException("Nao foi possivel identificar o Registro informado");
            }
            this.marcaRepository.save(marca);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /* -------------------delete--------------------------- */
    @DeleteMapping
    public ResponseEntity<?> exluirMarca(@RequestParam("id") final Long id) {
        try {
            final Marca marcaBanco = this.marcaRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Condutor não encontrado"));
            if (!this.modeloRepository.findByMarcaId(id).isEmpty()) {
                marcaBanco.setAtivo(false);
                this.marcaRepository.save(marcaBanco);
                return ResponseEntity.ok("Registro Desativado com sucesso!");
            } else {
                this.marcaRepository.delete(marcaBanco);
                return ResponseEntity.ok("Registro apagado com sucesso!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}