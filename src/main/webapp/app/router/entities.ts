import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const UserClient = () => import('@/entities/user-client/user-client.vue');
// prettier-ignore
const UserClientUpdate = () => import('@/entities/user-client/user-client-update.vue');
// prettier-ignore
const UserClientDetails = () => import('@/entities/user-client/user-client-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'user-client',
      name: 'UserClient',
      component: UserClient,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-client/new',
      name: 'UserClientCreate',
      component: UserClientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-client/:userClientId/edit',
      name: 'UserClientEdit',
      component: UserClientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-client/:userClientId/view',
      name: 'UserClientView',
      component: UserClientDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
