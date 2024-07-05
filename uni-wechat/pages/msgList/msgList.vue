<template>
	<view>
		<view class="myInfo">
			<view class="status_bar">
				<!-- 这里是状态栏 -->
			</view>
			
			<!-- class="navigation-bar" -->
			<!-- :class="{'fix-navigation-bar': fixNavigationBar, 'navigation-bar': true}" -->
			<view class="navigation-bar">
				<view class="space-none"></view>
				
				<view class="middle-title">微信</view>
				
				<image src="../../static/icons/wechat/add.png" class="add-icon" @click="displayPlusBox"></image>
			</view>
			
			<!-- :style="{'top': statusBarHeight}" -->
			<view class="plus-box" v-if="showPlusBox" :style="{'top': chatListPaddingTop + 'px'}">
				<image class="up-arrow-icon" src="../../static/icons/wechat/up-arrow.png"></image>
				<view class="items-wrapper">
					<view class="single-item" @click="createGroup()">
						<image class="item-icon" src="../../static/icons/wechat/do_group_chat.png"></image>
						<view class="item-text">
							发起群聊
						</view>
					</view>
					<view class="line" style="height: 1rpx; background-color: #5a5a5a;width: 68%;align-self: flex-end;"></view>
					<view class="single-item" @click="gotoAddFriend()">
						<image class="item-icon" src="../../static/icons/wechat/add_friend.png"></image>
						<view class="item-text">
							添加朋友
						</view>
					</view>
					<view class="line" style="height: 1rpx; background-color: #5a5a5a;width: 68%;align-self: flex-end;"></view>
					<view class="single-item" @click="scanQrCode()">
						<image class="item-icon" src="../../static/icons/wechat/scan.png"></image>
						<view class="item-text">
							扫一扫
						</view>
					</view>
				</view>
			</view>
			
			<!-- 'height': chatListHeight+'px', -->
			<!-- :style="{'marginTop': chatListPaddingTop + 'px'}" -->
			<view class="msg-list-box" @click="showPlusBox = false" >
				
				<!-- 占位 -->
				<!-- , 'backgroundColor': '#ecedec' -->
				<view v-if="chatMsgList.length > 0" class="block-me" :style="{'height': chatListPaddingTop + 'px'}">
					<image v-if="chatMsgList.length > 0 && showMe" class="show-me" src="../../static/weilai/miku-block.png"></image>
				</view>
				
				<view class="search-box" v-if="env == 'weilai'">
					<view class="search-wrapper">
						<view class="search-middle">
							<image class="icon-search" src="../../static/weilai/icon-search.png"></image>
							<text class="search-words">搜索</text>
						</view>
					</view>
				</view>
				
				<view
					v-if="env == 'weilai'"
					:class="{'item-line-notouch':!chatGLMTouched, 'item-line-touched': chatGLMTouched, 'single-msg-item': true}"
					@touchstart="chatGLMTouched=true, showPlusBox = false"
					@touchend="chatGLMTouched=false, showPlusBox = false"
					style="padding-left: 14px;">
					<view class="chat-cover-wrapper">
						<image class="chat-cover" src="../../static/weilai/ChatGLM.png"></image>
					</view>
					
					<view class="chat-msg-wrapper">
						<view class="chat-box">
							<view class="chat-title">ChatGLM</view>
							<view class="chat-time">昨天</view>
						</view>
						
						<view class="time-box">
							<view class="chat-content too-much-words">
								<text class="">这是AI生成的内容</text>
							</view>
						</view>
					</view>
				</view>
				
				<view 
					v-for="(msg, index) in lastestUserChatList" :key="msg.face"
					:class="{'msg-item-notouch':!msg.msgItemTouched, 'msg-item-touched': msg.msgItemTouched, 'single-msg-item-wrapper': true}"
					@touchstart="msg.msgItemTouched=true, showPlusBox = false"
					@touchend="msg.msgItemTouched=false, showPlusBox = false">
					
					<view class="single-msg-item" @click="gotoChatPage(msg.communicationType, msg.sourceUserId)">
						<!-- 对话图，用户头像或群聊组合图 -->
						<view class="chat-cover-wrapper">
							<uni-badge size="small" :text="msg.unReadCounts" absolute="rightTop" type="error" class="msg-badge" :custom-style="uniBadgeStyle">
								<image class="chat-cover" :src="msg.face" v-if="msg.communicationType == 1"></image>
								
								<view class="group-cover" v-if="msg.communicationType == 2">
									<image class="member-face" :src="msg.face"></image>
									<image class="member-face" :src="msg.face"></image>
									<image class="member-face" :src="msg.face"></image>
									<image class="member-face" :src="msg.face"></image>
									<image class="member-face" :src="msg.face"></image>
									<image class="member-face" :src="msg.face"></image>
									<image class="member-face" :src="msg.face"></image>
								</view>
							</uni-badge>
						</view>
						
						<view class="chat-msg-wrapper">
							<!-- 聊天基本内容的预览显示 -->
							<view class="chat-box">
								<!-- 群组名或对方用户昵称 -->
								<view class="chat-title">{{msg.name}}</view>
								<!-- 显示的最近一次的聊天日期 -->
								<!-- <view class="chat-time">{{ msg.chatTime}}</view> -->
								<view class="chat-time">{{DateUtil.formatWeekDatetime(new Date(Date.parse(msg.chatTime)))}}</view>
							</view>
							
							<view class="time-box">
								<!-- 聊天内容的一些预览信息 -->
								<view class="chat-content too-much-words">
									<text v-if="msg.isCallMe == 1" class="call-me">[有人@我]</text>

									<!-- msgType: 1-文字，2-语音，3-视频，4-图片，5-动画表情，6-个人名片，7-语音通话，8-视频通话 -->
									<text class="">{{msg.msgContent}}</text>
									<!-- <text v-if="msg.msgType == 1" class="">{{msg.msgContent}}</text>
									<text v-if="msg.msgType == 2" class="">[语音] 3″</text>
									<text v-if="msg.msgType == 3" class="">[视频]</text>
									<text v-if="msg.msgType == 4" class="">[图片]</text>
									<text v-if="msg.msgType == 5" class="">[动画表情]</text>
									<text v-if="msg.msgType == 6" class="">[个人名片]</text>
									<text v-if="msg.msgType == 7" class="">[语音通话]</text>
									<text v-if="msg.msgType == 8" class="">[视频通话]</text> -->
								</view>
								<!-- 是否忽略 -->
								<view class="is-ignore">
									<image v-if="msg.isIgnore == 1" class="icon-ignore-sound" src="../../static/icons/wechat/ignore-sound.png"></image>
								</view>
							</view>
						</view>
					</view>
					
					<!-- #ifdef H5 -->
					<view v-if="index == chatMsgList.length - 1" style="height: 44px; width: 300px;"></view>
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
				
				showPlusBox: false,
				chatListHeight: 0,
				chatListPaddingTop: 0,
				
				chatGLMTouched: false,
				msgItemTouched: false,
				
				chatMsgList1: [],
				
				// msgType: 1-文字，2-语音，3-视频，4-图片，5-动画表情，6-个人名片，7-语音通话，8-视频通话
				lastestUserChatList: [], 
				chatMsgList: [
					{
						face: "../../static/faces/face-003.png",
						nickname: "楓カレン",
						msgContent: "中饭吃的不错！吃得很饱！中饭吃的不错！吃得很饱！中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 1,
						isIgnore: 1,
						isCallMe: 1,
						unReadCounts: 9,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-003.png",
						nickname: "Java技术交流群",
						msgContent: "中饭吃的不错！吃得很饱！中饭吃的不错！吃得很饱！中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 1,
						isIgnore: 1,
						isCallMe: 1,
						unReadCounts: 9,
						msgItemTouched: false,
						communicationType: 2, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-004.png",
						nickname: "三上悠亚",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 2,
						isIgnore: 1,
						isCallMe: 1,
						unReadCounts: 100,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-005.png",
						nickname: "楓カレン",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 3,
						isIgnore: 0,
						isCallMe: 0,
						unReadCounts: 0,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-009.png",
						nickname: "桜空もも",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 4,
						isIgnore: 0,
						isCallMe: 1,
						unReadCounts: 0,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-010.png",
						nickname: "楓カレン",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 5,
						isIgnore: 1,
						isCallMe: 0,
						unReadCounts: 0,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-014.png",
						nickname: "桜空もも",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 2,
						isIgnore: 0,
						isCallMe: 0,
						unReadCounts: 10,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-002.png",
						nickname: "三上悠亚",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 6,
						isIgnore: 1,
						isCallMe: 0,
						unReadCounts: 33,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-007.png",
						nickname: "伊藤舞雪",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 7,
						isIgnore: 1,
						isCallMe: 0,
						unReadCounts: 0,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-008.png",
						nickname: "河北彩花",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 8,
						isIgnore: 0,
						isCallMe: 0,
						unReadCounts: 0,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-013.png",
						nickname: "桜空もも",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 6,
						isIgnore: 1,
						isCallMe: 0,
						unReadCounts: 6,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-006.png",
						nickname: "伊藤舞雪",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 1,
						isIgnore: 1,
						isCallMe: 1,
						unReadCounts: 8,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-012.png",
						nickname: "河北彩花",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 2,
						isIgnore: 1,
						isCallMe: 0,
						unReadCounts: 0,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-001.png",
						nickname: "三上悠亚",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 3,
						isIgnore: 1,
						isCallMe: 0,
						unReadCounts: 0,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
					{
						face: "../../static/faces/face-011.png",
						nickname: "河北彩花",
						msgContent: "中饭吃的不错！吃得很饱！",
						chatTime: "09:12",
						msgType: 4,
						isIgnore: 1,
						isCallMe: 1,
						unReadCounts: 65,
						msgItemTouched: false,
						communicationType: 1, 	// 1:单聊，2:群聊
					},
				]
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = app.getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			
			this.getChatListHeight();
			
			// 把最新的聊天记录列表渲染在此
			this.doMeAFavor();
			
			
			// 群主在拉人后的操作
			var pendingUserList = uni.getStorageSync("pendingUserList");
			var chooseContactType = uni.getStorageSync("chooseContactType");
			
			// chooseContactType: 1：添加用户到群，2：从群里剔除
			if (chooseContactType == 1) {
				uni.navigateTo({
					url: "/pages/msgList/groupChat",
					animationType: "pop-in"
				})
			} else if (chooseContactType == 2) {
				
			}
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
			var me = this;
			
			uni.$on("receiveMsgInMsgVue", function(data) {
				console.log('监听receiveMsgInMsgVue：' + data);
				me.dealReceiveLastestMsg(JSON.parse(data));
			});
			
			uni.$on("reRenderReceiveMsgInMsgVue", function(data) {
				console.log('监听reRenderReceiveMsgInMsgVue：' + data);
				me.doMeAFavor();
			});
			
		},
		onUnload() {
			uni.$off("receiveMsgInMsgVue");
			uni.$off("reRenderReceiveMsgInMsgVue");
		},
		methods:{
			
			doMeAFavor() {
				console.log('监听doMeAFavor!!!');
				var lastestUserChatList = uni.getStorageSync("lastestUserChatList");
				if (lastestUserChatList != null && lastestUserChatList != undefined && lastestUserChatList != "") {
					this.lastestUserChatList = lastestUserChatList;
					console.log(lastestUserChatList);
					// 获得我的未读数量map
					this.getMyUnReadCounts(lastestUserChatList);
				}
			},
			
			getMyUnReadCounts(lastestUserChatList) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/chat/getMyUnReadCounts?myId=" + userId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var unReadCountsMap = result.data.data;
							console.log(unReadCountsMap);
							
							// 底部的未读消息总数
							var tabTotalUnReadCounts = 0;
							
							for (var i = 0 ; i < lastestUserChatList.length ; i ++) {
								var tempChat = lastestUserChatList[i];
								var tempChatSourceUserId = tempChat.sourceUserId;
								var unReadCounts = unReadCountsMap[tempChatSourceUserId];
								if (unReadCounts == null || unReadCounts == "" || unReadCounts == undefined) {
									unReadCounts = 0;
								}
								unReadCounts = parseInt(unReadCounts);
								if (unReadCounts < 0) {
									unReadCounts = 0;
								}
								tempChat.unReadCounts = unReadCounts;
								lastestUserChatList.splice(i, 1, tempChat);
								
								tabTotalUnReadCounts += unReadCounts;
							}
							this.lastestUserChatList = lastestUserChatList;
							
							if (tabTotalUnReadCounts > 0) {
								app.setTabBarRedNumber(0, tabTotalUnReadCounts);
							} else {
								app.removeTabBarBadge(0);
							}
							
						}
					}
				})
			
			},
			
			// 处理收到的消息
			dealReceiveLastestMsg(msgJSON) {
				console.log(msgJSON);
				var chatMsg = msgJSON.chatMsg;
				var chatTime = msgJSON.chatTime;
				var currentSourceUserId = chatMsg.senderId;
				
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/userinfo/get?userId=" + currentSourceUserId,
					success(result) {
						// console.log(result);
						if (result.data.status == 200) {
							var currentSourceUserInfo = result.data.data;
							me.currentSourceUserInfo = currentSourceUserInfo;
							
							me.saveLastestMsgToLocal(currentSourceUserId, currentSourceUserInfo, chatMsg.msg, chatTime);
						}
					}
				})
			},
			
			// 记录每个人的最后一条消息，记录在本地，用于显示聊天列表
			saveLastestMsgToLocal(sourceUserId, sourceUser, msgContent, chatTime) {
				
				var lastMsg = {
					sourceUserId: sourceUserId,		// 源头用户，聊天对象，这里是 求职者
					name: sourceUser.nickname,
					face: sourceUser.face,
					msgContent: msgContent,
					chatTime: chatTime,
					unReadCounts: 0
				}
				// console.log(lastMsg);
				// return;
				// 先获得本地存储与候选人聊天的list，如果没有则新创建
				var lastestUserChatList = uni.getStorageSync("lastestUserChatList");
				if (lastestUserChatList == null || lastestUserChatList == undefined || lastestUserChatList == "") {
					lastestUserChatList = [];
				}
				
				// 循环判断，如果存在，则剔除，放入最新的
				var dealMsg = false;
				for ( var i = 0 ; i < lastestUserChatList.length ; i ++) {
					var tmp = lastestUserChatList[i];
					if (tmp.sourceUserId == lastMsg.sourceUserId) {
						lastestUserChatList.splice(i, 1, lastMsg);
						dealMsg = true;
						break;
					}
				}
				if (!dealMsg) {
					lastestUserChatList.unshift(lastMsg);
				}
				uni.setStorageSync("lastestUserChatList", lastestUserChatList);
				this.lastestUserChatList = lastestUserChatList;
				console.log(lastestUserChatList);
				// uni.removeStorageSync("lastestUserChatList");
				
			},
			
			// chatId 是动态传过来的，可以是朋友的id也可以是群组id
			gotoChatPage(type, chatId) {
				
				if (type == 1) {
					// 前往单聊页面
					uni.navigateTo({
						url: "/pages/msgList/singleChat?friendId=" + chatId,
					})
				} else if (type == 2) {
					// 前往群聊页面
					uni.navigateTo({
						url: "/pages/msgList/groupChat?groupId=" + chatId,
					})
				}
				
			},
			
			// 前往群聊页面
			gotoGroupChat() {
				
			},
			
			createGroup() {
				this.showPlusBox = false;
				uni.navigateTo({
					url: "../msgList/chooseUserToGroup?choosedUserIds=",
					animationType: "slide-in-bottom",
				});
			},
			
			gotoAddFriend() {
				this.showPlusBox = false;
				uni.navigateTo({
					url: "/pages/contacts/addFriend"
				})
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
				console.log("this.chatListPaddingTop = " + this.chatListPaddingTop);
			},
			
			displayPlusBox() {
				this.showPlusBox = !this.showPlusBox;
			},
			

			
			scanQrCode() {
				var me = this;
				this.showPlusBox = false;
				
				// #ifdef H5
				uni.showToast({
					title: "H5不支持扫码",
					icon: "none"
				})
				// #endif
				
				// #ifdef APP
				uni.scanCode({
					success: (e) => {
						console.log(e);
						var qrCode = e.result;
						console.log("qrCode = " + qrCode);
						var wechatNumber = JSON.parse(qrCode).wechatNumber;
						console.log(wechatNumber);
						// console.log(typeof(wechatNumber));
						me.goSearchFriend(wechatNumber);
					}
				})
				// #endif
			},
			
			goSearchFriend(queryString) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/userinfo/queryFriend?queryString=" + queryString,
					success(result) {
						console.log(111);
						if (result.data.status == 200) {
							console.log(222);
							var userInfo = result.data.data;
							console.log(userInfo);
							
							if (userInfo == null) {
								uni.showToast({
									title: "查无此人",
									icon: "none",
									duration: 3000
								});
							} else {
								uni.navigateTo({
									url: "/pages/contacts/searchedFriend?friend=" + JSON.stringify(userInfo)
								})
							}
						} else {
							console.log(333);
							uni.showToast({
								title: result.data.msg,
								icon: "none",
								duration: 3000
							});
						}
						
					}
				})
				
			},
		}
	}
</script>

<style>
	@import url("msgList.css");
</style>
