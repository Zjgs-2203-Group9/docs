<template>
  <view
    class="my-uni-app-body"
    :style="{ minHeight: `${mainHeight}px`, background: '#f8f8f8!important' }"
  >
    <u-navbar
      :title="
        noteInfo
          ? `单词本——${noteInfo.name}`
          : wordInfo
          ? wordInfo.name
          : '错误单词'
      "
      :bgColor="color"
      placeholder
      @leftClick="gotoIndex"
    ></u-navbar>
    <view style="padding: 20px 10px 0 10px">
      <u-row :gutter="20" v-if="!isNote || (isNote !== 1 && isNote !== 2)">
        <u-col v-for="(i, index) in fixedTypeList" :span="4" :key="index">
          <view
            @click="doGetWords(i.value)"
            :style="{
              background: type2 == i.value ? '' : color,
              borderRadius: '24px',
              padding: '5px',
              color: type2 == i.value ? '' : '#fff',
              fontSize: '14px',
              border: type2 == i.value ? `1px solid ${color}` : '',
              textAlign: 'center',
            }"
          >
            <text>{{ i.name }}</text>
          </view></u-col
        >
      </u-row>
      <view style="margin: 10px 0 10px 0">
        <u-search
          placeholder="输入单词"
          v-model="keyword"
          height="60"
          :size="size"
          @search="search"
          @custom="search"
        ></u-search
      ></view>
      <u-row :gutter="20">
        <u-col :span="6">
          <u-button
            type="primary"
            icon="edit-pen"
            shape="circle"
            :size="size"
            @click="gotoWordDetails"
            :disabled="!dataList || dataList.length <= 0"
            >前去默写单词</u-button
          >
        </u-col>
        <u-col :span="6">
          <u-button
            type="success"
            icon="grid"
            shape="circle"
            @click="gotoWordDetails1"
            :disabled="!dataList || dataList.length <= 0"
            >前去记忆单词</u-button
          >
        </u-col>
      </u-row>
    </view>
    <view style="padding: 20px 5px 0 5px">
      <view
        v-for="(i, index) in showDataList"
        :key="index"
        style="
          margin-bottom: 10px;
          background: #ffff;
          min-height: 50px;
          border-radius: 5px;
          padding: 5px;
        "
      >
        <u-row style="height: 100%">
          <u-col :span="7">
            <view>
              <text style="color: #7ecdf7; font-size: 20px; font-weight: bold">
                {{ i.word_name }}
              </text>
            </view>
            <view
              ><text style="color: #ababab; font-size: 14px">{{
                i.word_meaning
              }}</text></view
            >
          </u-col>
          <u-col :span="5" style="background: #e1f5f4">
            <u-row :gutter="5" v-if="isNote && isNote == 1">
              <u-col :span="6">
                <view
                  ><text style="font-size: 15px; color: #ababab">{{
                    i.word_pronunciation
                  }}</text></view
                ></u-col
              >

              <u-col :span="3">
                <view @click="playShenyin(i.word_name)">
                  <img
                    style="width: 20px; height: 20px; vertical-align: middle"
                    class="word-sheng"
                    src="https://itlifetime.com/resources/xiaochengxu/声音.png" /></view
              ></u-col>
			  
              <u-col :span="3">
                <view @click="cancelCollectWords(i.word_name, index)">
                  <img
                    style="width: 20px; height: 20px; vertical-align: middle"
                    class="word-sheng"
                    src="https://itlifetime.com/resources/xiaochengxu/爱心.png" /></view
              ></u-col>
            </u-row>
			
            <u-row :gutter="5" v-else>
              <u-col :span="7">
                <view
                  ><text style="font-size: 15px; color: #ababab">{{
                    i.word_pronunciation
                  }}</text></view
                ></u-col
              >
			  <u-col :span="2">
                <view @click="playShenyin(i.word_name)">
                  <img
                    style="width: 20px; height: 20px; vertical-align: middle"
                    class="word-sheng"
                    src="https://itlifetime.com/resources/xiaochengxu/声音.png" /></view
              ></u-col>
			  <u-col :span="3">
			      <view @click="toggleCollect(i)">
			        <u-icon 
			          :name="i.isCollected ? 'heart-fill' : 'heart'"
			          :color="i.isCollected ? '#ff4444' : '#c0c4cc'"
			          size="22"
			        ></u-icon>
			      </view>
			  </u-col>
            </u-row>
          </u-col>
        </u-row>
      </view>
      <!-- <u-collapse>
        <u-collapse-item
          :title="`${index + 1}、${i.word_name}`"
          v-for="(i, index) in dataList"
          :key="index"
        >
          <text class="u-collapse-content">{{ i.word_meaning }}</text>
        </u-collapse-item>
      </u-collapse> -->
    </view>
    <view>
      <u-popup
        :show="show"
        @close="show = false"
        @open="show = true"
        mode="bottom"
      >
        <!-- 默写设置，默写多少个，随机抽查还是默认顺序，单个单词默写时长-->
        <view class="word-set">
          <view style="height: 40px">
            <!-- <input
              type="number"
              :style="{ height: '40px', borderBottom: `1px solid ${color}` }"
              placeholder="请输入要默写的单词数量"
              v-model="wordSet.num"
            /> -->
            <view>
              <tetxt style="font-size: 12px; color: #ababab; margin-bottom: 8px"
                >单词个数(当前词量：{{ sourceDataList.length }})：</tetxt
              >
            </view>

            <!-- <u--input
              placeholder="请输入内容"
              number
              v-model="wordSet.num"
              :style="{ height: '40px', borderBottom: `1px solid ${color}` }"
              clearable
            ></u--input> -->

            <u-number-box
              :min="2"
              integer
              inputWidth="70px"
              :max="sourceDataList.length > 20 ? 20 : sourceDataList.length"
              v-model="wordSet.num"
            ></u-number-box>
          </view>

          <view style="margin-top: 30px">
            <view>
              <tetxt style="font-size: 12px; color: #ababab">单词顺序：</tetxt>
            </view>

            <view
              style="
                background: #fff;
                margin-top: 5px;
                padding: 5px;
                border-radius: 5px;
                line-height: 40px;
              "
              :style="{
                border:
                  wordSet.wordRandom == '顺序' ? `1px solid ${color}` : '',
              }"
            >
              <u-radio-group
                v-model="wordSet.wordRandom"
                iconSize="15px"
                size="15px"
                :activeColor="color"
                iconPlacement="right"
                placement="column"
              >
                <u-radio
                  label="顺序"
                  name="顺序"
                  :labelSize="labelSize"
                ></u-radio>
              </u-radio-group>
            </view>
            <view
              style="
                margin-top: 10px;
                background: #fff;
                padding: 5px;
                border-radius: 5px;
              "
              :style="{
                border:
                  wordSet.wordRandom == '随机' ? `1px solid ${color}` : '',
              }"
            >
              <u-radio-group
                v-model="wordSet.wordRandom"
                iconSize="15px"
                size="15px"
                :activeColor="color"
                iconPlacement="right"
                placement="column"
              >
                <u-radio
                  style="margin-top: 20px"
                  label="随机"
                  name="随机"
                  :labelSize="labelSize"
                ></u-radio> </u-radio-group
            ></view>
          </view>

          <view style="margin-top: 30px" v-if="isWrite">
            <view>
              <tetxt style="font-size: 12px; color: #ababab">默写规则：</tetxt>
            </view>

            <view
              style="
                background: #fff;
                margin-top: 5px;
                padding: 5px;
                border-radius: 5px;
                line-height: 40px;
              "
              :style="{
                border:
                  wordSet.wordDir == '中文→英文' ? `1px solid ${color}` : '',
              }"
            >
              <u-radio-group
                v-model="wordSet.wordDir"
                iconSize="15px"
                size="15px"
                :activeColor="color"
                iconPlacement="right"
                placement="column"
              >
                <u-radio
                  label="中文→英文"
                  name="中文→英文"
                  :labelSize="labelSize"
                ></u-radio>
              </u-radio-group>
            </view>
            <view
              style="
                margin-top: 10px;
                background: #fff;
                padding: 5px;
                border-radius: 5px;
              "
              :style="{
                border:
                  wordSet.wordDir == '英文→中文' ? `1px solid ${color}` : '',
              }"
            >
              <u-radio-group
                v-model="wordSet.wordDir"
                iconSize="15px"
                size="15px"
                :activeColor="color"
                iconPlacement="right"
                placement="column"
              >
                <u-radio
                  style="margin-top: 20px"
                  label="英文→中文"
                  name="英文→中文"
                  :labelSize="labelSize"
                ></u-radio> </u-radio-group
            ></view>
          </view>

          <view style="margin-top: 10px; background: #fff">
            <u-button type="primary" @click="doGotoWordDetails" shape="circle"
              >设置好了</u-button
            >
          </view>
        </view>
      </u-popup>
    </view>
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";
import { request } from '../common/request';

