package com.pts.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlakaHareket {
    private Long plakaHareketId;
    private Integer aboneId;
    private String plaka;
    private LocalDateTime girisTarihi;
    private LocalDateTime cikisTarihi;
    private Integer girisKameraId;
    private Integer cikisKameraId;
}
