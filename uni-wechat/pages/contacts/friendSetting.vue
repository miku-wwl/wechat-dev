<!-- 设置 -->
<template>
	<view>
		<view class="block-one">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineAccountTouched, 'item-line-touched': lineAccountTouched, 'block-line': true}"
					@touchstart="lineAccountTouched=true"
					@touchend="lineAccountTouched=false"
					@click="gotoFriendRemark()">
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">设置备注</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one" style="margin-top: 20upx;">
			<view class="block-box">
				<!-- <view
					:class="{'item-line-notouch':!lineMoreCoursesTouched, 'item-line-touched': lineMoreCoursesTouched, 'block-line': true}"
					@touchstart="lineMoreCoursesTouched=true"
					@touchend="lineMoreCoursesTouched=false"
					@click="">
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">设为星标朋友</text>
						<switch checked color="#06c160" style="transform:scale(0.8)"/>
					</view>
				</view> -->
				
				<view
					:class="{'item-line-notouch':!lineChatBGTouched, 'item-line-touched': lineChatBGTouched, 'block-line': true}"
					@touchstart="lineChatBGTouched=true"
					@touchend="lineChatBGTouched=false"
					@click="">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">加入黑名单</text>
						<switch :checked="isBlack" color="#06c160" style="transform:scale(0.8)" @change="switchBlack"/>
					</view>
				</view>
			</view>
		</view>
		
		<view 
			:class="{'options-item-notouch':!logoutTouched, 'options-item-touched': logoutTouched, 'options-item': true}"
			@touchstart="logoutTouched=true"
			@touchend="logoutTouched=false"
			style="margin-top: 20upx;justify-content: center;"  @click="deleteFriend">
			<text class="logout-words">删除联系人</text>
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
				friendId: "",
				
				friendship: {},
				isBlack: true,
			}
		},
		onShow() {
			var userType = uni.getStorageSync('userType');
			// console.log(userType);
			
		},
		onLoad(params) {
			var friendId = params.friendId;
			console.log(friendId);
			this.friendId = friendId;
			this.getFriendship(friendId);
		},
		methods: {
			switchBlack(e) {
				var me = this;
				var isBlack = e.detail.value;
				console.log("isBlack = " + isBlack);
				if (isBlack) {
					me.tobeBlack();
				} else {
					me.moveOutBlack();
				}
			},
			
			deleteFriend() {
				
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var friendId = me.friendId;
				
				uni.showModal({
					title: "确认删除该好友吗？",
					cancelText: "不了，我再想想",
					confirmText: "确定，我要删除",
					confirmColor: "#31B9B3",
					success(e) {
						if (e.confirm) {
							
							var serverUrl = app.globalData.serverUrl;
							uni.request({
								method: "POST",
								header: {
									headerUserId: userId,
									headerUserToken: app.getUserSessionToken()
								},
								url: serverUrl + "/friendship/delete?friendId=" + friendId,
								success(result) {
									console.log(result);
									if (result.data.status == 200) {
										// var friendship = result.data.data;
										// me.friendship = friendship;
										// console.log(friendship);
										uni.showToast({
											icon: "none",
											title: "好友删除成功!"
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
							
						}
					}
				})
				
			},
			
			tobeBlack() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var friendId = me.friendId;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendship/tobeBlack?friendId=" + friendId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							// var friendship = result.data.data;
							// me.friendship = friendship;
							// console.log(friendship);
							
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
			
			moveOutBlack() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var friendId = me.friendId;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendship/moveOutBlack?friendId=" + friendId,
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
							if(friendship.isBlack == 0) {
								me.isBlack = false;
							} else {
								me.isBlack = true;
							}
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
			
			gotoFriendRemark() {
				var friendId = this.friendId;
				console.log(friendId);
				uni.navigateTo({
					url: "friendRemark?friendId=" + friendId,
					animationType: "slide-in-bottom"
				});
			},
			
			logout() {
				var me = this;
				var userId = app.getUserInfoSession().id;
				console.log("logout")
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					url: serverUrl + "/passport/logout?userId=" + userId,
					success(result) {
						var status = result.data.status;
						console.log("logout1222211")
						if (status == 200) {
							console.log("logout111")
							app.clearUserInfo();
							// 退出则关闭ws连接
							app.closeWSConnect();
							uni.reLaunch({
								url: "../loginRegist/loginRegist"
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
	@import url("friendSetting.css");
</style>