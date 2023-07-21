package com.ms.bucket.service;

import com.ms.bucket.dto.ClienteDTO;
import com.ms.bucket.mapper.ClienteMapper;
import com.ms.bucket.model.Cliente;
import com.ms.bucket.repository.ClienteRepository;
import com.ms.bucket.s3.S3Buckets;
import com.ms.bucket.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private S3Buckets s3Buckets;

    @Autowired
    private S3Service s3Service;

    private final ClienteMapper clienteMapper = ClienteMapper.INSTANCE;

    public Cliente inserir(ClienteDTO clienteDTO) {
        return clienteRepository.save(clienteMapper.toObject(clienteDTO));
    }

    public void atualizar(Long idCliente, ClienteDTO clienteDTO) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);

        if (clienteOptional.isPresent()) {

            Cliente cliente = clienteOptional.get();

            clienteRepository.save(clienteMapper.updateObjectFromDTO(clienteDTO, cliente));
        }
    }

    public ClienteDTO buscar(Long idCliente) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);

        Cliente cliente = clienteOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        return clienteMapper.toDTO(cliente);
    }

    public Page<ClienteDTO> buscarTodos(Pageable pageable) {

        Page<Cliente> clientePage = clienteRepository.findAll(pageable);

        List<ClienteDTO> clienteDTOList = clientePage.stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(clienteDTOList, clientePage.getPageable(), clientePage.getTotalElements());
    }

    public void remover(Long idCliente) {

        buscar(idCliente);

        clienteRepository.deleteById(idCliente);
    }

    public void uploadImagem(Long idCliente, MultipartFile file) {

        String idImagemCliente = UUID.randomUUID().toString();

        if (clienteRepository.existsById(idCliente)) {

            try {
                s3Service.putObject(
                        s3Buckets.getCustomer(),
                        String.format("clientes-imagens/%s/%s", idCliente, idImagemCliente),
                        file.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException("Falha ao realizar upload da imagem", e);
            }

            ClienteDTO clienteDTO = buscar(idCliente);
            clienteDTO.setIdImagemCliente(idImagemCliente);
            atualizar(idCliente, clienteDTO);
        }
    }

    public byte[] downloadImagem(Long idCliente) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);

        if (clienteOptional.isPresent()) {
            return s3Service.getObject(
                    s3Buckets.getCustomer(),
                    String.format("clientes-imagens/%s/%s", idCliente, clienteOptional.get().getIdImagemCliente())
            );
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }
    }
}
