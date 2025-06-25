---
theme: default
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Tests Spring Boot - Java
  
  Cours sur les tests unitaires et d'intégration avec Spring Boot
drawings:
  persist: false
defaults:
  foo: true
title: Tests avec Spring Boot Java
mdc: true
---

# Tests avec Spring Boot

**Formation complète sur les tests en Java**

Tests unitaires, d'intégration et fonctionnels

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Commencer <carbon:arrow-right class="inline"/>
  </span>
</div>

<div class="abs-br m-6 flex gap-2">
  <button @click="$slidev.nav.openInEditor()" title="Open in Editor" class="text-xl slidev-icon-btn opacity-50 !border-none !hover:text-white">
    <carbon:edit />
  </button>
  <a href="https://github.com/slidevjs/slidev" target="_blank" alt="GitHub"
    class="text-xl slidev-icon-btn opacity-50 !border-none !hover:text-white">
    <carbon-logo-github />
  </a>
</div>

---

# Sommaire

<Toc minDepth="1" maxDepth="2"></Toc>

---

# Pourquoi tester ?

## <¯ Objectifs principaux

- **Qualité** : Assurer la fiabilité du code
- **Sécurité** : Détecter les régressions
- **Documentation** : Les tests expliquent le comportement attendu
- **Refactoring** : Modifier sans crainte

## <× Types de tests

- **Tests unitaires** : Tester une unité de code isolée
- **Tests d'intégration** : Tester l'interaction entre composants
- **Tests fonctionnels** : Tester les scénarios utilisateur

---

# Configuration Maven

## POM.xml - Dépendances de test

```xml
<dependencies>
    <!-- Spring Boot Test inclut JUnit 5, Mockito, AssertJ -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Plugins Maven

```xml
<!-- Plugin Surefire pour exécuter les tests -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.3</version>
</plugin>

<!-- Plugin JaCoCo pour la couverture -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.13</version>
</plugin>
```

---

# Anatomie d'un test JUnit 5

## Structure de base

```java
@ExtendWith(MockitoExtension.class)  // Extension pour Mockito
class AuthorServiceImplTest {
    
    @Mock                             // Mock d'une dépendance
    private AuthorRepository repository;
    
    @InjectMocks                     // Injection du mock dans la classe testée
    private AuthorServiceImpl service;
    
    @BeforeEach                      // Exécuté avant chaque test
    void setUp() {
        // Initialisation des données de test
    }
    
    @Test                            // Méthode de test
    void shouldReturnAuthorWhenFound() {
        // Given - Arrange
        // When - Act  
        // Then - Assert
    }
}
```

---

# Pattern AAA (Arrange-Act-Assert)

## Exemple concret : Test unitaire d'entité

```java
@Test
void shouldCreateAuthorWithNameAndEmail() {
    // ARRANGE - Préparer les données
    String name = "John Doe";
    String email = "john@example.com";
    
    // ACT - Exécuter l'action
    Author author = new Author(name, email);
    
    // ASSERT - Vérifier le résultat
    assertThat(author.getName()).isEqualTo("John Doe");
    assertThat(author.getEmail()).isEqualTo("john@example.com");
    assertThat(author.getBooks()).isEmpty();
}
```

## Bonnes pratiques
- **Un seul concept** par test
- **Noms explicites** : `shouldReturnAuthorWhenValidId()`
- **Tests indépendants** : ordre d'exécution non garanti

---

# Tests d'entités

## Test du modèle Author

```java
class AuthorTest {
    private Author author;
    
    @BeforeEach
    void setup() {
        author = new Author("John Doe", "john@example.com");
    }
    
    @Test
    void shouldAddBookToAuthor() {
        // Given
        Book book = new Book("Test Book", "1234567890", author);
        
        // When
        author.addBook(book);
        
        // Then
        assertThat(author.getBooks()).hasSize(1);
        assertThat(author.getBooks()).contains(book);
        assertThat(book.getAuthor()).isEqualTo(author);
    }
}
```

---

# Tests d'entités (suite)

## Test des méthodes métier

```java
@Test
void shouldBorrowAvailableBook() {
    // Given
    Book book = new Book("Title", "1234567890", author);
    assertThat(book.isAvailable()).isTrue();
    
    // When
    book.borrow();
    
    // Then
    assertThat(book.getStatus()).isEqualTo(Book.BookStatus.BORROWED);
    assertThat(book.isAvailable()).isFalse();
}

