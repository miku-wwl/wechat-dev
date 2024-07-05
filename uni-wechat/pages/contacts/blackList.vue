<template>
	<view>
		<view class="myInfo">
			
			<view class="msg-list-box">
				
				<!-- {
				    "friendshipId": "1754345233958637569",
				    "mySelfId": "1749256670632050690",
				    "myFriendId": "1753640850245595137",
				    "myFriendFace": "http://127.0.0.1:9000/weilai/face/1749619640390205441/e9f2be46-56ba-454c-a7ee-3e290bad6a59.jpg",
				    "myFriendNickname": "用户13******234",
				    "myFriendRemark": "Test",
				    "chatBg": null,
				    "isMsgIgnore": 0,
				    "isBlack": 1
				} -->
				<view
					v-for="(msg, index) in blackList" :key="msg.myFriendFace"
					:class="{'msg-item-notouch':!msg.isTouched, 'msg-item-touched': msg.isTouched, 'single-msg-item-wrapper': true}"
					@touchstart="msg.isTouched=true"
					@touchend="msg.isTouched=false"
					@click="gotoWatchFriend(msg.myFriendId)">
					
					<view class="single-msg-item">
						<!-- 对话图，用户头像或群聊组合图 -->
						<image class="chat-cover" :src="msg.myFriendFace"></image>
						
						<view class="chat-msg-wrapper">
							<view class="chat-box">
								<view class="chat-title">{{msg.myFriendNickname}}({{(msg.myFriendRemark != "" && msg.myFriendRemark != null) ? msg.myFriendRemark : ""}})</view>
							</view>
							
							<view class="status-box">
							</view>
						</view>
					</view>
					<!-- #ifdef H5 -->
					<view v-if="index == blackList.length - 1" style="height: 44px; width: 300px;"></view>
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
				
				blackList:[],
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
				]
				
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = app.getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			
			this.getChatListHeight();
			this.initBlackList();
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
			initBlackList() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendship/queryMyBlackList",
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var blackList = result.data.data;
							console.log(blackList);
							
							me.blackList = blackList;
							
						} else {
							uni.showToast({
								title: result.msg,
								icon: "none",
								duration: 3000
							});
						}
					}
				})
			},
			
			gotoWatchFriend(friendId) {
				uni.navigateTo({
					url: "/pages/contacts/friendPage?friendId=" + friendId
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
	@import url("blackList.css");
</style>
