<template>
  <view
    class="my-uni-app-body"
    :style="{ minHeight: `${mainHeight}px`, overflow: 'hidden' }"
  >
    <u-navbar
      :title="wordInfo.name"
      :bgColor="themeColor"
      placeholder
      @leftClick="gotoIndex"
    ></u-navbar>

    <view class="select-words" v-if="!isOver">
      <view style="margin-top: 10px; padding: 0 10px 0 10px">
        <u-row :gutter="20">
          <u-col :span="5">
            <view style="text-align: left; display: flex">
              <img
                style="
                  width: 30px;
                  height: 30px;
                  vertical-align: middle;
                  margin-right: 10px;
                  padding-top: 5px;
                "
                src="https://itlifetime.com/resources/xiaochengxu/答对.png"
              />
              <text style="font-size: 30px; color: red"> {{ rightNum }}</text>
            </view>
          </u-col>
          <u-col :span="3">
            <view>
              <text style="font-size: 25px">
                {{ finishIndex.length }}/{{ wordSet.num }}</text
              >
            </view>
          </u-col>
          <u-col :span="4">
            <view
              style="
                text-align: right;
                display: flex;
                justify-content: flex-end;
              "
            >
              <img
                style="
                  width: 30px;
                  height: 30px;
                  vertical-align: middle;
                  margin-right: 10px;
                "
                src="https://itlifetime.com/resources/xiaochengxu/闹钟.png"
              />
              <u-count-down
                ref="myCountdown"
                @finish="timeFinish"
                :time="time * 1000"
                format="ss"
              ></u-count-down>
            </view>
          </u-col>
        </u-row>
      </view>

      <view style="padding: 20px; margin-top: 30px">
        <view style="text-align: center; margin-bottom: 10px">
          <text style="font-size: 30px">{{
            dataList[index].word_name
          }}</text></view
        >
        <view style="text-align: center; margin-bottom: 20px">
          <!-- <text style="font-size: 14px; color: #ababab">{{
            dataList[index].word_pronunciation
          }}</text>
           -->
          <view
            @click="playShenyin(dataList[index].word_name)"
            style="margin-top: 15px"
          >
            <img
              style="
                width: 20px;
                height: 20px;
                vertical-align: middle;
                margin-right: 4px;
              "
              class="word-sheng"
              src="https://itlifetime.com/resources/xiaochengxu/声音.png"
            />
            <text style="font-size: 15px; color: #ababab">{{
              dataList[index].word_pronunciation
            }}</text></view
          >
        </view>

        <view style="background: #f8f8f8; border-radius: 20px">
          <view
            v-for="(i, index) in options"
            :key="index"
            class="word-option"
            @click="selectOption(i)"
            :style="{
              color:
                currentOption && currentOption.name == i.name ? '#fff' : '',
              background:
                currentOption && currentOption.name == i.name && i.isRight
                  ? themeColor
                  : currentOption && currentOption.name != i.name && i.isRight
                  ? themeColor
                  : currentOption && currentOption.name == i.name && !i.isRight
                  ? '#F1aca7'
                  : '',
            }"
            >
			  <view class="option-content">
			    <text class="option-name">{{ i.name }}</text>
			    <text class="option-meaning">{{ i.option.word_meaning }}</text>
			  </view>
			</view>
          <!-- 
             border:
                currentOption && currentOption.name == i.name && i.isRight
                  ? `1px solid ${themeColor}`
                  : currentOption && currentOption.name != i.name && i.isRight
                  ? `1px solid ${themeColor}`
                  : currentOption && currentOption.name == i.name && !i.isRight
                  ? `1px solid red`
                  : '',
          -->
        </view>
      </view>
    </view>

    <view class="select-words" v-else>
      <view style="text-align: center">
        <view style="margin-top: 20px">
          <img :src="img" style="width: 60px; height: 60px" />
        </view>

        <view style="font-size: 20px; font-weight: bold"
          >本次共学习了{{ finishIndex.length }}个单词</view
        >
        <view style="font-size: 20px; margin-top: 20px">
          <u-row>
            <u-col :span="4"
              ><text style="color: green">正确：{{ rightNum }}</text></u-col
            >
            <u-col :span="4"
              ><text style="color: red"
                >错误：{{ wrongWords.length }}</text
              ></u-col
            >

            <u-col :span="4"
              >得分：{{
                Math.floor((rightNum / finishIndex.length) * 100)
              }}</u-col
            >
          </u-row>
        </view>

        <view style="font-size: 20px; margin-top: 20px">
          <u-button type="primary" @click="reback" icon="eye" size="large">
            再来一遍
          </u-button>
        </view>

        <view style="padding: 10px 10px 0 10px">
          <view>
            <tetxt style="font-size: 12px; color: #ababab">错误单词：</tetxt>
          </view>

          <view
            v-for="(i, index) in wrongWords"
            :key="index"
            style="
              margin-bottom: 10px;
              background: #ffff;
              border-radius: 5px;
              padding: 5px;
            "
          >
            <u-row style="height: 100%">
              <u-col :span="8">
                <view>
                  <text
                    style="color: #7ecdf7; font-size: 20px; font-weight: bold"
                  >
                    {{ i.option.word_name }}
                  </text>
                </view>

                <view
                  ><text>{{ i.option.word_meaning }}</text></view
                >
              </u-col>
              <u-col :span="4" style="background: #e1f5f4">
                <u-row :gutter="5">
                  <u-col :span="9">
                    <view
                      ><text style="font-size: 15px; color: #ababab">{{
                        i.option.word_pronunciation
                      }}</text></view
                    ></u-col
                  ><u-col :span="3">
                    <view @click="playShenyin(i.option.word_name)">
                      <img
                        style="
                          width: 20px;
                          height: 20px;
                          vertical-align: middle;
                        "
                        class="word-sheng"
                        src="https://itlifetime.com/resources/xiaochengxu/声音.png" /></view
                  ></u-col>
                </u-row>
              </u-col>
            </u-row>
          </view>
        </view>
      </view>
    </view>
    <u-popup
      :show="show"
      @close="show = false"
      @open="show = true"
      mode="bottom"
    >
      <view style="background: #f8f8f8; border-radius: 20px">
        <view
          v-for="(i, index) in wordNoteList"
          :key="index"
          style="margin: 10px 0 10px 0; background: #ffff; border-radius: 5px"
        >
          <u-row>
            <u-col :span="9">
              <view style="padding: 10px 0 5px 20px">
                <view>
                  <text style="font-size: 18px; font-weight: bold">
                    {{ i.name }}
                  </text>
                </view>

                <view style="margin-top: 5x"
                  ><text style="font-size: 12px; color: #ababab"
                    >共{{ i.total }}个</text
                  ></view
                ></view
              >
            </u-col>
            <u-col :span="3">
              <view
                style="
                  background: #e1f5f4;
                  text-align: center;
                  padding: 0 !important;
                  margin: auto;
                  height: 65px;
                "
              >
                <view v-if="isCollect[i.type] != 0">
                  <img
                    style="width: 25px; height: 25px; padding-top: 8px"
                    src="https://itlifetime.com/resources/xiaochengxu/喜爱.png"
                  />
                  <view>
                    <text style="font-size: 13px; color: #ababab">
                      已记录
                    </text>
                  </view>
                </view>

                <view v-else @click="collectWords(i.type)">
                  <img
                    style="width: 25px; height: 25px; padding-top: 8px"
                    src="https://itlifetime.com/resources/xiaochengxu/爱心.png"
                  />
                  <view>
                    <text style="font-size: 13px; color: #ababab"> 记录 </text>
                  </view>
                </view>
              </view>
            </u-col>
          </u-row>
        </view></view
      >
    </u-popup>
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";