@Test
void shouldNotBorrowUnavailableBook() {
    // Given
    Book book = new Book("Title", "1234567890", author);
    book.setStatus(Book.BookStatus.BORROWED);
    
    // When & Then
    assertThatThrownBy(() -> book.borrow())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Le livre n'est pas disponible pour l'emprunt");
}
```

---

# Assertions avec AssertJ

## Pourquoi AssertJ ?

- **Lisibilité** : API fluide et expressive
- **Messages d'erreur** clairs
- **Découverte** : auto-complétion IDE

## Exemples d'assertions

```java
// Assertions basiques
assertThat(author.getName()).isEqualTo("John Doe");
assertThat(author.getId()).isNotNull();
assertThat(books).isEmpty();

// Assertions sur collections
assertThat(authors)
    .hasSize(2)
    .contains(author1)
    .extracting(Author::getName)
    .containsExactly("John", "Jane");

// Assertions sur exceptions
assertThatThrownBy(() -> service.findById(-1L))
    .isInstanceOf(AuthorNotFoundException.class)
    .hasMessage("Auteur non trouvé avec l'ID: -1");
```

---

# Tests unitaires avec Mockito

## Concepts clés

- **Mock** : Objet factice qui simule le comportement
- **Stub** : Définir le comportement d'un mock
- **Verify** : Vérifier les interactions

## Test de service avec mock

```java
@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    
    @Mock
    private AuthorRepository authorRepository;
    
    @InjectMocks
    private AuthorServiceImpl authorService;
    
    @Test
    void shouldFindAuthorById() {
        // Given
        Author author = new Author("John", "john@test.com");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        
        // When
        Author result = authorService.findById(1L);
        
        // Then
        assertThat(result).isEqualTo(author);
        verify(authorRepository).findById(1L);
    }
}
```

---

# Mockito - Techniques avancées

## Gestion des exceptions

```java
@Test
void shouldThrowExceptionWhenAuthorNotFound() {
    // Given
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());
    
    // When & Then
    assertThatThrownBy(() -> authorService.findById(1L))
        .isInstanceOf(AuthorNotFoundException.class)
        .hasMessage("Auteur non trouvé avec l'ID: 1");
    
    verify(authorRepository).findById(1L);
    verifyNoMoreInteractions(authorRepository);
}
```

## Vérifications sans interaction

```java
@Test
void shouldThrowExceptionWhenSavingAuthorWithNullName() {
    // Given
    Author author = new Author();
    author.setName(null);
    
    // When & Then
    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class);
    
    // Aucune interaction avec le repository
    verifyNoInteractions(authorRepository);
}
```

---

# Tests d'intégration Repository

## @DataJpaTest

```java
@DataJpaTest                    // Configure uniquement la couche JPA
@ActiveProfiles("test")         // Profile de test
class AuthorRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;  // Gestion des entités de test
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @BeforeEach
    void setUp() {
        Author author = new Author("Samuel", "sam@test.fr", 
                                 LocalDate.of(1981, 7, 9), "Formateur");
        entityManager.persistAndFlush(author);  // Sauvegarde immédiate
    }
    
    @Test
    void shouldFindAuthorByEmail() {
        Optional<Author> author = authorRepository.findByEmail("sam@test.fr");
        
        assertThat(author).isPresent();
        assertThat(author.get().getName()).isEqualTo("Samuel");
    }
}
```

---

# Configuration de test

## application-test.yaml

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
```

## Avantages de H2 en mémoire
- **Rapidité** : Base de données en mémoire
- **Isolation** : Chaque test a sa propre base
- **Compatibilité** : Syntaxe proche de PostgreSQL/MySQL

---

# Tests de contrôleurs avec @WebMvcTest

## Configuration du test

```java
@WebMvcTest(AuthorController.class)  // Test uniquement le contrôleur
@DisplayName("Tests du contrôleur Author")
class AuthorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;           // Simulation des requêtes HTTP
    
    @MockitoBean                       // Mock Spring Boot
    private IAuthorService authorService;
    
    @MockitoBean
    private DTOMapper dtoMapper;
    
    @Autowired
    private ObjectMapper objectMapper;  // Sérialisation JSON
}
```

---

# Tests de contrôleurs (suite)

## Test d'une API REST

