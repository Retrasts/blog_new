import { entityItemSelector } from '../../support/commands';
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

describe('IBlog e2e test', () => {
  const iBlogPageUrl = '/i-blog';
  const iBlogPageUrlPattern = new RegExp('/i-blog(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iBlogSample = { createUserId: 7317 };

  let iBlog: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-blogs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-blogs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-blogs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iBlog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-blogs/${iBlog.id}`,
      }).then(() => {
        iBlog = undefined;
      });
    }
  });

  it('IBlogs menu should load IBlogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-blog');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IBlog').should('exist');
    cy.url().should('match', iBlogPageUrlPattern);
  });

  describe('IBlog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iBlogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IBlog page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-blog/new$'));
        cy.getEntityCreateUpdateHeading('IBlog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iBlogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-blogs',
          body: iBlogSample,
        }).then(({ body }) => {
          iBlog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-blogs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iBlog],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iBlogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IBlog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iBlog');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iBlogPageUrlPattern);
      });

      it('edit button click should load edit IBlog page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IBlog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iBlogPageUrlPattern);
      });

      it('last delete button click should delete instance of IBlog', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iBlog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iBlogPageUrlPattern);

        iBlog = undefined;
      });
    });
  });

  describe('new IBlog page', () => {
    beforeEach(() => {
      cy.visit(`${iBlogPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IBlog');
    });

    it('should create an instance of IBlog', () => {
      cy.get(`[data-cy="createUserId"]`).type('8997').should('have.value', '8997');

      cy.get(`[data-cy="title"]`).type('Sausages Somalia action-items').should('have.value', 'Sausages Somalia action-items');

      cy.get(`[data-cy="label"]`).type('70357').should('have.value', '70357');

      cy.get(`[data-cy="classify"]`).type('73571').should('have.value', '73571');

      cy.get(`[data-cy="content"]`).type('wireless methodologies 上海市').should('have.value', 'wireless methodologies 上海市');

      cy.get(`[data-cy="likes"]`).type('5580').should('have.value', '5580');

      cy.get(`[data-cy="replynumber"]`).type('12310').should('have.value', '12310');

      cy.get(`[data-cy="createTime"]`).type('2022-03-17').should('have.value', '2022-03-17');

      cy.get(`[data-cy="updateTime"]`).type('2022-03-17').should('have.value', '2022-03-17');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iBlog = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iBlogPageUrlPattern);
    });
  });
});
