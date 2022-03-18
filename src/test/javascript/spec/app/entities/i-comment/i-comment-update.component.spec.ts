/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ICommentUpdateComponent from '@/entities/i-comment/i-comment-update.vue';
import ICommentClass from '@/entities/i-comment/i-comment-update.component';
import ICommentService from '@/entities/i-comment/i-comment.service';

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
  describe('IComment Management Update Component', () => {
    let wrapper: Wrapper<ICommentClass>;
    let comp: ICommentClass;
    let iCommentServiceStub: SinonStubbedInstance<ICommentService>;

    beforeEach(() => {
      iCommentServiceStub = sinon.createStubInstance<ICommentService>(ICommentService);

      wrapper = shallowMount<ICommentClass>(ICommentUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          iCommentService: () => iCommentServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.iComment = entity;
        iCommentServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iCommentServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.iComment = entity;
        iCommentServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iCommentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIComment = { id: 123 };
        iCommentServiceStub.find.resolves(foundIComment);
        iCommentServiceStub.retrieve.resolves([foundIComment]);

        // WHEN
        comp.beforeRouteEnter({ params: { iCommentId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.iComment).toBe(foundIComment);
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
