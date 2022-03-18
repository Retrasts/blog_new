/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ILabelComponent from '@/entities/i-label/i-label.vue';
import ILabelClass from '@/entities/i-label/i-label.component';
import ILabelService from '@/entities/i-label/i-label.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
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
  describe('ILabel Management Component', () => {
    let wrapper: Wrapper<ILabelClass>;
    let comp: ILabelClass;
    let iLabelServiceStub: SinonStubbedInstance<ILabelService>;

    beforeEach(() => {
      iLabelServiceStub = sinon.createStubInstance<ILabelService>(ILabelService);
      iLabelServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ILabelClass>(ILabelComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          iLabelService: () => iLabelServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      iLabelServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllILabels();
      await comp.$nextTick();

      // THEN
      expect(iLabelServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iLabels[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      iLabelServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(iLabelServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iLabels[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      iLabelServiceStub.retrieve.reset();
      iLabelServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(iLabelServiceStub.retrieve.callCount).toEqual(2);
      expect(comp.page).toEqual(1);
      expect(comp.iLabels[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      iLabelServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(iLabelServiceStub.retrieve.callCount).toEqual(1);

      comp.removeILabel();
      await comp.$nextTick();

      // THEN
      expect(iLabelServiceStub.delete.called).toBeTruthy();
      expect(iLabelServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
