package com.pts.service;

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
} 