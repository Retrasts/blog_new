/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IClassifyComponent from '@/entities/i-classify/i-classify.vue';
import IClassifyClass from '@/entities/i-classify/i-classify.component';
import IClassifyService from '@/entities/i-classify/i-classify.service';
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
  describe('IClassify Management Component', () => {
    let wrapper: Wrapper<IClassifyClass>;
    let comp: IClassifyClass;
    let iClassifyServiceStub: SinonStubbedInstance<IClassifyService>;

    beforeEach(() => {
      iClassifyServiceStub = sinon.createStubInstance<IClassifyService>(IClassifyService);
      iClassifyServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<IClassifyClass>(IClassifyComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          iClassifyService: () => iClassifyServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      iClassifyServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllIClassifys();
      await comp.$nextTick();

      // THEN
      expect(iClassifyServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iClassifies[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      iClassifyServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(iClassifyServiceStub.retrieve.callCount).toEqual(1);

      comp.removeIClassify();
      await comp.$nextTick();

      // THEN
      expect(iClassifyServiceStub.delete.called).toBeTruthy();
      expect(iClassifyServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
