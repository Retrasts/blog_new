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

describe('IRoleMenu e2e test', () => {
  const iRoleMenuPageUrl = '/i-role-menu';
  const iRoleMenuPageUrlPattern = new RegExp('/i-role-menu(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iRoleMenuSample = {};

  let iRoleMenu: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-role-menus+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-role-menus').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-role-menus/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iRoleMenu) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-role-menus/${iRoleMenu.id}`,
      }).then(() => {
        iRoleMenu = undefined;
      });
    }
  });

  it('IRoleMenus menu should load IRoleMenus page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-role-menu');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IRoleMenu').should('exist');
    cy.url().should('match', iRoleMenuPageUrlPattern);
  });

  describe('IRoleMenu page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iRoleMenuPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IRoleMenu page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-role-menu/new$'));
        cy.getEntityCreateUpdateHeading('IRoleMenu');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRoleMenuPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-role-menus',
          body: iRoleMenuSample,
        }).then(({ body }) => {
          iRoleMenu = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-role-menus+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iRoleMenu],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iRoleMenuPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IRoleMenu page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iRoleMenu');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRoleMenuPageUrlPattern);
      });

      it('edit button click should load edit IRoleMenu page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IRoleMenu');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRoleMenuPageUrlPattern);
      });

      it('last delete button click should delete instance of IRoleMenu', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iRoleMenu').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRoleMenuPageUrlPattern);

        iRoleMenu = undefined;
      });
    });
  });

  describe('new IRoleMenu page', () => {
    beforeEach(() => {
      cy.visit(`${iRoleMenuPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IRoleMenu');
    });

    it('should create an instance of IRoleMenu', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iRoleMenu = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iRoleMenuPageUrlPattern);
    });
  });
});
