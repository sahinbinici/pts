package com.pts.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlakaHareket {
    private String hareketTipi;
    private Integer kameraNo;
    private String plaka;
    private LocalDateTime sonGecisTarih;
}
