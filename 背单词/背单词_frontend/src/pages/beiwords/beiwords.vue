<template>
  <view
    class="my-uni-app-body"
    :style="{
      height: `${mainHeight}px`,
      overflow: 'hidden',
    }"
  >
    <!--      backgroundImage: 'url(' + bg + ')',
 -->
    <u-navbar
      title="我爱背单词"
      :bgColor="themeColor"
      placeholder
      @leftClick="gotoIndex"
    ></u-navbar>
    <view style="padding: 20px">
      <view>
        <u-row :gutter="10">
          <u-col :span="4">
            <!-- <img
              style="height: 60px; width: 60px; border-radius: 50%"
              src="https://itlifetime.com/resources/xiaochengxu/笔记本.png"
            /> -->
          </u-col>
          <u-col :span="9">
            <view>
              <u-col :span="12">
                <img
                  style="
                    height: 20px;
                    width: 20px;
                    margin-right: 10px;
                    vertical-align: middle;
                  "
                  src="https://itlifetime.com/resources/xiaochengxu/单词.png"
                />
                <text style="font-size: 15px">今日学习：</text>
                <text style="font-size: 19px">
                  {{ finished ? finished : 0 }}</text
                ><text style="font-size: 15px; margin-left: 3px"> 个</text>
              </u-col>
            </view>

            <view style="margin-top: 5px">
              <u-col :span="12">
                <img
                  style="
                    height: 20px;
                    width: 20px;
                    margin-right: 10px;
                    vertical-align: middle;
                  "
                  src="https://itlifetime.com/resources/xiaochengxu/错误提示.png"
                />
                <text style="font-size: 15px"> 错误单词：</text>
                <text style="font-size: 19px">
                  {{ wrongNum ? wrongNum : 0 }}</text
                >
                <text style="font-size: 15px; margin-left: 3px"> 个</text>
              </u-col>
            </view>
          </u-col>
        </u-row>
      </view>
      <view style="margin-top: 30px"> </view>
      <view v-for="(i, index) in list" :key="index">
        <u-row :gutter="20">
          <u-col :span="i[0].col">
            <view
              :style="{ background: themeColor }"
              style="
                padding: 25px;
                display: flex;
                margin-top: 10px;
                border-radius: 5px;
                box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.12);
              "
              @click="gotoWordDetails(i[0])"
            >
              <img
                :class="i[0].isShake ? 'shake-animation' : ''"
                :src="i[0].imgSrc"
                style="width: 35px; height: 35px; margin-right: 10px"
              />
              <text style="color: #d1f51b">{{ i[0].name }}</text>
            </view>
          </u-col>
          <u-col :span="i[1].col">
            <view
              :style="{ background: themeColor }"
              style="
                padding: 20px;
                display: flex;
                margin-top: 10px;
                border-radius: 5px;
                box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.12);
              "
              @click="gotoWordDetails(i[1])"
            >
              <img
                :class="i[1].isShake ? 'shake-animation' : ''"
                :src="i[1].imgSrc"
                style="width: 35px; height: 35px; margin-right: 10px"
              />
              <text style="color: #d1f51b">{{ i[1].name }}</text>
            </view>
          </u-col>
        </u-row>
      </view>
    </view>

    <view>
      <u-popup
        :show="show"
        @close="show = false"
        @open="show = true"
        mode="bottom"
      >
        <view style="background: #f8f8f8; border-radius: 20px">
          <view
            v-for="(i, index) in subjectEns"
            :key="index"
            style="
              border-radius: 5px;
              background: #e1f5f4;
              margin-bottom: 10px;
              background: #ffff;
            "
          >
            <u-row :gutter="20">
              <u-col :span="3">
                <view style="padding: 5px 0 5px 20px">
                  <img :src="i.src" style="height: 100px; width: 85px" />
                </view>
              </u-col>
              <u-col :span="7">
                <view style="margin: auto; line-height: 60px">
                  <text style="font-size: 16px">
                    {{ i.name }}
                  </text>
                </view>
              </u-col>
            </u-row>
          </view></view
        >
      </u-popup>
    </view>
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";

