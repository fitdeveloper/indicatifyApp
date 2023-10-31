package ma.indicatify.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import ma.indicatify.app.IntegrationTest;
import ma.indicatify.app.domain.Employee;
import ma.indicatify.app.domain.JobTitle;
import ma.indicatify.app.domain.Level;
import ma.indicatify.app.domain.Site;
import ma.indicatify.app.domain.User;
import ma.indicatify.app.domain.enumeration.GenderEnum;
import ma.indicatify.app.repository.EmployeeRepository;
import ma.indicatify.app.repository.EntityManager;
import ma.indicatify.app.service.EmployeeService;
import ma.indicatify.app.service.dto.EmployeeDTO;
import ma.indicatify.app.service.mapper.EmployeeMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_FIRSTNAME_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME_EMPLOYEE = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME_EMPLOYEE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULATION_NUMBER_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULATION_NUMBER_EMPLOYEE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH_EMPLOYEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH_EMPLOYEE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_EMPLOYEE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_EMPLOYEE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_HIRE_DATE_EMPLOYEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HIRE_DATE_EMPLOYEE = LocalDate.now(ZoneId.systemDefault());

    private static final GenderEnum DEFAULT_GENDER_EMPLOYEE = GenderEnum.Male;
    private static final GenderEnum UPDATED_GENDER_EMPLOYEE = GenderEnum.Female;

    private static final String DEFAULT_DESC_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_DESC_EMPLOYEE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeService employeeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .firstnameEmployee(DEFAULT_FIRSTNAME_EMPLOYEE)
            .lastnameEmployee(DEFAULT_LASTNAME_EMPLOYEE)
            .matriculationNumberEmployee(DEFAULT_MATRICULATION_NUMBER_EMPLOYEE)
            .dateOfBirthEmployee(DEFAULT_DATE_OF_BIRTH_EMPLOYEE)
            .emailEmployee(DEFAULT_EMAIL_EMPLOYEE)
            .phoneEmployee(DEFAULT_PHONE_EMPLOYEE)
            .hireDateEmployee(DEFAULT_HIRE_DATE_EMPLOYEE)
            .genderEmployee(DEFAULT_GENDER_EMPLOYEE)
            .descEmployee(DEFAULT_DESC_EMPLOYEE);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        employee.setUser(user);
        // Add required entity
        JobTitle jobTitle;
        jobTitle = em.insert(JobTitleResourceIT.createEntity(em)).block();
        employee.setJobTitle(jobTitle);
        // Add required entity
        Level level;
        level = em.insert(LevelResourceIT.createEntity(em)).block();
        employee.setLevel(level);
        // Add required entity
        Site site;
        site = em.insert(SiteResourceIT.createEntity(em)).block();
        employee.setSite(site);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .firstnameEmployee(UPDATED_FIRSTNAME_EMPLOYEE)
            .lastnameEmployee(UPDATED_LASTNAME_EMPLOYEE)
            .matriculationNumberEmployee(UPDATED_MATRICULATION_NUMBER_EMPLOYEE)
            .dateOfBirthEmployee(UPDATED_DATE_OF_BIRTH_EMPLOYEE)
            .emailEmployee(UPDATED_EMAIL_EMPLOYEE)
            .phoneEmployee(UPDATED_PHONE_EMPLOYEE)
            .hireDateEmployee(UPDATED_HIRE_DATE_EMPLOYEE)
            .genderEmployee(UPDATED_GENDER_EMPLOYEE)
            .descEmployee(UPDATED_DESC_EMPLOYEE);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        employee.setUser(user);
        // Add required entity
        JobTitle jobTitle;
        jobTitle = em.insert(JobTitleResourceIT.createUpdatedEntity(em)).block();
        employee.setJobTitle(jobTitle);
        // Add required entity
        Level level;
        level = em.insert(LevelResourceIT.createUpdatedEntity(em)).block();
        employee.setLevel(level);
        // Add required entity
        Site site;
        site = em.insert(SiteResourceIT.createUpdatedEntity(em)).block();
        employee.setSite(site);
        return employee;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_employee__knowledge_domain").block();
            em.deleteAll("rel_employee__perimeter").block();
            em.deleteAll(Employee.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
        JobTitleResourceIT.deleteEntities(em);
        LevelResourceIT.deleteEntities(em);
        SiteResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        employee = createEntity(em);
    }

    @Test
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstnameEmployee()).isEqualTo(DEFAULT_FIRSTNAME_EMPLOYEE);
        assertThat(testEmployee.getLastnameEmployee()).isEqualTo(DEFAULT_LASTNAME_EMPLOYEE);
        assertThat(testEmployee.getMatriculationNumberEmployee()).isEqualTo(DEFAULT_MATRICULATION_NUMBER_EMPLOYEE);
        assertThat(testEmployee.getDateOfBirthEmployee()).isEqualTo(DEFAULT_DATE_OF_BIRTH_EMPLOYEE);
        assertThat(testEmployee.getEmailEmployee()).isEqualTo(DEFAULT_EMAIL_EMPLOYEE);
        assertThat(testEmployee.getPhoneEmployee()).isEqualTo(DEFAULT_PHONE_EMPLOYEE);
        assertThat(testEmployee.getHireDateEmployee()).isEqualTo(DEFAULT_HIRE_DATE_EMPLOYEE);
        assertThat(testEmployee.getGenderEmployee()).isEqualTo(DEFAULT_GENDER_EMPLOYEE);
        assertThat(testEmployee.getDescEmployee()).isEqualTo(DEFAULT_DESC_EMPLOYEE);
    }

    @Test
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFirstnameEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setFirstnameEmployee(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLastnameEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setLastnameEmployee(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMatriculationNumberEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setMatriculationNumberEmployee(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateOfBirthEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setDateOfBirthEmployee(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setEmailEmployee(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGenderEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setGenderEmployee(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEmployees() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get all the employeeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(employee.getId().intValue()))
            .jsonPath("$.[*].firstnameEmployee")
            .value(hasItem(DEFAULT_FIRSTNAME_EMPLOYEE))
            .jsonPath("$.[*].lastnameEmployee")
            .value(hasItem(DEFAULT_LASTNAME_EMPLOYEE))
            .jsonPath("$.[*].matriculationNumberEmployee")
            .value(hasItem(DEFAULT_MATRICULATION_NUMBER_EMPLOYEE))
            .jsonPath("$.[*].dateOfBirthEmployee")
            .value(hasItem(DEFAULT_DATE_OF_BIRTH_EMPLOYEE.toString()))
            .jsonPath("$.[*].emailEmployee")
            .value(hasItem(DEFAULT_EMAIL_EMPLOYEE))
            .jsonPath("$.[*].phoneEmployee")
            .value(hasItem(DEFAULT_PHONE_EMPLOYEE))
            .jsonPath("$.[*].hireDateEmployee")
            .value(hasItem(DEFAULT_HIRE_DATE_EMPLOYEE.toString()))
            .jsonPath("$.[*].genderEmployee")
            .value(hasItem(DEFAULT_GENDER_EMPLOYEE.toString()))
            .jsonPath("$.[*].descEmployee")
            .value(hasItem(DEFAULT_DESC_EMPLOYEE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsEnabled() {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsNotEnabled() {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(employeeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get the employee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(employee.getId().intValue()))
            .jsonPath("$.firstnameEmployee")
            .value(is(DEFAULT_FIRSTNAME_EMPLOYEE))
            .jsonPath("$.lastnameEmployee")
            .value(is(DEFAULT_LASTNAME_EMPLOYEE))
            .jsonPath("$.matriculationNumberEmployee")
            .value(is(DEFAULT_MATRICULATION_NUMBER_EMPLOYEE))
            .jsonPath("$.dateOfBirthEmployee")
            .value(is(DEFAULT_DATE_OF_BIRTH_EMPLOYEE.toString()))
            .jsonPath("$.emailEmployee")
            .value(is(DEFAULT_EMAIL_EMPLOYEE))
            .jsonPath("$.phoneEmployee")
            .value(is(DEFAULT_PHONE_EMPLOYEE))
            .jsonPath("$.hireDateEmployee")
            .value(is(DEFAULT_HIRE_DATE_EMPLOYEE.toString()))
            .jsonPath("$.genderEmployee")
            .value(is(DEFAULT_GENDER_EMPLOYEE.toString()))
            .jsonPath("$.descEmployee")
            .value(is(DEFAULT_DESC_EMPLOYEE));
    }

    @Test
    void getNonExistingEmployee() {
        // Get the employee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).block();
        updatedEmployee
            .firstnameEmployee(UPDATED_FIRSTNAME_EMPLOYEE)
            .lastnameEmployee(UPDATED_LASTNAME_EMPLOYEE)
            .matriculationNumberEmployee(UPDATED_MATRICULATION_NUMBER_EMPLOYEE)
            .dateOfBirthEmployee(UPDATED_DATE_OF_BIRTH_EMPLOYEE)
            .emailEmployee(UPDATED_EMAIL_EMPLOYEE)
            .phoneEmployee(UPDATED_PHONE_EMPLOYEE)
            .hireDateEmployee(UPDATED_HIRE_DATE_EMPLOYEE)
            .genderEmployee(UPDATED_GENDER_EMPLOYEE)
            .descEmployee(UPDATED_DESC_EMPLOYEE);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, employeeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstnameEmployee()).isEqualTo(UPDATED_FIRSTNAME_EMPLOYEE);
        assertThat(testEmployee.getLastnameEmployee()).isEqualTo(UPDATED_LASTNAME_EMPLOYEE);
        assertThat(testEmployee.getMatriculationNumberEmployee()).isEqualTo(UPDATED_MATRICULATION_NUMBER_EMPLOYEE);
        assertThat(testEmployee.getDateOfBirthEmployee()).isEqualTo(UPDATED_DATE_OF_BIRTH_EMPLOYEE);
        assertThat(testEmployee.getEmailEmployee()).isEqualTo(UPDATED_EMAIL_EMPLOYEE);
        assertThat(testEmployee.getPhoneEmployee()).isEqualTo(UPDATED_PHONE_EMPLOYEE);
        assertThat(testEmployee.getHireDateEmployee()).isEqualTo(UPDATED_HIRE_DATE_EMPLOYEE);
        assertThat(testEmployee.getGenderEmployee()).isEqualTo(UPDATED_GENDER_EMPLOYEE);
        assertThat(testEmployee.getDescEmployee()).isEqualTo(UPDATED_DESC_EMPLOYEE);
    }

    @Test
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, employeeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .lastnameEmployee(UPDATED_LASTNAME_EMPLOYEE)
            .matriculationNumberEmployee(UPDATED_MATRICULATION_NUMBER_EMPLOYEE)
            .hireDateEmployee(UPDATED_HIRE_DATE_EMPLOYEE)
            .genderEmployee(UPDATED_GENDER_EMPLOYEE)
            .descEmployee(UPDATED_DESC_EMPLOYEE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstnameEmployee()).isEqualTo(DEFAULT_FIRSTNAME_EMPLOYEE);
        assertThat(testEmployee.getLastnameEmployee()).isEqualTo(UPDATED_LASTNAME_EMPLOYEE);
        assertThat(testEmployee.getMatriculationNumberEmployee()).isEqualTo(UPDATED_MATRICULATION_NUMBER_EMPLOYEE);
        assertThat(testEmployee.getDateOfBirthEmployee()).isEqualTo(DEFAULT_DATE_OF_BIRTH_EMPLOYEE);
        assertThat(testEmployee.getEmailEmployee()).isEqualTo(DEFAULT_EMAIL_EMPLOYEE);
        assertThat(testEmployee.getPhoneEmployee()).isEqualTo(DEFAULT_PHONE_EMPLOYEE);
        assertThat(testEmployee.getHireDateEmployee()).isEqualTo(UPDATED_HIRE_DATE_EMPLOYEE);
        assertThat(testEmployee.getGenderEmployee()).isEqualTo(UPDATED_GENDER_EMPLOYEE);
        assertThat(testEmployee.getDescEmployee()).isEqualTo(UPDATED_DESC_EMPLOYEE);
    }

    @Test
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .firstnameEmployee(UPDATED_FIRSTNAME_EMPLOYEE)
            .lastnameEmployee(UPDATED_LASTNAME_EMPLOYEE)
            .matriculationNumberEmployee(UPDATED_MATRICULATION_NUMBER_EMPLOYEE)
            .dateOfBirthEmployee(UPDATED_DATE_OF_BIRTH_EMPLOYEE)
            .emailEmployee(UPDATED_EMAIL_EMPLOYEE)
            .phoneEmployee(UPDATED_PHONE_EMPLOYEE)
            .hireDateEmployee(UPDATED_HIRE_DATE_EMPLOYEE)
            .genderEmployee(UPDATED_GENDER_EMPLOYEE)
            .descEmployee(UPDATED_DESC_EMPLOYEE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstnameEmployee()).isEqualTo(UPDATED_FIRSTNAME_EMPLOYEE);
        assertThat(testEmployee.getLastnameEmployee()).isEqualTo(UPDATED_LASTNAME_EMPLOYEE);
        assertThat(testEmployee.getMatriculationNumberEmployee()).isEqualTo(UPDATED_MATRICULATION_NUMBER_EMPLOYEE);
        assertThat(testEmployee.getDateOfBirthEmployee()).isEqualTo(UPDATED_DATE_OF_BIRTH_EMPLOYEE);
        assertThat(testEmployee.getEmailEmployee()).isEqualTo(UPDATED_EMAIL_EMPLOYEE);
        assertThat(testEmployee.getPhoneEmployee()).isEqualTo(UPDATED_PHONE_EMPLOYEE);
        assertThat(testEmployee.getHireDateEmployee()).isEqualTo(UPDATED_HIRE_DATE_EMPLOYEE);
        assertThat(testEmployee.getGenderEmployee()).isEqualTo(UPDATED_GENDER_EMPLOYEE);
        assertThat(testEmployee.getDescEmployee()).isEqualTo(UPDATED_DESC_EMPLOYEE);
    }

    @Test
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, employeeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeDelete = employeeRepository.findAll().collectList().block().size();

        // Delete the employee
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
