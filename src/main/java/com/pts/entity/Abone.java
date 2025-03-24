package com.pts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class Abone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Aboneld", nullable = false)
    private Integer id;

    @Column(name = "AboneKayitDurum", nullable = false)
    private Integer aboneKayitDurum;

    @Column(name = "Grup", nullable = false)
    private Integer grup;

    @Column(name = "Durum", nullable = false)
    private Integer durum;

    @Column(name = "Otopark", nullable = false)
    private Integer otopark;

    @Column(name = "AracSiniri", nullable = false)
    private Integer aracSiniri;

    @Nationalized
    @Column(name = "Plaka", nullable = false, length = 50)
    private String plaka;

    @Nationalized
    @Column(name = "Ad", nullable = false, length = 60)
    private String ad;

    @Nationalized
    @Column(name = "Soyad", nullable = false, length = 150)
    private String soyad;

    @Nationalized
    @ColumnDefault("isnull([Ad], '')+' '+isnull([Soyad], '')")
    @Column(name = "AdSoyad", nullable = false, length = 211)
    private String adSoyad;

    @Column(name = "AbonelikBaslamaTarih", nullable = false)
    private Instant abonelikBaslamaTarih;

    @Column(name = "AbonelikBitisTarih")
    private Instant abonelikBitisTarih;

    @Nationalized
    @Column(name = "TcKimlikNo", length = 11)
    private String tcKimlikNo;

    @Column(name = "SmsHizmeti")
    private Boolean smsHizmeti;

    @Column(name = "EmailHizmeti")
    private Boolean emailHizmeti;

    @Column(name = "InsertedDate", nullable = false)
    private Instant insertedDate;

    @Column(name = "InsertedUser", nullable = false)
    private Integer insertedUser;

    @Column(name = "UpdatedDate")
    private Instant updatedDate;

    @Column(name = "UpdatedUser")
    private Integer updatedUser;

    @Column(name = "DeletedDate")
    private Instant deletedDate;

    @Column(name = "DeletedUser")
    private Integer deletedUser;

    @Nationalized
    @Lob
    @Column(name = "AboneAciklama")
    private String aboneAciklama;

    @Nationalized
    @Column(name = "EtiketNo", length = 50)
    private String etiketNo;

    @Column(name = "Tarife", nullable = false)
    private Integer tarife;

    @Column(name = "Statu", nullable = false)
    private Boolean statu = false;

    @Column(name = "DoorState", nullable = false)
    private Boolean doorState = false;

    @Column(name = "KaraListe", nullable = false)
    private Boolean karaListe = false;

    @Nationalized
    @Column(name = "Email", length = 150)
    private String email;

    @Nationalized
    @Column(name = "Telefon", length = 50)
    private String telefon;

    @Nationalized
    @Column(name = "Adres", length = 150)
    private String adres;

    @Column(name = "Pazartesi", nullable = false)
    private Boolean pazartesi = false;

    @Column(name = "Sali", nullable = false)
    private Boolean sali = false;

    @Column(name = "Carsamba", nullable = false)
    private Boolean carsamba = false;

    @Column(name = "Persembe", nullable = false)
    private Boolean persembe = false;

    @Column(name = "Cuma", nullable = false)
    private Boolean cuma = false;

    @Column(name = "Cumartesi", nullable = false)
    private Boolean cumartesi = false;

    @Column(name = "Pazar", nullable = false)
    private Boolean pazar = false;

    @ColumnDefault("0")
    @Column(name = "Selection")
    private Boolean selection;

    @ColumnDefault("[dbo].[func_GetUyeKalanGunSayisi]([Aboneld])")
    @Column(name = "KalanGun", precision = 18, scale = 4)
    private BigDecimal kalanGun;

    @ColumnDefault("[dbo].[func_GetUyeKalanOdeme]([Aboneld])")
    @Column(name = "KalanOdeme")
    private Integer kalanOdeme;

    @Column(name = "EksiBakiyeKota", precision = 18, scale = 4)
    private BigDecimal eksiBakiyeKota;

    @Column(name = "AracTipi")
    private Integer aracTipi;

    @Column(name = "AracMarka")
    private Integer aracMarka;

    @Column(name = "AracModel")
    private Integer aracModel;

    @Column(name = "AracRenk")
    private Integer aracRenk;

    @Nationalized
    @Column(name = "ModelYili", length = 4)
    private String modelYili;

}