# CraftGest

Application de gestion pour artisans et créateurs : produits, stock, ventes, dépôts-ventes et événements.

> 🚧 Projet en cours de développement.

## Stack technique

| Partie | Technologies |
| --- | --- |
| Back-end | Java 25, Spring Boot 4, Spring JDBC (SQL écrit à la main) |
| Base de données | PostgreSQL 18 (via Docker) |
| Migrations | Flyway |
| Front-end | À venir |

## Fonctionnalités prévues

- Gestion des produits (types, collections, univers, matières, couleurs, apprêts, photos)
- Suivi du stock et des ventes
- Dépôts-ventes chez des partenaires
- Événements et lieux (marchés, salons…)
- Comptes utilisateurs

## Structure du projet

```
CraftGest/
├── back/                 # API Spring Boot
│   └── src/main/
│       ├── java/fr/gaedic/craftgest/
│       │   ├── model/        # Classes métier
│       │   └── repository/   # Accès aux données (JDBC)
│       └── resources/
│           ├── application.yaml
│           └── db/migration/ # Scripts SQL Flyway
├── front/                # Front-end (à venir)
└── docker-compose.yml    # Base PostgreSQL
```

## Installation

### Prérequis

- Java 25
- Docker et Docker Compose

### 1. Configurer l'environnement

Copier le fichier d'exemple puis remplir les valeurs :

```bash
cp back/.env.example back/.env
```

### 2. Lancer la base de données

```bash
docker compose up -d
```

PostgreSQL est exposé sur le port `5433`.

### 3. Lancer le back-end

```bash
cd back
./mvnw spring-boot:run
```

Flyway crée les tables automatiquement au démarrage.

## Auteur

Gaédic — projet personnel.
