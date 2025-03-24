package com.pts.controller;

import com.pts.entity.AboneData;
import com.pts.service.AboneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/kontrol/plaka/{plaka}")
    @ResponseBody
    public ResponseEntity<Boolean> plakaKontrol(@PathVariable String plaka) {
        logger.info("Plaka kontrol request received for: {}", plaka);
        return ResponseEntity.ok(aboneService.plakaVarMi(plaka));
    }

    @GetMapping("/ara/plaka/{plaka}")
    @ResponseBody
    public ResponseEntity<?> plakaIleAra(@PathVariable String plaka) {
        logger.info("Plaka ile arama request received for: {}", plaka);
        try {
            List<AboneData> aboneler = aboneService.findAllByPlaka(plaka);
            if (!aboneler.isEmpty()) {
                logger.info("Found {} subscribers for plaka: {}", aboneler.size(), plaka);
                return ResponseEntity.ok(aboneler);
            } else {
                logger.info("No subscribers found for plaka: {}", plaka);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error while searching by plaka: {}", plaka, e);
            return ResponseEntity.internalServerError().body("Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping("/ara/tc/{tcKimlikNo}")
    @ResponseBody
    public ResponseEntity<?> tcKimlikNoIleAra(@PathVariable String tcKimlikNo) {
        logger.info("TC Kimlik No ile arama request received for: {}", tcKimlikNo);
        try {
            List<AboneData> aboneler = aboneService.findAllByTcKimlikNo(tcKimlikNo);
            if (!aboneler.isEmpty()) {
                logger.info("Found {} subscribers for TC: {}", aboneler.size(), tcKimlikNo);
                return ResponseEntity.ok(aboneler);
            } else {
                logger.info("No subscribers found for TC: {}", tcKimlikNo);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error while searching by TC: {}", tcKimlikNo, e);
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