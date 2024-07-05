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
				
				<view class="middle-title">通讯录</view>
				
				<image src="../../static/icons/wechat/add-friend2.png" class="add-icon" @click="gotoAddFriend()"></image>
			</view>

			<view class="letter-words-box">
				<view class="letter-words-list">
					<image src="../../static/icons/wechat/icon-search.png" class="letter-icon" v-if="env == 'weilai'"></image>
					
					<!-- 星标用户如果不存在，则不显示 -->
					<!-- <image src="../../static/icons/wechat/icon-star.png" class="letter-icon"></image> -->
					
					<!-- 循环展示已存在用户的首字母开头循环，不存在的不现实 -->
					<!-- <text v-for="(letter,index) in letters" :key="letter" class="single-letter">{{letter}}</text> -->
					
					<!-- 特殊字符用户如果不存在，则不显示 -->
					<!-- <image src="../../static/icons/wechat/icon-jing.png" class="letter-icon"></image> -->
					
					
					<view @click="goToSomeWhere(contact.letter)" v-for="(contact, index) in contactList" :key="contact.letter + index" class="single-letter">
						<image src="../../static/icons/wechat/icon-star.png" v-if="contact.letter == 'star' && contact.friendList.length > 0" class="letter-icon"></image>
						
						<text v-if="contact.letter != 'star' && contact.letter != 'well_character' && contact.friendList.length > 0" class="single-letter" >{{contact.letter}}</text>
						
						<image src="../../static/icons/wechat/icon-jing.png" v-if="contact.letter == 'well_character' && contact.friendList.length > 0" class="letter-icon"></image>
					</view>
					
				</view>
			</view>
			 
			<!-- 'height': chatListHeight+'px', -->
			<!-- :style="{'marginTop': chatListPaddingTop + 'px'}" -->
			<view class="msg-list-box">
				
				<!-- 占位 -->
				<!-- , 'backgroundColor': '#ecedec' -->
				<view v-if="contactList.length > 0" class="block-me" :style="{'height': chatListPaddingTop + 'px'}">
					<image v-if="contactList.length > 0 && showMe" class="show-me" src="../../static/weilai/miku-block.png"></image>
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
					:class="{'msg-item-notouch':!newFriendTouched, 'msg-item-touched': newFriendTouched, 'single-msg-item-wrapper': true}"
					@touchstart="newFriendTouched=true"
					@touchend="newFriendTouched=false"
					@click="gotoNewFriend()">
					
					<view class="single-msg-item" style="padding: 0px 0;">
						<view class="chat-cover-wrapper icon-bg-orange">
							<image class="bg-icon" src="../../static/icons/wechat/new-friend.png"></image>
						</view>
						
						<!-- :class="{'bottom': index != contact.friendList.length - 1}" -->
						<view class="chat-msg-wrapper bottom" style="padding: 10px 14px 10px 0;">
							<view class="user-name">新的朋友</view>
						</view>
					</view>
				</view>
				
				<view
					:class="{'msg-item-notouch':!groupChatTouched, 'msg-item-touched': groupChatTouched, 'single-msg-item-wrapper': true}"
					@touchstart="groupChatTouched=true"
					@touchend="groupChatTouched=false"
					@click="gotoGroupList()">
					
					<view class="single-msg-item" style="padding: 0px 0;">
						<view class="chat-cover-wrapper icon-bg-green">
							<image class="bg-icon" src="../../static/icons/wechat/group-chat.png"></image>
						</view>
						
						<view class="chat-msg-wrapper bottom" style="padding: 10px 14px 10px 0;">
							<view class="user-name">群聊</view>
						</view>
					</view>
				</view>
				
				<view
					:class="{'msg-item-notouch':!blackFriendTouched, 'msg-item-touched': blackFriendTouched, 'single-msg-item-wrapper': true}"
					@touchstart="blackFriendTouched=true"
					@touchend="blackFriendTouched=false"
					@click="gotoBlackList()">
					
					<view class="single-msg-item bottom" style="padding: 0px 0;">
						<view class="chat-cover-wrapper icon-bg-black">
							<image class="bg-icon" src="../../static/icons/wechat/black-friend.png"></image>
						</view>
						
						<view class="chat-msg-wrapper" style="padding: 10px 14px 10px 0;">
							<view class="user-name">黑名单</view>
						</view>
					</view>
				</view>
				
				<view :id="contact.letter" v-for="(contact, index) in contactList" :key="contact.letter">
					<view class="line-letter-box" v-if="contact.friendList.length > 0">
						<!-- 星标用户如果不存在，则不显示 -->
						<image v-if="contact.letter == 'star'" src="../../static/icons/wechat/icon-star.png" class="letter-icon"></image>
						<text v-if="contact.letter == 'star'" class="letter-word-lable">星标朋友</text>
						
						<text v-if="contact.letter != 'star' && contact.letter != 'well_character'" class="letter-word-lable">{{contact.letter}}</text>
						
						<image v-if="contact.letter == 'well_character'" src="../../static/icons/wechat/icon-jing.png" class="letter-icon"></image>
					</view>
					
					<view
						v-for="(friend, index) in contact.friendList" :key="friend.myFriendFace"
						:class="{'msg-item-notouch':!friend.iTouched, 'msg-item-touched': friend.isTouched, 'single-msg-item-wrapper': true}"
						@touchstart="friend.isTouched=true"
						@touchend="friend.isTouched=false"
						@click="gotoMyFriendPage(friend.myFriendId)">
						
						<!-- class="single-msg-item" -->
						<!-- , 'bottom': index == starFriendList.length - 1 -->
						<view :class="{'single-msg-item': true, 'bottom': index == contact.friendList.length - 1}">
							<!-- 对话图，用户头像或群聊组合图 -->
							<!-- <view class="chat-cover-wrapper"> -->
								<image class="chat-cover" :src="friend.myFriendFace"></image>
							<!-- </view> -->
							
							<view class="chat-msg-wrapper" :class="{'bottom': index != contact.friendList.length - 1}">
								<view class="user-name">{{friend.friendName}}</view>
							</view>
						</view>
					</view>
					
					<!-- #ifdef H5 -->
					<view v-if="index == contactList.length - 1" style="height: 50px; width: 300px;"></view>
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
				
				letters: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"],
				
				// 26个字母外加 # 号
				enWords: [
					'A', 'B', 'C', 'D', 'E', 'F', 'G', 
					'H', 'I', 'J', 'K', 'L', 'M', 'N',
					'O', 'P', 'Q', 'R', 'S', 'T', 
					'U', 'V', 'W', 'X', 'Y', 'Z',
					'#'
				],
				contactList: [
					{ letter: "star", friendList: [] },
					{ letter: "A", friendList: [] },
					{ letter: "B", friendList: [] },
					{ letter: "C", friendList: [] },
					{ letter: "D", friendList: [] },
					{ letter: "E", friendList: [] },
					{ letter: "F", friendList: [] },
					{ letter: "G", friendList: [] },
					{ letter: "H", friendList: [] },
					{ letter: "I", friendList: [] },
					{ letter: "J", friendList: [] },
					{ letter: "K", friendList: [] },
					{ letter: "L", friendList: [] },
					{ letter: "M", friendList: [] },
					{ letter: "N", friendList: [] },
					{ letter: "O", friendList: [] },
					{ letter: "P", friendList: [] },
					{ letter: "Q", friendList: [] },
					{ letter: "R", friendList: [] },
					{ letter: "S", friendList: [] },
					{ letter: "T", friendList: [] },
					{ letter: "U", friendList: [] },
					{ letter: "V", friendList: [] },
					{ letter: "W", friendList: [] },
					{ letter: "X", friendList: [] },
					{ letter: "Y", friendList: [] },
					{ letter: "Z", friendList: [] },
					{ letter: "well_character", friendList: [] },
				],
				
				clearContactList: [
					{ letter: "star", friendList: [] },
					{ letter: "A", friendList: [] },
					{ letter: "B", friendList: [] },
					{ letter: "C", friendList: [] },
					{ letter: "D", friendList: [] },
					{ letter: "E", friendList: [] },
					{ letter: "F", friendList: [] },
					{ letter: "G", friendList: [] },
					{ letter: "H", friendList: [] },
					{ letter: "I", friendList: [] },
					{ letter: "J", friendList: [] },
					{ letter: "K", friendList: [] },
					{ letter: "L", friendList: [] },
					{ letter: "M", friendList: [] },
					{ letter: "N", friendList: [] },
					{ letter: "O", friendList: [] },
					{ letter: "P", friendList: [] },
					{ letter: "Q", friendList: [] },
					{ letter: "R", friendList: [] },
					{ letter: "S", friendList: [] },
					{ letter: "T", friendList: [] },
					{ letter: "U", friendList: [] },
					{ letter: "V", friendList: [] },
					{ letter: "W", friendList: [] },
					{ letter: "X", friendList: [] },
					{ letter: "Y", friendList: [] },
					{ letter: "Z", friendList: [] },
					{ letter: "well_character", friendList: [] },
				],
				
				contactList2: [
					{
						letter: "star",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-003.png",
								nickname: "楓カレン",
								isTouched: false,
							},
							{
								id: 1002,
								face: "../../static/faces/face-004.png",
								nickname: "三上悠亚",
								isTouched: false,
							},
						],
					},
					{
						letter: "A",
						friendList: [
							{
								id: 1003,
								face: "../../static/faces/face-005.png",
								nickname: "楓カレン",
								isTouched: false,
							},
							{
								id: 1001,
								face: "../../static/faces/face-009.png",
								nickname: "桜空もも",
								isTouched: false,
							},
							{
								id: 1001,
								face: "../../static/faces/face-010.png",
								nickname: "楓カレン",
								isTouched: false,
							},
						],
					},
					{
						letter: "B",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-014.png",
								nickname: "桜空もも",
								isTouched: false,
							},
						],
					},
					{
						letter: "C",
						friendList: [],
					},
					{
						letter: "D",
						friendList: [],
					},
					{
						letter: "E",
						friendList: [],
					},
					{
						letter: "F",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-008.png",
								nickname: "河北彩花",
								isTouched: false,
							},
						],
					},
					{
						letter: "G",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-002.png",
								nickname: "三上悠亚",
								isTouched: false,
							},
							{
								id: 1001,
								face: "../../static/faces/face-007.png",
								nickname: "伊藤舞雪",
								isTouched: false,
							},
							{
								id: 1001,
								face: "../../static/faces/face-013.png",
								nickname: "桜空もも",
								isTouched: false,
							},
						],
					},
					{
						letter: "H",
						friendList: [],
					},
					{
						letter: "I",
						friendList: [],
					},
					{
						letter: "J",
						friendList: [],
					},
					{
						letter: "K",
						friendList: [],
					},
					{
						letter: "L",
						friendList: [],
					},
					{
						letter: "M",
						friendList: [],
					},
					{
						letter: "N",
						friendList: [],
					},
					{
						letter: "O",
						friendList: [],
					},
					{
						letter: "P",
						friendList: [],
					},
					{
						letter: "Q",
						friendList: [],
					},
					{
						letter: "R",
						friendList: [],
					},
					{
						letter: "S",
						friendList: [],
					},
					{
						letter: "T",
						friendList: [],
					},
					{
						letter: "U",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-006.png",
								nickname: "伊藤舞雪",
								isTouched: false,
							},
						],
					},
					{
						letter: "V",
						friendList: [],
					},
					{
						letter: "W",
						friendList: [],
					},
					{
						letter: "X",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-012.png",
								nickname: "河北彩花",
								isTouched: false,
							},
							{
								id: 1001,
								face: "../../static/faces/face-001.png",
								nickname: "三上悠亚",
								isTouched: false,
							},
						],
					},
					{
						letter: "Y",
						friendList: [],
					},
					{
						letter: "Z",
						friendList: [],
					},
					{
						letter: "well_character",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-011.png",
								nickname: "河北彩花",
								isTouched: false,
							},
						],
					},
				]
				
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = app.getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			
			this.getChatListHeight();
			
			this.initContacts();
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
			
			clearContacts() {
				var me = this;
				var contactList = me.contactList;
				for (var i = 0 ; i < contactList.length ; i ++) { 
					var contact = contactList[i];
					contactList[i].friendList = [];
				}
			},
			
			initContacts() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				this.clearContacts();
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendship/queryMyFriends",
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var allFriends = result.data.data;
							console.log(allFriends);
							
							var contactList = me.contactList;
							for (var i = 0 ; i < allFriends.length ; i ++) {
								var friend = allFriends[i];
								var myFriendRemark = friend.myFriendRemark;
								var myFriendNickname = friend.myFriendNickname;
								var friendName = myFriendNickname;
								// if (myFriendRemark == null || myFriendRemark == undefined || myFriendRemark == "") {
								// 	friendName = myFriendNickname;
								// }
								friend.friendName = friendName;
								
								// 转换拼音
								var pingyin = me.NameUtil.convertPinyin(friendName);
								// 截取拼音的首字母
								var firstChar = pingyin.substr(0, 1).toUpperCase();
								// console.log("firstChar = " + firstChar);
								// 获取首字母在二维数组中的位置
								var order = me.getOrderOfEnWords(firstChar);
								// console.log("order = " + order);
								// 判断首字母匹配，如果匹配到英文，则放入，如果不匹配，则放入#
								var isInner = false;
								for (var j = 0 ; j < contactList.length ; j ++) {
									var contact = contactList[j];
									var letter = contact.letter;
									if (letter == firstChar) {
										contactList[j].friendList.push(friend);
										isInner = true;
									}
								}
								if (!isInner) {
									contactList[contactList.length-1].friendList.push(friend);
								}
							}
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
			
			// 获取英文字母所在数组的位置
			getOrderOfEnWords(enChar) {
				var enWords = this.enWords;
				for (var i = 0 ; i < enWords.length ; i ++) {
					if (enWords[i] == enChar) {
						return i;
					}
				}
				// 如果没有对应的值，说明用户昵称的首字母是乱七八糟的字符或者数字或者表情
				return enWords.length - 1;
			},
			
			gotoMyFriendPage(friendId) {
				uni.navigateTo({
					url: "/pages/contacts/friendPage?friendId=" + friendId
				})
			},
			
			gotoAddFriend() {
				uni.navigateTo({
					url: "/pages/contacts/addFriend"
				})
			},
			
			gotoGroupList() {
				this.showPlusBox = false;
				uni.navigateTo({
					url: "/pages/contacts/groupList"
				})
			},
			
			gotoBlackList() {
				this.showPlusBox = false;
				uni.navigateTo({
					url: "/pages/contacts/blackList"
				})
			},
			
			gotoNewFriend() {
				uni.navigateTo({
					url: "/pages/contacts/newFriend"
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
	@import url("contacts.css");
</style>
