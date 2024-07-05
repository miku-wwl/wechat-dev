<style>
.page {
	position: absolute;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	background-color: #ffffff;
}

/* .line {
	height: 1rpx;
	background-color: #dae0ff;
	width: 750rpx;
} */

.place-box {
	background-color: #c7cee0;
}
.place-box-touched {
	background-color: #f9f6f6;
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
			v-for="(item, index) in provinceList"
			:class="{active : index == activeIndex}"
			:key="index"
			@click="chooseLocation(item.province_id, item.province_name)" 
			@touchstart="touchstartLocation(index)"
			@touchend="touchendLocation()"
			style="padding-left: 20rpx;padding-right: 20rpx;height: 88rpx;display: flex;flex-direction: row;justify-content: space-between;border-bottom: 0.2px solid #ebebeb;">
			
			<text style="align-self: center;font-size: 16px;">{{item.province_name}}</text>

			<view class="" style="display: flex;flex-direction: row;justify-content: flex-start;align-self: center;">
				<image
					class="right-arrow" 
					style="align-self: center;" 
					src="../../../static/icons/right-arrow-gray.png" ></image>
			</view>
			
		</view>
		
	</view>
</template>

<script>
	export default {
		components: {
		},
		data() {
			return {
				placeTouched: false,
				provinceList: [],
				activeIndex: -1
			}
		},
		onLoad() {
			var provinceList = getApp().globalData.provinceList;
			this.provinceList = provinceList;
		},
		onNavigationBarButtonTap() {
			uni.removeStorageSync("chosenLocationArea");
			uni.setStorageSync("needRefresh", "yes");
			uni.navigateBack({
				delta: 1,
				animationType: "slide-out-bottom"
			})
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
			chooseLocation(provinceId, provinceName) {
				uni.setStorageSync("myLocationProvince", provinceName);
				
				
				uni.navigateTo({
					url: "chooseCity?provinceId=" + provinceId + "&provinceName=" + provinceName,
					animationType: "slide-in-bottom",
				})
			}
		}
	}
</script>

