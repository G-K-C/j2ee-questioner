/**
 * Created by qi on 2017/10/18.
 */
import fetch from '@/utils/fetch'

export function getFollowStatus (questionTypeId) {
  return fetch({
    url: '/questionType/hasFollow',
    method: 'get',
    params: {
      questionTypeId: questionTypeId
    }
  })
}

export function followQuestionType (questionTypeId) {
  return fetch({
    url: '/questionType/follow',
    method: 'put',
    params: {
      questionTypeId: questionTypeId
    }
  })
}

export function unFollowQuestionType (questionTypeId) {
  return fetch({
    url: '/questionType/unFollow',
    method: 'put',
    params: {
      questionTypeId: questionTypeId
    }
  })
}

export function modifyQuestionType (questionTypeId, questionCourse, questionSubject) {
  return fetch({
    url: '/questionType/modifyType',
    method: 'put',
    params: {
      questionTypeId: questionTypeId,
      questionCourse: questionCourse,
      questionSubject: questionSubject
    }
  })
}
export function addQuestionType (questionCourse, questionSubject) {
  return fetch({
    url: '/questionType/addType',
    method: 'post',
    params: {
      questionCourse: questionCourse,
      questionSubject: questionSubject
    }
  })
}
