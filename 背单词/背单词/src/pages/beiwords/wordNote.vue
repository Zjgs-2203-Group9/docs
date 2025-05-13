<template>
  <view
    class="my-uni-app-body"
    :style="{ height: `${mainHeight}px`, background: '#f8f8f8!important' }"
  >
    <u-navbar
      title="单词本"
      :bgColor="themeColor"
      placeholder
      @leftClick="gotoIndex"
    ></u-navbar>
    <view style="padding: 40px 10px 0 10px">
      <view
        v-for="(i, index) in wordNoteList"
        :key="index"
        @click="gotoWordNoteDetails(i)"
        style="margin-bottom: 10px; background: #ffff; border-radius: 5px"
      >
        <u-row>
          <u-col :span="9">
            <view style="padding: 5px">
              <view>
                <text
                  style="color: #7ecdf7; font-size: 18px; font-weight: bold"
                >
                  {{ i.name }}
                </text>
              </view>

              <view style="margin-top: 10px"
                ><text style="font-size: 12px; color: #ababab">{{
                  i.createDtme
                }}</text></view
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
              <img
                style="width: 30px; height: 30px; padding-top: 8px"
                src="https://itlifetime.com/resources/xiaochengxu/笔记本.png"
              />
              <view
                ><text style="font-size: 15px; color: #ababab"
                  >共{{ i.total }}个</text
                ></view
              ></view
            >
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
      wordNoteList: [],
      themeColor: "",
      mainHeight: "",
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
      this.user = JSON.parse(wx.getStorageSync("user"));
      this.wordNoteList = JSON.parse(wx.getStorageSync("wordNoteList"));
      this.getTotalNumForNote();
    },
    gotoWordNoteDetails(item) {
      wx.setStorageSync("isNote", 1);
      wx.setStorageSync("noteInfo", JSON.stringify(item));
      wx.navigateTo({
        url: "/pages/beiwords/total",
      });
    },
    getTotalNumForNote() {
      let http = common_http + "/getTotalNumForNote";
      let params = {};
      params.ephemeralParam = this.user.id;
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
          _this.wordNoteList[0].total = res.data.extra.type;
          _this.wordNoteList[1].total = res.data.extra.type1;

          _this.wordNoteList[2].total = res.data.extra.type2;
          _this.wordNoteList[3].total = res.data.extra.type3;
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
