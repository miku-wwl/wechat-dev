<!-- 修改昵称页面 -->
<template>
	<view class="page">
		
		<view class="title-wrapper">
			<text class="title-tips">设置备注</text>
			
			<view class="remark-wrapper">
				<input
					class="remark-input"
					type="text" 
					:value="remark" 
					:model="remark" 
					placeholder=""
					maxlength="10"
					@input="typingContent"/>
			</view>
		</view>
		
		<view :class="{'button-box-green': showGreen, 'button-box-gray': !showGreen,'button-box': true}" @click="passRequest()">
			<text class="button-words">完成</text>
		</view>
		
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			return {
				remark: "",
				wordsLength: 0,
				
				showGreen: false,
				
				newFriendRequestId: "",
			}
		},
		onLoad(params) {
			var newFriendRequestId = params.newFriendRequestId;
			this.newFriendRequestId = newFriendRequestId;
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.judgeButtonGreen(this.remark);
		},
		methods: {
			
			passRequest() {
				var friendRemark = this.remark;
				var newFriendRequestId = this.newFriendRequestId;
				console.log(friendRemark);
				console.log(newFriendRequestId);
				
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendRequest/pass?friendRequestId=" + newFriendRequestId + "&friendRemark=" + friendRemark,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var userInfo = result.data.data;
							me.newFriend = userInfo;
							// console.log(currentSourceUserInfo);
							uni.showToast({
								title: "好友请求已通过",
								icon: "none",
								duration: 2500,
								success() {
									setTimeout(()=>{
										uni.navigateBack({
											delta: 2,
										})
									}, 2000)
								}
							});
							
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
				var remark = e.detail.value;
				this.remark = remark;
				var wordsLength = remark.length;
				this.wordsLength = wordsLength;
				// console.log(wordsLength);
				this.judgeButtonGreen(remark);
			},
			
			judgeButtonGreen(remark) {
				var wordsLength = remark.length;
				var me = this;
				if (wordsLength >= 1) {
					me.showGreen = true;
				} else {
					me.showGreen = false;
				}
			}
			
		}
	}
</script>

<style>
	@import url("passFriendRequest.css");
</style>
