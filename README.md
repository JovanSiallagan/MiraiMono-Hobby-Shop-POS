# MIRAIMONO HOBBY SHOP - POINT OF SALES (POS)

---

## 1. IDENTITAS APLIKASI

Nama Aplikasi : MiraiMono Hobby Shop POS  
Versi         : 1.0 (Final Release)  
Platform      : Java Desktop (Swing GUI)  
Database      : MySQL  
Kategori      : Sistem Kasir / Manajemen Toko Hobi  

---

## 2. DESKRIPSI SINGKAT

MiraiMono POS adalah aplikasi kasir yang dirancang untuk toko hobi yang menjual barang-barang seperti Figure, Merchandise, Clothing, dan Gacha.

Aplikasi ini memisahkan hak akses antara Admin dan Kasir. Admin dapat mengelola stok barang (CRUD) dan melihat riwayat transaksi. Kasir dapat melakukan transaksi penjualan dengan fitur keranjang belanja, perhitungan diskon manual (persentase), dan perhitungan otomatis PPN 11% sesuai regulasi, serta mencetak struk belanja.

---

## 3. CARA MENJALANKAN

### A. Persiapan Database (PENTING)

1. Pastikan XAMPP terinstal dan aktifkan modul Apache & MySQL.
2. Buka browser ke alamat: http://localhost/phpmyadmin
3. Klik tab 'Databases', lalu buat database baru dengan nama: smartcashier
4. Klik nama database 'smartcashier' yang baru dibuat (pastikan posisinya sudah masuk ke database tersebut).
5. Klik tab 'Import', pilih file 'smartcashier.sql' yang disertakan dalam folder proyek.
6. Klik tombol 'Go' atau 'Kirim' di bagian bawah. Tunggu hingga muncul pesan sukses (centang hijau).


### B. Menjalankan Aplikasi

1. Buka project menggunakan IDE Java (Eclipse / NetBeans / IntelliJ).
2. Pastikan library 'mysql-connector-java' (JDBC Driver) sudah ditambahkan ke Build Path / Libraries project.
3. Buka file 'src/main/Main.java'.
4. Klik Run / Jalankan.


### C. Akun Login Default

- Role Admin  : Username = admin  | Password = admin  
- Role Kasir  : Username = kasir  | Password = kasir  

---

## 4. DAFTAR CLASS DAN FUNGSINYA

### [Package: main]

- Main.java  
  > Entry point (titik awal) aplikasi. Bertugas memanggil LoginFrame.


### [Package: module]

- DatabaseManager.java  
  > Mengelola koneksi ke MySQL dan menangani query (Login, Ambil Data Produk, Simpan Transaksi, Update Stok).

- User.java (Abstract)  
  > Induk class pengguna aplikasi.

- Admin.java & Cashier.java  
  > Turunan User untuk membedakan peran dan data sesi login.

- Product.java (Abstract)  
  > Class induk untuk semua barang dagangan. Berisi atribut umum seperti ID, Nama, Tipe, Harga, dan Stok.

- Figure.java, Merch.java, Clothing.java, Gacha.java  
  > Class turunan produk. Mengimplementasikan logika spesifik (polymorphism).

- Transaction.java  
  > Class logika bisnis ("otak" kasir). Menghitung Subtotal, menghitung nominal diskon berdasarkan input persen, menghitung PPN 11%, dan Total Akhir.


### [Package: view]

- LoginFrame.java  
  > Tampilan awal untuk autentikasi user ke database.

- AdminDashboardFrame.java  
  > Tampilan utama Admin untuk menambah, mengedit, menghapus produk, dan mengakses menu riwayat.

- CashierDashboardFrame.java  
  > Tampilan utama Kasir untuk transaksi. Memiliki fitur keranjang belanja, input diskon, kalkulasi pajak otomatis, dan cetak struk.

- HistoryFrame.java  
  > Menampilkan tabel laporan riwayat transaksi lengkap dengan rincian keuangan.

---

## 5. PENJELASAN KONSEP OOP

### A. Inheritance (Pewarisan)

**Class terkait:**  
- Product → Figure, Merch, Clothing, Gacha  
- User → Admin, Cashier  

**Penjelasan:**  
- Inheritance memungkinkan class turunan mewarisi atribut dan method dari class induknya. Dengan pendekatan ini, data umum seperti ID, nama, harga, dan role tidak perlu ditulis ulang di setiap class, sehingga kode menjadi lebih ringkas dan mudah dikembangkan.


### B. Encapsulation (Pembungkusan Data)

**Class terkait:**
- Seluruh class utama (Product, User, Transaction, dll.)

**Penjelasan:**
- Encapsulation membatasi akses langsung ke atribut penting dengan menjadikannya private. Akses data hanya dilakukan melalui method Getter dan Setter, sehingga perubahan nilai dapat dikontrol dan tidak merusak logika aplikasi.


### C. Polymorphism (Polimorfisme)

**Class terkait:**
- Product dan seluruh turunannya

**Penjelasan:**
- Polymorphism memungkinkan berbagai jenis produk diperlakukan sebagai satu tipe data, yaitu Product. Dengan cara ini, sistem transaksi dapat memproses semua jenis produk menggunakan satu alur logika tanpa perlakuan khusus.


### D. Abstraction (Abstraksi & Abstract Class)

**Class terkait:**
- Product (Abstract)
- User (Abstract)
  
**Penjelasan:**
- Abstraction mencegah pembuatan objek yang tidak memiliki jenis produk atau peran pengguna. Class abstract juga memaksa class turunan untuk mengimplementasikan logika yang dibutuhkan, sehingga struktur sistem tetap konsisten.  


---

## 6. Screenshot Hasil Aplikasi

### Login Page
![Screenshot 202](https://github.com/user-attachments/assets/f9ad3fea-9e86-418f-a5b1-bae406d46850)

### Admin Page
<img width="1358" height="803" alt="Admin Page" src="https://github.com/user-attachments/assets/a3e3572c-beb0-433b-b7f6-97ae98a3a22b" />

## History Page
<img width="1483" height="742" alt="History Page" src="https://github.com/user-attachments/assets/c64585de-61e1-4e7f-8a77-09760494e003" />

## Chasier Page
<img width="1483" height="928" alt="Chasier Page" src="https://github.com/user-attachments/assets/dc95509b-6cbc-46a5-8af5-e9c7bc0df7c0" />

