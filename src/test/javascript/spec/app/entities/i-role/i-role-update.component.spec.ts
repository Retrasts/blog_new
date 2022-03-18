/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IRoleUpdateComponent from '@/entities/i-role/i-role-update.vue';
import IRoleClass from '@/entities/i-role/i-role-update.component';
import IRoleService from '@/entities/i-role/i-role.service';

import IUserRoleService from '@/entities/i-user-role/i-user-role.service';

import IRoleMenuService from '@/entities/i-role-menu/i-role-menu.service';
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
  describe('IRole Management Update Component', () => {
    let wrapper: Wrapper<IRoleClass>;
    let comp: IRoleClass;
    let iRoleServiceStub: SinonStubbedInstance<IRoleService>;

    beforeEach(() => {
      iRoleServiceStub = sinon.createStubInstance<IRoleService>(IRoleService);

      wrapper = shallowMount<IRoleClass>(IRoleUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iRoleService: () => iRoleServiceStub,
          alertService: () => new AlertService(),

          iUserRoleService: () =>
            sinon.createStubInstance<IUserRoleService>(IUserRoleService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          iRoleMenuService: () =>
            sinon.createStubInstance<IRoleMenuService>(IRoleMenuService, {
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
        comp.iRole = entity;
        iRoleServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iRoleServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iRole = entity;
        iRoleServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iRoleServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIRole = { id: 123 };
        iRoleServiceStub.find.resolves(foundIRole);
        iRoleServiceStub.retrieve.resolves([foundIRole]);

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
