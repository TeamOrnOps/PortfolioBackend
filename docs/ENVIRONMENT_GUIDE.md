# Environment Configuration Guide

## Oversigt

Dette projekt bruger environment variabler til at konfigurere database og Spring Boot indstillinger. Der er tre forskellige environment filer til forskellige formål.

---

## 📁 Environment Filer

| Fil | Formål | Commit til Git? |
|-----|--------|-----------------|
| `.env.example` | Template til lokal udvikling | **JA** - Alle skal se denne |
| `.env` | Din faktiske lokale konfiguration | **NEJ** - I .gitignore |
| `.env.production` | Template til produktion | **JA** - Som dokumentation |

---

## 🚀 Quick Start

### Første gang setup:

```bash
# 1. Kopier template til .env
cp .env.example .env

# 2. (Valgfrit) Tilpas passwords hvis ønsket

# 3. Start applikationen
docker compose up
```

### Access services:
- **Frontend:** http://localhost/
- **Backend API:** http://localhost:8080/api/projects
- **Database:** localhost:3306 (kun fra host)

---

## 📋 Environment Variabler Forklaring

### Database Konfiguration

#### `MYSQL_ROOT_PASSWORD`
- **Hvad:** MySQL administrator (root) password
- **Bruges til:** Database administration og vedligeholdelse
- **Vigtigt:** Applikationen bruger IKKE root-brugeren
- **Dev værdi:** Kan være simpel (fx `local_dev_password`)
- **Prod værdi:** Skal være stærk og unik (20+ tegn)

#### `MYSQL_DATABASE`
- **Hvad:** Navnet på databasen der skal oprettes
- **Format:** KUN database navnet - IKKE JDBC URL!
- **Korrekt:** `alge_nord_db`
- **Forkert:** `jdbc:mysql://localhost:3306/alge_nord_db`
- **Note:** Docker Compose opretter automatisk databasen ved første start

#### `MYSQL_USER`
- **Hvad:** Application brugernavnet som Spring Boot forbinder med
- **Vigtigt:** Må IKKE være "root" - Docker kan ikke oprette root som MYSQL_USER
- **Dev værdi:** `ornops`
- **Prod værdi:** `algenord_prod` (eller lignende - forskellig fra dev)
- **Note:** Docker Compose opretter automatisk brugeren

#### `MYSQL_PASSWORD`
- **Hvad:** Password for MYSQL_USER (ikke root!)
- **Bruges af:** Spring Boot til database forbindelse
- **Dev værdi:** Kan være simpel (fx `local_dev_password`)
- **Prod værdi:** Skal være stærk og unik - FORSKELLIG fra MYSQL_ROOT_PASSWORD

### Spring Boot Konfiguration

#### `SPRING_PROFILES_ACTIVE`
- **Hvad:** Hvilken Spring profil der skal bruges
- **Værdier:**
  - `dev` → Lokal udvikling
  - `prod` → Produktion
- **Dev:** Loader testdata fra `dev-data.sql`
- **Prod:** Loader IKKE testdata

#### `SPRING_JPA_DDL_AUTO`
- **Hvad:** Hibernate's database schema strategi
- **Værdier:**

| Værdi | Adfærd | Hvornår |
|-------|--------|---------|
| `create-drop` | Drop database → Opret ny ved hver start | **Development** |
| `update` | Opdater schema automatisk (bevar data) | Staging/Test |
| `validate` | Valider kun - ingen ændringer | **Production** (sikrest) |

---

## 🔧 .env.example (Development Template)

**Placering:** `/Backend/.env.example`

**Formål:** Template som alle i teamet bruger til lokal udvikling

**Indhold:**

