package com.moviespace.app.web.rest.mapper;

import com.moviespace.app.domain.*;
import com.moviespace.app.web.rest.dto.LanguageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Language and its DTO LanguageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LanguageMapper {

    LanguageDTO languageToLanguageDTO(Language language);

    List<LanguageDTO> languagesToLanguageDTOs(List<Language> languages);

    @Mapping(target = "movies", ignore = true)
    Language languageDTOToLanguage(LanguageDTO languageDTO);

    List<Language> languageDTOsToLanguages(List<LanguageDTO> languageDTOs);
}
