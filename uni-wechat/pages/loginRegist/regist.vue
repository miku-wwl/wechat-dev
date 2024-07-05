<!-- 之前登录过的页面 -->
<template>
	<view class="page">

		<view class="navigation-bar" style="z-index: 77777;;background-color: #FFFFFF">
			<view class="nav-left">
				<image src="../../static/icons/close.png" class="back-icon" @click="goBack"></image>
			</view>
			
			<view class="nav-middle">
				<!-- <text class="page-title">page-title</text> -->
				<!-- <text class="page-info">page-info</text> -->
			</view>
			
			<view class="nav-right">
			</view>
		</view>

		<view class="login-box">

			<text class="logined-mobile">用手机号注册</text>
			
			<view class="unlogin-face-box">
				<image src="../../static/icons/icon-default-face.png" class="img-unlogin-face"></image>
			</view>

			<view class="line" style="height: 1rpx; background-color: #f0f0f0; margin-top: 90rpx;"></view>
			
			<view class="mobile-box">
 
				<view class="mobile-box-left">
					<text class="txt-code-words">昵称</text>

<!-- @input="typingVerifyCode" -->
					<input type="number" 
						class="mobile" 
						:value="nickname"
						:model="nickname"
						@input="typingNickname"
						placeholder="请填写昵称" 
						placeholder-class="mobile-placeholder"
						maxlength="6"/>
				</view>

			</view>

			<view class="line" style="height: 1rpx; background-color: #f0f0f0;"></view>

			<view class="mobile-box">

				<view class="mobile-box-left">
					<text class="txt-code-words">国家/地区</text>
					<text class="txt-cn-words" @click="onlyChooseCn">中国大陆</text>
				</view>
				<image src="../../static/icons/right-arrow.png" class="icon-choose-country" @click="onlyChooseCn"></image>
			</view>

			<view class="line" style="height: 1rpx; background-color: #f0f0f0;"></view>

			<view class="mobile-box">

				<view class="mobile-box-left">
					<text class="txt-code-words">手机号</text>

					<text class="txt-prefix-words">+86</text>

					<input type="number" 
						class="mobile" 
						:value="mobile"
						:model="mobile"
						@input="typingMobile"
						placeholder="请填写手机号码" 
						placeholder-class="mobile-placeholder"
						maxlength="11"/>
				</view>

			</view>
			
			<view class="line" style="height: 1rpx; background-color: #f0f0f0;"></view>
			
			<view class="mobile-box">

				<view class="mobile-box-left">
					<text class="txt-code-words">验证码</text>

					<input type="number" 
						class="mobile-verify-code" 
						:value="verifyCode"
						:model="verifyCode"
						@input="typingVerifyCode"
						placeholder="请输入验证码" 
						placeholder-class="mobile-placeholder"
						maxlength="6"/>
				</view>

				<view
					:class="{'btn-code':!codeTouched, 'btn-code-touched': codeTouched, 'btn-get-code': true}"
					@touchstart="touchstartCode"
					@touchend="touchendCode"
					@click="getCode()">
					<text class="code-btn-text" style="align-self: center;">{{codeBtnText}}</text>
				</view>
			</view>
			<view class="line" style="height: 1rpx; background-color: #f0f0f0;"></view>
			
		</view>

		<view class="bottom-side-box">
			<view 
				:class="{'btn-login':!loginTouched, 'btn-login-touched': loginTouched, 'btn-go-login': true}"
				@touchstart="touchstartLogin"
				@touchend="touchendLogin"
				@click="doRegist()">
					<text class="login-btn-text">点击注册</text>
			</view>
			
		</view>
	</view>
</template>

