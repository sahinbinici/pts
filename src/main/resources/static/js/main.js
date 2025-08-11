let pollingInterval = null;
let lastMovementData = null;

// Sayfa yüklendiğinde polling'i başlat
document.addEventListener('DOMContentLoaded', function() {
    console.log("Page loaded, initializing polling...");
    startPolling();
    // İlk yüklemede son hareketi göster
    loadLastMovement();

    // Abone arama formu submit handler'ı
    initializeAboneAraForm();
    // Abone güncelleme form handler'ı
    initializeAboneGuncelleForm();
    // Abone silme butonu handler'ı
    initializeAboneSilmeButton();
    // Abone ekleme form handler'ı
    initializeAboneEkleForm();

    // Initialize plaka hareket detay
    if (typeof initializePlakaHareketDetayForm === 'function') {
        initializePlakaHareketDetayForm();
    }

    // Initialize sidebar navigation
    document.querySelectorAll('.nav-link[data-section]').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Remove active class from all nav links
            document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
            this.classList.add('active');
            
            // Hide all content sections
            document.querySelectorAll('.content-section').forEach(section => {
                section.style.display = 'none';
                section.classList.remove('active');
            });
            
            // Show selected content section
            const sectionId = this.getAttribute('data-section') + '-content';
            const selectedSection = document.getElementById(sectionId);
            if (selectedSection) {
                selectedSection.style.display = 'block';
                selectedSection.classList.add('active');
                
                // Update URL hash without triggering navigation
                const newHash = this.getAttribute('data-section');
                history.pushState(null, '', `#${newHash}`);
                
                // Plaka Hareket sayfası açıldığında
                if (sectionId === 'plaka-hareket-content') {
                    loadLastMovement(); // Son hareketi yükle
                    startPolling(); // Polling'i başlat
                } else {
                    stopPolling(); // Başka sekmeye geçildiğinde polling'i durdur
                }
            }
        });
    });

    // Set initial active section based on URL hash
    const hash = window.location.hash.substring(1) || 'abone-ekle';
    const defaultLink = document.querySelector(`.nav-link[data-section="${hash}"]`);
    if (defaultLink) {
        // Manually set active state without triggering click event
        document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
        defaultLink.classList.add('active');
        
        document.querySelectorAll('.content-section').forEach(section => {
            section.style.display = 'none';
            section.classList.remove('active');
        });
        
        const selectedSection = document.getElementById(`${hash}-content`);
        if (selectedSection) {
            selectedSection.style.display = 'block';
            selectedSection.classList.add('active');
            
            if (hash === 'plaka-hareket') {
                loadLastMovement();
                startPolling();
            }
        }
    }
});

function startPolling() {
    // Plaka Hareket sekmesi aktif değilse polling'i başlatma
    const plakaHareketContent = document.getElementById('plaka-hareket-content');
    if (!plakaHareketContent || !plakaHareketContent.classList.contains('active')) {
        return;
    }

    // Son hareketi 5 saniyede bir kontrol et
    pollingInterval = setInterval(() => {
        loadLastMovement();
    }, 10000);
    
    // Son 30 dakikalık hareketleri 5 dakikada bir kontrol et
    setInterval(() => {
        loadRecentMovements();
    }, 300000); // 5 dakika = 300000 ms
    
    // İlk kontrolü hemen yap
    loadLastMovement();
    loadRecentMovements();
}

function stopPolling() {
    if (pollingInterval) {
        clearInterval(pollingInterval);
        pollingInterval = null;
    }
}

