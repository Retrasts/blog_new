/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import IBlogDetailComponent from '@/entities/i-blog/i-blog-details.vue';
import IBlogClass from '@/entities/i-blog/i-blog-details.component';
import IBlogService from '@/entities/i-blog/i-blog.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('IBlog Management Detail Component', () => {
    let wrapper: Wrapper<IBlogClass>;
    let comp: IBlogClass;
    let iBlogServiceStub: SinonStubbedInstance<IBlogService>;

    beforeEach(() => {
      iBlogServiceStub = sinon.createStubInstance<IBlogService>(IBlogService);

      wrapper = shallowMount<IBlogClass>(IBlogDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iBlogService: () => iBlogServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundIBlog = { id: 123 };
        iBlogServiceStub.find.resolves(foundIBlog);

        // WHEN
        comp.retrieveIBlog(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iBlog).toBe(foundIBlog);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIBlog = { id: 123 };
        iBlogServiceStub.find.resolves(foundIBlog);

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
