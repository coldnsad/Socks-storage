package org.example.socks.repository;

import org.example.socks.model.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long>, JpaSpecificationExecutor<Socks> {
    Optional<Socks> findByColorAndCottonPercentage(String color, int cottonPercentage);
}
