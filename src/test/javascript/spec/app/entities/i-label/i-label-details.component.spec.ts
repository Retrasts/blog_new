/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ILabelDetailComponent from '@/entities/i-label/i-label-details.vue';
import ILabelClass from '@/entities/i-label/i-label-details.component';
import ILabelService from '@/entities/i-label/i-label.service';
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
  describe('ILabel Management Detail Component', () => {
    let wrapper: Wrapper<ILabelClass>;
    let comp: ILabelClass;
    let iLabelServiceStub: SinonStubbedInstance<ILabelService>;

    beforeEach(() => {
      iLabelServiceStub = sinon.createStubInstance<ILabelService>(ILabelService);

      wrapper = shallowMount<ILabelClass>(ILabelDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iLabelService: () => iLabelServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundILabel = { id: 123 };
        iLabelServiceStub.find.resolves(foundILabel);

        // WHEN
        comp.retrieveILabel(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iLabel).toBe(foundILabel);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundILabel = { id: 123 };
        iLabelServiceStub.find.resolves(foundILabel);

        // WHEN
        comp.beforeRouteEnter({ params: { iLabelId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iLabel).toBe(foundILabel);
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
