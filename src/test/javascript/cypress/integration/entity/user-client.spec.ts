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

describe('UserClient e2e test', () => {
  const userClientPageUrl = '/user-client';
  const userClientPageUrlPattern = new RegExp('/user-client(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userClientSample = {
    idOperator: 12694,
    idUserOperator: 'Plaza digital',
    idTorneo: 75620,
    acumulado: 4710,
    lastReport: '2022-05-26T08:26:47.856Z',
  };

  let userClient: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-clients+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-clients').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-clients/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userClient) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-clients/${userClient.id}`,
      }).then(() => {
        userClient = undefined;
      });
    }
  });

  it('UserClients menu should load UserClients page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-client');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserClient').should('exist');
    cy.url().should('match', userClientPageUrlPattern);
  });

  describe('UserClient page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userClientPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserClient page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-client/new$'));
        cy.getEntityCreateUpdateHeading('UserClient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userClientPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-clients',
          body: userClientSample,
        }).then(({ body }) => {
          userClient = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-clients+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-clients?page=0&size=20>; rel="last",<http://localhost/api/user-clients?page=0&size=20>; rel="first"',
              },
              body: [userClient],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userClientPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserClient page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userClient');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userClientPageUrlPattern);
      });

      it('edit button click should load edit UserClient page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserClient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userClientPageUrlPattern);
      });

      it('last delete button click should delete instance of UserClient', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('userClient').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userClientPageUrlPattern);

        userClient = undefined;
      });
    });
  });

  describe('new UserClient page', () => {
    beforeEach(() => {
      cy.visit(`${userClientPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserClient');
    });

    it('should create an instance of UserClient', () => {
      cy.get(`[data-cy="idOperator"]`).type('15514').should('have.value', '15514');

      cy.get(`[data-cy="idUserOperator"]`).type('Galicia a bricks-and-clicks').should('have.value', 'Galicia a bricks-and-clicks');

      cy.get(`[data-cy="idTorneo"]`).type('36616').should('have.value', '36616');

      cy.get(`[data-cy="acumulado"]`).type('55232').should('have.value', '55232');

      cy.get(`[data-cy="lastReport"]`).type('2022-05-26T00:08').should('have.value', '2022-05-26T00:08');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        userClient = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', userClientPageUrlPattern);
    });
  });
});
