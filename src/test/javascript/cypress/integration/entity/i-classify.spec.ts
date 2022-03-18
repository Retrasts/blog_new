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

describe('IClassify e2e test', () => {
  const iClassifyPageUrl = '/i-classify';
  const iClassifyPageUrlPattern = new RegExp('/i-classify(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iClassifySample = { name: '巷 sticky Persistent' };

  let iClassify: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-classifies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-classifies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-classifies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iClassify) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-classifies/${iClassify.id}`,
      }).then(() => {
        iClassify = undefined;
      });
    }
  });

  it('IClassifies menu should load IClassifies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-classify');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IClassify').should('exist');
    cy.url().should('match', iClassifyPageUrlPattern);
  });

  describe('IClassify page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iClassifyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IClassify page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-classify/new$'));
        cy.getEntityCreateUpdateHeading('IClassify');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iClassifyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-classifies',
          body: iClassifySample,
        }).then(({ body }) => {
          iClassify = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-classifies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iClassify],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iClassifyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IClassify page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iClassify');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iClassifyPageUrlPattern);
      });

      it('edit button click should load edit IClassify page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IClassify');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iClassifyPageUrlPattern);
      });

      it('last delete button click should delete instance of IClassify', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iClassify').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iClassifyPageUrlPattern);

        iClassify = undefined;
      });
    });
  });

  describe('new IClassify page', () => {
    beforeEach(() => {
      cy.visit(`${iClassifyPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IClassify');
    });

    it('should create an instance of IClassify', () => {
      cy.get(`[data-cy="name"]`).type('product').should('have.value', 'product');

      cy.get(`[data-cy="alias"]`).type('Executive blue protocol').should('have.value', 'Executive blue protocol');

      cy.get(`[data-cy="description"]`).type('Web parallelism').should('have.value', 'Web parallelism');

      cy.get(`[data-cy="parentId"]`).type('19341').should('have.value', '19341');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iClassify = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iClassifyPageUrlPattern);
    });
  });
});
