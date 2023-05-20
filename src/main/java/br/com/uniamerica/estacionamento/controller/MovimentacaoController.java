/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@RestController
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
        return ResponseEntity.ok(this.movimentacaoRepository.findAllAbertas());
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerMovimentacao(@RequestBody @Validated final Movimentacao movimentacao) {
        try {
            this.movimentacaoService.validarCadastroMovimentacao(movimentacao);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarMovimentacao(
            @RequestBody @Validated final Movimentacao movimentacao
    ) {

        try {
            this.movimentacaoService.validarUpdateMovimentacao(movimentacao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* -------------------delete--------------------------- */
    @DeleteMapping
    public ResponseEntity<?> exluirMovimentacao(@RequestParam("id") final Long id) {
        try {
            this.movimentacaoService.validarDeleteMovimentacao(id);
            final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Movimentacao não encontrada"));
            if (!this.movimentacaoRepository.findByCondutorId(id).isEmpty()) {
                movimentacao.setAtivo(false);
                this.movimentacaoRepository.save(movimentacao);
                return ResponseEntity.ok("Registro Desativado com sucesso!");
            } else {
                this.movimentacaoRepository.delete(movimentacao);
                return ResponseEntity.ok("Registro apagado com sucesso!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}