function formatTarih(tarih) {
    if (!tarih) return '-';
    try {
        const date = new Date(tarih);
        if (isNaN(date.getTime())) return '-';
        return date.toLocaleString('tr-TR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        });
    } catch (e) {
        console.error("Tarih formatlanırken hata:", e);
        return '-';
    }
}

function loadLastMovement() {
    console.log("Fetching last movement...");
    
    fetch('/pts/plaka-hareket/last-movement')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Received last movement data:", data);
            if (data) {
                // Veri değişikliğini kontrol et
                const isNewData = !lastMovementData || 
                    lastMovementData.plaka !== data.plaka ||
                    lastMovementData.sonGecisTarih !== data.sonGecisTarih ||
                    lastMovementData.hareketTipi !== data.hareketTipi ||
                    lastMovementData.kameraNo !== data.kameraNo;

                // Format dates
                const sonGecisTarih = formatTarih(data.sonGecisTarih);
                
                // Update UI
                document.getElementById('lastMovementPlaka').textContent = data.plaka || '-';
                document.getElementById('lastMovementGecisTarih').textContent = sonGecisTarih;
                document.getElementById('lastMovementHareketTipi').textContent = data.hareketTipi;
                document.getElementById('lastMovementKameraNo').textContent = data.kameraNo;

                // Sadece yeni veri varsa animasyonu tetikle
                if (isNewData) {
                    console.log('Yeni veri tespit edildi, animasyon tetikleniyor...');
                    const card = document.querySelector('.last-movement-card');
                    if (card) {
                        // Önce mevcut stili temizle
                        card.style.backgroundColor = 'transparent';
                        
                        // Force reflow
                        void card.offsetWidth;
                        
                        // Animasyonu başlat
                        card.style.backgroundColor = '#0d6efd';
                        setTimeout(() => {
                            card.style.backgroundColor = 'transparent';
                        }, 2000);
                    }
                }

                // Son veriyi güncelle
                lastMovementData = { ...data };
            } else {
                resetLastMovementUI();
                lastMovementData = null;
            }
        })
        .catch(error => {
            console.error("Error fetching last movement:", error);
            resetLastMovementUI();
            lastMovementData = null;
        });
}

function resetLastMovementUI() {
    document.getElementById('lastMovementPlaka').textContent = '-';
    document.getElementById('lastMovementGiris').textContent = '-';
    document.getElementById('lastMovementCikis').textContent = '-';
    document.getElementById('lastMovementDurum').textContent = '-';
}

function loadRecentMovements() {
    console.log("Fetching recent movements...");
    
    fetch('/pts/plaka-hareket/recent')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Received recent movements data:", data);
            updateRecentMovementsTable(data);
        })
        .catch(error => {
            console.error("Error fetching recent movements:", error);
            const tableBody = document.getElementById('recentMovementsTableBody');
            if (tableBody) {
                tableBody.innerHTML = `<tr><td colspan="4" class="text-center text-danger">Veri yüklenirken hata oluştu: ${error.message}</td></tr>`;
            }
        });
}

function updateRecentMovementsTable(data) {
    const tableBody = document.getElementById('recentMovementsTableBody');
    if (!tableBody) {
        console.error("Recent movements table body not found!");
        return;
    }

    if (!Array.isArray(data) || data.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" class="text-center">Son 30 dakikada geçiş yapan araç bulunmamaktadır.</td></tr>';
        return;
    }

    // Önceki verileri sakla
    const oldData = tableBody.innerHTML;

    // Yeni verileri ekle
    tableBody.innerHTML = data.map(hareket => `
        <tr class="movement-row">
            <td>${hareket.plaka || '-'}</td>
            <td>${formatTarih(hareket.sonGecisTarih)}</td>
            <td>${hareket.hareketTipi || '-'}</td>
            <td>${hareket.kameraNo || '-'}</td>
        </tr>
    `).join('');

    // Eğer veriler değiştiyse animasyonu uygula
    if (oldData !== tableBody.innerHTML) {
        const rows = tableBody.querySelectorAll('.movement-row');
        rows.forEach(row => {
            row.classList.remove('highlight-update');
            void row.offsetWidth; // Force reflow
            row.classList.add('highlight-update');
        });
    }
}

// Form initialization functions
function initializeAboneAraForm() {
    const aboneAraForm = document.getElementById('abone-ara-form');
    if (!aboneAraForm) {
        console.error('Abone ara formu bulunamadı!');
        return;
    }

    aboneAraForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const formData = new FormData(this);
        const searchData = {
            plaka: formData.get('plaka'),
            tcKimlikNo: formData.get('tcKimlikNo'),
            adSoyad: formData.get('adSoyad')
        };
        
        console.log('Arama verileri:', searchData);
        
        fetch('/pts/abone/ara', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(searchData)
        })
        .then(response => {
            console.log('Sunucu yanıtı:', response.status);
            return response.json();
        })
        .then(data => {
            console.log('Arama sonuçları:', data);
            
            const tableBody = document.querySelector('#searchResults tbody');
            if (!tableBody) {
                throw new Error('Sonuç tablosu bulunamadı!');
            }
            
            if (Array.isArray(data) && data.length > 0) {
                tableBody.innerHTML = data.map(abone => `
                    <tr>
                        <td>${abone.plaka || ''}</td>
                        <td>${abone.ad || ''} ${abone.soyad || ''}</td>
                        <td>${abone.tcKimlikNo || ''}</td>
                        <td>${abone.telefon || ''}</td>
                        <td>${abone.email || ''}</td>
                        <td>${abone.adres || ''}</td>
                        <td>
                            <button type="button" class="btn btn-primary btn-sm" 
                                onclick='showUpdateForm(${JSON.stringify(JSON.stringify(abone))})'>
                                <i class='bx bxs-edit'></i> Düzenle
                            </button>
                        </td>
                    </tr>
                `).join('');
            } else {
                tableBody.innerHTML = '<tr><td colspan="7" class="text-center">Kayıt bulunamadı</td></tr>';
            }
            
            // Güncelleme formunu gizle
            const updateForm = document.getElementById('abone-guncelle-form');
            if (updateForm) {
                updateForm.style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Hata:', error);
            alert('Arama sırasında bir hata oluştu: ' + error.message);
        });
    });
}

