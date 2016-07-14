package com.moviespace.app.web.rest.mapper;

import com.moviespace.app.domain.*;
import com.moviespace.app.web.rest.dto.PersonCastDTO;

import org.mapstruct.*;
import java.util.List;


@Mapper(componentModel = "spring", uses = {})
public interface PersonCastMapper {

	
    PersonCastDTO personCastToPersonCastDTO(PersonCast personCast);

    List<PersonCastDTO> personCastsToPersonCastDTOs(List<PersonCast> languages);

    PersonCast personCastDTOToPersonCast(PersonCastDTO personCastDTO);

    List<PersonCast> personCastDTOsToPersonCasts(List<PersonCastDTO> personCastDTOs);

	
}

