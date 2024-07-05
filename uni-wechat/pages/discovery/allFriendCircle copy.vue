<template>
	<view>
		<view class="myInfo" @click="">
			
			<view class="status_bar" style="opacity: 0;">
				<!-- 这里是状态栏 -->
			</view>
			
			<!-- 解决video原生组件不能被覆盖，使用page中的titleNView来解决 -->
			<!-- <view class="navigation-bar navigation-bar-scroll" style="z-index: 99999;" :style="{opacity: opacity }">
				<image src="../../static/icons/wechat/friend-circle-goback-black.png" class="back-icon" @click="goBack()"></image>
				
				<view class="middle-title">朋友圈</view>
				
				<image src="../../static/icons/wechat/friend-circle-camera-black.png" v-if="env == 'weilai'" class="camera-icon"></image>
			</view>
			
			<view class="navigation-bar" >
				<image src="../../static/icons/wechat/friend-circle-goback-white.png" class="back-icon" @click="goBack()"></image>
				
				<view class="middle-title"></view>
				
				<image src="../../static/icons/wechat/friend-circle-camera-white.png" v-if="env == 'weilai'" class="camera-icon"></image>
			</view> -->
			
			<image :src="currentUserInfo.friendCircleBg" @click="changeCover()" class="bg-image" :style="{'width': windowWidth + 'px', 'height': windowWidth + 'px'}"></image>
			<!-- <image src="../../static/images/default.png" @click="changeCover()" class="bg-image" :style="{'width': windowWidth + 'px', 'height': windowWidth + 'px'}"></image> -->
			
			<view class="person-box">
				<view class="one-line">
					<view class="left-nickname">
						{{currentUserInfo.nickname}}
					</view>
					<image class="right-face" :src="currentUserInfo.face"></image>
				</view>
				<view class="two-line">
					{{currentUserInfo.signature}}
				</view>
			</view>
			
			
			<!-- 朋友圈列表内容 -->
			<view class="whole-box" touchend="loseComment()">
				
				<view class="one-content">
					<image src="../../static/faces/face-003.png" class="left-face"></image>
					
					<view class="right-content">
						
						<view class="nickname">楓カレン</view>
						
						<text class="say-words">测试竖屏视频~~~</text>
						
						<view class="image-list">
							<!-- 需要判断长宽比例，并且要按比例自动缩放 -->
							<video 
								src="../../static/videos/video1.mp4" 
								autoplay loop muted :controls="false" object-fit="cover" 
								style="width: 140px; height: 220px;"
								>
								<cover-view class="every-video-cover-box" @click="showVideo()"></cover-view>
							</video>
						</view>
						
						<view class="more-operator">
							<view class="left-wrapper">
								<view class="publish-time">15分钟前</view>
								<image src="../../static/icons/trash-can.png" class="icon-trash" @click="deleteContent()"></image>
							</view>
							
							<view class="more-right-wrapper">
								<view v-if="blackBox" class="black-operater-box">
									<view class="black-left-oper">
										<image src="../../static/icons/wechat/zan-unselected.png" class="icon-zan"></image>
										<text class="oper-words">赞</text>
										<!-- <text>取消</text> -->
									</view>
									<view class="black-middle-line">|</view>
									<view class="black-right-oper" @click="focus()">
										<image src="../../static/icons/wechat/comments.png" class="icon-comments"></image>
										<text class="oper-words">评论</text>
									</view>
								</view>
								
								<view class="two-points-box" @click="doOperator()">
									<image src="../../static/icons/wechat/icon-two-points.png" class="right-points"></image>
								</view>
							</view>
						</view>
					
						<view class="like-friend-list-box">
							<view class="like-list">
								<image src="../../static/icons/wechat/icon-like.png" class="icon-like"></image>
								<text class="like-friend-wrods">风间影月,</text>
								<text class="like-friend-wrods">三上悠亚,</text>
								<text class="like-friend-wrods">风间影月,</text>
								<text class="like-friend-wrods">三上悠亚,</text>
								<text class="like-friend-wrods">风间影月,</text>
								<text class="like-friend-wrods">三上悠亚,</text>
								<text class="like-friend-wrods">风间影月,</text>
								<text class="like-friend-wrods">三上悠亚,</text>
								<text class="like-friend-wrods">楓カレン</text>
							</view>
							
							<view class="line-wrapper" style="margin: 4px 0;"><view class="line" style="width: 100%;background-color: #e4e4e4;"></view></view>
							
							<view class="comments-list">
								<view class="one-line-content">
									<view>
										<text class="friend-name">风间影月:</text>
									</view>
									<text class="comment-content" @click="doCommentOperate()">今天天气不错呀~~</text>
								</view>
								<view class="one-line-content">
									<view>
										<text class="friend-name">三上悠亚</text>
										<text class="replay-words">回复</text>
										<text class="friend-name">风间影月:</text>
										<text class="comment-content" @click="doCommentOperate()">哈哈哈是的呀哈哈哈是的呀哈哈哈是的呀哈哈哈是的呀~~</text>
									</view>
								</view>
								<view class="one-line-content">
									<view>
										<text class="friend-name">风间影月</text>
										<text class="replay-words">回复</text>
										<text class="friend-name">三上悠亚:</text>
										<text class="comment-content" @click="doCommentOperate()">出来玩！</text>
									</view>
								</view>
								<view class="one-line-content">
									<view>
										<text class="friend-name">三上悠亚</text>
										<text class="replay-words">回复</text>
										<text class="friend-name">风间影月:</text>
										<text class="comment-content" @click="doCommentOperate()">哈哈哈是的呀哈哈哈是的呀哈哈哈是的呀哈哈哈是的呀~~</text>
									</view>
								</view>
								<view class="one-line-content">
									<view>
										<text class="friend-name">风间影月</text>
										<text class="replay-words">回复</text>
										<text class="friend-name">三上悠亚:</text>
										<text class="comment-content" @click="doCommentOperate()">出来玩！</text>
									</view>
								</view>
								
								<view class="load-more-comments">加载更多</view>
								
							</view>
							
						</view>
					
					</view>
				</view>
				
				<view class="line-wrapper" style="margin: 22px 0;"><view class="line" style="width: 100%;"></view></view>
				
				<view class="one-content">
					<image src="../../static/faces/face-002.png" class="left-face"></image>
					
					<view class="right-content">
						
						<view class="nickname">三上悠亚</view>
						
						<text class="say-words">今天吃的好撑，东坡肉太好吃了，努力工作ing。哈哈哈哈哈哈哈哈哈~~~</text>
						
						<!-- 如果是纯文字的，那么图片就不要显示了 -->
						<view class="image-list"></view>
						
						<view class="more-operator">
							<view class="publish-time">15分钟前</view>
							
							<view class="more-right-wrapper">
								<view v-if="blackBox" class="black-operater-box">
									<view class="black-left-oper">
										<image src="../../static/icons/wechat/zan-selected.png" class="icon-zan"></image>
										<text class="oper-words">取消</text>
									</view>
									<view class="black-middle-line">|</view>
									<view class="black-right-oper">
										<image src="../../static/icons/wechat/comments.png" class="icon-comments"></image>
										<text class="oper-words">评论</text>
									</view>
								</view>
								
								<view class="two-points-box" @click="doOperator()">
									<image src="../../static/icons/wechat/icon-two-points.png" class="right-points"></image>
								</view>
							</view>
						</view>
						
						<view class="like-friend-list-box">
							<view class="like-list">
								<image src="../../static/icons/wechat/icon-like.png" class="icon-like"></image>
								<text class="like-friend-wrods">风间影月,</text>
								<text class="like-friend-wrods">三上悠亚,</text>
								<text class="like-friend-wrods">风间影月,</text>
								<text class="like-friend-wrods">三上悠亚,</text>
								<text class="like-friend-wrods">三上悠亚,</text>
								<text class="like-friend-wrods">楓カレン</text>
							</view>
						</view>
						
					</view>
				</view>
				
				<view class="line-wrapper" style="margin: 22px 0;"><view class="line" style="width: 100%;"></view></view>
				
				<view class="one-content">
					<image src="../../static/faces/face-008.png" class="left-face"></image>
					
					<view class="right-content">
						
						<view class="nickname">伊藤舞雪</view>
						
						<text class="say-words">测试一张图，如果是单图，保证长边能显示出来~~~</text>
						
						<view class="image-list">
							<!-- 如果是单图，保证长边能显示出来 -->
							<!-- 需要判断长宽比例，并且要按比例自动缩放 -->
							<image src="../../static/images/bg/bg1001.png" mode="widthFix" class="one-image-width"></image>
						</view>
						
						<view class="publish-time">15分钟前</view>
					</view>
				</view>
				
				<view class="line-wrapper" style="margin: 22px 0;"><view class="line" style="width: 100%;"></view></view>
				
				<view class="one-content">
					<image src="../../static/faces/face-005.png" class="left-face"></image>
					
					<view class="right-content">
						
						<view class="nickname">楓カレン</view>
						
						<text class="say-words">测试一张图，如果是单图，保证长边能显示出来~~~测试一张图，如果是单图，保证长边能显示出来~~~</text>
						
						<view class="image-list">
							<!-- 如果是单图，保证长边能显示出来 -->
							<!-- 需要判断长宽比例，并且要按比例自动缩放 -->
							<image src="../../static/images/bg/bg1011.png" mode="widthFix" class="one-image-height" @click="previewImage('../../static/images/bg/bg1011.png')"></image>
						</view>
						
						<view class="publish-time">1小时前</view>
					</view>
				</view>
				
				<view class="line-wrapper" style="margin: 22px 0;"><view class="line" style="width: 100%;"></view></view>
				
				<view class="one-content">
					<image src="../../static/faces/face-002.png" class="left-face"></image>
					
					<view class="right-content">
						
						<view class="nickname">三上悠亚</view>
						
						<text class="say-words">多图测试显示~~~</text>
						
						<view class="image-list">
							<!-- 如果是多图，则正方形显示 -->
							<image src="../../static/images/bg/bg1011.png" mode="aspectFill" class="more-image"></image>
							<image src="../../static/images/bg/bg1012.png" mode="aspectFill" class="more-image"></image>
							<image src="../../static/images/bg/bg1010.png" mode="aspectFill" class="more-image"></image>
							<image src="../../static/images/bg/bg1009.png" mode="aspectFill" class="more-image"></image>
							<image src="../../static/images/bg/bg1008.png" mode="aspectFill" class="more-image"></image>
							<image src="../../static/images/bg/bg1007.png" mode="aspectFill" class="more-image"></image>
							<image src="../../static/images/bg/bg1006.png" mode="aspectFill" class="more-image"></image>
							<image src="../../static/images/bg/bg1005.png" mode="aspectFill" class="more-image"></image>
						</view>
						
						<view class="publish-time">1小时前</view>
					</view>
				</view>
				
				<view class="line-wrapper" style="margin: 22px 0;"><view class="line" style="width: 100%;"></view></view>
				
				<view class="one-content">
					<image src="../../static/faces/face-003.png" class="left-face"></image>
					
					<view class="right-content">
						
						<view class="nickname">楓カレン</view>
						
						<text class="say-words">测试横屏视频~~~</text>
						
						<view class="image-list">
							<!-- 需要判断长宽比例，并且要按比例自动缩放 -->
							<video
								src="../../static/videos/video2.mp4" 
								autoplay loop muted :controls="false" object-fit="cover" 
								style="width: 220px; height: 140px;"
								>
								<cover-view class="every-video-cover-box" @click="showVideo()"></cover-view>
							</video>
						</view>
						
						<view class="publish-time">15分钟前</view>
					</view>
				</view>
				
				
				<view class="line-wrapper" style="margin: 22px 0;"><view class="line" style="width: 100%;"></view></view>
				
				
				
				
				
				
			</view>
			
			<view style="margin-bottom: 50px;"></view>
			
			<!-- 消息输入框 -->
			<view class="message-wrapper" v-if="commentContentDisplay" :style="{'padding-bottom': commentPaddingBottom + 'px'}" ref="footer" id="footer">
					<textarea
						id="msgTextarea" 
						ref="msgTextarea" 
						value="" 
						maxlength="512" 
						fixed="true"
						class="message-input"
						placeholder="回复风间影月:" 
						placeholder-class="placeholder-class"
						v-model="commentContent"
						confirm-type="send"
						:focus="commentFocus"
						confirm-hold
						scroll-with-animation="true"
						@confirm="sendComment()"
						@linechange="linechange"
						@focus="focus"
						@blur="blur"/>
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
				
				commentContent: "",
				commentContentDisplay: false,
				
				commentPaddingBottom: 42,
				commentFocus: false,
				
				currentUserInfo: null, 
				
				blackBox: false,
				
				opacity: 0,
				scrollY: 0,
				
				page: 1,	// 当前第几页
				total: 0,	// 总页数
				allFriendCircleList: [],
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			console.log(currentUserInfo);
			
			// #ifdef H5
			this.commentPaddingBottom = 6;
			// #endif
			
			this.initAllFriendCircle(1);
		},
		onLoad(e) {
			let res =uni.getSystemInfoSync()
			this.windowWidth = res.windowWidth;
			this.windowHeight = res.windowHeight;
		},
		onPageScroll : function(e) {
			// var that = this,
			// 	scrollY = e.scrollTop;
			// console.log("scrollY = " + scrollY);
			// var opacity = scrollY / 200;
			// opacity = opacity > 1 ? 1 : opacity;
			// that.$set(that, 'opacity', opacity);
			// that.$set(that, 'scrollY', scrollY);
			// if (that.lock) {
			// 	that.$set(that, 'lock', false);
			// 	return;
			// }
			
			// 页面滚动，评论框消失
			// this.blur();
		},
		onNavigationBarButtonTap() {
			this.createMyContent();
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
				this.initAllFriendCircle(page);
			},
			
			initAllFriendCircle(page) {
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
					url: serverUrl + "/friendCircle/queryList?userId=" + userId + "&page=" + page + "&pageSize=15",
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							console.log(222);
							var newResult = result.data.data;
							console.log(newResult);
							var list = newResult.rows
							me.total = newResult.total;
							
							if (page == 1) {
								me.allFriendCircleList = [];
							}
							me.allFriendCircleList = me.allFriendCircleList.concat(list);
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
			
			createMyContent() {
				uni.navigateTo({
					url: "/pages/discovery/createContent",
					animationType: "slide-in-bottom",
				});
			},
			
			sendComment() {
			},
			
			linechange(e) {
				// console.log(e);
				var me = this;
				var lineCount = e.detail.lineCount;
				if (lineCount <= 5) {
					var msgHeight = me.msgHeight + 22;
					me.msgHeight = msgHeight;
				}
				if (lineCount > 1) {
					me.alignSelf = "flex-end";
				}
				
				uni.createSelectorQuery().in(this).select("#msgTextarea").boundingClientRect(data => {
				  // console.log(JSON.stringify(data))
				  if (lineCount > 5 && data.height < 118) {
					  me.msgHeight = 118;
				  }
				}).exec();
			},
			
			loseComment(e) {
				// 页面滚动，评论框消失
				// this.commentFocus = false;
				console.log(e);
			},
			
			focus() {
				this.commentContentDisplay = true;
				this.commentFocus = true;
				this.commentPaddingBottom = 6;
				this.blackBox = false;
			},
			
			blur() {
				this.commentContentDisplay = false;
				this.commentFocus = false;
				
				// #ifdef H5
				this.commentPaddingBottom = 6;
				// #endif
				// #ifdef APP
				this.commentPaddingBottom = 42;
				// #endif
			},
			
			doCommentOperate() {
				// 这里需要判断是否是我的评论，如果是我的则删除，如果是别人的，则弹出回复框
				
				// uni.showModal({
				// 	title: "删除我的评论？",
				// 	confirmText: "确认",
				// 	cancelText: "取消",
				// 	success() {
						
				// 	}
				// })
				
				this.focus();
			},
			
			deleteContent() {
				uni.showModal({
					title: "删除该朋友圈？",
					confirmText: "确认",
					cancelText: "取消",
					success() {
						
					}
				})
			},
			
			doOperator() {
				// TODO 后续结合数据动态判断，点击展示的时候关闭所有，再显示当前的，如果点击的是当前的已经存在，则关闭当前
				this.blackBox = !this.blackBox;
			},
			
			changeCover() {
				uni.navigateTo({
					url: "/pages/discovery/circleCover",
					animationType: "slide-in-bottom",
				});
			},
			
			previewImage(url) {
				uni.previewImage({
					current: url,
					urls: [url]
				})
			},
			
			showVideo() {
				var src = "../../static/videos/video2.mp4";
				var width = "300";
				var height = "140";
				
				uni.navigateTo({
					url: "/pages/discovery/videoShow?src=" + src + "&width=" + width + "&height=" + height,
					// url: "../hr/msgVideo?src=" + src + "&width=" + width + "&height=" + height,
					animationType: "fade-in",
					success(e) {
						console.log(e)
					},
					fail(e) {
						console.log(e)
					}
				});
			},
			
			goBack() {
				uni.navigateBack({
					delta: 1
				})
			},
			
			previewImage(url) {
				uni.previewImage({
					current: url,
					urls: [url]
				})
			},
		}
	}
</script>

<style>
	@import url("allFriendCircle.css");
</style>
