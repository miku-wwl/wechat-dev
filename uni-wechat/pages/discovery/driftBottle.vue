<template>
	<view>
		<view class="myInfo">
			
			<!-- <image src="../../static/files/sea.png" :style="{'width': windowWidth + 'px', 'height': windowHeight + 'px'}"></image> -->
			<image src="../../static/files/sea.gif" class="sea" :style="{'width': windowWidth + 'px'}"></image>

			<!-- 扔一个 捞一个 我捞的 -->
			
			<view class="display-area">
				<view 
					:class="{'bottle-oper-wrapper': true, 'bottle-oper-selected': lineRengyigeTouched}" 
					@touchstart="lineRengyigeTouched=true"
					@touchend="lineRengyigeTouched=false"
					@click="rengyige()">
					<image src="../../static/images/bottles/rengyige.png" class="rengyige"></image>
					<text class="action-words">扔一个</text>
				</view>
				
				<view
					:class="{'bottle-oper-wrapper': true, 'bottle-oper-selected': lineLaoyigeTouched}" 
					@touchstart="lineLaoyigeTouched=true"
					@touchend="lineLaoyigeTouched=false"
					@click="laoyige()"
					>
					<image src="../../static/images/bottles/laoyige.png" class="laoyige"></image>
					<text class="action-words">捞一个</text>
				</view>
				
				<!-- <view
					:class="{'bottle-oper-wrapper': true, 'bottle-oper-selected': lineWojiandeTouched}" 
					@touchstart="lineWojiandeTouched=true"
					@touchend="lineWojiandeTouched=false">
					<image src="../../static/images/bottles/wojiande.png" class="wojiande"></image>
					<text class="action-words">我捡的</text>
				</view> -->
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
				
				lineRengyigeTouched: false,
				lineLaoyigeTouched: false,
				lineWojiandeTouched: false,
				
				windowWidth: 0,
				windowHeight: 0,
			}
		},
		onShow() {
			// 获得用户信息
			// var currentUserInfo = getApp().getUserInfoSession();
			// this.currentUserInfo = currentUserInfo;
			
		},
		onLoad(e) {
			let res =uni.getSystemInfoSync()
			this.windowWidth = res.windowWidth;
			this.windowHeight = res.windowHeight;
		},
		methods:{
			createMyContent() {
				uni.navigateTo({
					url: "/pages/discovery/createContent",
					animationType: "slide-in-bottom",
				});
			},
			
			rengyige() {
				
				uni.showModal({
					title: "我要扔一个漂流瓶!",
					editable: true,
					cancelText: "撤回",
					confirmText: "扔出",
					success(e) {
						
						if (e.confirm) {
							var content = e.content;
							console.log(content);
						}
					}
				})
				
			},
			
			laoyige() {
				
				uni.showModal({
					title: "收获一个新漂流瓶!",
					content: "今天发大财了~~~今天发大财了~~~今天发大财了~~~",
					cancelText: "扔了",
					confirmText: "收藏",
					success(e) {
						
						if (e.confirm) {
							console.log("收藏...");
						}
					}
				})
				
			},
			
			loseComment(e) {
				// 页面滚动，评论框消失
				// this.commentFocus = false;
				console.log(e);
			},
			
		}
	}
</script>

<style>
	@import url("driftBottle.css");
</style>
