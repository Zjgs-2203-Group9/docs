import Vue from "vue";
import App from "./App";
import "./uni.promisify.adaptor";

Vue.config.productionTip = false;
App.mpType = "app";
const app = new Vue({
  ...App,
  // share,
});
import uView from "uview-ui";
Vue.use(uView);
// 如此配置即可
uni.$u.config.unit = "rpx";

const common_http = "xxx";

Vue.mixin({
  setup() {},
  methods: {
    gotoIndex() {
      uni.navigateBack({
        delta: 1, // 返回的页面数，如果是1则返回上一页
      });
    },

    // justPlayShenyin(wordMp3Src) {
    //   // ios端音频不能在静音下播放处理
    //   wx.setInnerAudioOption({
    //     obeyMuteSwitch: false,

    //     success: function (res) {
    //       console.log("开启静音模式下播放音乐的功能");
    //     },

    //     fail: function (err) {
    //       console.log("静音设置失败");
    //     },
    //   });

    //   let innerAudioContext = uni.createInnerAudioContext(); //创建实例
    //   innerAudioContext.src = "/static/qin.mp3";
    //   innerAudioContext.play();

    //   innerAudioContext.autoplay = false;
    //   innerAudioContext.loop = false;
    //   innerAudioContext.onPlay(() => {
    //     console.log("开始播放");
    //   });
    //   innerAudioContext.onStop((res) => {
    //     console.log("停止播放");
    //   });
    //   // 第一种
    //   innerAudioContext.play();
    //   // 第二种  如果第一种没有效果
    //   setTimeout(() => {
    //     innerAudioContext.play();
    //   }, 10);
    // },
    playShenyin(wordName) {
      // ios端音频不能在静音下播放处理
      wx.setInnerAudioOption({
        obeyMuteSwitch: false,

        success: function (res) {
          console.log("开启静音模式下播放音乐的功能");
        },

        fail: function (err) {
          console.log("静音设置失败");
        },
      });

      let wordMp3Src =
        "https://dict.youdao.com/dictvoice?audio=" + wordName + "&le=en";
      let innerAudioContext = uni.createInnerAudioContext(); //创建实例
      innerAudioContext.src = wordMp3Src;
      innerAudioContext.play();

      innerAudioContext.autoplay = false;
      innerAudioContext.loop = false;
      innerAudioContext.onPlay(() => {
        console.log("开始播放");
      });
      innerAudioContext.onStop((res) => {
        console.log("停止播放");
      });
      // 第一种
      innerAudioContext.play();
      // 第二种  如果第一种没有效果
      setTimeout(() => {
        innerAudioContext.play();
      }, 10);
    },
  },
});
app.$mount();
