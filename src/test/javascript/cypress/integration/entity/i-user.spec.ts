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

describe('IUser e2e test', () => {
  const iUserPageUrl = '/i-user';
  const iUserPageUrlPattern = new RegExp('/i-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iUserSample = { ip: 'Music Jewelery Shoes' };

  let iUser: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/i-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/i-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/i-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/i-users/${iUser.id}`,
      }).then(() => {
        iUser = undefined;
      });
    }
  });

  it('IUsers menu should load IUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('i-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IUser').should('exist');
    cy.url().should('match', iUserPageUrlPattern);
  });

  describe('IUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IUser page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/i-user/new$'));
        cy.getEntityCreateUpdateHeading('IUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/i-users',
          body: iUserSample,
        }).then(({ body }) => {
          iUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/i-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iUser],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iUser');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserPageUrlPattern);
      });

      it('edit button click should load edit IUser page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserPageUrlPattern);
      });

      it('last delete button click should delete instance of IUser', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iUserPageUrlPattern);

        iUser = undefined;
      });
    });
  });

  describe('new IUser page', () => {
    beforeEach(() => {
      cy.visit(`${iUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IUser');
    });

    it('should create an instance of IUser', () => {
      cy.get(`[data-cy="ip"]`).type('Consultant SSL').should('have.value', 'Consultant SSL');

      cy.get(`[data-cy="username"]`).type('state Computers teal').should('have.value', 'state Computers teal');

      cy.get(`[data-cy="nikename"]`).type('Executive').should('have.value', 'Executive');

      cy.get(`[data-cy="password"]`).type('cyan').should('have.value', 'cyan');

      cy.get(`[data-cy="sex"]`).type('10875').should('have.value', '10875');

      cy.get(`[data-cy="emaile"]`).type('江苏省').should('have.value', '江苏省');

      cy.get(`[data-cy="avatar"]`).type('convergence').should('have.value', 'convergence');

      cy.get(`[data-cy="createTime"]`).type('2021-12-31').should('have.value', '2021-12-31');

      cy.get(`[data-cy="updateTime"]`).type('2021-12-30').should('have.value', '2021-12-30');

      cy.get(`[data-cy="createUserId"]`).type('27669').should('have.value', '27669');

      cy.get(`[data-cy="updateUserId"]`).type('51008').should('have.value', '51008');

      cy.get(`[data-cy="birthday"]`).type('2021-12-30').should('have.value', '2021-12-30');

      cy.get(`[data-cy="company"]`).type('parse').should('have.value', 'parse');

      cy.get(`[data-cy="phone"]`).type('70014').should('have.value', '70014');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iUser = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iUserPageUrlPattern);
    });
  });
});
