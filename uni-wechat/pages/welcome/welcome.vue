<template>
	<view class="content">
		
		<view class="up-side" :style="{height: upSideHeight+'px', width: starterImgWidth + 'px'}"></view>
		
		<image class="middle-side" src="../../static/images/start_img.png" :style="{height: starterImgHeight+'px', width: starterImgWidth + 'px'}" ></image>
		
		<view class="down-side" :style="{height: downSideHeight+'px', width: starterImgWidth + 'px'}"></view>
	</view>
</template>

<script>
	var app = getApp();
	let sys = uni.getSystemInfoSync();
	export default {
		data() {
			return {
				userIsLogin: false,
				title: 'Hello Lee',
				
				// 获得屏幕宽度，同时动态赋值给中级图片的高度，形成正方形
				starterImgWidth: sys.windowWidth >= sys.windowHeight ? sys.windowHeight : sys.windowWidth,
				starterImgHeight: sys.windowWidth >= sys.windowHeight ? sys.windowHeight : sys.windowWidth,
				
				upSideHeight: 0,
				downSideHeight: 0,
			}
		},
		// watch() {
			
		// },
		onLoad() {
			var windowHeight = sys.windowHeight;
			var leftHeight = windowHeight - this.starterImgHeight;
			this.upSideHeight = leftHeight / 3 * 1;
			this.downSideHeight = leftHeight / 3 * 2;
			// console.log(this.upSideHeight);
			// console.log(this.downSideHeight);
			
			// uni.setTabBarItem({
			// 	index: 4,
			// 	visible: false
			// });
			// uni.hideTabBar();
			
			// 用于清理用户缓存，测试的，这个后面要注释
			// app.clearUserInfo();
			
			// 判断当前用户是否登录，如果没有登录，则跳转至登录页面，如果已经登录，则继续流程
			var me = this;
			var userIsLogin = app.userIsLogin();
			// userIsLogin = true;
			// app.clearUserInfo();
			this.userIsLogin = userIsLogin;
			setTimeout(function () {
				if (userIsLogin) {
					me.goto();
				} else {
					
					// 如果用户信息存在，则跳转到 loginBefore，否则跳转到登录页面
					// var userInfo = app.getUserInfoSession();
					// if (!app.isStrEmpty(userInfo)) {
					// 	me.goLoginBefore();
					// } else {
					// 	me.goLogin();
					// }
					
					me.goLoginBefore();
				}
			}, 100);
		},
		onShow() {
			// var me = this;
			// var userIsLogin = app.userIsLogin();
			// this.userIsLogin = userIsLogin;
			// setTimeout(function () {
			// 	if (userIsLogin) {
			// 		me.goto();
			// 	} else {
			// 		me.goLogin();
			// 	}
			// }, 1500);
		},
		methods: {
			goLoginBefore() {
				uni.redirectTo({
					url: "../loginRegist/loginBefore",
					animationType: "fade-in"
				})
			},
			
			goLogin() {
				uni.redirectTo({
					url: "../loginRegist/loginNewAccount",
					animationType: "fade-in"
				})
			},
			
			goto() {
				var me = this;
				uni.switchTab({
					url: "../msgList/msgList"
				});
			}
		}
	}
</script>

<style>
	@import url("welcome.css");
</style>
