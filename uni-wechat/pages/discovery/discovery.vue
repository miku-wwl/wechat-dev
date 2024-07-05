<template>
	<view>
		<view class="block-one">
			<view class="block-box">
				<view 
					:class="{'item-line-notouch':!lineFriendsTouched, 'item-line-touched': lineFriendsTouched, 'block-line': true}"
					@touchstart="lineFriendsTouched=true"
					@touchend="lineFriendsTouched=false"
					@click="gotoFriendCircle()">
					<view class="left-part">
						<image src="../../static/icons/wechat/discovery/friend-circle.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">朋友圈</text>
						
						<view class="sub-part-right">
							<uni-badge size="small" absolute="rightTop" type="error" text="9" :is-dot="true" class="friend-badge">
								<!-- <image class="friend-face" src="../../static/faces/face-005.png"></image> -->
								<image class="friend-face" :src="currentUserInfo.face"></image>
							</uni-badge>
							<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one">
			<view class="block-box">
				<view 
					:class="{'item-line-notouch':!lineVideosTouched, 'item-line-touched': lineVideosTouched, 'block-line': true}"
					@touchstart="lineVideosTouched=true"
					@touchend="lineVideosTouched=false"
					@click="gotoVideoNum()">
					<view class="left-part">
						<image src="../../static/icons/wechat/discovery/shipinhao2.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">视频号</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
				
				<view 
					:class="{'item-line-notouch':!lineShakeTouched, 'item-line-touched': lineShakeTouched, 'block-line': true}"
					@touchstart="lineShakeTouched=true"
					@touchend="lineShakeTouched=false"
					@click="gotoBottles()">
					<view class="left-part">
						<image src="../../static/icons/bottles-1.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">漂流瓶</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineScanTouched, 'item-line-touched': lineScanTouched, 'block-line': true}"
					@touchstart="lineScanTouched=true"
					@touchend="lineScanTouched=false"
					@click="scanQrCode()">
					<view class="left-part">
						<image src="../../static/icons/wechat/discovery/saoyisao.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">扫一扫</text>
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
		data() {
			return {
				env: app.getAppEnv(),
				
				lineFriendsTouched: false,
				lineVideosTouched: false,
				lineShakeTouched: false,
				lineScanTouched: false,
				
				currentUserInfo: {},
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
		},
		onLoad(e) {
			let res =uni.getSystemInfoSync()
			this.height = res.windowHeight;
			this.navbarRight = res.windowWidth;
		},
		onPageScroll : function(e) {
		    
		},
		methods:{
			gotoFriendCircle() {
				uni.navigateTo({
					url: "/pages/discovery/allFriendCircle"
				});
			},
			
			gotoBottles() {
				uni.navigateTo({
					url: "/pages/discovery/driftBottle"
				});
			},
			
			gotoVideoNum() {
				uni.showToast({
					icon: "none",
					title: "本功能暂未开放"
				})
			},
			
			scanQrCode() {
				var me = this;
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
						// console.log(wechatNumber);
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
	@import url("discovery.css");
</style>
