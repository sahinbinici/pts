package com.pts.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pts.entity.AboneData;
import com.pts.repository.AboneDataRepository;

@Service
@Transactional
public class AboneService {

    private final AboneDataRepository aboneDataRepository;

    public AboneService(AboneDataRepository aboneDataRepository) {
        this.aboneDataRepository = aboneDataRepository;
    }

    public Integer createAbone(AboneData aboneData) {
        return aboneDataRepository.createAbone(aboneData);
    }

    public Integer updateAbone(AboneData aboneData) {
        return aboneDataRepository.updateAbone(aboneData);
    }

    public Integer deleteAbone(String plaka) {
        return aboneDataRepository.deleteAbone(plaka);
    }

    public boolean plakaVarMi(String plaka) {
        return !aboneDataRepository.findAllByPlaka(plaka).isEmpty();
    }

    public List<AboneData> findAllByPlaka(String plaka) {
        return aboneDataRepository.findAllByPlaka(plaka);
    }

    public List<AboneData> findAllByTcKimlikNo(String tcKimlikNo) {
        return aboneDataRepository.findAllByTcKimlikNo(tcKimlikNo);
    }

    public List<AboneData> findAllByAdSoyad(String adSoyad) {
        return aboneDataRepository.findAllByAdSoyad(adSoyad);
    }
} 