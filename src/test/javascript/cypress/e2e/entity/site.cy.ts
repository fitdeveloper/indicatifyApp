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

describe('Site e2e test', () => {
  const sitePageUrl = '/site';
  const sitePageUrlPattern = new RegExp('/site(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const siteSample = { nameSite: 'pink', addressSite: 'Orchestrator hacking Garden' };

  let site;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sites+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sites').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sites/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (site) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sites/${site.id}`,
      }).then(() => {
        site = undefined;
      });
    }
  });

  it('Sites menu should load Sites page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('site');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Site').should('exist');
    cy.url().should('match', sitePageUrlPattern);
  });

  describe('Site page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sitePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Site page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/site/new$'));
        cy.getEntityCreateUpdateHeading('Site');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sitePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sites',
          body: siteSample,
        }).then(({ body }) => {
          site = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sites+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sites?page=0&size=20>; rel="last",<http://localhost/api/sites?page=0&size=20>; rel="first"',
              },
              body: [site],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(sitePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Site page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('site');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sitePageUrlPattern);
      });

      it('edit button click should load edit Site page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Site');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sitePageUrlPattern);
      });

      it('edit button click should load edit Site page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Site');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sitePageUrlPattern);
      });

      it('last delete button click should delete instance of Site', () => {
        cy.intercept('GET', '/api/sites/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('site').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sitePageUrlPattern);

        site = undefined;
      });
    });
  });

  describe('new Site page', () => {
    beforeEach(() => {
      cy.visit(`${sitePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Site');
    });

    it('should create an instance of Site', () => {
      cy.get(`[data-cy="nameSite"]`).type('mobile').should('have.value', 'mobile');

      cy.get(`[data-cy="addressSite"]`).type('black').should('have.value', 'black');

      cy.get(`[data-cy="descSite"]`).type('Mexico').should('have.value', 'Mexico');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        site = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', sitePageUrlPattern);
    });
  });
});
