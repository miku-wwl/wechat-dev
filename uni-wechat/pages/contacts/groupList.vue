<template>
	<view>
		<view class="myInfo">
			
			<view class="msg-list-box">
				
				
				<view
					v-for="(msg, index) in groupList" :key="msg.face"
					:class="{'msg-item-notouch':!msg.isTouched, 'msg-item-touched': msg.isTouched, 'single-msg-item-wrapper': true, 'bottom': index == groupList.length - 1}"
					@touchstart="msg.isTouched=true"
					@touchend="msg.isTouched=false"
					@click="gotoWatchFriend()">
					
					<view :class="{'single-msg-item': true}">
						<!-- 对话图，用户头像或群聊组合图 -->
						<view class="group-cover">
							<image class="member-face" :src="groupList[0].face"></image>
							<image class="member-face" :src="groupList[1].face"></image>
							<image class="member-face" :src="groupList[2].face"></image>
							<image class="member-face" :src="groupList[3].face"></image>
							<image class="member-face" :src="groupList[4].face"></image>
							<image class="member-face" :src="groupList[5].face"></image>
							<image class="member-face" :src="groupList[6].face"></image>
							<image class="member-face" :src="groupList[7].face"></image>
							<image class="member-face" :src="groupList[8].face"></image>
						</view>
						
						<view :class="{'chat-msg-wrapper': true, 'bottom': index != groupList.length - 1}">
							<view class="chat-box">
								<view class="chat-title">{{msg.groupName}}</view>
							</view>
							
							<view class="status-box">
							</view>
						</view>
					</view>
					<!-- #ifdef H5 -->
					<!-- <view v-if="index == groupList.length - 1" style="height: 44px; width: 300px;"></view> -->
					<!-- #endif -->
				</view>
				<view class="bottom-msg ">{{groupList.length}}个群聊</view>
			
			</view>
			
		</view>
		
	</view>
</template>

<script>
	var app = getApp();
	let sys = uni.getSystemInfoSync();
	export default {
		data() {
			return {
				env: app.getAppEnv(),
				uniBadgeStyle: {
					backgroundColor: "#f43530"
				},
				
				fixNavigationBar: false,
				
				showMe: false,
				opacity: 0,
				scrollY: 0,
				
				chatListHeight: 0,
				chatListPaddingTop: 0,
				
				chatGLMTouched: false,
				msgItemTouched: false,
				verifyMessageTouched: false,
				newFriendTouched: false,
				groupChatTouched: false,
				blackFriendTouched: false,
				
				groupList: [
					{
						face: "../../static/faces/face-003.png",
						groupName: "Java 线上技术交流大会",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-004.png",
						groupName: "前端学习群",
						isTouched: false,
					},
					
					{
						face: "../../static/faces/face-005.png",
						groupName: "风间影月的知识星球",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-009.png",
						groupName: "Java 架构师2.0学习群",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-010.png",
						groupName: "Vip终身会员交流群",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-014.png",
						groupName: "PMP项目管理学习群",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-008.png",
						groupName: "Jeep 车友大队",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-002.png",
						groupName: "线下英语角",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-007.png",
						groupName: "工作室",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-013.png",
						groupName: "部门会议群",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-006.png",
						groupName: "总经办",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-012.png",
						groupName: "会计财务",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-001.png",
						groupName: "商务合作群",
						isTouched: false,
					},
					{
						face: "../../static/faces/face-011.png",
						groupName: "办公用品采购群",
						isTouched: false,
					},
				]
				
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = app.getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			
			this.getChatListHeight();
		},
		onPageScroll : function(e) {
			var that = this,
				scrollY = e.scrollTop;
				
			if (scrollY < 0) {
				that.showMe = true;
			}
				
			// var opacity = scrollY / 30;
			// opacity = opacity > 1 ? 1 : opacity;
			// that.$set(that, 'opacity', opacity);
			// that.$set(that, 'scrollY', scrollY);
			// if (that.lock) {
			// 	that.$set(that, 'lock', false);
			// 	return;
			// }
		},
		onNavigationBarButtonTap(e) {
			console.log(e);
		},
		onLoad(e) {
		},
		methods:{
			
			gotoWatchFriend() {
				uni.navigateTo({
					url: "/pages/contacts/friendPage"
				})
			},
			
			gotoAddFriend() {
				uni.navigateTo({
					url: "/pages/contacts/addFriend"
				})
			},
			
			goToSomeWhere(id) {
				var chatListPaddingTop = this.chatListPaddingTop;
				
				uni.pageScrollTo({
					selector: '#' + id,
					duration: 0
				})
				
			    // 获取目标元素
			  //   uni.createSelectorQuery().select('#' + id).boundingClientRect(function(rect){
					// console.log("rect.top = " + rect.top);
					// uni.pageScrollTo({
					// 	scrollTop: rect.top + 'rpx',
					// 	duration: 0
					// })
			  //   }).exec();
			},
			
			getChatListHeight() {
				// https://uniapp.dcloud.net.cn/tutorial/syntax-css.html#%E5%9B%BA%E5%AE%9A%E5%80%BC
				// 页面高度 =  屏幕高度 -（状态栏高度+导航栏高度+tab栏高度）
				
				var windowHeight = sys.windowHeight;
				
				var statusBarHeight = sys.statusBarHeight;
				// console.log("statusBarHeight = " + statusBarHeight)
				
				var navigationBarHeight = 44;
				var tabBarHeight = 50;
				
				var chatListHeight = windowHeight - (statusBarHeight + navigationBarHeight);
				this.chatListHeight = chatListHeight;
				
				// H5额外减去导航栏的高度
				// // #ifdef H5
				// this.chatListHeight = chatListHeight - 44;
				// // #endif
				
				this.chatListPaddingTop = statusBarHeight + navigationBarHeight;
				// console.log("this.chatListPaddingTop = " + this.chatListPaddingTop);
			},

		}
	}
</script>

<style>
	@import url("groupList.css");
</style>
