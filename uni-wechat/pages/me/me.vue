<!-- HR 我的页面 -->
<template>
	<view>
		<view class="myInfo" @click="goToMyPage()">
			
			<view class="real-me">
				<image :src="currentUserInfo.face" class="my-face"></image>
				
				<view class="my-name-wrapper">
					<view class="header-left-part">
						<view class="my-name-box">
							<text class="my-name">{{currentUserInfo.nickname}}</text>
						</view>
						<view class="edit-wrapper" style="justify-content: flex-start;">
							<view class="edit-my-resume-wrapper" >
								<text class="edit-my-resume">微信号：{{currentUserInfo.wechatNum}}</text>
							</view>
						</view>
					</view>
					
					<view class="header-right-part">
						<image src="../../static/weilai/icon-qrcode.png" class="icon-qrcode"></image>
						<image src="../../static/icons/right-arrow.png" class="right-arrow"></image>
					</view>
				</view>
			</view>
			
		</view>
		
		<view class="block-one">
			<view class="block-box">
				<view 
					
					:class="{'item-line-notouch':!lineFriendsTouched, 'item-line-touched': lineFriendsTouched, 'block-line': true}"
					@touchstart="lineFriendsTouched=true"
					@touchend="lineFriendsTouched=false"
					@click="gotoMyFriendCircle()">
					<view class="left-part">
						<image src="../../static/icons/wechat/me/photo3.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">朋友圈</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
				
				<view 
					:class="{'item-line-notouch':!lineVideosTouched, 'item-line-touched': lineVideosTouched, 'block-line': true}"
					@touchstart="lineVideosTouched=true"
					@touchend="lineVideosTouched=false"
					@click="gotoVideoNum()">
					<view class="left-part">
						<image src="../../static/icons/wechat/me/play2.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">视频号</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
				
				<view 
					v-if="env == 'weilai'" 
					:class="{'item-line-notouch':!lineFacesTouched, 'item-line-touched': lineFacesTouched, 'block-line': true}"
					@touchstart="lineFacesTouched=true"
					@touchend="lineFacesTouched=false"
					style=""
					@click="gotoFaceMng()">
					<view class="left-part">
						<image src="../../static/icons/wechat/me/face.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">表情</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view class="block-one">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineSettingsTouched, 'item-line-touched': lineSettingsTouched, 'block-line': true}"
					@touchstart="lineSettingsTouched=true"
					@touchend="lineSettingsTouched=false"
					@click="goToSettings">
					<view class="left-part">
						<image src="../../static/icons/wechat/me/settings.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">设置</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
				
				<view
					:class="{'item-line-notouch':!lineChatBGTouched, 'item-line-touched': lineChatBGTouched, 'block-line': true}"
					@touchstart="lineChatBGTouched=true"
					@touchend="lineChatBGTouched=false"
					@click="goToChatBG">
					<view class="left-part">
						<image src="../../static/icons/wechat/chatBG.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">聊天背景</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
				
				<view
					:class="{'item-line-notouch':!lineMoreCoursesTouched, 'item-line-touched': lineMoreCoursesTouched, 'block-line': true}"
					@touchstart="lineMoreCoursesTouched=true"
					@touchend="lineMoreCoursesTouched=false"
					@click="goToMyCourses">
					<view class="left-part">
						<image src="../../static/icons/wechat/me/more-courses.png" style="width: 22px;height: 22px;align-self: center;"></image>
					</view>
					
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">更多课程</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		
		<view class="say-something-block">
			<view class="say-something-box" style="margin-top: 4px;">
				<text class="more-text">本App为项目演示的体验版</text>
			</view>
			
			<view class="say-something-box" style="margin-top: 4px;">
				<text class="more-text">源码相关内容所有权归风间影月</text>
			</view>
			
			<view class="say-something-box" style="margin-top: 3px;">
				<text class="more-text">www.weilai.com</text>
			</view>
			
			<view class="say-something-box" style="margin-top: 3px;">
				<text class="more-text">®️ 风间影月</text>
			</view>
		</view>
		
		<view class="smill-box">
			<image src="../../static/icons/smill.png" class="smill" style=""></image>
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
				lineFacesTouched: false,
				lineSettingsTouched: false,
				lineChatBGTouched: false,
				lineMoreCoursesTouched: false,
				
				background:{backgroundColor:'#fd5365'},
				height:0,
				navbarRight:0,
				opacity: 0,
				scrollY: 0,
				
				reFresh:"",
				
				currentUserInfo: null, 
				companyInfo: null,
				
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			console.log(currentUserInfo);
		},
		onLoad(e) {
			let res =uni.getSystemInfoSync()
			this.height = res.windowHeight;
			this.navbarRight = res.windowWidth;
		},
		onPageScroll : function(e) {
		},
		methods:{
			goScan() {
				uni.scanCode({
					success: (e) => {
						var qrToken = e.result;
						console.log("qrToken = " + qrToken);
						
						var serverUrl = app.globalData.serverUrl;
						var userId = app.getUserInfoSession().id;
						// 扫码登录
						uni.request({
							method: "POST",
							url: serverUrl + "/saas/scanCode?qrToken=" + qrToken,
							header: {
								"appUserId": userId,
								"appUserToken": app.getUserSessionToken()
							},
							success(result) {
								var status = result.data.status;
								if (status == 200) {
									var preToken = result.data.data;
									console.log("preToken=" + preToken);
									
									uni.showModal({
										title: "您将登录企业SAAS后台管理，确认登录吗？",
										cancelText: "不了，我再想想",
										confirmText: "确定，我要登录",
										confirmColor: "#31B9B3",
										success(e) {
											if (e.confirm) {
												// 点击确认，请求后端登录
												uni.request({
													method: "POST",
													url: serverUrl + "/saas/goQRLogin?userId=" + userId + "&qrToken=" + qrToken + "&preToken=" + preToken,
													success(result) {
														// do nothing
													}
												});
												
											}
										}
									})
									
								} else if (status != 200) {
									uni.showToast({
										title: result.data.msg,
										icon: "none",
										duration: 3000
									});
								}
							},
							fail(e) {
								console.log(e);
							}
						});
						
						
						
					}
				})
			},
			
			gotoMyFriendCircle() {
				uni.navigateTo({
					url: "../discovery/allFriendCircle",
					animationType: "slide-in-bottom",
				});
			},
			
			doUpgrade() {
				uni.showModal({
				    title: '提示',
				    content: '请登录企业后台进行VIP升级',
				    success: function (res) {
				        // if (res.confirm) {
				        //     console.log('用户点击确定');
				        // } else if (res.cancel) {
				        //     console.log('用户点击取消');
				        // }
				    }
				});
			},
			scroll: function(e) {
				var that = this,
					scrollY = e.detail.scrollTop;
				var opacity = scrollY / 200;
				opacity = opacity > 1 ? 1 : opacity;
				that.$set(that, 'opacity', opacity);
				that.$set(that, 'scrollY', scrollY);
				if (that.lock) {
					that.$set(that, 'lock', false);
					return;
				}
			},
			
			gotoVideoNum() {
				uni.showToast({
					icon: "none",
					title: "本功能暂未开放"
				})
			},
			
			gotoFaceMng() {
				uni.showToast({
					icon: "none",
					title: "本功能暂未开放"
				})
			},
			
			gotoMyCollectResume() {
				uni.navigateTo({
					url: "./statistics/collectResume",
					animationType: "slide-in-bottom",
				});
			},
			gotoMyReadResume() {
				uni.navigateTo({
					url: "./statistics/readResume",
					animationType: "slide-in-bottom",
				});
			},
			gotoMyPositionList() {
				uni.switchTab({
					url: "job"
				});
			},
			gotoMyInterview() {
				uni.navigateTo({
					url: "./interview/pendingList",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
			goToMyPage() {
				uni.navigateTo({
					url: "../me/myPage",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToHRInfo() {
				uni.navigateTo({
					url: "./hrInfo?type=" + this.userType.hr,
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToSettings() {
				uni.navigateTo({
					url: "../others/settings",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToChatBG() {
				uni.navigateTo({
					url: "../others/chatBG",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToAboutUs() {
				uni.navigateTo({
					url: "../others/aboutUs",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToPrivacy() {
				uni.navigateTo({
					url: "../others/privacyRule",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToMyCourses() {
				uni.navigateTo({
					url: "../others/moreCourse",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			changeToCandidate() {
				uni.switchTab({
					url: "../welcome/changePerson"
				});
				
				// uni.navigateTo({
				// 	url: "../welcome/changePerson",
				// 	animationType: "slide-in-bottom",
				// 	success() {
				// 	}
				// });
				
				
				// var candUserType = this.userType.candidate;
				// uni.setStorageSync('userType', candUserType);
				// app.changeToCandidate();
			},
			
		}
	}
</script>

<style>
	@import url("me.css");
</style>
