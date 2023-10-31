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

describe('KnowledgeDomain e2e test', () => {
  const knowledgeDomainPageUrl = '/knowledge-domain';
  const knowledgeDomainPageUrlPattern = new RegExp('/knowledge-domain(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const knowledgeDomainSample = { nameKnowledgeDomain: 'quantify' };

  let knowledgeDomain;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/knowledge-domains+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/knowledge-domains').as('postEntityRequest');
    cy.intercept('DELETE', '/api/knowledge-domains/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (knowledgeDomain) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/knowledge-domains/${knowledgeDomain.id}`,
      }).then(() => {
        knowledgeDomain = undefined;
      });
    }
  });

  it('KnowledgeDomains menu should load KnowledgeDomains page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('knowledge-domain');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('KnowledgeDomain').should('exist');
    cy.url().should('match', knowledgeDomainPageUrlPattern);
  });

  describe('KnowledgeDomain page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(knowledgeDomainPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create KnowledgeDomain page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/knowledge-domain/new$'));
        cy.getEntityCreateUpdateHeading('KnowledgeDomain');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', knowledgeDomainPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/knowledge-domains',
          body: knowledgeDomainSample,
        }).then(({ body }) => {
          knowledgeDomain = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/knowledge-domains+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/knowledge-domains?page=0&size=20>; rel="last",<http://localhost/api/knowledge-domains?page=0&size=20>; rel="first"',
              },
              body: [knowledgeDomain],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(knowledgeDomainPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details KnowledgeDomain page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('knowledgeDomain');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', knowledgeDomainPageUrlPattern);
      });

      it('edit button click should load edit KnowledgeDomain page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('KnowledgeDomain');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', knowledgeDomainPageUrlPattern);
      });

      it('edit button click should load edit KnowledgeDomain page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('KnowledgeDomain');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', knowledgeDomainPageUrlPattern);
      });

      it('last delete button click should delete instance of KnowledgeDomain', () => {
        cy.intercept('GET', '/api/knowledge-domains/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('knowledgeDomain').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', knowledgeDomainPageUrlPattern);

        knowledgeDomain = undefined;
      });
    });
  });

  describe('new KnowledgeDomain page', () => {
    beforeEach(() => {
      cy.visit(`${knowledgeDomainPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('KnowledgeDomain');
    });

    it('should create an instance of KnowledgeDomain', () => {
      cy.get(`[data-cy="nameKnowledgeDomain"]`).type('Checking Pound').should('have.value', 'Checking Pound');

      cy.get(`[data-cy="descKnowledgeDomain"]`).type('Research Mouse').should('have.value', 'Research Mouse');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        knowledgeDomain = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', knowledgeDomainPageUrlPattern);
    });
  });
});