export default {
  setup() {},
  data() {
    return {
	favoriteWords: new Set(), // 使用Set存储已收藏的word_id

      show: false,
      wordSet: { wordRandom: "顺序", num: 0, wordDir: "中文→英文" },
      color: "",
      mainHeight: "",
      labelSize: "15px",
      dataList: [],
      size: "normal",
      sourceDataList: [],
      showDataList: [],
      wordInfo: {},
      isWrite: true,
      keyword: "",
      user: "",
      fixedTypeList: [
        { name: "基础篇", value: 0 },
        { name: "核心篇", value: 1 },
        { name: "进阶篇", value: 2 },
      ],
      type2: 0,
      isNote: 0,
      noteInfo: "",
      wordInfo: "",
      page: { startPage: 1, pageSize: 10 },
    };
  },
  onLoad() {
    this.getInit();
  },
  /**
   * 页面相关事件处理函数--监听用户上拉动作
   */
  onPullDownRefresh() {
    // this.page.startPage += 1;
    // wx.showLoading({
    //   title: "加载中",
    // });
    // this.getNextPage();
  },
  /**
   * 页面下拉触底事件的处理函数
   */
  
  onReachBottom() {
    this.page.startPage += 1;
    //  在当前页面显示导航条加载动画, 是一个状态遮罩;
    wx.showNavigationBarLoading();
    wx.showLoading({
      title: "加载中",
    });
    this.getNextPage();
  },
  methods: {
    getInit() {
      this.color = theme_color;
      this.mainHeight = wx.getStorageSync("mainHeight");
      this.user = JSON.parse(wx.getStorageSync("user"));

      this.isNote = wx.getStorageSync("isNote");

      this.getData();
    },
    getNextPage() {
      //默认只显示前10个，否则太卡
      let index = this.page.startPage * this.page.pageSize;

      if (this.sourceDataList.length > index) {
        for (let i = index; i < index + this.page.pageSize; i++) {
          this.showDataList.push(this.sourceDataList[i]);
        }
        wx.hideLoading();
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();
      }
    },
	// 获取收藏状态
	    async loadFavoriteStatus() {
	      try {
	        const res = await request({
	          url: '/words/favor',
	          method: 'GET',
	          needAuth: true
	        });
	        
	        if (res.code === 1) {
	          this.favoriteWords = new Set(res.extra.map(w => w.word_id));
	          this.updateCollectionStatus();
	        }
	      } catch (error) {
	        console.error('获取收藏状态失败:', error);
	      }
	    },
	async toggleCollect(word) {
	  try {
	    const wasCollected = word.isCollected;
	    
	    // 立即更新UI状态
	    word.isCollected = !wasCollected;
	    this.updateAllCollectionStatus(word.word_id, !wasCollected);
	    
	    // 发送请求
	    const res = await request({
	      url: `/words/favor/${wasCollected ? 'cancelCollect' : 'addCollect'}`,
	      method: 'POST',
	      data: {
	        word_id: word.word_id,
	        word_name: word.word_name
	      },
	      needAuth: true
	    });
	
	    // 失败回滚
	    if (res.code !== 1) {
	      word.isCollected = wasCollected;
	      this.updateAllCollectionStatus(word.word_id, wasCollected);
	      throw new Error(res.msg || '操作失败');
	    }
	
	    // 更新本地缓存
	    if (wasCollected) {
	      this.favoriteWords.delete(word.word_id);
	    } else {
	      this.favoriteWords.add(word.word_id);
	    }
	  } catch (error) {
	    console.error('收藏操作失败:', error);
	    wx.showToast({ title: error.message || '操作失败', icon: 'none' });
	  }
	},
	updateAllCollectionStatus(wordId, isCollected) {
	  this.sourceDataList = this.sourceDataList.map(item => 
	    item.word_id === wordId ? {...item, isCollected} : item
	  );
	  
	  this.showDataList = this.showDataList.map(item => 
	    item.word_id === wordId ? {...item, isCollected} : item
	  );
	},

    async getData() {
      try {
        if (this.isNote == 1) {
          this.noteInfo = JSON.parse(wx.getStorageSync("noteInfo"));
          await this.getCollectWords(this.noteInfo.type);
        } else if (this.isNote == 2) {
          const totalwordList = JSON.parse(wx.getStorageSync("totalwordList"));
          await this.loadFavoriteStatus(); // 先加载收藏状态
          this.initWords(totalwordList);
        } else {
          this.wordInfo = JSON.parse(wx.getStorageSync("wordInfo"));
          await this.getWords(); 
          await this.loadFavoriteStatus(); // 确保收藏状态加载完成
        }
      } catch (error) {
        console.error('初始化失败:', error);
      }
    },
	playShenyin(wordName){
		const innerAudioContext = wx.createInnerAudioContext();
		innerAudioContext.src = `http://dict.youdao.com/dictvoice?audio=${wordName}`;
		innerAudioContext.play();
	},
	// 获取收藏状态
    async loadFavoriteStatus() {
      try {
        const res = await request({
          url: '/words/favor',
          method: 'GET',
          needAuth: true
        });
        
        if (res.code === 1) {
          this.favoriteWords = new Set(res.extra.map(w => w.word_id));
          this.updateCollectionStatus();
        }
      } catch (error) {
        console.error('获取收藏状态失败:', error);
      }
    },
	    // 更新列表项的收藏状态
    updateCollectionStatus() {
      this.showDataList = this.showDataList.map(item => ({
        ...item,
        isCollected: this.favoriteWords.has(item.word_id)
      }));
    },
	// 收藏/取消收藏操作
	    async toggleCollect(word) {
	      try {
	        const isCollecting = !word.isCollected;
	        const action = isCollecting ? 'add' : 'remove';
	        
	        // 立即更新UI状态
	        word.isCollected = isCollecting;
	        this.$forceUpdate();
	
	        // 执行网络请求
	        const res = await request({
	          url: `/words/favor${!isCollecting ? '' : ''}`, // 根据实际API调整
	          method: isCollecting ? 'POST' : 'DELETE',
	          data: { 
	            word_id: word.word_id,
	            word_name: word.word_name
	          },
	          needAuth: true
	        });
	
	        if (res.code !== 1) {
	          // 操作失败时回滚状态
	          word.isCollected = !isCollecting;
	          this.$forceUpdate();
	          throw new Error(res.msg || '操作失败');
	        }
	
	        // 更新本地收藏集合
	        if (isCollecting) {
	          this.favoriteWords.add(word.word_id);
	        } else {
	          this.favoriteWords.delete(word.word_id);
	        }
	
	        wx.showToast({
	          title: isCollecting ? '已加入收藏' : '已取消收藏',
	          icon: 'none'
	        });
	      } catch (error) {
	        console.error('收藏操作失败:', error);
	        wx.showToast({
	          title: error.message || '操作失败',
	          icon: 'none'
	        });
	      }
	    },
    cancelCollectWords(wordName, index) {
      let http = common_http + "/cancelCollectWords";
      let params = {};
      params.ephemeralParam = {
        user_id: this.user.id,
        word_name: wordName,
        collect_type: this.noteInfo.type,
      };
      let _this = this;
      wx.request({
        method: "post",
        url: http,
        data: {
          params: JSON.stringify(params),
        },
        header: {
          "content-type": "application/x-www-form-urlencoded",
        },
        success(res) {
          wx.showToast({
            title: "移除成功！",
            icon: "none",
          });
          //刷新下
          _this.getCollectWords(_this.noteInfo.type);
        },
      });
    },
    initWords(data) {
      // 合并收藏状态到数据
      this.sourceDataList = data.map(item => ({
        ...item,
        isCollected: this.favoriteWords.has(item.word_id)
      }));
      
      // 分页逻辑保持不变
      this.showDataList = [];
      let size = Math.min(data.length, this.page.pageSize);
      for (let i = 0; i < size; i++) {
        this.showDataList.push(this.sourceDataList[i]);
      }
      this.dataList = this.showDataList;
    },
    // 修改doGetWords方法
    async doGetWords(type2) {
      try {
        this.type2 = type2;
        wx.showLoading({ title: '加载中' });
        this.favoriteWords.clear();
        // 并行请求数据和收藏状态
        const [wordsData] = await Promise.all([
          this.getWords(), // 你的Redis数据获取方法
          this.loadFavoriteStatus()
        ]);
        
        this.initWords(wordsData);
      } finally {
        wx.hideLoading();
      }
    },
    async getWords() {
      try {
        wx.showLoading({ title: "加载中" });
        
        const res = await request({
          url: `/words/collect?type=${this.type2}`,
          needAuth: true
        });
    
        if (res.code === 1) {
          this.initWords(res.extra);
          wx.setStorageSync("totalwordList", JSON.stringify(res.extra));
        } else {
          wx.showToast({ title: res.msg || '请求失败', icon: 'none' });
        }
      } catch (err) {
        console.error('获取单词失败:', err);
      } finally {
        wx.hideLoading();
      }
    },

    getWordSet() {
      if (wx.getStorageSync("wordSet")) {
        this.wordSet = JSON.parse(wx.getStorageSync("wordSet"));
        console.log(this.wordSet.num);
        if (this.wordSet.num > this.sourceDataList.length) {
          this.wordSet.num = this.sourceDataList.length;
        }
        console.log(this.sourceDataList.length);
      } else {
        this.wordSet.num = this.sourceDataList.length;
      }
    },
    gotoWordDetails() {
      this.show = true;
      this.wordSet.num =
        this.sourceDataList.length > 20 ? 20 : this.sourceDataList.length;
      this.isWrite = true;
    },
    search() {
      if (this.keyword) {
        let newList = [];
        this.sourceDataList.forEach((element) => {
          if (element.word_name.indexOf(this.keyword) != -1) {
            newList.push(element);
          }
        });
        this.showDataList = newList;
      } else {
        wx.showLoading({
          title: "",
          mask: true,
          success(res) {
            console.log(res);
          },
        });
        setTimeout(() => {
          wx.hideLoading({
            success: (res) => {
              this.showDataList = this.dataList;
            },
          });
        }, 200);
      }
    },
    gotoWordDetails1() {
      this.show = true;
      this.isWrite = false;
    },

    doGotoWordDetails() {
      wx.setStorageSync("wordSet", JSON.stringify(this.wordSet));

      if (this.isWrite) {
        wx.navigateTo({
          url: "/pages/beiwords1/words",
        });
      } else {
        wx.navigateTo({
          url: "/pages/beiwords1/wordSelect",
        });
      }
    },
	
    getCollectWords(type) {
      let http = common_http + "/words/getCollectWords";
      let params = {};
      params.ephemeralParam = type;
      params.ephemeralParam1 = this.user.id;

      let _this = this;
      wx.request({
        method: "post",
        url: http,
        data: {
          params: JSON.stringify(params),
        },
        header: {
          "content-type": "application/x-www-form-urlencoded",
        },
        success(res) {
          wx.setStorageSync("wordList", JSON.stringify(res.data.extra));

          _this.initWords(res.data.extra);
        },
      });
    },
  },
};
</script>
<style scoped lang="scss">
.word-sheng {
  transition: transform 0.3s ease; /* 添加变换动画 */
}

.word-sheng:active {
  transform: scale(2); /* 缩放图片 */
}
.word-set {
  height: 400px;
  padding: 20px;
  background: #f8f8f8;

  ::v-deep .u-radio-label--right {
    height: 30px;
    line-height: 30px;
  }
}
</style>
