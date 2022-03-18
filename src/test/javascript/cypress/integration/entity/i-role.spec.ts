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

describe('IRole e2e test', () => {
  const iRolePageUrl = '/i-role';
  const iRolePageUrlPattern = new RegExp('/i-role(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iRoleSample = { roleName: 'deliver' };

  let iRole: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-roles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-roles/${iRole.id}`,
      }).then(() => {
        iRole = undefined;
      });
    }
  });

  it('IRoles menu should load IRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IRole').should('exist');
    cy.url().should('match', iRolePageUrlPattern);
  });

  describe('IRole page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IRole page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-role/new$'));
        cy.getEntityCreateUpdateHeading('IRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRolePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-roles',
          body: iRoleSample,
        }).then(({ body }) => {
          iRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iRole],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iRole');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRolePageUrlPattern);
      });

      it('edit button click should load edit IRole page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRolePageUrlPattern);
      });

      it('last delete button click should delete instance of IRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iRolePageUrlPattern);

        iRole = undefined;
      });
    });
  });

  describe('new IRole page', () => {
    beforeEach(() => {
      cy.visit(`${iRolePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IRole');
    });

    it('should create an instance of IRole', () => {
      cy.get(`[data-cy="roleName"]`).type('payment Account 辽宁省').should('have.value', 'payment Account 辽宁省');

      cy.get(`[data-cy="remark"]`).type('bluetooth Proactive granular').should('have.value', 'bluetooth Proactive granular');

      cy.get(`[data-cy="createTime"]`).type('2021-12-30').should('have.value', '2021-12-30');

      cy.get(`[data-cy="updateTime"]`).type('2021-12-30').should('have.value', '2021-12-30');

      cy.get(`[data-cy="createUserId"]`).type('67577').should('have.value', '67577');

      cy.get(`[data-cy="updateUserId"]`).type('9948').should('have.value', '9948');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iRole = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iRolePageUrlPattern);
    });
  });
});
