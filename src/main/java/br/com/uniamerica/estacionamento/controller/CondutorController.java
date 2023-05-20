/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.CondutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@RestController
@RequestMapping(value = "api/condutor")
public class CondutorController {

    @Autowired
    private CondutorRepository condutorRepository;

    @Autowired
    private CondutorService condutorService;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;


    /* -------------------get by id--------------------------- */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Condutor condutor = this.condutorRepository.findById(id).orElse(null);
        return condutor == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(condutor);
    }

    /* -------------------get by all--------------------------- */
    @GetMapping("/all")
    public ResponseEntity<?> findByAllRequest() {
        return ResponseEntity.ok(this.condutorRepository.findAll());
    }

    /* -------------------get by ativo--------------------------- */
    @GetMapping("/ativos")
    public ResponseEntity<?> findCondutoresAtivos() {
        return ResponseEntity.ok(this.condutorRepository.findAllAtivo());
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> cadastrarCondutor(@RequestBody @Validated final Condutor condutor) {
        try {
            this.condutorService.validarCadastroCondutor(condutor);
            return ResponseEntity.ok("Condutor Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarCondutor(
            @RequestBody @Validated final Condutor condutor
    ) {
        try {
            this.condutorService.validarUpdateCondutor(condutor);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());

        }
    }

    /* -------------------delete--------------------------- */
    @DeleteMapping
    public ResponseEntity<?> exluirCondutor(@RequestParam("id") final Long id) {
        try {
            this.condutorService.validarDeleteCondutor(id);
            final Condutor condutorBanco = this.condutorRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Condutor não encontrado"));
            if (!this.movimentacaoRepository.findByCondutorId(id).isEmpty()) {
                condutorBanco.setAtivo(false);
                this.condutorRepository.save(condutorBanco);
                return ResponseEntity.ok("Registro Desativado com sucesso!");
            } else {
                this.condutorRepository.delete(condutorBanco);
                return ResponseEntity.ok("Registro apagado com sucesso!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}