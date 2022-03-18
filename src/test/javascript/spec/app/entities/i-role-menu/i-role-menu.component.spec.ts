/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IRoleMenuComponent from '@/entities/i-role-menu/i-role-menu.vue';
import IRoleMenuClass from '@/entities/i-role-menu/i-role-menu.component';
import IRoleMenuService from '@/entities/i-role-menu/i-role-menu.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('IRoleMenu Management Component', () => {
    let wrapper: Wrapper<IRoleMenuClass>;
    let comp: IRoleMenuClass;
    let iRoleMenuServiceStub: SinonStubbedInstance<IRoleMenuService>;

    beforeEach(() => {
      iRoleMenuServiceStub = sinon.createStubInstance<IRoleMenuService>(IRoleMenuService);
      iRoleMenuServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<IRoleMenuClass>(IRoleMenuComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          iRoleMenuService: () => iRoleMenuServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      iRoleMenuServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllIRoleMenus();
      await comp.$nextTick();

      // THEN
      expect(iRoleMenuServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iRoleMenus[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      iRoleMenuServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(iRoleMenuServiceStub.retrieve.callCount).toEqual(1);

      comp.removeIRoleMenu();
      await comp.$nextTick();

      // THEN
      expect(iRoleMenuServiceStub.delete.called).toBeTruthy();
      expect(iRoleMenuServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
