<!-- 上传头像页面 -->
<template>
	<view class="page">
		
		<!-- <view class="bg-wrapper"> -->
			<image :src="faceUrl" class="bg-size"></image>
			
			<!-- <view 
				class="button-change-bg"
				@click="changeFace()"
				style="width: 250rpx;height: 80rpx;border-radius: 50px;display: flex;flex-direction: column;justify-content: center;margin-top: 150rpx;align-self: center;">
				<text style="color: #FFFFFF;align-self: center;">更换头像</text>
			</view> -->
		<!-- </view> -->
		
	</view>
</template>

<script>
	var app = getApp();
	let sys = uni.getSystemInfoSync();
	export default {
		data() {
			return {
				bgTop: 0,
				
				faceUrl: "../../../static/images/me.png",
				// faceUrl: "https://img.alicdn.com/imgextra/i4/O1CN01aG16y424E11XsURUd_!!6000000007358-2-tps-206-240.png",
				// faceUrl: app.getUserInfoSession().face,
			}
		},
		onLoad() {
			// var windowHeight = sys.windowHeight;
			// var windowWidth = sys.windowWidth;
			
			// console.log(windowHeight);
			// console.log(windowWidth);
			
			// var bgTop = windowHeight/2 - windowWidth/2;
			// this.bgTop = 50;
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
			this.faceUrl = currentUserInfo.face;
		},
		onNavigationBarButtonTap() {
			var me = this;
			uni.showActionSheet({
				itemList: ['拍照', '从手机相册选择', '保存图片'],
				success: function (res) {
					var index = res.tapIndex;
					
					if (index == 0) {
						me.changeFaceByCamera();
					} else if (index == 1) {
						me.changeFaceByPhoto();
					} else if (index == 2) {
						me.downloadFace();
					}
					
				}
			});	
		},
		methods: {
			downloadFace() {
				
				var faceUrl = this.faceUrl;
				
				// H5不支持下载文件
				// #ifdef H5
					uni.showModal({
						title: 'H5不支持图片下载, 建议使用鼠标 [右键另存为]',
						cancelText: "好吧",
						confirmText: "我知道了",
						confirmColor: "#31B9B3",
						success(e) {
							if (e.confirm) {
							}
						}
					});
				// #endif
				
				// #ifdef APP
					uni.downloadFile({
						url: faceUrl, //仅为示例，并非真实的资源
						success: (res) => {
							if (res.statusCode === 200) {
								console.log('下载成功');
								console.log(res);
							}
							let that = this;
							
							uni.saveImageToPhotosAlbum({
								filePath: res.tempFilePath,
								success() {
									uni.showToast({
										title: '图片保存成功',  
										icon: 'success'  
									})  
								},
								fail() {
									uni.showToast({
										title: '保存失败，请稍后重试！',  
										icon: 'none'  
									})  
								}
							})
							
							// uni.saveFile({
							// 	tempFilePath: res.tempFilePath,
							// 	// tempFilePath: this.fileNameEscape(res.tempFilePath),
							// 	success: function(res) {
							// 		that.file = res.savedFilePath
							// 		console.log(res)
									
							// 			//打开文档查看
							// 			uni.openDocument({
							// 				filePath: that.file,
							// 				success: function(res) {
							// 					console.log('打开文档成功');
							// 				},
							// 				fail: function() {
							// 					console.log('打开失败');
							// 				}
							// 			});
							// 	},
							// 	fail(e) {
							// 		console.log(e);
							// 	}
							// });
						},
						fail(e) {
							console.log(e);
						}
					});
				// #endif
			},
			
			changeFaceByPhoto() {
				var me = this;
				// var userId = app.getUserInfoSession().id;
				
				uni.chooseImage({
					count: 1,
					sourceType: ["album"],
					crop: {
						quality: 60,
						width: 300,
						height: 300
					},
					success: (e) => {
						var newFace = e.tempFilePaths[0];
						me.faceUrl = newFace;
						
						me.updateNewFace(newFace);
					}
				})
			},
			
			changeFaceByCamera() {
				var me = this;
				// var userId = app.getUserInfoSession().id;
				
				uni.chooseImage({
					count: 1,
					sourceType: ["camera"],
					crop: {
						quality: 60,
						width: 300,
						height: 300
					},
					success: (e) => {
						var newFace = e.tempFilePaths[0];
						me.faceUrl = newFace;
						
						me.updateNewFace(newFace);
					}
				})
			},
			
			updateNewFace(newFace) {
				// 上传
				var serverUrl = app.globalData.serverUrl;
				var userId = app.getUserInfoSession().id;
				
				uni.uploadFile({
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/file/uploadFace?userId=" + userId,
					name: "file",
					filePath: newFace,
					success(result) {
						// console.log(result);
						// console.log(result.data);
						
						if (result.statusCode == 200) {
							var res = JSON.parse(result.data);
							// console.log(res);
							
							if (res.status == 200) {
								var userInfo = res.data;
								// console.log(userInfo);
								// 重置本地用户信息
								app.setUserInfoSession(userInfo);
								app.setUserSessionToken(userInfo.userToken);
								
								uni.showToast({
									title: "用户头像修改成功~",
									duration: 1500
								})
								
								// uni.request({
								// 	method: "POST",
								// 	url: serverUrl + "/userinfo/modify",
								// 	data: {
								// 		"userId": userId,
								// 		"face": faceNewUrl
								// 	},
								// 	header: {
								// 		headerUserId: userId,
								// 		headerUserToken: app.getUserSessionToken()
								// 	},
								// 	success(result) {
								// 		var status = result.data.status;
								// 		if (status == 200) {
								// 			var userInfo = result.data.data;
								// 			// 重置本地用户信息
								// 			app.setUserInfoSession(userInfo);
								// 			app.setUserSessionToken(userInfo.userToken);
											
								// 			uni.showToast({
								// 				title: "用户头像修改成功~",
								// 				duration: 1500
								// 			})
											
								// 		} else if (status != 200) {
								// 			uni.showToast({
								// 				title: result.data.msg,
								// 				icon: "none",
								// 				duration: 3000
								// 			});
								// 		}
								// 	}
								// });
								
								// uni.navigateBack({
								// 	delta: 1,
								// 	animationType: "fade-out"
								// })
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'error'
								})
							}
						} else {
							uni.showToast({
								title: "上传失败",
								icon: 'error'
							})
						}
					}
				})
			},
			
		}
	}
</script>

<style>
	@import url("myFace.css");
</style>