/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IUserRoleComponent from '@/entities/i-user-role/i-user-role.vue';
import IUserRoleClass from '@/entities/i-user-role/i-user-role.component';
import IUserRoleService from '@/entities/i-user-role/i-user-role.service';
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
  describe('IUserRole Management Component', () => {
    let wrapper: Wrapper<IUserRoleClass>;
    let comp: IUserRoleClass;
    let iUserRoleServiceStub: SinonStubbedInstance<IUserRoleService>;

    beforeEach(() => {
      iUserRoleServiceStub = sinon.createStubInstance<IUserRoleService>(IUserRoleService);
      iUserRoleServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<IUserRoleClass>(IUserRoleComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          iUserRoleService: () => iUserRoleServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      iUserRoleServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllIUserRoles();
      await comp.$nextTick();

      // THEN
      expect(iUserRoleServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iUserRoles[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      iUserRoleServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(iUserRoleServiceStub.retrieve.callCount).toEqual(1);

      comp.removeIUserRole();
      await comp.$nextTick();

      // THEN
      expect(iUserRoleServiceStub.delete.called).toBeTruthy();
      expect(iUserRoleServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
