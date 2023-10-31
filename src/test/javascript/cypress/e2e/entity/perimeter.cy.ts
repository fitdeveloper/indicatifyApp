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

describe('Perimeter e2e test', () => {
  const perimeterPageUrl = '/perimeter';
  const perimeterPageUrlPattern = new RegExp('/perimeter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const perimeterSample = { namePerimeter: 'Realigned maroon 17(E.U.A.-17)' };

  let perimeter;
  let activity;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/activities',
      body: { nameActivity: 'Colorado Sleek', descActivity: 'withdrawal' },
    }).then(({ body }) => {
      activity = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/perimeters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/perimeters').as('postEntityRequest');
    cy.intercept('DELETE', '/api/perimeters/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/activities', {
      statusCode: 200,
      body: [activity],
    });

    cy.intercept('GET', '/api/employees', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (perimeter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/perimeters/${perimeter.id}`,
      }).then(() => {
        perimeter = undefined;
      });
    }
  });

  afterEach(() => {
    if (activity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/activities/${activity.id}`,
      }).then(() => {
        activity = undefined;
      });
    }
  });

  it('Perimeters menu should load Perimeters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('perimeter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Perimeter').should('exist');
    cy.url().should('match', perimeterPageUrlPattern);
  });

  describe('Perimeter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(perimeterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Perimeter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/perimeter/new$'));
        cy.getEntityCreateUpdateHeading('Perimeter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', perimeterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/perimeters',
          body: {
            ...perimeterSample,
            activity: activity,
          },
        }).then(({ body }) => {
          perimeter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/perimeters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/perimeters?page=0&size=20>; rel="last",<http://localhost/api/perimeters?page=0&size=20>; rel="first"',
              },
              body: [perimeter],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(perimeterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Perimeter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('perimeter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', perimeterPageUrlPattern);
      });

      it('edit button click should load edit Perimeter page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Perimeter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', perimeterPageUrlPattern);
      });

      it('edit button click should load edit Perimeter page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Perimeter');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', perimeterPageUrlPattern);
      });

      it('last delete button click should delete instance of Perimeter', () => {
        cy.intercept('GET', '/api/perimeters/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('perimeter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', perimeterPageUrlPattern);

        perimeter = undefined;
      });
    });
  });

  describe('new Perimeter page', () => {
    beforeEach(() => {
      cy.visit(`${perimeterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Perimeter');
    });

    it('should create an instance of Perimeter', () => {
      cy.get(`[data-cy="namePerimeter"]`).type('Kids Throughway').should('have.value', 'Kids Throughway');

      cy.get(`[data-cy="descPerimeter"]`).type('transmitter Fresh deposit').should('have.value', 'transmitter Fresh deposit');

      cy.get(`[data-cy="activity"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        perimeter = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', perimeterPageUrlPattern);
    });
  });
});
