/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IUserUpdateComponent from '@/entities/i-user/i-user-update.vue';
import IUserClass from '@/entities/i-user/i-user-update.component';
import IUserService from '@/entities/i-user/i-user.service';

import IUserRoleService from '@/entities/i-user-role/i-user-role.service';
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
  describe('IUser Management Update Component', () => {
    let wrapper: Wrapper<IUserClass>;
    let comp: IUserClass;
    let iUserServiceStub: SinonStubbedInstance<IUserService>;

    beforeEach(() => {
      iUserServiceStub = sinon.createStubInstance<IUserService>(IUserService);

      wrapper = shallowMount<IUserClass>(IUserUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iUserService: () => iUserServiceStub,
          alertService: () => new AlertService(),

          iUserRoleService: () =>
            sinon.createStubInstance<IUserRoleService>(IUserRoleService, {
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
        comp.iUser = entity;
        iUserServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iUserServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iUser = entity;
        iUserServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iUserServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIUser = { id: 123 };
        iUserServiceStub.find.resolves(foundIUser);
        iUserServiceStub.retrieve.resolves([foundIUser]);

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
