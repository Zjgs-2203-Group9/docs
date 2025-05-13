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
      <view
        style="
          height: 200px;
          background: #fff;
          padding: 10px;
          border-bottom: 1px solid #c7cee0;
        "
      >
        <input
          type="textarea"
          placeholder="输入需要翻译的文本"
          v-model="content"
        />
      </view>
      <view style="height: 140px; background: #fff; padding: 10px">
        {{ res }}
      </view>
      <u-row
        :gutter="20"
        style="margin-top: 20px; border-bottom: 1px solid #c7cee0; width: 340px"
      >
        <u-col :span="6">
          <u-button type="primary" size="meduim" @click="translate"
            >点击翻译</u-button
          ></u-col
        >
        <u-col :span="6">
          <u-button type="success" size="meduim">记到单词本上</u-button></u-col
        >
      </u-row>
    </view>
  </view>
</template>
<script>
import { common_http, theme_color } from "../common/common.js";

export default {
  setup() {},
  data() {
    return {
      list: [
        { name: "自考树英语", type: 0 },
        { name: "学科英语", type: 1 },
        { name: "术语英语", type: 2 },
      ],
      themeColor: "",
      mainHeight: "",
      labelSize: "17px",
      dataList: [],
      content: "",
      res: "",
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
    isAlphabetic(str) {
      return /^[A-Za-z]+$/.test(str);
    },

    translate() {
      let http = common_http + "/translate";
      let params = {};
      params.ephemeralParam = this.content;
      params.ephemeralParam1 = this.isAlphabetic(this.content) ? "" : 1;

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
          _this.res = res.data.extra;
          if (typeof _this.res == "Object") {
            res = _this.res.word_meaing;
          }
        },
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