```env
# ============================================================================
# ALGENORD PORTFOLIO - ENVIRONMENT CONFIGURATION
# ============================================================================
# Dette er en template fil til lokale miljøvariabler.
# Kopier denne fil til .env og udfyld værdierne.
#
# VIGTIGT: 
# - .env filen er i .gitignore og må ALDRIG committes til Git
# - Alle i teamet bruger SAMME værdier til lokal udvikling
# - Produktions-credentials skal være unikke og stærke
# ============================================================================

# ============================================================================
# MYSQL DATABASE CONFIGURATION
# ============================================================================

# MySQL root administrator password
# Dette er superbruger passwordet til databasen
# Bruges kun til administration - ikke af applikationen
MYSQL_ROOT_PASSWORD=local_dev_password

# Database navn
# VIGTIGT: Skriv KUN database navnet, IKKE hele JDBC URL!
# Forkert: jdbc:mysql://localhost:3306/alge_nord_db
# Korrekt: alge_nord_db
MYSQL_DATABASE=alge_nord_db

# Application database bruger
# VIGTIGT: Må IKKE være "root" - Docker kan ikke oprette root som MYSQL_USER
# Dette er den bruger som Spring Boot applikationen forbinder med
MYSQL_USER=ornops

# Application database password
# Password for ovenstående MYSQL_USER
# I lokal udvikling kan dette være det samme som MYSQL_ROOT_PASSWORD
MYSQL_PASSWORD=local_dev_password

# NOTE: Docker Compose opretter automatisk brugeren og databasen 
# ved første opstart baseret på ovenstående værdier

# ============================================================================
# SPRING BOOT CONFIGURATION
# ============================================================================

# Spring profil
# dev  = Lokal udvikling (create-drop schema, loader testdata)
# prod = Produktion (update schema, ingen testdata)
SPRING_PROFILES_ACTIVE=dev

# Hibernate DDL strategi
# create-drop = Drop og genopret database ved hver opstart (til udvikling)
# update      = Opdater schema automatisk (til produktion)
# validate    = Valider kun at schema matcher entities (sikrest til produktion)
SPRING_JPA_DDL_AUTO=create-drop

# ============================================================================
# SETUP INSTRUKTIONER
# ============================================================================
# 
# 1. Kopier denne fil:
#    cp .env.example .env
#
# 2. (Valgfrit) Ændr passwords hvis ønsket
#    
# 3. Start applikationen:
#    docker compose up
#
# 4. Tilgå services:
#    - Frontend:    http://localhost/
#    - Backend API: http://localhost:8080/api/projects
#    - Database:    localhost:3306 (kun fra host)
#
# ============================================================================
```

---

## 🏭 .env.production (Production Template)

**Placering:** `/Backend/.env.production`

**Formål:** Template til produktion deployment (dokumentation)

**Indhold:**

