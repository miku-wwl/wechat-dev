<!-- 别人添加我，我需要验证该好友请求 -->
<template>
	<view>
		<view class="myInfo">
			
			<view class="real-me">
				<image :src="newFriend.face" class="my-face"></image>
				
				<view class="my-name-wrapper">
					<view class="header-left-part">
						<view class="my-name-box">
							<!-- 如果有备注，则此处显示备注名 -->
							<text class="my-name">{{newFriend.nickname}}</text>
							
							<image src="../../static/icons/wechat/sex-man.png" v-if="newFriend.sex == 1" class="sex-icon"></image>
							<image src="../../static/icons/wechat/sex-woman.png" v-if="newFriend.sex == 0" class="sex-icon"></image>
						</view>
						<view class="edit-wrapper">
							<!-- 如果有备注，此处显示昵称 -->
							<text class="user-info-words">昵称：{{newFriend.nickname}}</text>
							<text class="user-info-words">微信号：{{newFriend.wechatNum}}</text>
							<!-- 地区为空则不显示 -->
							<text class="user-info-words" v-if="newFriend.province != null && newFriend.province != ''">地区：{{newFriend.province}} {{newFriend.city}}</text>
						</view>
					</view>
					
					<view class="header-right-part">
					</view>
				</view>
			</view>
			
			<view class="msg-wrapper">
				<view class="msg-box">
					<text class="msg-words">验证消息：{{newFriendRequest.verifyMessage}}</text>
				</view>
			</view>
			
		</view>
		
		<view
			:class="{'options-item-notouch':!lineGoVerifyTouched, 'options-item-touched': lineGoVerifyTouched, 'options-item': true}"
			@touchstart="lineGoVerifyTouched=true"
			@touchend="lineGoVerifyTouched=false"
			style="margin-top: 20upx;justify-content: center;"  @click="gotoFinishVerify()">
			<text class="logout-words">前往验证</text>
		</view>
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			return {
				env: app.getAppEnv(),
				
				lineSignTouched: false,
				linePhotoTouched: false,
				lineVideoTouched: false,
				lineGoVerifyTouched: false,
				
				currentUserInfo: null, 
				
				newFriendRequest: {},
				newFriend: {}
			}
		},
		onShow() {
			// 获得用户信息
		},
		onLoad(params) {
			var newFriendRequest = JSON.parse(params.newFriendRequest);
			// console.log(newFriendRequest);
			this.newFriendRequest = newFriendRequest;
			console.log("newFriendRequest.myFriendId = " + newFriendRequest.myFriendId);
			this.getUserInfo(newFriendRequest.myFriendId);
		},
		onPageScroll : function(e) {
		},
		// onNavigationBarButtonTap() {
		// 	this.gotoFinishVerify();
		// },
		methods:{
			
			getUserInfo(myFriendId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/userinfo/get?userId=" + myFriendId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var userInfo = result.data.data;
							me.newFriend = userInfo;
							// console.log(currentSourceUserInfo);
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
			
			gotoFinishVerify() {
				var newFriendRequestId = this.newFriendRequest.friendRequestId;
				uni.navigateTo({
					url: "passFriendRequest?newFriendRequestId=" + newFriendRequestId,
					animationType: "slide-in-bottom"
				})
			},
			
			goToMyPage() {
				
			},
			
		}
	}
</script>

<style>
	@import url("verifyFriend.css");
</style>
