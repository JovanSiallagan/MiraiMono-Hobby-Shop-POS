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

MiraiMono POS adalah aplikasi kasir yang dirancang untuk toko hobi yang  
menjual barang-barang seperti Figure, Merchandise, Clothing, dan Gacha.

Aplikasi ini memisahkan hak akses antara Admin dan Kasir. Admin dapat  
mengelola stok barang (CRUD) dan melihat riwayat transaksi. Kasir dapat  
melakukan transaksi penjualan dengan fitur keranjang belanja, perhitungan  
diskon manual (persentase), dan perhitungan otomatis PPN 11% sesuai  
regulasi, serta mencetak struk belanja.

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
2. Pastikan library 'mysql-connector-java' (JDBC Driver) sudah  
   ditambahkan ke Build Path / Libraries project.
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
  > Mengelola koneksi ke MySQL dan menangani query (Login, Ambil Data Produk,  
  > Simpan Transaksi, Update Stok).

- User.java (Abstract)  
  > Induk class pengguna aplikasi.

- Admin.java & Cashier.java  
  > Turunan User untuk membedakan peran dan data sesi login.

- Product.java (Abstract)  
  > Class induk untuk semua barang dagangan. Berisi atribut umum seperti  
  > ID, Nama, Tipe, Harga, dan Stok.

- Figure.java, Merch.java, Clothing.java, Gacha.java  
  > Class turunan produk. Mengimplementasikan logika spesifik (polymorphism).

- Transaction.java  
  > Class logika bisnis ("otak" kasir). Menghitung Subtotal, menghitung nominal  
  > Diskon berdasarkan input persen, menghitung PPN 11%, dan Total Akhir.


### [Package: view]

- LoginFrame.java  
  > Tampilan awal untuk autentikasi user ke database.

- AdminDashboardFrame.java  
  > Tampilan utama Admin untuk menambah, mengedit, menghapus produk,  
  > dan mengakses menu riwayat.

- CashierDashboardFrame.java  
  > Tampilan utama Kasir untuk transaksi. Memiliki fitur keranjang belanja,  
  > input diskon, kalkulasi pajak otomatis, dan cetak struk.

- HistoryFrame.java  
  > Menampilkan tabel laporan riwayat transaksi lengkap dengan rincian keuangan.

---

## 5. PENJELASAN KONSEP OOP

### A. Inheritance (Pewarisan)

Aplikasi menerapkan pewarisan dimana class 'Figure', 'Merch', 'Clothing',  
dan 'Gacha' mewarisi atribut (ID, Nama, Harga) dari class induk 'Product'.  
Begitu juga dengan 'Admin' dan 'Cashier' yang mewarisi class 'User'.  
Hal ini meminimalkan duplikasi kode (DRY Principle).


### B. Encapsulation (Pembungkusan Data)

Seluruh atribut vital (seperti harga barang, password user, persentase diskon)  
diset sebagai 'private'. Akses dari luar class dibatasi hanya melalui  
method 'Getter' dan 'Setter' (Public API). Ini mencegah perubahan nilai  
variabel secara sembarangan yang bisa merusak logika perhitungan.


### C. Polymorphism (Polimorfisme)

- Dynamic Binding: Dalam keranjang belanja, sistem memperlakukan berbagai  
  objek berbeda (Figure, Gacha, Kaos) sebagai satu tipe data yaitu 'Product'.

- Method Overriding: Method 'hitungDiskon()' dideklarasikan di induk,  
  tapi implementasi pastinya ditentukan oleh masing-masing anak class  
  (sub-class).


### D. Abstraction (Abstraksi & Abstract Class)

- Class 'Product' dan 'User' didefinisikan sebagai 'Abstract Class'.  
  Artinya, sistem mencegah pembuatan objek 'Produk Kosong' atau  
  'User Tanpa Role'.

- Penggunaan 'Abstract Method' pada hitungDiskon() memaksa setiap pengembang  
  yang ingin menambah jenis barang baru untuk menentukan aturan diskonnya,  
  menjaga konsistensi logika bisnis.


### E. Generics (Type Safety)

Aplikasi menggunakan fitur Java Generics pada struktur data keranjang belanja:  
`ArrayList<Product> cart`.

Hal ini menjamin Type Safety, di mana keranjang belanja hanya bisa diisi  
oleh objek turunan Product, mencegah masuknya objek lain (seperti User)  
ke dalam perhitungan transaksi.


### F. Penerapan SOLID Principles (Selected)

1. Single Responsibility Principle (SRP):  
   Setiap class memiliki satu tanggung jawab spesifik.  
   - 'DatabaseManager' hanya mengurus koneksi SQL.  
   - 'Transaction' hanya mengurus logika matematika (Pajak/Diskon).  
   - 'Frame' (View) hanya mengurus tampilan GUI.  
   Kode menjadi lebih rapi dan mudah dirawat (maintainable).

2. Open/Closed Principle (OCP):  
   Sistem terbuka untuk penambahan tapi tertutup untuk modifikasi.  
   Contoh: Saat menambahkan fitur 'Gacha', kita hanya perlu membuat class  
   baru 'Gacha.java' yang mewarisi 'Product', tanpa perlu mengacak-acak  
   logika perhitungan inti di class 'Transaction'.

3. Liskov Substitution Principle (LSP):  
   Objek 'Product' dalam sistem dapat digantikan oleh turunannya (Figure,  
   Merch) tanpa merusak jalannya aplikasi. Transaksi tetap berjalan normal  
   apapun jenis barang yang dimasukkan ke keranjang.

---

## 6. Screenshot Hasil Aplikasi

### Login Page
![Screenshot 202](https://github.com/user-attachments/assets/f9ad3fea-9e86-418f-a5b1-bae406d46850)

### Admin Page
<img width="1358" height="803" alt="Admin Page" src="https://github.com/user-attachments/assets/a3e3572c-beb0-433b-b7f6-97ae98a3a22b" />

## History Page
<img width="1483" height="742" alt="History Page" src="https://github.com/user-attachments/assets/c64585de-61e1-4e7f-8a77-09760494e003" />

##Chasier Page
<img width="1483" height="928" alt="Chasier Page" src="https://github.com/user-attachments/assets/dc95509b-6cbc-46a5-8af5-e9c7bc0df7c0" />

