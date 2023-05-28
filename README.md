# WeatherBroadcast
一个Android天气预报APP **(详细介绍请移步我的博客：https://www.cocobronie.cn/)**

# 一、主要功能

## 1、**主视图**和**细节视图**

在手机中包含**主视图**和**细节视图**，**主视图**显示连续多天的天气预报简讯，如图表 1所示，用户在主视图中点击某一天的天气简讯以后，跳出**细节视图**，显示用户选定当天天气的详细信息。

![image-20230528110917512](https://github.com/Cocobronie/WeatherBroadcast/assets/98938169/9088710c-48c1-4b26-9d60-2ec17893bfb5)


## 2、 支持平板显示

在**平板**中使用**Master-detail**视图，当用户点击某一天的天气预览以后，直接在界面右边显示当天天气的详细信息，如图表 3所示。

![image-20230528112110529](https://github.com/Cocobronie/WeatherBroadcast/assets/98938169/945a79a1-b920-4be9-9d39-2619c41a70f6)


## 3、主视图菜单栏功能实现

- 主视图中包含**Map Location**和**setting**选项，通过**”Map location”** 选项，可以调用手机中安装的地图应用显示当前天气预报所对应的位置，如图表 4所示，用户可以通过**setting**选项可以修改天气预报的位置，温度的单位（华氏度、摄氏度）以及是否开启天气通知，如图表 5所示。如果setting选项中的**天气通知选项**打开，会**定期发送通知消息**，其中显示当天的天气简讯，如图表 6所示。

 ![image-20230528112239798](https://github.com/Cocobronie/WeatherBroadcast/assets/98938169/dcd1f10e-6bf2-4365-8eb4-44d888600e5c)


## 4、细节视图菜单栏功能实现

细节视图菜单中包含**分享**和**setting选项**，用户可以通过分享选项通过其他应用（邮件、短信等）将天气详细信息分享给别人。如图表 2所示。

![image-20230528112305880](https://github.com/Cocobronie/WeatherBroadcast/assets/98938169/13f550d5-0596-46ad-8177-7feb63736ebb)


## 5、SQLite对天气预报数据进行持久化保存

利用SQLite对天气预报数据进行持久化保存，如果网络不可用的情况下，从SQLite中提取天气预报数据。

## 6、调用Web API获取天气预报数据

heweather：[https://www.heweather.com/documents/api/s6/weather-forecast](https://www.heweather.com/documents/api/s6/weather-forecast)

# 二、开发细节

见我的Blog：https://www.cocobronie.cn/
