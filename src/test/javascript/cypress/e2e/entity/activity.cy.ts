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

describe('Activity e2e test', () => {
  const activityPageUrl = '/activity';
  const activityPageUrlPattern = new RegExp('/activity(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const activitySample = { nameActivity: 'reboot Grocery' };

  let activity;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/activities/*').as('deleteEntityRequest');
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

  it('Activities menu should load Activities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Activity').should('exist');
    cy.url().should('match', activityPageUrlPattern);
  });

  describe('Activity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(activityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Activity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/activity/new$'));
        cy.getEntityCreateUpdateHeading('Activity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/activities',
          body: activitySample,
        }).then(({ body }) => {
          activity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/activities?page=0&size=20>; rel="last",<http://localhost/api/activities?page=0&size=20>; rel="first"',
              },
              body: [activity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(activityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Activity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('activity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });

      it('edit button click should load edit Activity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });

      it('edit button click should load edit Activity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });

      it('last delete button click should delete instance of Activity', () => {
        cy.intercept('GET', '/api/activities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('activity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);

        activity = undefined;
      });
    });
  });

  describe('new Activity page', () => {
    beforeEach(() => {
      cy.visit(`${activityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Activity');
    });

    it('should create an instance of Activity', () => {
      cy.get(`[data-cy="nameActivity"]`).type('generating Legacy Pound').should('have.value', 'generating Legacy Pound');

      cy.get(`[data-cy="descActivity"]`).type('Sausages').should('have.value', 'Sausages');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        activity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', activityPageUrlPattern);
    });
  });
});
