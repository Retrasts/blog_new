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

describe('IComment e2e test', () => {
  const iCommentPageUrl = '/i-comment';
  const iCommentPageUrlPattern = new RegExp('/i-comment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iCommentSample = { createTime: '2022-03-17' };

  let iComment: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-comments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-comments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-comments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iComment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-comments/${iComment.id}`,
      }).then(() => {
        iComment = undefined;
      });
    }
  });

  it('IComments menu should load IComments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-comment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IComment').should('exist');
    cy.url().should('match', iCommentPageUrlPattern);
  });

  describe('IComment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iCommentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IComment page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-comment/new$'));
        cy.getEntityCreateUpdateHeading('IComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iCommentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-comments',
          body: iCommentSample,
        }).then(({ body }) => {
          iComment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-comments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iComment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iCommentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IComment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iComment');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iCommentPageUrlPattern);
      });

      it('edit button click should load edit IComment page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iCommentPageUrlPattern);
      });

      it('last delete button click should delete instance of IComment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iComment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iCommentPageUrlPattern);

        iComment = undefined;
      });
    });
  });

  describe('new IComment page', () => {
    beforeEach(() => {
      cy.visit(`${iCommentPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IComment');
    });

    it('should create an instance of IComment', () => {
      cy.get(`[data-cy="createTime"]`).type('2022-03-17').should('have.value', '2022-03-17');

      cy.get(`[data-cy="createUserId"]`).type('42091').should('have.value', '42091');

      cy.get(`[data-cy="blogId"]`).type('67979').should('have.value', '67979');

      cy.get(`[data-cy="content"]`).type('neural').should('have.value', 'neural');

      cy.get(`[data-cy="likes"]`).type('85313').should('have.value', '85313');

      cy.get(`[data-cy="parentId"]`).type('61520').should('have.value', '61520');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iComment = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iCommentPageUrlPattern);
    });
  });
});
