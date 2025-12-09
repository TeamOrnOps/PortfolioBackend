# GitHub Container Registry (GHCR) Setup

## Nuværende Opsætning

### To Separate Repositories
- **Backend:** `TeamOrnOps/PortfolioBackend` → Bygger `ghcr.io/teamornops/algenord-backend:latest`
- **Frontend:** `TeamOrnOps/PortfolioFrontend` → Skal bygge `ghcr.io/teamornops/algenord-frontend:latest`

### Hvorfor Separate Images?

| Fordel | Forklaring |
|--------|-----------|
| **Uafhængig deployment** | Backend og frontend kan deployes separat uden at påvirke hinanden |
| **Mindre images** | Hver service indeholder kun hvad den skal bruge |
| **Hurtigere builds** | Kun ændrede services genbygges |
| **Nemmere rollback** | Kan rulle tilbage til tidligere version af én service |
| **Microservices ready** | Følger moderne deployment praksis |

## Backend GHCR Workflow

**Lokation:** `.github/workflows/ghcr.yaml`

**Triggers:**
- PRs til `development` eller `main`: Bygger + tester (pusher IKKE)
- Push til `main`: Bygger + tester + pusher til GHCR

**Image navn:** `ghcr.io/teamornops/algenord-backend:latest`

**Hvorfor lowercase?** GHCR kræver at repository navne er lowercase (`teamornops`, ikke `TeamOrnOps`)

## Frontend GHCR Workflow (TODO)

Frontend repository skal have samme workflow struktur:

```yaml
# .github/workflows/ghcr.yaml i PortfolioFrontend
name: Build and Push Frontend to GHCR

on:
  push:
    branches: [main]
  pull_request:
    branches: [main, development]

# ... bygger og pusher ghcr.io/teamornops/algenord-frontend:latest
```

## Produktion: Hvad Skal Ændres?

### 1. Versioning Strategy

**Nuværende:** Bruger kun `:latest` tag

**Produktion:** Brug semantisk versioning

```yaml
# I workflow - tilføj version tags
- name: Set version
  id: version
  run: echo "tag=v$(date +%Y%m%d)-${{ github.sha:0:7 }}" >> $GITHUB_OUTPUT

- name: Build and tag
  run: |
    docker build -t ghcr.io/teamornops/algenord-backend:latest .
    docker tag ghcr.io/teamornops/algenord-backend:latest \
               ghcr.io/teamornops/algenord-backend:${{ steps.version.outputs.tag }}

- name: Push both tags
  run: |
    docker push ghcr.io/teamornops/algenord-backend:latest
    docker push ghcr.io/teamornops/algenord-backend:${{ steps.version.outputs.tag }}
```

**Resultat:** 
- `latest` = nyeste version
- `v20251209-a421cff` = specifik version til rollback

### 2. Environment-Specific Tags

**Tilføj staging miljø:**

```yaml
# Byg forskellige tags baseret på branch
- name: Tag for environment
  run: |
    if [ "${{ github.ref }}" = "refs/heads/main" ]; then
      docker tag ... :latest
      docker tag ... :production
    elif [ "${{ github.ref }}" = "refs/heads/development" ]; then
      docker tag ... :staging
    fi
```

### 3. Image Scanning (Sikkerhed)

**Tilføj security scanning før push:**

```yaml
- name: Scan image for vulnerabilities
  uses: aquasecurity/trivy-action@master
  with:
    image-ref: ghcr.io/teamornops/algenord-backend:latest
    format: 'sarif'
    output: 'trivy-results.sarif'
    severity: 'CRITICAL,HIGH'
    exit-code: '1'  # Fail hvis kritiske sårbarheder findes
```

### 4. Automatisk Cleanup

**Slet gamle images:**

```yaml
# Tilføj til repository settings eller separat workflow
# Behold kun de 10 seneste images af hver service
```

Via GitHub UI: 
- Gå til Package settings
- Sæt retention policy: "Keep 10 most recent versions"

### 5. Server Deployment (compose.prod.yaml)

**Opdater på server når nyt image er pushed:**

```yaml
# compose.prod.yaml på serveren
services:
  backend:
    image: ghcr.io/teamornops/algenord-backend:latest
    # Eller pin til specifik version:
    # image: ghcr.io/teamornops/algenord-backend:v20251209-a421cff
  
  frontend:
    image: ghcr.io/teamornops/algenord-frontend:latest
  
  database:
    image: mysql:8.0
```

**Deployment kommandoer på server:**

```bash
# Pull nyeste images
docker compose -f compose.prod.yaml pull

# Genstart services med nye images (zero-downtime)
docker compose -f compose.prod.yaml up -d --no-deps backend frontend

# Eller med downtime (simplere)
docker compose -f compose.prod.yaml down
docker compose -f compose.prod.yaml up -d
```

## Quick Reference

### Se Images på GHCR

https://github.com/orgs/TeamOrnOps/packages

### Pull Image Lokalt

```bash
docker pull ghcr.io/teamornops/algenord-backend:latest
docker pull ghcr.io/teamornops/algenord-frontend:latest
```

### Test Image Lokalt

```bash
# Start med GHCR images
docker compose -f compose.prod.yaml up
```

### Current Workflow Files

| Repository | File | Status |
|-----------|------|--------|
| PortfolioBackend | `.github/workflows/ghcr.yaml` | ✅ Implementeret |
| PortfolioFrontend | `.github/workflows/ghcr.yaml` | ⏳ TODO |

## Checkliste: Klar til Produktion

- [ ] Frontend GHCR workflow implementeret
- [ ] Versioning strategy implementeret (begge repos)
- [ ] Environment tags (staging/production)
- [ ] Security scanning (Trivy)
- [ ] Image retention policy sat (10 seneste)
- [ ] `compose.prod.yaml` med GHCR images
- [ ] Nginx reverse proxy konfigureret
- [ ] SSL certifikat (Let's Encrypt)
- [ ] Environment variabler i `.env.production`
- [ ] Database backup strategi
- [ ] Monitoring setup (logs, uptime)
- [ ] Rollback procedure dokumenteret

## Konklusion

**Nuværende setup:** Fungerer til development
- ✅ Automatisk build og test
- ✅ Push til GHCR ved merge til main
- ⚠️ Kun `:latest` tag

**Produktions-upgrades:**
- Versioning for rollback
- Security scanning
- Environment separation
- Retention policies
- Zero-downtime deployment

**Tidsestimat til production-ready:** 2-4 timer
