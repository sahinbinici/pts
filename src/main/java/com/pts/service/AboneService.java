package com.pts.service;

import com.pts.entity.AboneData;
import com.pts.repository.AboneDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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
        return aboneDataRepository.existsByPlaka(plaka);
    }

    public List<AboneData> findAllByPlaka(String plaka) {
        return aboneDataRepository.findAllByPlaka(plaka);
    }

    public List<AboneData> findAllByTcKimlikNo(String tcKimlikNo) {
        return aboneDataRepository.findAllByTcKimlikNo(tcKimlikNo);
    }

    public Optional<AboneData> findByPlaka(String plaka) {
        List<AboneData> aboneler = findAllByPlaka(plaka);
        return aboneler.isEmpty() ? Optional.empty() : Optional.of(aboneler.get(0));
    }

    public Optional<AboneData> findByTcKimlikNo(String tcKimlikNo) {
        List<AboneData> aboneler = findAllByTcKimlikNo(tcKimlikNo);
        return aboneler.isEmpty() ? Optional.empty() : Optional.of(aboneler.get(0));
    }
} 