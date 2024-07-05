<script>
	import provinceList from './json/area_province.js';
	import cityList from './json/area_city.js';
	import districtList from './json/area_district.js';
	export default {
		globalData: {
			serverUrl: "http://127.0.0.1:1000",		// 开发环境，不能使用localhost
			// serverUrl: "http://localhost",					// 错误示范！！！
			// serverUrl: "http://192.168.1.6",					// 生产环境
			
			chatServerUrl: "ws://10.0.0.3:875/ws",
			
			minNode: {},
			
			provinceList: provinceList,
			cityList: cityList,
			districtList: districtList,
			
			env: "miku",
			// env: "weilai",
			
			// 整个聊天的websocket对象
			CHAT: null,
			// 用于标记当前的聊天服务有没有连接上
			chatSocketOpen: false,
		},
		data() {
			return {
			}
		},
		onLaunch: function() {
			console.log('App Launch')
			
			// this.getWSServerLink();
			
			// this.clearUserInfo();
			
			// 程序启动的时候，连接聊天服务器
			// setTimeout(()=>{
				this.doConnect(false);
			// }, 1000)
				
			// 仅竖屏
			// #ifdef APP-PLUS
			plus.screen.lockOrientation("portrait-primary")
			// #endif
						
			// this.showTabBarRedDot(2);
			// this.hideTabBarRedDot(2);
			
			// this.setTabBarRedNumber(0, 2);
			// this.removeTabBarBadge(0);
		},
		onShow: function() {
			console.log('App Show:' + this.getAppEnv());
			
			// ws重连
			// if (!me.globalData.chatSocketOpen) {
			// 	me.globalData.chatSocketOpen = true;
			// }
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			
			getWSServerLink() {
				var me = this;
				var serverUrl = me.globalData.serverUrl;
				uni.request({
					method: "POST",
					url: serverUrl + "/chat/getNettyOnlineInfo",
					success(result) {
						console.log(result);
						if (result.data.status == 200) {
							var minNode = result.data.data;
							console.log(minNode);
							me.globalData.chatServerUrl = "ws://" + minNode.ip + ":" + minNode.port + "/ws";
							me.globalData.minNode = minNode;
						}
					}
				})
			},
			
			/**
			 * @param {Object} isReConect 是否重连连接，true-是；false-否
			 */
			doConnect(isFirst) {
				if (isFirst) {
					uni.showToast({
						icon: "loading",
						title: "断线重连中...",
						duration: 2000
					});
				}
				
				// console.log("++++++++++++++++++++");
				// console.log(this.globalData.chatServerUrl);
				// console.log("++++++++++++++++++++");
				
				// 程序启动的时候，连接聊天服务器
				var me = this;
				if (me.getUserInfoSession() != null && me.getUserInfoSession() != "" && me.getUserInfoSession() != undefined) {
					me.globalData.CHAT = uni.connectSocket({
						url: me.globalData.chatServerUrl,
						complete: ()=> {}
					});
					
					// 当客户端和聊天服务连接，表示ws连接打开，则会被监听到
					me.globalData.CHAT.onOpen(function(){
						me.globalData.chatSocketOpen = true;
						console.log("ws连接已打开，socketOpen = " + me.globalData.chatSocketOpen);
						
						var chatMsg = {
							senderId: me.getUserInfoSession().id,
							msgType: 0
						} 
						var dataContent = { 
							chatMsg: chatMsg, 
							// serverNode: me.globalData.minNode
						}
						var msgPending = JSON.stringify(dataContent);
						
						// 第一次初始化，发送的初始化消息
						me.globalData.CHAT.send({
							data: msgPending
						}); 
					});  
					me.globalData.CHAT.onClose(function(){
						me.globalData.chatSocketOpen = false;
						console.log("ws连接已关闭，socketOpen = " + me.globalData.chatSocketOpen);
					}); 
					 
					me.globalData.CHAT.onMessage(function(res){
						console.log('App.vue 收到服务器内容：' + res.data);
						
						me.dealReceiveLastestMsg(JSON.parse(res.data));
						 
						// uni.$emit('receiveMsgInMsgVue', res.data);
						// uni.$emit('receiveMsgInMsgListVue', res.data);
					});
					 
					// 连接失败
					me.globalData.CHAT.onError(function(){
						me.globalData.chatSocketOpen = false;
						console.log('WebSocket连接打开失败，请检查！');
					});
				}
			},
			
			// 处理收到的消息
			dealReceiveLastestMsg(msgJSON) {
				console.log(msgJSON);
				var chatMsg = msgJSON.chatMsg;
				var chatTime = msgJSON.chatTime;
				var senderId = chatMsg.senderId;
				
				var receiverType = chatMsg.receiverType;
				console.log('chatMsg.receiverType = ' + receiverType);
				// if (receiverType != 2) {
				// 	return;
				// }
				
				var me = this;
				var userId = me.getUserInfoSession().id;
				var userToken = me.getUserSessionToken();
				var serverUrl = me.globalData.serverUrl;
				uni.request({
					method: "POST",
					header: {
						headerUserId: userId,
						headerUserToken: userToken
					},
					url: serverUrl + "/userinfo/get?userId=" + senderId,
					success(result) {
						// console.log(result);
						if (result.data.status == 200) {
							var currentSourceUserInfo = result.data.data;
							me.currentSourceUserInfo = currentSourceUserInfo;
							var msgShow = chatMsg.msg;
							if (chatMsg.msgType == 2) {
								msgShow = "[图片]"
							} else if (chatMsg.msgType == 4) {
								msgShow = "[视频]"
							} else if (chatMsg.msgType == 3) {
								msgShow = "[语音]"
							} 
							me.saveLastestMsgToLocal(senderId, currentSourceUserInfo, msgShow, chatTime, msgJSON);
						}
					}
				})
			},
			
			// 记录每个人的最后一条消息，记录在本地，用于显示聊天列表
			saveLastestMsgToLocal(sourceUserId, sourceUser, msgContent, chatTime, msgJSON) {
				
				var lastMsg = {
					sourceUserId: sourceUserId,		// 源头用户，聊天对象
					name: sourceUser.nickname,
					face: sourceUser.face,
					msgContent: msgContent,
					chatTime: chatTime,
					unReadCounts: 0,
					communicationType: 1, 	// 1:单聊，2:群聊
				}
				console.log(lastMsg);
				// return;
				// 先获得本地存储与候选人聊天的list，如果没有则新创建
				var lastestUserChatList = uni.getStorageSync("lastestUserChatList");
				if (lastestUserChatList == null || lastestUserChatList == undefined || lastestUserChatList == "") {
					lastestUserChatList = [];
				}
				
				// 循环判断，如果存在，则剔除，放入最新的
				var dealMsg = false;
				for ( var i = 0 ; i < lastestUserChatList.length ; i ++) {
					var tmp = lastestUserChatList[i];
					if (tmp.sourceUserId == lastMsg.sourceUserId) {
						lastestUserChatList.splice(i, 1, lastMsg);
						dealMsg = true;
						break;
					}
				}
				if (!dealMsg) {
					lastestUserChatList.unshift(lastMsg);
				}
				uni.setStorageSync("lastestUserChatList", lastestUserChatList);

				// 显示聊天列表
				uni.$emit('reRenderReceiveMsgInMsgVue', "domeafavor");
				// uni.$emit('receiveMsgInMsgVue', res.data);	// 用户处理自己的消息，目前用不到，目的是同步给消息的发送方的多设备里
				
				uni.$emit('receiveMsgInMsgListVue', msgJSON);
			},
			
			sendSocketMessage(msg) {
				if (this.globalData.chatSocketOpen) {
					uni.sendSocketMessage({
						data: msg
					});
				} else {
					uni.showToast({
						icon: "none",
						title: "您已断开聊天服务器的连接"
					})
				}
			},
			
			getAppEnv() {
				return this.globalData.env;
			},
			
			removeTabBarBadge(index, number) {
				uni.removeTabBarBadge({
				  index: index,
				})	
			},
			setTabBarRedNumber(index, number) {
				uni.setTabBarBadge({
				  index: index,
				  text: number+""
				})	
			},
			showTabBarRedDot(index) {
				uni.showTabBarRedDot({
					index: index
				});
			},
			hideTabBarRedDot(index) {
				uni.hideTabBarRedDot({
					index: index
				});
			},

			goBack(pages) {
				if (pages == null || pages == "" || pages == undefined) {
					pages = 1;
				}
				uni.navigateBack({
					delta: pages
				})
			},

			closeWSConnect() {
				this.globalData.CHAT.close();
			},
			
			
			// 根据生日计算年龄
			getAge(birthday){     
				if (birthday == null || birthday == undefined || birthday =='') {
					return 0;
				}
			    var returnAge;  
			    var strBirthdayArr = birthday.split("-");  
			    var birthYear = strBirthdayArr[0];  
			    var birthMonth = strBirthdayArr[1];  
			    var birthDay = strBirthdayArr[2];  
			      
			    var d = new Date();  
			    var nowYear = d.getFullYear();  
			    var nowMonth = d.getMonth() + 1;  
			    var nowDay = d.getDate();  
			      
			    if(nowYear == birthYear){  
			        returnAge = 0;//同年 则为0岁  
			    }  
			    else{  
			        var ageDiff = nowYear - birthYear ; //年之差  
			        if(ageDiff > 0){  
			            if(nowMonth == birthMonth) {  
			                var dayDiff = nowDay - birthDay;//日之差  
			                if(dayDiff < 0)  
			                {  
			                    returnAge = ageDiff - 1;  
			                }  
			                else  
			                {  
			                    returnAge = ageDiff ;  
			                }  
			            }  
			            else  
			            {  
			                var monthDiff = nowMonth - birthMonth;//月之差  
			                if(monthDiff < 0)  
			                {  
			                    returnAge = ageDiff - 1;  
			                }  
			                else  
			                {  
			                    returnAge = ageDiff ;  
			                }  
			            }  
			        }  
			        else  
			        {  
			            returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天  
			        }  
			    }  
			    return returnAge;//返回周岁年龄  
			},
			
			// 判断是否为空
			isStrEmpty (str) {
				if (str == null || str == undefined || str == "") {
					return true;
				} else {
					return false;
				}
				
			},
			// 判断用户是否登录
			userIsLogin() {
				var userToken = this.getUserSessionToken();
				// var userInfo = this.getUserInfoSession();
				// console.log("userInfo=" + userInfo);
				console.log("userToken=" + userToken);
				 // && !this.isStrEmpty(userInfo)
				if (!this.isStrEmpty(userToken)) {
					return true;
				} else {
					return false;
				}
			},
			
			// 用户登录以后的session存取，token和userInfo
			setUserSessionToken(token){
				uni.setStorageSync("userToken", token);
			},
			getUserSessionToken() {
				var token = uni.getStorageSync("userToken");
				if (this.isStrEmpty(token)) {
					return "";
				}
				return token;
			},
			setUserInfoSession(user){
				uni.setStorageSync("userInfo", JSON.stringify(user));
			},
			getUserInfoSession() {
				var user = uni.getStorageSync("userInfo");
				if (this.isStrEmpty(user)) {
					return null;
				}
				return JSON.parse(user);
			},
			clearUserInfo() {
				uni.removeStorageSync("userInfo");
				uni.removeStorageSync("userToken");
			},
			clearUserToken() {
				uni.removeStorageSync("userToken");
			},
			
			graceNumber(number) {
				if (number == 0) {
					return "0";
				} else if (number > 999 && number <= 9999) {
					return (number/1000).toFixed(1) + 'k';
				} else if (number > 9999 && number <= 99999) {
					return (number/10000).toFixed(1) + 'w';
				} else if (number > 99999) {
					return "10w+";
				} 
				return number;
			},
			
			getDateBeforeNow(stringTime) {
				// console.log(stringTime);
				stringTime = new Date(stringTime.replace(/-/g,'/'))
				
				var minute = 1000 * 60;
				var hour = minute * 60;
				var day = hour * 24;
				var week = day * 7;
				var month = day * 30;
					
				var time1 = new Date().getTime(); //当前的时间戳
				// console.log(time1);
				// console.log(new Date(stringTime));
				var time2 = Date.parse(new Date(stringTime)); //指定时间的时间戳
				// console.log(time2);
				var time = time1 - time2;
					
				var result = null;
				if(time < 0) {
					// alert("设置的时间不能早于当前时间！");
					result = stringTime;
				}else if(time/month >= 1){
					result = parseInt(time/month) + "月前";
				}else if(time/week >= 1){
					result = parseInt(time/week) + "周前";
				}else if(time/day >= 1){
					result = parseInt(time/day) + "天前";
				}else if(time/hour >= 1){
					result = parseInt(time/hour) + "小时前";
				}else if(time/minute >= 1){
					result = parseInt(time/minute) + "分钟前";
				}else {
					result = "刚刚";
				}
				// console.log(result);
				return result;
			},
			
			dateFormat(fmt, date) {
			    let ret;
			    const opt = {
			        "Y+": date.getFullYear().toString(),        // 年
			        "m+": (date.getMonth() + 1).toString(),     // 月
			        "d+": date.getDate().toString(),            // 日
			        "H+": date.getHours().toString(),           // 时
			        "M+": date.getMinutes().toString(),         // 分
			        "S+": date.getSeconds().toString()          // 秒
			        // 有其他格式化字符需求可以继续添加，必须转化成字符串
			    };
			    for (let k in opt) {
			        ret = new RegExp("(" + k + ")").exec(fmt);
			        if (ret) {
			            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
			        };
			    };
			    return fmt;
			},
			
		}
	}
