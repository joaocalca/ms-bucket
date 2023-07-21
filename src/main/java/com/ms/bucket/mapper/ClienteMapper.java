package com.ms.bucket.mapper;

import com.ms.bucket.dto.ClienteDTO;
import com.ms.bucket.model.Cliente;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDTO toDTO(Cliente cliente);

    Cliente toObject(ClienteDTO clienteDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Cliente updateObjectFromDTO(ClienteDTO dto, @MappingTarget Cliente entidade);
}
