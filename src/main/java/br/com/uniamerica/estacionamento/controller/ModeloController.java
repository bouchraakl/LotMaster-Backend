/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.ModeloService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* ----------------------------------------------------- */
@RestController
@RequestMapping(value = "/api/modelo")
public class ModeloController {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ModeloService modeloService;


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
        List<Modelo> modeloList = this.modeloRepository.findAllAtivo();
        if (modeloList == null || modeloList.isEmpty()) {
            return ResponseEntity.badRequest().body("Não tem nem um modelo ativo");
        } else {
            return ResponseEntity.ok(modeloList);
        }
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerModelo(@RequestBody @Validated final Modelo modelo) {
        try {
            this.modeloService.validarCadastroModelo(modelo);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarModelo(
            @RequestBody @Validated final Modelo modelo
    ) {

        try {
            this.modeloService.validarUpdateModelo(modelo);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    /* -------------------delete--------------------------- */
    @DeleteMapping
    public ResponseEntity<?> exluirMarca(@RequestParam("id") final Long id) {
        try {
            this.modeloService.validarDeleteModelo(id);
            final Modelo modeloBanco = this.modeloRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Modelo não encontrada"));
            if (!this.modeloRepository.findByMarcaId(id).isEmpty()) {
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
