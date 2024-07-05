<!-- 我添加别人为好友，搜索到的好友页面 -->
<template>
	<view>
		<view class="myInfo">
			
			<view class="real-me">
				<image :src="friend.face" class="my-face"></image>
				
				<view class="my-name-wrapper">
					<view class="header-left-part">
						<view class="my-name-box">
							<!-- 如果有备注，则此处显示备注名 -->
							<text class="my-name">{{friend.nickname}}</text>
							
							<image src="../../static/icons/wechat/sex-man.png" v-if="friend.sex == 1" class="sex-icon"></image>
							<image src="../../static/icons/wechat/sex-woman.png" v-if="friend.sex == 0" class="sex-icon"></image>
						</view>
						<view class="edit-wrapper">
							<!-- 如果有备注，此处显示昵称 -->
							<text class="user-info-words">昵称：{{friend.nickname}}</text>
							<text class="user-info-words">微信号：{{friend.wechatNum}}</text>
							<!-- 地区为空则不显示 -->
							<text class="user-info-words" v-if="friend.province != null && friend.province != ''">地区：{{friend.province}} {{friend.city}}</text>
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
		
		<view
			:class="{'options-item-notouch':!lineGoVerifyTouched, 'options-item-touched': lineGoVerifyTouched, 'options-item': true}"
			@touchstart="lineGoVerifyTouched=true"
			@touchend="lineGoVerifyTouched=false"
			style="margin-top: 20upx;justify-content: center;"  @click="goSendRequest()">
			<text class="logout-words">添加到通讯录</text>
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
				friendStr: "",
				friend: {},
			}
		},
		onShow() {
			// 获得用户信息
		},
		onLoad(params) {
			var friendStr = params.friend;
			var friend = JSON.parse(friendStr);
			console.log(friend);
			this.friendStr = friendStr;
			this.friend = friend;
		},
		onPageScroll : function(e) {
		},
		onNavigationBarButtonTap() {
			this.goSendRequest();
		},
		methods:{
			
			goSendRequest() {
				var friendStr = this.friendStr;
				uni.navigateTo({
					url: "sendMyAddRequest?friend=" + friendStr,
					animationType: "slide-in-bottom"
				})
			},
			
			goToMyPage() {
				
			},
			
		}
	}
</script>

<style>
	@import url("searchedFriend.css");
</style>
