/**
 * Created by qi on 2017/10/17.
 */
import fetch from '@/utils/fetch'

export function giveAnswerFeedback (answerId, isGood) {
  return fetch({
    url: '/answer/giveFeedback',
    method: 'put',
    params: {
      answerId: answerId,
      isGood: isGood
    }
  })
}

export function getQuestionSingleAnswer (questionId, answerId) {
  return fetch({
    url: '/answer/' + questionId + '/' + answerId,
    method: 'get'
  })
}

export function acceptAnswer (answerId) {
  return fetch({
    url: '/answer/acceptAnswer',
    method: 'put',
    params: {
      answerId: answerId
    }
  })
}

export function getUserAnswerCount (userId) {
  return fetch({
    url: '/answer/getUserAnswerCount/' + userId,
    method: 'get'
  })
}

export function getUserAnswerByQuestionId (questionId) {
  return fetch({
    url: '/answer/getUserAnswerByQuestionId',
    method: 'get',
    params: {
      questionId: questionId
    }
  })
}

export function unHiddenAnswer (answerId) {
  return fetch({
    url: '/answer/unHiddenAnswer',
    method: 'put',
    params: {
      answerId: answerId
    }
  })
}

export function hiddenAnswer (answerId) {
  return fetch({
    url: '/answer/hiddenAnswer',
    method: 'put',
    params: {
      answerId: answerId
    }
  })
}

export function modifyAnswer (answerId, answerContent) {
  const data = {
    answerContent: answerContent
  }
  return fetch({
    url: '/answer/modify/' + answerId,
    method: 'put',
    data
  })
}

export function getHiddenAnswersSortedByDateTime (userId, currentPage, pageSize) {
  return fetch({
    url: '/hiddenAnswersSortedByDateTime/' + userId,
    method: 'get',
    params: {
      currentPage: currentPage,
      pageSize: pageSize
    }
  })
}

export function getHiddenAnswersSortedByThumbsUpCount (userId, currentPage, pageSize) {
  return fetch({
    url: '/hiddenAnswersSortedByThumbsUpCount/' + userId,
    method: 'get',
    params: {
      currentPage: currentPage,
      pageSize: pageSize
    }
  })
}