<script>
	var system = uni.getSystemInfoSync();
	var app = getApp();
	export default {
		data() {
			return {
				statusBarHeight: 0,
				
				nickname: "",
				mobile: "13912340000",
				verifyCode: "",
				codeBtnText: "获得验证码",
				codeTimes: 0,
				
				loginTouched: false,
				codeTouched: false,
			}
		},
		onLoad() {
			this.statusBarHeight = system.statusBarHeight;
		},
		methods: {
			onlyChooseCn() {
				uni.showToast({
					title: '仅提供中国大陆地区',
					icon: "none",
					duration: 2000
				});
			},

			goBack() {
				app.goBack(1);
			},

			touchstartLogin() {
				this.loginTouched = true;
			},
			touchendLogin() {
				this.loginTouched = false;
			},
			touchstartCode() {
				// timer在运行期间，禁止触摸
				if(this.codeTimes > 0) {
					return;
				} else {
					this.codeTouched = true;
				}
			},
			touchendCode() {
				// timer在运行期间，禁止触摸
				if(this.codeTimes > 0) {
					return;
				} else {
					this.codeTouched = false;
				}
			},
			close() {
				uni.navigateBack({
					delta: 1,
					animationType: "slide-out-bottom"
				})
			},
			typingMobile(e) {
				var event = e;
				this.mobile = e.detail.value;
			},
			typingVerifyCode(e) {
				var event = e;
				this.verifyCode = e.detail.value;
			},
			typingNickname(e) {
				var event = e;
				this.nickname = e.detail.value;
			},
			getCode() {
				var me = this;
				
				if(me.codeTimes > 0) {
					return;
				}
				
				me.codeTouched = true;
				
				var mobile = me.mobile;
				
				if (app.isStrEmpty(mobile) || mobile.length != 11) {
					uni.showToast({
						title: "手机号不正确",
						icon: "none"
					});
					return;
				}
				
				var serverUrl = app.globalData.serverUrl;
				// 调用后端发送验证码
				uni.request({
					method: "POST",
					url: serverUrl + "/passport/getSMSCode?mobile=" + mobile,
					success(result) {
						var status = result.data.status;
						if (status != 200) {
							uni.showToast({
								title: result.data.msg,
								icon: "none"
							});
						}
						
						// 开始倒数60秒限制
						if(me.codeTimes == 0) {
							me.doTimer(59);
						}
					}
				});
					
			},
			// 发送验证码的倒计时方法
			doTimer(times) {
				var me = this;
				// 倒计时定时器
				var sendCodeBtnFunction = function(){
					var left = times--;
	
					if (left <= 0) {
						me.codeTouched = false;
						me.codeBtnText = "发送验证码";
						clearInterval(smsTimer);
					} else {
						me.codeBtnText = left + "s";
					}
					me.codeTimes = left;
				};
				var smsTimer = setInterval(sendCodeBtnFunction, 1000);
			},
			
			doRegist() {
				var me = this;
				var verifyCode = me.verifyCode;
				var mobile = me.mobile;
				
				if (app.isStrEmpty(verifyCode)) {
					uni.showToast({
						title: "请填写验证码",
						icon: "none"
					});
					return;
				}
				
				if (app.isStrEmpty(mobile) || mobile.length != 11) {
					uni.showToast({
						title: "手机号不正确",
						icon: "none"
					});
					return;
				}
				 
				var nickname = me.nickname;
				
				var serverUrl = app.globalData.serverUrl;
				// 调用后端登录注册
				uni.request({
					method: "POST",
					url: serverUrl + "/passport/regist",
					data: {
						"mobile": mobile,
						"smsCode": verifyCode,
						"nickname": nickname
					},
					success(result) {
						var status = result.data.status;
						if (status == 200) {
							var userInfo = result.data.data;
							app.setUserInfoSession(userInfo);
							app.setUserSessionToken(userInfo.userToken);
							// 登录成功，关闭当前页
							me.goto();
						} else if (status != 200) {
							uni.showToast({
								title: result.data.msg,
								icon: "none",
								duration: 3000
							});
						}
					}
				});
			},
			
			goto() {
				var me = this;
				uni.switchTab({
					url: "../msgList/msgList"
				});
			}
		}
	}
</script>

<style>
	@import url("regist.css");
</style>
