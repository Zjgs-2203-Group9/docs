// common/request.js
import { common_http } from './common.js';

export const request = (options) => {
  const token = wx.getStorageSync('token');
  
  return new Promise((resolve, reject) => {
    if (!token && options.needAuth) {
      wx.redirectTo({ url: '/pages/login/login' });
      return reject('未登录');
    }

    wx.request({
      url: common_http + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 401) {
          handleTokenExpired();
          return reject('登录过期');
        }
        resolve(res.data);
      },
      fail: (err) => {
        console.error('请求失败:', err);
        wx.showToast({ title: '网络异常', icon: 'none' });
        reject(err);
      }
    });
  });
};

const handleTokenExpired = () => {
  wx.removeStorageSync('token');
  wx.showToast({
    title: '登录已过期',
    icon: 'none',
    complete: () => {
      wx.redirectTo({ url: '/pages/login/login' });
    }
  });
};