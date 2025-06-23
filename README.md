# 🚀 Guide d'exécution - Projet Bibliothèque

## 📁 Structure finale du projet

```
library/
├── src/
│   ├── main/java/com/formation/library/
│   │   ├── LibraryApplication.java
│   │   ├── entity/
│   │   │   ├── Author.java
│   │   │   └── Book.java
│   │   ├── dto/                     # Data Transfer Objects (Records)
│   │   │   ├── AuthorDTO.java
│   │   │   ├── AuthorWithBooksDTO.java
│   │   │   ├── BookDTO.java
│   │   │   └── BookWithAuthorDTO.java
│   │   ├── mapper/                  # Entity-DTO Conversion
│   │   │   └── DTOMapper.java
│   │   ├── repository/
│   │   │   ├── AuthorRepository.java
│   │   │   └── BookRepository.java
│   │   ├── service/
│   │   │   ├── IAuthorService.java
│   │   │   ├── AuthorServiceImpl.java
│   │   │   ├── IBookService.java
│   │   │   └── BookServiceImpl.java
│   │   ├── controller/
│   │   │   ├── AuthorController.java
│   │   │   └── BookController.java
│   │   └── exception/
│   │       ├── AuthorNotFoundException.java
│   │       ├── BookNotFoundException.java
│   │       ├── BookAlreadyBorrowedException.java
│   │       ├── InvalidIsbnException.java
│   │       └── GlobalExceptionHandler.java
│   ├── main/resources/
│   │   ├── application.yml
│   │   └── application-test.yml
│   └── test/java/com/formation/library/
│       ├── entity/
│       │   ├── AuthorTest.java
│       │   └── BookTest.java
│       ├── service/
│       │   ├── AuthorServiceImplTest.java
│       │   └── BookServiceImplTest.java
│       ├── repository/
│       │   ├── AuthorRepositoryTest.java
│       │   └── BookRepositoryTest.java
│       ├── controller/
│       │   ├── AuthorControllerTest.java
│       │   └── BookControllerTest.java
│       ├── exception/
│       │   ├── ExceptionTest.java
│       │   └── GlobalExceptionHandlerTest.java
│       └── integration/
│           └── LibraryIntegrationTest.java
├── api-authors.http                 # Kulala test file for authors
├── api-books.http                   # Kulala test file for books
├── pom.xml
└── README.md
```

## 🛠️ Étapes de création du projet

### 1. Initialisation du projet

```bash
# Créer le projet avec Spring Initializr
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,h2,validation,actuator,devtools \
  -d groupId=com.formation \
  -d artifactId=library \
  -d name=library \
  -d description="Système de gestion de bibliothèque" \
  -d packageName=com.formation.library \
  -d javaVersion=21 \
  -d platformVersion=3.5.3 \
  -d type=maven-project \
  -d packaging=jar \
  -o library.zip

# Décompresser et ouvrir le projet
unzip library.zip
cd library
```

### 2. Structure des répertoires

```bash
# Créer la structure des packages
mkdir -p src/main/java/com/formation/library/{entity,dto,mapper,repository,service,controller,exception}
mkdir -p src/test/java/com/formation/library/{entity,repository,service,controller,exception,integration}
```

## 🚀 Commandes d'exécution

### Compilation et Build

```bash
# Compiler le projet
mvn clean compile

# Build complet avec tests
mvn clean package

# Build sans tests (si besoin)
mvn clean package -DskipTests
```

### Exécution des tests

#### Tests unitaires uniquement

```bash
# Tous les tests unitaires
mvn test -Dtest="**/*Test"

# Tests d'une classe spécifique
mvn test -Dtest="AuthorServiceImplTest"

# Tests d'un package spécifique
mvn test -Dtest="com.formation.library.service.*Test"
```

#### Tests par type

```bash
# Tests des entités
mvn test -Dtest="com.formation.library.entity.*Test"

# Tests des services
mvn test -Dtest="com.formation.library.service.*Test"

# Tests des repositories
mvn test -Dtest="com.formation.library.repository.*Test"

# Tests des contrôleurs
mvn test -Dtest="com.formation.library.controller.*Test"

# Tests d'intégration
mvn test -Dtest="com.formation.library.integration.*Test"
```

### Couverture de code avec JaCoCo

```bash
# Générer le rapport de couverture
mvn clean test jacoco:report

# Voir le rapport dans target/site/jacoco/index.html
open target/site/jacoco/index.html
```

### Lancement de l'application

```bash
# Démarrer l'application
mvn spring-boot:run

# Ou avec un profil spécifique
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Avec port spécifique
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Tests avec Maven Surefire

```bash
# Rapport détaillé des tests
mvn surefire-report:report

# Voir le rapport dans target/site/surefire-report.html
open target/report/surefire.html
```

## 🧪 Commandes de test avancées

### Tests en mode debug

```bash
# Lancer les tests en debug
mvn test -Dmaven.surefire.debug