```env
# ============================================================================
# ALGENORD PORTFOLIO - PRODUCTION ENVIRONMENT
# ============================================================================
# Dette er en template til PRODUKTION deployment på cloud server
# 
# SIKKERHED:
# - Generer STÆRKE, UNIKKE passwords (brug pwgen eller password manager)
# - Gem aldrig denne fil i Git
# - Brug forskellige passwords for hver server/miljø
# - Brug minimum 20 tegn lange passwords med mixed characters
# ============================================================================

# ============================================================================
# MYSQL DATABASE CONFIGURATION - PRODUCTION
# ============================================================================

# MySQL root administrator password
# VIGTIGT: Generer et STÆRKT, UNIKT password!
# Eksempel generering: pwgen 32 1
MYSQL_ROOT_PASSWORD=GENERER_STÆRKT_PASSWORD_HER

# Database navn
# Dette kan være det samme som i development
MYSQL_DATABASE=alge_nord_db

# Application database bruger
# ANBEFALING: Brug et andet brugernavn end i development
# Må IKKE være "root"
MYSQL_USER=algenord_prod

# Application database password
# VIGTIGT: Generer et STÆRKT, UNIKT password (forskelligt fra root!)
# Dette password bruges af Spring Boot til database adgang
MYSQL_PASSWORD=GENERER_ANDET_STÆRKT_PASSWORD_HER

# ============================================================================
# SPRING BOOT CONFIGURATION - PRODUCTION
# ============================================================================

# Spring profil - SKAL være prod i produktion
SPRING_PROFILES_ACTIVE=prod

# Hibernate DDL strategi for produktion
# validate = Sikrest - ingen automatiske schema ændringer (ANBEFALET)
# update   = Opdater schema automatisk (risikabelt i produktion)
# VIGTIGT: Brug ALDRIG create-drop i produktion!
SPRING_JPA_DDL_AUTO=validate

# ============================================================================
# DEPLOYMENT CHECKLIST
# ============================================================================
#
# FØR DEPLOYMENT:
# [ ] Alle passwords er genereret og er STÆRKE (20+ tegn)
# [ ] Passwords er FORSKELLIGE fra development
# [ ] SPRING_PROFILES_ACTIVE er sat til "prod"
# [ ] SPRING_JPA_DDL_AUTO er sat til "validate" eller "update"
# [ ] Database backup strategi er på plads
# [ ] Denne fil er IKKE committed til Git
# [ ] .env filen er sat med korrekte permissions (chmod 600 .env)
#
# EFTER DEPLOYMENT:
# [ ] Test at applikationen kan forbinde til database
# [ ] Verificer at ingen testdata er loaded
# [ ] Check at API endpoints virker korrekt
# [ ] Verificer at data persisterer mellem container restarts
#
# ============================================================================

# ============================================================================
# PASSWORD GENERATION GUIDE
# ============================================================================
#
# Metode 1: pwgen (Linux/Mac)
#   pwgen 32 1
#
# Metode 2: OpenSSL
#   openssl rand -base64 32
#
# Metode 3: Python
#   python3 -c "import secrets; print(secrets.token_urlsafe(32))"
#
# Metode 4: Online (brug kun pålidelige sources)
#   https://www.lastpass.com/features/password-generator
#
# ============================================================================

# ============================================================================
# EKSEMPEL PÅ GENEREREDE PASSWORDS (brug IKKE disse!)
# ============================================================================
# MYSQL_ROOT_PASSWORD=xK9mP2vN8qL5wR3jT7hF9nQ4zL8pM1sD2xY6wZ3vB5nM
# MYSQL_PASSWORD=yH3nK7mV5pQ9wR2xT6jL4nM8sF1zD0cP9xW5vY2nK8mL
# ============================================================================
```

---

## Sikkerhed & Best Practices

### Lokal Udvikling

**Tilladt:**

- Alle i teamet bruger samme passwords
- Simple passwords (fx `local_dev_password`)
- Passwords kan være i `.env.example`

**Hvorfor det er OK:**

- Kører kun lokalt på egen computer
- Database er ikke tilgængelig fra internettet
- Nemmere collaboration og onboarding
- Konsistent udviklings-setup

### Produktion

**Påkrævet:**

- Stærke, unikke passwords (minimum 20 tegn)
- Forskellige passwords for hver server
- Forskellige passwords end development
- Aldrig commit `.env` til Git
- Brug password manager eller secrets vault

**Aldrig:**

- Genbruge development passwords
- Commit production `.env` til Git
- Dele passwords i Slack/email
- Bruge simple passwords (fx `password123`)

---

## Almindelige Opgaver

### Skift mellem Dev og Prod lokalt

```bash
# Skift til dev mode
nano .env
# Ændr:
SPRING_PROFILES_ACTIVE=dev
SPRING_JPA_DDL_AUTO=create-drop

# Genstart
docker compose down -v
docker compose up

# Skift til prod mode (test produktion setup lokalt)
nano .env
# Ændr:
SPRING_PROFILES_ACTIVE=prod
SPRING_JPA_DDL_AUTO=validate

# Genstart
docker compose down -v
docker compose up
```

### Generer Sikre Passwords

```bash
# Metode 1: pwgen (anbefalet)
pwgen 32 1

# Metode 2: OpenSSL
openssl rand -base64 32

# Metode 3: Python
python3 -c "import secrets; print(secrets.token_urlsafe(32))"

# Generer 2 forskellige passwords på én gang
pwgen 32 2
```

