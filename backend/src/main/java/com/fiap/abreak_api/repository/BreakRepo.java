package com.fiap.abreak_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fiap.abreak_api.model.Break;

@Repository
public interface BreakRepo extends JpaRepository<Break, Long> {

        Page<Break> findByUserIdOrderByDateTimeDesc(Long userId, Pageable pageable);

        List<Break> findByUserIdAndDateTimeBetween(Long userId,
                        LocalDateTime start,
                        LocalDateTime end);

        Long countByUserIdAndDateTimeBetween(Long userId,
                        LocalDateTime start,
                        LocalDateTime end);

}
