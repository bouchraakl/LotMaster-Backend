/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@Controller
@RequestMapping("api/configuracao")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    /* -------------------get by id--------------------------- */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Configuracao configuracao = this.configuracaoRepository.findById(id).orElse(null);
        return configuracao == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(configuracao);
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerMarca(@RequestBody final Configuracao configuracao) {
        try {
            this.configuracaoRepository.save(configuracao);
            return ResponseEntity.ok("Configuracao Registrada Com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ERRO :" + e.getCause().getCause().getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarMarca(
            @RequestParam("id") final Long id,
            @RequestBody final Configuracao configuracao
    ) {
        try {
            final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);
            if (configuracaoBanco == null || configuracaoBanco.getId().equals(configuracao.getId())) {
                throw new RuntimeException("Nao foi possivel identificar o Registro informado");
            }
            this.configuracaoRepository.save(configuracao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
