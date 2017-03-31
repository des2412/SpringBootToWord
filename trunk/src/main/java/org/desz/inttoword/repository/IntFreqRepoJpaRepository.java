package org.desz.inttoword.repository;

import org.desz.inttoword.persistence.document.NumberFrequency;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IntFreqRepoJpaRepository extends MongoRepository<NumberFrequency, String> {

	boolean isAvailable();

}