# Puis connecter votre IDE sur le port 5005
```

### Tests avec profils

```bash
# Tests avec profil test
mvn test -Dspring.profiles.active=test

# Tests avec base de données spécifique
mvn test -Dspring.datasource.url=jdbc:h2:mem:testdb
```

### Tests parallèles

```bash
# Exécuter les tests en parallèle
mvn test -Dparallel=classes -DthreadCount=4
```

## 📊 Vérification de la couverture

### Objectifs de couverture

```bash
# Vérifier si couverture > 90%
mvn clean test jacoco:report jacoco:check

# Configuration dans pom.xml :
# <rules>
#   <rule>
#     <element>PACKAGE</element>
#     <limits>
#       <limit>
#         <counter>LINE</counter>
#         <value>COVEREDRATIO</value>
#         <minimum>0.90</minimum>
#       </limit>
#     </limits>
#   </rule>
# </rules>
```

## 🌐 Tests API avec curl

### Une fois l'application lancée (http://localhost:8080)

#### Tests des auteurs

```bash
# Créer un auteur
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Victor Hugo",
    "email": "victor.hugo@example.com",
    "birthDate": "1802-02-26",
    "biography": "Écrivain français"
  }'

# Récupérer tous les auteurs
curl http://localhost:8080/api/authors

# Récupérer un auteur par ID
curl http://localhost:8080/api/authors/1

# Rechercher des auteurs
curl "http://localhost:8080/api/authors/search?name=Victor"
```

#### Tests des livres

```bash
# Créer un livre (nécessite un auteur existant)
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Les Misérables",
    "isbn": "9782070408001",
    "publicationDate": "1862-01-01",
    "pages": 1500,
    "genre": "Roman",
    "author": {
      "id": 1,
      "name": "Victor Hugo",
      "email": "victor.hugo@example.com"
    }
  }'

# Emprunter un livre
curl -X PATCH http://localhost:8080/api/books/9782070408001/borrow

# Rendre un livre
curl -X PATCH http://localhost:8080/api/books/9782070408001/return

# Rechercher des livres par statut
curl http://localhost:8080/api/books/status/AVAILABLE
```

## 🎯 Plan de cours sur 3 jours

### Jour 1 : Tests unitaires purs

```bash
# Matin : Entities et Services
mvn test -Dtest="com.formation.library.entity.*Test"
mvn test -Dtest="com.formation.library.service.*Test"

# Après-midi : Exceptions et validation
mvn test -Dtest="com.formation.library.exception.*Test"
```

### Jour 2 : Tests de couches

```bash
# Matin : Repository tests (@DataJpaTest)
mvn test -Dtest="com.formation.library.repository.*Test"

# Après-midi : Controller tests (@WebMvcTest)
mvn test -Dtest="com.formation.library.controller.*Test"
```

### Jour 3 : Tests d'intégration

```bash
# Matin : Tests d'intégration
mvn test -Dtest="com.formation.library.integration.*Test"

# Après-midi : Couverture et optimisation
mvn clean test jacoco:report
```

## 📈 Métriques et rapports

### Génération de tous les rapports

```bash
# Rapport complet
mvn clean compile test jacoco:report surefire-report:report

# Ouvrir tous les rapports
open target/site/jacoco/index.html      # Couverture
open target/site/surefire-report.html   # Tests
```

### Métriques cibles pour le cours

- **Couverture de code** : > 95%
- **Nombre de tests** : > 80 tests
- **Temps d'exécution** : < 30 secondes
- **Tests par couche** :
  - Tests unitaires : ~60 tests
  - Tests de couches : ~15 tests
  - Tests d'intégration : ~8 tests

## 🛡️ Commandes de vérification qualité

### Vérification complète

```bash
# Pipeline de vérification complète
mvn clean compile test jacoco:report jacoco:check surefire-report:report

# Vérification que tous les tests passent
mvn test -Dfailsafe.rerunFailingTestsCount=0
```

### CheckStyle (optionnel)

```bash
# Ajouter au pom.xml et exécuter
mvn checkstyle:check
```

## 🚨 Dépannage

### Si les tests échouent

```bash
# Tests en mode verbose
mvn test -X

# Tests avec logs détaillés
mvn test -Dlogging.level.com.formation.library=DEBUG
```

### Si problème de base de données

```bash
# Nettoyer et recréer la DB
mvn clean test -Dspring.jpa.hibernate.ddl-auto=create-drop
```

### Console H2 pour debug

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:library
Username: sa
Password: password
```

## 📚 Ressources additionnelles

### Documentation générée

- **API REST** : http://localhost:8080/swagger-ui.html (si Swagger ajouté)
- **Actuator** : http://localhost:8080/actuator
- **Health Check** : http://localhost:8080/actuator/health

### Commandes Git pour versioning

```bash
git init
git add .
git commit -m "Initial commit: Library management system with comprehensive tests"
```
