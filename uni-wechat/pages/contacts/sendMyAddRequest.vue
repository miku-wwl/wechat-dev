<!-- 我添加别人，需要发送的请求 -->
<template>
	<view class="page">
		
		<view class="request-wrapper">
			<text class="title-tips">发送添加朋友申请</text>
			
			<view class="remark-wrapper">
				<textarea
					:value="requestWords"
					:model="requestWords" 
					maxlength="30" 
					@input="typingRequestWords"
					class="textarea"/>
			</view>
		</view>
		
		<view class="title-wrapper">
			<text class="title-tips">设置备注</text>
			
			<view class="remark-wrapper">
				<input
					class="remark-input"
					type="text" 
					:value="remark" 
					:model="remark"
					@input="typingRemark"
					maxlength="10"/>
			</view>
		</view>
		
		<view class="button-box-green button-box" @click="sendVerify">
			<text class="button-words">发送</text>
		</view>
		
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			return {
				requestWords: "",
				remark: "",
				// wordsLength: 0,
				
				friendStr: "",
				friend: {},
			}
		},
		onLoad(params) {
			var friendStr = params.friend;
			var friend = JSON.parse(friendStr);
			console.log(friend);
			this.friendStr = friendStr;
			this.friend = friend;
		},
		methods: {
			
			sendVerify() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var friend = this.friend;
				
				var requestWords = this.requestWords;
				var remark = this.remark;
				
				console.log("requestWords = " + requestWords);
				console.log("remark = " + remark);
				
				var pendingFriendRequest = {
					myId: userId,
					friendId: friend.id,
					verifyMessage: requestWords,
					friendRemark: remark,
				}
				console.log(pendingFriendRequest);
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendRequest/add",
					data: pendingFriendRequest,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							console.log(222);
							var userInfo = result.data.data;
							console.log(userInfo);
							
							uni.showToast({
								title: "好友请求已发送！",
								icon: "none",
								duration: 2000
							});
							
							// var goback = function(){
							// 	uni.navigateBack({
							// 		delta: 1
							// 	})
							// };
							// var smsTimer = setInterval(goback, 1500);
						} else {
							console.log(333);
							uni.showToast({
								title: result.data.data.verifyMessage,
								icon: "none",
								duration: 3000
							});
						}
					}
				})
			},
			
			typingRequestWords(e) {
				var me = this;
				
				var event = e;
				var requestWords = e.detail.value;
				this.requestWords = requestWords;
			},
			
			typingRemark(e) {
				var me = this;
				
				var event = e;
				var remark = e.detail.value;
				this.remark = remark;
			}
			
			
		}
	}
</script>

<style>
	@import url("sendMyAddRequest.css");
</style>
