/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import UserClientDetailComponent from '@/entities/user-client/user-client-details.vue';
import UserClientClass from '@/entities/user-client/user-client-details.component';
import UserClientService from '@/entities/user-client/user-client.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('UserClient Management Detail Component', () => {
    let wrapper: Wrapper<UserClientClass>;
    let comp: UserClientClass;
    let userClientServiceStub: SinonStubbedInstance<UserClientService>;

    beforeEach(() => {
      userClientServiceStub = sinon.createStubInstance<UserClientService>(UserClientService);

      wrapper = shallowMount<UserClientClass>(UserClientDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { userClientService: () => userClientServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundUserClient = { id: 123 };
        userClientServiceStub.find.resolves(foundUserClient);

        // WHEN
        comp.retrieveUserClient(123);
        await comp.$nextTick();

        // THEN
        expect(comp.userClient).toBe(foundUserClient);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundUserClient = { id: 123 };
        userClientServiceStub.find.resolves(foundUserClient);

        // WHEN
        comp.beforeRouteEnter({ params: { userClientId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.userClient).toBe(foundUserClient);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
