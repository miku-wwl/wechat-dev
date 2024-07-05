<!-- 设置 -->
<template>
	<view>
		<view class="top-box">
			<view class="one-user">
				<image src="../../static/images/me.png" class="face"></image>
				<text class="nickname" style="">风间影月</text>
			</view>
			
			<view class="plus-box" @click="goChooseUserToGroup()">
				<view class="plus-wrapper">
					<image src="../../static/icons/plus-add.png" class="plus"></image>
				</view>
				<text class="nickname" style=""></text>
			</view>
			
		</view>
		
		<view class="block-one" style="margin-top: 20upx;">
			<view class="block-box">
				<view
					@touchstart="lineChatBGTouched=true"
					@touchend="lineChatBGTouched=false"
					@click="goToChatBG">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">消息免打扰</text>
						<switch :checked="checked == 1" color="#03c25f" style="transform:scale(0.7)"/>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one" style="margin-top: 20upx;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineMoreCoursesTouched, 'item-line-touched': lineMoreCoursesTouched, 'block-line': true}"
					@touchstart="lineMoreCoursesTouched=true"
					@touchend="lineMoreCoursesTouched=false"
					@click="goToChatBG()">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">设置当前聊天背景</text>
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
				checked: 1,
				
				lineAccountTouched: false,
				lineChatBGTouched: false,
				lineMoreCoursesTouched: false,
				logoutTouched: false,
				
				whoami: "",
			}
		},
		onShow() {
			var userType = uni.getStorageSync('userType');
			// console.log(userType);
			
		},
		onLoad() {
		},
		methods: {
			
			goToChatBG() {
				uni.navigateTo({
					url: "../others/chatBG",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
			// goChooseUserToGroup(userId) {
			goChooseUserToGroup() {
				var userId = "1002,1003,";
				// 单聊就发送一个id就行
				var choosedUserIds = userId;
				uni.navigateTo({
					url: "../msgList/chooseUserToGroup?choosedUserIds=" + choosedUserIds,
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
		}
	}
</script>

<style>
	@import url("singleChatDetail.css");
</style>