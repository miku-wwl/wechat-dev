<template>
	<view class="content">
		
		<view class="icon-wrapper">
			<image class="icon-wechat" src="../../static/icons/icon-wechat.png" :style="{paddingTop: iconTopDistance +'upx'}"></image>
		</view>
		
		<view class="logo" :style="{bottom: logoBottomDistance + 'upx'}">
			<text class="logo-text" style="font-size: 14px; font-weight: 400;">Java架构师2.0 - 仿微信</text>
			<text class="logo-text" style="font-size: 14px; font-weight: 200;">By: 风间影月</text>
		</view>
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
				
				iconTopDistance: 0,
				logoBottomDistance: 0,
			}
		},
		onLoad() {
			
			var windowWidth = sys.windowWidth;
			var windowHeight = sys.windowHeight;
			
			
			if (windowWidth < windowHeight) {
				var range = windowHeight/windowWidth;
				// console.log(windowHeight / windowWidth);
				// 高比宽比例越大，图标距离顶部越远，按照比例计算
				this.iconTopDistance = 280 * range;
				this.logoBottomDistance = 60 * range;
			} else if (windowWidth >= windowHeight) {
				var range = windowWidth/windowHeight;
				console.log(range);
				// 高比宽比例越大，图标距离顶部越远，按照比例计算
				this.iconTopDistance = 280 * range;
				this.logoBottomDistance = 60 * range;
			}
			
			
			// uni.setTabBarItem({
			// 	index: 4,
			// 	visible: false
			// });
			// uni.hideTabBar();
			
			// 判断当前用户是否登录，如果没有登录，则跳转至登录页面，如果已经登录，则继续流程
			var me = this;
			var userIsLogin = app.userIsLogin();
			this.userIsLogin = userIsLogin;
			setTimeout(function () {
				if (userIsLogin) {
					me.goto();
				} else {
					me.goLogin();
				}
			}, 2500);
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
			// }, 2500);
		},
		methods: {
			goLogin() {
				uni.navigateTo({
					url: "../loginRegist/loginRegist",
					animationType: "fade-in"
				})
			},
			
			goto() {
				var me = this;
				var userType = uni.getStorageSync('userType');
				// 如果没有，默认设置为候选人
				if (userType == undefined || userType == "" || userType == null) {
					uni.setStorageSync('userType', me.userType.candidate);
				}
				
				
			}
		}
	}
</script>

<style>
	@import url("welcome1.css");
</style>
