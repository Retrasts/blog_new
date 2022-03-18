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

describe('IUserRole e2e test', () => {
  const iUserRolePageUrl = '/i-user-role';
  const iUserRolePageUrlPattern = new RegExp('/i-user-role(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iUserRoleSample = {};

  let iUserRole: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-user-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-user-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-user-roles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iUserRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-user-roles/${iUserRole.id}`,
      }).then(() => {
        iUserRole = undefined;
      });
    }
  });

  it('IUserRoles menu should load IUserRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-user-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IUserRole').should('exist');
    cy.url().should('match', iUserRolePageUrlPattern);
  });

  describe('IUserRole page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iUserRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IUserRole page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-user-role/new$'));
        cy.getEntityCreateUpdateHeading('IUserRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserRolePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-user-roles',
          body: iUserRoleSample,
        }).then(({ body }) => {
          iUserRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-user-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iUserRole],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iUserRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IUserRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iUserRole');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserRolePageUrlPattern);
      });

      it('edit button click should load edit IUserRole page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IUserRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserRolePageUrlPattern);
      });

      it('last delete button click should delete instance of IUserRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iUserRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserRolePageUrlPattern);

        iUserRole = undefined;
      });
    });
  });

  describe('new IUserRole page', () => {
    beforeEach(() => {
      cy.visit(`${iUserRolePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IUserRole');
    });

    it('should create an instance of IUserRole', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iUserRole = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iUserRolePageUrlPattern);
    });
  });
});