</script>

<style>
/*每个页面公共css */

/* .uni-badge--error {
	background-color: #f43530;
} */



.line-wrapper {
	display: flex;
	flex-direction: row;
	justify-content: center;
}
.line {
	background-color: #f6f6f6;
	/* background-color: red; */
	height: 1px;
	width: 100%;
	align-self: center;
}

.spliter {
	border-left: #dcdada solid 1px;
	margin: 0 10px;
	height: 16px;

	align-self: center;
}

.status_bar {
	height: var(--status-bar-height);
	width: 100%;
	background-color: #ecedec;
	
	z-index: 99;
	
	position: fixed;
	top: 0px;
}

.fix-navigation-bar {
	position: fixed;
}

.navigation-bar {
	width: 100%;
	height: 44px;
	
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	
	position: fixed;
	/* top: var(--status-bar-height); */
	
	padding-top: var(--status-bar-height);
	
	
	/* padding: 0px 10px; */
	
	background-color: #ecedec;
	z-index: 99;
	
	
	/* #ifdef H5 */
	/* padding-top: 0; */
	/* #endif */
	/* #ifndef H5 */
	/* padding-top: 44px; */
	/* #endif */
	
	/* #ifdef APP-IOS */
	/* padding-top: env(safe-area-inset-top); */
	/* #endif */
}


