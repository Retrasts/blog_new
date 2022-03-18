/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IUserRoleUpdateComponent from '@/entities/i-user-role/i-user-role-update.vue';
import IUserRoleClass from '@/entities/i-user-role/i-user-role-update.component';
import IUserRoleService from '@/entities/i-user-role/i-user-role.service';

import IRoleService from '@/entities/i-role/i-role.service';

import IUserService from '@/entities/i-user/i-user.service';
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
  describe('IUserRole Management Update Component', () => {
    let wrapper: Wrapper<IUserRoleClass>;
    let comp: IUserRoleClass;
    let iUserRoleServiceStub: SinonStubbedInstance<IUserRoleService>;

    beforeEach(() => {
      iUserRoleServiceStub = sinon.createStubInstance<IUserRoleService>(IUserRoleService);

      wrapper = shallowMount<IUserRoleClass>(IUserRoleUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iUserRoleService: () => iUserRoleServiceStub,
          alertService: () => new AlertService(),

          iRoleService: () =>
            sinon.createStubInstance<IRoleService>(IRoleService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          iUserService: () =>
            sinon.createStubInstance<IUserService>(IUserService, {
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
        comp.iUserRole = entity;
        iUserRoleServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iUserRoleServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iUserRole = entity;
        iUserRoleServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iUserRoleServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIUserRole = { id: 123 };
        iUserRoleServiceStub.find.resolves(foundIUserRole);
        iUserRoleServiceStub.retrieve.resolves([foundIUserRole]);

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