export default {
  setup() {},
  data() {
    return {
      show: false,

      wordNoteList: [],
      answer: "",
      list: [
        { name: "自考树英语", value: 0 },
        { name: "学科英语", value: 1 },
        { name: "术语英语", value: 2 },
      ],
      show: false,

      valList: [],
      temList: [],
      keyS: "s10",
      keyReplce: "x",
      keyEntText: "确定",
      ShowKeyBoard: false,
      animation: "",
      a1: "test",
      a2: "test1 ",
      showMask: false,
      dataList: [],
      themeColor: "",
      mainHeight: "",
      num: "",
      labelSize: "17px",
      index: 0,
      wordArrays: [],
      span: 1,
      user_answer: "",
      focus_index: 0,
      collect_type: 0,
      user: "",
      successWords: [],
      wrongWords: [],
      isOver: false,
      wordInfo: {},
      rightNum: 0,
      time: 60,
      outTime: false,
      innerAudioContext: {},
      wordyin: [
        {
          name: "empty",
          src: "https://itlifetime.com/resources/xiaochengxu/wordyin/empty.mp3",
        },
      ],
      animationData: {},
      animationData1: {},
      animation1: null,
      isRote: 1,
      animation: null,
      isCollect: {},
      wordSet: "",
      finishIndex: [],

      currentOption: "",
      options: [],
      optionsName: ["A", "B", "C", "D"],
      totalwordList: [],
      img: "/static/success.png",
    };
  },
  onLoad() {
    this.getInit();
  },
  onShow() {
    var animation = uni.createAnimation({}); //创建一个动画实例
    console.log(animation);
    this.animation = animation;
  },
  methods: {
    getInit() {
      this.wordNoteList = JSON.parse(wx.getStorageSync("wordNoteList"));

      this.themeColor = theme_color;
      this.mainHeight = wx.getStorageSync("mainHeight");
      this.wordInfo = JSON.parse(wx.getStorageSync("wordInfo"));

      this.dataList = JSON.parse(wx.getStorageSync("totalwordList"));
      if (!wx.getStorageSync("totalwordList")) {
        this.getWords(this.wordInfo.type);
      } else {
        this.totalwordList = JSON.parse(wx.getStorageSync("totalwordList"));
        this.initOptions(this.totalwordList);
        //初始选项
        if (this.wordSet.wordRandom == "随机") {
          this.index = this.randomNumBoth(0, this.dataList.length);
        }
        this.options = this.createOption(this.index);
      }
      if (wx.getStorageSync("user")) {
        this.user = JSON.parse(wx.getStorageSync("user"));
      }

      this.wordSet = JSON.parse(wx.getStorageSync("wordSet"));
    },
    initOptions(totalDataList) {
      let isNote = wx.getStorageSync("isNote");
      //收藏进来的
      if (isNote && isNote == 1) {
      } else {
        //全部进来的
        this.dataList = totalDataList;
      }

      //初始选项
      if (this.wordSet.wordRandom == "随机") {
        this.index = this.randomNumBoth(0, this.totalwordList.length);
      }
      this.options = this.createOption(this.index);
    },
    //用来生成选项
    getWords(type) {
      let http = common_http + "/getWords";
      let params = {};
      params.ephemeralParam = type;
      let _this = this;
      wx.showLoading({
        title: "单词加载中",
        mask: true,
        success(res) {
          console.log(res);
        },
      });
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
          _this.initOptions(res.data.extra);
          wx.setStorageSync("totalwordList", JSON.stringify(res.data.extra));

          setTimeout(() => {
            wx.hideLoading({
              success: (res) => {},
            });
          }, 200);
        },
      });
    },
    showCollect() {
      this.show = true;
      let http = common_http + "/checkWordIsNote";
      let params = {};
      params.ephemeralParam = this.user.id;
      params.ephemeralParam1 = this.dataList[this.index].word_name;

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
          _this.isCollect = res.data.extra;
        },
      });
    },
	playShenyin(wordName){
		const innerAudioContext = wx.createInnerAudioContext();
		innerAudioContext.src = `http://dict.youdao.com/dictvoice?audio=${wordName}`;
		innerAudioContext.play();
	},
    timeFinish() {
      this.user_answer = "xx";
      const arr = Array.from(this.dataList[this.index].word_name);
      this.wordArrays = [];
      arr.forEach((_element) => {
        this.wordArrays.push({ v: _element });
      });
    },

    randomNumBoth(min, max) {
      var Range = max - min;

      var Rand = Math.random();

      return min + Math.round(Rand * Range); //四舍五入
    },
    //选中之后计算正确与否以及下一个
    selectOption(option) {
      this.currentOption = option;
      if (this.currentOption.isRight) {
        this.rightNum += 1;
      } else {
        //给出正确单词
        this.options.some((element) => {
          if (element.isRight) {
            this.wrongWords.push(element);
            return true;
          }
        });
      }
      if (this.finishIndex.length < parseInt(this.wordSet.num) - 1) {
        //先执行。1秒钟之后切换，刚好花完时间
        //不能取到重复的
        let index = this.index;
        if (this.wordSet.wordRandom == "随机") {
          index = this.randomNumBoth(0, this.dataList.length - 1);
          if (this.finishIndex.indexOf(index) > -1) {
            //则包含该元素，重新生成
            index = this.randomNumBoth(0, this.dataList.length - 1);
          }
        } else {
          index += 1;
        }
        let options = this.createOption(index);
        setTimeout(() => {
          this.options = options;
          this.currentOption = "";
          this.$refs.myCountdown.reset();
          this.index = index;
        }, 1000);
      } else {
        setTimeout(() => {
          this.isOver = true;
          this.calThis();
        }, 1000);
      }
      this.finishIndex.push(this.index);
    },
    createOption(currentIndex) {
      let options = [];
      //随机设置一个正确的选项
      let rightIndex = this.randomNumBoth(0, 3);
      let rightOptionName =
        rightIndex == 0
          ? "A"
          : rightIndex == 1
          ? "B"
          : rightIndex == 2
          ? "C"
          : "D";

      //移除掉正确选项的下标，剩下3个错误选项
      let optionsName = ["A", "B", "C", "D"];

      let newList = [];
      this.totalwordList.forEach((element, index) => {
        newList.push(index);
      });
      //   newList.splice(currentIndex, 1);

      optionsName.forEach((optionName) => {
        if (optionName == rightOptionName) {
          options.push({
            name: rightOptionName,
            option: this.dataList[currentIndex],
            isRight: 1,
          });
        } else {
          //移除掉正确选项，然后随机选择三个错误选项
          let rindex = this.randomNumBoth(0, newList.length - 1);
          let windex = newList[rindex];
          options.push({
            name: optionName,
            option: this.totalwordList[windex],
          });
          newList.splice(rindex, 1);
        }
      });
      return options;
    },
    //计算一下本次默写结果
    calThis() {
      //结束 本地记录一下
      let wrongNum;
      if (wx.getStorageSync("wrongNum")) {
        wrongNum = wx.getStorageSync("wrongNum") + this.wrongWords.length;
      } else {
        wrongNum = this.wrongWords.length;
      }
      wx.setStorageSync("wrongNum", wrongNum);
      let finished;
      if (wx.getStorageSync("finished")) {
        finished = wx.getStorageSync("finished") + this.finishIndex.length;
      } else {
        finished = this.finishIndex.length;
      }
      wx.setStorageSync("finished", finished);
    },

    reback() {
      this.finishIndex = [];
      this.isOver = false;
      this.currentOption = "";
      this.wrongWords = [];
      this.index = 0;
      this.rightNum = 0;
      if (this.wordSet.wordRandom == "随机") {
        this.index = this.randomNumBoth(0, this.dataList.length);
      }
      this.options = this.createOption(this.index);
    },

    collectWords(collectType) {
      this.isCollect[collectType] = 1;
      let http = common_http + "/collectWords";
      let params = {};
      params.ephemeralParam = {
        user_id: this.user.id,
        word_name: this.dataList[this.index].word_name,
        word_pronunciation: this.dataList[this.index].word_pronunciation,
        word_meaning: this.dataList[this.index].word_meaning,
        word_type: this.dataList[this.index].type,
        word_type1: this.dataList[this.index].type1,
        collect_type: collectType,
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
            title: res.data.extra ? res.data.extra : "记录成功！",
            icon: "none",
          });
        },
      });
    },

    // getWords(type) {
    //   let http = common_http + "/getWords";
    //   let params = {};
    //   params.ephemeralParam = type;
    //   let _this = this;
    //   wx.request({
    //     method: "post",
    //     url: http,
    //     data: {
    //       params: JSON.stringify(params),
    //     },
    //     header: {
    //       "content-type": "application/x-www-form-urlencoded",
    //     },
    //     success(res) {
    //       _this.dataList = res.data.extra;
    //       if (_this.wordSet.wordRandom == "随机") {
    //         _this.index = _this.randomNumBoth(0, _this.dataList.length);
    //       }
    //       _this.options = _this.createOption(_this.index);
    //     },
    //   });
    // },
  },
};
</script>
<style scoped lang="scss">
.select-words {
  padding: 20px 10px 20px 10px;
  .word-input {
    text-align: center;
    padding: 10px;
    height: 20px;
    border-radius: 3px;
  }
// 修改样式部分
.word-option {
  padding: 12rpx 20rpx;
  background: #fff;
  margin: 20rpx 0;
  border-radius: 8rpx;
  min-height: 80rpx; // 最小高度替代固定高度
  display: flex;
  align-items: center;

  .option-content {
    flex: 1;
    display: flex;
    align-items: baseline; // 基线对齐
  }

  .option-name {
    font-weight: bold;
    font-size: 28rpx;
    color: #333;
    min-width: 60rpx; // 保证字母宽度一致
    margin-right: 20rpx;
  }

  .option-meaning {
    flex: 1;
    font-size: 28rpx;
    color: #666;
    line-height: 1.6;
    word-break: break-word; // 允许单词内断行
    white-space: normal; // 允许换行
    overflow-wrap: break-word; // 强制换行
  }
}

  ::v-deep .u-count-down__text {
    font-size: 30px !important;
  }
  ::v-deep .u-count-down {
    padding-top: 4px !important;
  }

  .word-sheng {
    transition: transform 0.3s ease; /* 添加变换动画 */
  }

  .word-sheng:active {
    transform: scale(2); /* 缩放图片 */
  }
}
</style>
