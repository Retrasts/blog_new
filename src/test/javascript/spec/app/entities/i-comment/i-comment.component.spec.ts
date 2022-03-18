/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ICommentComponent from '@/entities/i-comment/i-comment.vue';
import ICommentClass from '@/entities/i-comment/i-comment.component';
import ICommentService from '@/entities/i-comment/i-comment.service';
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
  describe('IComment Management Component', () => {
    let wrapper: Wrapper<ICommentClass>;
    let comp: ICommentClass;
    let iCommentServiceStub: SinonStubbedInstance<ICommentService>;

    beforeEach(() => {
      iCommentServiceStub = sinon.createStubInstance<ICommentService>(ICommentService);
      iCommentServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ICommentClass>(ICommentComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          iCommentService: () => iCommentServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      iCommentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllIComments();
      await comp.$nextTick();

      // THEN
      expect(iCommentServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iComments[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      iCommentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(iCommentServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iComments[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      iCommentServiceStub.retrieve.reset();
      iCommentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(iCommentServiceStub.retrieve.callCount).toEqual(2);
      expect(comp.page).toEqual(1);
      expect(comp.iComments[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      iCommentServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(iCommentServiceStub.retrieve.callCount).toEqual(1);

      comp.removeIComment();
      await comp.$nextTick();

      // THEN
      expect(iCommentServiceStub.delete.called).toBeTruthy();
      expect(iCommentServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
