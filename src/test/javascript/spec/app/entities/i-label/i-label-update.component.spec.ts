/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ILabelUpdateComponent from '@/entities/i-label/i-label-update.vue';
import ILabelClass from '@/entities/i-label/i-label-update.component';
import ILabelService from '@/entities/i-label/i-label.service';

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
  describe('ILabel Management Update Component', () => {
    let wrapper: Wrapper<ILabelClass>;
    let comp: ILabelClass;
    let iLabelServiceStub: SinonStubbedInstance<ILabelService>;

    beforeEach(() => {
      iLabelServiceStub = sinon.createStubInstance<ILabelService>(ILabelService);

      wrapper = shallowMount<ILabelClass>(ILabelUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iLabelService: () => iLabelServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.iLabel = entity;
        iLabelServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iLabelServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iLabel = entity;
        iLabelServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iLabelServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundILabel = { id: 123 };
        iLabelServiceStub.find.resolves(foundILabel);
        iLabelServiceStub.retrieve.resolves([foundILabel]);

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