function showUpdateForm(aboneJson) {
    try {
        const abone = JSON.parse(aboneJson);
        const form = document.getElementById('abone-guncelle-form');
        if (!form) {
            console.error('Güncelleme formu bulunamadı!');
            return;
        }

        // Form alanlarını doldur
        form.querySelector('input[name="plaka"]').value = abone.plaka || '';
        form.querySelector('input[name="ad"]').value = abone.ad || '';
        form.querySelector('input[name="soyad"]').value = abone.soyad || '';
        form.querySelector('input[name="tcKimlikNo"]').value = abone.tcKimlikNo || '';
        form.querySelector('input[name="telefon"]').value = abone.telefon || '';
        form.querySelector('input[name="email"]').value = abone.email || '';
        form.querySelector('input[name="adres"]').value = abone.adres || '';

        // Orijinal plakayı sakla (hidden field olarak)
        let originalPlakaInput = form.querySelector('input[name="originalPlaka"]');
        if (!originalPlakaInput) {
            originalPlakaInput = document.createElement('input');
            originalPlakaInput.type = 'hidden';
            originalPlakaInput.name = 'originalPlaka';
            form.appendChild(originalPlakaInput);
        }
        originalPlakaInput.value = abone.plaka; // Orijinal plakayı sakla

        form.style.display = 'block';
        form.scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        console.error('Abone verisi işlenirken hata:', error);
        alert('Düzenleme formu açılırken bir hata oluştu!');
    }
}

function initializeAboneGuncelleForm() {
    const aboneGuncelleForm = document.getElementById('abone-guncelle-form');
    if (!aboneGuncelleForm) return;

    aboneGuncelleForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const formData = new FormData(this);
        const aboneData = {
            plaka: formData.get('plaka'),              // Yeni plaka
            originalPlaka: formData.get('originalPlaka'), // Eski plaka
            ad: formData.get('ad'),
            soyad: formData.get('soyad'),
            tcKimlikNo: formData.get('tcKimlikNo'),
            telefon: formData.get('telefon'),
            email: formData.get('email'),
            adres: formData.get('adres')
        };
        
        // CRITICAL: Ensure originalPlaka is not null/empty
        if (!aboneData.originalPlaka || aboneData.originalPlaka.trim() === '') {
            alert('Original plaka değeri eksik! Formu yeniden yükleyin.');
            return;
        }
        
        console.log('Güncellenecek veri:', aboneData);
        
        fetch('/pts/abone/guncelle', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(aboneData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.error) {
                throw new Error(data.error);
            }
            alert('Abone bilgileri başarıyla güncellendi!');
            this.style.display = 'none';
            document.getElementById('abone-ara-form').dispatchEvent(new Event('submit'));
        })
        .catch(error => {
            console.error('Hata:', error);
            alert('Güncelleme sırasında bir hata oluştu: ' + error.message);
        });
    });
}

// Function to populate the update form (you need to call this when editing)
function populateUpdateForm(aboneData) {
    const form = document.getElementById('abone-guncelle-form');
    if (form) {
        // Set all form fields
        form.querySelector('[name="plaka"]').value = aboneData.plaka;
        form.querySelector('[name="ad"]').value = aboneData.ad;
        form.querySelector('[name="soyad"]').value = aboneData.soyad;
        form.querySelector('[name="tcKimlikNo"]').value = aboneData.tcKimlikNo;
        form.querySelector('[name="telefon"]').value = aboneData.telefon;
        form.querySelector('[name="email"]').value = aboneData.email;
        form.querySelector('[name="adres"]').value = aboneData.adres;
        
        // CRITICAL: Set the original plate in a hidden field
        let originalPlakaField = form.querySelector('[name="originalPlaka"]');
        if (!originalPlakaField) {
            // Create hidden field if it doesn't exist
            originalPlakaField = document.createElement('input');
            originalPlakaField.type = 'hidden';
            originalPlakaField.name = 'originalPlaka';
            form.appendChild(originalPlakaField);
        }
        originalPlakaField.value = aboneData.plaka; // Use current plaka as original
        
        form.style.display = 'block';
    }
}

