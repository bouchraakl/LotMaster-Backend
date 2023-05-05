/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@Controller
@RequestMapping("api/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private MovimentacaoService movimentacaoService;

    /* -------------------get by id--------------------------- */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(movimentacao);
    }

    /* -------------------get by all--------------------------- */
    @GetMapping("/all")
    public ResponseEntity<?> findByAllRequest() {
        return ResponseEntity.ok(this.movimentacaoRepository.findAll());
    }

    /* -------------------get by abertos--------------------------- */
    @GetMapping("/abertas")
    public ResponseEntity<?> findMovimentacoesAbertas() {
        return ResponseEntity.ok(this.movimentacaoRepository.findByMovAbertas());
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerMovimentacao(@RequestBody final Movimentacao movimentacao) {
        try {
            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarMovimentacao(
            @RequestParam("id") final Long id,
            @RequestBody final Movimentacao movimentacao
    ) {

        try {
            this.movimentacaoService.validarCadastroMovimentacao(movimentacao);
            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /* -------------------delete--------------------------- */
    @DeleteMapping
    public ResponseEntity<?> exluirMovi(@RequestParam("id") final Long id) {
        try {
            final Movimentacao moviBanco = this.movimentacaoRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Condutor não encontrado"));
            if (!moviBanco.isAtivo()){
                this.movimentacaoRepository.delete(moviBanco);
            }
            return ResponseEntity.ok("Registro apagado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}