### Reset Database Lokalt

```bash
# Stop containere og slet volumes (SLETTER AL DATA!)
docker compose down -v

# Start forfra med ren database
docker compose up
```

### Check Environment Variabler

```bash
# Se hvad backend containeren bruger
docker exec algenord-backend env | grep SPRING

# Se database credentials
docker exec algenord-backend env | grep MYSQL
```

---

## Troubleshooting

### "MYSQL_USER="root" cannot be used"

**Problem:** Du har sat `MYSQL_USER=root` i `.env`

**Løsning:** 
```bash
nano .env
# Ændr MYSQL_USER til noget andet (fx ornops)
docker compose down -v
docker compose up
```

### "Database ikke fundet" eller "Access denied"

**Problem:** Forkert database URL eller credentials

**Check:**
```bash
# Verificer .env indstillinger
cat .env

# Check at MYSQL_DATABASE IKKE er en JDBC URL
# ✅ Korrekt: MYSQL_DATABASE=alge_nord_db
# ❌ Forkert: MYSQL_DATABASE=jdbc:mysql://localhost:3306/alge_nord_db

# Check database logs
docker compose logs database
```

### Testdata loader ikke

**Problem:** Forkert profil eller DDL strategi

**Løsning:**
```bash
nano .env
# Sæt:
SPRING_PROFILES_ACTIVE=dev
SPRING_JPA_DDL_AUTO=create-drop

# Genstart med ren database
docker compose down -v
docker compose up
```

### Backend kan ikke forbinde til database

**Problem:** Database er ikke klar når backend starter

**Check:**
```bash
# Se om database er healthy
docker ps

# Se database startup logs
docker compose logs database

# Se backend connection errors
docker compose logs backend | grep -i "connection\|mysql"
```

**Løsning:** Docker Compose burde vente automatisk, men hvis ikke:
```bash
docker compose down
docker compose up
```

---

## Eksempler

### Eksempel: Ny Teammedlem Setup

```bash
# 1. Clone repository
git clone https://github.com/TeamOrnOps/PortfolioBackend.git
cd PortfolioBackend

# 2. Setup environment
cp .env.example .env

# 3. Start applikationen
docker compose up

# 4. Test API
curl http://localhost:8080/api/projects

# 5. Åbn frontend
open http://localhost/
```

### Eksempel: Production Deployment

```bash
# På serveren (efter Docker er installeret)
# 1. Clone eller upload projektet
git clone https://github.com/TeamOrnOps/PortfolioBackend.git
cd PortfolioBackend

# 2. Opret production .env
nano .env

# 3. Generer stærke passwords
pwgen 32 2

# 4. Indsæt production konfiguration (brug .env.production som template)

# 5. Sæt korrekte permissions
chmod 600 .env

# 6. Start med production compose fil
docker compose -f compose.prod.yaml up -d

# 7. Verificer
docker compose -f compose.prod.yaml ps
docker compose -f compose.prod.yaml logs
```

---

## Team Guidelines

### Hvad committes til Git?

| Fil | Git? | Hvorfor |
|-----|------|---------|
| `.env.example` | Ja | Template - ingen følsomme data |
| `.env.production` | Ja | Documentation/template - ingen faktiske passwords |
| `.env` | Nej | Indeholder faktiske credentials |
| `.gitignore` | Ja | Skal indeholde `.env` |

### Når du ændrer environment variabler:

1. Opdater `.env.example` med den nye variabel
2. Opdater denne dokumentation
3. Informer teamet i Slack/Discord
4. Opdater `.env.production` hvis relevant
5. Commit changes til Git

### Når du onboarder nyt teammedlem:

1. Send dem denne dokumentation
2. Bed dem køre `cp .env.example .env`
3. Verificer at deres setup virker
4. Svar på spørgsmål

---

