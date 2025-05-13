<template>
  <view
    class="my-uni-app-body"
    :style="{ height: `${mainHeight}px`, background: '#f8f8f8!important' }"
  >
    <u-navbar
      :title="noteInfo.name"
      :bgColor="themeColor"
      placeholder
      @leftClick="gotoIndex"
    ></u-navbar>
    <view style="padding: 40px 10px 0 10px">
      <view
        v-for="(i, index) in dataList"
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
          <u-col :span="8">
            <view>
              <text style="color: #7ecdf7; font-size: 20px; font-weight: bold">
                {{ i.word_name }}
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
                    style="width: 20px; height: 20px; vertical-align: middle"
                    class="word-sheng"
                    src="https://itlifetime.com/resources/xiaochengxu/声音.png" /></view
              ></u-col>
            </u-row>
          </u-col>
        </u-row>
      </view>
    </view>
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";

export default {
  setup() {},
  data() {
    return {
      dataList: [],
      themeColor: "",
      mainHeight: "",
      noteInfo: {},
      user: "",
    };
  },
  onLoad() {
    this.getInit();
  },
  methods: {
    getInit() {
      this.themeColor = theme_color;
      this.mainHeight = wx.getStorageSync("mainHeight") + 40;
      this.noteInfo = JSON.parse(wx.getStorageSync("noteInfo"));
      this.user = JSON.parse(wx.getStorageSync("user"));

      this.getCollectWords(this.noteInfo.type);
    },
    getCollectWords(type) {
      let http = common_http + "/getCollectWords";
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
          _this.dataList = res.data.extra;
        },
      });
    },

    gotoWordDetails(type) {
      wx.setStorageSync("wordInfo", JSON.stringify(type));
      wx.navigateTo({
        url: "/pages/beiwords/total",
      });
    },
  },
};
</script>
<style scoped>
.my-uni-app-body {
  /* background: #a6e2fa; */
}
</style>
