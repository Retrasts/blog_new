/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ICommentDetailComponent from '@/entities/i-comment/i-comment-details.vue';
import ICommentClass from '@/entities/i-comment/i-comment-details.component';
import ICommentService from '@/entities/i-comment/i-comment.service';
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
  describe('IComment Management Detail Component', () => {
    let wrapper: Wrapper<ICommentClass>;
    let comp: ICommentClass;
    let iCommentServiceStub: SinonStubbedInstance<ICommentService>;

    beforeEach(() => {
      iCommentServiceStub = sinon.createStubInstance<ICommentService>(ICommentService);

      wrapper = shallowMount<ICommentClass>(ICommentDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { iCommentService: () => iCommentServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundIComment = { id: 123 };
        iCommentServiceStub.find.resolves(foundIComment);

        // WHEN
        comp.retrieveIComment(123);
        await comp.$nextTick();

        // THEN
        expect(comp.iComment).toBe(foundIComment);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundIComment = { id: 123 };
        iCommentServiceStub.find.resolves(foundIComment);

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
