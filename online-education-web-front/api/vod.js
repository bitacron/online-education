import request from '@/utils/request'
const api_name = '/eduvod/video'

export default {

  getPlayAuth(vid) {
    return request({
      url: `/eduvod/video/getPlayAuth/${vid}`,
      method: 'get'
    })
  }

}