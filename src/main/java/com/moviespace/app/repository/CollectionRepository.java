package com.moviespace.app.repository;

import com.moviespace.app.domain.Collection;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Collection entity.
 */
@SuppressWarnings("unused")
public interface CollectionRepository extends JpaRepository<Collection,Long> {
	
	Collection findOneByExternalId(Integer id);
	
	@Query("select collection from Collection collection left join fetch collection.parts where collection.id =:id")
    Collection findOneWithEagerRelationships(@Param("id") Long id);

	@Query("select count(collection) from Collection collection")
	Integer findTotalCollectionCount();
	
}
