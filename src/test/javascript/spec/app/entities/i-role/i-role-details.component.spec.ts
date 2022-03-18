/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import IRoleDetailComponent from '@/entities/i-role/i-role-details.vue';
import IRoleClass from '@/entities/i-role/i-role-details.component';
import IRoleService from '@/entities/i-role/i-role.service';
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
  describe('IRole Management Detail Component', () => {
    let wrapper: Wrapper<IRoleClass>;
    let comp: IRoleClass;
    let iRoleServiceStub: SinonStubbedInstance<IRoleService>;

    beforeEach(() => {
      iRoleServiceStub = sinon.createStubInstance<IRoleService>(IRoleService);

      wrapper = shallowMount<IRoleClass>(IRoleDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iRoleService: () => iRoleServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundIRole = { id: 123 };
        iRoleServiceStub.find.resolves(foundIRole);

        // WHEN
        comp.retrieveIRole(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iRole).toBe(foundIRole);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIRole = { id: 123 };
        iRoleServiceStub.find.resolves(foundIRole);

        // WHEN
        comp.beforeRouteEnter({ params: { iRoleId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iRole).toBe(foundIRole);
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
