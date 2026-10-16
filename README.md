# Rickothèque

Une application Android (Kotlin + Jetpack Compose) qui explore l'univers de
*Rick and Morty* via la [Rick and Morty API](https://rickandmortyapi.com/),
en consommant les **mêmes données via deux transports différents** : une API
**REST** (Ktor) et une API **GraphQL** (Apollo). L'utilisateur choisit la
source de données au lancement, et toute l'app bascule de l'une à l'autre
sans changer une ligne de code métier.

Ce projet est avant tout un **bac à sable pédagogique** : il sert à comparer
concrètement REST et GraphQL sur un même cas d'usage, dans une architecture
multi-module moderne.

## ✨ Fonctionnalités

- Écran de **choix de la source de données** (REST ou GraphQL) au démarrage.
- Liste et détail des **personnages**.
- Liste et détail des **épisodes**.
- Liste et détail des **lieux**.
- Chargement d'images distantes (Coil).

## 🏗️ Architecture

Le projet est organisé en modules Gradle suivant une architecture multi-module
par feature, chaque feature étant elle-même découpée en couches
`domain` / `data` / `ui` :

```
app/                          # Module d'application, assemble toutes les features
core/
  datasource/                 # Sélecteur de source de données (REST/GraphQL), partagé par toutes les features
  network/                    # Client réseau REST (Ktor)
  network/graphql/            # Client réseau GraphQL (Apollo) + schéma et requêtes .graphql
feature/
  character/
    domain/                   # Modèles et contrats métier
    data/                     # Implémentations REST et GraphQL du repository
    ui/                       # Écrans Compose + ViewModels
  episode/
    domain/ data/ ui/
  location/
    domain/ data/ ui/
  splash/
    ui/                       # Écran de démarrage
  choice/
    ui/                       # Écran de choix REST / GraphQL
```

Pour chaque feature, le `Repository` de domaine est implémenté deux fois
(une version REST, une version GraphQL) et un "router" choisit l'implémentation
à utiliser à l'exécution, en fonction du choix fait sur l'écran d'accueil
(`DataSourceSelector` dans `core/datasource`).

Un guide détaillé de l'intégration GraphQL (Apollo) se trouve dans
[`core/network/graphql/README.md`](core/network/graphql/README.md).

## 🛠️ Stack technique

Le projet vise les dernières versions stables de chaque dépendance :

- **Kotlin** 2.4 / **Jetpack Compose** (Compose UI 1.12, Material3 1.4)
- **Koin** 4.2 (DSL classique `org.koin.core.module.dsl.viewModel`,
  `koinInject()` / `koinViewModel()` côté Compose)
- **Ktor** 3.6 pour les appels REST
- **Apollo Kotlin** 5.2 pour les appels GraphQL
- **Coil** 3.6 (`coil3`) pour le chargement d'images, avec le moteur réseau
  Ktor (`coil-network-ktor3` + moteur `ktor-client-cio`), cohérent avec le
  reste de la stack réseau du projet
- **Navigation Compose** 2.10
- **Gradle** 9.6 avec convention plugins (`build-logic/`) et accesseurs de
  projet typés (`projects.*`)

Toutes les versions sont centralisées dans
[`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## 🚀 Lancer le projet

### Prérequis

- Android Studio (dernière version stable recommandée)
- JDK 17+

### Compiler et lancer

```bash
./gradlew assembleDebug
```

Ou ouvrez simplement le projet dans Android Studio et lancez le module `app`
sur un émulateur ou un appareil (minSdk 24, targetSdk 36, compileSdk 37).

### Tests

```bash
./gradlew test
```

## 📡 API utilisée

- REST : `https://rickandmortyapi.com/api`
- GraphQL : `https://rickandmortyapi.com/graphql`

## 📄 Licence

Voir le fichier [LICENSE](LICENSE).
