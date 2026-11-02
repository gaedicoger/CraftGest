# CraftGest

Application de gestion pour artisan·es et créateur·ices : produits, stock, ventes, dépôts-ventes et événements.

> 🚧 Projet en cours de développement.

## Stack technique

| Partie | Technologies |
| --- | --- |
| Back-end | Java 25, Spring Boot 4, Spring JDBC (SQL écrit à la main) |
| Base de données | PostgreSQL 18 (via Docker) |
| Migrations | Flyway |
| Front-end | À venir |

## Fonctionnalités prévues en V1

**Comptes et sécurité**
- Connexion par email et mot de passe (US1)
- Deux rôles, `ADMIN` et `ARTISAN` : chaque utilisateur n'accède qu'à ses propres données (US2)
- Création des comptes par l'admin, pas d'inscription libre (US3)

**Catalogue**
- Listes personnelles : types, collections, univers, matières, couleurs, apprêts (US5)
- Création d'un produit, et duplication d'une fiche existante (US7)
- Modification d'un produit (US8)

**Stock**
- Ajout de pièces au stock, « J'en ai refait » (US10)
- Correction manuelle du stock : casse, perte, inventaire (US11)

**Lieux et événements**
- Lieux de vente avec remise et commission par défaut (US13)
- Événements : marchés, salons, dépôts-ventes, ventes directes (US14)

**Ventes et dépôts-ventes**
- Saisie de toutes les ventes d'un événement en une fois (US15)
- Dépôt de pièces dans une boutique (US17)
- Clôture d'un dépôt-vente et retour des invendus en stock (US18)

## Structure du projet

```
CraftGest/
├── .github/workflows/    # Intégration continue (GitHub Actions)
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

Sous Windows, utiliser `.\mvnw.cmd` à la place de `./mvnw`.

Flyway crée les tables automatiquement au démarrage.

## Commandes pratiques

Les commandes Maven se lancent depuis le dossier `back` (sous Windows : `.\mvnw.cmd` au lieu de `./mvnw`).

| Commande | Rôle |
| --- | --- |
| `docker compose up -d` | Démarrer la base PostgreSQL (depuis la racine) |
| `docker compose down` | Arrêter la base, sans perdre les données |
| `./mvnw spring-boot:run` | Lancer le back-end |
| `./mvnw verify` | Compiler et lancer tous les tests (comme la CI) |
| `./mvnw spotless:apply` | Reformater tout le code Java |
| `./mvnw spotless:check` | Vérifier le format sans rien modifier (comme la CI) |
| `./mvnw javadoc:javadoc` | Générer la Javadoc dans `target/reports/apidocs/` |
| `start target\reports\apidocs\index.html` | Ouvrir la Javadoc dans le navigateur (Windows) |
| `git switch develop && git pull` | Se mettre à jour avant de commencer |
| `git switch -c feature/US<n>-<nom>` | Créer la branche d'une user story depuis `develop` |

## Documentation

| Documentation | Rôle | Où | État |
| --- | --- | --- | --- |
| README | Présenter le projet et l'installer | `README.md` | ✅ |
| Javadoc | Le « dictionnaire » du code back : classes et méthodes à règle métier | Générée avec `./mvnw javadoc:javadoc` dans `back/target/reports/apidocs/` | ✅ en local, publication sur GitHub Pages à venir |
| Guides | Architecture, décisions techniques, schémas (MCD, MLD, maquettes) | `docs/` | 🔜 |
| Doc API | Les routes de l'API (springdoc / OpenAPI), générée par la CI | `docs/api/` | 🔜 avec le premier controller (US1) |
| TSDoc | Le « dictionnaire » du code front, lu dans l'éditeur | Commentaires dans `front/` | 🔜 avec le front |

## Conventions de nommage

**Java**
- Classes en `PascalCase`, méthodes et variables en `camelCase`, packages en minuscules.
- Une classe par couche, avec un suffixe : `ProductRepository`, `ProductService`, `ProductController`.
- Méthodes des repositories : `findById`, `findByUserId`, `save`, `update`, `deleteById`.

**Base de données**
- Tables et colonnes en `snake_case`, tables au pluriel : `products`, `product_colors`, `user_id`.
- Migrations Flyway : `V<numéro>__<description>.sql` (deux tirets bas), par exemple `V2__add_admin_user.sql`. Une migration déjà appliquée ne se modifie jamais : on en crée une nouvelle.

**Format du code**
- Le code Java est formaté par Spotless (style Palantir, 4 espaces). Avant de commiter : `./mvnw spotless:apply`.

## Participer au projet

### Branches

| Branche | Rôle |
| --- | --- |
| `main` | Version stable. Protégée : on n'y pousse jamais directement, uniquement par pull request depuis `develop`. |
| `develop` | Branche de travail, où les fonctionnalités terminées sont regroupées. |
| `feature/US<n>-<nom>` | Une branche par user story, créée depuis `develop`. Exemple : `feature/US1-connexion`. |
| `fix/<nom>` | Correction d'un bug, créée depuis `develop`. |

### Commits

Format : `Type: description courte`, par exemple `Feat: ajout du repo produit`.

| Type | Usage |
| --- | --- |
| `Feat` | Nouvelle fonctionnalité |
| `Fix` | Correction de bug |
| `Style` | Formatage seul, sans changement de comportement |
| `Docs` | Documentation |
| `CI` | Intégration continue |
| `Build` | Dépendances, `pom.xml` |

Le formatage se fait dans un commit à part (`Style:`), pour ne pas noyer les vrais changements.

### Pull requests

1. Créer sa branche depuis `develop`.
2. Avant de pousser : `./mvnw spotless:apply`, puis `./mvnw verify`.
3. Ouvrir une pull request vers `develop`.
4. La CI doit être au vert. La pull request est relue avant d'être fusionnée.
5. Quand une fonctionnalité fonctionne de bout en bout, `develop` est fusionnée dans `main` par pull request.

## Auteur

Gaédic — projet personnel.
