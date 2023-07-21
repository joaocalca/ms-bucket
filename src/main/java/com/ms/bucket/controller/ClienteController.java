package com.ms.bucket.controller;

import com.ms.bucket.dto.ClienteDTO;
import com.ms.bucket.model.Cliente;
import com.ms.bucket.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/inserir")
    public ResponseEntity<Cliente> inserirCliente(@RequestBody @Valid ClienteDTO clienteDTO) {

        Cliente cliente = clienteService.inserir(clienteDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping("/buscar/{idCliente}")
    public ResponseEntity<ClienteDTO> buscarCliente(@PathVariable Long idCliente) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscar(idCliente));
    }

    @GetMapping("/buscar-todos")
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientes(@PageableDefault()Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscarTodos(pageable));
    }

    @PutMapping("/atualizar/{idCliente}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long idCliente, @RequestBody ClienteDTO clienteDTO) {

        clienteService.atualizar(idCliente, clienteDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/remover/{idCliente}")
    public ResponseEntity<?> removerCliente(@PathVariable Long idCliente) {

        clienteService.remover(idCliente);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping(value = "{idCliente}/upload-imagem",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadClienteImagem(@PathVariable Long idCliente, @RequestParam("file") MultipartFile file) {

        clienteService.uploadImagem(idCliente, file);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/{idCliente}/download-imagem",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadClienteImagem(@PathVariable Long idCliente) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.downloadImagem(idCliente));
    }
}
