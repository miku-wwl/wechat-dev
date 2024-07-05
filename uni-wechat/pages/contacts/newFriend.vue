<template>
	<view>
		<view class="myInfo">
			
			<view class="msg-list-box">
				
				<!-- <view class="search-box">
					<view class="search-wrapper" @click="gotoAddFriend()">
						<view class="search-middle">
							<image class="icon-search" src="../../static/weilai/icon-search.png"></image>
							<text class="search-words">账号/手机号</text>
						</view>
					</view>
				</view> -->
				
				<view
					v-for="(msg, index) in newFriendsList" :key="msg.myFriendFace"
					:class="{'msg-item-notouch':!msg.isTouched, 'msg-item-touched': msg.isTouched, 'single-msg-item-wrapper': true}"
					@touchstart="msg.isTouched=true"
					@touchend="msg.isTouched=false"
					@click="gotoWatchFriend(msg.friendRequestId)">
					
					<view class="single-msg-item">
						<!-- 对话图，用户头像或群聊组合图 -->
						<image class="chat-cover" :src="msg.myFriendFace"></image>
						
						<view class="chat-msg-wrapper">
							<view class="chat-box">
								<view class="chat-title">{{msg.myFriendNickname}}</view>
								<text class="chat-content too-much-words">{{msg.verifyMessage}}</text>
							</view>
							
							<view class="status-box">
								<!-- 不管是别人加我还是我加别人，都会[待审核] -->
								<button v-if="msg.verifyStatus == 0" size="mini" type="default" @click="gotoWatchFriend(msg.friendRequestId)" style="align-self: center;">查看</button>
								
								<image v-if="msg.verifyStatus == 1" class="status-add" src="../../static/icons/wechat/arrow-right-top.png"></image>
								<text v-if="msg.verifyStatus == 1" class="request-status-words">已添加</text>
								<text v-if="msg.verifyStatus == 2" class="request-status-words">已过期</text>
								
							</view>
						</view>
					</view>
					<!-- #ifdef H5 -->
					<view v-if="index == newFriendsList.length - 1" style="height: 44px; width: 300px;"></view>
					<!-- #endif -->
				</view>
			
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
				
				friendRequestList: [
					{
						face: "../../static/faces/face-003.png",
						nickname: "楓カレン",
						verifyMsg: "你好，我是 楓カレン",
						requestType: 1,		// 请求类型： 1-我添加别人  2-别人添加我
						requestStatus: 1,	// 请求状态： 1-待审核(显示查看按钮) 2-已添加 3-已过期
						isTouched: false,
					},
					{
						face: "../../static/faces/face-004.png",
						nickname: "三上悠亚",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 2,
						isTouched: false,
					},
					
					{
						face: "../../static/faces/face-005.png",
						nickname: "楓カレン",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-009.png",
						nickname: "桜空もも",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-010.png",
						nickname: "楓カレン",
						verifyMsg: "你好~~",
						requestType: 2,
						requestStatus: 2,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-014.png",
						nickname: "桜空もも",
						verifyMsg: "你好~~",
						requestType: 2,
						requestStatus: 2,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-008.png",
						nickname: "河北彩花",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-002.png",
						nickname: "三上悠亚",
						verifyMsg: "你好~~",
						requestType: 2,
						requestStatus: 2,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-007.png",
						nickname: "伊藤舞雪",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-013.png",
						nickname: "桜空もも",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-006.png",
						nickname: "伊藤舞雪",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-012.png",
						nickname: "河北彩花",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-001.png",
						nickname: "三上悠亚",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
					{
						face: "../../static/faces/face-011.png",
						nickname: "河北彩花",
						verifyMsg: "你好~~",
						requestType: 1,
						requestStatus: 3,
						isTouched: false,
					},
				],
				
				page: 1,	// 当前第几页
				total: 0,	// 总页数
				newFriendsList: [],
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = app.getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			
			this.getChatListHeight();
		},
		onLoad(e) {
			
		},
		onShow() {
			this.initNewFriends(1);
		},
		onReachBottom() {
			this.loadMore();
		},
		methods:{
			
			loadMore() {
				var page = this.page + 1;
				this.page = page;
				
				var total = this.total;
				if (page > total) {
					return;
				}
				this.initNewFriends(page);
			},
			
			initNewFriends(page) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				this.page = page;
				var total = this.total;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendRequest/queryNew?userId=" + userId + "&page=" + page + "&pageSize=15",
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							console.log(222);
							var newResult = result.data.data;
							console.log(newResult);
							var list = newResult.rows
							me.total = newResult.total;
							
							if (page == 1) {
								me.newFriendsList = [];
							}
							me.newFriendsList = me.newFriendsList.concat(list);
						} else {
							console.log(333);
							uni.showToast({
								title: result.msg,
								icon: "none",
								duration: 3000
							});
						}
					}
				})
			},
			
			gotoWatchFriend(friendRequestId) {
				// 请求类型： 1-我添加别人  2-别人添加我
				// 请求状态： 1-待审核(显示查看按钮) 2-已添加 3-已过期
				
				// 已添加的，点击后查看好友页面
				// 已过期的，点击后可以重新发起验证 (同样适用于我搜索朋友并进行添加操作)
				// 我添加别人，待审核的，查看当前页面，啥操作都不能做
				// 别人添加我，待审核的，查看后可以点击按钮通过验证
				
				// console.log("requestType = " + requestType);
				// console.log("requestStatus = " + requestStatus);
				
				var newFriendsList = this.newFriendsList;
				var newFriend = null;
				for (var i = 0 ; i < newFriendsList.length ; i++) {
					var tempFriend = newFriendsList[i];
					if (tempFriend.friendRequestId == friendRequestId) {
						newFriend = tempFriend;
						break;
					}
				}
				
				console.log(newFriend);
				
				var requestStatus = newFriend.verifyStatus;
				if (requestStatus == 1) {
					uni.navigateTo({
						url: "/pages/contacts/friendPage?friendId=" + newFriend.myFriendId
					})
				} else if (requestStatus == 2) {
					// 已过期就不跳转了
					// uni.navigateTo({
					// 	url: "/pages/contacts/verifyFriend"
					// })
				} else if (requestStatus == 0) {
					
					uni.navigateTo({
						url: "/pages/contacts/verifyFriend?newFriendRequest=" + JSON.stringify(newFriend)
					})
				}
				
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
	@import url("newFriend.css");
</style>
