<!-- HR 个人信息页面 -->
<template>
	<view>
		<view class="block-box">
			<view
				:class="{'item-line-notouch':!lineFaceTouched, 'item-line-touched': lineFaceTouched, 'block-line': true}"
				@touchstart="lineFaceTouched=true"
				@touchend="lineFaceTouched=false"
				@click="goToEditMyFace()"
				style="height: 76px;">
				<view class="left-part">
					<text class="field-lable">头像</text>
				</view>
				
				<view class="right-part">
					<image :src="currentUserInfo.face" class="small-face"></image>
					
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view
				:class="{'item-line-notouch':!lineNicknameTouched, 'item-line-touched': lineNicknameTouched, 'block-line': true}"
				@touchstart="lineNicknameTouched=true"
				@touchend="lineNicknameTouched=false"
				@click="goToEditMyNickname()"
				>
				<view class="left-part">
					<text class="field-lable">名字</text>
				</view>
				
				<view class="right-part">
					<text class="field-words">{{currentUserInfo.nickname}}</text>
					
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view
				:class="{'item-line-notouch':!linePaiTouched, 'item-line-touched': linePaiTouched, 'block-line': true}"
				@touchstart="linePaiTouched=true"
				@touchend="linePaiTouched=false"
				@click="goToPaiyipai()">
				<view class="left-part">
					<text class="field-lable">拍一拍</text>
				</view>
				
				<view class="right-part">
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view
				:class="{'item-line-notouch':!lineWechatNumTouched, 'item-line-touched': lineWechatNumTouched, 'block-line': true}"
				@touchstart="lineWechatNumTouched=true"
				@touchend="lineWechatNumTouched=false"
				@click="goToEditMyWechatNumber()">
				<view class="left-part">
					<text class="field-lable">微信号</text>
				</view>
				
				<view class="right-part">
					<text class="field-words">{{currentUserInfo.wechatNum}}</text>
					
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view
				:class="{'item-line-notouch':!lineMyQrCodeTouched, 'item-line-touched': lineMyQrCodeTouched, 'block-line': true}"
				@touchstart="lineMyQrCodeTouched=true"
				@touchend="lineMyQrCodeTouched=false"
				@click="goToMyQrCode">
				<view class="left-part">
					<text class="field-lable">我的二维码</text>
				</view>
				
				<view class="right-part">
					<image src="../../static/weilai/icon-qrcode.png" class="icon-qrcode"></image>
					
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view
				:class="{'item-line-notouch':!lineSexTouched, 'item-line-touched': lineSexTouched, 'block-line': true}"
				@touchstart="lineSexTouched=true"
				@touchend="lineSexTouched=false" 
				@click="goToEditMySex()">
				<view class="left-part">
					<text class="field-lable">性别</text>
				</view>
				
				<view class="right-part">
					<text class="field-words" v-if="currentUserInfo.sex == 1">男</text>
					<text class="field-words" v-if="currentUserInfo.sex == 2">保密</text>
					<text class="field-words" v-if="currentUserInfo.sex == 0">女</text>
					
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view
				:class="{'item-line-notouch':!lineAddressTouched, 'item-line-touched': lineAddressTouched, 'block-line': true}"
				@touchstart="lineAddressTouched=true"
				@touchend="lineAddressTouched=false"
				@click="goToMyAddress()">
				<view class="left-part">
					<text class="field-lable">地区</text>
				</view>
				
				<view class="right-part">
					<text class="field-words">{{currentUserInfo.province}} {{currentUserInfo.city}}</text>
					
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view
				:class="{'item-line-notouch':!lineSingTouched, 'item-line-touched': lineSingTouched, 'block-line': true}"
				@touchstart="lineSingTouched=true"
				@touchend="lineSingTouched=false"
				@click="goToEditMySign()">
				<view class="left-part">
					<text class="field-lable">个性签名</text>
				</view>
				
				<view class="right-part my-sign-wrapper" style="width: 68%;">
					<text class="field-words too-much-words" style="width: 90%;text-align: right;">{{currentUserInfo.signature}}</text>
					
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<!-- <view class="block-line">
				<view class="left-part">
					<text class="field-lable">生日</text>
				</view>
				
				<view class="right-part" @click="">
					<picker mode="multiSelector" :range="dateRange" :value="currentUserInfo.birthday" @change="bindBirthdayChange"
						:class="currentUserInfo == null ? 'picker' : 'picker-words'">
						<view class="field-words">{{currentUserInfo.birthday}}</view>
					</picker>
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view> -->
			
			<!-- <view class="line-wrapper"><view class="line"></view></view>
			
			<view class="line-wrapper"><view class="line"></view></view>
			
			<view class="block-line">
				<view class="left-part">
					<text class="field-lable">邮箱</text>
				</view>
				
				<view class="right-part" @click="goToEditMyEmail()">
					<text class="field-words undo">未填写</text>
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view> -->
			
			<!-- <view class="block-line" v-show="currentUserType == 2">
				<view class="left-part">
					<text class="field-lable">参加工作时间</text>
				</view>
				
				<view class="right-part" @click="">
					<picker mode="multiSelector" :range="workYearRange" :value="currentUserInfo.startWorkDate" @change="bindWorkYearChange"
						:class="(currentUserInfo == null || currentUserInfo.startWorkDate == null || currentUserInfo.startWorkDate == '') ? 'picker' : 'picker-words'">
						<view class="field-words">{{(currentUserInfo.startWorkDate == null || currentUserInfo.startWorkDate == '') ? "请选择" : currentUserInfo.startWorkDate}}</view>
					</picker>
					<image src="../../static/icons/right-arrow-gray.png" class="right-arrow"></image>
				</view>
			</view>
			
			<view class="line-wrapper"><view class="line"></view></view> -->
			
		</view>
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			const date = new Date()
			const years = []
			const year = date.getFullYear()
			const months = []
			const month = date.getMonth() + 1
			const days = []
			const day = date.getDate()
			for (let i = 1990; i <= date.getFullYear(); i++) {
				years.push(i)
			}
			for (let i = 1; i <= 12; i++) {
				months.push(i)
			}
			for (let i = 1; i <= 31; i++) {
				days.push(i)
			}
			return {
				lineFaceTouched: false,
				lineNicknameTouched: false,
				linePaiTouched: false,
				lineWechatNumTouched: false,
				lineMyQrCodeTouched: false,
				lineSexTouched: false,
				lineAddressTouched: false,
				lineSingTouched: false,
				
				
				birthday: "请选择",
				workYear: "请选择",
				startWorkYear: "",
				
				years,
				year,
				months,
				month,
				days,
				day,
				dateRange: [
					years, 
					months,
					days
				],
				workYearRange: [
					years, 
					months
				],
				
				currentUserInfo: null, 
			}
		},
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.currentUserInfo = currentUserInfo;
		},
		onLoad(options) {
			// console.log(options);
		},
		methods: {
            // 定义一个函数function取名为getNumber
            getNumber(e) {
                // 定时任务，打印随机数
                setInterval(function() {
                    // 随机数
                    var random = Math.random();
                    // 把随机数赋值给num
                    this.num = random;   
                    console.log(this.num);   
                }, 1000);
            },

            // 定义一个函数function取名为getNumber
			
			bindWorkYearChange(e) {
				var selectedArray = e.detail.value;
				var yearIndex = selectedArray[0];
				var monthIndex = selectedArray[1];
				
				var year = this.workYearRange[0][yearIndex];
				var month = this.workYearRange[1][monthIndex];
				
				this.workYear = year + "-" + (month < 10 ? '0' + month : month);
				this.startWorkYear = year + "-" + (month < 10 ? '0' + month : month) + "-01";
				
				this.currentUserInfo.startWorkDate = this.startWorkYear;
				
				this.updateStartWorkDate(this.startWorkYear);
			},
			
			updateStartWorkDate(startWorkDate) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				console.log("startWorkDate=" + startWorkDate);
				var pendingUserInfo = {
					"userId": userId,
					"startWorkDate": startWorkDate
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
			
			bindBirthdayChange(e) {
				var selectedArray = e.detail.value;
				var yearIndex = selectedArray[0];
				var monthIndex = selectedArray[1];
				var dayIndex = selectedArray[2];
				
				var year = this.dateRange[0][yearIndex];
				var month = this.dateRange[1][monthIndex];
				var day = this.dateRange[2][dayIndex];
				
				this.birthday = year + "-" + (month < 10 ? '0' + month : month) + "-" + (day < 10 ? '0' + day : day);
				this.currentUserInfo.birthday = this.birthday;
				
				this.updateBirthday(this.birthday);
			},
			
			goToEditMyFace() {
				uni.navigateTo({
					url: "../me/userInfo/myFace",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToEditMyNickname() {
				uni.navigateTo({
					url: "../me/userInfo/myNickname",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToEditMyWechatNumber() {
				uni.navigateTo({
					url: "../me/userInfo/myWechatNumber",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToMyQrCode() {
				uni.navigateTo({
					url: "../me/userInfo/myQrCode",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToPaiyipai() {
				uni.navigateTo({
					url: "../me/userInfo/paiyipai",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
			goToEditMyName() {
				uni.navigateTo({
					url: "../userInfo/myName",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			goToEditMySex() {
				uni.navigateTo({
					url: "../me/userInfo/mySex",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
			goToEditMySign() {
				uni.navigateTo({
					url: "../me/userInfo/mySign",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
			goToMyAddress() {
				uni.navigateTo({
					url: "../me/userInfo/chooseProvince",
					animationType: "slide-in-bottom",
					success() {
					}
				});
			},
			
		}
	}
</script>

<style>
	@import url("myPage.css");
</style>
