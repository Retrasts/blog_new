/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import IUserRoleDetailComponent from '@/entities/i-user-role/i-user-role-details.vue';
import IUserRoleClass from '@/entities/i-user-role/i-user-role-details.component';
import IUserRoleService from '@/entities/i-user-role/i-user-role.service';
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
  describe('IUserRole Management Detail Component', () => {
    let wrapper: Wrapper<IUserRoleClass>;
    let comp: IUserRoleClass;
    let iUserRoleServiceStub: SinonStubbedInstance<IUserRoleService>;

    beforeEach(() => {
      iUserRoleServiceStub = sinon.createStubInstance<IUserRoleService>(IUserRoleService);

      wrapper = shallowMount<IUserRoleClass>(IUserRoleDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iUserRoleService: () => iUserRoleServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundIUserRole = { id: 123 };
        iUserRoleServiceStub.find.resolves(foundIUserRole);

        // WHEN
        comp.retrieveIUserRole(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iUserRole).toBe(foundIUserRole);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIUserRole = { id: 123 };
        iUserRoleServiceStub.find.resolves(foundIUserRole);

        // WHEN
        comp.beforeRouteEnter({ params: { iUserRoleId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iUserRole).toBe(foundIUserRole);
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
