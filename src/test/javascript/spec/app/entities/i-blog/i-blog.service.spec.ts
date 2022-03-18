/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/date/filters';
import IBlogService from '@/entities/i-blog/i-blog.service';
import { IBlog } from '@/shared/model/i-blog.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('IBlog Service', () => {
    let service: IBlogService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new IBlogService();
      currentDate = new Date();
      elemDefault = new IBlog(123, 0, 'AAAAAAA', 0, 0, 'AAAAAAA', 0, 0, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createTime: dayjs(currentDate).format(DATE_FORMAT),
            updateTime: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a IBlog', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            createTime: dayjs(currentDate).format(DATE_FORMAT),
            updateTime: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createTime: currentDate,
            updateTime: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a IBlog', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a IBlog', async () => {
        const returnedFromService = Object.assign(
          {
            createUserId: 1,
            title: 'BBBBBB',
            label: 1,
            classify: 1,
            content: 'BBBBBB',
            likes: 1,
            replynumber: 1,
            createTime: dayjs(currentDate).format(DATE_FORMAT),
            updateTime: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createTime: currentDate,
            updateTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a IBlog', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a IBlog', async () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            classify: 1,
            content: 'BBBBBB',
            replynumber: 1,
            updateTime: dayjs(currentDate).format(DATE_FORMAT),
          },
          new IBlog()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createTime: currentDate,
            updateTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a IBlog', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of IBlog', async () => {
        const returnedFromService = Object.assign(
          {
            createUserId: 1,
            title: 'BBBBBB',
            label: 1,
            classify: 1,
            content: 'BBBBBB',
            likes: 1,
            replynumber: 1,
            createTime: dayjs(currentDate).format(DATE_FORMAT),
            updateTime: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createTime: currentDate,
            updateTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of IBlog', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a IBlog', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a IBlog', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
