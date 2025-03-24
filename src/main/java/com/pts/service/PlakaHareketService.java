package com.pts.service;

import com.pts.entity.PlakaHareket;
import com.pts.repository.PlakaHareketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlakaHareketService {

    private final PlakaHareketRepository plakaHareketRepository;

    public List<PlakaHareket> getAllPlakaHareketler() {
        return plakaHareketRepository.findAllByOrderByGirisTarihiDesc();
    }

    public List<PlakaHareket> getLastTenPlates() {
        return plakaHareketRepository.findLastTenPlates();
    }

    public List<PlakaHareket> getRecentMovements() {
        return plakaHareketRepository.findMovementsInLastThirtyMinutes();
    }

    @Transactional
    public PlakaHareket plakaCikisKaydet(PlakaHareket plakaHareket) {
        PlakaHareket mevcutHareket = plakaHareketRepository.findByPlakaAndCikisTarihiIsNull(plakaHareket.getPlaka())
                .orElseThrow(() -> new RuntimeException("Plaka için giriş kaydı bulunamadı: " + plakaHareket.getPlaka()));

        LocalDateTime cikisTarihi = LocalDateTime.now();
        mevcutHareket.setCikisTarihi(cikisTarihi);
        mevcutHareket.setCikisKameraId(plakaHareket.getCikisKameraId());
        return mevcutHareket;
    }
} 