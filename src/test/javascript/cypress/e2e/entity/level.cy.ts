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

describe('Level e2e test', () => {
  const levelPageUrl = '/level';
  const levelPageUrlPattern = new RegExp('/level(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const levelSample = { nameLevel: 'Group calculating', valueLevel: 'N3' };

  let level;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/levels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/levels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/levels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (level) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/levels/${level.id}`,
      }).then(() => {
        level = undefined;
      });
    }
  });

  it('Levels menu should load Levels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('level');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Level').should('exist');
    cy.url().should('match', levelPageUrlPattern);
  });

  describe('Level page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(levelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Level page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/level/new$'));
        cy.getEntityCreateUpdateHeading('Level');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', levelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/levels',
          body: levelSample,
        }).then(({ body }) => {
          level = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/levels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/levels?page=0&size=20>; rel="last",<http://localhost/api/levels?page=0&size=20>; rel="first"',
              },
              body: [level],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(levelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Level page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('level');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', levelPageUrlPattern);
      });

      it('edit button click should load edit Level page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Level');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', levelPageUrlPattern);
      });

      it('edit button click should load edit Level page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Level');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', levelPageUrlPattern);
      });

      it('last delete button click should delete instance of Level', () => {
        cy.intercept('GET', '/api/levels/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('level').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', levelPageUrlPattern);

        level = undefined;
      });
    });
  });

  describe('new Level page', () => {
    beforeEach(() => {
      cy.visit(`${levelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Level');
    });

    it('should create an instance of Level', () => {
      cy.get(`[data-cy="nameLevel"]`).type('payment Tasty Arkansas').should('have.value', 'payment Tasty Arkansas');

      cy.get(`[data-cy="valueLevel"]`).select('N0');

      cy.get(`[data-cy="descLevel"]`).type('calculate').should('have.value', 'calculate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        level = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', levelPageUrlPattern);
    });
  });
});
