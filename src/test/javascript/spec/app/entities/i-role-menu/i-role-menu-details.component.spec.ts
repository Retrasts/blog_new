/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import IRoleMenuDetailComponent from '@/entities/i-role-menu/i-role-menu-details.vue';
import IRoleMenuClass from '@/entities/i-role-menu/i-role-menu-details.component';
import IRoleMenuService from '@/entities/i-role-menu/i-role-menu.service';
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
  describe('IRoleMenu Management Detail Component', () => {
    let wrapper: Wrapper<IRoleMenuClass>;
    let comp: IRoleMenuClass;
    let iRoleMenuServiceStub: SinonStubbedInstance<IRoleMenuService>;

    beforeEach(() => {
      iRoleMenuServiceStub = sinon.createStubInstance<IRoleMenuService>(IRoleMenuService);

      wrapper = shallowMount<IRoleMenuClass>(IRoleMenuDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iRoleMenuService: () => iRoleMenuServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundIRoleMenu = { id: 123 };
        iRoleMenuServiceStub.find.resolves(foundIRoleMenu);

        // WHEN
        comp.retrieveIRoleMenu(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iRoleMenu).toBe(foundIRoleMenu);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIRoleMenu = { id: 123 };
        iRoleMenuServiceStub.find.resolves(foundIRoleMenu);

        // WHEN
        comp.beforeRouteEnter({ params: { iRoleMenuId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iRoleMenu).toBe(foundIRoleMenu);
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
