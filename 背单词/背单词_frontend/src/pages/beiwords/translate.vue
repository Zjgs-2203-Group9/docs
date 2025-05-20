<template>
  <view
    class="my-uni-app-body"
    :style="{ height: `${mainHeight}px`, background: '#f8f8f8!important' }"
  >
    <u-navbar
      title="我爱背单词"
      :bgColor="themeColor"
      placeholder
      @leftClick="gotoIndex"
    ></u-navbar>
    
    <view style="padding: 40px 10px 0 10px">
      <!-- 输入区域 -->
	<view 
      class="input-container"
      style="
        height: 200px;
        background: #fff;
        padding: 15px;
        border: 2px solid #33bcd7;
        border-radius: 12px;
        margin-bottom: 20px;
      "
    >
      <textarea
        placeholder="输入需要翻译的文本"
        placeholder-class="placeholder-style"
        v-model="content"
        class="translation-textarea"
		maxlength="-1"
      />
    </view>

      <!-- 结果展示 -->
      <view 
            class="result-container"
            style="
              min-height: 140px;
              background: #fff;
              padding: 20px;
              border: 2px solid #33bcd7;
              border-radius: 12px;
            "
          >
            <text class="result-text">{{ res }}</text>
          </view>

      <!-- 操作按钮 -->
      <view style="margin-top: 20px">
        <u-button 
          type="primary" 
          size="medium" 
          @click="translate"
          :customStyle="{ width: '100%' }"
        >
          点击翻译
        </u-button>
      </view>
    </view>
  </view>
</template>

<script>
import { common_http, theme_color } from "../common/common.js";
import { request } from '../common/request';

export default {
  data() {
    return {
      themeColor: "",
      mainHeight: "",
      content: "",
      res: ""
    };
  },
  onLoad() {
    this.getInit();
  },
  methods: {
    getInit() {
      this.themeColor = theme_color;
      this.mainHeight = wx.getStorageSync("mainHeight") + 40;
    },

    translate() {
      if (!this.content.trim()) {
        wx.showToast({ title: '请输入翻译内容', icon: 'none' });
        return;
      }

      request({
        url: "/translate",
        method: "POST",
        data: { text: this.content }
      })
      .then(res => {
        if (res.code === 1) {
          // 直接获取翻译结果
          this.res = res.extra.text || '无翻译结果';
        } else {
          wx.showToast({ title: res.msg || '翻译失败', icon: 'none' });
        }
      })
      .catch(err => {
        console.error('翻译失败:', err);
        wx.showToast({ title: '翻译服务不可用', icon: 'none' });
      });
    }
  }
};
</script>

<style scoped>
/* 输入框样式 */
.input-container {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.translation-textarea {
  width: 100%;
  height: 100%;
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  overflow-wrap: break-word;
  white-space: pre-wrap;
}

/* 结果框样式 */
.result-container {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.result-text {
  font-size: 16px;
  line-height: 1.8;
  color: #444;
  white-space: pre-wrap;
  word-break: break-word;
  display: block;
}

/* 占位符样式 */
.placeholder-style {
  color: #999;
  font-size: 14px;
}

/* 按钮优化 */
.custom-btn {
  font-size: 16px;
  height: 48px;
  letter-spacing: 1px;
}
</style>