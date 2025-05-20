<template>
  <view class="login-container">
    <view class="login-box">
      <view class="login-header">
        <image class="logo" src="https://itlifetime.com/resources/xiaochengxu/单词.png" />
        <text class="title">欢迎登录</text>
      </view>
      
      <form @submit="handleLogin">
        <view class="input-box">
          <image class="input-icon" src="https://itlifetime.com/resources/xiaochengxu/用户.png" />
          <input 
            :value="username"
            @input="e => username = e.detail.value"
            type="text" 
            placeholder="请输入用户名" 
            class="custom-input"
          />
        </view>
        
        <view class="input-box">
          <image class="input-icon" src="https://s2.loli.net/2025/05/13/cpeUNzwavlimQGE.png" />
          <input 
            :value="password"
            @input="e => password = e.detail.value"
            type="password" 
            placeholder="请输入密码" 
            class="custom-input"
          />
        </view>
        
        <button 
          form-type="submit" 
          class="login-btn"
          :style="{ backgroundColor: themeColor }"
          hover-class="btn-hover"
        >
          立即登录
        </button>
      </form>

      <view class="footer-tip" @tap="handleToRegister()">
        <text>没有账号？</text>
        <text class="link-text">立即注册</text>
      </view>
    </view>
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";

export default {
    data() {
      return {
        username: '',
        password: '',
        themeColor: '#33bcd7'
      }
    },
	methods: {
	    validateLoginForm() {
	      if (!this.username || !this.password) {
	        wx.showToast({ title: '请填写完整信息', icon: 'none' })
	        return false
	      }
	      return true
	    },
	    handleLogin(e) {
			if(this.validateLoginForm()===false)return;
      // 添加加载提示
      wx.showLoading({
        title: '登录中...',
        mask: true
      });

      // 使用 wx.request 替换 axios
      wx.request({
        url: `${common_http}/users/login`,
        method: 'POST',
        data: {
          username: this.username,
          password: this.password
        },
        header: {
          'Content-Type': 'application/json'
        },
        success: (res) => {
          wx.hideLoading();
          if (res.data.code===1) {
            // 微信存储方案
            wx.setStorageSync('token', res.data.extra.token);
            // 微信页面跳转方式
            wx.redirectTo({
              url: '/pages/beiwords/beiwords' // 根据实际路由修改
            });
          } else {
            wx.showToast({
              title: res.data.msg || '登录失败',
              icon: 'none'
            });
          }
        },
        fail: (err) => {
          wx.hideLoading();
          console.error('请求失败:', err);
          wx.showToast({
            title: '网络连接失败',
            icon: 'none'
          });
        }
      });
    },
	    handleToRegister() {
	      wx.navigateTo({ url: '/pages/register/register' })
	    }
	  }
  }
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #f0fcc7 0%, #dff8f5 100%);
}

.login-box {
  background: rgba(255, 255, 255, 0.95);
  width: 85%;
  max-width: 400px;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(5px);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
  
  .logo {
    width: 60px;
    height: 60px;
    margin-bottom: 15px;
    filter: drop-shadow(0 2px 4px rgba(0,0,0,0.1));
  }
  
  .title {
    font-size: 24px;
    color: #333;
    font-weight: bold;
    display: block;
  }
}

.input-box {
  position: relative;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
  background: #f8f8f8;
  border-radius: 10px;
  padding: 8px 15px;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  }
  
  .input-icon {
    width: 20px;
    height: 20px;
    margin-right: 12px;
    opacity: 0.6;
  }
}

.custom-input {
  flex: 1;
  height: 40px;
  font-size: 16px;
  color: #333;
  background: transparent;
  border: none;
  outline: none;
  
  &::placeholder {
    color: #aaa;
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  border: none;
  border-radius: 12px;
  color: white;
  font-size: 16px;
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 20px;
  
  &::after {
    display: none;
  }
}

.btn-hover {
  opacity: 0.9;
  transform: scale(0.98);
}

.footer-tip {
  text-align: center;
  margin-top: 25px;
  font-size: 14px;
  color: #666;
  
  .link-text {
    color: #33bcd7;
    margin-left: 8px;
    text-decoration: underline;
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.logo {
  animation: float 3s ease-in-out infinite;
}
</style>