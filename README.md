# LaptopCare

LaptopCare adalah aplikasi desktop JavaFX untuk manajemen servis laptop UMKM.
Project mengikuti pola MVC dengan alur:

`FXML -> Controller -> Service -> Repository -> XML`

## Modul Utama

- Customer: CRUD pelanggan dan TableView.
- Laptop: CRUD laptop, relasi ke customer, dan TableView.
- Service: CRUD servis, Queue antrean servis, Stack riwayat status, dan TableView.
- Dashboard: ringkasan data, Bar Chart servis bulanan, Pie Chart status servis, dan tabel recent service.

## Struktur Data XML

Data aplikasi disimpan di folder `xml` pada root project:

- `xml/customer.xml`
- `xml/laptop.xml`
- `xml/service.xml`

Jalankan aplikasi dari root project `LaptopCare` agar repository membaca folder XML yang benar.

## Cara Menjalankan

1. Buka folder project ini di VS Code.
2. Pastikan JavaFX tersedia. Project ini dibuat untuk Java/JavaFX 9 sesuai FXML bawaan.
3. Jalankan class `application.Main`.

Jika menjalankan lewat terminal, gunakan working directory root project ini.
