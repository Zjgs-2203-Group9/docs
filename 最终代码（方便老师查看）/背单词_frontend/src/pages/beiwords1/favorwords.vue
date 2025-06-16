<template>
  <view class="container" :style="{minHeight: `${mainHeight}px`}">
    <!-- 导航栏 -->
    <u-navbar
      title="我的单词本"
      :bgColor="themeColor"
      placeholder
      backIconColor="#fff"
      @leftClick="gotoIndex"
    ></u-navbar>

    <!-- 内容区域 -->
    <view class="content">
      <!-- 搜索框 -->
      <u-search
        v-model="keyword"
        placeholder="搜索单词"
        height="50"
        @search="handleSearch"
        bgColor="#fff"
      ></u-search>

      <!-- 单词列表 -->
      <view class="word-list">
        <view 
          v-for="(word, index) in filteredList" 
          :key="word.word_id"
          class="word-item"
		  style="margin-top: 20rpx;"
        >
		<view class="word-content"> 
          <view class="word-header">
            <text class="word-name">{{ word.word_name }}</text>
            <view class="word-actions">
				<img
				      style="width: 20px; height: 20px; vertical-align: middle"
				      class="word-sheng"
					  @click="playSound(word.word_name)"
				      src="https://itlifetime.com/resources/xiaochengxu/声音.png" />
              <u-icon
                name="heart-fill"
                color="#ff4444"
                @click="removeFromFavor(word.word_id, index)"
              ></u-icon>
            </view>
          </view>
          <text class="pronunciation">{{ word.word_pronunciation }}</text>
          <text class="meaning">{{ word.word_meaning }}</text>
        </view>
		</view>

        <!-- 空状态 -->
        <u-empty
          v-if="filteredList.length === 0"
          mode="list"
          text="暂无收藏单词"
        ></u-empty>
      </view>
    </view>
  </view>
</template>

<script>
import { request } from '../common/request';
import { theme_color } from "../common/common.js";

export default {
  data() {
    return {
      themeColor: theme_color,
      mainHeight: 0,
      keyword: '',
      wordList: [],    // 完整数据
      filteredList: [] // 过滤后数据
    };
  },
  onLoad() {
    this.initPage();
  },
  methods: {
    async initPage() {
      this.mainHeight = wx.getStorageSync("mainHeight");
      await this.loadFavoriteWords();
    },

    // 加载收藏单词
    async loadFavoriteWords() {
      try {
        wx.showLoading({ title: '加载中' });
        
        const res = await request({
          url: '/words/favor',
          method: 'GET',
          needAuth: true
        });

        if (res.code === 1) {
          this.wordList = res.extra || [];
          this.filteredList = [...this.wordList];
        }
      } catch (error) {
        console.error('加载失败:', error);
        wx.showToast({ title: '加载失败', icon: 'none' });
      } finally {
        wx.hideLoading();
      }
    },

    // 搜索功能
    handleSearch() {
      if (!this.keyword) {
        this.filteredList = [...this.wordList];
        return;
      }
      
      const keyword = this.keyword.toLowerCase();
      this.filteredList = this.wordList.filter(word => 
        word.word_name.toLowerCase().includes(keyword) ||
        word.word_meaning.toLowerCase().includes(keyword)
      );
    },

    // 移除收藏
    async removeFromFavor(wordId, index) {
      try {
        const res = await request({
          url: '/words/favor',
          method: 'DELETE',
          data: { word_id: wordId },
          needAuth: true
        });

        if (res.code === 1) {
          this.wordList.splice(index, 1);
          this.filteredList.splice(index, 1);
          wx.showToast({ title: '已移除收藏' });
        }
      } catch (error) {
        wx.showToast({ title: '操作失败', icon: 'none' });
      }
    },

    // 语音播放
    playSound(wordName) {
      const innerAudioContext = wx.createInnerAudioContext();
      innerAudioContext.src = `http://dict.youdao.com/dictvoice?audio=${wordName}`;
      innerAudioContext.play();
    },

  }
};
</script>

<style lang="scss" scoped>
.container {
  background: #f8f8f8;
}

.content {
  padding: 20rpx;
}


.word-item {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);

  .word-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;

    .word-name {
      font-size: 34rpx;
      color: #333;
      font-weight: 500;
    }

    .word-actions {
      display: flex;
      gap: 30rpx;

      .u-icon {
        padding: 10rpx;
      }
    }
  }

  .pronunciation {
    display: block;
    font-size: 28rpx;
    color: #7ecdf7;
    margin-bottom: 12rpx;
  }

  .meaning {
    font-size: 30rpx;
    color: #666;
    line-height: 1.6;
  }
}
</style>