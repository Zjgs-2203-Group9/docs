<template>
  <view
    class="my-uni-app-body"
    :style="{ minHeight: `${mainHeight}px`, background: '#f8f8f8!important' }"
  >
    <u-navbar
      :title="scoreTitle"
      :bgColor="color"
      placeholder
      @leftClick="gotoIndex"
    ></u-navbar>

    <view style="padding: 20px 5px 0 5px">
      <view v-if="scoreList && scoreList.length > 0">
        <view
          v-for="(i, index) in scoreList"
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
            <u-col :span="9">
              <view>
                <text style="color: red; font-size: 16px; font-weight: bold">
                  {{ i.score }}分
                </text>
                <text style="font-size: 14px"> (错{{ i.wrong_num }}) </text>
              </view>

              <view style="margin-top: 5px"
                ><text style="color: #ababab; font-size: 12px">{{
                  i.create_dtme
                }}</text></view
              >
            </u-col>
            <u-col :span="3" style="background: #e1f5f4">
              <view @click="gotoWrongWords(i)">
                <img
                  style="
                    width: 20px;
                    height: 20px;
                    vertical-align: middle;
                    margin-right: 4px;
                  "
                  class="word-sheng"
                  src="https://itlifetime.com/resources/xiaochengxu/1711596995455.png"
                />

                <text style="font-size: 15px; color: #ababab">
                  查看
                </text></view
              >
            </u-col>
          </u-row>
        </view></view
      >
      <view v-else>
        <u-empty mode="car" icon="http://cdn.uviewui.com/uview/empty/car.png">
        </u-empty>
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
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";

export default {
  setup() {},
  data() {
    return {
      color: "",
      mainHeight: "",
      user: "",

      scoreList: [],
      scoreTitle: "成绩",
    };
  },
  onLoad() {
    this.getInit();
  },
  methods: {
    getInit() {
      this.color = theme_color;
      this.mainHeight = wx.getStorageSync("mainHeight");
      this.user = JSON.parse(wx.getStorageSync("user"));

      this.getMyScore();
    },
    gotoWrongWords(i) {
      wx.setStorageSync("isNote", 2);
      wx.setStorageSync("totalwordList", JSON.stringify(i.wrong_words));
      wx.navigateTo({
        url: "/pages/beiwords/total",
      });
    },
    getMyScore() {
      let http = common_http + "/getMyScore";
      let params = {};
      params.ephemeralParam = this.user.id;
      params.ephemeralParam1 = 0;
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
          _this.scoreList = res.data.extra;
        },
      });
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
  height: 300px;
  padding: 20px;
  background: #f8f8f8;

  ::v-deep .u-radio-label--right {
    height: 30px;
    line-height: 30px;
  }
}
</style>
