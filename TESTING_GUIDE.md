# Testing Guide

---

## Test Structure

```
src/test/java/com/crm/springmvc/
├── entity/
│   └── CustomerTest.java          ← POJO unit tests
├── service/
│   └── CustomerServiceTest.java   ← Service layer with Mockito
├── controller/
│   └── CustomerControllerTest.java ← Controller with Mockito
└── dao/
    └── CustomerDAOTest.java        ← DAO with mocked SessionFactory
```

---

## Running Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=CustomerServiceTest

# Specific test method
mvn test -Dtest=CustomerServiceTest#testGetCustomers_returnsAllCustomers

# Skip tests during build
mvn package -DskipTests
```

---

## Test Coverage by Layer

### Entity Tests (`CustomerTest`)
- Default constructor initialises all fields to null/0
- Parameterised constructor sets all fields correctly
- Getters and setters work correctly
- `getFullName()` concatenates first and last name
- `toString()` includes all field values

### Service Tests (`CustomerServiceTest`)
Uses `@Mock CustomerDAO` to isolate service logic from the database.

| Test | Verifies |
|---|---|
| `testGetCustomers_returnsAllCustomers` | delegates to DAO and returns list |
| `testGetCustomers_returnsEmptyList` | handles empty result |
| `testGetCustomer_existingId` | returns correct customer |
| `testGetCustomer_nonExistingId_returnsNull` | returns null gracefully |
| `testSaveCustomer_newCustomer_callsDAO` | calls `customerDAO.saveCustomer` |
| `testDeleteCustomer_callsDAODelete` | calls `customerDAO.deleteCustomer` |
| `testSearchCustomers_withValidName_returnsList` | filters results |
| `testSearchCustomers_withNull_returnsAll` | handles null search |

### Controller Tests (`CustomerControllerTest`)
Uses `@Mock CustomerService` and `@Mock BindingResult`.

| Test | Verifies |
|---|---|
| `testListCustomers_returnsListView` | returns `"list-customers"` with model |
| `testShowFormForAdd_returnsFormView` | returns `"customer-form"` with empty Customer |
| `testSaveCustomer_validData_redirectsToList` | redirects on success |
| `testSaveCustomer_validationErrors_returnsForm` | stays on form on errors |
| `testShowFormForUpdate_existingCustomer_returnsForm` | populates model |
| `testShowFormForUpdate_notFound_redirectsToList` | handles missing ID |
| `testDeleteCustomer_existingCustomer_redirectsToList` | deletes and redirects |
| `testDeleteCustomer_notFound_redirectsToList` | no-op if customer missing |
| `testSearchCustomers_withSearchTerm` | filters and adds to model |

### DAO Tests (`CustomerDAOTest`)
Uses `@Mock SessionFactory` and `@Mock Session`.

| Test | Verifies |
|---|---|
| `testGetCustomers_returnsAll` | executes correct HQL query |
| `testGetCustomer_existingId` | calls `session.get` |
| `testGetCustomer_nonExistingId_returnsNull` | handles null from Hibernate |
| `testSaveCustomer_callsSaveOrUpdate` | calls `session.saveOrUpdate` |
| `testDeleteCustomer_executesDeleteQuery` | executes DELETE HQL |
| `testSearchCustomers_withBlankName_returnsAll` | no filter on blank search |
| `testSearchCustomers_withValidName` | passes LIKE parameter |

---

## Test Dependencies

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>4.11.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.3.39</version>
    <scope>test</scope>
</dependency>
```

---

## Generating a Test Report

```bash
mvn surefire-report:report
```

Open `target/site/surefire-report.html` in a browser.

---

## Writing New Tests

### Service test pattern
```java
@RunWith(MockitoJUnitRunner.class)
public class MyServiceTest {

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void testMyMethod() {
        when(customerDAO.getCustomers()).thenReturn(someList);
        List<Customer> result = customerService.getCustomers();
        assertEquals(someList, result);
        verify(customerDAO, times(1)).getCustomers();
    }
}
```

### Controller test pattern
```java
@RunWith(MockitoJUnitRunner.class)
public class MyControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController controller;

    @Test
    public void testListCustomers() {
        Model model = new ExtendedModelMap();
        when(customerService.getCustomers()).thenReturn(Arrays.asList());
        String view = controller.listCustomers(model);
        assertEquals("list-customers", view);
    }
}
```

---

## Integration Testing (Manual)

After starting the application, verify these flows:

1. **Add** — click "Add Customer", fill in form, save → new row appears in table
2. **Update** — click "Update" on any row, change email, save → change reflected
3. **Delete** — click "Delete", confirm in dialog → row removed
4. **Search** — type "John" in search box → only matching rows shown
5. **Validation** — submit the add form with blank fields → error messages appear
6. **Back navigation** — "Back to List" link from form → returns to list
