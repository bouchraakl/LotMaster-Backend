package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private MovimentacaoService movimentacaoService;

    /**
     * Retrieves a Movimentacao by ID.
     *
     * @param id The ID of the Movimentacao to retrieve.
     * @return ResponseEntity with the Movimentacao if found, otherwise a bad request response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdRequest(@PathVariable("id") Long id) {
        final Movimentacao movimentacao = movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(movimentacao);
    }

    /**
     * Retrieves all Movimentacoes.
     *
     * @return ResponseEntity with a list of all Movimentacoes.
     */
    @GetMapping
    public ResponseEntity<Page<Movimentacao>> getAllRequest(Pageable pageable) {
        return ResponseEntity.ok(this.movimentacaoService.listAll(pageable));
    }
    /**
     * Retrieves open Movimentacoes.
     *
     * @return ResponseEntity with a list of open Movimentacoes.
     */
    @GetMapping("/abertas")
    public ResponseEntity<?> findMovimentacoesAbertas(@RequestParam(value = "placa", required = false) String placa) {
        if (placa != null && !placa.isEmpty()) {
            // Perform a search based on the placa (vehicle license plate)
            List<Movimentacao> movimentacoes = movimentacaoRepository.findMovimentacoesAbertasByPlaca(placa);
            return ResponseEntity.ok(movimentacoes);
        } else {
            // Retrieve all open Movimentacoes
            List<Movimentacao> movimentacoes = movimentacaoRepository.findAllAbertas();
            return ResponseEntity.ok(movimentacoes);
        }
    }


    @GetMapping("/closed")
    public ResponseEntity<?> findMovimentacoesFechadas() {
        return ResponseEntity.ok(movimentacaoRepository.findAllFechadas());
    }

    @GetMapping("/last-five")
    public ResponseEntity<?> findMovimentacoesLastFive() {
        List<Movimentacao> lastFiveMovimentacoes = movimentacaoRepository.findLastFiveByOrderByEntryDateDesc();
        return ResponseEntity.ok(lastFiveMovimentacoes);
    }

    @GetMapping("/last")
    public ResponseEntity<?> getLastRequest() {
        return ResponseEntity.ok(movimentacaoRepository.ultimaMovimentacao());
    }

    /**
     * Registers a new Movimentacao.
     *
     * @param movimentacao The Movimentacao object to register.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<?> registerMovimentacao(@RequestBody @Validated final Movimentacao movimentacao) {
        try {
            movimentacaoService.validarCadastroMovimentacao(movimentacao);
            if (movimentacao.getSaida() != null){
                String relatorio = movimentacaoService.emitirRelatorio(movimentacao);
                return ResponseEntity.ok(relatorio);
            }else{
                return ResponseEntity.ok("Registro Cadastrado com Sucesso");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing Movimentacao.
     *
     * @param movimentacao The updated Movimentacao object.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PutMapping
    public ResponseEntity<?> editarMovimentacao(@RequestBody @Validated final Movimentacao movimentacao) {
        try {
            movimentacaoService.validarUpdateMovimentacao(movimentacao);
            if (movimentacao.getSaida() != null){
                String relatorio = movimentacaoService.emitirRelatorio(movimentacao);
                return ResponseEntity.ok(relatorio);
            }else{
                return ResponseEntity.ok("Registro Atualizado com Sucesso");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * Deletes a Movimentacao by ID.
     *
     * @param id The ID of the Movimentacao to delete.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @DeleteMapping
    public ResponseEntity<?> excluirMovimentacao(@RequestParam("id") final Long id) {
        try {
            movimentacaoService.validarDeleteMovimentacao(id);
            return ResponseEntity.ok("Registro apagado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
