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
			
			<view class="plus-box" @click="goRemoveUserFromGroup()">
				<view class="plus-wrapper">
					<image src="../../static/icons/wechat/minus.png" class="plus"></image>
				</view>
				<text class="nickname" style=""></text>
			</view>
			
		</view>
		
		<view class="block-one" style="margin-top: 20upx;" v-if="env == 'weilai'">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineGroupNameTouched, 'item-line-touched': lineGroupNameTouched, 'block-line': true}"
					@touchstart="lineGroupNameTouched=true"
					@touchend="lineGroupNameTouched=false"
					@click="goToEditMyNickname()"
					>
					<view class="left-part">
						<text class="field-lable">群聊名称</text>
					</view>
					
					<view class="right-part">
						<text class="field-words">未命名</text>
						
						<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
					</view>
				</view>
				
				<view class="line-wrapper"><view class="line"></view></view>
				
				<view
					:class="{'item-line-notouch':!lineNoticeTouched, 'item-line-touched': lineNoticeTouched, 'block-line': true}"
					@touchstart="lineNoticeTouched=true"
					@touchend="lineNoticeTouched=false"
					@click="goToEditMyNickname()"
					>
					<view class="left-part">
						<text class="field-lable">群公告</text>
					</view>
					
					<view class="right-part">
						<text class="field-words">未设置</text>
						
						<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one" style="margin-top: 20upx;" v-if="env == 'weilai'">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineNoAlarmTouched, 'item-line-touched': lineNoAlarmTouched, 'block-line': true}"
					@touchstart="lineNoAlarmTouched=true"
					@touchend="lineNoAlarmTouched=false"
					@click="goToEditMyNickname()"
					>
					<view class="left-part">
						<text class="field-lable">消息免打扰</text>
					</view>
					
					<view class="right-part">
						<switch :checked="checked == 1" color="#03c25f" style="transform:scale(0.7)"/>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one" style="margin-top: 20upx;" v-if="env == 'weilai'">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineChatBGTouched, 'item-line-touched': lineChatBGTouched, 'block-line': true}"
					@touchstart="lineChatBGTouched=true"
					@touchend="lineChatBGTouched=false"
					@click="goToEditMyNickname()"
					>
					<view class="left-part">
						<text class="field-lable">设置当前聊天背景</text>
					</view>
					
					<view class="right-part">
						<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one" style="margin-top: 20upx;">
				<view
					:class="{'item-line-notouch':!lineOutTouched, 'item-line-touched': lineOutTouched, 'block-line-out': true}"
					@touchstart="lineOutTouched=true"
					@touchend="lineOutTouched=false"
					@click="quitGroup()"
					style="display: flex;flex-direction: row;justify-content: center;"
					>
					<view class="middle-part">退出群聊</view>
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
				
				lineGroupNameTouched: false,
				lineNoticeTouched: false,
				lineNoAlarmTouched: false,
				lineChatBGTouched: false,
				lineOutTouched: false,
				
				whoami: "",
				
				env: app.getAppEnv(),
			}
		},
		onShow() {
			var userType = uni.getStorageSync('userType');
			// console.log(userType);
			
		},
		onLoad() {
		},
		methods: {
			
			quitGroup() {
				uni.showModal({
					title: "确定退群？",
					cancelText: "不了，我再想想",
					confirmText: "确定，我要退群",
					confirmColor: "#03c25f",
					success(e) {
						if (e.confirm) {
							
							// 跳转入驻认证流程
							
							
						}
					}
				})
			},
			
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
			
			goRemoveUserFromGroup() {
				// 剔除用户只需要添加一个“剔除”标记flag即可
				uni.navigateTo({
					url: "../msgList/chooseUserToGroup?choosedUserIds=&isRemove=1",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
		}
	}
</script>

<style>
	@import url("groupChatDetail.css");
</style>