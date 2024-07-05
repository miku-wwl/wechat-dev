<!-- 修改昵称页面 -->
<template>
	<view class="page">
		
		<view class="title-wrapper">
			<text class="update-title-words">填写新的微信号</text>
			<text class="title-tips">微信号长度限6-20位，建议避免包含姓名、生日等涉及个人隐私信息。</text>
			
			<view class="number-box">
				<text class="lable-wechat">微信号</text>
				<input
					class="number-input"
					type="text" 
					:value="wechatNum" 
					:model="wechatNum" 
					placeholder=""
					maxlength="20"
					@input="typingContent"/>
			</view>
			
			<text class="tips">微信号是账号的唯一凭证，一年只能修改一次。</text> 
			
		</view>
		
		<view :class="{'button-box-green': showGreen, 'button-box-gray': !showGreen,'button-box': true}" @click="updateWechatNum()">
			<text class="button-words">修改微信号</text>
		</view>
		
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			return {
				// oldNickname: getApp().getUserInfoSession().nickname,
				// nickname: getApp().getUserInfoSession().nickname,
				wechatNum: "",
				wordsLength: 0,
				
				showGreen: false,
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.wechatNum = currentUserInfo.wechatNum;
			console.log("wechatNum = " + currentUserInfo.wechatNum);
		},
		methods: {
			updateWechatNum() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var wechatNum = this.wechatNum;
				
				var pendingUserInfo = {
					"userId": userId,
					"wechatNum": wechatNum
				};
				
				// 修改昵称
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/userinfo/modify",
					data: pendingUserInfo,
					success(result) {
						
						if (result.data.status == 200) {
							var userInfo = result.data.data;
							// 重置本地用户信息
							app.setUserInfoSession(userInfo);
							app.setUserSessionToken(userInfo.userToken);
							
							uni.showToast({
								title: "修改成功~",
								duration: 1500,
								// success() {
								// 	uni.navigateBack({
								// 		delta: 1,
								// 		animationType: "fade-out"
								// 	})
								// }
							})
						} else {
							uni.showToast({
								title: result.data.msg,
								icon: "none",
								duration: 3000
							});
						}
						
					}
				})
			},
			
			typingContent(e) {
				var me = this;
				
				var event = e;
				var wechatNum = e.detail.value;
				this.wechatNum = wechatNum;
				var wordsLength = wechatNum.length;
				this.wordsLength = wordsLength;
				console.log(wordsLength);
				if (wordsLength >= 6) {
					me.showGreen = true;
				} else {
					me.showGreen = false;
				}
			},
		}
	}
</script>

<style>
	@import url("myWechatNumber.css");
</style>
