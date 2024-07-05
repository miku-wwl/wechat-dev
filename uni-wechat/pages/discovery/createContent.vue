<!-- 修改昵称页面 -->
<template>
	<view class="page">
		
		<view class="title-wrapper">
			<view class="number-box">
				<!-- <input
					class="number-input"
					type="text" 
					:value="wechatNumber" 
					:model="wechatNumber" 
					placeholder="这一刻的想法"
					maxlength="10"
					@input="typingContent"/> -->
				
				<textarea 
					id="thinkingTextarea"
					ref="thinkingTextarea" 
					:value="thinkingWords" 
					@input="typing" 
					:model="thinkingWords"
					class="thinking-input" 
					placeholder="这一刻的想法..." 
					maxlength="1024" 
					:style="{height: thinkingHeight+'px'}"
					@linechange="linechange"/>
			</view>
		</view>
		
		<view id="imageBox" ref="imageBox" class="image-box" >
			<block v-for="(image,index) in imageList" :key="image">
				<image :src="image" class="image-item" @longpress="deleteImage(index)" :style="{marginRight: getImageRightBlankPx(index)+'px', width: imageSize + 'px', height: imageSize + 'px'}"></image>
			</block>
		
			<view class="upload-image" @click="addImage()" v-if="imageList.length < 9" :style="{width: imageSize + 'px', height: imageSize + 'px'}">
				<image src="../../static/icons/plus-add.png" class="plus-add"></image>
			</view>
		</view>
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			return {
				// oldNickname: getApp().getUserInfoSession().nickname,
				// nickname: getApp().getUserInfoSession().nickname,
				wechatNumber: "",
				wordsLength: 0,
				
				showGreen: false,
				
				thinkingHeight: 46,
				imageRightBlankPx: 4,
				
				imageSize: 0,
				imageList:[],
				imageList2: [
					"../../static/faces/face-001.png",
					"../../static/faces/face-002.png",
					"../../static/faces/face-003.png",
					"../../static/faces/face-004.png",
					// "../../static/faces/face-005.png",
					"../../static/faces/face-005.png",
					"../../static/faces/face-006.png",
					"../../static/faces/face-007.png",
					"../../static/faces/face-008.png",
				],
				
				// pendingDeleteImageIndex: -1,
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
		},
		onNavigationBarButtonTap() {
			var imageList = this.imageList;
			var thinkingWords = this.thinkingWords;
			// console.log(imageList);
			// console.log(thinkingWords);
			
			if (imageList.length == 0 && app.isStrEmpty(thinkingWords)) {
				uni.showToast({
					icon: "none",
					title: "内容不能为空",
					duration: 2000
				})
				return;
			}
			
			var me = this;
			var userId = app.getUserInfoSession().id;
			
			var imageUrls = "";
			if (imageList.length > 0) {
				imageUrls = ",";
				for (var i = 0 ; i < imageList.length; i ++ ) {
					imageUrls += imageList[i];
					imageUrls += ",";
				}
			}
			
			var friendCircleBO = {
				"words": thinkingWords,
				"images": imageUrls,
			};
			console.log(friendCircleBO);
			
			var serverUrl = app.globalData.serverUrl;
			uni.request({
				method: "POST",
				header: {
					headerUserId: userId,
					headerUserToken: app.getUserSessionToken()
				},
				url: serverUrl + "/friendCircle/publish",
				data: friendCircleBO,
				success(result) {
					console.log(result);
					if (result.data.status == 200) {
						console.log(result.data);
						
						uni.navigateBack({
							delta: 1,
						});
						
					} else {
						uni.showToast({
							title: result.data.msg,
							icon: "none",
							duration: 3000
						});
					}
				}
			});
			
		},
		onReady() {
			this.reSize();
		},
		methods: {
			deleteImage(pendingDeleteImageIndex) {
				var me = this;
				
				uni.showActionSheet({
					itemList: ['删除'],
					success: function (res) {
						var index = res.tapIndex;
						
						if (index == 0) {
							var imageList = me.imageList;
							imageList.splice(pendingDeleteImageIndex, 1);
						} 
						
					}
				});	
			},
			
			typing(e) {
				var event = e;
				var thinkingWords = e.detail.value;
				this.thinkingWords = thinkingWords;
			},
			
			addImage() {
				var me = this;
				var imageList = me.imageList;
				// var userId = app.getUserInfoSession().id;
				var count = 9 - imageList.length;
				
				uni.chooseImage({
					count: count,
					sourceType: ["album"],
					success: (e) => {
						console.log(e);
						var newImageList = e.tempFilePaths;
						
						for (var i = 0 ; i < newImageList.length ; i ++) {
							// imageList.push(newImageList[i]);	
							me.uploadImages(newImageList[i]);
						}
						
						// return;
					}
				})
			},
			
			uploadImages(image) {
				var me = this;
				// 上传
				var serverUrl = app.globalData.serverUrl;
				var userId = app.getUserInfoSession().id;
				
				uni.uploadFile({
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/file/uploadFriendCircleImage?userId=" + userId,
					name: "file",
					filePath: image,
					success(result) {
						console.log(result);
						// console.log(result.data);
						
						if (result.statusCode == 200) {
							var res = JSON.parse(result.data);
							// console.log(res);
							var image = res.data;
							console.log(image);
							
							var imageList = me.imageList;
							imageList.push(image);
						} else {
							uni.showToast({
								title: "上传失败",
								icon: 'error'
							})
						}
					}
				})
			},
			
			getImageRightBlankPx(index) {
				var me = this;
				if ((index+1) % 3 == 0) {
					return 0;
				}
				return me.imageRightBlankPx;
			},
			
			reSize() {
				var me = this;
				uni.createSelectorQuery().in(this).select("#imageBox").boundingClientRect(data => {
				  console.log(JSON.stringify(data))
				  var imageBoxWidth = data.width;
				  var imageRightBlankPx = me.imageRightBlankPx;
				  // var imageSize = (imageBoxWidth - imageRightBlankPx*2) / 3;
				  var imageSize = (imageBoxWidth - 6*2) / 3;
				  console.log("imageSize = " + imageSize);
				  me.imageSize = imageSize;
				  // if (lineCount > 5 && data.height < 118) {
					 //  me.msgHeight = 118;
				  // }
				}).exec();
			},
			
			linechange(e) {
				// event.detail = {height: 0, heightRpx: 0, lineCount: 0}
				// console.log(e);
				var me = this;
				var lineCount = e.detail.lineCount;
				// if (lineCount <= 5) {
					var thinkingHeight = me.thinkingHeight + 23;
					me.thinkingHeight = thinkingHeight;
				// }
				// if (lineCount > 1) {
				// 	me.alignSelf = "flex-end";
				// }
				
				// uni.createSelectorQuery().in(this).select("#thinkingTextarea").boundingClientRect(data => {
				//   // console.log(JSON.stringify(data))
				//   if (lineCount > 5 && data.height < 118) {
				// 	  me.msgHeight = 118;
				//   }
				// }).exec();
			},
			
			updateNickname() {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				var nickname = this.nickname;
				
				var pendingUserInfo = {
					"userId": userId,
					"nickname": nickname
				};
				
				// 修改昵称
				var serverUrl = app.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/userinfo/modify",
					data: pendingUserInfo,
					success(result) {
						
						if (result.data.status == 200) {
							var userInfo = result.data.data;
							// 重置本地用户信息
							app.setUserInfoSession(userInfo);
							app.setUserSessionToken(userInfo.userToken);
							
							uni.showToast({
								title: "修改成功~",
								duration: 1500,
								// success() {
								// 	uni.navigateBack({
								// 		delta: 1,
								// 		animationType: "fade-out"
								// 	})
								// }
							})
						} else {
							uni.showToast({
								title: result.data.msg,
								icon: "none",
								duration: 3000
							});
						}
						
					}
				})
			},
			
			typingContent(e) {
				var me = this;
				
				var event = e;
				var wechatNumber = e.detail.value;
				this.wechatNumber = wechatNumber;
				var wordsLength = wechatNumber.length;
				this.wordsLength = wordsLength;
				console.log(wordsLength);
				// if (wordsLength >= 1) {
				// 	me.showGreen = true;
				// } else {
				// 	me.showGreen = false;
				// }
			},
		}
	}
</script>

<style>
	@import url("createContent.css");
</style>
