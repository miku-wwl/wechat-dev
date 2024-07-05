<template>
	<view>
		<view class="myInfo">
			<view class="status_bar">
				<!-- 这里是状态栏 -->
			</view>
			
			<!-- class="navigation-bar" -->
			<!-- :class="{'fix-navigation-bar': fixNavigationBar, 'navigation-bar': true}" -->
			<view class="navigation-bar" :style="{'height': navigationHeight + 'px'}">
				<view class="navigation-bar-up" style="height: 44px;">
					
					<view class="space-icon">
						<image src="../../static/icons/go-back.png" class="back-icon" @click="goBack()"></image>
					</view>
					
					<view class="middle-title">选择联系人</view>
					
					<view class="space-none">
						<button 
							v-if="checkedList.length > 0"
							size="mini" 
							type="default" 
							:disabled="doneBtn" 
							@click="initGroupChat()" 
							style="align-self: center;color:#ffffff;backgroundColor:#1AAD19;borderColor:#1AAD19;padding: 0 10px;"
							>完成{{checkedList.length > 0 ? '(' + checkedList.length + ')' : ""}}</button>
					</view>
				</view>
				
				<!-- <view class="navigation-bar-down" v-if="checkedList.length > 0"> -->
					
					<view class="" style="">
						
					<scroll-view class="navigation-bar-down scroll-view_H" :scroll-x="true" v-if="checkedList.length > 0">
						<block v-for="(contact, index) in contactList" :key="contact.letter+1001">
							<block v-for="(friend, index) in contact.friendList" :key="friend.face+1002">
								<image v-if="friend.checked && !judgeUserAlreadyInGroup(friend.id)" class="checked-cover" :src="friend.face"></image>
							</block>
						</block>
					</scroll-view>
					
					</view>
				<!-- </view> -->
			</view>

			<view class="letter-words-box">
				<view class="letter-words-list">
					<image src="../../static/icons/wechat/icon-search.png" class="letter-icon" v-if="env == 'weilai'"></image>
					
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
				
				<checkbox-group @change="checkboxChange">
					<view :id="contact.letter" v-for="(contact, index) in contactList" :key="contact.letter">
						<view class="line-letter-box" v-if="contact.friendList.length > 0">
							<!-- 星标用户如果不存在，则不显示 -->
							<image v-if="contact.letter == 'star'" src="../../static/icons/wechat/icon-star.png" class="letter-icon"></image>
							<text v-if="contact.letter == 'star'" class="letter-word-lable">星标朋友</text>
							
							<text v-if="contact.letter != 'star' && contact.letter != 'well_character'" class="letter-word-lable">{{contact.letter}}</text>
							
							<image v-if="contact.letter == 'well_character'" src="../../static/icons/wechat/icon-jing.png" class="letter-icon"></image>
						</view>
					
					
						<view
							v-for="(friend, index) in contact.friendList" :key="friend.face"
							:class="{'msg-item-notouch':!friend.iTouched, 'msg-item-touched': friend.isTouched, 'single-msg-item-wrapper': true}"
							@touchstart="friend.isTouched=true"
							@touchend="friend.isTouched=false">
							
							<!-- class="single-msg-item" -->
							<!-- , 'bottom': index == starFriendList.length - 1 -->
							<view :class="{'single-msg-item': true, 'bottom': index == contact.friendList.length - 1}">
								
								<!-- <view class="chat-cover-wrapper"> -->
								
								
									<checkbox 
										color="#03c25f" 
										style="transform:scale(0.9); align-self: center; margin-right: 6px;" 
										:disabled="judgeUserAlreadyInGroup(friend.id)"
										:value="friend.id" :checked="judgeUserAlreadyInGroup(friend.id) || friend.checked" />
									<image class="chat-cover" :src="friend.face"></image>
								<!-- </view> -->
								
								<view class="chat-msg-wrapper" :class="{'bottom': index != contact.friendList.length - 1}">
									<view class="user-name">{{friend.nickname}}</view>
								</view>
							</view>
						</view>
					
						<view v-if="index == contactList.length - 1" style="height: 50px; width: 300px;"></view>
					</view>
				</checkbox-group>
				
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
				navigationHeight: 44,
				
				doneBtn: true,
				
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
				
				choosedUserIdList: [],
				
				checkedList: [],
				contactList: [
					{
						letter: "star",
						friendList: [
							{
								id: 1001,
								face: "../../static/faces/face-003.png",
								nickname: "楓カレン",
								isTouched: false,
								checked: false,
								disabled: false,	// 用于提供给checklist进行判断是否要禁用
							},
							{
								id: 1002,
								face: "../../static/faces/face-004.png",
								nickname: "三上悠亚",
								isTouched: false,
								checked: false,
								disabled: false,
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
								checked: false,
								disabled: false,
							},
							{
								id: 1004,
								face: "../../static/faces/face-009.png",
								nickname: "桜空もも",
								isTouched: false,
								checked: false,
								disabled: false,
							},
							{
								id: 1005,
								face: "../../static/faces/face-010.png",
								nickname: "楓カレン",
								isTouched: false,
								checked: false,
								disabled: false,
							},
						],
					},
					{
						letter: "B",
						friendList: [
							{
								id: 1006,
								face: "../../static/faces/face-014.png",
								nickname: "桜空もも",
								isTouched: false,
								checked: false,
								disabled: false,
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
								id: 1007,
								face: "../../static/faces/face-008.png",
								nickname: "河北彩花",
								isTouched: false,
								checked: false,
								disabled: false,
							},
						],
					},
					{
						letter: "G",
						friendList: [
							{
								id: 1008,
								face: "../../static/faces/face-002.png",
								nickname: "三上悠亚",
								isTouched: false,
								checked: false,
								disabled: false,
							},
							{
								id: 1009,
								face: "../../static/faces/face-007.png",
								nickname: "伊藤舞雪",
								isTouched: false,
								checked: false,
								disabled: false,
							},
							{
								id: 1010,
								face: "../../static/faces/face-013.png",
								nickname: "桜空もも",
								isTouched: false,
								checked: false,
								disabled: false,
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
								id: 1011,
								face: "../../static/faces/face-006.png",
								nickname: "伊藤舞雪",
								isTouched: false,
								checked: false,
								disabled: false,
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
								id: 1012,
								face: "../../static/faces/face-012.png",
								nickname: "河北彩花",
								isTouched: false,
								checked: false,
								disabled: false,
							},
							{
								id: 1013,
								face: "../../static/faces/face-001.png",
								nickname: "三上悠亚",
								isTouched: false,
								checked: false,
								disabled: false,
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
								id: 1014,
								face: "../../static/faces/face-011.png",
								nickname: "河北彩花",
								isTouched: false,
								checked: false,
								disabled: false,
							},
						],
					},
				],
				
				// 标记当前的用户列表是用于选择剔除的还是添加的标记
				isRemove: 0,
				
			}
		},
		watch: {
			checkedList(newValue, oldValue) {
				
				if (newValue && newValue.length > 0) {
					this.doneBtn = false;
				} else {
					this.doneBtn = true;
				}
			},
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
		onLoad(params) {
			var choosedUserIds = params.choosedUserIds;
			var isRemove = params.isRemove;
			if (isRemove != null && isRemove != undefined && isRemove != '') {
				this.isRemove = isRemove;
			}
			
			
			// 从上一个页面传过来的，用于判断选中已选中的用户，打钩
			var choosedUserIdArray = choosedUserIds.split(",");
			// console.log(choosedUserIdArray);
			// 去除空格
			var choosedUserIdList = [];
			
			for (var i = 0 ; i < choosedUserIdArray.length ; i ++) {
				var userId = choosedUserIdArray[i];
				if (userId != null && userId != '' && userId != undefined) {
					choosedUserIdList.push(userId);
				}
			}
			// console.log(choosedUserIdList);
			this.choosedUserIdList = choosedUserIdList;
		},
		methods:{
			// 用于判断当前用户id是否选中，要checked
			judgeUserAlreadyInGroup(userId) {
				var choosedUserIdList = this.choosedUserIdList;
				for (var i = 0 ; i < choosedUserIdList.length ; i ++) {
					var tempId = choosedUserIdList[i];
					if (userId == tempId) {
						return true;
					}
				}
				return false;
			},
			
			initGroupChat() {
				var me = this;
				
				var pendingUserList = [];
				
				var contactList = this.contactList;
				for (var i = 0; i < contactList.length; i++) {
					const innerList = contactList[i].friendList;
					if (innerList == null || innerList == undefined || innerList.length <= 0) {
						continue;
					}
					for (var j = 0; j < innerList.length; j++) {
						// 判断选中并且不存在 judgeUserAlreadyInGroup
						var friend = innerList[j];
						if (friend == null || friend == undefined) {
							continue;
						}
						var friendId = friend.id;
						if (!me.judgeUserAlreadyInGroup(friendId) && friend.checked) {
							pendingUserList.push(friend);
							// pendingJoinGroupUserIdList.push(friendId);
						}
					}
				}
				// 这是真正要发到后端的将要加入到群里的用户，发到后端给他们发送一个确认加入群的通知
				console.log(pendingUserList);
				
				var isRemove = this.isRemove;
				console.log("isRemove = " + isRemove);
				// 携带参数，用于给群判断是否要进行初始化，
				if (isRemove == 0) {
					// uni.redirectTo({
					// 	url: "/pages/msgList/groupChat?pendingJoinGroupUserIds=" + JSON.stringify(pendingJoinGroupUserIdList),
					// 	animationType: "pop-in"
					// })
					
					uni.setStorageSync("pendingUserList", pendingUserList);
					// chooseContactType: 1：添加用户到群，2：从群里剔除
					uni.setStorageSync("chooseContactType", 1);
					uni.navigateBack({
						delta: 2,
					});
				} else if (isRemove == 1) {
					// uni.redirectTo({
					// 	url: "/pages/msgList/groupChat?pendingJoinGroupUserIds=" + JSON.stringify(pendingJoinGroupUserIdList) + "&isRemove=" + isRemove,
					// 	animationType: "pop-in"
					// })
					
					uni.setStorageSync("pendingUserList", pendingUserList);
					// chooseContactType: 1：添加用户到群，2：从群里剔除
					uni.setStorageSync("chooseContactType", 2);
					uni.navigateBack({
						delta: 2,
					});
				}
				
				
				
				
			},
			
			checkboxChange(e) {
				var me = this;
				var contactList = this.contactList;
				var checkedList = e.detail.value;
				this.checkedList = checkedList;
				
				var choosedUserIdList = this.choosedUserIdList; // 要排除的列表
					
				// console.log(checkedList);
					
				// console.log(this.contactList);
				for (var i = 0; i < contactList.length; i++) {
					const innerList = contactList[i].friendList;
					for (var j = 0; j < innerList.length; j++) {
						
						const item = innerList[j];
						if(checkedList.includes(item.id)){
							this.$set(item,'checked',true)
						}else{
							this.$set(item,'checked',false)
						}
					}
				}
				// console.log(this.contactList);
				
				if (checkedList.length > 0) {
					me.navigationHeight = 102;
					me.getChatListHeight(58);
				} else {
					me.navigationHeight = 44;
					me.getChatListHeight();
				}
				
				// 排除从上个页面传过来的已在群的用户id
				// var choosedUserIdList = this.choosedUserIdList; // 要排除的列表
				
			},
						
			goBack() {
				uni.navigateBack({
					delta: 1
				})
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
			
			getChatListHeight(moreHeight) {
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
				
				if (moreHeight != null && moreHeight != undefined && moreHeight != '') {
					this.chatListPaddingTop = statusBarHeight + navigationBarHeight + moreHeight;
				}
			},

		}
	}
</script>

<style>
	@import url("chooseUserToGroup.css");
</style>
