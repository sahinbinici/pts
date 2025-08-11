package com.pts.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pts.entity.PlakaHareket;
import com.pts.repository.PlakaHareketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlakaHareketService {

    private final PlakaHareketRepository plakaHareketRepository;

    public List<PlakaHareket> getRecentMovements() {
        return plakaHareketRepository.findMovementsInLastThirtyMinutes();
    }

    public PlakaHareket getLastMovement() {
        return plakaHareketRepository.findLastMovement();
    }

    public List<PlakaHareket> searchPlakaHareketByDateRange(LocalDateTime startDate, LocalDateTime endDate, String plaka) {
        return plakaHareketRepository.searchPlakaHareketByDateRange(startDate, endDate, plaka);
    }
} 