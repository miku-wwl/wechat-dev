<template>
	<view>
		<view class="myInfo">
			<view class="status_bar">
				<!-- 这里是状态栏 -->
			</view>
			
			<view class="msg-list-box">
				
				<view class="search-box" :style="{'paddingTop': chatListPaddingTop + 'px'}">
					<view class="search-wrapper">
						
						<view class="search-middle">
							<image class="icon-search" src="../../static/weilai/icon-search.png"></image>
						</view>
						
						<input type="text" v-model="queryString" value="queryString" :focus="true" placeholder="账号/手机号" confirm-type="send" @confirm="goSearchFriend()" placeholder-class="search-words" class="search-words-input"/>
						
					</view>
					
					<text class="cancel-search" @click="cancelSearch()">取消</text>
					
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
				
				queryString: "",
				
				showMe: false,
				opacity: 0,
				scrollY: 0,
				
				chatListHeight: 0,
				chatListPaddingTop: 0,
				
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = app.getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			
			this.getChatListHeight();
		},
		onNavigationBarButtonTap(e) {
			console.log(e);
		},
		onLoad(e) {
		},
		methods:{
			
			goSearchFriend() {
				
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var queryString = this.queryString;
				
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
			
			cancelSearch() {
				uni.navigateBack({
					delta: 1
				})
			},
			
			getChatListHeight() {
				// https://uniapp.dcloud.net.cn/tutorial/syntax-css.html#%E5%9B%BA%E5%AE%9A%E5%80%BC
				// 页面高度 =  屏幕高度 -（状态栏高度+导航栏高度+tab栏高度）
				
				var windowHeight = sys.windowHeight;
				
				var statusBarHeight = sys.statusBarHeight;
				// console.log("statusBarHeight = " + statusBarHeight)
				
				var navigationBarHeight = 0;
				var tabBarHeight = 0;
				
				var chatListHeight = windowHeight - (statusBarHeight + navigationBarHeight);
				this.chatListHeight = chatListHeight;
				
				// H5额外减去导航栏的高度
				// // #ifdef H5
				// this.chatListHeight = chatListHeight - 44;
				// // #endif
				
				this.chatListPaddingTop = statusBarHeight + navigationBarHeight + 10;
				// console.log("this.chatListPaddingTop = " + this.chatListPaddingTop);
			},

		}
	}
</script>

<style>
	@import url("addFriend.css");
</style>
