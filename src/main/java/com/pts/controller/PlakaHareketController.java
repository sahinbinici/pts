package com.pts.controller;

import com.pts.entity.PlakaHareket;
import com.pts.service.PlakaHareketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/plaka-hareket")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlakaHareketController {

    private final PlakaHareketService plakaHareketService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/list")
    public List<PlakaHareket> getAllPlakaHareketler() {
        log.debug("getAllPlakaHareketler endpoint called");
        List<PlakaHareket> hareketler = plakaHareketService.getAllPlakaHareketler();
        log.debug("Found {} hareketler", hareketler.size());
        return hareketler;
    }

    @GetMapping("/last-ten")
    public List<PlakaHareket> getLastTenPlates() {
        log.debug("getLastTenPlates endpoint called");
        List<PlakaHareket> plates = plakaHareketService.getLastTenPlates();
        log.debug("Returning {} plates", plates.size());
        return plates;
    }

    @GetMapping("/recent")
    public List<PlakaHareket> getRecentMovements() {
        log.debug("getRecentMovements endpoint called");
        List<PlakaHareket> movements = plakaHareketService.getRecentMovements();
        log.debug("Found {} recent movements", movements.size());
        return movements;
    }
} 