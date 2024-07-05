<!-- 设置 -->
<template>
	<view>
		<view class="block-one">
			<view class="block-box">
				<view
					:class="{'block-line': true}"
					@click="goToSettings">
					<view class="right-part" style="line-height: 55px;">
						<image :src="currentUserInfo.face" class="face"></image>
						<text class="item-words" style="font-size: 15px; color: gray;font-weight: 400;margin-right: 12px;">{{currentUserInfo.nickname}}</text>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one" style="margin-top: 20upx;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineAccountTouched, 'item-line-touched': lineAccountTouched, 'block-line': true}"
					@touchstart="lineAccountTouched=true"
					@touchend="lineAccountTouched=false"
					@click="goToMyPage">
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">账号信息</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
				
				<view
					:class="{'item-line-notouch':!lineChatBGTouched, 'item-line-touched': lineChatBGTouched, 'block-line': true}"
					@touchstart="lineChatBGTouched=true"
					@touchend="lineChatBGTouched=false"
					@click="goToChatBG">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">聊天背景</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one" style="margin-top: 20upx;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineMoreCoursesTouched, 'item-line-touched': lineMoreCoursesTouched, 'block-line': true}"
					@touchstart="lineMoreCoursesTouched=true"
					@touchend="lineMoreCoursesTouched=false"
					@click="goToMyCourses">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">更多课程</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view 
			:class="{'options-item-notouch':!logoutTouched, 'options-item-touched': logoutTouched, 'options-item': true}"
			@touchstart="logoutTouched=true"
			@touchend="logoutTouched=false"
			style="margin-top: 20upx;justify-content: center;"  @click="logout()">
			<text class="logout-words">退出登录</text>
		</view>
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		components: {
		},
		data() {
			return {
				lineAccountTouched: false,
				lineChatBGTouched: false,
				lineMoreCoursesTouched: false,
				logoutTouched: false,
				
				whoami: "",
				currentUserInfo: {},
			}
		},
		onShow() {
			var userType = uni.getStorageSync('userType');
			// console.log(userType);
			
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			
		},
		onLoad() {
		},
		methods: {
			logout() {
				var me = this;
				var userId = app.getUserInfoSession().id;
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/passport/logout?userId=" + userId,
					success(result) {
						var status = result.data.status;
						if (status == 200) {
							app.clearUserToken();
							// 退出则关闭ws连接 TODO
							// app.closeWSConnect();
							uni.reLaunch({
								url: "../loginRegist/loginBefore"
							})
						} else if (status != 200) {
							uni.showToast({
								title: result.data.msg,
								icon: "none"
							});
						}
						
					}
				});
			},
			
			goToMyPage() {
				uni.navigateTo({
					url: "../me/myPage",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
			goToChatBG() {
				uni.navigateTo({
					url: "chatBG",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
			goToMyCourses() {
				uni.navigateTo({
					url: "../others/moreCourse",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
		}
	}
</script>

<style>
	@import url("settings.css");
</style>