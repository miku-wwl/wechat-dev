<!-- 设置 -->
<template>
	<view>
		
		<view class="block-one" style="border-bottom: 0.5px solid #ebebeb;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineChooseImgTouched, 'item-line-touched': lineChooseImgTouched, 'block-line': true}"
					@touchstart="lineChooseImgTouched=true"
					@touchend="lineChooseImgTouched=false"
					@click="chooseFromAlbum()">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">从手机相册选择</text>
						<image src="../../static/icons/right-arrow.png" style="width: 12px;height: 12px;align-self: center;margin-right: 12px;"></image>
					</view>
				</view>
			</view>
		</view>
		<view class="block-one" style="border-bottom: 0.5px solid #ebebeb;">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!lineDefaultTouched, 'item-line-touched': lineDefaultTouched, 'block-line': true}"
					@touchstart="lineDefaultTouched=true"
					@touchend="lineDefaultTouched=false"
					@click="chooseFromCamera()">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">拍一个</text>
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
		components: {
		},
		data() {
			return {
				lineChooseImgTouched: false,
				lineDefaultTouched: false,
				
				whoami: "",
			}
		},
		onShow() {
			var userType = uni.getStorageSync('userType');
			console.log(userType);
			
		},
		onLoad() {
		},
		methods: {
			returnToDefault() {
				
			},
			
			chooseFromAlbum() {
				var me = this;
				
				uni.chooseImage({
					count: 1,
					sourceType: ["album"],
					crop: {
						quality: 60,
						width: 300,
						height: 300
					},
					success: (e) => {
						var img = e.tempFilePaths[0];
						me.img = img;
						
						me.updateNewBg(img);
					}
				})
			},
			
			chooseFromCamera() {
				var me = this;
				
				uni.chooseImage({
					count: 1,
					sourceType: ["camera"],
					crop: {
						quality: 60,
						width: 300,
						height: 300
					},
					success: (e) => {
						var img = e.tempFilePaths[0];
						me.img = img;
						
						me.updateNewBg(img);
					}
				})
			},
			
			
			updateNewBg(newBg) {
				// 上传
				var serverUrl = app.globalData.serverUrl;
				var userId = app.getUserInfoSession().id;
				
				uni.uploadFile({
					header: {
						headerUserId: userId,
						headerUserToken: app.getUserSessionToken()
					},
					url: serverUrl + "/file/uploadFriendCircleBg?userId=" + userId,
					name: "file",
					filePath: newBg,
					success(result) {
						// console.log(result);
						// console.log(result.data);
						
						if (result.statusCode == 200) {
							var res = JSON.parse(result.data);
							// console.log(res);
							
							if (res.status == 200) {
								var userInfo = res.data;
								console.log(userInfo);
								// 重置本地用户信息
								app.setUserInfoSession(userInfo);
								app.setUserSessionToken(userInfo.userToken);
								
								uni.showToast({
									icon: "none",
									title: "朋友圈背景更换成功~",
									duration: 1500
								})
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'none'
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
	@import url("circleCover.css");
</style>