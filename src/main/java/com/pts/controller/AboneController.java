package com.pts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pts.entity.AboneData;
import com.pts.service.AboneService;

@Controller
@RequestMapping("/abone")
@CrossOrigin(origins = "*")
public class AboneController {
    private static final Logger logger = LoggerFactory.getLogger(AboneController.class);
    private final AboneService aboneService;

    public AboneController(AboneService aboneService) {
        this.aboneService = aboneService;
        logger.info("AboneController initialized");
    }

    @PostMapping("/kaydet")
    @ResponseBody
    public ResponseEntity<?> aboneKaydet(@RequestBody AboneData aboneData) {
        logger.info("Abone kaydet request received for plaka: {}", aboneData.getPlaka());
        Map<String, String> response = new HashMap<>();
        
        try {
            if (aboneService.plakaVarMi(aboneData.getPlaka())) {
                response.put("error", "Bu plaka zaten kayıtlı!");
                return ResponseEntity.badRequest().body(response);
            }

            Integer result = aboneService.createAbone(aboneData);
            if (result > 0) {
                response.put("success", "Abone başarıyla kaydedildi.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Abone kaydı başarısız oldu.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Error while saving abone", e);
            response.put("error", "Kayıt sırasında bir hata oluştu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/ara")
    @ResponseBody
    public ResponseEntity<?> aboneAra(@RequestBody Map<String, String> searchParams) {
        String plaka = searchParams.get("plaka");
        String tcKimlikNo = searchParams.get("tcKimlikNo");
        String adSoyad = searchParams.get("adSoyad");
        
        logger.info("Abone arama request received with plaka: {}, TC: {}, Ad Soyad: {}", plaka, tcKimlikNo, adSoyad);
        
        try {
            List<AboneData> aboneler;
            
            if (plaka != null && !plaka.trim().isEmpty()) {
                aboneler = aboneService.findAllByPlaka(plaka);
            } else if (tcKimlikNo != null && !tcKimlikNo.trim().isEmpty()) {
                aboneler = aboneService.findAllByTcKimlikNo(tcKimlikNo);
            } else if (adSoyad != null && !adSoyad.trim().isEmpty()) {
                aboneler = aboneService.findAllByAdSoyad(adSoyad);
            } else {
                return ResponseEntity.badRequest().body("Plaka, TC Kimlik No veya Ad Soyad gerekli!");
            }
            
            if (!aboneler.isEmpty()) {
                logger.info("Found {} subscribers", aboneler.size());
                return ResponseEntity.ok(aboneler);
            } else {
                logger.info("No subscribers found");
                return ResponseEntity.ok(java.util.Collections.emptyList());
            }
        } catch (Exception e) {
            logger.error("Error while searching subscribers", e);
            return ResponseEntity.internalServerError().body("Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @PutMapping("/guncelle")
    @ResponseBody
    public ResponseEntity<?> aboneGuncelle(@RequestBody AboneData aboneData) {
        logger.info("Abone güncelle request received for plaka: {}", aboneData.getPlaka());
        Map<String, String> response = new HashMap<>();
        
        try {
            Integer result = aboneService.updateAbone(aboneData);
            if (result > 0) {
                response.put("success", "Abone başarıyla güncellendi.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Abone güncelleme işlemi başarısız oldu.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Error while updating abone", e);
            response.put("error", "Güncelleme sırasında bir hata oluştu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/sil/{plaka}")
    @ResponseBody
    public ResponseEntity<?> aboneSil(@PathVariable String plaka) {
        logger.info("Abone silme request received for plaka: {}", plaka);
        Map<String, String> response = new HashMap<>();
        
        try {
            Integer result = aboneService.deleteAbone(plaka);
            if (result > 0) {
                response.put("success", "Abone başarıyla silindi.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Abone silme işlemi başarısız oldu.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Error while deleting abone", e);
            response.put("error", "Silme sırasında bir hata oluştu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 