<template>
	<view>
		<view class="myInfo" @click="">
			
			<view class="status_bar" style="opacity: 0;">
				<!-- 这里是状态栏 -->
			</view>
			
			<view class="navigation-bar" >
				<image src="../../static/icons/hr/icon-back-white.png" class="back-icon" @click="goBack()"></image>
				
				<view class="middle-title"></view>
				
				<image src="../../static/icons/wechat/moreinfo.png" v-if="env == 'weilai'" class="add-icon"></image>
			</view>
			
			<image src="../../static/images/default.png" class="bg-image" :style="{'width': windowWidth + 'px', 'height': windowWidth + 'px'}"></image>
			
			<view class="person-box">
				<view class="one-line">
					<view class="left-nickname">
						风间影月
					</view>
					<image class="right-face" src="../../static/faces/face-001.png"></image>
				</view>
				<view class="two-line">
					真理永远掌握在远光灯的射程范围内。
				</view>
			</view>
			
		</view>
		
		<view class="all-status-box">
			<view class="every-day-wrapper">
				<view class="day-box">今天</view>
				
				<view class="list-box">
					<image class="take-a-shot" src="../../static/images/camera2.png" style="margin-bottom: 6px;"></image>
					
					<view class="img-item-wrapper">
						<!-- 只有一张图片的时候 -->
						<view class="">
							<image class="take-a-shot" mode="aspectFill" @click="previewImage('../../static/images/face500k.jpg')" src="../../static/images/face500k.jpg"></image>
						</view>
						
						<view class="words-box">
							<text class="status-words status-box">风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~</text>
						</view>
					</view>
					
					<!-- 纯文字 -->
					<view class="img-item-wrapper" style="padding: 4px;background-color: #f6f7f6;margin-bottom: 6px;">
						<text class="status-words only-words status-box">风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~</text>
					</view>
					
					<view class="img-item-wrapper">
						<!-- 有2张图片的时候 -->
						<view class="two-part-wrapper">
							<image class="two-part-imgs" mode="aspectFill" src="../../static/images/default.png"></image>
							<image class="two-part-imgs" mode="aspectFill" src="../../static/faces/face-009.png" style="margin-left: 2px;"></image>
						</view>
						
						<view class="words-box contain-up-down">
							<text class="status-words status-box">风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~</text>
							
							<text class="total-wods">共2张</text>
						</view>
					</view>
					
					<view class="img-item-wrapper">
						<!-- 有4张以上图片的时候 -->
						<view class="four-part-wrapper">
							<image class="four-part-imgs" mode="aspectFill" src="../../static/images/face500k.jpg"></image>
							<image class="four-part-imgs" mode="aspectFill" src="../../static/images/default.png"></image>
							<image class="four-part-imgs" mode="aspectFill" src="../../static/faces/face-002.png"></image>
							<image class="four-part-imgs" mode="aspectFill" src="../../static/images/lee-face.jpg"></image>
						</view>
						
						<view class="words-box contain-up-down">
							<text class="status-words status-box"></text>
							
							<text class="total-wods">共5张</text>
						</view>
					</view>
					
					<view class="img-item-wrapper">
						<!-- 有3张图片的时候 -->
						<view class="three-part-wrapper">
							<image class="two-part-imgs" mode="aspectFill" src="../../static/images/face500k.jpg"></image>
							
							<view class="right-updown-box">
								<image class="four-part-imgs" mode="aspectFill" src="../../static/images/default.png"></image>
								<image class="four-part-imgs" mode="aspectFill" src="../../static/faces/face-002.png"></image>
							</view>
						</view>
						
						<view class="words-box contain-up-down">
							<text class="status-words status-box"></text>
							
							<text class="total-wods">共3张</text>
						</view>
					</view>
				</view>
			</view>
			
			<view class="every-day-wrapper">
				<view class="day-box">昨天</view>
				
				<view class="list-box">
					
					<view class="img-item-wrapper">
						<!-- 有2张图片的时候 -->
						<view class="two-part-wrapper">
							<image class="two-part-imgs" mode="aspectFill" src="../../static/images/default.png"></image>
							<image class="two-part-imgs" mode="aspectFill" src="../../static/faces/face-009.png" style="margin-left: 2px;"></image>
						</view>
						
						<view class="words-box contain-up-down">
							<text class="status-words status-box">风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~风景不错呀~天气不错呀~~今天吃的不错呀~~~</text>
							
							<text class="total-wods">共2张</text>
						</view>
					</view>
					
					<view class="img-item-wrapper">
						<!-- 有3张图片的时候 -->
						<view class="three-part-wrapper">
							<image class="two-part-imgs" mode="aspectFill" src="../../static/images/face500k.jpg"></image>
							
							<view class="right-updown-box">
								<image class="four-part-imgs" mode="aspectFill" src="../../static/images/default.png"></image>
								<image class="four-part-imgs" mode="aspectFill" src="../../static/faces/face-002.png"></image>
							</view>
						</view>
						
						<view class="words-box contain-up-down">
							<text class="status-words status-box"></text>
							
							<text class="total-wods">共3张</text>
						</view>
					</view>
				</view>
			</view>
			
			<view class="every-day-wrapper">
				<view class="day-box">
					<text>25</text>
					<text style="font-size: 12px;margin-left: 2px;">12月</text>
				</view>
				
				<view class="list-box">
					
					<view class="img-item-wrapper">
						<!-- 有4张以上图片的时候 -->
						<view class="four-part-wrapper">
							<image class="four-part-imgs" mode="aspectFill" src="../../static/images/face500k.jpg"></image>
							<image class="four-part-imgs" mode="aspectFill" src="../../static/images/default.png"></image>
							<image class="four-part-imgs" mode="aspectFill" src="../../static/faces/face-002.png"></image>
							<image class="four-part-imgs" mode="aspectFill" src="../../static/images/lee-face.jpg"></image>
						</view>
						
						<view class="words-box contain-up-down">
							<text class="status-words status-box"></text>
							
							<text class="total-wods">共5张</text>
						</view>
					</view>
					
					<view class="img-item-wrapper">
						<!-- 有3张图片的时候 -->
						<view class="three-part-wrapper">
							<image class="two-part-imgs" mode="aspectFill" src="../../static/images/face500k.jpg"></image>
							
							<view class="right-updown-box">
								<image class="four-part-imgs" mode="aspectFill" src="../../static/images/default.png"></image>
								<image class="four-part-imgs" mode="aspectFill" src="../../static/faces/face-002.png"></image>
							</view>
						</view>
						
						<view class="words-box contain-up-down">
							<text class="status-words status-box"></text>
							
							<text class="total-wods">共3张</text>
						</view>
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
				
				currentUserInfo: null, 
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
		onPageScroll(e) {
		},
		methods:{
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
	@import url("myFriendCircle.css");
</style>
