/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.service.ConfiguracaoService;
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

    @Autowired
    private ConfiguracaoService configuracaoService;

    /* -------------------get by id--------------------------- */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Configuracao configuracao = this.configuracaoRepository.findById(id).orElse(null);
        return configuracao == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(configuracao);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getallRequest() {
        return ResponseEntity.ok(this.configuracaoRepository.findAll());
    }


    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerMarca(@RequestBody final Configuracao configuracao) {
        try {
            this.configuracaoService.validarCadastroConfiguracao(configuracao);
            this.configuracaoRepository.save(configuracao);
            return ResponseEntity.ok("Configuracao Registrada Com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarConfiguracao(
            @RequestParam("id") final Long id,
            @RequestBody final Configuracao configuracao
    ) {
        try {
            this.configuracaoService.validarUpdateConfiguracao(configuracao);
            this.configuracaoRepository.save(configuracao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
