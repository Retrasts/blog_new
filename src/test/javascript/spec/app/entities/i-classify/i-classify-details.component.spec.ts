/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import IClassifyDetailComponent from '@/entities/i-classify/i-classify-details.vue';
import IClassifyClass from '@/entities/i-classify/i-classify-details.component';
import IClassifyService from '@/entities/i-classify/i-classify.service';
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
  describe('IClassify Management Detail Component', () => {
    let wrapper: Wrapper<IClassifyClass>;
    let comp: IClassifyClass;
    let iClassifyServiceStub: SinonStubbedInstance<IClassifyService>;

    beforeEach(() => {
      iClassifyServiceStub = sinon.createStubInstance<IClassifyService>(IClassifyService);

      wrapper = shallowMount<IClassifyClass>(IClassifyDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iClassifyService: () => iClassifyServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundIClassify = { id: 123 };
        iClassifyServiceStub.find.resolves(foundIClassify);

        // WHEN
        comp.retrieveIClassify(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iClassify).toBe(foundIClassify);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIClassify = { id: 123 };
        iClassifyServiceStub.find.resolves(foundIClassify);

        // WHEN
        comp.beforeRouteEnter({ params: { iClassifyId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iClassify).toBe(foundIClassify);
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
