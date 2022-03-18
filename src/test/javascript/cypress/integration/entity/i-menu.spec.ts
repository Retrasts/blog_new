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

describe('IMenu e2e test', () => {
  const iMenuPageUrl = '/i-menu';
  const iMenuPageUrlPattern = new RegExp('/i-menu(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iMenuSample = { url: 'https://嘉懿.biz' };

  let iMenu: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-menus+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-menus').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-menus/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iMenu) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-menus/${iMenu.id}`,
      }).then(() => {
        iMenu = undefined;
      });
    }
  });

  it('IMenus menu should load IMenus page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-menu');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IMenu').should('exist');
    cy.url().should('match', iMenuPageUrlPattern);
  });

  describe('IMenu page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iMenuPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IMenu page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-menu/new$'));
        cy.getEntityCreateUpdateHeading('IMenu');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iMenuPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-menus',
          body: iMenuSample,
        }).then(({ body }) => {
          iMenu = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-menus+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iMenu],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iMenuPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IMenu page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iMenu');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iMenuPageUrlPattern);
      });

      it('edit button click should load edit IMenu page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IMenu');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iMenuPageUrlPattern);
      });

      it('last delete button click should delete instance of IMenu', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iMenu').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iMenuPageUrlPattern);

        iMenu = undefined;
      });
    });
  });

  describe('new IMenu page', () => {
    beforeEach(() => {
      cy.visit(`${iMenuPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IMenu');
    });

    it('should create an instance of IMenu', () => {
      cy.get(`[data-cy="url"]`).type('http://炎彬.info').should('have.value', 'http://炎彬.info');

      cy.get(`[data-cy="menuName"]`).type('Pataca').should('have.value', 'Pataca');

      cy.get(`[data-cy="parentId"]`).type('42425').should('have.value', '42425');

      cy.get(`[data-cy="createTime"]`).type('2021-12-31').should('have.value', '2021-12-31');

      cy.get(`[data-cy="updateTime"]`).type('2021-12-30').should('have.value', '2021-12-30');

      cy.get(`[data-cy="createUserId"]`).type('38172').should('have.value', '38172');

      cy.get(`[data-cy="updateUserId"]`).type('47924').should('have.value', '47924');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iMenu = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iMenuPageUrlPattern);
    });
  });
});
