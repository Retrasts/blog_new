/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IBlogComponent from '@/entities/i-blog/i-blog.vue';
import IBlogClass from '@/entities/i-blog/i-blog.component';
import IBlogService from '@/entities/i-blog/i-blog.service';
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
  describe('IBlog Management Component', () => {
    let wrapper: Wrapper<IBlogClass>;
    let comp: IBlogClass;
    let iBlogServiceStub: SinonStubbedInstance<IBlogService>;

    beforeEach(() => {
      iBlogServiceStub = sinon.createStubInstance<IBlogService>(IBlogService);
      iBlogServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<IBlogClass>(IBlogComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          iBlogService: () => iBlogServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      iBlogServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllIBlogs();
      await comp.$nextTick();

      // THEN
      expect(iBlogServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iBlogs[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      iBlogServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(iBlogServiceStub.retrieve.called).toBeTruthy();
      expect(comp.iBlogs[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      iBlogServiceStub.retrieve.reset();
      iBlogServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(iBlogServiceStub.retrieve.callCount).toEqual(2);
      expect(comp.page).toEqual(1);
      expect(comp.iBlogs[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      iBlogServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(iBlogServiceStub.retrieve.callCount).toEqual(1);

      comp.removeIBlog();
      await comp.$nextTick();

      // THEN
      expect(iBlogServiceStub.delete.called).toBeTruthy();
      expect(iBlogServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
