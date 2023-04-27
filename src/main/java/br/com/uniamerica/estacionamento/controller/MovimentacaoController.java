/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
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
            final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);
            if (movimentacaoBanco == null || movimentacaoBanco.getId().equals(movimentacao.getId())) {
                throw new RuntimeException("Nao foi possivel identificar o registro informado");
            }
            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }

    }


}
