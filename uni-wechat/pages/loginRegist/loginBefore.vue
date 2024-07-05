<!-- 之前登录过的页面 -->
<template>
	<view class="page">
		

		<view class="login-box">
			
			<!-- 如果没有登录过的，用默认头像 -->
			<!-- <view class="unlogin-face-box">
				<image src="../../static/icons/icon-default-face.png" class="img-unlogin-face"></image>
			</view> -->
			<!-- 如果是已登录的，用下面的 -->
			<image :src="userInfo.face" class="img-login-face"></image>


			<text class="logined-mobile" v-if="userInfo.mobile != null && userInfo.mobile != ''">+86 {{userInfo.mobile}}</text>
			
			<view class="line" style="height: 1rpx; background-color: #f0f0f0; margin-top: 90rpx;"></view>
			
			<view class="mobile-box">

				<view class="mobile-box-left">
					<text class="txt-code-words">验证码</text>

					<input type="number" 
						class="mobile" 
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

			<!-- <text class="txt-go-regist">前往注册</text> -->
			
		</view>

		<view class="bottom-side-box">
			<view 
				:class="{'btn-login':!loginTouched, 'btn-login-touched': loginTouched, 'btn-go-login': true}"
				@touchstart="touchstartLogin"
				@touchend="touchendLogin"
				@click="loginOrRegist()">
					<text class="login-btn-text">登录</text>
			</view>
			
			<view class="change-page-box">
				<text class="txt-bottom-nav" @click="goToRegist">前往注册</text>
				
				<view class="spliter"></view>
				
				<text class="txt-bottom-nav" @click="goToNewAccountLogin">更换账号</text>
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
				
				userInfo: {
					mobile: "",
					face: ""
				},
				
				verifyCode: "",
				codeBtnText: "获得验证码",
				codeTimes: 0,
				
				loginTouched: false,
				codeTouched: false,
			}
		},
		onLoad() {
			this.statusBarHeight = system.statusBarHeight;
			
			this.initUser();
		},
		methods: {
			initUser() {
				
				var userInfo = app.getUserInfoSession();
				console.log(userInfo);
				if (userInfo != null && userInfo != undefined) {
					this.userInfo = userInfo;
				}
				
			},
			
			goToRegist() {
				uni.navigateTo({
					url: "../loginRegist/regist",
					animationType: "slide-in-bottom"
				})
			},

			goToNewAccountLogin() {
				uni.navigateTo({
					url: "../loginRegist/loginNewAccount",
					animationType: "slide-in-bottom"
				})
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
			
			typingVerifyCode(e) {
				this.verifyCode = e.detail.value;
			},
			getCode() {
				var me = this;
				
				if(me.codeTimes > 0) {
					return;
				}
				
				me.codeTouched = true;
				
				var mobile = me.userInfo.mobile;
				
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
			
			loginOrRegist() {
				var me = this;
				var verifyCode = me.verifyCode;
				var mobile = me.userInfo.mobile;
				
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
				 
				var serverUrl = app.globalData.serverUrl;
				// 调用后端登录注册
				uni.request({
					method: "POST",
					url: serverUrl + "/passport/login",
					data: {
						"mobile": mobile,
						"smsCode": verifyCode
					},
					success(result) {
						var status = result.data.status;
						if (status == 200) {
							var userInfo = result.data.data;
							console.log(userInfo);
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
	@import url("loginBefore.css");
</style>
