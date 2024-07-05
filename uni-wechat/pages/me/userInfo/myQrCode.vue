<!-- 修改昵称页面 -->
<template>
	<view class="page">
		
		<view class="qrcode-box">
			
			<view class="face-box">
			
				<image :src="currentUserInfo.face" class="face"></image>
				
				<view class="my-info-box">
					<text class="nickname">{{currentUserInfo.nickname}}</text>
					<text class="address" v-if="currentUserInfo.city != null && currentUserInfo.city != '' && currentUserInfo.province != null && currentUserInfo.province != ''">{{currentUserInfo.province}} {{currentUserInfo.city}}</text>
				</view>
			
			</view>
			
			<view class="qrcode-wrapper">
				<image :src="currentUserInfo.wechatNumImg" class="qrcode"></image>
				
				<text class="tips">扫一扫上面的二维码图案，加我为朋友。</text>
			</view>
			
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
				wechatNumber: "",
				wordsLength: 0,
				
				showGreen: false,
				currentUserInfo: null
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
		},
		methods: {
			updateNickname() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var nickname = this.nickname;
				
				var pendingUserInfo = {
					"userId": userId,
					"nickname": nickname
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
				var wechatNumber = e.detail.value;
				this.wechatNumber = wechatNumber;
				var wordsLength = wechatNumber.length;
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
	@import url("myQrCode.css");
</style>