function initializeAboneSilmeButton() {
    const deleteAboneBtn = document.getElementById('deleteAboneBtn');
    if (!deleteAboneBtn) {
        console.error('Silme butonu bulunamadı!');
        return;
    }

    console.log('Silme butonu başarıyla bulundu ve initialize ediliyor...');

    deleteAboneBtn.addEventListener('click', function() {
        if (!confirm('Bu aboneyi silmek istediğinizden emin misiniz?')) {
            return;
        }
        
        const plaka = document.querySelector('#abone-guncelle-form input[name="plaka"]').value;
        if (!plaka) {
            alert('Plaka bilgisi bulunamadı!');
            return;
        }

        console.log('Silinecek plaka:', plaka);
        
        fetch(`/pts/abone/sil/${plaka}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json'
            }
        })
        .then(response => {
            console.log('Sunucu yanıtı:', response.status);
            if (!response.ok) {
                throw new Error(`Silme işlemi başarısız oldu! (${response.status})`);
            }
            // Try to parse as JSON, but don't fail if it's not JSON
            return response.text().then(text => {
                try {
                    return JSON.parse(text);
                } catch (e) {
                    return { success: 'Abone başarıyla silindi.' };
                }
            });
        })
        .then(data => {
            console.log('Sunucudan gelen yanıt:', data);
            if (data.error) {
                throw new Error(data.error);
            }
            alert(data.success || 'Abone başarıyla silindi!');
            document.getElementById('abone-guncelle-form').style.display = 'none';
            document.getElementById('abone-ara-form').dispatchEvent(new Event('submit'));
        })
        .catch(error => {
            console.error('Hata:', error);
            alert('Silme işlemi sırasında bir hata oluştu: ' + error.message);
        });
    });
}

function initializeAboneEkleForm() {
    const aboneForm = document.getElementById('abone-form');
    if (!aboneForm) {
        console.error('Abone formu bulunamadı! (ID: abone-form)');
        return;
    }

    console.log('Abone formu başarıyla bulundu ve initialize ediliyor...');

    aboneForm.addEventListener('submit', function(e) {
        e.preventDefault();
        console.log('Form submit eventi tetiklendi');
        
        const formData = new FormData(this);
        const aboneData = {
            plaka: (formData.get('plaka') || '').toUpperCase(),
            ad: formData.get('ad') || '',
            soyad: formData.get('soyad') || '',
            tcKimlikNo: formData.get('tcKimlikNo') || '',
            telefon: formData.get('telefon') || '',
            email: formData.get('email') || '',
            adres: formData.get('adres') || ''
        };
        
        console.log('Gönderilecek veri:', aboneData);
        
        // Validasyon
        if (!aboneData.plaka) {
            alert('Plaka alanı zorunludur!');
            return;
        }
        if (!aboneData.tcKimlikNo) {
            alert('TC Kimlik No alanı zorunludur!');
            return;
        }
        if (!aboneData.ad || !aboneData.soyad) {
            alert('Ad ve Soyad alanları zorunludur!');
            return;
        }
        
        fetch('/pts/abone/kaydet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(aboneData)
        })
        .then(response => {
            console.log('Sunucu yanıtı:', response.status);
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data.error || 'Abone eklenirken bir hata oluştu!');
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Sunucudan gelen yanıt:', data);
            if (data.error) {
                throw new Error(data.error);
            }
            alert(data.success || 'Abone başarıyla eklendi!');
            this.reset();
        })
        .catch(error => {
            console.error('Hata:', error);
            alert(error.message);
        });
    });
}

// Sayfa kapatılırken polling'i durdur
window.addEventListener('beforeunload', function() {
    stopPolling();
});

// Sekme değiştiğinde polling'i başlat/durdur
document.addEventListener('visibilitychange', function() {
    if (document.hidden) {
        stopPolling();
    } else {
        startPolling();
    }
});

// Handle URL hash changes
window.addEventListener('hashchange', function() {
    const hash = window.location.hash.substring(1);
    const link = document.querySelector(`.nav-link[data-section="${hash}"]`);
    if (link) {
        link.click();
    }
});