export default {
  setup() {},
  data() {
    return {
      wordDaily: {},
      show: false,
      subjectEns: [
        {
          name: "七年级英语",
          src: "https://itlifetime.com/resources/xiaochengxu/1711797431940.jpeg",
        },
        {
          name: "八年级英语",
          src: "https://itlifetime.com/resources/xiaochengxu/1711797431940.jpeg",
        },
        {
          name: "九年级英语",
          src: "https://itlifetime.com/resources/xiaochengxu/1711797431940.jpeg",
        },
        {
          name: "大学英语四级",
          src: "https://itlifetime.com/resources/xiaochengxu/1711777197373.png",
        },
      ],

      list: [
        [
          {
            name: "综合",
            type: 0,
            col: 7,
            imgSrc:
              "https://itlifetime.com/resources/xiaochengxu/1711957674490.png",
            isShake: true,
          },
          {
            name: "学科",
            type: 1,
            col: 5,
            isShake: false,
            imgSrc:
              "https://itlifetime.com/resources/xiaochengxu/1711957529099.png",
          },
        ],
        [
          {
            name: "术语",
            type: 2,
            col: 5,
            isShake: false,
            imgSrc:
              "https://itlifetime.com/resources/xiaochengxu/1711957790715.png",
          },
          {
            name: "场景",
            type: 3,
            col: 7,
            isShake: false,
            imgSrc:
              "https://itlifetime.com/resources/xiaochengxu/1711957659551.png",
          },
        ],
        [
          {
            name: "名词",
            type: 4,
            col: 7,
            isShake: false,
            imgSrc:
              "https://itlifetime.com/resources/xiaochengxu/1711957666800.png",
          },
          {
            name: "随机挑战",
            type: 5,
            col: 5,
            isShake: false,
            imgSrc:
              "https://itlifetime.com/resources/xiaochengxu/1711957700627.png",
          },
        ],
        // {
        //   name: "翻译",
        //   type: 3,
        //   imgSrc:
        //     "https://itlifetime.com/resources/xiaochengxu/1711526006547.png",
        // },
      ],
      themeColor: "",
      mainHeight: "",
      labelSize: "17px",
      user: "",
      finished: 0,
      wrongNum: 0,
    };
  },
  //每次进来都刷新
  onShow() {
    this.finished = wx.getStorageSync("finished");
    this.wrongNum = wx.getStorageSync("wrongNum");
    let wordDailySt = wx.getStorageSync("wordDaily");
    if (!wordDailySt) {
      this.apiAll(this.user.id, "/getTodayWords")
        .then((res) => {
          wx.setStorageSync("wordDaily", JSON.stringify(res.extra.wordDaily));
          this.wordDaily = res.extra.wordDaily;
        })
        .catch((err) => {
          console.log(err); //这个error是接收的reject参数
        });
    } else {
      this.wordDaily = JSON.parse(wordDailySt);
    }
  },
  onLoad() {
    this.getInit();
  },
  methods: {
    getInit() {
      // this.justPlayShenyin();

      this.themeColor = theme_color;
      if (wx.getStorageSync("user")) {
        this.user = JSON.parse(wx.getStorageSync("user"));
      } else {
        this.user = { name: "小白" };
        wx.setStorageSync("user", JSON.stringify(this.user));
      }

      this.mainHeight = wx.getStorageSync("mainHeight");
      if (!this.mainHeight) {
        let _this = this;
        wx.getSystemInfo({
          success: function (res) {
            _this.mainHeight = res.windowHeight;
            wx.setStorageSync("mainHeight", _this.mainHeight);
          },
        });
      }
      if (!wx.getStorageSync("wordNoteList")) {
        let wordNoteList = [
          { name: "记不住的", total: 123, createDtme: "2024-03-19", type: 0 },
          { name: "复杂的", total: 323, createDtme: "2024-03-19", type: 1 },
          { name: "相近的", total: 621, createDtme: "2024-03-19", type: 2 },
          { name: "其他", total: 621, createDtme: "2024-03-19", type: 3 },
        ];
        wx.setStorageSync("wordNoteList", JSON.stringify(wordNoteList));
      }
    },

    gotoCollect() {
      wx.navigateTo({
        url: "/pages/beiwords/wordNote",
      });
    },
    gotoScoreList() {
      wx.navigateTo({
        url: "/pages/beiwords2/scoreList",
      });
    },

    gotoWordDetails(item) {
      if (item.type == 3) {
        wx.navigateTo({
          url: "/pages/beiwords/translate",
        });
      } else if (item.type == 1) {
        this.show = true;
      } else {
        wx.setStorageSync("wordInfo", JSON.stringify(item));
        wx.setStorageSync("isNote", 0);
        wx.navigateTo({
          url: "/pages/beiwords/total",
        });
      }
    },
  },
};
</script>
<style scoped lang="scss">
.my-uni-app-body {
  /* background: #a6e2fa; */

  background: linear-gradient(#f0fcc7, #dff8f5); // 标准的语法
  /* background: -webkit-linear-gradient(#caeefc, #caeefc); 

  background: -o-linear-gradient(#caeefc, #caeefc); 

  background: -moz-linear-gradient(#caeefc, #caeefc); 
  */

  @keyframes shake {
    0% {
      transform: rotate(0);
    }
    10% {
      transform: rotate(-15deg);
    }
    20% {
      transform: rotate(15deg);
    }
    30% {
      transform: rotate(-15deg);
    }
    40% {
      transform: rotate(15deg);
    }
    50% {
      transform: rotate(-15deg);
    }
    60% {
      transform: rotate(15deg);
    }

    70% {
      transform: rotate(-15deg);
    }
    80% {
      transform: rotate(15deg);
    }
    90% {
      transform: rotate(-15deg);
    }
    100% {
      transform: rotate(0);
    }
  }

  .shake-animation {
    display: inline-block;
    // animation: shake 0.8s infinite none;
    animation-name: shake; //执行动画的名字
    animation-duration: 1s; //动画时常
    animation-delay: 1s; //页面加载完成后动画执行前的延迟时间
    animation-timing-function: linear; //执行速率
    animation-iteration-count: infinite; //动画播放次数，infinite表示无限播放
    // animation-fill-mode: alternate;
  }
}
</style>
