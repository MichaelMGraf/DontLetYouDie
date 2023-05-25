package de.dontletyoudie.backend.persistence.stat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    List<Stat> findStatsByMiniMeId(Long id);
    Stat findStatByMiniMe_IdAndCategory_Id(Long miniMeId, Long categoryId);
}
