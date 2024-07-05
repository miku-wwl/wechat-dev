<!-- 修改备注名页面 -->
<template>
	<view class="page">
		<!-- <view class="single-line-box" style=""> -->
			
			<input 
				class="remark-input"
				type="text" 
				:value="remark" 
				:model="remark" 
				placeholder="添加备注名"
				maxlength="18"
				@input="typingContent"/>
			
			<!-- <view class="counts-limt" style="margin-top: 10px;">
				<text class="tips">*注：请设置2-12的备注名长度</text> 
				
				<text class="length-text">{{wordsLength}}/12</text> 
			</view> -->
		<!-- </view> -->
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			return {
				remark: "",
				wordsLength: 0
			}
		},
		onNavigationBarButtonTap() {
			var remark = this.remark;
			if (remark.length > 18 || remark.length < 1) {
				uni.showToast({
					icon: "none",
					title: "备注长度为1-18"
				})
				return;
			}
			
			this.updateRemark(remark);
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			// this.remark = currentUserInfo.remark;
			
			// this.wordsLength = this.remark.length;
		},
		onLoad(params) {
			var friendId = params.friendId;
			console.log(friendId);
			this.friendId = friendId;
		},
		methods: {
			updateRemark(remark) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var friendId = this.friendId;
				
				// 修改备注名
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendship/updateFriendRemark?friendId=" + friendId  + "&friendRemark=" + remark ,
					success(result) {
						
						if (result.data.status == 200) {
							var userInfo = result.data.data;
							
							uni.showToast({
								title: "修改成功~",
								duration: 1500,
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
				var event = e;
				this.remark = e.detail.value;
				this.wordsLength = this.remark.length;
			},
		}
	}
</script>

<style>
	@import url("friendRemark.css");
</style>
