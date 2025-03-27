package com.pts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pts.entity.PlakaHareket;
import com.pts.service.PlakaHareketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/plaka-hareket")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlakaHareketController {

    private final PlakaHareketService plakaHareketService;

    @GetMapping("/recent")
    public List<PlakaHareket> getRecentMovements() {
        log.debug("getRecentMovements endpoint called");
        List<PlakaHareket> movements = plakaHareketService.getRecentMovements();
        log.debug("Found {} recent movements", movements.size());
        return movements;
    }

    @GetMapping("/last-movement")
    public PlakaHareket getLastMovement() {
        log.debug("getLastMovement endpoint called");
        PlakaHareket movement = plakaHareketService.getLastMovement();
        log.debug("Found last movement for plate: {}", movement != null ? movement.getPlaka() : "null");
        return movement;
    }
} 