<!-- 设置 -->
<template>
	<view>
		
		<view class="block-one" style="border-bottom: 0.5px solid #ebebeb;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineChooseImgTouched, 'item-line-touched': lineChooseImgTouched, 'block-line': true}"
					@touchstart="lineChooseImgTouched=true"
					@touchend="lineChooseImgTouched=false"
					@click="changeChatBG">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">选择背景图</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		<view class="block-one" style="border-bottom: 0.5px solid #ebebeb;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineDefaultTouched, 'item-line-touched': lineDefaultTouched, 'block-line': true}"
					@touchstart="lineDefaultTouched=true"
					@touchend="lineDefaultTouched=false"
					@click="returnToDefault">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">恢复默认</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
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
				lineChooseImgTouched: false,
				lineDefaultTouched: false,
				
				whoami: "",
			}
		},
		onShow() {
			var userType = uni.getStorageSync('userType');
			console.log(userType);
			
		},
		onLoad() {
		},
		methods: {
			returnToDefault() {
				
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var pendingUserInfo = {
					"userId": userId,
					"chatBg": "http://127.0.0.1:9000/weilai/chatBg/1749256670632050690/5b364a72-ae94-4d5c-a13b-44df03c5b1fd.PNG",
				};
				
				var serverUrl = app.globalData.serverUrl;
				
				console.log(pendingUserInfo);
				console.log(serverUrl);
				
				// 修改昵称
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
								title: "聊天背景恢复成功~",
								duration: 1500,
							})
						} else {
							console.log(333);
							uni.showToast({
								title: result.data.msg,
								icon: "none",
								duration: 3000
							});
						}
						
					}
				})
				
			},
			
			changeChatBG() {
				var me = this;
				
				uni.chooseImage({
					count: 1,
					sourceType: ["album"],
					success: (e) => {
						var newBg = e.tempFilePaths[0];
						me.newBg = newBg;
						
						me.updateNewChatBg(newBg);
					}
				})
			},
			
			updateNewChatBg(newBg) {
				// 上传
				var serverUrl = app.globalData.serverUrl;
				var userId = app.getUserInfoSession().id;
				
				uni.uploadFile({
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/file/uploadChatBg?userId=" + userId,
					name: "file",
					filePath: newBg,
					success(result) {
						// console.log(result);
						// console.log(result.data);
						
						if (result.statusCode == 200) {
							var res = JSON.parse(result.data);
							// console.log(res);
							
							if (res.status == 200) {
								var userInfo = res.data;
								// console.log(userInfo);
								// 重置本地用户信息
								app.setUserInfoSession(userInfo);
								app.setUserSessionToken(userInfo.userToken);
								
								uni.showToast({
									title: "聊天背景更换成功~",
									duration: 1500
								})
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'error'
								})
							}
						} else {
							uni.showToast({
								title: "上传失败",
								icon: 'error'
							})
						}
					}
				})
			},
			
		}
	}
</script>

<style>
	@import url("chatBG.css");
</style>