<template>
	<view class="page">
		
		<view class="block-one">
			<view class="block-box">
				<view
					:class="{'item-line-notouch':!chooseBoy, 'item-line-touched': chooseBoy, 'block-line': true}"
					@touchstart="chooseBoy=true"
					@touchend="chooseBoy=false"
					@click="chooseSex(1)">
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">男</text>
						<image src="../../../static/icons/wechat/right-green.png" v-if="sex == 1" class="right-green-icon"></image>
					</view>
				</view>
				
				<view
					:class="{'item-line-notouch':!chooseGirl, 'item-line-touched': chooseGirl, 'block-line': true}"
					@touchstart="chooseGirl=true"
					@touchend="chooseGirl=false"
					@click="chooseSex(0)">
					<view class="right-part" style="border-bottom: 0.5px solid #ebebeb;line-height: 55px;">
						<text class="left-part-words">女</text>
						<image src="../../../static/icons/wechat/right-green.png" v-if="sex == 0" class="right-green-icon"></image>
					</view>
				</view>
				 
				<view
					:class="{'item-line-notouch':!chooseSecret, 'item-line-touched': chooseSecret, 'block-line': true}"
					@touchstart="chooseSecret=true"
					@touchend="chooseSecret=false"
					@click="chooseSex(2)">
					<view class="right-part" style="line-height: 55px;">
						<text class="left-part-words">保密</text>
						<image src="../../../static/icons/wechat/right-green.png" v-if="sex == 2" class="right-green-icon"></image>
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
				chooseBoy: false,
				chooseGirl: false,
				chooseSecret: false,
				sex: 0
			}
		},
		onNavigationBarButtonTap() {
			// console.log(this.sex);
			var me = this;
			var userId = getApp().getUserInfoSession().id;
			var serverUrl = app.globalData.serverUrl;
			uni.request({
				method: "POST",
				url: serverUrl + "/userinfo/modify",
				header: {
					headerUserId: userId,
					headerUserToken: app.getUserSessionToken()
				},
				data: {
					"userId": userId,
					"sex": me.sex
				},
				success(result) {
					var status = result.data.status;
					if (status == 200) {
						var userInfo = result.data.data;
						app.setUserInfoSession(userInfo);
						app.setUserSessionToken(userInfo.userToken);
						
						uni.showToast({
							title: "修改成功~",
							duration: 1500
						})
						
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
		onShow() {
			// 获得用户信息
			var currentUserInfo = getApp().getUserInfoSession();
			this.sex = currentUserInfo.sex;
		},
		methods: {
			chooseSex(sex) {
				this.sex = sex;
			}
		}
	}
</script>

<style>
	@import url("mySex.css");
</style>
