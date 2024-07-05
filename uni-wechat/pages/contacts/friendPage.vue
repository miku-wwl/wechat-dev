<template>
	<view>
		<view class="myInfo">
			
			<view class="real-me">
				<image :src="friendInfo.face" class="my-face"></image>
				
				<view class="my-name-wrapper">
					<view class="header-left-part">
						<view class="my-name-box">
							<!-- 如果有备注，则此处显示备注名 -->
							<text class="my-name">{{friendInfo.nickname}}{{(friendship.friendRemark != "" && friendship.friendRemark != null) ? "(" + friendship.friendRemark + ")" : ""}}</text>
							
							<image src="../../static/icons/wechat/sex-man.png" v-if="friendInfo.sex == 1" class="sex-icon"></image>
							<image src="../../static/icons/wechat/sex-woman.png" v-if="friendInfo.sex == 0" class="sex-icon"></image>
						</view>
						<view class="edit-wrapper">
							<!-- 如果有备注，此处显示昵称 -->
							<text class="user-info-words">昵称：{{friendInfo.nickname}}</text>
							<text class="user-info-words">微信号：{{friendInfo.wechatNum}}</text>
							<!-- 地区为空则不显示 -->
							<text class="user-info-words">地区：{{friendInfo.province}} {{friendInfo.city}}</text>
						</view>
					</view>
					
					<view class="header-right-part">
					</view>
				</view>
			</view>
			
			<!-- <view class="msg-wrapper">
				<view class="msg-box">
					<text class="msg-words">验证消息：你好~我是xxx</text>
				</view>
			</view> -->
			
		</view>
		
		<view class="block-one" style="margin-top: 20upx;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineSignTouched, 'item-line-touched': lineSignTouched, 'block-line': true}"
					@touchstart="lineSignTouched=true"
					@touchend="lineSignTouched=false"
					@click="">
					<view class="whole-line bottom-line">
						<text class="left-part-words">个性签名</text>
						
						<view class="right-part">
							<text class="sign-words">{{friendInfo.signature}}</text>
						</view>
					</view>
				</view>
				
				<!-- <view
					:class="{'item-line-notouch':!linePhotoTouched, 'item-line-touched': linePhotoTouched, 'block-line': true}"
					@touchstart="linePhotoTouched=true"
					@touchend="linePhotoTouched=false"
					@click="">
					<view class="whole-line bottom-line" style="line-height: 90px;">
						<text class="left-part-words">朋友圈</text>
						
						<view class="right-part">
							<view class="photo-list">
								<image src="../../static/faces/face-001.png" class="friend-imgs"></image>
								<image src="../../static/faces/face-002.png" class="friend-imgs"></image>
								<image src="../../static/faces/face-003.png" class="friend-imgs"></image>
								<image src="../../static/faces/face-004.png" class="friend-imgs"></image>
								<image src="../../static/faces/face-007.png" class="friend-imgs"></image>
							</view>
							
							<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
						</view>
					</view>
				</view> -->
				
				<!-- <view
					:class="{'item-line-notouch':!lineVideoTouched, 'item-line-touched': lineVideoTouched, 'block-line': true}"
					@touchstart="lineVideoTouched=true"
					@touchend="lineVideoTouched=false"
					@click="goToVideoNumber()">
					<view class="whole-line bottom-line" style="line-height: 50px;padding-bottom: 20px;">
						<text class="left-part-words">视频号</text>
						
						<view class="right-part" style="flex-direction: column;">
							
							<text class="video-number-name">风间影月</text>
							
							<view class="video-num-wrapper">
								<view class="photo-list">
									<image src="../../static/faces/face-001.png" class="friend-imgs"></image>
									<image src="../../static/faces/face-002.png" class="friend-imgs"></image>
									<image src="../../static/faces/face-003.png" class="friend-imgs"></image>
									<image src="../../static/faces/face-004.png" class="friend-imgs"></image>
									<image src="../../static/faces/face-007.png" class="friend-imgs"></image>
								</view>
								
								<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
							</view>
						</view>
					</view>
				</view> -->
			</view>
		</view>
		
		<view
			:class="{'options-item-notouch':!lineGoVerifyTouched, 'options-item-touched': lineGoVerifyTouched, 'options-item': true}"
			@touchstart="lineGoVerifyTouched=true"
			@touchend="lineGoVerifyTouched=false"
			style="margin-top: 20upx;justify-content: center;"  @click="goChat">
			<text class="logout-words">发消息</text>
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
				friendInfo: {},
				friendship: {},
			}
		},
		onShow() {
			// 获得用户信息
		},
		onLoad(params) {
			var friendId = params.friendId;
			console.log(friendId);
			this.friendId = friendId;
			this.getUserInfo(friendId);
			this.getFriendship(friendId);
		},
		onNavigationBarButtonTap() {
			var friendId = this.friendId;
			console.log(friendId);
			uni.navigateTo({
				url: "friendSetting?friendId=" + friendId,
				animationType: "slide-in-bottom"
			})
		},
		onPageScroll : function(e) {
		},
		methods:{
			goChat() {
				var friendId = this.friendId;
				uni.navigateTo({
					url: "/pages/msgList/singleChat?friendId=" + friendId,
				})
			},
			
			getFriendship(friendId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendship/getFriendship?friendId=" + friendId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var friendship = result.data.data;
							me.friendship = friendship;
							console.log(friendship);
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
			
			getUserInfo(friendId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/userinfo/get?userId=" + friendId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var userInfo = result.data.data;
							me.friendInfo = userInfo;
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
			
			goToMyPage() {
				
			},
			
			goToVideoNumber() {
				uni.showToast({
					title: "本功能暂未开通",
					icon: "none"
				})
			}
		}
	}
</script>

<style>
	@import url("verifyFriend.css");
</style>
