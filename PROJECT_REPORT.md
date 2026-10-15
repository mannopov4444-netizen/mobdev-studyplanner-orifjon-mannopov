# "StudyPlanner" Android Ilovasi Bo'yicha Texnik Loyiha Hisoboti (Project Report)

**Talaba / Dasturchi:** Orifjon Mannopov  
**O'quv Muassasasi:** Toshkent shahridagi Turin Politexnika Universiteti (TTPU)  
**Fan:** Mobile Development  
**Loyiha nomi:** StudyPlanner (Talabalar uchun shaxsiy vazifalar va o'quv rejalashtiruvchi mobil ilova)  
**GitHub Repozitoriyasi:** [mobdev-studyplanner-orifjon-mannopov](https://github.com/mannopov4444-netizen/mobdev-studyplanner-orifjon-mannopov.git)  
**Sinov Qurilmasi:** Redmi S2 (Android 8.1, API 27)  

---

## 1. Kirish va Loyihaning Maqsadi

Zamonaviy ta'lim jarayonida talabalar ko'plab fanlar, topshiriqlar, amaliy mashg'ulotlar va muddatli vazifalar (due dates) bilan ishlaydi. Ushbu jarayonlarni samarali boshqarish, vaqtni to'g'ri taqsimlash va o'z vaqtida eslatmalar olish uchun mo'ljallangan mobil dasturiy ta'minotga ehtiyoj yuqori.

**"StudyPlanner"** mobil ilovasining asosiy maqsadi — talabalarga o'quv topshiriqlarini qulay kiritish, fanlar bo'yicha guruhlash, muhimlik darajalariga (Priority) ko'ra saralash, qidirish hamda bildirishnomalar (Notifications) orqali vazifalarni o'z vaqtida bajarilishini ta'minlaydigan zamonaviy, ishonchli va estetik Android ilovani yaratishdan iborat.

---

## 2. Texnik Stek va Arxitektura

Loyiha modern Android dasturlash standartlariga to'liq mos keladigan ilg'or texnologiyalar va MVI/MVVM mos keluvchi qatlamli arxitektura (Clean Architecture tamoyillari) asosida qurilgan.

### 2.1 Texnologik Stek
- **Dasturlash tili:** Kotlin (v2.0.21)
- **UI Framework:** Jetpack Compose (Declarative UI)
- **Dizayn tizimi:** Material 3 Design Guidelines
- **Mahalliy ma'lumotlar bazasi:** Room Persistence Library (v2.6.1, KSP orqali)
- **Asinxronlik va Flow:** Kotlin Coroutines & StateFlow
- **Fon jarayonlari:** WorkManager (`work-runtime-ktx`)
- **Versiyani boshqarish:** Git, GitHub Flow, Semantic Versioning (v0.1.0 – v1.0.1)

### 2.2 Ilova Arxitekturasi
Loyiha quyidagi 3 ta asosiy qatlamga (Layers) bo'lingan:

```
uz.ttpu.studyplanner
├── data/              # Data Layer (Room DB, Entity, DAO, Repository)
│   ├── TaskEntity.kt
│   ├── TaskDao.kt
│   ├── AppDatabase.kt
│   └── TaskRepository.kt
├── ui/                # UI Layer (Compose Screens, ViewModels, Dialogs, Theme)
│   ├── TaskViewModel.kt
│   ├── TaskListScreen.kt
│   ├── TaskItem.kt
│   ├── AddTaskDialog.kt
│   ├── TaskDetailDialog.kt
│   ├── DeleteConfirmationDialog.kt
│   └── theme/
└── worker/            # Background Layer (WorkManager & Notifications)
    └── NotificationWorker.kt
```

---

## 3. Bosqichma-bosqich Bajarilgan Ishlar (Milestones)

Loyiha bosqichma-bosqich (Milestones) reja bo'yicha amalga oshirildi va har bir bosqich Semantic Versioning qoidalariga muvofiq tag'lar bilan belgilandi:

| Bosqich | Versiya (Tag) | Bajarilgan vazifalar va natijalar |
| :--- | :--- | :--- |
| **M0** | `v0.1.0` | Loyiha strukturasi yaratildi, Git repozitoriyasi, Gradle versiyalar katalogi (`libs.versions.toml`) hamda bazaviy Android sozlamalari o'rnatildi. |
| **M1** | `v0.2.0` | Data Layer yaratildi: `TaskEntity` (jadval), `TaskDao` (CRUD so'rovlar), `AppDatabase` (Room DB) hamda reaktiv `TaskRepository` joriy etildi. |
| **M2** | `v0.3.0` | UI Layer yaratildi: `TaskViewModel`, `TaskListScreen`, `TaskItem` va `AddTaskDialog` yordamida vazifalarni ro'yxatga chiqarish, qo'shish va holatini o'zgartirish amalga oshirildi. |
| **M3** | `v1.0.0` | Qidiruv va Saralash (Search & Priority Filter) funksiyalari va `NotificationWorker` orqali fon bildirishnomalari (Local Notifications) integratsiya qilindi. |
| **Fix/UI**| `v1.0.1` | Redmi S2 (Android 8.1) mosligi ta'minlandi, `ClassNotFoundException` tuzatildi, UI Material 3 standartlariga to'liq o'tkazildi hamda tasdiqlash va tafsilot dialoglari qo'shildi. |

---

## 4. Duch Kelingan Texnik Muammolar va Ularning Yechimlari

Dasturlash jarayonida yuzaga kelgan murakkab texnik xatoliklar va ularning ilmiy-amaliy yechimlari:

### 4.1 `ClassNotFoundException: Didn't find class "uz.ttpu.studyplanner.MainActivity"`
* **Muammo:** Fizik Redmi S2 (Android 8.1) qurilmasiga ilovani ishga tushirishda `MainActivity` topilmay ilova halokatga (Crash) uchradi.
* **Sababi:** Manifest faylida paket nomining noto'g'ri ko'rsatilganligi hamda eskirgan DEX va kesh fayllarining saqlanib qolgani.
* **Yechim:** `app/src/main/AndroidManifest.xml` faylida `package="uz.ttpu.studyplanner"` hamda `android:name="uz.ttpu.studyplanner.MainActivity"` to'liq ko'rsatildi. Eskirgan keshlar `./gradlew :app:clean :app:assembleDebug` orqali tozalanib, to'liq qayta komDerived qilindi.

### 4.2 Room `AppDatabase_Impl does not exist` va KSP Sozlamalari
* **Muammo:** Room bazasini ishga tushirishda `AppDatabase_Impl` klassi topilmadi.
* **Sababi:** Kotlin `.kt` fayllari uchun standart Java `annotationProcessor` ishlamaydi.
* **Yechim:** Google KSP (`com.google.devtools.ksp` v2.0.21-1.0.28) plagini yoqildi. `libs.versions.toml` da AGP versiyasi barqaror `8.8.2` ga o'tkazilib, `gradle.properties` ga `android.useAndroidX=true` sozlamasi qo'shildi.

---

## 5. UI/UX Dizaynni Modernizatsiya Qilish va Yangi Xususiyatlar

Ilovaning foydalanuvchi interfeysi (UI) Material 3 standartlari asosida to'liq qayta ko'rib chiqildi:

1. **Standartlashtirilgan Vazifa Kartalari (`TaskItem`):**
   - Kartalarning balandligini bir xil (Uniform Size) qilish uchun vazifa sarlavhasi `maxLines = 1` va tavsifi `maxLines = 2` bilan `TextOverflow.Ellipsis` orqali cheklandi.
   - Kartaning chap chetiga prioritet darajasini ko'rsatuvchi 5dp o'lchamdagi vertikal rangli chiziq qo'shildi.

2. **Material 3 Chip va Ranglar:**
   - Prioritet belgilari uchun rangli chiplar (Qizil - High, To'q sariq - Medium, Yashil - Low).
   - Fan atributi (Subject) uchun binafsha/lavanda rangli (`#EDE7F6`) to'ldirilgan chip yaratildi.

3. **Gorizontal Sana Formati:**
   - Eskirgan vertikal sana olib tashlanib, kartaning pastki o'ng burchagida kalendar belgisi bilan birga qulay `yyyy-MM-dd` formati o'rnatildi.

4. **Yangi Interaktiv Dialoglar:**
   - **`TaskDetailDialog`:** Kartaga bosilganda vazifaning to'liq sarlavhasi, tavsifi, fan, muddat va bajarilish holatini ko'rsatadi.
   - **`DeleteConfirmationDialog`:** Vazifani tasodifan o'chirib yubormaslik uchun "Are you sure you want to delete this task?" tasdiqlash oynasi.
   - **`AddTaskDialog`:** 28dp burchak radiusi, interaktiv `DatePickerDialog` va jonli validatsiyaga ega bo'lgan zamonaviy topshiriq qo'shish oynasi.

---

## 6. Sinovdan O'tkazish va Xulosa

### 6.1 Sinov Jarayoni
Ilova haqiqiy Redmi S2 (Android 8.1, Oreo, API 27) fizik qurilmasida USB Debugging (ADB) orqali har taraflama sinovdan o'tkazildi:
- **CRUD Amallari:** Vazifa yaratish, o'qish, holatini o'zgartirish va o'chirish tezkor va xatosiz ishladi.
- **Qidiruv va Saralash:** Sarlavha va fan bo'yicha qidiruv hamda prioritet bo'yicha saralash reaktiv ravishda (StateFlow) sekundning ulushlarida aks etdi.
- **Fon Jarayoni:** WorkManager orqali kiritilgan vazifalar uchun belgilangan vaqtda mahalliy bildirishnomalar (Notifications) chiqarildi.

### 6.2 Xulosa
"StudyPlanner" mobil ilovasi o'quv rejasini samarali tashkil etish uchun barcha texnik va arxitektura talablariga to'liq javob beradi. Loyihada modern Android development texnologiyalaridan unumli foydalanildi, xatoliklar bartaraf etildi hamda foydalanuvchi uchun zamonaviy va qulay interfeys taqdim etildi.

---
**Sana:** 12-Oktyabr, 2026 yil  
**Imzo:** Orifjon Mannopov
