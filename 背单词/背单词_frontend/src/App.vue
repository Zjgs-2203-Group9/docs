<script>
export default {
  // created() {

  // },
  onLoad: function () {
    document.addEventListener("touchstart", function (event) {
      if (event.touches.length > 1) {
        event.preventDefault();
      }
    });
    var lastTouchEnd = 0;
    document.addEventListener(
      "touchend",
      function (event) {
        var now = new Date().getTime();
        if (now - lastTouchEnd <= 300) {
          event.preventDefault();
        }
        lastTouchEnd = now;
      },
      false
    );
  },
  onLaunch: function () {
    console.log("App Launch");
  },
  onShow: function () {
    console.log("App Show");
  },
  onHide: function () {
    console.log("App Hide");
  },
  // onShareAppMessage: function () {
  //   return {
  //     title: "分享给朋友",
  //     path: "/page/index",
  //   };
  // },
  // onShareTimeline() {
  //   return {
  //     title: "分享到朋友圈标题",
  //     imageUrl: "/page/index",
  //   };
  // },
};
</script>

<style lang="scss">
/*每个页面公共css */
/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
@import "uview-ui/index.scss";
.my-uni-app-body {
  background: #f6f6f6;
  // background: rgb(222, 235, 243);
  .my-uni-app {
    margin-top: 80px;
  }

  @keyframes marquee {
    0% {
      transform: translateX(0);
    }
    100% {
      transform: translateX(-100%);
    }
  }
  .scrolling-text {
    background: #fff;
    overflow: hidden;
    margin: 0;
    white-space: nowrap;
    width: 100%;
  }

  .scrolling-text text {
    display: inline-block;
    padding-left: 100%;
    animation: marquee 30s linear infinite; /* 调整动画时长和效果 */
  }
}
::v-deep .u-icon__icon {
  font-size: 18px !important;
}
.u-button--primary {
  background-color: #33bcd7 !important;
  border-color: #33bcd7 !important;
}
.content-last-next {
  height: 10px;
  width: 35%;
  margin: auto; //居中
  padding: 11px 0 22px 57px;
  color: #fff;
  font-size: 15px;
  border-radius: 20px;
}
</style>
