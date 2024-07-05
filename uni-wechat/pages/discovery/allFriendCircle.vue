<template>
	<view>
		<view class="myInfo">
			
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
				
				<block v-for="(content,index) in allFriendCircleList" :key="content.friendCircleId">
					<view class="one-content">
						<image :src="content.userFace" class="left-face"></image>
						
						<view class="right-content">
							
							<view class="nickname">{{content.userNickname}}</view>
							
							<text class="say-words">{{content.words}}</text>
							
							<view class="image-list">
								<image 
									v-for="(img, index) in dealImageList(content.images)" 
									:key="img"
									:src="img" 
									mode="aspectFill" 
									class="more-image" 
									@click="previewImage(img)"
								></image>
							</view>
							
							<view class="more-operator">
								<view class="left-wrapper">
									<!-- <view class="publish-time">{{DateUtil.toShowTimeFormat(formatDateTime(content.publishTime))}}</view> -->
									<view class="publish-time">{{DateUtil.toShowTime(new Date(Date.parse(content.publishTime)))}}</view>
									<image src="../../static/icons/trash-can.png" v-if="content.userId == currentUserInfo.id" class="icon-trash" @click="deleteContent(content.friendCircleId)"></image>
								</view>
								
								<view class="more-right-wrapper">
									<view v-if="blackBoxIndex == content.friendCircleId" class="black-operater-box">
										<view class="black-left-oper" @click="doLike(content.friendCircleId)" v-if="!content.doILike">
											<image src="../../static/icons/wechat/zan-unselected.png" class="icon-zan"></image>
											<text class="oper-words">赞</text>
											<!-- <text>取消</text> -->
										</view>
										<view class="black-left-oper" @click="doUnLike(content.friendCircleId)" v-if="content.doILike">
											<image src="../../static/icons/wechat/zan-selected.png" class="icon-zan"></image>
											<text class="oper-words">取消</text>
										</view>
										
										<view class="black-middle-line">|</view>
										<view class="black-right-oper" @click="leaveComment(content.friendCircleId, content.userId, '')">
											<image src="../../static/icons/wechat/comments.png" class="icon-comments"></image>
											<text class="oper-words">评论</text>
										</view>
									</view>
									
									<view class="two-points-box" @click="doOperator(content.friendCircleId)">
										<image src="../../static/icons/wechat/icon-two-points.png" class="right-points"></image>
									</view>
								</view>
							</view>
						
							<view class="like-friend-list-box" v-if="content.likedFriends.length > 0 || content.commentList.length > 0">
								<view class="like-list">
									<!-- v-if="content.likedFriends != null && content.likedFriends != undefined && content.likedFriends.length > 0" -->
									<image src="../../static/icons/wechat/icon-like.png" class="icon-like"  v-if="content.likedFriends != null && content.likedFriends != undefined && content.likedFriends.length > 0"></image>
									<block v-for="(friend, index) in content.likedFriends"  v-if="content.likedFriends != null && content.likedFriends != undefined && content.likedFriends.length > 0">
										<text class="like-friend-wrods">{{friend.likedUserName}}</text>
										<block v-if="index < content.likedFriends.length-1">,</block>
									</block>
									
									<!-- <text class="like-friend-wrods">三上悠亚,</text>
									<text class="like-friend-wrods">楓カレン</text> -->
								</view>
								
								<view class="line-wrapper" style="margin: 4px 0 0 0;"><view class="line" style="width: 100%;background-color: #e4e4e4;margin: 0 0 4px 0;" v-if="content.likedFriends.length > 0 && content.commentList.length > 0"></view></view>
								
								<view class="comments-list" v-if="content.commentList != null && content.commentList != undefined && content.commentList.length > 0">
									
									<view class="one-line-content" v-for="(comment,index) in content.commentList" :key="comment.commentId" v-if="comment.replyedUserNickname == null || comment.replyedUserNickname == ''">
										<view>
											<text class="friend-name">{{comment.commentUserNickname}}:</text>
										</view>
										<text class="comment-content" @click="doCommentReplay(comment.belongUserId, comment.commentId, comment.commentUserId, comment.friendCircleId, comment.commentUserNickname)">{{comment.commentContent}}</text>
									</view>
									<view class="one-line-content" v-for="(comment,index) in content.commentList" :key="comment.commentId" v-if="comment.replyedUserNickname != null && comment.replyedUserNickname != ''">
										<view>
											<text class="friend-name">{{comment.commentUserNickname}}</text>
											<text class="replay-words">回复</text>
											<text class="friend-name">{{comment.replyedUserNickname}}:</text>
											<text class="comment-content" @click="doCommentReplay(comment.belongUserId, comment.commentId, comment.commentUserId, comment.friendCircleId, comment.commentUserNickname)">{{comment.commentContent}}</text>
										</view>
									</view>
									
									<!-- <view class="one-line-content">
										<view>
											<text class="friend-name">风间影月:</text>
										</view>
										<text class="comment-content" @click="doCommentReplay()">今天天气不错呀~~</text>
									</view>
									<view class="one-line-content">
										<view>
											<text class="friend-name">三上悠亚</text>
											<text class="replay-words">回复</text>
											<text class="friend-name">风间影月:</text>
											<text class="comment-content" @click="doCommentReplay()">哈哈哈是的呀哈哈哈是的呀哈哈哈是的呀哈哈哈是的呀~~</text>
										</view>
									</view>
									<view class="one-line-content">
										<view>
											<text class="friend-name">风间影月</text>
											<text class="replay-words">回复</text>
											<text class="friend-name">三上悠亚:</text>
											<text class="comment-content" @click="doCommentReplay()">出来玩！</text>
										</view>
									</view> -->
									
									<!-- <view class="load-more-comments">加载更多</view> -->
									
								</view>
								
							</view>
						
						</view>
					</view>
					
					<view class="line-wrapper" style="margin: 22px 0;"><view class="line" style="width: 100%;"></view></view>
				</block>
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
						:placeholder="placeholder" 
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
				blackBoxIndex: "",
				
				opacity: 0,
				scrollY: 0,
				
				page: 1,	// 当前第几页
				total: 0,	// 总页数
				allFriendCircleList: [],
				
				friendCircleId: "", 
				belongUserId: "", 
				fatherId: "", 
				
				placeholder: "发表留言: ",
				// placeholder: "回复风间影月: ",
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
			this.blackBoxIndex = "";
		},
		onNavigationBarButtonTap() {
			this.createMyContent();
		},
		onReachBottom() {
			this.loadMore();
		},
		methods:{
			doCommentReplay(belongUserId, commentId, commentUserId, friendCircleId, commentUserNickname) {
				// 这里需要判断是否是我的评论，如果是我的留言则删除，如果是别人的，则弹出回复框
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				// console.log(commentId);
				// console.log(commentUserId);
				// console.log(friendCircleId);
				// console.log(commentUserNickname);
				
				if (commentUserId === userId) {
					uni.showModal({
						title: "删除评论？",
						confirmText: "确认",
						cancelText: "取消",
						success(e) {
							if (e.confirm) {
								me.deleteComment(commentUserId, commentId, friendCircleId);
							}
						}
					})
				} else {
					me.placeholder = "回复" + commentUserNickname + ":";
					me.leaveComment(friendCircleId, belongUserId, commentId);
				}
				
			},
			
			deleteComment(commentUserId, commentId, friendCircleId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/comment/delete?commentUserId=" + commentUserId + "&commentId=" + commentId + "&friendCircleId=" + friendCircleId,
					success(result) {
						if (result.data.status == 200) {
							// var newCommentList = result.data.data;
							// console.log(newCommentList);
							
							me.renderNewComments(friendCircleId);
							
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
			
			leaveComment(friendCircleId, belongUserId, fatherId) {
				this.friendCircleId = friendCircleId;
				this.belongUserId = belongUserId;
				this.fatherId = fatherId;
				
				this.focus();
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
			
			sendComment() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var commentContent = me.commentContent;
				if(commentContent == null || commentContent == "") {
					uni.showToast({
						icon: "none",
						title: "评论不能为空"
					})
					return;
				}
				
				var bo = {
					belongUserId: me.belongUserId,
					friendCircleId: me.friendCircleId,
					fatherId: me.fatherId,
					commentUserId: userId,
					commentContent: me.commentContent
				};
				
				// console.log(bo);
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/comment/create",
					data: bo,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							console.log(222);
							var newResult = result.data.data;
							console.log(newResult);
							
							// 重新查询评论列表，因为有可能有其他朋友的记录，都可以在此显示
							me.renderNewComments(me.friendCircleId);
							
							me.blur();
							me.commentContent = "";
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
			
			renderNewComments(friendCircleId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/comment/query?friendCircleId=" + friendCircleId,
					success(result) {
						if (result.data.status == 200) {
							var newCommentList = result.data.data;
							console.log(newCommentList);
							
							
							// 循环当前的朋友圈列表，把新的评论列表插进去
							var allFriendCircleList = me.allFriendCircleList;
							for (var i  = 0 ; i < allFriendCircleList.length ; i ++) {
								var temp = allFriendCircleList[i];
								if (temp.friendCircleId == friendCircleId) {
									temp.commentList = newCommentList;
									allFriendCircleList.splice(i, 1, temp);
									break;
								}
							}
							me.allFriendCircleList = allFriendCircleList; 
							// console.log(me.allFriendCircleList);
							
							
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
			
			doLike(friendCircleId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendCircle/like?friendCircleId=" + friendCircleId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							console.log(222);
							// var newResult = result.data;
							// console.log(newResult);
							
							// 隐藏黑色操作框
							me.blackBoxIndex = "";
							// 查询点赞列表
							me.getSingleFriendCircleLikedFriends(friendCircleId);
							me.setSingleLikeOrNot(friendCircleId, true);
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
			
			setSingleLikeOrNot(friendCircleId, doILike) {
				var me = this;
				var allFriendCircleList = me.allFriendCircleList;
				for (var i  = 0 ; i < allFriendCircleList.length ; i ++) {
					var temp = allFriendCircleList[i];
					if (temp.friendCircleId == friendCircleId) {
						temp.doILike = doILike;
						allFriendCircleList.splice(i, 1, temp);
					}
				}
				me.allFriendCircleList = allFriendCircleList; 
			},
			
			doUnLike(friendCircleId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendCircle/unlike?friendCircleId=" + friendCircleId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							console.log(222);
							// var newResult = result.data;
							// console.log(newResult);
							
							// 隐藏黑色操作框
							me.blackBoxIndex = "";
							// 查询点赞列表
							me.getSingleFriendCircleLikedFriends(friendCircleId);
							me.setSingleLikeOrNot(friendCircleId, false);
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
			
			
			getSingleFriendCircleLikedFriends(friendCircleId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendCircle/likedFriends?friendCircleId=" + friendCircleId,
					success(result) {
						// console.log(result);
						if (result.data.status == 200) {
							// console.log(222);
							var newResult = result.data;
							// console.log(newResult);
							
							var likedList = newResult.data;
							// 循环当前的朋友圈列表，把用户点赞列表插进去
							var allFriendCircleList = me.allFriendCircleList;
							for (var i  = 0 ; i < allFriendCircleList.length ; i ++) {
								var temp = allFriendCircleList[i];
								if (temp.friendCircleId == friendCircleId) {
									// allFriendCircleList[i].likedFriends = likedList;
									temp.likedFriends = likedList;
									allFriendCircleList.splice(i, 1, temp);
								}
							}
							me.allFriendCircleList = allFriendCircleList; 
							console.log(me.allFriendCircleList);
							
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
			
			formatDateTime(dateTime) {
				if (dateTime == null || dateTime == '' || dateTime == undefined) {
					return "";
				}
				var arr = dateTime.split("T");
				var times = arr[0] + " " + arr[1];
				return times;
			},
			
			dealImageList(imageStr) {
				var imageList = [];
				if (app.isStrEmpty(imageStr)) {
					return imageList;
				}
				
				var imageArr = imageStr.split(",");
				for (var i = 0 ; i < imageArr.length ; i ++) {
					var img = imageArr[i];
					if (!app.isStrEmpty(img)) {
						imageList.push(img);
					}
				}
				// console.log(imageList);
				return imageList;
			},
			
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
					url: serverUrl + "/friendCircle/queryList?userId=" + userId + "&page=" + page + "&pageSize=10",
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
			
			
			
			
			deleteContent(friendCircleId) {
				var me = this;
				
				uni.showModal({
					title: "删除该朋友圈？",
					confirmText: "确认",
					cancelText: "取消",
					success(e) {
						if (e.confirm) {
							me.doDelete(friendCircleId);
						}
					}
				})
			},
			
			doDelete(friendCircleId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendCircle/delete?friendCircleId=" + friendCircleId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							
							var allFriendCircleList = me.allFriendCircleList;
							for (var i = 0 ; i < allFriendCircleList.length ; i ++) {
								var friendCircle = allFriendCircleList[i];
								if (friendCircleId == friendCircle.friendCircleId) {
									allFriendCircleList.splice(i, 1);
									break;
								}
							}
							
							uni.showToast({
								title: "删除成功！",
								icon: "none",
								duration: 2000
							});
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
			
			hideBlackOperator() {
				this.blackBoxIndex = "";
			},
			
			doOperator(id) {
				// 结合数据动态判断，点击展示的时候关闭所有，再显示当前的，如果点击的是当前的已经存在，则关闭当前
				// this.blackBox = !this.blackBox;
				var me = this;
				if (me.blackBoxIndex != id) {
					me.blackBoxIndex = id;
				} else {
					me.blackBoxIndex = "";
				}
				
				// 判断我是否点赞朋友圈
				// me.judgeDoILike(id);
			},
			
			judgeDoILike(friendCircleId) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var serverUrl = app.globalData.serverUrl;
				
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/friendCircle/doILike?friendCircleId=" + friendCircleId,
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							console.log(result.data.data);	
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
			
			// previewImage(url) {
			// 	uni.previewImage({
			// 		current: url,
			// 		urls: [url]
			// 	})
			// },
		}
	}
</script>

<style>
	@import url("allFriendCircle.css");
</style>
