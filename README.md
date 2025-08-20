# P9 — Évaluation du risque de diabète (microservices)

## 🎯 Objectif
Fournir une solution en **microservices** permettant :
- de gérer les **patients** (création, consultation, mise à jour),
- de gérer les **notes médicales** rattachées aux patients,
- de **calculer** et **afficher** le **risque de diabète** d’un patient selon des règles métier définies.

## 🧱 Architecture attendue
> Noms de dossiers/services indicatifs — adaptez à votre arborescence réelle.

- **gateway-service** — Passerelle (ex. Spring Cloud Gateway) pour exposer une API unifiée.
- **patient-service** — Back de gestion des patients (BD relationnelle).
- **notes-service** — Back de gestion des notes (BD NoSQL, ex. MongoDB).
- **assessment-service** — Back de calcul du risque (stateless, interroge *patient* et *notes*).
- **front** — Interface web (framework libre, indépendant du back).

**Sécurité :** accès protégés (ex. Spring Security / Basic Auth).  
**Conteneurisation :** un `Dockerfile` par microservice + un `docker-compose.yml` à la racine.

## 🧪 Règles métier (évaluation du risque)
Niveaux possibles : `None`, `Borderline`, `In Danger`, `Early onset`.

- **None** : aucune note ne contient de **terme déclencheur**.
- **Borderline (≥30 ans)** : entre **2 et 5** déclencheurs.
- **In Danger** :
  - **<30 ans** : Homme → **≥3** déclencheurs ; Femme → **≥4** déclencheurs.
  - **≥30 ans** : **6 ou 7** déclencheurs.
- **Early onset** :
  - **<30 ans** : Homme → **≥5** ; Femme → **≥7**.
  - **≥30 ans** : **≥8** déclencheurs.

**Déclencheurs à détecter** dans les notes (casse/accents à gérer) :  
_Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeur/Fumeuse, Anormal, Cholestérol, Vertiges, Rechute, Réaction, Anticorps._

## 🗃️ Données de démo (4 cas de test)
Patients (Sprint 1) :

| Id | Nom           | Prénom | Naissance   | Sexe |
|----|---------------|--------|-------------|------|
| 1  | TestNone      | Test   | 1966-12-31  | F    |
| 2  | TestBorderline| Test   | 1945-06-24  | M    |
| 3  | TestInDanger  | Test   | 2004-06-18  | M    |
| 4  | TestEarlyOnset| Test   | 2002-06-28  | F    |

Notes associées (Sprint 2) contiennent des déclencheurs pour valider les niveaux.  
Risque **attendu** (Sprint 3) : `1 → None`, `2 → Borderline`, `3 → InDanger`, `4 → EarlyOnset`.

## 🚀 Démarrage

### Option A — Docker (recommandé)
1) Cloner le dépôt :
```bash
git clone https://github.com/Z093/P9
cd P9
```

2) Démarrer tous les services :
```bash
docker compose up --build
# ou: docker-compose up --build
```

3) Accéder à l’application :
- **Front** via le port exposé (voir `docker-compose.yml`).
- **API** via la **gateway** (voir `docker-compose.yml` pour les ports et paths).

> Astuce : si les BDs sont conteneurisées (ex. MongoDB), vérifiez les variables `MONGO_URI`/`SPRING_DATA_MONGODB_URI` côté **notes-service** et l’URL JDBC côté **patient-service**.

### Option B — Dev local (sans Docker)
- **Backs** : ouvrir chaque microservice dans l’IDE et exécuter (Maven/Gradle).  
- **Front** :
  ```bash
  cd front
  npm install
  npm run dev
  ```
- Adapter les `application.yml/properties` (ports, BDs, sécurité) et la config **gateway**.

## 🌐 API — exemples (à adapter à vos ports/routes)
> Remplacez `<host>`, `<port_gateway>` et, si nécessaire, ajoutez l’authentification (`-u <user>:<pass>`).

- **Lister les patients**  
  `GET http://<host>:<port_gateway>/patients`
- **Créer un patient**  
  `POST http://<host>:<port_gateway>/patients` (JSON)
- **Lister les notes d’un patient**  
  `GET http://<host>:<port_gateway>/notes?patientId=<id>`
- **Ajouter une note**  
  `POST http://<host>:<port_gateway>/notes` (JSON)
- **Obtenir l’évaluation de risque**  
  `GET http://<host>:<port_gateway>/assessments?patientId=<id>`
  
> Selon votre implémentation, les paths peuvent être `/api/...` ou être réécrits par la **gateway**.

## 🧰 Jeux de données & validation
1. **Importer** les 4 patients de démo (Sprint 1) dans **patient-service**.  
2. **Importer** les notes (Sprint 2) dans **notes-service**.  
3. Vérifier que **assessment-service** renvoie :
   - `patId=1 → None`
   - `patId=2 → Borderline`
   - `patId=3 → InDanger`
   - `patId=4 → EarlyOnset`

## 🔒 Sécurité
- Authentification simple (ex. **Spring Security** / Basic Auth).
- Mots de passe **hachés** côté configuration.
- Ne pas exposer les identifiants en clair dans le dépôt (utiliser variables d’environnement).

## ⚙️ Configuration (exemples)
- `patient-service` : JDBC URL, user/password (H2/MySQL/PostgreSQL).  
- `notes-service` : `spring.data.mongodb.uri=mongodb://...`.  
- `assessment-service` : URL des services **patient** et **notes** (via la gateway ou discovery).  
- `gateway-service` : routes → `/patients`, `/notes`, `/assessments` (ou vos paths).

> Les **ports** et les **variables d’env.** de référence sont définis dans `docker-compose.yml`.

## 🧩 Arborescence type
```
P9/
 ├─ gateway-service/
 ├─ patient-service/
 ├─ notes-service/
 ├─ assessment-service/
 ├─ front/
 ├─ docker-compose.yml
 └─ README.md
```

## 🧪 Tests & qualité
- Tests unitaires / d’intégration sur chaque microservice.
- Validation manuelle avec Postman (patients, notes, assessment).


## 🌿 Pistes Green Code
- **Pagination** et **filtrage** côté API pour limiter les charges.
- Logs en prod au niveau `WARN/ERROR`, pas de traces verbeuses.
- Images Docker **slim**, **layer caching**, multi-stage builds.
- **Time-outs** et **pooling** raisonnés côté HTTP clients.
- Réduire JS/CSS côté front, activer **caching**.

## 📦 Prérequis
- Git, Docker + Docker Compose
- Java 17+ (si build local), Node.js 18+ (front)

## 📄 Licence
Voir `LICENSE` si présent dans le dépôt.
