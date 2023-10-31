import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Employee e2e test', () => {
  const employeePageUrl = '/employee';
  const employeePageUrlPattern = new RegExp('/employee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const employeeSample = {"firstnameEmployee":"Myanmar world-class Generic","lastnameEmployee":"Savings Polarised","matriculationNumberEmployee":"program","dateOfBirthEmployee":"2023-10-31","emailEmployee":"Shoes","genderEmployee":"Female"};

  let employee;
  // let user;
  // let jobTitle;
  // let level;
  // let site;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"generating optical","firstName":"Albina","lastName":"Jacobi"},
    }).then(({ body }) => {
      user = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/job-titles',
      body: {"nameJobTitle":"iterate","descJobTitle":"cohesive"},
    }).then(({ body }) => {
      jobTitle = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/levels',
      body: {"nameLevel":"Cambridgeshire","valueLevel":"N2","descLevel":"XSS Pennsylvania"},
    }).then(({ body }) => {
      level = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/sites',
      body: {"nameSite":"Dynamic","addressSite":"mobile mobile purple","descSite":"Sleek cross-platform SQL"},
    }).then(({ body }) => {
      site = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/employees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employees').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employees/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/employees', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/job-titles', {
      statusCode: 200,
      body: [jobTitle],
    });

    cy.intercept('GET', '/api/levels', {
      statusCode: 200,
      body: [level],
    });

    cy.intercept('GET', '/api/knowledge-domains', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/perimeters', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/sites', {
      statusCode: 200,
      body: [site],
    });

  });
   */

  afterEach(() => {
    if (employee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employees/${employee.id}`,
      }).then(() => {
        employee = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
    if (jobTitle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/job-titles/${jobTitle.id}`,
      }).then(() => {
        jobTitle = undefined;
      });
    }
    if (level) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/levels/${level.id}`,
      }).then(() => {
        level = undefined;
      });
    }
    if (site) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sites/${site.id}`,
      }).then(() => {
        site = undefined;
      });
    }
  });
   */

  it('Employees menu should load Employees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Employee').should('exist');
    cy.url().should('match', employeePageUrlPattern);
  });

  describe('Employee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(employeePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Employee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/employee/new$'));
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/employees',
          body: {
            ...employeeSample,
            user: user,
            jobTitle: jobTitle,
            level: level,
            site: site,
          },
        }).then(({ body }) => {
          employee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/employees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/employees?page=0&size=20>; rel="last",<http://localhost/api/employees?page=0&size=20>; rel="first"',
              },
              body: [employee],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(employeePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(employeePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Employee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('employee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it('edit button click should load edit Employee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it('edit button click should load edit Employee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Employee', () => {
        cy.intercept('GET', '/api/employees/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('employee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);

        employee = undefined;
      });
    });
  });

  describe('new Employee page', () => {
    beforeEach(() => {
      cy.visit(`${employeePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Employee');
    });

    it.skip('should create an instance of Employee', () => {
      cy.get(`[data-cy="firstnameEmployee"]`).type('Strategist Synergized metrics').should('have.value', 'Strategist Synergized metrics');

      cy.get(`[data-cy="lastnameEmployee"]`).type('transform').should('have.value', 'transform');

      cy.get(`[data-cy="matriculationNumberEmployee"]`).type('back-end reboot XML').should('have.value', 'back-end reboot XML');

      cy.get(`[data-cy="dateOfBirthEmployee"]`).type('2023-10-31').blur().should('have.value', '2023-10-31');

      cy.get(`[data-cy="emailEmployee"]`).type('Illinois Producer envisioneer').should('have.value', 'Illinois Producer envisioneer');

      cy.get(`[data-cy="phoneEmployee"]`).type('Kip').should('have.value', 'Kip');

      cy.get(`[data-cy="hireDateEmployee"]`).type('2023-10-31').blur().should('have.value', '2023-10-31');

      cy.get(`[data-cy="genderEmployee"]`).select('Male');

      cy.get(`[data-cy="descEmployee"]`).type('Illinois Refined Investor').should('have.value', 'Illinois Refined Investor');

      cy.get(`[data-cy="user"]`).select(1);
      cy.get(`[data-cy="jobTitle"]`).select(1);
      cy.get(`[data-cy="level"]`).select(1);
      cy.get(`[data-cy="site"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        employee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', employeePageUrlPattern);
    });
  });
});
