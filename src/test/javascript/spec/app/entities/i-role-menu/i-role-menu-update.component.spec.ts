/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IRoleMenuUpdateComponent from '@/entities/i-role-menu/i-role-menu-update.vue';
import IRoleMenuClass from '@/entities/i-role-menu/i-role-menu-update.component';
import IRoleMenuService from '@/entities/i-role-menu/i-role-menu.service';

import IRoleService from '@/entities/i-role/i-role.service';

import IMenuService from '@/entities/i-menu/i-menu.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('IRoleMenu Management Update Component', () => {
    let wrapper: Wrapper<IRoleMenuClass>;
    let comp: IRoleMenuClass;
    let iRoleMenuServiceStub: SinonStubbedInstance<IRoleMenuService>;

    beforeEach(() => {
      iRoleMenuServiceStub = sinon.createStubInstance<IRoleMenuService>(IRoleMenuService);

      wrapper = shallowMount<IRoleMenuClass>(IRoleMenuUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iRoleMenuService: () => iRoleMenuServiceStub,
          alertService: () => new AlertService(),

          iRoleService: () =>
            sinon.createStubInstance<IRoleService>(IRoleService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          iMenuService: () =>
            sinon.createStubInstance<IMenuService>(IMenuService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.iRoleMenu = entity;
        iRoleMenuServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iRoleMenuServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iRoleMenu = entity;
        iRoleMenuServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iRoleMenuServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIRoleMenu = { id: 123 };
        iRoleMenuServiceStub.find.resolves(foundIRoleMenu);
        iRoleMenuServiceStub.retrieve.resolves([foundIRoleMenu]);

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
