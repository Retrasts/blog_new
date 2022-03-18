/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import IUserDetailComponent from '@/entities/i-user/i-user-details.vue';
import IUserClass from '@/entities/i-user/i-user-details.component';
import IUserService from '@/entities/i-user/i-user.service';
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
  describe('IUser Management Detail Component', () => {
    let wrapper: Wrapper<IUserClass>;
    let comp: IUserClass;
    let iUserServiceStub: SinonStubbedInstance<IUserService>;

    beforeEach(() => {
      iUserServiceStub = sinon.createStubInstance<IUserService>(IUserService);

      wrapper = shallowMount<IUserClass>(IUserDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iUserService: () => iUserServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundIUser = { id: 123 };
        iUserServiceStub.find.resolves(foundIUser);

        // WHEN
        comp.retrieveIUser(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iUser).toBe(foundIUser);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIUser = { id: 123 };
        iUserServiceStub.find.resolves(foundIUser);

        // WHEN
        comp.beforeRouteEnter({ params: { iUserId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iUser).toBe(foundIUser);
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
