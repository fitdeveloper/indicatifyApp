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

describe('JobTitle e2e test', () => {
  const jobTitlePageUrl = '/job-title';
  const jobTitlePageUrlPattern = new RegExp('/job-title(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const jobTitleSample = { nameJobTitle: 'Outdoors Handcrafted' };

  let jobTitle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/job-titles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/job-titles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/job-titles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (jobTitle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/job-titles/${jobTitle.id}`,
      }).then(() => {
        jobTitle = undefined;
      });
    }
  });

  it('JobTitles menu should load JobTitles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('job-title');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('JobTitle').should('exist');
    cy.url().should('match', jobTitlePageUrlPattern);
  });

  describe('JobTitle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(jobTitlePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create JobTitle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/job-title/new$'));
        cy.getEntityCreateUpdateHeading('JobTitle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobTitlePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/job-titles',
          body: jobTitleSample,
        }).then(({ body }) => {
          jobTitle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/job-titles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/job-titles?page=0&size=20>; rel="last",<http://localhost/api/job-titles?page=0&size=20>; rel="first"',
              },
              body: [jobTitle],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(jobTitlePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details JobTitle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('jobTitle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobTitlePageUrlPattern);
      });

      it('edit button click should load edit JobTitle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('JobTitle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobTitlePageUrlPattern);
      });

      it('edit button click should load edit JobTitle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('JobTitle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobTitlePageUrlPattern);
      });

      it('last delete button click should delete instance of JobTitle', () => {
        cy.intercept('GET', '/api/job-titles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('jobTitle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobTitlePageUrlPattern);

        jobTitle = undefined;
      });
    });
  });

  describe('new JobTitle page', () => {
    beforeEach(() => {
      cy.visit(`${jobTitlePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('JobTitle');
    });

    it('should create an instance of JobTitle', () => {
      cy.get(`[data-cy="nameJobTitle"]`).type('copy Functionality').should('have.value', 'copy Functionality');

      cy.get(`[data-cy="descJobTitle"]`).type('National Rustic copying').should('have.value', 'National Rustic copying');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        jobTitle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', jobTitlePageUrlPattern);
    });
  });
});
