<template>
  <view
    class="my-uni-app-body"
    :style="{ minHeight: `${mainHeight}px`, overflow: 'hidden' }"
  >
    <u-navbar
      :title="
        noteInfo
          ? `单词本——${noteInfo.name}`
          : wordInfo
          ? wordInfo.name
          : '错误单词'
      "
      :bgColor="themeColor"
      placeholder
      @leftClick="rebackIndex"
    ></u-navbar>

    <view class="bei-words" v-if="!isOver">
      <view style="margin-top: 5px; padding: 0 10px 0 10px">
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
      <!-- <view style="text-align: center">
        <view style="font-size: 20px; margin-top: 20px">{{
          index < dataList.length
            ? "第" + (index + 1) + "个"
            : "恭喜您，已经默写完全部单词"
        }}</view>
      </view> -->

      <view
        style="text-align: center; margin: 10px 10px 0 10px; min-height: 60px"
        v-if="dataList && dataList.length > 0"
      >
        <swiper
          :autoplay="false"
          interval="3000"
          :circular="true"
          :indicator-dots="false"
          indicator-color="#ffffff"
          indicator-active-color="#ff0000"
          :current="index"
        >
          <!--          @change="swiperChange"
 -->
          <swiper-item
            @touchmove.stop
            v-for="(i, index1) in dataList"
            :key="index1"
          >
            <view style="font-size: 25px">
              {{
                wordSet.wordDir == "中文→英文" ? i.word_meaning : i.word_name
              }}
            </view>

            <view @click="playShenyin(i.word_name)" style="margin-top: 5px">
              <img
                style="
                  width: 16px;
                  height: 16px;
                  vertical-align: middle;
                  margin-right: 4px;
                "
                class="word-sheng"
                src="https://itlifetime.com/resources/xiaochengxu/声音.png"
              />
              <text style="font-size: 15px; color: #ababab">{{
                i.word_pronunciation
              }}</text></view
            >
            <view style="font-size: 25px; height: 30px">
              <text v-if="user_answer && user_answer !== i.word_name">
                {{
                  wordSet.wordDir == "中文→英文" ? i.word_name : i.word_meaning
                }}</text
              >
            </view>
          </swiper-item>
        </swiper>
        <!-- <view
          style="font-size: 25px; font-weight: bold"
          :animation="animationData1"
        >
          {{ dataList[index].word_meaning }}
        </view> -->
      </view>

      <view style="margin-top: 10px">
        <view v-if="wordSet.wordDir == '中文→英文'">
          <u-row :gutter="20">
            <u-col :span="span" v-for="(w, index) in wordArrays" :key="index">
              <view
                @click="setCurrent(index)"
                class="word-input"
                :style="{
                  border:
                    focus_index == index
                      ? `1px solid  green`
                      : wordArrays[index].wrong == 1
                      ? `1px solid  red`
                      : `1px solid${themeColor}`,
                }"
                ><view class="input-view">
                  <text class="input-text">{{ w.v }}</text>
                  <!-- 模拟输入光标 -->
                  <view
                    :class="focus_index == index ? inputCursor : ''"
                  ></view> </view
              ></view>
            </u-col>
          </u-row>
        </view>
        <view v-else style="display: flex; padding: 20px">
          <view class="view-contain-ti">
            <text class="text-ti">输入意思</text>
            <input class="input1" v-model="user_en_answer" />
          </view>
          <view
            @click="checkUserAnswer"
            style="width: 60px"
            :style="{
              background: user_en_answer ? themeColor : 'grey',
              color: '#fff',
              fontSize: '15px',
              borderRadius: '5px',
              padding: '10px 0 10px 25px',
            }"
            >确认</view
          >
        </view>
      </view>
      <view style="margin-top: 10px; height: 32px; line-height: 32px">
        <u-row>
          <u-col :span="8">
            <view v-if="user_answer">
              <view
                v-if="
                  wordSet.wordDir == '中文→英文'
                    ? user_answer == dataList[index].word_name
                    : dataList[index].word_meaning.indexOf(user_answer) != -1
                "
              >
                <img
                  style="vertical-align: middle; width: 36px; height: 36px"
                  src="https://itlifetime.com/resources/xiaochengxu/笑脸.png"
                />
                <text> 答对啦</text>
              </view>
              <view v-else>
                <img
                  style="vertical-align: middle; width: 32px; height: 32px"
                  src="https://itlifetime.com/resources/xiaochengxu/哭脸.png"
                />

                <text style="color: red">
                  {{ outTime ? "没记住吧？" : "拼错啦" }}</text
                >
              </view>
            </view>
          </u-col>
          <u-col :span="2">
            <u-icon
              name="close-circle"
              @click="removeCurrentKey"
              color="red"
            ></u-icon>
          </u-col>
          <u-col :span="2">
            <u-icon name="eye" @click="showWord" color="green"></u-icon>
          </u-col>
        </u-row>
      </view>

      <view style="margin-top: 5px">
        <u-row :gutter="20">
          <u-col :span="6">
            <u-button
              type="primary"
              @click="dNextWord"
              icon="close-circle"
              size="meduim"
              shape="circle"
            >
              没记住跳过
            </u-button></u-col
          >
          <u-col :span="6">
              <u-button
                type="success"
                size="medium"
                icon="edit-pen"
                @click="toggleCollect"
                shape="circle"
                :customStyle="{
                  background: isCollected ? '#f56c6c' : '#34b233',
                  color: '#fff',
                  transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)'
                }"
              >
                {{ isCollected ? '从单词本移除' : '记到单词本上' }}
              </u-button>
          </u-col>
        </u-row>
      </view>
    </view>
    <view class="bei-words" v-else>
      <view style="text-align: center">
        <view style="font-size: 20px; font-weight: bold"
          >本次共默写了{{ finishIndex.length }}个单词</view
        >
        <view style="font-size: 20px; margin-top: 20px"
          >恭喜您，已经默写完全部单词</view
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

            <u-col :span="4">得分：{{ score }}</u-col>
          </u-row>
        </view>

        <view style="font-size: 20px; margin-top: 20px">
          <u-row :gutter="20">
            <u-col :span="6">
              <u-button
                type="success"
                @click="reback"
                icon="eye"
                shape="circle"
              >
                重新默写
              </u-button>
            </u-col>
            <u-col :span="6">
              <u-button
                type="primary"
                icon="edit-pen"
                shape="circle"
                @click="gotoScoreList"
                >去查看成绩单</u-button
              >
            </u-col>
          </u-row>
        </view>

        <view style="padding: 10px 10px 0 10px">
          <u-divider
            textSize="30"
            text="错误单词："
            :hairline="true"
          ></u-divider>

          <!-- <u-collapse>
            <u-collapse-item
              :title="`${index + 1}、${i.word_name}`"
              v-for="(i, index) in wrongWords"
              :key="index"
            >
              <text class="u-collapse-content">{{ i.word_meaning }}</text>
            </u-collapse-item>
          </u-collapse> -->

          <view
            v-for="(i, index) in wrongWords"
            :key="index"
            style="
              margin-bottom: 10px;
              background: #ffff;
              max-height: 80px;
              border-radius: 5px;
              padding: 9px;
            "
          >
            <u-row style="height: 100%">
              <u-col :span="8">
                <view>
                  <text style="color: #7ecdf7; font-size: 20px">
                    {{ i.word_name }}
                  </text>
                </view>
                <view>
                  <text style="color: red; font-size: 14px">
                    {{ i.wrongAnswer }}
                  </text>
                </view>

                <view
                  ><text>{{ i.word_meaning }}</text></view
                >
              </u-col>
              <u-col :span="4" style="background: #e1f5f4">
                <u-row :gutter="5">
                  <u-col :span="9">
                    <view
                      ><text style="font-size: 15px; color: #ababab">{{
                        i.word_pronunciation
                      }}</text></view
                    ></u-col
                  ><u-col :span="3">
                    <view @click="playShenyin(i.word_name)">
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

    <view v-if="!isOver && wordSet.wordDir == '中文→英文'">
      <div class="md-popup bottom" v-show="showKeyBoard">
        <div class="md-popup-mask" v-show="showMask"></div>
        <view :style="{ animation: `${animation} 0.3s linear forwards` }">
          <div class="cus-key-cont">
            <div class="key-num">
              <ul class="key-num-list">
                <li
                  @click="getKeyValue(item)"
                  class="key-num-item"
                  v-for="(item, index) in keyList"
                  :key="index"
                >
                  <span>{{ item }}</span>
                </li>
                <li
                  @click="delKeyValue"
                  v-if="keyS == 's50'"
                  class="key-num-item no-bg delete"
                >
                  <span></span>
                </li>
              </ul>
            </div></div
        ></view>
      </div>
    </view>
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";
import { request } from "../common/request.js";
export default {
  setup() {},
  data() {
    return {
	isCollected: false,
	currentWordId: null, // 新增存储当前单词ID
      showKeyBoard: false,  // 初始化 showKeyBoard
      show: false,
      wordNoteList: [],
      answer: "",
      list: [
        { name: "自考树英语", value: 0 },
        { name: "学科英语", value: 1 },
        { name: "术语英语", value: 2 },
      ],
      show: false,
      keyList: [
        "a",
        "b",
        "c",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "w",
        "x",
        "y",
        "z",
      ],
      valList: [],
      temList: [],
      keyS: "s10",
      keyReplce: "x",
      keyEntText: "确定",
      animation: "",
      a1: "test",
      a2: "test1 ",
      showMask: false,
      dataList: [],
      themeColor: "",
      inputCursor: "input-cursor",
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
      score: "",
      isOver: false,
      wordInfo: "",
      noteInfo: "",
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
      user_en_answer: "",
    };
  },
  onLoad() {
    this.getInit();
    // 新增初始化检查
    if(this.dataList.length > 0){
      this.currentWordId = this.dataList[0].word_id;
      this.checkCollectStatus();
    }
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
      if (wx.getStorageSync("wordInfo")) {
        this.wordInfo = JSON.parse(wx.getStorageSync("wordInfo"));
      }
      if (wx.getStorageSync("user")) {
        this.user = JSON.parse(wx.getStorageSync("user"));
      }
      this.wordSet = JSON.parse(wx.getStorageSync("wordSet"));
      let isNote = wx.getStorageSync("isNote");
      //收藏进来的
      if (isNote && isNote == 1) {
        this.noteInfo = JSON.parse(wx.getStorageSync("noteInfo"));
        this.dataList = JSON.parse(wx.getStorageSync("wordList"));
      } else {
        //全部进来的
        this.dataList = JSON.parse(wx.getStorageSync("totalwordList"));
      }

      //不能取到重复的，应该一开始就打乱顺序然后放入，不应该每次都计算，问题是很慢
      let len = parseInt(this.wordSet.num);

      if (this.wordSet.wordRandom == "随机") {
        //打乱顺序
        // setTimeout(() => {}, 300);
        //则包含该元素，重新生成一组指定长度不重复的数组
        let randomDataList = [];
        // for (let i = 0; i < len; i++) {
        //   let index = this.randomNumBoth(0, this.dataList.length - 1);
        //   randomDataList.push(this.dataList[index]);
        // }

        const randoms = new Set();
        while (randoms.size < len) {
          const index = this.randomNumBoth(0, this.dataList.length - 1);
          if (!randoms.has(index)) {
            randoms.add(index);
            randomDataList.push(this.dataList[index]);
          }
        }

        this.dataList = randomDataList;
      } else {
        //直接取前面部分
        let newList = [];
        for (let i = 0; i < len; i++) {
          newList.push(this.dataList[i]);
        }
        this.dataList = newList;
      }

      this.setNextWord();
    },
    keypress(e) {
      console.log(e.keycode);
    },
// 简化后的收藏方法
    async toggleCollect() {
      if (!this.currentWordId) return;
      
      try {
        if (this.isCollected) {
          await request({
            url: "/words/favor",
            method: "DELETE",
            data: { word_id: this.currentWordId },
            needAuth: true
          });
        } else {
          await request({
            url: "/words/favor",
            method: "POST",
            data: { word_id: this.currentWordId },
            needAuth: true
          });
        }
        this.isCollected = !this.isCollected;
        uni.showToast({
          title: this.isCollected ? '添加成功' : '移除成功',
          icon: 'none'
        });
      } catch (error) {
        console.error('操作失败:', error);
      }
    },
	// 修改后的检查收藏状态方法
    async checkCollectStatus() {
      try {
        const res = await request({
          url: "/words/favor/status",
          method: "GET",
          data: { // 改为params参数
            word_id: this.currentWordId
          },
          needAuth: true
        });
        this.isCollected = res.extra;
      } catch (error) {
        console.error('获取状态失败:', error);
      }
    },
    reback() {
      this.finishIndex = [];
      this.isOver = false;

      this.wrongWords = [];
      this.wordArrays = [];
      this.index = 0;
      this.rightNum = 0;
      this.user_answer = "";
      this.setNextWord();
    },
    // getWords(type) {
    //     let http = common_http + "/getWords";
    //     let params = {};
    //     params.ephemeralParam = type;
    //     let _this = this;
    //     wx.request({
    //       method: "post",
    //       url: http,
    //       data: {
    //         params: JSON.stringify(params),
    //       },
    //       header: {
    //         "content-type": "application/x-www-form-urlencoded",
    //       },
    //       success(res) {
    //         _this.dataList = res.data.extra;
    //         if (_this.wordSet.wordRandom == "随机") {
    //           _this.index = _this.randomNumBoth(0, _this.dataList.length);
    //         }
    //         const arr = Array.from(_this.dataList[_this.index].word_name);

    //         arr.forEach((element) => {
    //           _this.wordArrays.push({ v: "", wrong: 0 });
    //         });
    //         _this.calSpan();
    //       },
    //     });
    //   },
    setCurrent(index) {
      this.focus_index = index;
    },
    rebackIndex() {
      //确认框
      let _this = this;
      if (this.isOver) {
        _this.gotoIndex();
      } else {
        wx.showModal({
          title: "确认",
          content: "确认返回吗，本次默写成绩取消了哦",
          success(res) {
            if (res.confirm) {
              console.log("用户点击确定");
              _this.gotoIndex();
            } else if (res.cancel) {
              console.log("用户点击取消");
            }
          },
        });
      }
    },
	playShenyin(wordName){
		const innerAudioContext = wx.createInnerAudioContext();
		innerAudioContext.src = `http://dict.youdao.com/dictvoice?audio=${wordName}`;
		innerAudioContext.play();
	},
    showCollect() {
      this.show = true;
	    this.currentWordId = this.dataList[this.index].word_id; // 获取当前单词ID

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

    timeFinish() {
      this.user_answer = "xx";
      const arr = Array.from(this.dataList[this.index].word_name);
      this.wordArrays = [];
      arr.forEach((_element) => {
        this.wordArrays.push({ v: _element });
      });
    },
    //显示正确单词
    showWord() {
		this.currentWordId = this.dataList[this.index].word_id;
		this.checkCollectStatus();
      this.timeFinish();
    },
    //移除掉一个key
    removeCurrentKey() {
      if (this.wordArrays[this.focus_index - 1]) {
        this.wordArrays[this.focus_index - 1].v = "";
        this.wordArrays[this.focus_index - 1].wrong = 0;
        this.focus_index -= 1;
      }
    },
    checkUserAnswer() {
      if (!this.user_en_answer) {
        return false;
      }
      this.user_answer = this.user_en_answer;
      if (
        this.dataList[this.index].word_meaning.indexOf(this.user_en_answer) !=
        -1
      ) {
        this.rightNum += 1;
      } else {
        //答错，给出正确的
        this.dataList[this.index].wrongAnswer = this.user_en_answer;
        this.wrongWords.push(this.dataList[this.index]);
        // const arr = Array.from(this.dataList[this.index].word_name);
        // this.wordArrays.forEach((element) => {
        //   arr.forEach((element1) => {
        //     if (element.v != element1) {
        //       element.wrong = 1;
        //     }
        //   });
        // });
        // setTimeout(() => {
        //   this.nextWord();
        // }, 900);
      }
      setTimeout(() => {
        this.nextWord();
        this.user_en_answer = "";
      }, 900);
    },
    getKeyValue(item) {
      //所有值都存在，校验正确与否
      if (this.wordArrays[this.focus_index]) {
        this.wordArrays[this.focus_index].v = item;
        const arr = Array.from(this.dataList[this.index].word_name);
        if (arr[this.focus_index] !== item) {
          this.wordArrays[this.focus_index].wrong = 1;
        } else {
          this.wordArrays[this.focus_index].wrong = 0;
        }
        this.focus_index += 1;
      }

      let total = 0;
      this.wordArrays.forEach((element) => {
        if (element.v) {
          total += 1;
        }
      });
      if (total == this.wordArrays.length) {
        this.wordArrays.forEach((element) => {
          this.user_answer += element.v;
        });
        //答对了3秒跳页【防止有大写】
        //2种比对
        if (
          this.user_answer == this.dataList[this.index].word_name ||
          this.user_answer.toUpperCase() ==
            this.dataList[this.index].word_name.toUpperCase()
        ) {
          this.rightNum += 1;
        } else {
          //答错，给出正确的
          this.dataList[this.index].wrongAnswer = this.user_answer;
          this.wrongWords.push(this.dataList[this.index]);
          // const arr = Array.from(this.dataList[this.index].word_name);
          // this.wordArrays.forEach((element) => {
          //   arr.forEach((element1) => {
          //     if (element.v != element1) {
          //       element.wrong = 1;
          //     }
          //   });
          // });
          // setTimeout(() => {
          //   this.nextWord();
          // }, 900);
        }
        setTimeout(() => {
          this.nextWord();
        }, 900);
      }
    },
    // //滑动下一个
    // swiperChange(v) {
    //   this.wrongWords.push({ a: this.dataList[this.index], b: "" });

    //   this.nextWord();
    // },
    dNextWord() {
      this.dataList[this.index].wrongAnswer = "";
      this.wrongWords.push(this.dataList[this.index]);
      this.nextWord();
    },

    randomNumBoth(min, max) {
      var Range = max - min;

      var Rand = Math.random();

      return min + Math.round(Rand * Range); //四舍五入
    },

    gotoScoreList() {
      wx.navigateTo({
        url: "/pages/beiwords2/scoreList",
      });
    },

    setNextWord() {
	this.currentWordId = this.dataList[this.index].word_id; // 新增
	this.checkCollectStatus(); // 新增
      this.$refs.myCountdown.reset();
      const arr = Array.from(this.dataList[this.index].word_name);
      this.wordArrays = [];
      this.user_answer = "";
      this.focus_index = 0;
      //有可能取到重复的单词
      arr.forEach((_element) => {
        this.wordArrays.push({ v: "", wrong: 0 });
      });
      this.calSpan();

      //this.startAnimation();
    },
    nextWord() {
      if (this.finishIndex.length < parseInt(this.wordSet.num) - 1) {
        this.index += 1;
        this.setNextWord();
      } else {
        this.isOver = true;
        this.score = Math.floor(this.rightNum / this.finishIndex.length) * 100;
        this.showKeyBoard = false; //关闭输入框

        this.calThis();
        //记录此次分分数
        this.recordScore();
      }
      this.finishIndex.push(this.index);
    },
    //计算一下本次默写结果
    calThis() {},
    calSpan() {
      this.span =
        this.dataList[this.index].word_name.length % 12 == 0
          ? 12 / this.dataList[this.index].word_name.length
          : 12 / this.dataList[this.index].word_name.length;
      console.log(this.span);
    },
    check() {
      this.answer = this.word_name == this.dataList[this.index].word_name;
    },

    delKeyValue() {
      let del_in = this.valList.length - 1;
      this.valList.splice(del_in, 1);
      //console.log(this.valList);
    },
    cloKeyBoard() {
      this.animation = this.a1;
      this.showMask = false;
      setTimeout(() => {
        this.ShowKeyBoard = false;
      }, 300);
    },
    entKeyValue() {
      this.temList = this.valList;
      let resNum = this.temList.join("");
      console.log(resNum);
    },
    openKeyBoard() {
      this.ShowKeyBoard = true;
      this.showMask = true;
      this.animation = this.a2;
    },

    check1(e) {
      //所有值都存在
      let total = 0;
      this.wordArrays.forEach((element) => {
        if (element.v) {
          total += 1;
        }
      });
      if (total == this.wordArrays.length) {
        this.wordArrays.forEach((element) => {
          this.user_answer += element.v;
        });
        //答对了3秒跳页
        if (this.user_answer == this.dataList[this.index].word_name) {
          this.rightNum += 1;
          setTimeout(() => {
            this.nextWord();
          }, 1500);
        }
      }
      //自动移动光标
      var index = e.target.id.replace("input-", "");
      var value = e.detail.value;
      if (index != "10" && value != "") {
        this.focus_index = Number(index) + 1;
      }
    },

    recordScore() {
      let http = common_http + "/recordScore";
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

    cancelCollectWords(collectType) {
      this.show = false;
    },
    collectWords(collectType) {
      this.show = false;
    },
    startAnimation() {
      // this.animation.rotate3d(0, 0, 0, 350).step({
      //   // delay:1000,
      //   duration: 800,
      // });

      this.animationData = this.animation.export();
      console.log(JSON.stringify(this.animationData));
    },
  },
};
</script>
<style scoped lang="scss">
.bei-words {
  padding: 20px 10px 20px 10px;
  .word-input {
    display: flex;
    text-align: center;
    padding: 6px 6px 6px 6px;
    height: 20px;
    border-radius: 3px;
  }
  ::v-deep.u-row {
    height: 100% !important;
  }
  ::v-deep .u-count-down__text {
    font-size: 30px !important;
  }
  ::v-deep .u-count-down {
    padding-top: 4px !important;
  }
  swiper {
    height: 100px !important;
  }

  .word-sheng {
    transition: transform 0.3s ease; /* 添加变换动画 */
  }

  .word-sheng:active {
    transform: scale(2); /* 缩放图片 */
  }
  .input-cursor {
    width: 2px;
    margin-left: 2px;
    height: 22px;
    background-color: #33bcd7;
    animation: blink 1.5s linear infinite;
  }
  @keyframes blink {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
}
.md-popup {
  position: fixed;
  // top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  display: -webkit-box;
  display: flex;
  z-index: 1000;
}
.md-popup.bottom {
  flex-direction: column;
  justify-content: flex-end;
}
.md-popup-mask {
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  position: absolute;
  pointer-events: auto;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 1;
}
.cus-num-key-slot {
  position: relative;
  width: 100%;
  height: 40px;
  line-height: 40px;
  background-color: #f8f8f8;
  color: #666;
  font-size: 14px;
  z-index: 9;
  text-align: center;
}
.cus-key-cont {
  position: relative;
  -webkit-user-select: none;
  -webkit-tap-highlight-color: transparent;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  width: 100%;
  height: 250px; //4.28rem;
  z-index: 9;
}

@keyframes test {
  0% {
    height: 300px;
  }
  25% {
    height: 200px;
  }
  50% {
    height: 100px;
  }
  100% {
    height: 0px;
  }
}
@keyframes test1 {
  0% {
    height: 0px;
  }
  50% {
    height: 100px;
  }
  100% {
    height: 300px;
  }
}
.cus-key-cont:after {
  content: "";
  position: absolute;
  z-index: 2;
  -webkit-transform-origin: 100% 50%;
  transform-origin: 100% 50%;
  -webkit-transform: scaleY(0.5) translateY(-100%);
  transform: scaleY(0.5) translateY(-100%);
  top: 0;
  left: 0;
  right: auto;
  bottom: auto;
  width: 100%;
  border-top: 0.02rem solid #e2e4ea;
  -webkit-transform-origin: 50% 0;
  transform-origin: 50% 0;
}
@media (-webkit-min-device-pixel-ratio: 3), (min-resolution: 3dppx) {
  .cus-key-cont:after {
    -webkit-transform: scaleY(0.33) translateY(-100%);
    transform: scaleY(0.33) translateY(-100%);
  }
}
.cus-key-cont .key-num,
.cus-key-cont .keyb-ope {
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
}
.cus-key-cont .key-num {
  -webkit-box-flex: 3;
  -webkit-flex: 3;
  flex: 3;
  background: #f9fafb;
}
.cus-key-cont .key-num .key-num-list {
  float: left;
  width: 100%;
}
.cus-key-cont .key-num .key-num-list .key-num-item {
  position: relative;
  float: left;
  width: 20%;
  height: 40px;
  text-align: center;
  line-height: 40px;
  font-size: 19px;
  font-weight: 500;
  font-family: Microsoft YaHei, 微软雅黑, Arial, sans-serif;
  color: #111a34;
  -webkit-transition: background 0.3s;
  transition: background 0.3s;
  background: #fff;
  box-sizing: border-box;
  cursor: pointer;
}
.cus-key-cont .key-num .key-num-list .key-num-item:before {
  content: "";
  position: absolute;
  z-index: 2;
  -webkit-transform: scaleX(0.5) translateX(100%);
  transform: scaleX(0.5) translateX(100%);
  top: 0;
  right: 0;
  left: auto;
  bottom: auto;
  border-right: 0.02rem solid #e2e4ea;
  height: 100%;
  -webkit-transform-origin: 100% 50%;
  transform-origin: 100% 50%;
}
@media (-webkit-min-device-pixel-ratio: 3), (min-resolution: 3dppx) {
  .cus-key-cont .key-num .key-num-list .key-num-item:before {
    -webkit-transform: scaleX(0.33) translateX(100%);
    transform: scaleX(0.33) translateX(100%);
  }
}
.cus-key-cont .key-num .key-num-list .key-num-item:after {
  content: "";
  position: absolute;
  z-index: 2;
  -webkit-transform-origin: 100% 50%;
  transform-origin: 100% 50%;
  -webkit-transform: scaleY(0.5) translateY(-100%);
  transform: scaleY(0.5) translateY(-100%);
  top: 0;
  left: 0;
  right: auto;
  bottom: auto;
  width: 100%;
  border-top: 0.02rem solid #e2e4ea;
  -webkit-transform-origin: 50% 0;
  transform-origin: 50% 0;
}
@media (-webkit-min-device-pixel-ratio: 3), (min-resolution: 3dppx) {
  .cus-key-cont .key-num .key-num-list .key-num-item:after {
    -webkit-transform: scaleY(0.33) translateY(-100%);
    transform: scaleY(0.33) translateY(-100%);
  }
}
.cus-key-cont .key-num .key-num-list .key-num-item.no-bg {
  background: 0 0;
}
.cus-key-cont .key-num .key-num-list .key-num-item:nth-of-type(-n + 3):after {
  display: none;
}
// .cus-key-cont .key-num .key-num-list .key-num-item:nth-of-type(3n) {
//   width: 33.4%;
// }
.cus-key-cont .key-num .key-num-list .key-num-item.delete {
  background: url("https://itlifetime.com/resources/xiaochengxu/删除.png") 50%
    no-repeat;
  background-size: 0.46rem;
}
.cus-key-cont .key-num .key-num-list .key-num-item.slidedown {
  background: #fff url("https://itlifetime.com/resources/xiaochengxu/收起.png")
    50% no-repeat;
  background-size: 0.54rem;
}
.cus-key-cont .key-num .key-num-list .key-num-item.large-item {
  width: 66.6%;
}
.cus-key-cont .key-num .key-num-list .key-num-item.active,
.cus-key-cont .key-num .key-num-list .key-num-item:active {
  background-color: #33bcd7;
}
.cus-key-cont .key-num .key-num-list .key-num-item:after,
.cus-key-cont .key-num .key-num-list .key-num-item:before {
  pointer-events: none;
}
.cus-key-cont .keyb-ope {
  -webkit-box-flex: 1;
  -webkit-flex: 1;
  flex: 1;
}
.cus-key-cont .keyb-ope .keyb-ope-list {
  position: relative;
  float: left;
  width: 100%;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -webkit-flex-direction: column;
  flex-direction: column;
}
.cus-key-cont .keyb-ope .keyb-ope-list .keyb-ope-item {
  position: relative;
  float: left;
  width: 100%;
  background: #fff;
  -webkit-transition: background 0.3s;
  transition: background 0.3s;
  cursor: pointer;
}
.cus-key-cont .keyb-ope .keyb-ope-list .keyb-ope-item.delete {
  height: 20px;
  background: #fff url("https://itlifetime.com/resources/xiaochengxu/删除.png")
    50% no-repeat;
  background-size: 40px;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-flex: 1;
  -webkit-flex: 1;
  flex: 1;
}
.cus-key-cont .keyb-ope .keyb-ope-list .keyb-ope-item.delete:active {
  background-color: #f9fafb;
}
.cus-key-cont .keyb-ope .keyb-ope-list .keyb-ope-item.confirm {
  padding-top: 0.05rem;
  color: #fff;
  font-size: 20px;
  background: #2f86f6;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-flex: 1;
  -webkit-flex: 1;
  flex: 1;
  -webkit-box-align: center;
  -webkit-align-items: center;
  align-items: center;
  -webkit-box-pack: center;
  -webkit-justify-content: center;
  justify-content: center;
}
.cus-key-cont .keyb-ope .keyb-ope-list .keyb-ope-item.confirm:active {
  background-color: #2a78dc;
}
.cus-key-cont.simple .key-num-item {
  color: number-keyb-key-color-simple !important;
}
.cus-slide-up-enter,
.cus-slide-up-leave-to {
  -webkit-transform: translate3d(0, 100%, 0);
  transform: translate3d(0, 100%, 0);
}
.cus-slide-up-enter-active {
  -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
  transition: -webkit-transform 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
  transition: transform 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
  transition: transform 0.3s cubic-bezier(0.165, 0.84, 0.44, 1),
    -webkit-transform 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
}
.cus-slide-up-leave-active {
  -webkit-transition: -webkit-transform 0.25s cubic-bezier(0.165, 0.84, 0.44, 1);
  transition: -webkit-transform 0.25s cubic-bezier(0.165, 0.84, 0.44, 1);
  transition: transform 0.25s cubic-bezier(0.165, 0.84, 0.44, 1);
  transition: transform 0.25s cubic-bezier(0.165, 0.84, 0.44, 1),
    -webkit-transform 0.25s cubic-bezier(0.165, 0.84, 0.44, 1);
}
.view-contain-ti {
  width: 70%;
  display: flex;
  margin-right: 10px;
  height: 40px;
  //margin: 20px;
  border: 3rpx solid #faca82;
  border-radius: 10rpx;
}

.text-ti {
  position: absolute;
  font-size: 12px;
  background: white;
  margin: -10px 0 0 10px;
  padding: 0 5px;
  color: rgb(144, 147, 167);
}

.input1 {
  margin: auto 10px;
}
</style>
