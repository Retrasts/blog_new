/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import IClassifyUpdateComponent from '@/entities/i-classify/i-classify-update.vue';
import IClassifyClass from '@/entities/i-classify/i-classify-update.component';
import IClassifyService from '@/entities/i-classify/i-classify.service';

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
  describe('IClassify Management Update Component', () => {
    let wrapper: Wrapper<IClassifyClass>;
    let comp: IClassifyClass;
    let iClassifyServiceStub: SinonStubbedInstance<IClassifyService>;

    beforeEach(() => {
      iClassifyServiceStub = sinon.createStubInstance<IClassifyService>(IClassifyService);

      wrapper = shallowMount<IClassifyClass>(IClassifyUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iClassifyService: () => iClassifyServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.iClassify = entity;
        iClassifyServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iClassifyServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iClassify = entity;
        iClassifyServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iClassifyServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIClassify = { id: 123 };
        iClassifyServiceStub.find.resolves(foundIClassify);
        iClassifyServiceStub.retrieve.resolves([foundIClassify]);

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
