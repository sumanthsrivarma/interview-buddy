-- InterviewBuddy — Seed Questions
-- V2__seed_questions.sql
-- 30 questions: Java Core, Spring Boot, Design Patterns, SOLID, TDD
-- Topics spread across difficulty and experience ranges

-- ============================================================
-- JAVA CORE (8 questions)
-- ============================================================

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'String immutability in Java',
    'Why is String immutable in Java? What are the design reasons behind this decision?',
    'String is immutable because: (1) Security — Strings are used as parameters in network connections, file paths, and class loading. Mutability would create security holes. (2) String Pool — The JVM maintains a string pool to reuse literals; mutability would corrupt pooled values for other references. (3) Thread safety — Immutable objects are inherently thread-safe with no synchronisation cost. (4) Hashcode caching — String caches its hashcode on first call; mutability would break HashMap/HashSet behaviour.',
    'How does String pool work in JVM heap? | What is the difference between String literal and new String()? | How does intern() relate to immutability?',
    'JAVA_CORE', 'JAVA', 'EASY', 0, 4,
    ARRAY['string', 'immutability', 'jvm', 'string-pool'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'equals() vs == in Java',
    'Explain the difference between == and equals() in Java. When would you override equals()?',
    '== compares object references (memory addresses) for objects, and primitive values for primitives. equals() compares logical equality — what the object represents. By default, equals() in Object class behaves like ==. You override equals() when two objects with the same field values should be considered equal (e.g., two Employee objects with same ID). Always override hashCode() when you override equals() — the contract requires equal objects to have equal hash codes.',
    'What happens if you override equals() but not hashCode()? | How does this affect HashMap behaviour? | What is the equals/hashCode contract?',
    'JAVA_CORE', 'JAVA', 'EASY', 0, 3,
    ARRAY['equals', 'hashcode', 'object', 'collections'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Java Memory Model — Stack vs Heap',
    'Explain Java memory areas. What goes on the stack vs the heap? What is the Metaspace?',
    'Stack stores method call frames, local variables, and reference variables (not the objects themselves). Each thread has its own stack. Heap stores all object instances and is shared across threads — divided into Young Generation (Eden + Survivor spaces) and Old Generation. Metaspace (Java 8+, replaced PermGen) stores class metadata, static variables, and compiled code. String pool lives in the heap (moved from PermGen in Java 7). Stack memory is automatically reclaimed when a method returns; heap memory is managed by GC.',
    'What triggers a StackOverflowError vs OutOfMemoryError? | How does GC decide what to collect? | What changed between PermGen and Metaspace?',
    'JVM_INTERNALS', 'JAVA', 'MEDIUM', 3, 8,
    ARRAY['jvm', 'memory', 'heap', 'stack', 'metaspace', 'gc'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Checked vs Unchecked Exceptions',
    'What is the difference between checked and unchecked exceptions in Java? When should you use each?',
    'Checked exceptions extend Exception (not RuntimeException) and must be declared in the method signature or caught. They represent recoverable conditions the caller should handle (e.g., IOException, SQLException). Unchecked exceptions extend RuntimeException and do not need to be declared. They represent programming errors (NullPointerException, IllegalArgumentException) or unrecoverable conditions. Best practice: use checked exceptions for external failures (file not found, network timeout) and unchecked for invalid inputs or bugs.',
    'What is the controversy around checked exceptions? | How does Spring handle checked exceptions in its DAO layer? | What are the trade-offs of wrapping checked exceptions as unchecked?',
    'JAVA_CORE', 'JAVA', 'EASY', 0, 4,
    ARRAY['exceptions', 'error-handling', 'checked', 'unchecked'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'HashMap internal working',
    'How does HashMap work internally in Java? What happens during put() and get()?',
    'HashMap uses an array of Node buckets. On put(key, value): (1) hashCode() is called on the key, (2) the hash is further spread via (h ^ h>>>16), (3) index = hash & (capacity-1) determines the bucket. If the bucket is empty, the node is placed directly. If a collision occurs, entries are chained as a linked list. In Java 8+, chains exceeding 8 nodes are converted to a Red-Black Tree (treeified) for O(log n) lookup. On get(), the same hash/index logic is applied, then equals() is used to find the right key in the bucket.',
    'What is the default load factor and initial capacity? | When does HashMap resize? | What is the difference between HashMap and ConcurrentHashMap?',
    'COLLECTIONS', 'JAVA', 'MEDIUM', 2, 7,
    ARRAY['hashmap', 'collections', 'data-structures', 'hashing'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Java 8 Streams — lazy evaluation',
    'What is lazy evaluation in Java Streams? Why does it matter?',
    'Stream operations are divided into intermediate (filter, map, flatMap — return a Stream) and terminal (collect, forEach, count — trigger execution). Intermediate operations are lazy — they do not execute until a terminal operation is called. This allows the JVM to optimise the pipeline: for example, with filter().map().findFirst(), the stream may process only the first matching element rather than the entire source. Short-circuiting operations (findFirst, anyMatch, limit) benefit most from this — they can stop processing early. Without lazy evaluation, each intermediate step would materialise the full collection.',
    'How is a stream different from a collection? | What is the difference between map() and flatMap()? | When would you use parallel streams and what are the risks?',
    'JAVA_CORE', 'JAVA', 'MEDIUM', 3, 8,
    ARRAY['streams', 'java8', 'functional', 'lazy-evaluation'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'volatile keyword in Java',
    'What does the volatile keyword do in Java? When should you use it?',
    'volatile guarantees visibility: writes to a volatile variable are immediately flushed to main memory, and reads always fetch from main memory rather than a thread-local CPU cache. This prevents stale reads in multi-threaded scenarios. It does NOT guarantee atomicity — volatile int count++; is still a race condition (read-modify-write is not atomic). Use volatile for simple flags (boolean stopRequested) or single-writer/multi-reader scenarios. For compound operations, use AtomicInteger or synchronised blocks.',
    'What is the Java Memory Model? | What is the difference between volatile and synchronized? | When would you use AtomicInteger instead of volatile?',
    'CONCURRENCY', 'JAVA', 'MEDIUM', 4, 10,
    ARRAY['concurrency', 'volatile', 'threads', 'memory-model'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Functional interfaces and lambdas',
    'What is a functional interface? How do lambdas relate to them? Name the key built-in functional interfaces in Java 8.',
    'A functional interface has exactly one abstract method (SAM — Single Abstract Method). Lambdas are syntactic sugar for implementing functional interfaces anonymously. Key built-in interfaces: Predicate<T> (takes T, returns boolean — for filtering), Function<T,R> (takes T, returns R — for transformation), Consumer<T> (takes T, returns void — for side effects), Supplier<T> (takes nothing, returns T — for lazy creation), BiFunction<T,U,R> (two inputs, one output). The @FunctionalInterface annotation enforces the single-abstract-method constraint at compile time.',
    'What is method reference and how does it relate to lambdas? | How does Optional use functional interfaces? | What is the difference between Predicate.and() and Predicate.or()?',
    'JAVA_CORE', 'JAVA', 'MEDIUM', 2, 7,
    ARRAY['java8', 'lambdas', 'functional-interface', 'streams'],
    true, NOW(), NOW()
);

-- ============================================================
-- SPRING BOOT (8 questions)
-- ============================================================

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Spring Boot auto-configuration',
    'How does Spring Boot auto-configuration work? What is @SpringBootApplication doing?',
    '@SpringBootApplication is a composite annotation combining @Configuration, @EnableAutoConfiguration, and @ComponentScan. @EnableAutoConfiguration triggers Spring Boot''s auto-configuration mechanism: it reads META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports (Spring Boot 3.x) to find all auto-configuration classes. Each class is annotated with @Conditional variants (e.g., @ConditionalOnClass, @ConditionalOnMissingBean) so beans are only created when certain conditions are met — e.g., DataSourceAutoConfiguration only fires if a JDBC driver is on the classpath. This is the magic behind "zero XML configuration."',
    'How do you exclude a specific auto-configuration? | How do you write your own auto-configuration? | What is the difference between @Component and @Bean?',
    'SPRING_BOOT', 'SPRING', 'MEDIUM', 2, 7,
    ARRAY['spring-boot', 'auto-configuration', 'annotations', 'conditional'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Bean scopes in Spring',
    'What are the different bean scopes in Spring? When would you use prototype over singleton?',
    'Singleton (default): one instance per Spring container, shared across all requests. Prototype: new instance created every time the bean is requested. Request: one instance per HTTP request (web apps). Session: one instance per HTTP session. Application: one instance per ServletContext. Use prototype when the bean holds state that should not be shared (e.g., a command object or a stateful helper). Singleton is appropriate for stateless services, repositories, and controllers. Injecting a prototype bean into a singleton requires using ApplicationContext.getBean() or @Lookup — plain injection gives you only one instance.',
    'What problem does @Lookup solve? | How does scope affect performance? | What happens if a session-scoped bean is injected into a singleton?',
    'SPRING_BOOT', 'SPRING', 'MEDIUM', 3, 8,
    ARRAY['spring', 'bean-scope', 'singleton', 'prototype', 'ioc'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Spring @Transactional — how it works',
    'How does @Transactional work in Spring? What are its common pitfalls?',
    'Spring creates a proxy around the annotated bean. When a transactional method is called externally, the proxy intercepts the call, opens a transaction, delegates to the real method, and commits or rolls back. Common pitfalls: (1) Self-invocation — calling a @Transactional method from within the same class bypasses the proxy and no transaction is started. (2) Visibility — @Transactional on private methods has no effect (proxies cannot override private methods). (3) Default rollback — only rolls back on RuntimeException; for checked exceptions you need rollbackFor. (4) Propagation mismatches — REQUIRES_NEW vs REQUIRED behave differently in nested calls.',
    'What is the difference between REQUIRED and REQUIRES_NEW propagation? | How would you handle transactions across microservices? | What is optimistic vs pessimistic locking?',
    'SPRING_BOOT', 'SPRING', 'HARD', 5, 12,
    ARRAY['spring', 'transactions', 'transactional', 'proxy', 'aop'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Spring profiles and environment config',
    'How do Spring profiles work? How do you manage environment-specific configuration?',
    'Profiles allow you to define beans and configuration that are active only in specific environments. Use @Profile("dev") on @Configuration or @Bean classes. In application.yml, separate profiles with --- and spring.config.activate.on-profile. Activate via spring.profiles.active property (command line, env var, or application.yml). For secrets, use environment variables or Spring Cloud Config rather than committing values. Spring Boot 3.x supports profile groups: spring.profiles.group.production=prod-db,prod-security to activate multiple profiles with one name.',
    'How do you manage secrets in production? | What is Spring Cloud Config and when would you use it? | How do profile-specific beans interact with auto-configuration?',
    'SPRING_BOOT', 'SPRING', 'MEDIUM', 3, 8,
    ARRAY['spring', 'profiles', 'configuration', 'environment'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Spring Boot Actuator',
    'What is Spring Boot Actuator? Which endpoints are most useful in production?',
    'Actuator exposes built-in endpoints for monitoring and management. Key endpoints: /actuator/health (readiness and liveness probes — critical for Kubernetes), /actuator/metrics (JVM, HTTP, custom metrics), /actuator/env (active properties and their sources), /actuator/loggers (change log levels at runtime without restart), /actuator/info (build version, git commit), /actuator/threaddump and /actuator/heapdump (for diagnosing production issues). In production, secure actuator endpoints with Spring Security and expose only what is needed. Integrate with Prometheus via the micrometer-registry-prometheus dependency.',
    'How do you write a custom health indicator? | How do you secure actuator endpoints? | How does Actuator integrate with Prometheus and Grafana?',
    'SPRING_BOOT', 'SPRING', 'MEDIUM', 3, 8,
    ARRAY['spring-boot', 'actuator', 'monitoring', 'production', 'prometheus'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Exception handling in Spring REST — @ControllerAdvice',
    'How do you handle exceptions globally in a Spring REST API?',
    '@ControllerAdvice (or @RestControllerAdvice for JSON responses) defines a global exception handler. Use @ExceptionHandler methods inside it to handle specific exception types. Best practice: define a custom ErrorResponse DTO with fields like timestamp, status, message, path. Map domain exceptions (e.g., ResourceNotFoundException) to HTTP 404, validation exceptions to 400, security exceptions to 403. Avoid leaking stack traces in production responses. Spring Boot''s default BasicErrorController can be replaced or extended. Consider using RFC 7807 Problem Details (supported natively in Spring 6 via ProblemDetail).',
    'What is ProblemDetail in Spring 6? | How do you handle validation errors from @Valid? | How do you handle exceptions in WebFlux?',
    'SPRING_BOOT', 'SPRING', 'MEDIUM', 3, 8,
    ARRAY['spring', 'exception-handling', 'rest', 'controller-advice'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Spring Data JPA — N+1 problem',
    'What is the N+1 query problem in JPA? How do you detect and fix it?',
    'N+1 occurs when loading a parent entity triggers N additional queries to load each child — e.g., fetching 50 Orders then separately querying OrderItems for each one = 51 queries. Causes: lazy loading on @OneToMany with default fetch type. Detection: enable show-sql, use p6spy, or Hypersistence Optimizer. Fixes: (1) JOIN FETCH in JPQL — SELECT o FROM Order o JOIN FETCH o.items, (2) @EntityGraph to declaratively define fetch plan, (3) @BatchSize to batch lazy loads, (4) DTO projections with native or JPQL query to avoid loading entities at all. Prefer projections for read-heavy endpoints.',
    'What is the difference between EAGER and LAZY fetching? | When would you use a DTO projection vs an entity? | What is the difference between JOIN FETCH and @EntityGraph?',
    'HIBERNATE_JPA', 'SPRING', 'HARD', 4, 12,
    ARRAY['jpa', 'hibernate', 'n+1', 'performance', 'queries'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Dependency Injection types in Spring',
    'What are the three types of dependency injection in Spring? Which is preferred and why?',
    'Constructor injection: dependencies passed via constructor. Field injection: @Autowired on field directly. Setter injection: @Autowired on setter method. Constructor injection is preferred: (1) dependencies are explicit and immutable (final fields), (2) the object is always in a valid state after construction, (3) easier to unit test — no reflection needed, just call the constructor, (4) detects circular dependencies at startup rather than at runtime. Field injection hides dependencies and makes testing harder (requires mocking frameworks or Spring context). Setter injection is useful for optional dependencies.',
    'How does Spring detect circular dependencies? | Why does Spring recommend constructor injection in its own documentation? | How do you break a circular dependency?',
    'SPRING_BOOT', 'SPRING', 'EASY', 1, 5,
    ARRAY['spring', 'dependency-injection', 'ioc', 'constructor-injection'],
    true, NOW(), NOW()
);

-- ============================================================
-- DESIGN PATTERNS (7 questions)
-- ============================================================

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Singleton pattern — thread safety',
    'How do you implement a thread-safe Singleton in Java? What are the different approaches?',
    'Options: (1) Eager initialisation — instance created at class load time, inherently thread-safe but instance created even if never used. (2) Synchronized getInstance() — thread-safe but every call acquires a lock, hurting performance. (3) Double-checked locking with volatile — checks null twice: once without lock, once with lock to avoid the synchronisation bottleneck; volatile prevents instruction reordering. (4) Bill Pugh / Holder pattern — uses a static inner class; class is loaded lazily by the JVM and initialisation is thread-safe by the class loader. (5) Enum singleton — Josh Bloch''s recommendation; handles serialisation and reflection attacks, prevents multiple instances.',
    'Why does Enum prevent reflection-based attacks? | How does serialisation break a Singleton and how do you fix it? | When is Singleton an anti-pattern?',
    'DESIGN_PATTERNS', 'JAVA', 'MEDIUM', 3, 8,
    ARRAY['singleton', 'design-patterns', 'thread-safety', 'creational'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Builder pattern — when and why',
    'When would you use the Builder pattern? How does it compare to telescoping constructors?',
    'Use Builder when: (1) An object has many optional parameters — telescoping constructors become unreadable beyond 4-5 params, (2) You want to enforce immutability (fields can be final), (3) You want to validate the object before construction. The pattern separates construction from representation. In Java, Lombok''s @Builder generates this automatically. In Spring, UriComponentsBuilder and MockMvcRequestBuilders are classic examples. Compared to telescoping constructors: Builder is more readable (named params via method chaining), safer (no risk of confusing param order), and flexible (reuse a builder to create similar objects).',
    'How does Builder differ from Factory Method? | How would you make a Builder thread-safe? | What is the difference between Lombok @Builder and manual Builder?',
    'DESIGN_PATTERNS', 'JAVA', 'EASY', 2, 7,
    ARRAY['builder', 'design-patterns', 'creational', 'immutability'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Strategy pattern in real-world Spring',
    'Explain the Strategy pattern. Give a real-world example using Spring.',
    'Strategy defines a family of algorithms, encapsulates each one, and makes them interchangeable. The context holds a reference to a strategy interface and delegates to it. Real-world Spring example: payment processing — PaymentStrategy interface with process(Payment) method. Implementations: CreditCardStrategy, UPIStrategy, NetBankingStrategy. Spring can inject the right strategy using a Map<String, PaymentStrategy> where Spring auto-populates all implementations by bean name. The service picks the right strategy based on a key (e.g., payment type) without any if-else. This is also how Spring Security''s AuthenticationProvider chain works.',
    'How is Strategy different from State pattern? | How does Spring''s HandlerMapping use Strategy? | How would you add a new payment method without changing existing code?',
    'DESIGN_PATTERNS', 'JAVA', 'MEDIUM', 4, 10,
    ARRAY['strategy', 'design-patterns', 'behavioural', 'spring'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Observer pattern and Spring Events',
    'How does the Observer pattern work? How does Spring implement it with application events?',
    'Observer defines a one-to-many relationship — when the subject changes state, all observers are notified. In Spring: publish events via ApplicationEventPublisher.publishEvent(). Listeners use @EventListener on a method or implement ApplicationListener<T>. Events are synchronous by default; use @Async to make them asynchronous. Use case: after a user registers, publish UserRegisteredEvent — email service, audit service, and notification service each listen independently. This decouples the registration flow from downstream processing. Spring also supports conditional listeners with @EventListener(condition = "...") using SpEL.',
    'What is the difference between synchronous and async event listeners? | How do you handle exceptions in async event listeners? | How does this compare to Kafka for event-driven systems?',
    'DESIGN_PATTERNS', 'SPRING', 'MEDIUM', 4, 10,
    ARRAY['observer', 'design-patterns', 'spring-events', 'behavioural', 'event-driven'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Decorator pattern vs Inheritance',
    'When would you use the Decorator pattern instead of inheritance to extend behaviour?',
    'Decorator attaches additional responsibilities to an object dynamically without altering its class. Prefer Decorator over inheritance when: (1) you need to add behaviour at runtime, (2) you need to combine multiple behaviours independently (a logging+caching+retry decorator vs a combinatorial explosion of subclasses), (3) the class to extend is final. Java examples: BufferedReader wraps FileReader; Collections.unmodifiableList() wraps a list. Spring example: HttpServletRequestWrapper decorates HttpServletRequest to add custom headers or body parsing. The key principle — Decorator implements the same interface as the component it wraps.',
    'How is Decorator different from Proxy pattern? | How does Spring AOP relate to Decorator? | What is the difference between Decorator and Chain of Responsibility?',
    'DESIGN_PATTERNS', 'JAVA', 'MEDIUM', 4, 10,
    ARRAY['decorator', 'design-patterns', 'structural', 'inheritance'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Factory Method vs Abstract Factory',
    'What is the difference between Factory Method and Abstract Factory patterns?',
    'Factory Method defines an interface for creating an object but lets subclasses decide which class to instantiate — one product, varying implementations. Abstract Factory provides an interface for creating families of related objects without specifying concrete classes — multiple related products created together. Example: Factory Method — NotificationFactory.createNotification() returns Email or SMS. Abstract Factory — UIFactory.createButton() and UIFactory.createTextField() together ensure consistent Windows or Mac UI components. In Spring: BeanFactory is conceptually a Factory; FactoryBean<T> is Factory Method applied to bean creation.',
    'Where does Spring use these patterns internally? | How is Factory different from DI? | When would you choose one over the other?',
    'DESIGN_PATTERNS', 'JAVA', 'HARD', 5, 12,
    ARRAY['factory', 'abstract-factory', 'design-patterns', 'creational'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Proxy pattern — static vs dynamic',
    'Explain the Proxy pattern. What is the difference between static and dynamic proxies? How does Spring use proxies?',
    'Proxy provides a surrogate that controls access to another object — for access control, lazy loading, caching, or logging. Static proxy: you write the proxy class manually implementing the same interface. Dynamic proxy (java.lang.reflect.Proxy): generated at runtime for interfaces — no manual class needed. CGLIB proxy: generates a subclass at runtime for classes without an interface — Spring uses this for @Configuration classes. Spring AOP uses JDK dynamic proxies (interface-based) or CGLIB (class-based) to implement @Transactional, @Cacheable, @Async, and @Secured. This is why self-invocation bypasses these annotations — you''re calling the real object, not the proxy.',
    'Why can''t @Transactional work on private methods? | What is the difference between Spring AOP and AspectJ? | How does CGLIB proxy affect final classes?',
    'DESIGN_PATTERNS', 'SPRING', 'HARD', 6, 14,
    ARRAY['proxy', 'design-patterns', 'aop', 'spring', 'cglib', 'structural'],
    true, NOW(), NOW()
);

-- ============================================================
-- SOLID PRINCIPLES (4 questions)
-- ============================================================

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Single Responsibility Principle — practical application',
    'What is the Single Responsibility Principle? How do you apply it in a Spring Boot service layer?',
    'SRP: a class should have only one reason to change — one responsibility. In a Spring Boot service, violation looks like: UserService that creates users, sends welcome emails, logs audit events, and generates PDFs. Each of these is a separate reason to change. Apply SRP by splitting: UserService (user creation logic), EmailService (sending emails), AuditService (audit logging), ReportService (PDF generation). UserService calls the others via injection. Benefits: each class is smaller, testable in isolation, and changes to email logic don''t risk breaking user creation. Signals of SRP violation: class with many unrelated methods, or method names with "And" in them.',
    'How does SRP relate to microservices decomposition? | Where do you draw the line — can you over-apply SRP? | How does SRP interact with cohesion and coupling?',
    'SOLID', 'JAVA', 'EASY', 2, 7,
    ARRAY['solid', 'srp', 'clean-code', 'design-principles'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Open/Closed Principle with Spring',
    'Explain the Open/Closed Principle. How do you implement it using Spring?',
    'OCP: software entities should be open for extension but closed for modification. When new behaviour is needed, add new code rather than changing existing code. In Spring: use interfaces and inject different implementations. Example: DiscountCalculator interface with calculate(Order) method. Implementations: SeasonalDiscountCalculator, LoyaltyDiscountCalculator, BulkDiscountCalculator. To add a new discount type, create a new class — no existing code changes. Spring''s Strategy pattern injection (Map<String, DiscountCalculator>) combined with a discriminator key (discount type from the order) achieves OCP cleanly. Design patterns that enable OCP: Strategy, Template Method, Decorator.',
    'How does OCP relate to the Strategy pattern? | Can you over-apply OCP and make code harder to read? | How does OCP apply at the microservice level?',
    'SOLID', 'JAVA', 'MEDIUM', 3, 9,
    ARRAY['solid', 'ocp', 'open-closed', 'design-principles', 'spring'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Liskov Substitution Principle — violations and fixes',
    'What is the Liskov Substitution Principle? Give an example of a violation and how to fix it.',
    'LSP: objects of a subclass should be substitutable for objects of the superclass without breaking the program. Classic violation: Square extends Rectangle. Rectangle has setWidth() and setHeight() independently. Square overrides both to keep sides equal. Code that calls rect.setWidth(5); rect.setHeight(10); and expects area=50 breaks for Square (area=100). Fix: don''t inherit — use a separate Shape hierarchy or a common interface. In Spring, LSP violations appear when @Service implementations throw unexpected exceptions, return nulls when the interface contract doesn''t allow it, or strengthen preconditions. LSP is fundamentally about behavioural contracts, not just type compatibility.',
    'How does LSP relate to interface segregation? | How do you express contracts in Java (e.g., using preconditions)? | What is covariance and contravariance in the context of LSP?',
    'SOLID', 'JAVA', 'MEDIUM', 4, 10,
    ARRAY['solid', 'lsp', 'liskov', 'design-principles', 'inheritance'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Dependency Inversion Principle vs Dependency Injection',
    'What is the Dependency Inversion Principle? How is it different from Dependency Injection?',
    'DIP: (1) High-level modules should not depend on low-level modules — both should depend on abstractions. (2) Abstractions should not depend on details — details should depend on abstractions. Example: OrderService (high-level) should not directly instantiate EmailSender (low-level). Both should depend on NotificationService interface. DIP is a design principle — it tells you what the architecture should look like. Dependency Injection is a pattern/mechanism — it is one way to implement DIP by having a container supply the dependencies. You can have DI without DIP (injecting concrete classes). Spring implements DIP via constructor injection of interfaces, not concrete classes.',
    'What is Inversion of Control? How does it differ from DI? | How does DIP affect testability? | Can you violate DIP even while using Spring DI?',
    'SOLID', 'JAVA', 'MEDIUM', 3, 9,
    ARRAY['solid', 'dip', 'dependency-inversion', 'design-principles', 'spring'],
    true, NOW(), NOW()
);

-- ============================================================
-- TDD (3 questions)
-- ============================================================

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'TDD Red-Green-Refactor cycle',
    'Explain the TDD cycle. What are the rules of TDD and why does the order matter?',
    'TDD follows Red → Green → Refactor: (1) Red — write a failing test for the smallest piece of behaviour. Do not write any production code yet. (2) Green — write the minimum code to make the test pass. No more than needed — even hardcoding is acceptable at this stage. (3) Refactor — clean up both test and production code without changing behaviour. The order matters: writing the test first forces you to think about the API before the implementation. The failing test proves the test actually tests something (a test that never fails is useless). The discipline of minimum green code prevents over-engineering. Refactoring only happens with a green safety net.',
    'What is the difference between TDD and testing-after? | How do you apply TDD to a Spring Boot service? | What is the "fake it till you make it" principle in TDD?',
    'TDD', 'JAVA', 'EASY', 1, 5,
    ARRAY['tdd', 'testing', 'red-green-refactor', 'clean-code'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Mocking with Mockito — when and how',
    'When should you mock a dependency in a unit test? Explain the difference between mock, stub, and spy in Mockito.',
    'Mock: a fake object that records interactions — used to verify behaviour (was a method called? how many times?). Stub: a fake that returns pre-configured values — used to control the test environment (when x.get() is called, return y). Spy: wraps a real object — real methods are called unless explicitly stubbed; useful for partial mocking. In Mockito: mock() creates a mock, when().thenReturn() stubs it, verify() asserts interactions. Mock dependencies that: (1) are slow (DB, network), (2) are non-deterministic (time, random), (3) have side effects (email, payment). Do NOT mock value objects or simple utility classes — test those directly. Over-mocking is a test smell that means production code is too tightly coupled.',
    'What is the difference between @Mock and @InjectMocks? | When would you use @MockBean in a Spring Boot test? | What is the difference between unit test, integration test, and slice test in Spring?',
    'TDD', 'JAVA', 'MEDIUM', 2, 7,
    ARRAY['tdd', 'mockito', 'testing', 'mocking', 'unit-test'],
    true, NOW(), NOW()
);

INSERT INTO question (id, title, body, answer, follow_up_probes, topic, tech_stack, difficulty_level, experience_range_min, experience_range_max, tags, is_active, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'Testing Spring Boot — @WebMvcTest vs @SpringBootTest',
    'What is the difference between @WebMvcTest and @SpringBootTest? When do you use each?',
    '@SpringBootTest loads the full application context — all beans, auto-configuration, and the embedded server (optionally). Use it for end-to-end integration tests that need the full stack. It is slow because it boots the entire app. @WebMvcTest loads only the web layer (controllers, filters, @ControllerAdvice) — service and repository beans are not loaded and must be mocked with @MockBean. Use it for focused controller tests: request mapping, request/response serialisation, validation, and error handling. Other slice annotations: @DataJpaTest (repository layer + H2), @JsonTest (JSON serialisation only). Slice tests are faster and more focused than full @SpringBootTest.',
    'What is TestRestTemplate vs MockMvc? | How do you test a repository with @DataJpaTest? | How do you write a contract test with Spring Cloud Contract?',
    'TDD', 'SPRING', 'MEDIUM', 3, 8,
    ARRAY['tdd', 'spring-boot-test', 'webmvctest', 'integration-test', 'slice-test'],
    true, NOW(), NOW()
);

-- ============================================================
-- Verify count
-- ============================================================
-- SELECT COUNT(*) FROM question;  -- Expected: 30
REQEOF