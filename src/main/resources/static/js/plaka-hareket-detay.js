// Sayfa yüklendiğinde çalışacak
document.addEventListener('DOMContentLoaded', function() {
    setupPlakaHareketDetayForm();
});

function setupPlakaHareketDetayForm() {
    // Form elementini bul
    const form = document.getElementById('plaka-hareket-detay-form');
    if (!form) return;

    // Varsayılan tarih aralığını ayarla
    setDefaultDates();

    // Form submit olayını engelle ve kendi fonksiyonumuzu çalıştır
    form.onsubmit = function(e) {
        e.preventDefault();
        e.stopPropagation();
        handleFormSubmit(this);
        return false;
    };
}

function setDefaultDates() {
    const endDate = new Date();
    const startDate = new Date(endDate);
    startDate.setDate(startDate.getDate() - 1);

    document.getElementById('startDate').value = formatDateTime(startDate);
    document.getElementById('endDate').value = formatDateTime(endDate);
}

function handleFormSubmit(form) {
    // Form verilerini al
    const plaka = document.getElementById('searchPlaka').value;
    let startDate = document.getElementById('startDate').value;
    let endDate = document.getElementById('endDate').value;

    // Tarihler boşsa son 24 saati kullan
    if (!startDate || !endDate) {
        const now = new Date();
        const yesterday = new Date(now);
        yesterday.setDate(yesterday.getDate() - 1);
        
        startDate = formatDateTime(yesterday);
        endDate = formatDateTime(now);
    }

    // Yükleniyor mesajını göster
    const tableBody = document.getElementById('plakaHareketDetayTableBody');
    tableBody.innerHTML = '<tr><td colspan="4" class="text-center">Yükleniyor...</td></tr>';

    // API'ye istek at
    const params = new URLSearchParams({
        startDate: startDate,
        endDate: endDate,
        plaka: plaka
    });

    fetch(`/pts/plaka-hareket/search?${params.toString()}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displayResults(data);
        })
        .catch(error => {
            console.error('Error:', error);
            tableBody.innerHTML = '<tr><td colspan="4" class="text-center text-danger">Hata oluştu. Lütfen tekrar deneyiniz.</td></tr>';
        });
}

function displayResults(hareketler) {
    const tableBody = document.getElementById('plakaHareketDetayTableBody');
    
    if (!hareketler || hareketler.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" class="text-center">Kayıt bulunamadı.</td></tr>';
        return;
    }

    tableBody.innerHTML = hareketler.map(hareket => `
        <tr>
            <td>${hareket.plaka || '-'}</td>
            <td>${formatDateTime(new Date(hareket.sonGecisTarih))}</td>
            <td>${hareket.hareketTipi || '-'}</td>
            <td>${hareket.kameraNo || '-'}</td>
        </tr>
    `).join('');
}

function formatDateTime(date) {
    const pad = (num) => String(num).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
} 