```java
@Test
void shouldCreateValidAuthor() throws Exception {
    // Given
    Author author = new Author("Victor Hugo", "victor@test.com");
    author.setId(1L);
    
    AuthorDTO authorDTO = new AuthorDTO(1L, "Victor Hugo", 
                                       "victor@test.com", null, null);
    
    when(authorService.save(any(Author.class))).thenReturn(author);
    when(dtoMapper.toAuthorDTO(author)).thenReturn(authorDTO);
    
    // When & Then
    mockMvc.perform(
        post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(author)))
        .andDo(print())                    // Affiche la requête/réponse
        .andExpect(status().isCreated())   // Code 201
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Victor Hugo"));
}
```

---

# Couverture de code avec JaCoCo

## Configuration Maven

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.13</version>
    <configuration>
        <excludes>
            <exclude>com/formation/library/mapper/**</exclude>
            <exclude>com/formation/library/exception/**</exclude>
            <exclude>com/formation/library/dto/**</exclude>
        </excludes>
    </configuration>
</plugin>
```

## Commandes utiles

```bash
# Exécuter les tests avec couverture
mvn clean test

# Générer le rapport de couverture
mvn jacoco:report

# Le rapport est généré dans target/site/jacoco/index.html
```

---

# Tests Spring Boot complets

## @SpringBootTest pour tests d'intégration

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LibraryApplicationTests {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Test
    void contextLoads() {
        // Vérifie que le contexte Spring se charge correctement
    }
    
    @Test
    void shouldCreateAndRetrieveAuthor() {
        // Test complet avec vraie base de données
        Author author = new Author("Test Author", "test@test.com");
        
        ResponseEntity<AuthorDTO> response = restTemplate.postForEntity(
            "/api/authors", author, AuthorDTO.class);
            
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(authorRepository.count()).isEqualTo(1);
    }
}
```

---

# Stratégies de test

## Pyramide des tests

```
        /\
       /  \     Tests E2E (Peu)
      /____\    - Interface utilisateur
     /      \   - Scénarios complets
    /        \  
   /__________\ Tests Intégration (Quelques-uns)
  /            \ - APIs, bases de données
 /              \- Composants ensemble  
/________________\
Tests Unitaires (Beaucoup)
- Logique métier
- Classes isolées
```

## Règles d'or
- **70% tests unitaires** : Rapides, isolés
- **20% tests d'intégration** : Composants ensemble
- **10% tests E2E** : Parcours utilisateur complets

---

# Bonnes pratiques

## Nommage des tests

```java
// L Mauvais
@Test
void test1() { }

//  Bon - Format: should[Behavior]When[Condition]
@Test
void shouldReturnAuthorWhenValidIdProvided() { }

@Test
void shouldThrowExceptionWhenAuthorNotFound() { }

@Test
void shouldCreateAuthorWithValidEmail() { }
```

## Organisation des tests

```java
class AuthorServiceTest {
    
    @Nested
    @DisplayName("Tests de création")
    class CreationTests {
        
        @Nested
        @DisplayName("Cas valides")
        class ValidCases { }
        
        @Nested  
        @DisplayName("Cas d'erreur")
        class ErrorCases { }
    }
}
```

---

# Données de test

## Utilisation de @BeforeEach

```java
class BookTest {
    private Author author;
    private Book book;
    
    @BeforeEach
    void setUp() {
        author = new Author("John Doe", "john@test.com");
        book = new Book("Test Title", "1234567890", author);
    }
    
    // Tests utilisent les mêmes données de base
}
```

## Factory pattern pour les tests

```java
class TestDataFactory {
    public static Author createAuthor() {
        return new Author("Default Author", "default@test.com");
    }
    
    public static Author createAuthorWithBooks() {
        Author author = createAuthor();
        author.addBook(new Book("Book 1", "1111111111", author));
        return author;
    }
}
```

---

# Gestion des exceptions

## Test des validations

```java
@Test
void shouldValidateAuthorEmail() {
    // Given
    Author author = new Author("John", "invalid-email");
    
    // When & Then
    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Format d'email invalide");
}

@Test
void shouldValidateBookIsbn() {
    // Given
    Book book = new Book("Title", "invalid-isbn", author);
    
    // When & Then
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    
    assertThat(violations)
        .hasSize(1)
        .extracting(ConstraintViolation::getMessage)
        .contains("Format ISBN invalide");
}
```

---

# Tests asynchrones et temporels

## Test avec @Timeout

```java
@Test
@Timeout(value = 5, unit = TimeUnit.SECONDS)
void shouldCompleteWithinTimeLimit() {
    // Test qui doit se terminer en moins de 5 secondes
    authorService.processLargeDataSet();
}
```

## Test avec LocalDate

```java
@Test
void shouldFindAuthorsByBirthDateBetween() {
    // Given
    LocalDate start = LocalDate.of(1980, 1, 1);
    LocalDate end = LocalDate.of(1990, 12, 31);
    
    Author author = new Author("John", "john@test.com");
    author.setBirthDate(LocalDate.of(1985, 6, 15));
    
    when(repository.findByBirthDateBetween(start, end))
        .thenReturn(List.of(author));
    
    // When
    List<Author> result = service.findByBirthDateBetween(start, end);
    
    // Then
    assertThat(result).hasSize(1).contains(author);
}
```

---

# Debugging des tests

## Affichage des détails avec MockMvc

```java
@Test
void shouldReturnValidationErrors() throws Exception {
    Author invalidAuthor = new Author("", "invalid-email");
    
    mockMvc.perform(
        post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidAuthor)))
        .andDo(print())  // = Affiche la requête complète
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").exists());
}
```

## Messages d'erreur personnalisés

```java
@Test
void shouldContainExpectedBooks() {
    List<Book> books = author.getBooks();
    
    assertThat(books)
        .as("L'auteur devrait avoir 2 livres")  // Message personnalisé
        .hasSize(2);
}
```

---

# Tests paramétrés

## @ParameterizedTest avec JUnit 5

```java
@ParameterizedTest
@ValueSource(strings = {"", "   ", "\t", "\n"})
void shouldRejectBlankNames(String blankName) {
    Author author = new Author(blankName, "test@test.com");
    
    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Le nom de l'auteur ne peut pas être vide");
}

@ParameterizedTest
@CsvSource({
    "john@test.com, true",
    "invalid-email, false", 
    "@test.com, false",
    "john@, false"
})
void shouldValidateEmailFormats(String email, boolean isValid) {
    Author author = new Author("John", email);
    
    if (isValid) {
        assertThatNoException().isThrownBy(() -> authorService.save(author));
    } else {
        assertThatThrownBy(() -> authorService.save(author))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
```

---

# Commandes Maven utiles

## Exécution des tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter une classe de test spécifique
mvn test -Dtest=AuthorServiceImplTest

# Exécuter une méthode de test spécifique
mvn test -Dtest=AuthorServiceImplTest#shouldFindAuthorById

# Ignorer les tests
mvn compile -DskipTests

# Tests en mode verbose
mvn test -X

# Tests avec profil spécifique
mvn test -Dspring.profiles.active=test
```

## Rapports et couverture

```bash
# Générer le rapport de couverture
mvn clean test jacoco:report

# Rapport HTML dans target/site/jacoco/index.html
```

---

# Anti-patterns à éviter

## L Tests fragiles

```java
// Mauvais : dépend de l'ordre d'exécution
@Test
void test1() {
    author.setName("John");
}

@Test  
void test2() {
    assertThat(author.getName()).isEqualTo("John"); // L Fragile
}
```

## L Tests trop complexes

```java
// Mauvais : teste trop de choses
@Test
void complexTest() {
    // 50 lignes de setup
    // Multiple assertions non liées
    // Logique métier dans le test
}
```

## L Mocks excessifs

```java
// Mauvais : trop de mocks
@Mock Repository repo1;
@Mock Repository repo2;  
@Mock Service service1;
@Mock Service service2;
// ... 10 autres mocks
```

---

# Récapitulatif

## <¯ Points clés à retenir

1. **Structure AAA** : Arrange, Act, Assert
2. **Tests indépendants** : Chaque test doit pouvoir s'exécuter seul
3. **Noms explicites** : Le nom du test doit expliquer ce qui est testé
4. **Une assertion par concept** : Tests focalisés
5. **Pyramide des tests** : Beaucoup d'unitaires, quelques intégrations

## =à Outils essentiels

- **JUnit 5** : Framework de test
- **Mockito** : Mocking et stubbing  
- **AssertJ** : Assertions expressives
- **@DataJpaTest** : Tests repository
- **@WebMvcTest** : Tests contrôleur
- **JaCoCo** : Couverture de code

---

# Questions ?

<div class="text-center text-6xl">
>
</div>

## Ressources utiles

- [Documentation Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Guide JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Assertions](https://assertj.github.io/doc/)

<div class="abs-br m-6">
  <span class="text-sm opacity-50">
    Merci pour votre attention !
  </span>
</div>