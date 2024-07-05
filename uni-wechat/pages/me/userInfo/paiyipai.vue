<!-- 修改昵称页面 -->
<template>
	<view class="page">
		
		<view class="title-wrapper">
			<text class="update-title-words">设置拍一拍</text>
			<text class="title-tips"></text>
			
			<view class="number-box">
				<text class="lable-wechat">朋友拍了拍我</text>
				<input
					class="number-input"
					type="text" 
					:value="wechatNumber" 
					:model="wechatNumber" 
					placeholder=""
					maxlength="10"
					@input="typingContent"/>
			</view>
			
			<text class="tips">设置后，朋友拍你的时候将会出现。</text> 
			
		</view>
		
		<view :class="{'button-box-green': showGreen, 'button-box-gray': !showGreen,'button-box': true}">
			<text class="button-words">确定</text>
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
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			
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
				if (wordsLength >= 1) {
					me.showGreen = true;
				} else {
					me.showGreen = false;
				}
			},
		}
	}
</script>

<style>
	@import url("paiyipai.css");
</style>
