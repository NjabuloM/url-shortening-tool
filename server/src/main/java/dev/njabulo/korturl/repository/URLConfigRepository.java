package dev.njabulo.korturl.repository;

import dev.njabulo.korturl.entity.URLConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface URLConfigRepository extends MongoRepository<URLConfig, String> {

    @Query("{id :?0}")
    Optional<URLConfig> findUrlById(String id);

    @Query("{shortenedSuffix: ?0}")
    Optional<URLConfig> findUrlByShortenedSuffix(String shortenedSuffix);

    @Query(value ="{shortenedSuffix: ?0}", exists = true)
    Boolean isGenSuffixInUse(String shortenedSuffix);

}
