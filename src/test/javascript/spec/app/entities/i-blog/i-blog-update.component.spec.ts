/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IBlogUpdateComponent from '@/entities/i-blog/i-blog-update.vue';
import IBlogClass from '@/entities/i-blog/i-blog-update.component';
import IBlogService from '@/entities/i-blog/i-blog.service';

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
  describe('IBlog Management Update Component', () => {
    let wrapper: Wrapper<IBlogClass>;
    let comp: IBlogClass;
    let iBlogServiceStub: SinonStubbedInstance<IBlogService>;

    beforeEach(() => {
      iBlogServiceStub = sinon.createStubInstance<IBlogService>(IBlogService);

      wrapper = shallowMount<IBlogClass>(IBlogUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iBlogService: () => iBlogServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.iBlog = entity;
        iBlogServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iBlogServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iBlog = entity;
        iBlogServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iBlogServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIBlog = { id: 123 };
        iBlogServiceStub.find.resolves(foundIBlog);
        iBlogServiceStub.retrieve.resolves([foundIBlog]);

        // WHEN
        comp.beforeRouteEnter({ params: { iBlogId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iBlog).toBe(foundIBlog);
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
