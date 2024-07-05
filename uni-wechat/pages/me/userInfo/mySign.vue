<!-- 修改昵称页面 -->
<template>
	<view class="page">
				
			<view class="signature-box">
				<textarea class="signature-input" :value="signature" @input="typing" :model="signature" maxlength="30"/>
				<text style="align-self: flex-end; color: lightgray;">{{leftWordsLength}}</text>
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
				signature: "",
				wordsLength: 0,
				leftWordsLength: 30,
				
				currentUserInfo: null,
			}
		},
		onNavigationBarButtonTap() {
			var signature = this.signature;
			console.log("signature.length = " + signature.length);
			if (signature.length > 30 || signature.length < 1) {
				uni.showToast({
					icon: "none",
					title: "个性签名长度为1-30"
				})
				return;
			}
			
			this.updateSign();
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.signature = currentUserInfo.signature;
			
			// this.wordsLength = this.nickname.length;
		},
		methods: {
			typing(e) {
				var event = e;
				var signature = e.detail.value;
				this.signature = signature;
				this.leftWordsLength = 30 - signature.length;
			},
			
			updateSign() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var signature = this.signature;
				
				var pendingUserInfo = {
					"userId": userId,
					"signature": signature
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
								success() {
									uni.navigateBack({
										delta: 1,
									})
								}
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
		}
	}
</script>

<style>
	@import url("mySign.css");
</style>
