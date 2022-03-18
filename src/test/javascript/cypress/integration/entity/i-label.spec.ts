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

describe('ILabel e2e test', () => {
  const iLabelPageUrl = '/i-label';
  const iLabelPageUrlPattern = new RegExp('/i-label(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iLabelSample = { name: 'Frozen static Investment' };

  let iLabel: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-labels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-labels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-labels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iLabel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-labels/${iLabel.id}`,
      }).then(() => {
        iLabel = undefined;
      });
    }
  });

  it('ILabels menu should load ILabels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-label');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ILabel').should('exist');
    cy.url().should('match', iLabelPageUrlPattern);
  });

  describe('ILabel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iLabelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ILabel page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-label/new$'));
        cy.getEntityCreateUpdateHeading('ILabel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iLabelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-labels',
          body: iLabelSample,
        }).then(({ body }) => {
          iLabel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-labels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iLabel],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iLabelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ILabel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iLabel');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iLabelPageUrlPattern);
      });

      it('edit button click should load edit ILabel page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ILabel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iLabelPageUrlPattern);
      });

      it('last delete button click should delete instance of ILabel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iLabel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iLabelPageUrlPattern);

        iLabel = undefined;
      });
    });
  });

  describe('new ILabel page', () => {
    beforeEach(() => {
      cy.visit(`${iLabelPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ILabel');
    });

    it('should create an instance of ILabel', () => {
      cy.get(`[data-cy="name"]`).type('monitor Awesome Applications').should('have.value', 'monitor Awesome Applications');

      cy.get(`[data-cy="alias"]`).type('Shoes Computers connecting').should('have.value', 'Shoes Computers connecting');

      cy.get(`[data-cy="description"]`).type('productize Cambridgeshire Israel').should('have.value', 'productize Cambridgeshire Israel');

      cy.get(`[data-cy="parentId"]`).type('11848').should('have.value', '11848');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iLabel = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iLabelPageUrlPattern);
    });
  });
});
