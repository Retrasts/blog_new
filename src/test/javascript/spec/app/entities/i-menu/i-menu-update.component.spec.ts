/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IMenuUpdateComponent from '@/entities/i-menu/i-menu-update.vue';
import IMenuClass from '@/entities/i-menu/i-menu-update.component';
import IMenuService from '@/entities/i-menu/i-menu.service';

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
  describe('IMenu Management Update Component', () => {
    let wrapper: Wrapper<IMenuClass>;
    let comp: IMenuClass;
    let iMenuServiceStub: SinonStubbedInstance<IMenuService>;

    beforeEach(() => {
      iMenuServiceStub = sinon.createStubInstance<IMenuService>(IMenuService);

      wrapper = shallowMount<IMenuClass>(IMenuUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iMenuService: () => iMenuServiceStub,
          alertService: () => new AlertService(),

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
        comp.iMenu = entity;
        iMenuServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iMenuServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iMenu = entity;
        iMenuServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iMenuServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIMenu = { id: 123 };
        iMenuServiceStub.find.resolves(foundIMenu);
        iMenuServiceStub.retrieve.resolves([foundIMenu]);

        // WHEN
        comp.beforeRouteEnter({ params: { iMenuId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iMenu).toBe(foundIMenu);
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
