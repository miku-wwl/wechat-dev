<style>
.page {
	position: absolute;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	background-color: #ffffff;
}

.place-box {
	background-color: #4a4c52;
}
.place-box-touched {
	background-color: #6d6b6b;
}

.right-arrow {
	width: 32rpx;
	height: 32rpx;
	margin-left: 20rpx;
}

.active {
	background-color: #e5e5e5;
}
</style>
<template>
	<view class="page">
			
		<view 
			v-for="(item, index) in cityList"
			:class="{active : index == activeIndex}"
			:key="index"
			@click="chooseLocation(item.city_id, showDistrict ? item.district_name : item.city_name)" 
			@touchstart="touchstartLocation(index)"
			@touchend="touchendLocation()"
			style="padding-left: 30rpx;padding-right: 30rpx;width: 750rpx;height: 88rpx;display: flex;flex-direction: row;justify-content: space-between;border-bottom: 0.2px solid #ebebeb;">
			
			<text style="align-self: center;font-size: 16px;">{{showDistrict ? item.district_name : item.city_name}}</text>
			
				<image
					class="right-arrow" 
					style="align-self: center;" 
					src="../../../static/icons/right-arrow-gray.png" ></image>
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
				placeTouched: false,
				cityList: [],
				activeIndex: -1,
				showDistrict: false,
				
				provinceId: "",
				provinceName: "",
			}
		},
		onLoad(params) {
			var provinceId = params.provinceId;
			var provinceName = params.provinceName;
			this.provinceId = provinceId;
			this.provinceName = provinceName;
			
			var cityList = getApp().globalData.cityList;
			var districtList = getApp().globalData.districtList;
			
			this.showDistrict = false;
			var realCityList = [];
			for (var i = 0; i < cityList.length; i ++) {
				var tempProvinceId = cityList[i].province_id;
				if (tempProvinceId == provinceId) {
					realCityList.push(cityList[i]);
				}
			}
			
			// 如果是北京1、天津2、上海9、重庆22，他们需要从区中获得，作为给用户展示并且可以更具体的选择
			if(provinceId == 1 || provinceId == 2 || provinceId == 9 || provinceId == 22) {
					var thatCityId = realCityList[0].city_id;
					realCityList = [];
					for (var i = 0; i < districtList.length; i ++) {
						var city_id = districtList[i].city_id;
						if (city_id == thatCityId) {
							var realCity = districtList[i];
							// var districtName = realCity.district_name.split("区")[0];
							// realCity.district_name = districtName;
							// districtName = realCity.district_name.split("县")[0];
							// realCity.district_name = districtName;
							// districtName = realCity.district_name.split("市")[0];
							// realCity.district_name = districtName;
							realCityList.push(realCity);
						}
					}
					this.showDistrict = true;
			}
			
			this.cityList = realCityList;
		},
		methods: {
			updateBirthday() {
			},
			touchstartLocation(index) {
				this.activeIndex = index;
			},
			touchendLocation() {
				this.activeIndex = -1;
			},
			chooseLocation2(cityId, areaName) {
				uni.setStorageSync("chosenLocationArea", areaName);
				uni.setStorageSync("needRefresh", "yes");
				
				uni.navigateBack({
					delta: 2,
					animationType: "slide-out-bottom"
				})
			},
			
			chooseLocation(cityId, areaName) {
				var me = this;
				var userId = getApp().getUserInfoSession().id;
				
				var provinceId = this.provinceId;
				var provinceName = this.provinceName;
				console.log("provinceName = " + provinceName);
				
				var pendingUserInfo = {
					"userId": userId,
					"province": provinceName,
					"city": areaName,
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
								success() {
									uni.navigateBack({
										delta: 2,
									})
								}
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
			
		}
	}
</script>