.nav-left {
	align-self: center;
}

.back-icon {
	width: 22px;
	height: 22px;
	margin-left: 20px;
}

.nav-middle {
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-self: center;
}

.page-title {
	color: #000000;
	align-self: center;
	font-size: 18px;
	font-weight: 400;
}

.page-info {
	color: #6d6d6d;
	align-self: center;
	font-size: 12px;
	font-weight: 400;
}

.nav-right {
	align-self: center;
}

.white-icon {
	width: 22px;
	height: 22px;
	margin-right: 20px;
}

.item-line-notouch {
	background-color: #fefffe;
}

.item-line-touched {
	background-color: #e5e5e5;
}


/* uni-radio .uni-radio-input.uni-radio-input-checked{
	background-color: #31B9B3!important;
	border-color: #31B9B3!important;
	background-clip: content-box!important;
	padding: 6rpx!important;
	box-sizing: border-box;
} */

/* uni-radio .uni-radio-input.uni-radio-input-checked::before{
	display: none!important;
} */

.uni-radio-input {
	width: 26px;
	height: 26px;
}
uni-radio:not([disabled]) .uni-radio-input:hover {
	border-color: #31B9B3 !important;
}
uni-radio .uni-radio-input.uni-radio-input-checked {
	border-color: #31B9B3 !important;
	background: #31B9B3 !important;
}
uni-radio .uni-radio-input.uni-radio-input-checked::before {
	background: #31B9B3 !important;
	border-color: #31B9B3 !important;
}

.radio-group {
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
}

.radio-item {
	display: flex;
	flex-direction: row;
	justify-content: flex-start;
}

.radio-words {
	align-self: center;
	/* margin-left: 6px; */
	font-size: 17px;
	font-weight: 400;
}

uni-slider .uni-slider-handle-wrapper{
	height: 3px;
}

</style>
